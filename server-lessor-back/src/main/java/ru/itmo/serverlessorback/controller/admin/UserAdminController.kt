package ru.itmo.serverlessorback.controller.admin;

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse
import ru.itmo.serverlessorback.service.UserService
import ru.itmo.serverlessorback.utils.HttpResponse

@RestController
@RequestMapping("/api/admin/users")
class UserAdminController(
    val userService: UserService
) {
    @GetMapping("")
    fun getAllUsers(): ResponseEntity<*> {
        return userService.findAll().toResponse()
    }

    fun Either<Throwable, *>.toResponse(): ResponseEntity<*> = fold(
        ifLeft = { error ->
            HttpResponse.unexpectedError(
                ErrorResponse("Неожиданная ошибка: ${error.message}")
            )
        },
        ifRight = HttpResponse::ok
    )
}
