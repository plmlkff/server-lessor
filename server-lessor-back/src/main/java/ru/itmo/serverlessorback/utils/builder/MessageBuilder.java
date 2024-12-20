package ru.itmo.serverlessorback.utils.builder;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {
    private Message message;

    public MessageBuilder(Session session){
        message = new MimeMessage(session);
    }

    public MessageBuilder setSubject(String subject) throws MessagingException {
        message.setSubject(subject);
        return this;
    }

    public MessageBuilder setBody(String body) throws MessagingException {
        message.setText(body);
        return this;
    }

    public MessageBuilder setFrom(String from) throws MessagingException {
        message.setFrom(new InternetAddress(from));
        return this;
    }

    public MessageBuilder setTo(List<String> addressStrings) throws Exception {
        List<Address> addresses = new ArrayList<>();
        for (var address : addressStrings){
            addresses.add(new InternetAddress(address));
        }
        message.setRecipients(Message.RecipientType.TO, addresses.toArray(new Address[0]));
        return this;
    }

    public Message build(){
        return message;
    }
}
