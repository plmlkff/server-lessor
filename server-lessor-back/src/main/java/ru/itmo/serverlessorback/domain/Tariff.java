package ru.itmo.serverlessorback.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {
    private UUID id;

    private String name;

    private Integer price;

    private Integer configurationCount;
}
