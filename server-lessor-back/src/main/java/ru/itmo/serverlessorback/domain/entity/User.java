package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.User.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "user";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true, length = 64)
    private String login;

    @NotNull
    @Column(length = 64)
    private String password;

    @NotNull
    @Column(name = "ref_code", unique = true)
    private Integer refCode;

    @OneToMany(mappedBy = "referee", cascade = CascadeType.ALL)
    private List<Invitation> referrals;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
