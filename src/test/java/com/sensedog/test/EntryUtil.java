package com.sensedog.test;

import com.sensedog.detection.DetectionType;
import com.sensedog.repository.entry.Detection;

import java.time.ZonedDateTime;

public class EntryUtil {

    public static Detection detection(ZonedDateTime detectionDate)  {
        return detection(detectionDate, DetectionType.ROTATION);
    }

    public static Detection detection(ZonedDateTime detectionDate, DetectionType detectionType) {
        Detection detection = new Detection();
        detection.setDetectionType(detectionType);
        detection.setDetectionDate(detectionDate);

        return detection;
    }
}
