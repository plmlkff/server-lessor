package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table
public class Location {
    public static final String TABLE_NAME = "location";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column
    private String country;

    @Column
    private String city;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Server> servers;
}
