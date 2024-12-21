package ru.itmo.serverlessorback.domain.entity.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

public enum Role implements GrantedAuthority {
    USER("ROLE_USER"),

    ADMIN("ROLE_ADMIN");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    public static Role fromAuthority(String authority) {
        return Arrays.stream(Role.values())
                .filter(role -> role.roleName.equals(authority))
                .findFirst()
                .orElse(null);
    }
}
