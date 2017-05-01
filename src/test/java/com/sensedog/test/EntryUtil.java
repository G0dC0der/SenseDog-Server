package com.sensedog.test;

import com.sensedog.detection.DetectionType;
import com.sensedog.repository.entry.Detection;
import com.sensedog.rest.entry.request.AlarmCreateRequest;
import com.sensedog.rest.entry.request.DetectRequest;
import com.sensedog.rest.entry.request.MasterUserCreateRequest;

import java.time.ZonedDateTime;

import static com.sensedog.test.Strings.junk;
import static com.sensedog.test.Strings.numbers;

public class EntryUtil {

    public static Detection detection(ZonedDateTime detectionDate)  {
        return detection(detectionDate, DetectionType.ROTATION);
    }

    public static DetectRequest detectRequest() {
        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setDetectionType(DetectionType.ROTATION);
        detectRequest.setValue(numbers());
        return detectRequest;
    }

    public static Detection detection(ZonedDateTime detectionDate, DetectionType detectionType) {
        Detection detection = new Detection();
        detection.setDetectionType(detectionType);
        detection.setDetectionDate(detectionDate);

        return detection;
    }

    public static AlarmCreateRequest alarmCreateRequest() {
        AlarmCreateRequest alarmCreateRequest = new AlarmCreateRequest();
        alarmCreateRequest.setAppVersion(junk());
        alarmCreateRequest.setCarrier(junk());
        alarmCreateRequest.setCloudToken(junk());
        alarmCreateRequest.setDeviceModel(junk());
        alarmCreateRequest.setOsVersion(junk());
        alarmCreateRequest.setServiceName(junk());
        alarmCreateRequest.setBattery(1.0f);
        return alarmCreateRequest;
    }

    public static MasterUserCreateRequest masterUserCreateRequest() {
        MasterUserCreateRequest masterUserCreateRequest = new MasterUserCreateRequest();
        masterUserCreateRequest.setPhone(numbers());
        masterUserCreateRequest.setEmail(junk() + "@" + junk() + ".com");
        masterUserCreateRequest.setName(junk());
        masterUserCreateRequest.setCloudToken(junk());

        return masterUserCreateRequest;
    }
}
