package com.sensedog.transmit;

import com.sensedog.util.Mapper;

import java.util.Map;

public class CloudMessage<T> {

    private T data;
    private String to;

    private CloudMessage(T data, String to) {
        this.to = to;
        this.data = data;
    }

    String getJson() {
        return Mapper.stringify(new SerializedMessage(Mapper.toMap(data), to));
    }

    public static <T> CloudMessage<T> from(T data, String to) {
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

        SerializedMessage(Map<?, ?> data, String to) {
            this.data = data;
            this.to = to;
        }

        Map<?, ?> getData() {
            return data;
        }

        void setData(Map<String, String> data) {
            this.data = data;
        }

        String getTo() {
            return to;
        }

        void setTo(String to) {
            this.to = to;
        }
    }
}
