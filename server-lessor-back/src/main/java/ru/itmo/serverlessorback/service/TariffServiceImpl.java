package ru.itmo.serverlessorback.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.serverlessorback.controller.model.response.GetAllTariffsResponse;
import ru.itmo.serverlessorback.repository.TariffRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {
    private final TariffRepository tariffRepository;

    public Either<Exception, List<GetAllTariffsResponse>> getAllTariffs() {
        try {
            return Either.right(tariffRepository.findAll()
                    .stream()
                    .map(GetAllTariffsResponse::fromDomain)
                    .toList());
        } catch (Exception e) {
            return Either.left(e);
        }
    }
}