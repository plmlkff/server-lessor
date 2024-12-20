package ru.itmo.serverlessorback.utils.facade;

import ru.itmo.serverlessorback.utils.ProtocolCredentials;

public interface ProtocolFacade {
    ProtocolCredentials create(ProtocolCredentials rootCredentials, Object...args) throws Exception;

    boolean remove(ProtocolCredentials rootCredentials, Object...args);
}
