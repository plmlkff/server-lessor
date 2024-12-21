package ru.itmo.serverlessorback.service;

import arrow.core.Either
import ru.itmo.serverlessorback.controller.model.response.TransactionResponse
import java.util.UUID

interface TransactionService {
    fun create(login: String, tariffId: UUID): Either<Throwable, TransactionResponse>

    fun pay(transactionId: UUID): Either<Throwable, TransactionResponse>

    fun findByUserId(userId: UUID): Either<Throwable, List<TransactionResponse>>

    fun findByLogin(login: String): Either<Throwable, List<TransactionResponse>>

    fun findById(transactionId: UUID): Either<Throwable, TransactionResponse>
}
