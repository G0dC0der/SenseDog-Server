package com.sensedog.communication.mail;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sensedog.system.ServerSettings;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;

@Service
public class MailClient {

    private final Email from;
    private final SendGrid sg;

    @Inject
    public MailClient(final ServerSettings serverSettings) {
        from = new Email(serverSettings.getSendgridSender());
        sg = new SendGrid(serverSettings.getSendgridKey());
    }

    public void mail(final MailMessage mailMessage) {
        mail(mailMessage.getReceiver(), mailMessage.getSubject(), mailMessage.getMessage());
    }

    public void mail(final String receiver, final String subject, final String message) {
        final Email to = new Email(receiver);
        final Content content = new Content("text/plain", message);
        final Mail mail = new Mail(from ,subject, to, content);

        final Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            sg.api(request); //TODO: Investigate error handling
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
