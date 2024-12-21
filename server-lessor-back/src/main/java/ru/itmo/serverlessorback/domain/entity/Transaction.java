package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.itmo.serverlessorback.domain.entity.enums.TransactionStatus;

import java.time.LocalDateTime;
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
    private LocalDateTime creationTime;

    @NotNull
    @Column
    private Float amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;
}
