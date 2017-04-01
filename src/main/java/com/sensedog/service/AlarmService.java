package com.sensedog.service;

import com.sensedog.detection.DetectionMessage;
import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import com.sensedog.repository.AlarmDeviceRepository;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.entry.AlarmDevice;
import com.sensedog.repository.entry.Detection;
import com.sensedog.repository.entry.PinCode;
import com.sensedog.repository.entry.Service;
import com.sensedog.repository.entry.Subscriber;
import com.sensedog.security.Capability;
import com.sensedog.security.Cipher;
import com.sensedog.security.SecurityManager;
import com.sensedog.security.UnauthorizedException;
import com.sensedog.system.SystemStatus;
import com.sensedog.transmit.CloudClient;
import com.sensedog.transmit.MailClient;

import javax.inject.Inject;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmService {

    private final ServiceRepository serviceRepository;
    private final DetectionRepository detectionRepository;
    private final AlarmDeviceRepository alarmDeviceRepository;
    private final SecurityManager securityManager;
    private final MailClient mailClient;
    private final CloudClient cloudClient;

    @Inject
    public AlarmService(final ServiceRepository serviceRepository,
                        final DetectionRepository detectionRepository,
                        final AlarmDeviceRepository alarmDeviceRepository,
                        final SecurityManager securityManager,
                        final MailClient mailClient,
                        final CloudClient cloudClient) {
        this.serviceRepository = serviceRepository;
        this.detectionRepository = detectionRepository;
        this.alarmDeviceRepository = alarmDeviceRepository;
        this.securityManager = securityManager;
        this.mailClient = mailClient;
        this.cloudClient = cloudClient;
    }

    public String create(String cloudToken,
                         String deviceModel,
                         String osVersion,
                         String appVersion,
                         String carrier,
                         Float battery) {

        Service service = new Service();
        service.setCreationDate(ZonedDateTime.now());
        service.setStatus(SystemStatus.IN_MOTION);

        PinCode pinCode = new PinCode();
        pinCode.setPinCode(Cipher.pinCode());
        pinCode.setCreationDate(ZonedDateTime.now());
        pinCode.setService(service);

        AlarmDevice alarmDevice = new AlarmDevice();
        alarmDevice.setCloudToken(cloudToken);
        alarmDevice.setDeviceModel(deviceModel);
        alarmDevice.setOsVersion(osVersion);
        alarmDevice.setAppVersion(appVersion);
        alarmDevice.setCarrier(carrier);
        alarmDevice.setAuthToken(Cipher.authToken());
        alarmDevice.setBattery(battery);
        alarmDevice.setService(service);

        service.setPinCode(pinCode);
        service.setAlarmDevice(alarmDevice);

        serviceRepository.create(service);

        return alarmDevice.getAuthToken();
    }

    //TODO: This method should NOT see if PP is within the safe radius. That should the OP do before doing a request to the detection end point
    public void detect(String authToken, DetectionType detectionType, String value) {
        Service service = securityManager.authorize(authToken);
        service.setStatus(SystemStatus.IN_MOTION); //TODO: Denna sparas ej

        Severity severity = securityManager.determineSeverity(service, detectionType);

        Detection detection = new Detection();
        detection.setDetectionDate(ZonedDateTime.now());
        detection.setDetectionType(detectionType);
        detection.setValue(value);
        detection.setService(service);
        detection.setSeverity(severity);

        detectionRepository.save(detection);
        alarmDeviceRepository.updateLastSeen(service.getId(), ZonedDateTime.now());

        List<Subscriber> warningReceivers = service
                .getSubscribers()
                .stream()
                .filter(subscriber -> subscriber.getMinimumSeverity().isAboveOrEqual(severity))
                .filter(securityManager::receiveCapable)
                .collect(Collectors.toList());

        mailTo(service, detection, warningReceivers);

        if (severity.isAboveOrEqual(Severity.WARNING)) {
            //Send text
            //Push message
        }
        if (severity.isAboveOrEqual(Severity.CRITICAL)) {
            //Call text
        }

        //TODO: Don't forget to update "lastNotification"
    }

    private void mailTo(Service service, Detection detection, List<Subscriber> receivers) {
        receivers.stream()
                .filter(securityManager::mailCapable)
                .map(subscriber -> DetectionMessage.of(subscriber, service.getMasterUser(), detection))
                .forEach(mailClient::mail);
    }

    private void textTo() {

    }

    private void callTo() {

    }
}
