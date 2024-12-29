package com.example.club_sporting_final.utils;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {

    private static final String SENDER_EMAIL = System.getenv("SENDER_EMAIL");
    private static final String SENDER_PASSWORD = "tqkr rmek cmzl spql";


    public static void sendEmail(String recipient, String subject, String messageBody) throws MessagingException {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty.");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject cannot be null or empty.");
        }
        if (messageBody == null || messageBody.isBlank()) {
            throw new IllegalArgumentException("Email message body cannot be null or empty.");
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        try {
            System.out.println("Sending email to: " + recipient); // Debugging log
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        } catch (AddressException e) {
            throw new MessagingException("Invalid recipient email address: " + recipient, e);
        }
        message.setSubject(subject);
        message.setText(messageBody);

        Transport.send(message);
    }
}
