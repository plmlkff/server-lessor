package ru.itmo.serverlessorback.service;

import io.vavr.control.Either;
import ru.itmo.serverlessorback.controller.model.response.GetAllTariffsResponse;

import java.util.List;

public interface TariffService {
    Either<Exception, List<GetAllTariffsResponse>> getAllTariffs();
}
