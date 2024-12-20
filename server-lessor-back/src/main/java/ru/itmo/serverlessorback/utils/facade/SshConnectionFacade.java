package ru.itmo.serverlessorback.utils.facade;

import ru.itmo.serverlessorback.utils.SshConnectionUtil;
import ru.itmo.serverlessorback.utils.SshConnectionUtil.SshCredentials;

import java.util.UUID;

public class SshConnectionFacade {

    public static SshCredentials createUser(SshCredentials rootCredentials) throws Exception {
        String randString = UUID.randomUUID().toString();
        String userName = randString.substring(0, 7);
        String passwd = randString.substring(9, 15);
        String preparedCommand = String.format(Commands.CREATE_USER_AND_SET_PASSWORD, userName, userName, passwd, passwd, userName);

        try {
            SshConnectionUtil.executeCommand(preparedCommand, rootCredentials);
        } catch (Exception e){
            if (!e.getMessage().contains("successfully")){
                removeUser(rootCredentials, userName);
                throw e;
            }
        }

        return new SshCredentials(userName, passwd, rootCredentials.getHost(), rootCredentials.getPort());
    }

    public static boolean removeUser(SshCredentials rootCredentials, String userLogin) {
        try {
            String preparedCommand = String.format(Commands.REMOVE_USER, userLogin);

            SshConnectionUtil.executeCommand(preparedCommand, rootCredentials);
        } catch (Exception e){
            if (!e.getMessage().contains(" mail spool (/var/mail")){
                return false;
            }
        }

        return true;
    }

    public interface Commands{
        String CREATE_USER_AND_SET_PASSWORD = """
                sudo useradd -m -g users -d /home/%s %s
                echo "%s\n%s\n" | sudo passwd %s
                """;

        String REMOVE_USER = """
                sudo userdel -rf %s
                """;
    }
}
