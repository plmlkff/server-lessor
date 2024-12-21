package ru.itmo.serverlessorback.service

import arrow.core.Either
import ru.itmo.serverlessorback.controller.model.response.GetAllProtocolsResponse

interface ProtocolService {
    fun findAll(): Either<Throwable, List<GetAllProtocolsResponse>>
}
