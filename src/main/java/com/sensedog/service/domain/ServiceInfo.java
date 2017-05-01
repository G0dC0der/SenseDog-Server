package com.sensedog.service.domain;

import com.sensedog.detection.DetectionType;
import com.sensedog.repository.entry.Detection;
import com.sensedog.repository.entry.Service;
import com.sensedog.system.SystemStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceInfo {

    private String serviceName;
    private String masterToken;
    private ZonedDateTime lastDetectionTime;
    private DetectionType lastDetectionType;
    private Integer numberOfDetections;
    private SystemStatus status;
    private List<SubscriberInfo> subscribers;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ZonedDateTime getLastDetectionTime() {
        return lastDetectionTime;
    }

    public void setLastDetectionTime(ZonedDateTime lastDetectionTime) {
        this.lastDetectionTime = lastDetectionTime;
    }

    public DetectionType getLastDetectionType() {
        return lastDetectionType;
    }

    public void setLastDetectionType(DetectionType lastDetectionType) {
        this.lastDetectionType = lastDetectionType;
    }

    public Integer getNumberOfDetections() {
        return numberOfDetections;
    }

    public void setNumberOfDetections(Integer numberOfDetections) {
        this.numberOfDetections = numberOfDetections;
    }

    public SystemStatus getStatus() {
        return status;
    }

    public void setStatus(SystemStatus status) {
        this.status = status;
    }

    public List<SubscriberInfo> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<SubscriberInfo> subscribers) {
        this.subscribers = subscribers;
    }

    public String getMasterToken() {
        return masterToken;
    }

    public void setMasterToken(String masterToken) {
        this.masterToken = masterToken;
    }

    public static ServiceInfo from(Service service) {
        List<Detection> detections = service.getDetections();

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setLastDetectionTime(detections.isEmpty() ? null : detections.get(detections.size() - 1).getDetectionDate());
        serviceInfo.setLastDetectionType(detections.isEmpty() ? null : detections.get(detections.size() - 1).getDetectionType());
        serviceInfo.setNumberOfDetections(detections.size());
        serviceInfo.setServiceName(service.getServiceName());
        serviceInfo.setStatus(service.getStatus());
        serviceInfo.setSubscribers(service.getSubscribers().stream().map(SubscriberInfo::from).collect(Collectors.toList()));
        serviceInfo.setMasterToken(service.getMasterAuthToken());

        return serviceInfo;
    }
}
