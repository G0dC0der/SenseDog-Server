package com.sensedog.service.model;

import com.sensedog.detection.DetectionType;
import com.sensedog.repository.model.SqlDetection;
import com.sensedog.repository.model.SqlService;
import com.sensedog.system.SystemStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceData {

    private String serviceName;
    private String masterToken;
    private ZonedDateTime lastDetectionTime;
    private DetectionType lastDetectionType;
    private Integer numberOfDetections;
    private SystemStatus status;
    private List<Subscriber> subscribers;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public ZonedDateTime getLastDetectionTime() {
        return lastDetectionTime;
    }

    public void setLastDetectionTime(final ZonedDateTime lastDetectionTime) {
        this.lastDetectionTime = lastDetectionTime;
    }

    public DetectionType getLastDetectionType() {
        return lastDetectionType;
    }

    public void setLastDetectionType(final DetectionType lastDetectionType) {
        this.lastDetectionType = lastDetectionType;
    }

    public Integer getNumberOfDetections() {
        return numberOfDetections;
    }

    public void setNumberOfDetections(final Integer numberOfDetections) {
        this.numberOfDetections = numberOfDetections;
    }

    public SystemStatus getStatus() {
        return status;
    }

    public void setStatus(final SystemStatus status) {
        this.status = status;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(final List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public String getMasterToken() {
        return masterToken;
    }

    public void setMasterToken(final String masterToken) {
        this.masterToken = masterToken;
    }

    public static ServiceData from(final SqlService service) {
        final List<SqlDetection> detections = service.getDetections();

        final ServiceData serviceInfo = new ServiceData();
        serviceInfo.setLastDetectionTime(detections.isEmpty() ? null : detections.get(detections.size() - 1).getDetectionDate());
        serviceInfo.setLastDetectionType(detections.isEmpty() ? null : detections.get(detections.size() - 1).getDetectionType());
        serviceInfo.setNumberOfDetections(detections.size());
        serviceInfo.setServiceName(service.getServiceName());
        serviceInfo.setStatus(service.getStatus());
        serviceInfo.setSubscribers(service.getSubscribers().stream().map(Subscriber::from).collect(Collectors.toList()));
        serviceInfo.setMasterToken(service.getMasterAuthToken());

        return serviceInfo;
    }
}
