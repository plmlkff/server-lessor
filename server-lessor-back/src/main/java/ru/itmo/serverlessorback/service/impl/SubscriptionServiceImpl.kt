package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.itmo.serverlessorback.controller.model.response.SubscriptionResponse
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.repository.UserRepository
import ru.itmo.serverlessorback.service.SubscriptionService
import java.util.UUID

@Service
class SubscriptionServiceImpl(
    private val userRepository: UserRepository,
) : SubscriptionService {
    override fun findByLogin(login: String): Either<Throwable, SubscriptionResponse> = Either.catch {
        val user = userRepository.findByLogin(login)
            .orElseThrow { NotFoundException("Пользователя с указанным идентификатором не существует") }
        user.subscription.takeIf {
            it.tariff != null
        }?.let {
            SubscriptionResponse.fromDomain(it)
        } ?: throw NotFoundException("У пользователя нет активной подписки")
    }

    override fun findByUserId(userId: UUID): Either<Throwable, SubscriptionResponse> = Either.catch {
        val user = userRepository.findById(userId)
            .orElseThrow { NotFoundException("Пользователя с указанным идентификатором не существует") }
        user.subscription.takeIf {
            it.tariff != null
        }?.let {
            SubscriptionResponse.fromDomain(it)
        } ?: throw NotFoundException("У пользователя нет активной подписки")
    }
}
