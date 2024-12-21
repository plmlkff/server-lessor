package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.itmo.serverlessorback.domain.entity.enums.Role;

import java.util.List;
import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.UserRole.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class
UserRole {
    public static final String TABLE_NAME = "user_role";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private Role name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_to_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<User> users;
}
