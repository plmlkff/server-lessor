package ru.itmo.serverlessorback.controller.admin

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.exception.ForbiddenException
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.service.ConfigurationService
import ru.itmo.serverlessorback.utils.HttpResponse
import java.util.UUID

@RestController
@RequestMapping("/api/admin/configurations")
class ConfigurationAdminController(
    private val configurationService: ConfigurationService
) {
    @GetMapping("")
    fun getConfigurations(
        @RequestParam(required = false) serverId: UUID?,
        @RequestParam(required = false) userId: UUID?
    ): ResponseEntity<*> {
        return configurationService.getAdminConfigurations(serverId, userId)
            .toResponse()
    }

    fun Either<Throwable, *>.toResponse(): ResponseEntity<*> = fold(
        ifLeft = { error ->
            when (error) {
                is NotFoundException -> HttpResponse.notFound(ErrorResponse(error.message))

                is ForbiddenException -> HttpResponse.forbidden(ErrorResponse(error.message))

                is IllegalStateException -> HttpResponse.conflict(ErrorResponse(error.message))

                else -> throw error
            }
        },
        ifRight = HttpResponse::ok
    )
}
