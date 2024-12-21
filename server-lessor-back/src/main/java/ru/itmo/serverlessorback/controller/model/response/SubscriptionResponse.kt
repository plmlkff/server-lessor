package ru.itmo.serverlessorback.controller.model.response

import ru.itmo.serverlessorback.domain.entity.Subscription
import java.time.LocalDateTime
import java.util.UUID

data class SubscriptionResponse(
    val id: UUID,
    val tariffName: String,
    val expirationTime: LocalDateTime,
    val creationTime: LocalDateTime,
) {
    companion object {
        fun fromDomain(subscription: Subscription): SubscriptionResponse =
            SubscriptionResponse(
                id = subscription.id,
                tariffName = subscription.tariff.name,
                expirationTime = subscription.expirationTime,
                creationTime = subscription.creationTime,
            )
    }
}
