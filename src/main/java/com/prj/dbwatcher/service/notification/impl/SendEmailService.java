package com.prj.dbwatcher.service.notification.impl;

import com.prj.dbwatcher.model.ComparedObject;
import com.prj.dbwatcher.service.notification.INotificationService;
import com.prj.dbwatcher.utils.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

/**
 * Created by vvinton on 1/27/2017.
 */
public class SendEmailService implements INotificationService {

    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);

    public void sendEmail(String subject, String messageText) {

    }

    public void notify(List<ComparedObject> results) {
        Session session = Session.getDefaultInstance(AppProperties.getProps());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress());
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(AppProperties.getProps().getProperty("notification.to.email")));
            message.setSubject("" + results.size() + " updates on db sync check found, run on:" + new Date());
            message.setContent(getMessageBody(results), "text/html");

            Transport tr = session.getTransport(AppProperties.getProps().getProperty("mail.transport.protocol"));
            tr.connect(AppProperties.getProps().getProperty("mail.smtp.host"),
                    AppProperties.getProps().getProperty("mail.smtp.user"),
                    AppProperties.getProps().getProperty("mail.smtp.password"));
            message.saveChanges();      // don't forget this
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

            logger.info("Sent message successfully on:" + new Date());
        } catch (MessagingException mex) {
            logger.error("Sent message failed:" + mex.getMessage());
        }
    }

    public String getMessageBody(List<ComparedObject> results) {
        String body = "<table border=\"1\">";
        body += "<th>";
            body += "<tr>";
                body += "<td>id</td>";
                body += "<td>oldValue</td>";
                body += "<td>newValue</td>";
            body += "</tr>";
        body += "</th>";
        for (ComparedObject obj: results) {
            body += "<tr>";
                body += ("<td>" + obj.key + "</td>");
                body += ("<td>" + obj.oldValue + "</td>");
                body += ("<td>" + obj.newValue + "</td>");
            body += "</tr>";
        }
        body += "</table>";
        return body;
    }
}
