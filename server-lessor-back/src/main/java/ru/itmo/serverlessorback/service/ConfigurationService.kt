package ru.itmo.serverlessorback.service

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.itmo.serverlessorback.controller.model.response.ConfigurationResponse
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import java.util.UUID

@Service
interface ConfigurationService {
    fun create(
        login: String,
        country: String?,
        protocolType: ProtocolType,
    ): Either<Throwable, ConfigurationResponse>

    fun deleteById(
        login: String,
        configurationId: UUID,
    ): Either<Throwable, Unit>

    fun getAdminConfigurations(
        serverId: UUID?,
        userId: UUID?
    ): Either<Throwable, List<ConfigurationResponse>>

    fun getConfigurations(
        login: String,
    ): Either<Throwable, List<ConfigurationResponse>>
}
