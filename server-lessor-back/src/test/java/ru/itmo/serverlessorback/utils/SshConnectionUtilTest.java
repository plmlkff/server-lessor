package ru.itmo.serverlessorback.utils;

import org.junit.jupiter.api.Test;
import ru.itmo.serverlessorback.utils.facade.SshConnectionFacade;

import static org.junit.jupiter.api.Assertions.*;

class SshConnectionUtilTest {

    @Test
    void executeCommand() throws Exception {
        var credentials = new SshConnectionUtil.SshCredentials("", "", "", 22);
        var res = SshConnectionUtil.executeCommand("echo -n Hello world!", credentials);

        assertNotNull(res);

        assertEquals("Hello world!", res);
    }

    @Test
    void executeCommandAsync() throws Exception {
        var credentials = new SshConnectionUtil.SshCredentials("", "", "", 22);
        var res = SshConnectionUtil.executeCommandAsync("echo -n Hello world!", credentials);
        var waitedRes = res.get();

        assertNotNull(res);
        assertNotNull(waitedRes);

        assertEquals("Hello world!", waitedRes);
    }

    @Test
    void createUserViaFacadeAndRemove() throws Exception {
        var rootCredentials = new SshConnectionUtil.SshCredentials("", "", "", 22);
        var credentials = SshConnectionFacade.createUser(rootCredentials);

        assertNotNull(credentials);

        System.out.printf("Created user: %s\n", credentials.getUsername());

        assertTrue(SshConnectionFacade.removeUser(rootCredentials, credentials.getUsername()));

        System.out.printf("Removed user: %s\n", credentials.getUsername());
    }
}