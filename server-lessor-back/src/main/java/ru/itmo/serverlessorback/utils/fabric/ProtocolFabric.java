package ru.itmo.serverlessorback.utils.fabric;

import ru.itmo.serverlessorback.exception.UnknownProtocolException;
import ru.itmo.serverlessorback.utils.enums.ProtocolType;
import ru.itmo.serverlessorback.utils.facade.ProtocolFacade;
import ru.itmo.serverlessorback.utils.facade.SshProtocolFacade;

public class ProtocolFacadeFabric {
    public ProtocolFacade getFacade(ProtocolType type) throws UnknownProtocolException {
        return switch (type){
            case SSH -> new SshProtocolFacade();
            default -> throw new UnknownProtocolException("Received unknown protocol type!");
        };
    }
}
