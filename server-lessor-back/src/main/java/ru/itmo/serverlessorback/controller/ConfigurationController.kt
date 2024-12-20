package ru.itmo.serverlessorback.controller

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.request.CreateConfigurationRequest
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.exception.ForbiddenException
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.service.ConfigurationService
import ru.itmo.serverlessorback.utils.HttpResponse
import java.util.UUID

@RestController
@RequestMapping("/api/configurations")
class ConfigurationController(
    private val configurationService: ConfigurationService
) {
    @PostMapping("")
    fun createConfiguration(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody createConfigurationRequest: CreateConfigurationRequest
    ): ResponseEntity<*> {
        return configurationService.create(
            userDetails.username,
            createConfigurationRequest.country,
            createConfigurationRequest.protocolType
        ).toResponse()
    }

    @DeleteMapping("/{configurationId}")
    fun deleteConfiguration(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable configurationId: UUID
    ): ResponseEntity<*> {
        return configurationService.deleteById(userDetails.username, configurationId)
            .toResponse()
    }

    @GetMapping("")
    fun getConfigurations(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<*> {
        return configurationService.getConfigurations(userDetails.username)
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
