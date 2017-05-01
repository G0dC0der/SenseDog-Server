package com.sensedog.rest.entry.response;

import com.sensedog.detection.DetectionType;
import com.sensedog.system.SystemStatus;

import java.util.List;

public class ServicesResponse {

    private List<Service> services;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public static class Service {
        private String serviceName;
        private Long lastDetectionTime;
        private DetectionType lastDetectionType;
        private Integer numberOfDetections;
        private SystemStatus status;
        private List<SubscriberViewResponse> subscribers;

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public Long getLastDetectionTime() {
            return lastDetectionTime;
        }

        public void setLastDetectionTime(Long lastDetectionTime) {
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

        public List<SubscriberViewResponse> getSubscribers() {
            return subscribers;
        }

        public void setSubscribers(List<SubscriberViewResponse> subscribers) {
            this.subscribers = subscribers;
        }
    }
}
