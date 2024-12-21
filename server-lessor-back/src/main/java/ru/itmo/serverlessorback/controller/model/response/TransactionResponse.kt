package ru.itmo.serverlessorback.controller.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import ru.itmo.serverlessorback.domain.entity.Transaction
import ru.itmo.serverlessorback.domain.entity.enums.TransactionStatus
import java.time.LocalDateTime
import java.util.UUID


@JsonInclude(JsonInclude.Include.NON_NULL)
data class TransactionResponse(
    val id: UUID,
    val amount: Float,
    val creationTime: LocalDateTime,
    val updateTime: LocalDateTime,
    val status: TransactionStatus,
    val tariffName: String,
    val paymentUrl: String? = null
) {
    companion object {
        fun fromDomain(
            transaction: Transaction,
            paymentUrl: String? = null
        ): TransactionResponse =
            TransactionResponse(
                id = transaction.id,
                amount = transaction.amount,
                creationTime = transaction.creationTime,
                updateTime = transaction.updateTime,
                status = transaction.status,
                tariffName = transaction.tariff.name,
                paymentUrl = paymentUrl,
            )
    }
}
