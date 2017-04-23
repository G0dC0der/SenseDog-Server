package com.sensedog.transmit;

import com.sun.xml.internal.ws.client.SenderException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CloudClient {

    private final String apiKey;
    private final Client client;

    public CloudClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = ClientBuilder.newClient();
    }

    public void send(CloudMessage<?> message) {
        Response response = client.target("https://fcm.googleapis.com/fcm/send")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "key=" + apiKey)
                .header("Content-Type", "application/json")
                .post(Entity.json(message.getJson()));

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new SenderException("Could not notification.");
        }
    }
}
