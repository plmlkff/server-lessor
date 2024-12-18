package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Server.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Server {
    public static final String TABLE_NAME = "server";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String ip;

    @NotNull
    @Column(name = "root_login", unique = true)
    private String rootLogin;

    @Column(name = "root_password")
    private String rootPassword;

    @ManyToOne
    @JoinColumn
    private Location location;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "protocol_to_server",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "protocol_id")
    )
    private List<Protocol> protocols;

    @OneToMany(mappedBy = "server")
    private List<Configuration> configurations;
}
