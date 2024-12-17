package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Configuration.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Configuration {
    public static final String TABLE_NAME = "configuration";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String login;

    @NotNull
    @Column
    private String password;

    @Column(name = "deleted_time")
    private Date deletedTime;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne
    @JoinColumn(name = "protocol_id")
    private Protocol protocol;
}
