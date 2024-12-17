package ru.itmo.serverlessorback.controller.model.response;

import lombok.Data;

import java.util.UUID;

@Data
class GetTariffByIdResponse {
    private UUID id;
    private String name;
    private Integer price;
    private Integer configurationCount;
}
