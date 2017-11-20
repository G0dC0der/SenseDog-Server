package com.sensedog.test;

import com.sensedog.detection.DetectionType;
import com.sensedog.repository.model.SqlDetection;
import com.sensedog.rest.model.request.ApiAlarmCreate;
import com.sensedog.rest.model.request.ApiDetect;
import com.sensedog.rest.model.request.ApiMasterUserCreate;

import java.time.ZonedDateTime;

import static com.sensedog.test.Strings.junk;
import static com.sensedog.test.Strings.numbers;

public class EntryUtil {

    public static SqlDetection detection(final ZonedDateTime detectionDate)  {
        return detection(detectionDate, DetectionType.ROTATION);
    }

    public static ApiDetect detectRequest() {
        final ApiDetect detectRequest = new ApiDetect();
        detectRequest.setDetectionType(DetectionType.ROTATION);
        detectRequest.setValue(numbers());
        return detectRequest;
    }

    public static SqlDetection detection(final ZonedDateTime detectionDate, final DetectionType detectionType) {
        final SqlDetection detection = new SqlDetection();
        detection.setDetectionType(detectionType);
        detection.setDetectionDate(detectionDate);

        return detection;
    }

    public static ApiAlarmCreate alarmCreateRequest() {
        final ApiAlarmCreate apiAlarmCreate = new ApiAlarmCreate();
        apiAlarmCreate.setAppVersion(junk());
        apiAlarmCreate.setCarrier(junk());
        apiAlarmCreate.setCloudToken(junk());
        apiAlarmCreate.setDeviceModel(junk());
        apiAlarmCreate.setOsVersion(junk());
        apiAlarmCreate.setServiceName(junk());
        apiAlarmCreate.setBattery(1.0f);
        return apiAlarmCreate;
    }

    public static ApiMasterUserCreate masterUserCreateRequest() {
        final ApiMasterUserCreate apiMasterUserCreate = new ApiMasterUserCreate();
        apiMasterUserCreate.setPhone(numbers());
        apiMasterUserCreate.setEmail(junk() + "@" + junk() + ".com");
        apiMasterUserCreate.setName(junk());
        apiMasterUserCreate.setCloudToken(junk());

        return apiMasterUserCreate;
    }
}
