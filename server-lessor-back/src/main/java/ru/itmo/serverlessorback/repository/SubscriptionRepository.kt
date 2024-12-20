package ru.itmo.serverlessorback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.serverlessorback.domain.entity.Subscription
import java.util.UUID

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, UUID>
