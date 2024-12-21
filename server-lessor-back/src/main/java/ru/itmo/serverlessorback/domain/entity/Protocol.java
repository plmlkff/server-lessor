package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType;

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
    @Enumerated(EnumType.STRING)
    private ProtocolType type;

    @NotNull
    @Column
    @Max(65535)
    @Min(1)
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
