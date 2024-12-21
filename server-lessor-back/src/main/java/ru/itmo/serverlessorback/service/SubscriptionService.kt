package ru.itmo.serverlessorback.service

import arrow.core.Either
import ru.itmo.serverlessorback.controller.model.response.SubscriptionResponse
import java.util.UUID

interface SubscriptionService {
    fun findByLogin(login: String): Either<Throwable, SubscriptionResponse>

    fun findByUserId(userId: UUID): Either<Throwable, SubscriptionResponse>
}
