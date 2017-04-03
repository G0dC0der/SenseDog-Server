package com.sensedog.security;

import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.entry.AlarmDevice;
import com.sensedog.repository.entry.Detection;
import com.sensedog.repository.entry.PinCode;
import com.sensedog.repository.entry.Service;
import com.sensedog.repository.entry.Subscriber;
import com.sensedog.system.SystemStatus;

import javax.inject.Inject;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

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

    public boolean hasExpired(PinCode pinCode) {
        Duration between = Duration.between(pinCode.getCreationDate(), ZonedDateTime.now());
        return between.toMillis() > settings.getPinCodeLife();
    }

    public boolean isLost(AlarmDevice alarmDevice) {
        Duration between = Duration.between(alarmDevice.getLastSeen(), ZonedDateTime.now());
        return between.toMinutes() > 30;
    }

    public Severity determineSeverity(Service service, DetectionType newType) {
        if (newType == DetectionType.COMPASS) {
            return Severity.CRITICAL;
        }
        Severity severity = Severity.SUSPICIOUS;
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime halfMinuteAgo = now.minusSeconds(31);
        ZonedDateTime aMinuteAgo = now.minusSeconds(61);
        List<Detection> detections = detectionRepository.getBetween(service.getId(), aMinuteAgo, now);

        if (!detections.isEmpty()) {
            detections.sort(Comparator.comparing(Detection::getDetectionDate));
            for (Detection detection : detections) {
                ZonedDateTime detectionDate = detection.getDetectionDate();
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

    public boolean receiveCapable(Subscriber subscriber, Detection detection) {
        if (detection.getDetectionType() != subscriber.getLastDetectionType()) {
            return true;
        }
        if (detection.getSeverity().isAbove(subscriber.getMinimumSeverity())) {
            return true;
        }

        Duration diff = Duration.between(subscriber.getLastNotificationDate(), ZonedDateTime.now());
        return diff.toMillis() > subscriber.getNotifyRegularity();
    }

    public boolean hasCapability(Subscriber subscriber, Capability capability) {
        return subscriber.getSubscriberCapabilities()
                .stream()
                .anyMatch(subscriberCapability -> subscriberCapability.getCapability() == capability);
    }

    public Service authenticate(Token token) {
        Service service;
        if (token instanceof Token.Alarm) {
            service = serviceRepository.getByAlarmDeviceToken(token.getToken());
        } else if (token instanceof  Token.Master) {
            service = serviceRepository.getByMasterToken(token.getToken());
        } else {
            throw new RuntimeException("Unknown token: " + token.getClass());
        }

        if (service == null) {
            throw new AuthenticationFailedException("Invalid token.");
        }

        return service;
    }

    public void stateVerify(Service service) {
        if (service.getStatus() != SystemStatus.ACTIVE) {
            throw new StateViolationException("Can not connect to a service in a stopped state.");
        }
    }
}
