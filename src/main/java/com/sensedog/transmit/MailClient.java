package com.sensedog.transmit;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import java.io.IOException;
import java.io.UncheckedIOException;

public class MailClient {

    private final Email from;
    private final SendGrid sg;

    public MailClient(final String sender, final String apiKey) {
        from = new Email(sender);
        sg = new SendGrid(apiKey);
    }

    public void mail(MailMessage mailMessage) {
        mail(mailMessage.getReceiver(), mailMessage.getSubject(), mailMessage.getMessage());
    }

    public void mail(String receiver, String subject, String message) {
        Email to = new Email(receiver);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from ,subject, to, content);

        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            sg.api(request); //TODO: Investigate error handling
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
