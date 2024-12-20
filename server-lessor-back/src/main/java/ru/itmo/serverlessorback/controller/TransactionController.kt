package ru.itmo.serverlessorback.controller;

import arrow.core.Either
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.request.CreateTransactionRequest
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.service.TransactionService
import ru.itmo.serverlessorback.utils.HttpResponse
import java.util.UUID

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    val transactionService: TransactionService
) {
    @PostMapping("")
    fun createTransaction(
        @Valid @RequestBody createTransactionRequest: CreateTransactionRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<*> {
        return transactionService.create(userDetails.username, createTransactionRequest.tariffId)
            .toResponse()
    }

    @GetMapping("/{transactionId}/pay")
    fun payTransaction(
        @Valid @PathVariable transactionId: UUID,
    ): ResponseEntity<*> {
        return transactionService.pay(transactionId)
            .toResponse()
    }

    @GetMapping("/{transactionId}")
    fun getTransactionByTransactionId(
        @Valid @PathVariable transactionId: UUID,
    ): ResponseEntity<*> {
        return transactionService.findById(transactionId)
            .toResponse()
    }

    @GetMapping("")
    fun getTransactions(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<*> {
        return transactionService.findByLogin(userDetails.username)
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
