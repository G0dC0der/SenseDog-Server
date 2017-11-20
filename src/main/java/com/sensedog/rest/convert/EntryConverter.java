package com.sensedog.rest.convert;

import com.sensedog.rest.model.ApiServices;
import com.sensedog.rest.model.ApiSubscriberView;
import com.sensedog.service.model.ServiceData;
import com.sensedog.service.model.Subscriber;

import java.util.stream.Collectors;

public class EntryConverter {

    public static ApiServices.Service convert(final ServiceData serviceInfo) {
        final ApiServices.Service servicesResponse = new ApiServices.Service();
        servicesResponse.setLastDetectionTime(serviceInfo.getLastDetectionTime() == null ? null : serviceInfo.getLastDetectionTime().toInstant().getEpochSecond()); //TODO: Temp
        servicesResponse.setLastDetectionType(serviceInfo.getLastDetectionType());
        servicesResponse.setNumberOfDetections(serviceInfo.getNumberOfDetections());
        servicesResponse.setServiceName(serviceInfo.getServiceName());
        servicesResponse.setStatus(serviceInfo.getStatus());
        servicesResponse.setSubscribers(serviceInfo.getSubscribers().stream().map(EntryConverter::convert).collect(Collectors.toList()));

        return servicesResponse;
    }

    public static ApiSubscriberView convert(final Subscriber subscriberInfo) {
        final ApiSubscriberView apiSubscriberView = new ApiSubscriberView();
        apiSubscriberView.setEmail(subscriberInfo.getEmail());
        apiSubscriberView.setPhone(subscriberInfo.getPhone());
        apiSubscriberView.setName(subscriberInfo.getName());

        return apiSubscriberView;
    }
}
