package com.sensedog.security;

import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import com.sensedog.repository.DetectionRepository;
import com.sensedog.repository.ServiceRepository;
import com.sensedog.repository.entry.Detection;
import com.sensedog.repository.entry.PinCode;
import com.sensedog.repository.entry.Service;
import com.sensedog.test.EntryUtil;
import com.sensedog.test.Strings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.sensedog.test.Strings.junk;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

public class SecurityManagerTest {

    private DetectionRepository detectionRepository;
    private ServiceRepository serviceRepository;
    private SecurityManager securityManager;
    private long expireDelay;

    @Before
    public void init() {
        expireDelay = 30;
        detectionRepository = Mockito.mock(DetectionRepository.class);
        serviceRepository = Mockito.mock(ServiceRepository.class);
        securityManager = new SecurityManager(()-> expireDelay * 60000, detectionRepository, serviceRepository);
    }

    @Test
    public void exceptNotExpired() {
        PinCode pinCode = new PinCode();

        pinCode.setCreationDate(ZonedDateTime.now());
        assertFalse(securityManager.hasExpired(pinCode));

        pinCode.setCreationDate(ZonedDateTime.now().minusMinutes(expireDelay / 2));
        assertFalse(securityManager.hasExpired(pinCode));

        pinCode.setCreationDate(ZonedDateTime.now().minusMinutes(expireDelay - 1));
        assertFalse(securityManager.hasExpired(pinCode));

        pinCode.setCreationDate(ZonedDateTime.now().plusMinutes(expireDelay / 2));
        assertFalse(securityManager.hasExpired(pinCode));

        pinCode.setCreationDate(ZonedDateTime.now().plusMinutes(expireDelay));
        assertFalse(securityManager.hasExpired(pinCode));
    }

    @Test
    public void exceptExpired() {
        PinCode pinCode = new PinCode();

        pinCode.setCreationDate(ZonedDateTime.now().minusMinutes(expireDelay));
        assertTrue(securityManager.hasExpired(pinCode));

        pinCode.setCreationDate(ZonedDateTime.now().minusMinutes(expireDelay + 15));
        assertTrue(securityManager.hasExpired(pinCode));

        pinCode.setCreationDate(ZonedDateTime.now().minusMinutes(expireDelay + 999999));
        assertTrue(securityManager.hasExpired(pinCode));
    }

    @Test
    public void assertSuspiciousSeverityWhenNothing() {
        when(detectionRepository.getBetween(anyObject(), anyObject(), anyObject())).thenReturn(new ArrayList<>());

        Severity severity = securityManager.determineSeverity(new Service(), DetectionType.ROTATION);
        assertEquals(Severity.SUSPICIOUS, severity);
    }

    @Test
    public void assertSuspiciousSeverityWhenFuture() {
        Detection detection = EntryUtil.detection(ZonedDateTime.now().plusSeconds(30));
        when(detectionRepository.getBetween(anyObject(), anyObject(), anyObject())).thenReturn(Collections.singletonList(detection));

        Severity severity = securityManager.determineSeverity(new Service(), DetectionType.ROTATION);
        assertEquals(Severity.SUSPICIOUS, severity);
    }

    @Test
    public void assertWarningSeverity() {
        Detection detection1 = EntryUtil.detection(ZonedDateTime.now());
        Detection detection2 = EntryUtil.detection(ZonedDateTime.now().minusSeconds(30));
        Detection detection3 = EntryUtil.detection(ZonedDateTime.now().plusSeconds(30));

        when(detectionRepository.getBetween(anyObject(), anyObject(), anyObject())).thenReturn(Arrays.asList(detection1, detection2, detection3));
        Severity severity = securityManager.determineSeverity(new Service(), DetectionType.ROTATION);
        assertEquals(Severity.WARNING, severity);
    }

    @Test
    public void assertWarningSeverityTwo() {
        Detection detection1 = EntryUtil.detection(ZonedDateTime.now());
        Detection detection2 = EntryUtil.detection(ZonedDateTime.now().minusSeconds(1));
        Detection detection3 = EntryUtil.detection(ZonedDateTime.now().plusSeconds(30));

        when(detectionRepository.getBetween(anyObject(), anyObject(), anyObject())).thenReturn(Arrays.asList(detection1, detection2, detection3));
        Severity severity = securityManager.determineSeverity(new Service(), DetectionType.ROTATION);
        assertEquals(Severity.WARNING, severity);
    }

    @Test
    public void assertCriticalSeverity() {
        Detection detection1 = EntryUtil.detection(ZonedDateTime.now());
        Detection detection2 = EntryUtil.detection(ZonedDateTime.now().minusSeconds(25));
        Detection detection3 = EntryUtil.detection(ZonedDateTime.now().minusSeconds(40));
        Detection detection4 = EntryUtil.detection(ZonedDateTime.now().plusSeconds(30));

        when(detectionRepository.getBetween(anyObject(), anyObject(), anyObject())).thenReturn(Arrays.asList(detection1, detection2, detection3, detection4));
        Severity severity = securityManager.determineSeverity(new Service(), DetectionType.ROTATION);
        assertEquals(Severity.CRITICAL, severity);
    }

    @Test
    public void assertCriticalSeverityTwo() {
        Detection detection1 = EntryUtil.detection(ZonedDateTime.now());
        Detection detection2 = EntryUtil.detection(ZonedDateTime.now().minusSeconds(30));
        Detection detection3 = EntryUtil.detection(ZonedDateTime.now().minusSeconds(60));
        Detection detection4 = EntryUtil.detection(ZonedDateTime.now().plusSeconds(30));

        when(detectionRepository.getBetween(anyObject(), anyObject(), anyObject())).thenReturn(Arrays.asList(detection1, detection2, detection3, detection4));
        Severity severity = securityManager.determineSeverity(new Service(), DetectionType.ROTATION);
        assertEquals(Severity.CRITICAL, severity);
    }


}