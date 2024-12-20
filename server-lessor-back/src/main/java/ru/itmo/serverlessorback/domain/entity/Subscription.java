package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
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

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(name = "auto_fund")
    private Boolean autoFund = false;

    @OneToMany(mappedBy = "subscription")
    private List<Configuration> configurations;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
