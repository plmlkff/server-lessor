package ru.itmo.serverlessorback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.serverlessorback.domain.entity.Location
import java.util.UUID

@Repository
interface LocationRepository : JpaRepository<Location, UUID>
