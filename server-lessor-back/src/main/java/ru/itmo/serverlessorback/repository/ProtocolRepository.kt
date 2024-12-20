package ru.itmo.serverlessorback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.serverlessorback.domain.entity.Protocol
import java.util.UUID

@Repository
interface ProtocolRepository : JpaRepository<Protocol, UUID>
