package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Protocol.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Protocol {
    public static final String TABLE_NAME = "protocol";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String type;

    @NotNull
    @Column
    private Integer port;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "protocol_to_server",
            joinColumns = @JoinColumn(name = "protocol_id"),
            inverseJoinColumns = @JoinColumn(name = "server_id")
    )
    private List<Server> servers;

    @OneToMany(mappedBy = "protocol", fetch = FetchType.LAZY)
    private List<Configuration> configurations;
}
