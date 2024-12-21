package ru.itmo.serverlessorback.controller

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.service.SubscriptionService
import ru.itmo.serverlessorback.utils.HttpResponse

@RestController
@RequestMapping("/api/subscriptions")
class SubscriptionController(
    val subscriptionService: SubscriptionService,
) {
    @GetMapping("")
    fun getConfigurations(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<*> {
        return subscriptionService.findByLogin(userDetails.username)
            .toResponse()
    }

    fun Either<Throwable, *>.toResponse(): ResponseEntity<*> = fold(
        ifLeft = { error ->
            when (error) {
                is NotFoundException -> HttpResponse.notFound(ErrorResponse(error.message))

                else -> throw error
            }
        },
        ifRight = HttpResponse::ok
    )
}
