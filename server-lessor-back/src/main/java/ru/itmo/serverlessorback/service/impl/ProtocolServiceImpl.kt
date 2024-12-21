package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.itmo.serverlessorback.controller.model.response.GetAllProtocolsResponse
import ru.itmo.serverlessorback.repository.ProtocolRepository
import ru.itmo.serverlessorback.service.ProtocolService

@Service
class ProtocolServiceImpl(
    private val protocolRepository: ProtocolRepository,
) : ProtocolService {
    override fun findAll(): Either<Throwable, List<GetAllProtocolsResponse>> = Either.catch {
        protocolRepository.findAll()
            .map {
                GetAllProtocolsResponse.fromDomain(it)
            }
    }
}
