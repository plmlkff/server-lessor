package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.itmo.serverlessorback.controller.model.response.GetInvitationsResponse
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.repository.UserRepository
import ru.itmo.serverlessorback.service.InvitationService

@Service
class InvitationServiceImpl(
    private val userRepository: UserRepository,
) : InvitationService {
    override fun findAllByLogin(login: String): Either<Throwable, List<GetInvitationsResponse>> = Either.catch {
        val user = userRepository.findByLogin(login)
            .orElseThrow { NotFoundException("Пользователя с указанным идентификатором не существует") }
        user.referrals.map {
            GetInvitationsResponse.fromDomain(it)
        }
    }
}
