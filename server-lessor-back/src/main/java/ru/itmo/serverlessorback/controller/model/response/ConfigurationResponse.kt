package ru.itmo.serverlessorback.controller.model.response

import ru.itmo.serverlessorback.domain.entity.Configuration
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import java.util.UUID

data class ConfigurationResponse(
    val id: UUID,
    val serverLogin: String,
    val protocol: ProtocolResponse,
    val serverIp: String,
    val userId: UUID,
    val serverId: UUID,
) {
    data class ProtocolResponse(
        val id: UUID,
        val type: ProtocolType,
        val port: Int,
    )

    companion object {
        fun fromDomain(configuration: Configuration): ConfigurationResponse =
            ConfigurationResponse(
                id = configuration.id,
                serverLogin = configuration.login,
                protocol = ProtocolResponse(
                    id = configuration.protocol.id,
                    type = configuration.protocol.type,
                    port = configuration.protocol.port,
                ),
                serverIp = configuration.server.ip,
                userId = configuration.subscription.owner.id,
                serverId = configuration.server.id,
            )
    }
}
