package com.sensedog.service;

import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.model.SqlAlarmDevice;
import com.sensedog.repository.model.SqlService;
import com.sensedog.security.SecurityManager;
import com.sensedog.security.Token;
import com.sensedog.service.model.HealthStatus;
import com.sensedog.system.SystemStatus;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;

@Service
public class StatusService {

    private final SecurityManager securityManager;
    private final ServiceRepository serviceRepository;

    @Inject
    public StatusService(final SecurityManager securityManager,
                         final ServiceRepository serviceRepository) {
        this.securityManager = securityManager;
        this.serviceRepository = serviceRepository;
    }

    public void report(final Token.Alarm token, final Float battery) {
        final SqlService service = securityManager.authenticate(token);
        service.setStatus(SystemStatus.ACTIVE);

        final SqlAlarmDevice sqlAlarmDevice = service.getAlarmDevice();
        sqlAlarmDevice.setBattery(battery);
        sqlAlarmDevice.setLastSeen(ZonedDateTime.now());

        serviceRepository.update(service);
    }

    public HealthStatus read(final Token token) {
        final SqlService service = securityManager.authenticate(token);
        final SqlAlarmDevice sqlAlarmDevice = service.getAlarmDevice();

        if (service.getStatus().isActive() && securityManager.isLost(sqlAlarmDevice)) {
            service.setStatus(SystemStatus.STOPPED);
            serviceRepository.update(service);
        }

        final HealthStatus healthStatus = new HealthStatus();
        healthStatus.setBattery(sqlAlarmDevice.getBattery());
        healthStatus.setLastSeen(sqlAlarmDevice.getLastSeen());
        healthStatus.setSystemStatus(service.getStatus());

        return healthStatus;
    }
}
