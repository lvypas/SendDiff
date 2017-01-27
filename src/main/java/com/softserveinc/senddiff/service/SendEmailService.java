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
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by vvinton on 1/27/2017.
 */
public class SendEmailService {

    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);

    public void sendEmail(String to, String subject, String messageText) {
        // Get system properties
        Properties emailProperties = new Properties();

        try {
            emailProperties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Properties file not found");
        }

        Session session = Session.getDefaultInstance(emailProperties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress());
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(messageText, "text/html");

            Transport tr = session.getTransport("smtp");
            tr.connect(emailProperties.getProperty("mail.smtp.host"),
                    emailProperties.getProperty("mail.smtp.user"), emailProperties.getProperty("mail.smtp.password"));
            message.saveChanges();      // don't forget this
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

            logger.info("Sent message successfully on:" + new Date());
        }catch (MessagingException mex) {
            logger.error("Sent message failed:" + mex.getMessage());
        }
    }

    public static void main( String[] args ) {
        new SendEmailService().sendEmail("vitaliy.vintonyak@gmail.com", "Email!!!", "<strong>Strongy</strong>");
    }

}
