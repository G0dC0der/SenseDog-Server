package com.sensedog.rest.entry;

import com.sensedog.rest.entry.response.ServicesResponse;
import com.sensedog.rest.entry.response.SubscriberViewResponse;
import com.sensedog.service.domain.ServiceInfo;
import com.sensedog.service.domain.SubscriberInfo;

import java.util.stream.Collectors;

public class EntryConverter {

    public static ServicesResponse.Service convert(ServiceInfo serviceInfo) {
        ServicesResponse.Service servicesResponse = new ServicesResponse.Service();
        servicesResponse.setLastDetectionTime(serviceInfo.getLastDetectionTime() == null ? null : serviceInfo.getLastDetectionTime().toInstant().getEpochSecond()); //TODO: Temp
        servicesResponse.setLastDetectionType(serviceInfo.getLastDetectionType());
        servicesResponse.setNumberOfDetections(serviceInfo.getNumberOfDetections());
        servicesResponse.setServiceName(serviceInfo.getServiceName());
        servicesResponse.setStatus(serviceInfo.getStatus());
        servicesResponse.setSubscribers(serviceInfo.getSubscribers().stream().map(EntryConverter::convert).collect(Collectors.toList()));

        return servicesResponse;
    }

    public static SubscriberViewResponse convert(SubscriberInfo subscriberInfo) {
        SubscriberViewResponse subscriberViewResponse = new SubscriberViewResponse();
        subscriberViewResponse.setEmail(subscriberInfo.getEmail());
        subscriberViewResponse.setPhone(subscriberInfo.getPhone());
        subscriberViewResponse.setName(subscriberInfo.getName());

        return subscriberViewResponse;
    }
}
