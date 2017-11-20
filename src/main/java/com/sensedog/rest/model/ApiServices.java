package com.sensedog.rest.model;

import com.sensedog.detection.DetectionType;
import com.sensedog.system.SystemStatus;

import java.util.List;

public class ApiServices {

    private List<Service> services;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(final List<Service> services) {
        this.services = services;
    }

    public static class Service {
        private String serviceName;
        private Long lastDetectionTime;
        private DetectionType lastDetectionType;
        private Integer numberOfDetections;
        private SystemStatus status;
        private List<ApiSubscriberView> subscribers;

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(final String serviceName) {
            this.serviceName = serviceName;
        }

        public Long getLastDetectionTime() {
            return lastDetectionTime;
        }

        public void setLastDetectionTime(final Long lastDetectionTime) {
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

        public List<ApiSubscriberView> getSubscribers() {
            return subscribers;
        }

        public void setSubscribers(final List<ApiSubscriberView> subscribers) {
            this.subscribers = subscribers;
        }
    }
}
