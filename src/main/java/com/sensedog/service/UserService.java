package com.sensedog.service;

import com.sensedog.detection.Severity;
import com.sensedog.repository.MasterUserRepository;
import com.sensedog.repository.PinCodeRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.SubscriberRepository;
import com.sensedog.repository.model.SqlMaster;
import com.sensedog.repository.model.SqlPincode;
import com.sensedog.repository.model.SqlService;
import com.sensedog.repository.model.SqlSubscriber;
import com.sensedog.repository.model.SqlSubscriberCapability;
import com.sensedog.security.AuthenticationFailedException;
import com.sensedog.security.Capability;
import com.sensedog.security.Cipher;
import com.sensedog.security.CredentialException;
import com.sensedog.security.SecurityManager;
import com.sensedog.security.Token;
import com.sensedog.service.model.ServiceData;
import com.sensedog.system.SystemStatus;
import org.hibernate.exception.ConstraintViolationException;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final MasterUserRepository masterUserRepository;
    private final ServiceRepository serviceRepository;
    private final PinCodeRepository pinCodeRepository;
    private final SubscriberRepository subscriberRepository;
    private final SecurityManager securityManager;

    @Inject
    public UserService(final MasterUserRepository masterUserRepository,
                       final ServiceRepository serviceRepository,
                       final PinCodeRepository pinCodeRepository,
                       final SubscriberRepository subscriberRepository,
                       final SecurityManager securityManager) {
        this.masterUserRepository = masterUserRepository;
        this.serviceRepository = serviceRepository;
        this.subscriberRepository = subscriberRepository;
        this.securityManager = securityManager;
        this.pinCodeRepository = pinCodeRepository;
    }

    public void createMasterUser(
            final String name,
            final String phone,
            final String email,
            final String cloudToken) {
        final SqlMaster masterUser = new SqlMaster();
        masterUser.setEmail(email);
        masterUser.setName(name);
        masterUser.setPhone(phone);
        masterUser.setCloudToken(cloudToken);

        try {
            masterUserRepository.save(masterUser);
        } catch (final ConstraintViolationException e) {
            throw new CredentialException("Email or Phone already in use.", e);
        }
    }

    public String connectToService(final String pinCode, final String email) {
        final SqlMaster masterUser = masterUserRepository.findByEmail(email);

        if (masterUser == null) {
            throw new CredentialException("Invalid email. Please register an account first.");
        }

        final SqlPincode pin = pinCodeRepository.getByPinCode(pinCode);

        if (pin == null) {
            throw new AuthenticationFailedException("Invalid pin code.");
        }
        if (securityManager.hasExpired(pin)) {
            throw new AuthenticationFailedException("Pin code has expired.");
        }
        final SqlService service = pin.getService();

        if (service.getMasterUser() != null) {
            throw new AuthenticationFailedException("A master user is already assigned for this service.");
        }

        service.setStatus(SystemStatus.ACTIVE);
        service.setMasterUser(masterUser);
        service.setMasterAuthToken(Cipher.authToken());

        serviceRepository.update(service);

        return service.getMasterAuthToken();
    }

    public void login(final String email) {
        final SqlMaster masterUser = masterUserRepository.findByEmail(email);
        if (masterUser == null) {
            throw new CredentialException("Failed to login: " + email);
        }
    }

    public void invite(final Token.Master token,
                       final String name,
                       final String phone,
                       final String email) {
        final SqlService service = securityManager.authenticate(token);

        for (final SqlSubscriber subscriber : service.getSubscribers()) {
            if (subscriber.getEmail().equals(email) || subscriber.getPhone().equals(phone)) {
                throw new BadRequestException("Can not invite someone already invited.");
            }
        }

        final SqlSubscriber subscriber = new SqlSubscriber();
        subscriber.setEmail(email);
        subscriber.setPhone(phone);
        subscriber.setName(name);
        subscriber.setMinimumSeverity(Severity.SUSPICIOUS);
        subscriber.setNotifyRegularity(SecurityManager.MINIMUM_NOTIFICATION_REGULARITY);
        subscriber.setSecurityKey(Cipher.authToken());
        subscriber.setService(service);

        final SqlSubscriberCapability subscriberCapability1 = new SqlSubscriberCapability();
        subscriberCapability1.setCapability(Capability.CALL_RECEIVER);
        subscriberCapability1.setSubscriber(subscriber);

        final SqlSubscriberCapability subscriberCapability2 = new SqlSubscriberCapability();
        subscriberCapability2.setCapability(Capability.SMS_READER);
        subscriberCapability2.setSubscriber(subscriber);

        subscriber.setSubscriberCapabilities(Arrays.asList(subscriberCapability1, subscriberCapability2));

        subscriberRepository.save(subscriber);
    }

    public List<ServiceData> viewAll(final String email) {
        final SqlService[] services = serviceRepository.findServicesByMasterEmail(email);

        return Stream.of(services)
                .map(ServiceData::from)
                .collect(Collectors.toList());
    }

    public void resume(final Token token) {
        final SqlService service = securityManager.authenticate(token);
        service.setStatus(SystemStatus.ACTIVE);
        serviceRepository.update(service);
    }

    public void pause(final Token token) {
        final SqlService service = securityManager.authenticate(token);
        service.setStatus(SystemStatus.STOPPED);
        serviceRepository.update(service);
    }

    public void updateAlarmCloudToken(final Token.Alarm token, final String cloudToken) {
        final SqlService service = securityManager.authenticate(token);

        service.getAlarmDevice().setCloudToken(cloudToken);
        serviceRepository.update(service);
    }

    public void updateMasterCloudToken(final Token.Master token, final String cloudToken) {
        final SqlService service = securityManager.authenticate(token);

        service.getMasterUser().setCloudToken(cloudToken);
        serviceRepository.update(service);
    }
}
