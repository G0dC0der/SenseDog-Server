package com.sensedog.security;

import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.entry.Detection;
import com.sensedog.repository.entry.MasterUser;
import com.sensedog.repository.entry.PinCode;
import com.sensedog.repository.entry.Service;
import com.sensedog.repository.entry.Subscriber;

import javax.inject.Inject;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

public class SecurityManager {

    public interface Settings {
        long getPinCodeLife();
    }

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
                if (aMinuteAgo.isBefore(detectionDate) && detectionDate.isBefore(halfMinuteAgo)) {
                    severity = Severity.CRITICAL;
                    break;
                }
            }
        }
        return severity;
    }

    public boolean receiveCapable(Subscriber subscriber) {
        Duration diff = Duration.between(subscriber.getLastNotification(), ZonedDateTime.now());
        return diff.toMillis() > subscriber.getNotifyRegularity();
    }

    public boolean mailCapable(Subscriber subscriber) {
        return subscriber.getSubscriberCapabilities()
                .stream()
                .anyMatch(subscriberCapability -> subscriberCapability.getCapability() == Capability.MAIL_READER);
    }

    public Service authorize(String authToken) {
        Service service = serviceRepository.getByAlarmDeviceToken(authToken);

        if (service == null) {
            throw new UnauthorizedException("Invalid token.");
        }
        return service;
    }
}
