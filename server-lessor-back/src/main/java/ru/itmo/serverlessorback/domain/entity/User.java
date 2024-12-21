package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.User.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String login;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column(name = "ref_code", unique = true)
    private Integer refCode;

    @OneToMany(mappedBy = "referee", cascade = CascadeType.ALL)
    private List<Invitation> referrals;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_to_role",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRole> roles;

    @NotNull
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Subscription subscription;
}
