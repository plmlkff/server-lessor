package ru.itmo.serverlessorback.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.serverlessorback.domain.Tariff;
import ru.itmo.serverlessorback.repository.TariffRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository tariffRepository;

    public Either<Exception, List<Tariff>> findAllTariffs() {
        try {
            return Either.right(tariffRepository.findAll());
        } catch (Exception e) {
            return Either.left(e);
        }
    }
}

