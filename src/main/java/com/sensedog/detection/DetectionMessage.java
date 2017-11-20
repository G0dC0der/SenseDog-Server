package com.sensedog.detection;

import com.sensedog.repository.model.SqlDetection;
import com.sensedog.repository.model.SqlMaster;
import com.sensedog.repository.model.SqlSubscriber;
import com.sensedog.communication.mail.MailMessage;

public class DetectionMessage implements MailMessage {

    private String receiver;
    private String subject;
    private String message;

    private DetectionMessage(final String receiver, final String subject, final String message) {
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

    public static MailMessage of(final SqlSubscriber subscriber, final SqlMaster master, final SqlDetection detection) {
        final String receiver = subscriber.getEmail();
        final String subject = String.format("[%s] SenseDog Detection - %s", detection.getSeverity(), master.getName());
        final String message = String.format("Hi %s!\nSenseDog service %s have detected malicious movement with severity %s in sensor %s.",
                subscriber.getName(),
                master.getName(),
                detection.getSeverity(),
                detection.getDetectionType());

        return new DetectionMessage(receiver, subject, message);
    }
}
