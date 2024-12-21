package ru.itmo.serverlessorback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.serverlessorback.domain.entity.Server
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import java.util.Optional
import java.util.UUID


@Repository
interface ServerRepository : JpaRepository<Server, UUID> {
    @Query(
        """
        SELECT DISTINCT s FROM Server s
        JOIN s.protocols p
        WHERE (:country IS NULL OR s.location.country = :country)
          AND (p.type = :protocolType)
        """
    )
    fun findFirstByCountryAndProtocolType(
        @Param("country") country: String?,
        @Param("protocolType") protocolType: ProtocolType
    ): Optional<Server>

    @Query(
        """
        SELECT s FROM Server s
        JOIN s.protocols p
        WHERE (:country IS NULL OR s.location.country = :country)
            AND (:type IS NULL OR p.type = :type)
        """
    )
    fun findAllByCountryAndProtocolType(
        @Param("country") country: String?,
        @Param("type") type: ProtocolType?
    ): List<Server>
}
