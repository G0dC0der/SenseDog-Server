package com.sensedog.transmit;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TransmitClient {

    private final String sender;
    private final String accountToken;
    private final String authToken;

    public TransmitClient(final String sender, final String accountToken, final String authToken) {
        Twilio.init(accountToken, authToken);
        this.sender = sender;
        this.accountToken = accountToken;
        this.authToken = authToken;
    }

    public void text(String text, String receiver) {
        Message.creator(new PhoneNumber(receiver), new PhoneNumber(sender), text).create();
    }

    public void call(String message, String receiver) {
        //TODO: Implement
    }
}
