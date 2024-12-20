package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.itmo.serverlessorback.controller.model.response.GetAllLocationsResponse
import ru.itmo.serverlessorback.repository.LocationRepository
import ru.itmo.serverlessorback.service.LocationService

@Service
class LocationServiceImpl(
    private val locationRepository: LocationRepository,
) : LocationService {
    override fun findAll(): Either<Throwable, List<GetAllLocationsResponse>> = Either.catch {
        locationRepository.findAll()
            .map {
                GetAllLocationsResponse.fromDomain(it)
            }
    }
}
