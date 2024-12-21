package ru.itmo.serverlessorback.controller.model.response

import ru.itmo.serverlessorback.domain.entity.Server
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import java.util.UUID

data class ServerResponse(
    val id: UUID,
    val location: LocationResponse,
    val ip: String,
    val protocols: List<ProtocolResponse>,
    val rootLogin: String,
    val rootPassword: String,
) {
    data class LocationResponse(
        val id: UUID,
        val country: String,
        val city: String? = null,
    )

    data class ProtocolResponse(
        val id: UUID,
        val type: ProtocolType,
        val port: Int
    )

    companion object {
        fun fromDomain(server: Server): ServerResponse =
            ServerResponse(
                id = server.id,
                location = LocationResponse(
                    id = server.location.id,
                    country = server.location.country,
                    city = server.location.city,
                ),
                ip = server.ip,
                protocols = server.protocols.map {
                    ProtocolResponse(
                        id = it.id,
                        type = it.type,
                        port = it.port
                    )
                },
                rootLogin = server.rootLogin,
                rootPassword = server.rootPassword,
            )
    }


}
