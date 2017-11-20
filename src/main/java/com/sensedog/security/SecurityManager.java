package com.sensedog.security;

import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.model.SqlAlarmDevice;
import com.sensedog.repository.model.SqlDetection;
import com.sensedog.repository.model.SqlMaster;
import com.sensedog.repository.model.SqlPincode;
import com.sensedog.repository.model.SqlService;
import com.sensedog.repository.model.SqlSubscriber;
import com.sensedog.system.SystemStatus;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class SecurityManager {

    public interface Settings {
        long getPinCodeLife();
    }

    public static final int MINIMUM_NOTIFICATION_REGULARITY = 25000;

    private final Settings settings;
    private final DetectionRepository detectionRepository;
    private final ServiceRepository serviceRepository;

    @Inject
    public SecurityManager(final Settings settings,
                           final DetectionRepository detectionRepository,
                           final ServiceRepository serviceRepository) {
        this.settings = settings;
        this.detectionRepository = detectionRepository;
        this.serviceRepository = serviceRepository;
    }

    public boolean hasExpired(final SqlPincode pinCode) {
        final Duration between = Duration.between(pinCode.getCreationDate(), ZonedDateTime.now());
        return between.toMillis() > settings.getPinCodeLife();
    }

    public boolean isLost(final SqlAlarmDevice sqlAlarmDevice) {
        final Duration between = Duration.between(sqlAlarmDevice.getLastSeen(), ZonedDateTime.now());
        return between.toMinutes() > 30;
    }

    public Severity determineSeverity(final SqlService service, final DetectionType newType) {
        if (newType == DetectionType.COMPASS) {
            return Severity.CRITICAL;
        }
        Severity severity = Severity.SUSPICIOUS;
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime halfMinuteAgo = now.minusSeconds(31);
        final ZonedDateTime aMinuteAgo = now.minusSeconds(61);
        final List<SqlDetection> detections = detectionRepository.getBetween(service.getId(), aMinuteAgo, now);

        if (!detections.isEmpty()) {
            detections.sort(Comparator.comparing(SqlDetection::getDetectionDate));
            for (final SqlDetection detection : detections) {
                final ZonedDateTime detectionDate = detection.getDetectionDate();
                if (detectionDate.isAfter(halfMinuteAgo) && now.isAfter(detectionDate)) {
                    severity = Severity.WARNING;
                }
                if (severity == Severity.WARNING && aMinuteAgo.isBefore(detectionDate) && detectionDate.isBefore(halfMinuteAgo)) {
                    severity = Severity.CRITICAL;
                    break;
                }
            }
        }
        return severity;
    }

    public boolean receiveCapable(final SqlSubscriber subscriber, final SqlDetection detection) {
        if (detection.getDetectionType() != subscriber.getLastDetectionType()) {
            return true;
        }
        if (detection.getSeverity().isAbove(subscriber.getMinimumSeverity())) {
            return true;
        }

        final Duration diff = Duration.between(subscriber.getLastNotificationDate(), ZonedDateTime.now());
        return diff.toMillis() > subscriber.getNotifyRegularity();
    }

    public boolean hasCapability(final SqlSubscriber subscriber, final Capability capability) {
        return subscriber.getSubscriberCapabilities()
                .stream()
                .anyMatch(subscriberCapability -> subscriberCapability.getCapability() == capability);
    }

    public SqlService authenticate(final Token token) {
        final SqlService service;
        if (token instanceof Token.Alarm) {
            service = serviceRepository.findByAlarmDeviceToken(token.getToken());
        } else if (token instanceof  Token.Master) {
            service = serviceRepository.findByMasterToken(token.getToken());
        } else {
            throw new RuntimeException("Unknown token: " + token.getClass());
        }

        if (service == null) {
            throw new AuthenticationFailedException("Invalid token.");
        }

        return service;
    }

    public SqlMaster requireMaster(final SqlService service) {
        if (service.getMasterUser() == null) {
            throw new StateViolationException("No master connected yet.");
        }
        return service.getMasterUser();
    }

    public void stateVerify(final SqlService service) {
        if (service.getStatus() != SystemStatus.ACTIVE) {
            throw new StateViolationException("State violation.");
        }
    }
}
