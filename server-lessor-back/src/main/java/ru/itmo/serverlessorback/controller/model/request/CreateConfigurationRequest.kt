package ru.itmo.serverlessorback.controller.model.request

import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType

data class CreateConfigurationRequest(
    val name: String,
    val protocolType: ProtocolType,
    val country: String? = null,
)
