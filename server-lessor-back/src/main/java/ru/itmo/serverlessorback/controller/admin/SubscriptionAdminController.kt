package ru.itmo.serverlessorback.controller.admin;

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.service.SubscriptionService
import ru.itmo.serverlessorback.utils.HttpResponse
import java.util.UUID

@RestController
@RequestMapping("/api/admin/subscriptions")
class SubscriptionAdminController(
    val subscriptionService: SubscriptionService,
) {
    @GetMapping("")
    fun getConfigurations(
        @RequestParam userId: UUID
    ): ResponseEntity<*> {
        return subscriptionService.findByUserId(userId)
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
