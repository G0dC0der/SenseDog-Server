package com.sensedog.communication.transmit;

import com.sensedog.system.ServerSettings;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public class TransmitClient {

    private final String sender;
    private final String accountToken;
    private final String authToken;

    @Inject
    public TransmitClient(final ServerSettings serverSettings) {
        Twilio.init(serverSettings.getTwiloAccountToken(), serverSettings.getTwiloAccountToken());
        this.sender = serverSettings.getTwiloSender();
        this.accountToken = serverSettings.getTwiloAccountToken();
        this.authToken = serverSettings.getTwiloAuthToken();
    }

    public void text(final String text, final String receiver) {
        Message.creator(new PhoneNumber(receiver), new PhoneNumber(sender), text).create();
    }

    public void call(final String message, final String receiver) {
        //TODO: Implement
    }
}
