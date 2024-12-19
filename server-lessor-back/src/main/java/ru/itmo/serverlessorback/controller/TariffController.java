package ru.itmo.serverlessorback.controller;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.serverlessorback.controller.model.response.ErrorResponse;
import ru.itmo.serverlessorback.exception.NotFoundException;
import ru.itmo.serverlessorback.service.impl.TariffServiceImpl;
import ru.itmo.serverlessorback.utils.HttpResponse;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
public class TariffController {
    private final TariffServiceImpl tariffServiceImpl;

    @GetMapping({"/", ""})
    public ResponseEntity<?> getAllTariffs() {
        return toResponse(tariffServiceImpl.getAllTariffs());
    }

    public ResponseEntity<?> toResponse(Either<? extends Exception, ?> either) {
        return either.fold(
                error -> {
                    if (error instanceof NotFoundException) {
                        return HttpResponse.notFound(new ErrorResponse(error.getMessage()));
                    } else {
                        return HttpResponse.unexpectedError(new ErrorResponse("Unexpected error"));
                    }
                },
                HttpResponse::ok
        );
    }
}
