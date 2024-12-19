package ru.itmo.serverlessorback.utils;

import com.jcraft.jsch.*;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Data
public class ShhConnectionUtil {
    public final int THREAD_WAITING_TIME_IN_MS = 100;
    public final int WAITING_LOOP_ITERATION_LIMIT = 600;

    private final ExecutorService commandExecutor = Executors.newCachedThreadPool();

    private final String username;

    private final String password;

    private final String host;

    private final int port;

    public String executeCommand(String command) throws Exception {
        var session = createAndConfigureSession();
        var outputStream = new ByteArrayOutputStream();

        var channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        channel.setOutputStream(outputStream);

        sendAndWait(channel);

        var res = outputStream.toString();

        closeResources(session, channel);

        return res;
    }

    public Future<Channel> asyncExecuteCommand(String command) throws Exception {
        var session = createAndConfigureSession();
        var outputStream = new ByteArrayOutputStream();

        var channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        channel.setOutputStream(outputStream);

        return commandExecutor.submit(() -> {
            var res = sendAndWait(channel);

            closeResources(session, channel);

            return res;
        });
    }

    private Session createAndConfigureSession() throws Exception{
        var connection = new JSch();

        var session = connection.getSession(username, host, port);
        session.setPassword(password);
        return session;

    }

    private Channel sendAndWait(Channel channel) throws Exception {
        int loopsCounter = 0;
        channel.connect();

        while (channel.isConnected() && loopsCounter++ < WAITING_LOOP_ITERATION_LIMIT){
            Thread.sleep(THREAD_WAITING_TIME_IN_MS);
        }

        return channel;
    }

    private void closeResources(Session session, Channel channel){
        if (session != null){
            session.disconnect();
        }
        if (channel != null){
            channel.disconnect();
        }
    }
}
