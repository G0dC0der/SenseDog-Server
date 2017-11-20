package com.sensedog.service;

import com.sensedog.detection.DetectionMessage;
import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import com.sensedog.repository.AlarmDeviceRepository;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.SubscriberRepository;
import com.sensedog.repository.model.SqlAlarmDevice;
import com.sensedog.repository.model.SqlDetection;
import com.sensedog.repository.model.SqlMaster;
import com.sensedog.repository.model.SqlPincode;
import com.sensedog.repository.model.SqlService;
import com.sensedog.repository.model.SqlSubscriber;
import com.sensedog.security.AlertMessage;
import com.sensedog.security.Capability;
import com.sensedog.security.Cipher;
import com.sensedog.security.SecurityManager;
import com.sensedog.security.Token;
import com.sensedog.service.model.AlarmServiceInfo;
import com.sensedog.system.SystemStatus;
import com.sensedog.communication.cloud.CloudClient;
import com.sensedog.communication.cloud.CloudMessage;
import com.sensedog.communication.mail.MailClient;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

    public AlarmServiceInfo create(final String cloudToken,
                                   final String deviceModel,
                                   final String osVersion,
                                   final String appVersion,
                                   final String carrier,
                                   final String serviceName,
                                   final Float battery) {

        final SqlService service = new SqlService();
        service.setCreationDate(ZonedDateTime.now());
        service.setStatus(SystemStatus.STOPPED);
        service.setServiceName(serviceName);

        final SqlPincode pinCode = new SqlPincode();
        pinCode.setPinCode(Cipher.pinCode());
        pinCode.setCreationDate(ZonedDateTime.now());
        pinCode.setService(service);

        final SqlAlarmDevice sqlAlarmDevice = new SqlAlarmDevice();
        sqlAlarmDevice.setCloudToken(cloudToken);
        sqlAlarmDevice.setDeviceModel(deviceModel);
        sqlAlarmDevice.setOsVersion(osVersion);
        sqlAlarmDevice.setAppVersion(appVersion);
        sqlAlarmDevice.setCarrier(carrier);
        sqlAlarmDevice.setAuthToken(Cipher.authToken());
        sqlAlarmDevice.setBattery(battery);
        sqlAlarmDevice.setService(service);

        service.setPinCode(pinCode);
        service.setAlarmDevice(sqlAlarmDevice);

        serviceRepository.create(service);

        final AlarmServiceInfo alarmServiceInfo = new AlarmServiceInfo();
        alarmServiceInfo.setPinCode(pinCode.getPinCode());
        alarmServiceInfo.setAlarmAuthToken(sqlAlarmDevice.getAuthToken());

        return alarmServiceInfo;
    }

    public Severity detect(final Token.Alarm token,
                           final DetectionType detectionType,
                           final String value) {
        final SqlService service = securityManager.authenticate(token);
        final SqlMaster masterUser = securityManager.requireMaster(service);
        securityManager.stateVerify(service);

        final Severity severity = securityManager.determineSeverity(service, detectionType);

        final SqlDetection detection = new SqlDetection();
        detection.setDetectionDate(ZonedDateTime.now());
        detection.setDetectionType(detectionType);
        detection.setValue(value);
        detection.setService(service);
        detection.setSeverity(severity);

        detectionRepository.save(detection);
        alarmDeviceRepository.updateLastSeen(service.getId(), ZonedDateTime.now());

        final List<SqlSubscriber> warningReceivers = service
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
        }
        if (severity.isAboveOrEqual(Severity.CRITICAL)) {
            //Call text
        }

        final AlertMessage alertMessage = new AlertMessage();
        alertMessage.setMessage("Your alarm device have detected suspicious movement.");
        cloudClient.send(CloudMessage.from(alertMessage, masterUser.getCloudToken()));

        if (!warningReceivers.isEmpty()) {
            subscriberRepository.updateLastNotifications(warningReceivers, detection.getDetectionDate());
        }

        return severity;
    }

    public void start(final Token token) {
        final SqlService service = securityManager.authenticate(token);
        service.setStatus(service.getMasterUser() != null ? SystemStatus.ACTIVE : SystemStatus.STOPPED);
        serviceRepository.update(service);
    }

    public void stop(final Token token) {
        final SqlService service = securityManager.authenticate(token);
        service.setStatus(SystemStatus.STOPPED);
        serviceRepository.update(service);
    }
}
