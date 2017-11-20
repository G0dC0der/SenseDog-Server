package com.sensedog.communication.cloud;

import com.sensedog.system.ServerSettings;
import com.sun.xml.internal.ws.client.SenderException;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class CloudClient {

    private final String apiKey;
    private final Client client;

    @Inject
    public CloudClient(final ServerSettings serverSettings) {
        this.apiKey = serverSettings.getCloudKey();
        this.client = ClientBuilder.newClient();
    }

    public void send(final CloudMessage<?> message) {
        final Response response = client.target("https://fcm.googleapis.com/fcm/send")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "key=" + apiKey)
                .header("Content-Type", "application/json")
                .post(Entity.json(message.getJson()));

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new SenderException("Could not notification.");
        }
    }
}
