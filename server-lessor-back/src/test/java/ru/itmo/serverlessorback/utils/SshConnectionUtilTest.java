package ru.itmo.serverlessorback.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SshConnectionUtilTest {

    @Test
    void executeCommand() throws Exception {
        var credentials = new SshConnectionUtil.SshCredentials("", "", "se.ifmo.ru", 2222);
        var res = SshConnectionUtil.executeCommand("echo -n Hello world!", credentials);

        assertNotNull(res);

        assertEquals("Hello world!", res);
    }

    @Test
    void executeCommandAsync() throws Exception {
        var credentials = new SshConnectionUtil.SshCredentials("", "", "se.ifmo.ru", 2222);
        var res = SshConnectionUtil.executeCommandAsync("echo -n Hello world!", credentials);
        var waitedRes = res.get();

        assertNotNull(res);
        assertNotNull(waitedRes);

        assertEquals("Hello world!", waitedRes);
    }
}