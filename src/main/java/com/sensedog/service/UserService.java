package com.sensedog.service;

import com.sensedog.detection.Severity;
import com.sensedog.repository.MasterUserRepository;
import com.sensedog.repository.PinCodeRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.SubscriberRepository;
import com.sensedog.repository.entry.MasterUser;
import com.sensedog.repository.entry.PinCode;
import com.sensedog.repository.entry.Service;
import com.sensedog.repository.entry.Subscriber;
import com.sensedog.repository.entry.SubscriberCapability;
import com.sensedog.security.Capability;
import com.sensedog.security.Cipher;
import com.sensedog.security.CredentialException;
import com.sensedog.security.SecurityManager;
import com.sensedog.security.UnauthorizedException;
import org.hibernate.exception.ConstraintViolationException;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.Arrays;

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

    public void createMasterUser(String name,
                                 String phone,
                                 String email,
                                 String cloudToken) {

        MasterUser masterUser = new MasterUser();
        masterUser.setEmail(email);
        masterUser.setName(name);
        masterUser.setPhone(phone);
        masterUser.setCloudToken(cloudToken);

        try {
            masterUserRepository.save(masterUser);
        } catch (ConstraintViolationException e) {
            throw new CredentialException("Email or Phone already in use.", e);
        }
    }

    public String connectToService(String pinCode, String email) {
        MasterUser masterUser = masterUserRepository.findByEmail(email);

        if (masterUser == null) {
            throw new CredentialException("Invalid email. Please register an account first.");
        }

        PinCode pin = pinCodeRepository.getByPinCode(pinCode);

        if (pin == null) {
            throw new UnauthorizedException("Invalid pin code.");
        }
        if (securityManager.hasExpired(pin)) {
            throw new UnauthorizedException("Pin code has expired.");
        }
        Service service = pin.getService();

        if (service.getMasterUser() != null) {
            throw new UnauthorizedException("A master user is already assigned for this service.");
        }

        service.setMasterUser(masterUser);
        service.setMasterAuthToken(Cipher.authToken());

        serviceRepository.update(service);

        return service.getMasterAuthToken();
    }

    public void invite(String token,
                       String name,
                       String phone,
                       String email) {
        Service service = serviceRepository.getByMasterToken(token);

        if (service == null) {
            throw new UnauthorizedException("Invalid token.");
        }

        for (Subscriber subscriber : service.getSubscribers()) {
            if (subscriber.getEmail().equals(email) || subscriber.getPhone().equals(phone)) {
                throw new BadRequestException("Can not invite someone already invited.");
            }
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);
        subscriber.setPhone(phone);
        subscriber.setName(name);
        subscriber.setMinimumSeverity(Severity.SUSPICIOUS);
        subscriber.setNotifyRegularity(1);
        subscriber.setUnsubscribeKey(Cipher.authToken());
        subscriber.setService(service);

        SubscriberCapability subscriberCapability1 = new SubscriberCapability();
        subscriberCapability1.setCapability(Capability.CALL_RECEIVER);
        subscriberCapability1.setSubscriber(subscriber);

        SubscriberCapability subscriberCapability2 = new SubscriberCapability();
        subscriberCapability2.setCapability(Capability.SMS_READER);
        subscriberCapability2.setSubscriber(subscriber);

        subscriber.setSubscriberCapabilities(Arrays.asList(subscriberCapability1, subscriberCapability2));

        subscriberRepository.save(subscriber);
    }
}
