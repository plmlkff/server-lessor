package ru.itmo.serverlessorback.utils;

import jakarta.mail.*;
import ru.itmo.serverlessorback.utils.builder.MessageBuilder;

import java.util.List;
import java.util.Properties;

public class MailsUtil {
    public static final String LOGIN_PROPERTY_NAME = "mail.credentials.login";
    public static final String PASSWORD_PROPERTY_NAME = "mail.credentials.password";
    public static final String MAIL_PROPERTIES_PATH = "/mails/mail.properties";

    public static boolean sendMail(String subject, String body, String...recipients) throws Exception {
        var properties = loadContext();
        var session = initSession(properties);
        var builder = new MessageBuilder(session);
        var message = builder.setSubject(subject)
                .setBody(body)
                .setTo(List.of(recipients))
                .build();
        Transport.send(message);
        return true;
    }

    private static Properties loadContext() throws Exception{
        Properties properties = new Properties();
        properties.load(MailsUtil.class.getResourceAsStream(MAIL_PROPERTIES_PATH));

        return properties;
    }

    private static Session initSession(Properties properties){
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String login = properties.getProperty(LOGIN_PROPERTY_NAME);
                String password = properties.getProperty(PASSWORD_PROPERTY_NAME);
                return new PasswordAuthentication(login, password);
            }
        });
    }
}
