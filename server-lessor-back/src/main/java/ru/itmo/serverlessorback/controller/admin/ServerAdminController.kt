package ru.itmo.serverlessorback.controller.admin

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.service.ServerService
import ru.itmo.serverlessorback.utils.HttpResponse
import java.util.UUID

@RestController
@RequestMapping("/api/admin/servers")
class ServerAdminController(
    private val serverService: ServerService,
) {
    @GetMapping("")
    fun getServersByCountryAndProtocol(
        @RequestParam(required = false) country: String?,
        @RequestParam(required = false) protocol: ProtocolType?
    ): ResponseEntity<*> {
        return serverService.findAllByCountryAndProtocol(country, protocol)
            .toResponse()
    }

    @GetMapping("/{serverId}")
    fun getServerById(
        @PathVariable serverId: UUID,
    ): ResponseEntity<*> {
        return serverService.findById(serverId)
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
