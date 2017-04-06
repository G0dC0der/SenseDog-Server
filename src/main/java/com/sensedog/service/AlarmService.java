package com.sensedog.service;

import com.sensedog.detection.DetectionMessage;
import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import com.sensedog.repository.AlarmDeviceRepository;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.SubscriberRepository;
import com.sensedog.repository.entry.AlarmDevice;
import com.sensedog.repository.entry.Detection;
import com.sensedog.repository.entry.PinCode;
import com.sensedog.repository.entry.Service;
import com.sensedog.repository.entry.Subscriber;
import com.sensedog.security.Capability;
import com.sensedog.security.Cipher;
import com.sensedog.security.SecurityManager;
import com.sensedog.security.Token;
import com.sensedog.system.SystemStatus;
import com.sensedog.transmit.CloudClient;
import com.sensedog.transmit.MailClient;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmService {

    private final ServiceRepository serviceRepository;
    private final DetectionRepository detectionRepository;
    private final AlarmDeviceRepository alarmDeviceRepository;
    private final SubscriberRepository subscriberRepository;
    private final SecurityManager securityManager;
    private final MailClient mailClient;
    private final CloudClient cloudClient;

    @Inject
    public AlarmService(final ServiceRepository serviceRepository,
                        final DetectionRepository detectionRepository,
                        final AlarmDeviceRepository alarmDeviceRepository,
                        final SubscriberRepository subscriberRepository,
                        final SecurityManager securityManager,
                        final MailClient mailClient,
                        final CloudClient cloudClient) {
        this.serviceRepository = serviceRepository;
        this.detectionRepository = detectionRepository;
        this.alarmDeviceRepository = alarmDeviceRepository;
        this.subscriberRepository = subscriberRepository;
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
        service.setStatus(SystemStatus.ACTIVE);

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

    public Severity detect(Token.Alarm token,
                       DetectionType detectionType,
                       String value) {
        Service service = securityManager.authenticate(token);
        securityManager.stateVerify(service);

        if (service.getMasterUser() == null) {
            throw new BadRequestException("Can not broadcast a detection with master user absence.");
        }

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
                .filter(subscriber -> securityManager.receiveCapable(subscriber, detection))
                .collect(Collectors.toList());

        warningReceivers.stream()
                .filter(subscriber -> securityManager.hasCapability(subscriber, Capability.MAIL_READER))
                .map(subscriber -> DetectionMessage.of(subscriber, service.getMasterUser(), detection))
                .forEach(mailClient::mail);

        if (severity.isAboveOrEqual(Severity.WARNING)) {
            //Send text
            //Push message
        }
        if (severity.isAboveOrEqual(Severity.CRITICAL)) {
            //Call text
        }

        //Always push to master user

        subscriberRepository.updateLastNotifications(warningReceivers, detection.getDetectionDate());

        return severity;
    }

    public void start(Token token) {
        Service  service = securityManager.authenticate(token);
        service.setStatus(SystemStatus.ACTIVE);
        PinCode pinCode = new PinCode();
        pinCode.setCreationDate(ZonedDateTime.now());
        pinCode.setPinCode(Cipher.pinCode());
        pinCode.setService(service);
        service.setPinCode(pinCode);
        serviceRepository.update(service);
    }

    public void stop(Token token) {
        Service  service = securityManager.authenticate(token);
        service.setStatus(SystemStatus.STOPPED);
        service.setMasterAuthToken(null);
        service.setMasterUser(null);
        service.setPinCode(null);
        serviceRepository.update(service);
    }
}
