package com.sensedog.service;

import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.entry.AlarmDevice;
import com.sensedog.repository.entry.Service;
import com.sensedog.security.SecurityManager;
import com.sensedog.security.Token;
import com.sensedog.service.domain.HealthStatus;
import com.sensedog.system.SystemStatus;

import javax.inject.Inject;
import java.time.ZonedDateTime;

public class StatusService {

    private final SecurityManager securityManager;
    private final ServiceRepository serviceRepository;

    @Inject
    public StatusService(final SecurityManager securityManager,
                         final ServiceRepository serviceRepository) {
        this.securityManager = securityManager;
        this.serviceRepository = serviceRepository;
    }

    public void report(Token.Alarm token, Float battery) {
        Service service = securityManager.authenticate(token);
        service.setStatus(SystemStatus.ACTIVE);

        AlarmDevice alarmDevice = service.getAlarmDevice();
        alarmDevice.setBattery(battery);
        alarmDevice.setLastSeen(ZonedDateTime.now());

        serviceRepository.update(service);
    }

    public HealthStatus read(Token token) {
        Service service = securityManager.authenticate(token);
        AlarmDevice alarmDevice = service.getAlarmDevice();

        if (service.getStatus().isActive() && securityManager.isLost(alarmDevice)) {
            service.setStatus(SystemStatus.STOPPED);
            serviceRepository.update(service);
        }

        HealthStatus healthStatus = new HealthStatus();
        healthStatus.setBattery(alarmDevice.getBattery());
        healthStatus.setLastSeen(alarmDevice.getLastSeen());
        healthStatus.setSystemStatus(service.getStatus());

        return healthStatus;
    }
}
