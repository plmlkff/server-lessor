package ru.itmo.serverlessorback.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailsUtilTest {

    @Test
    void sendMail() throws Exception {
        MailsUtil.sendMail("Hello", "World", "");
    }
}