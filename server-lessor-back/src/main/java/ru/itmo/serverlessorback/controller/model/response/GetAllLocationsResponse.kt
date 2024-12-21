package ru.itmo.serverlessorback.controller.model.response

import ru.itmo.serverlessorback.domain.entity.Location
import java.util.UUID

data class GetAllLocationsResponse(
    val id: UUID,
    val country: String,
    val city: String? = null,
) {
    companion object {
        fun fromDomain(location: Location) =
            GetAllLocationsResponse(
                id = location.id,
                country = location.country,
                city = location.city,
            )
    }
}
