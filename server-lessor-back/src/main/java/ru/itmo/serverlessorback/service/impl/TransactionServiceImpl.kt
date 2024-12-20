package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.serverlessorback.controller.model.response.TransactionResponse
import ru.itmo.serverlessorback.domain.entity.Transaction
import ru.itmo.serverlessorback.domain.entity.enums.TransactionStatus
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.repository.TariffRepository
import ru.itmo.serverlessorback.repository.TransactionRepository
import ru.itmo.serverlessorback.repository.UserRepository
import ru.itmo.serverlessorback.service.TransactionService
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class TransactionServiceImpl(
    val userRepository: UserRepository,
    val tariffRepository: TariffRepository,
    val transactionRepository: TransactionRepository,
) : TransactionService {
    private fun generatePaymentUrl(transactionId: UUID): String {
        return "/api/transactions/$transactionId"   // TODO: убрать хардкод
    }

    @Transactional
    override fun create(login: String, tariffId: UUID): Either<Throwable, TransactionResponse> = Either.catch {
        val userEntity = userRepository.findByLogin(login)
            .orElseThrow { NotFoundException("Пользователя с указанным именем не существует") }
        val tariffEntity = tariffRepository.findById(tariffId)
            .orElseThrow { NotFoundException("Тарифа с указанным идентификатором не существует") }
        val transaction = Transaction().apply {
            creator = userEntity
            this.tariff = tariffEntity
            creationTime = LocalDateTime.now(ZoneOffset.UTC)
            updateTime = creationTime
            status = TransactionStatus.NEW
            amount = tariffEntity.price
            subscription = userEntity.subscription
        }
        transactionRepository.save(transaction)

        TransactionResponse.fromDomain(
            transaction = transaction,
            paymentUrl = generatePaymentUrl(transaction.id)
        )
    }

    @Transactional
    override fun pay(transactionId: UUID): Either<Throwable, TransactionResponse> = Either.catch {
        val transaction = transactionRepository.findById(transactionId)
            .orElseThrow { NotFoundException("Транзакции с указанным идентификатором не существует") }
        transaction.apply {
            status = TransactionStatus.PAID
            updateTime = LocalDateTime.now(ZoneOffset.UTC)
        }
        transaction.subscription.apply {
            creationTime = transaction.updateTime
            expirationTime = creationTime
            tariff = transaction.tariff
        }
        transactionRepository.save(transaction)

        TransactionResponse.fromDomain(transaction)
    }

    @Transactional
    override fun findByUserId(userId: UUID): Either<Throwable, List<TransactionResponse>> = Either.catch {
        val user = userRepository.findById(userId)
            .orElseThrow { NotFoundException("Пользователя с указанным идентификатором не существует") }
        user.transactions.map {
            TransactionResponse.fromDomain(it)
        }
    }

    @Transactional
    override fun findByLogin(login: String): Either<Throwable, List<TransactionResponse>> = Either.catch {
        val user = userRepository.findByLogin(login)
            .orElseThrow { NotFoundException("Пользователя с указанным именем не существует") }
        user.transactions.map {
            TransactionResponse.fromDomain(it)
        }
    }

    @Transactional
    override fun findById(transactionId: UUID): Either<Throwable, TransactionResponse> = Either.catch {
        val transaction = transactionRepository.findById(transactionId)
            .orElseThrow { NotFoundException("Транзакции с указанным идентификатором не существует") }
        TransactionResponse.fromDomain(transaction)
    }
}
