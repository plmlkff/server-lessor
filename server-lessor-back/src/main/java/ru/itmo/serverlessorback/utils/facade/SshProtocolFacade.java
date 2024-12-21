package ru.itmo.serverlessorback.utils.facade;

import ru.itmo.serverlessorback.exception.CanNotCreateConfigurationException;
import ru.itmo.serverlessorback.utils.SshConnectionUtil;
import ru.itmo.serverlessorback.utils.ProtocolCredentials;

import java.util.UUID;

public class SshProtocolFacade implements ProtocolFacade{

    public ProtocolCredentials create(ProtocolCredentials rootCredentials, Object...args) throws Exception {
        String randString = UUID.randomUUID().toString();
        String userName = randString.substring(0, 7) + "l";
        String passwd = randString.substring(9, 15);
        String preparedCommand = String.format(Commands.CREATE_USER_AND_SET_PASSWORD, userName, userName, passwd, passwd, userName);

        try {
            SshConnectionUtil.executeCommand(preparedCommand, rootCredentials);
        } catch (Exception e){
            if (!e.getMessage().contains("successfully")){
                remove(rootCredentials, userName);
                throw new CanNotCreateConfigurationException("Не удалось создать конфигурацию!");
            }
        }

        return new ProtocolCredentials(userName, passwd, rootCredentials.getHost(), rootCredentials.getPort());
    }

    public boolean remove(ProtocolCredentials rootCredentials, Object...args) {
        if (args.length == 0) return false;

        String userLogin = (String) args[0];
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
