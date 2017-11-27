package com.sensedog.system;

import com.sensedog.communication.cloud.CloudClient;
import com.sensedog.communication.mail.MailClient;
import com.sensedog.communication.transmit.TransmitClient;
import com.sensedog.repository.AlarmDeviceRepository;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.MasterUserRepository;
import com.sensedog.repository.PinCodeRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.SubscriberRepository;
import com.sensedog.rest.AlarmEndpoint;
import com.sensedog.rest.MasterEndpoint;
import com.sensedog.service.AlarmService;
import com.sensedog.service.StatusService;
import com.sensedog.service.UserService;
import com.sensedog.util.PropertyUtil;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;

import javax.inject.Singleton;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import java.util.Properties;

public class SystemBootstrap extends ResourceConfig {

    SystemBootstrap(@Context final ServiceLocator locator) {
        register(AlarmEndpoint.class);
        register(MasterEndpoint.class);

        packages(true, "com.sensedog");

        final SessionFactory sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("sensedog-engine");
        final SessionProvider sessionProvider = sessionFactory::openSession;

        final Properties props = PropertyUtil.readProperties("settings.properties");
        final ServerSettings serverSettings = ServerSettings.builder()
                .cloudKey(props.getProperty("cloud.key"))
                .sendgridKey(props.getProperty("sendgrid.key"))
                .sendgridSender(props.getProperty("sendgrid.sender"))
                .twiloSender(props.getProperty("twilio.sender"))
                .twiloAccountToken(props.getProperty("twilio.account.token"))
                .twiloAuthToken(props.getProperty("twilio.auth.token"))
                .pincodeDuration(Integer.parseInt(props.getProperty("security.pincode.life")))
                .gpsSafeRadius(Integer.parseInt(props.getProperty("security.gps.saferadius")))
                .build();

        final CloudClient cloudClient = new CloudClient(serverSettings);
        final TransmitClient transmitClient = new TransmitClient(serverSettings);
        final MailClient messenger = new MailClient(serverSettings);
        final com.sensedog.security.SecurityManager.Settings settings = ()-> Long.parseLong(props.getProperty("security.pincode.life"));

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(sessionProvider).to(SessionProvider.class);
                bind(serverSettings).to(ServerSettings.class);
                bind(ServiceRepository.class).to(ServiceRepository.class).in(Singleton.class);
                bind(cloudClient).to(CloudClient.class);
                bind(transmitClient).to(TransmitClient.class);
                bind(messenger).to(MailClient.class);
                bind(settings).to(com.sensedog.security.SecurityManager.Settings.class);
                bind(AlarmService.class).to(AlarmService.class).in(Singleton.class);
                bind(AlarmDeviceRepository.class).to(AlarmDeviceRepository.class).in(Singleton.class);
                bind(MasterUserRepository.class).to(MasterUserRepository.class).in(Singleton.class);
                bind(DetectionRepository.class).to(DetectionRepository.class).in(Singleton.class);
                bind(UserService.class).to(UserService.class).in(Singleton.class);
                bind(PinCodeRepository.class).to(PinCodeRepository.class).in(Singleton.class);
                bind(SubscriberRepository.class).to(SubscriberRepository.class).in(Singleton.class);
                bind(com.sensedog.security.SecurityManager.class).to(com.sensedog.security.SecurityManager.class).in(Singleton.class);
                bind(StatusService.class).to(StatusService.class).in(Singleton.class);
            }
        });
    }
}
