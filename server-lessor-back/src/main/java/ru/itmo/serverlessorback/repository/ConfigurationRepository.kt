package ru.itmo.serverlessorback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.serverlessorback.domain.entity.Configuration
import ru.itmo.serverlessorback.domain.entity.Subscription
import java.util.UUID

@Repository
interface ConfigurationRepository : JpaRepository<Configuration, UUID> {
    fun countBySubscriptionAndDeletedTimeIsNull(subscription: Subscription): Long

    @Query("SELECT * FROM delete_expired_subscriptions_and_configs()", nativeQuery = true)
    fun deleteExpiredSubscriptionsAndConfigs(): List<Configuration>

    @Query("""
        SELECT c FROM Configuration c
        WHERE (:serverId IS NULL OR c.server.id = :serverId)
          AND (:userId IS NULL OR c.subscription.owner.id = :userId)
          AND c.deletedTime IS NULL
    """)
    fun findAllByServerIdAndUserId(
        @Param("serverId") serverId: UUID?,
        @Param("userId") userId: UUID?
    ): List<Configuration>
}
