package ru.itmo.serverlessorback.controller.model.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateTransactionRequest(
    @NotNull
    val tariffId: UUID,
)
