package ru.itmo.serverlessorback.service

import arrow.core.Either
import ru.itmo.serverlessorback.controller.model.response.GetAllUsersResponse

interface UserService {
    fun findAll(): Either<Throwable, List<GetAllUsersResponse>>
}
