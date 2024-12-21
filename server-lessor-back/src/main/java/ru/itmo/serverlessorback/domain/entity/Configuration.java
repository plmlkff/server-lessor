package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Configuration.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"login", "server_id"})
        }
)
public class Configuration {
    public static final String TABLE_NAME = "configuration";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "login")
    private String login;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

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
