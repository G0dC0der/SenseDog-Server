package com.sensedog.transmit;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.util.Map.Entry;

public class CloudClient {

    private final Sender sender;
    private final int retries;

    public CloudClient(String apiKey, int retries) {
        this.sender = new Sender(apiKey);
        this.retries = retries;
    }

    public void send(CloudMessage<?> message) { //TODO: Error handling
        try {
            Message sendData = prepareMessage(message);
            sender.send(sendData, message.getReceivers(), retries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message prepareMessage(CloudMessage<?> message) {
        Message.Builder builder = new Message.Builder();

        for(Entry<String, String> entry : message.getData().entrySet())  {
            builder.addData(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }
}
