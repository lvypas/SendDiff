package com.softserveinc.senddiff.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by vvinton on 1/27/2017.
 */
public class SendEmailService {

    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);

    public void sendEmail(String subject, String messageText) {
        Session session = Session.getDefaultInstance(AppProperties.getProps());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress());
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(AppProperties.getProps().getProperty("senddiff.to.email")));
            message.setSubject(subject);
            message.setContent(messageText, "text/html");

            Transport tr = session.getTransport("smtp");
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

    public static void main(String[] args) {
        new SendEmailService().sendEmail("Email!!!", "<strong>Strongy</strong>");
    }

}
