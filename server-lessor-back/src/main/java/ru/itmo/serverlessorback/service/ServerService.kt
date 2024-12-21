package ru.itmo.serverlessorback.service

import arrow.core.Either
import ru.itmo.serverlessorback.controller.model.response.ServerResponse
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import java.util.UUID

interface ServerService {
    fun findAllByCountryAndProtocol(
        country: String?,
        protocolType: ProtocolType?,
    ): Either<Throwable, List<ServerResponse>>

    fun findById(
        serverId: UUID,
    ): Either<Throwable, ServerResponse>
}
