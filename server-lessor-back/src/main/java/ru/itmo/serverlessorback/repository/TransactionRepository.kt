package ru.itmo.serverlessorback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.serverlessorback.domain.entity.Transaction
import java.util.UUID

@Repository
interface TransactionRepository : JpaRepository<Transaction, UUID>
