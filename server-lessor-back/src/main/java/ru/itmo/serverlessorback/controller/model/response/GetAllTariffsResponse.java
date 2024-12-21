package ru.itmo.serverlessorback.controller.model.response;

import lombok.Data;
import ru.itmo.serverlessorback.domain.entity.Tariff;

import java.util.UUID;

@Data
public class GetAllTariffsResponse {
    private UUID id;
    private String name;
    private Float price;
    private Integer configCount;

    public static GetAllTariffsResponse fromDomain(Tariff tariff) {
        GetAllTariffsResponse response = new GetAllTariffsResponse();
        response.setId(tariff.getId());
        response.setName(tariff.getName());
        response.setPrice(tariff.getPrice());
        response.setConfigCount(tariff.getConfigCount());
        return response;
    }
}
