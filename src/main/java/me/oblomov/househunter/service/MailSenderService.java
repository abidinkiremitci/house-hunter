package me.oblomov.househunter.service;

import me.oblomov.househunter.config.MailNotifyConfig;
import me.oblomov.househunter.config.MailServerConfig;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSenderService {

    private final MailNotifyConfig mailNotifyConfig;
    private final MailServerConfig mailServerConfig;

    private static final Logger log = LoggerFactory.getLogger(MailSenderService.class);

    public MailSenderService(MailNotifyConfig mailNotifyConfig, MailServerConfig mailServerConfig) {
        this.mailNotifyConfig = mailNotifyConfig;
        this.mailServerConfig = mailServerConfig;
    }

    public void send(Map<String, Object> mailBodyParameters) {

        final String username = mailServerConfig.getUsername();
        final String password = mailServerConfig.getPassword();

        Properties prop = new Properties();
        prop.put("mail.smtp.host", mailServerConfig.getHost());
        prop.put("mail.smtp.port",  mailServerConfig.getPort());
        prop.put("mail.smtp.auth",  mailServerConfig.isAuthorizationRequired());
        prop.put("mail.smtp.socketFactory.port", mailServerConfig.getPort());
        prop.put("mail.smtp.socketFactory.class", mailServerConfig.getFactory());

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailNotifyConfig.getSender()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mailNotifyConfig.getTOs())
            );

            message.setSubject(mailBodyParameters.get("NUMBER_OF_NEW_HOUSES") + " new houses found in " + mailBodyParameters.get("PROVINCE") + " " + mailBodyParameters.get("DISTRICT"));
            message.setContent(getBody(mailBodyParameters), "text/html; charset=utf-8");

            Transport.send(message);

        } catch (MessagingException | IOException e) {
            log.error("Error sending email. ", e);
        }
    }

    public String getBody(Map<String, Object> mailBodyParameters) throws IOException {
        String mailTemplate;
        String mailTemplateFileName = mailNotifyConfig.getMailTemplate();
        ClassPathResource classPathResource = new ClassPathResource(mailTemplateFileName);
        if(classPathResource.exists()) {
            mailTemplate = new String(classPathResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            Path mailTemplateFilePath = Path.of(mailTemplateFileName);
            if(Files.exists(mailTemplateFilePath)) {
                mailTemplate = Files.readString(mailTemplateFilePath);
            } else {
                throw new FileNotFoundException("Mail template cannot found with path: " + mailTemplateFileName);
            }
        }
        return StringSubstitutor.replace(mailTemplate, mailBodyParameters);
    }
}
