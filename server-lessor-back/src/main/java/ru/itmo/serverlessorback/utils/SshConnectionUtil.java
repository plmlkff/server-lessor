package ru.itmo.serverlessorback.utils;

import com.jcraft.jsch.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class SshConnectionUtil {
    private static final int CONNECTING_WAITING_TIME_IN_MS = 5000;
    private static final int THREAD_WAITING_TIME_IN_MS = 100;
    private static final int WAITING_LOOP_ITERATION_LIMIT = 600;

    private static final ExecutorService commandExecutor = Executors.newCachedThreadPool();

    public static String executeCommand(String command, ProtocolCredentials credentials) throws Exception {
        var outputStream = new ByteArrayOutputStream();
        var errStream = new ByteArrayOutputStream();
        var session = createAndConfigureSession(credentials);
        session.connect(CONNECTING_WAITING_TIME_IN_MS);

        var channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        channel.setOutputStream(outputStream);
        channel.setErrStream(errStream);

        sendAndWait(channel);

        if (errStream.size() != 0){
            throw new Exception(String.format("Can not execute command!\nServer error: %s", errStream));
        }

        var res = outputStream.toString();

        closeResources(session, channel);

        return res;
    }

    public static Future<String> executeCommandAsync(String command, ProtocolCredentials credentials) throws Exception {
        return commandExecutor.submit(() -> executeCommand(command, credentials));
    }

    private static Session createAndConfigureSession(ProtocolCredentials credentials) throws Exception {
        var connection = new JSch();

        var session = connection.getSession(credentials.getUsername(), credentials.getHost(), credentials.getPort());
        session.setPassword(credentials.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");

        return session;
    }

    private static void sendAndWait(Channel channel) throws Exception {
        int loopsCounter = 0;
        channel.connect();

        while (channel.isConnected() && loopsCounter++ < WAITING_LOOP_ITERATION_LIMIT) {
            Thread.sleep(THREAD_WAITING_TIME_IN_MS);
        }

    }

    private static void closeResources(Session session, Channel channel) {
        if (session != null) {
            session.disconnect();
        }
        if (channel != null) {
            channel.disconnect();
        }
    }
}
