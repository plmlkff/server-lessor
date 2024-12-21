package ru.itmo.serverlessorback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.itmo.serverlessorback.domain.entity.Subscription
import ru.itmo.serverlessorback.domain.entity.SubscriptionIdProjectionView
import java.util.UUID

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, UUID>{
    @Query("SELECT * FROM get_subscriptions_with_less_than_one_day_left()", nativeQuery = true)
    fun getSubscriptionsWithLessThanOneDayLeft() : List<SubscriptionIdProjectionView>
}
