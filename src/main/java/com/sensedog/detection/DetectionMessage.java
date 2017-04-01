package com.sensedog.detection;

import com.sensedog.repository.entry.Detection;
import com.sensedog.repository.entry.MasterUser;
import com.sensedog.repository.entry.Service;
import com.sensedog.repository.entry.Subscriber;
import com.sensedog.transmit.MailMessage;

public class DetectionMessage implements MailMessage {

    private String subject;
    private String message;
    private String receiver;

    private DetectionMessage(String subject, String message, String receiver) {
        this.subject = subject;
        this.message = message;
        this.receiver = receiver;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static MailMessage of(Subscriber subscriber, MasterUser admin, Detection... detections) {
        final String receiver = subscriber.getEmail();
//        final String subject = String.format("[%s] SenseDog Detection - %s", detection.getSeverity(), )

        return null;
    }
}
