package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.itmo.serverlessorback.domain.entity.enums.TransactionStatus;

import java.util.Date;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Transaction.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Transaction {
    public static final String TABLE_NAME = "transaction";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "creation_time")
    private Date creationTime;

    @NotNull
    @Column
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "update_time")
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;
}
