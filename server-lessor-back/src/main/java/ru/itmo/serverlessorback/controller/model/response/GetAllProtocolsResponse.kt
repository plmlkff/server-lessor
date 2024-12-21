package ru.itmo.serverlessorback.controller.model.response

import ru.itmo.serverlessorback.domain.entity.Protocol
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import java.util.UUID

data class GetAllProtocolsResponse(
    val id: UUID,
    val type: ProtocolType,
    val port: Int
) {
    companion object {
        fun fromDomain(protocol: Protocol) =
            GetAllProtocolsResponse(
                id = protocol.id,
                type = protocol.type,
                port = protocol.port,
            )
    }
}
