package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.itmo.serverlessorback.controller.model.response.ServerResponse
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.repository.ServerRepository
import ru.itmo.serverlessorback.service.ServerService
import java.util.UUID

@Service
class ServerServiceImpl(
    private val serverRepository: ServerRepository,
) : ServerService {
    override fun findAllByCountryAndProtocol(
        country: String?,
        protocolType: ProtocolType?,
    ): Either<Throwable, List<ServerResponse>> = Either.catch {
        val servers = serverRepository.findAllByCountryAndProtocolType(country, protocolType)
        servers.map {
            ServerResponse.fromDomain(it)
        }
    }

    override fun findById(serverId: UUID): Either<Throwable, ServerResponse> = Either.catch {
        val server = serverRepository.findById(serverId)
            .orElseThrow { NotFoundException("Сервера с указанным идентификатором не существует") }
        ServerResponse.fromDomain(server)
    }
}
