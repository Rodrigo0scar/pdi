package com.utec.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailFacade {
    private final MailConfig mailConfig;

    public EmailFacade(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailConfig.getHost());
        properties.put("mail.smtp.port", mailConfig.getPort());
        properties.put("mail.smtp.auth", String.valueOf(mailConfig.isAuth()));
        properties.put("mail.smtp.starttls.enable", String.valueOf(mailConfig.isStarttls()));

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getUsername(), mailConfig.getPassword());
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getUsername()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + destinatario);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
