package ru.itmo.serverlessorback.controller.admin;

import arrow.core.Either
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.service.TransactionService
import ru.itmo.serverlessorback.utils.HttpResponse
import java.util.UUID

@RestController
@RequestMapping("/api/admin/transactions")
class TransactionAdminController(
    val transactionService: TransactionService
) {
    @GetMapping("")
    fun getTransactionByUserId(
        @Valid @PathParam("userId") userId: UUID,
    ): ResponseEntity<*> {
        return transactionService.findByUserId(userId)
            .toResponse()
    }

    fun Either<Throwable, *>.toResponse(): ResponseEntity<*> = fold(
        ifLeft = { error ->
            when (error) {
                is NotFoundException -> HttpResponse.notFound(ErrorResponse(error.message))
                else -> HttpResponse.unexpectedError(
                    ErrorResponse("Unexpected error: ${error.message}")
                )
            }
        },
        ifRight = HttpResponse::ok
    )
}
