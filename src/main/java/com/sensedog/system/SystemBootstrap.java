package com.sensedog.system;

import com.sensedog.repository.AlarmDeviceRepository;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.MasterUserRepository;
import com.sensedog.repository.PinCodeRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.SubscriberRepository;
import com.sensedog.rest.AlarmResource;
import com.sensedog.rest.MasterResource;
import com.sensedog.rest.StatusResource;
import com.sensedog.rest.TestResource;
import com.sensedog.security.SecurityManager;
import com.sensedog.service.AlarmService;
import com.sensedog.service.StatusService;
import com.sensedog.service.UserService;
import com.sensedog.transmit.CloudClient;
import com.sensedog.transmit.MailClient;
import com.sensedog.transmit.TransmitClient;
import com.sensedog.util.PropertyUtil;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;

import javax.inject.Singleton;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import java.util.Properties;

class SystemBootstrap extends ResourceConfig {

    SystemBootstrap(@Context ServiceLocator locator) {
        register(AlarmResource.class);
        register(MasterResource.class);
        register(StatusResource.class);
        register(JacksonFeature.class);

        final SessionFactory sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("sensedog-engine");
        final SessionProvider sessionProvider = sessionFactory::openSession;
        final Properties props = PropertyUtil.readProperties("settings.properties");
        final CloudClient cloudClient = new CloudClient(props.getProperty("cloud.key"));
        final TransmitClient transmitClient = new TransmitClient(props.getProperty("twilio.sender"), props.getProperty("twilio.account.token"), props.getProperty("twilio.auth.token"));
        final MailClient messenger = new MailClient(props.getProperty("sendgrid.sender"), props.getProperty("sendgrid.key"));
        final SecurityManager.Settings settings = ()-> Long.parseLong(props.getProperty("security.pincode.life"));

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(sessionProvider).to(SessionProvider.class);
                bind(ServiceRepository.class).to(ServiceRepository.class).in(Singleton.class);
                bind(cloudClient).to(CloudClient.class);
                bind(transmitClient).to(TransmitClient.class);
                bind(messenger).to(MailClient.class);
                bind(settings).to(SecurityManager.Settings.class);
                bind(AlarmService.class).to(AlarmService.class).in(Singleton.class);
                bind(AlarmDeviceRepository.class).to(AlarmDeviceRepository.class).in(Singleton.class);
                bind(MasterUserRepository.class).to(MasterUserRepository.class).in(Singleton.class);
                bind(DetectionRepository.class).to(DetectionRepository.class).in(Singleton.class);
                bind(UserService.class).to(UserService.class).in(Singleton.class);
                bind(PinCodeRepository.class).to(PinCodeRepository.class).in(Singleton.class);
                bind(SubscriberRepository.class).to(SubscriberRepository.class).in(Singleton.class);
                bind(SecurityManager.class).to(SecurityManager.class).in(Singleton.class);
                bind(StatusService.class).to(StatusService.class).in(Singleton.class);
            }
        });
    }
}
