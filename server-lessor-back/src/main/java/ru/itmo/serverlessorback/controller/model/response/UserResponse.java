package ru.itmo.serverlessorback.controller.model.response;

import lombok.Data;
import ru.itmo.serverlessorback.domain.entity.User;
import ru.itmo.serverlessorback.domain.entity.UserRole;
import ru.itmo.serverlessorback.domain.entity.enums.Role;

import java.util.List;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String login;
    private Integer referralCode;
    private List<Role> roles;
    private String accessToken;

    public static UserResponse fromDomain(User user, String accessToken) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setLogin(user.getLogin());
        response.setRoles(user.getRoles().stream().map(UserRole::getName).toList());
        response.setAccessToken(accessToken);
        return response;
    }
}
