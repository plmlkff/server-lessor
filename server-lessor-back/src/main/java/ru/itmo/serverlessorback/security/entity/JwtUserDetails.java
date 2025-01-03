package ru.itmo.serverlessorback.security.entity;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.serverlessorback.domain.entity.User;
import ru.itmo.serverlessorback.domain.entity.UserRole;
import ru.itmo.serverlessorback.domain.entity.enums.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
public class JwtUserDetails implements UserDetails {
    private String userName;

    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles == null ? new HashSet<>() : roles;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public static JwtUserDetails fromDomain(User user) {
        JwtUserDetails userDetails = new JwtUserDetails();
        userDetails.setUserName(user.getLogin());
        userDetails.setRoles(user.getRoles().stream().map(UserRole::getName).collect(Collectors.toSet()));
        return userDetails;
    }
}
