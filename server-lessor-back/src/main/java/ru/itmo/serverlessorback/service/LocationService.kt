package ru.itmo.serverlessorback.service

import arrow.core.Either
import ru.itmo.serverlessorback.controller.model.response.GetAllLocationsResponse

interface LocationService {
    fun findAll(): Either<Throwable, List<GetAllLocationsResponse>>
}
