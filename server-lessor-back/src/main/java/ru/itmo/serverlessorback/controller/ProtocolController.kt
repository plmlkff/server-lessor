package ru.itmo.serverlessorback.controller;

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.service.LocationService
import ru.itmo.serverlessorback.service.ProtocolService
import ru.itmo.serverlessorback.utils.HttpResponse

@RestController
@RequestMapping("/api/locations")
class ProtocolController(
    val protocolService: ProtocolService
) {
    @GetMapping("")
    fun getAllLocations(): ResponseEntity<*> {
        return protocolService.findAll().toResponse()
    }

    fun Either<Throwable, *>.toResponse(): ResponseEntity<*> = fold(
        ifLeft = { error ->
            HttpResponse.unexpectedError(
                ErrorResponse("Unexpected error: ${error.message}")
            )
        },
        ifRight = HttpResponse::ok
    )
}
