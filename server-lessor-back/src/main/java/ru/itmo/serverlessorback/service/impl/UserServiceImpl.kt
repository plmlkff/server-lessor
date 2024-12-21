package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import ru.itmo.serverlessorback.controller.model.response.GetAllUsersResponse
import ru.itmo.serverlessorback.repository.UserRepository
import ru.itmo.serverlessorback.service.UserService

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {
    override fun findAll(): Either<Throwable, List<GetAllUsersResponse>> = Either.catch {
        userRepository.findAll()
            .map {
                GetAllUsersResponse.fromDomain(it)
            }
    }
}
