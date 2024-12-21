package ru.itmo.serverlessorback.controller.model.response

import ru.itmo.serverlessorback.domain.entity.User
import ru.itmo.serverlessorback.domain.entity.enums.Role
import java.util.UUID

data class GetAllUsersResponse(
    val id: UUID,
    val login: String,
    val referralCode: Int,
    val roles: List<Role>,
) {
    companion object {
        fun fromDomain(user: User): GetAllUsersResponse =
            GetAllUsersResponse(
                id = user.id,
                login = user.login,
                referralCode = user.refCode,
                roles = user.roles.map { it.name },
            )
    }
}
