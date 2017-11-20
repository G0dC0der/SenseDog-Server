package com.sensedog.communication.cloud;

import com.sensedog.util.Mapper;

import java.util.Map;

public class CloudMessage<T> {

    private final T data;
    private final String to;

    private CloudMessage(final T data, final String to) {
        this.to = to;
        this.data = data;
    }

    String getJson() {
        return Mapper.stringify(new SerializedMessage(Mapper.toMap(data), to));
    }

    public static <T> CloudMessage<T> from(final T data, final String to) {
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one receiver.");
        }

        return new CloudMessage<>(data, to);
    }

    private static class SerializedMessage {
        Map<?, ?> data;
        String to;

        SerializedMessage(){
        }

        SerializedMessage(final Map<?, ?> data, final String to) {
            this.data = data;
            this.to = to;
        }

        Map<?, ?> getData() {
            return data;
        }

        void setData(final Map<String, String> data) {
            this.data = data;
        }

        String getTo() {
            return to;
        }

        void setTo(final String to) {
            this.to = to;
        }
    }
}
