package com.sensedog.transmit;

import com.sensedog.util.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CloudMessage<T> {

    private T data;
    private String receivers[];

    private CloudMessage(T data, String[] receivers) {
        this.receivers = receivers;
        this.data = data;
    }

    Map<String, String> getData() {
        return Mapper.toMap(data)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Object::toString, Object::toString));
    }

    List<String> getReceivers() {
        return Arrays.asList(receivers);
    }

    public static <T> CloudMessage<T> from(T data, String... receivers) {
        if (receivers == null || receivers.length == 0) {
            throw new IllegalArgumentException("Must have at least one receiver.");
        }

        return new CloudMessage<>(data, receivers);
    }
}
