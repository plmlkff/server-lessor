package ru.itmo.serverlessorback.service

import arrow.core.Either
import ru.itmo.serverlessorback.controller.model.response.GetInvitationsResponse

interface InvitationService {
    fun findAllByLogin(login: String): Either<Throwable, List<GetInvitationsResponse>>
}
