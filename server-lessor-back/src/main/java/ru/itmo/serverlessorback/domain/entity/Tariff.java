package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Tariff.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Tariff {
    public static final String TABLE_NAME = "tariff";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(name = "config_count")
    private Integer configCount;

    @NotNull
    @Column
    private Float price;

    @OneToMany(mappedBy = "tariff", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;
}
