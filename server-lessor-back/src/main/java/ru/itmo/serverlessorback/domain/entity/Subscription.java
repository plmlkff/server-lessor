package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Subscription.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Subscription {
    public static final String TABLE_NAME = "subscription";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "creation_time")
    private Date creationTime;

    @NotNull
    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "auto_fund")
    private Boolean autoFund;

    @OneToMany(mappedBy = "subscription")
    private List<Configuration> configurations;

    @OneToMany(mappedBy = "subscription")
    private List<Transaction> transactions;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User owner;
}
