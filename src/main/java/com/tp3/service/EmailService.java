package com.tp3.service;


import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailService {

    private final String username = "azangueleonel9@gmail.com";
    private final String password = "abhl wyrs nuqh hoau";

    public void envoyerEmail(String destinataire, String sujet, String messageTexte) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinataire)
            );
            message.setSubject(sujet);
            message.setText(messageTexte);

            Transport.send(message);

            System.out.println("📧 Email envoyé à " + destinataire);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de l'envoi de l'e-mail.");
        }
    }
}
