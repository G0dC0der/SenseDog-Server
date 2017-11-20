package com.sensedog.communication.mail;

public interface MailMessage {

    String getReceiver();

    String getSubject();

    String getMessage();
}
