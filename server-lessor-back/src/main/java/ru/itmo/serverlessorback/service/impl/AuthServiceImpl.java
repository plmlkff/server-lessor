package ru.itmo.serverlessorback.service.impl;


import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.serverlessorback.controller.model.response.AuthUserResponse;
import ru.itmo.serverlessorback.domain.entity.Invitation;
import ru.itmo.serverlessorback.domain.entity.Subscription;
import ru.itmo.serverlessorback.domain.entity.User;
import ru.itmo.serverlessorback.domain.entity.UserRole;
import ru.itmo.serverlessorback.domain.entity.enums.InvitationStatus;
import ru.itmo.serverlessorback.domain.entity.enums.Role;
import ru.itmo.serverlessorback.exception.AlreadyExistsException;
import ru.itmo.serverlessorback.exception.AuthenticationException;
import ru.itmo.serverlessorback.exception.NotFoundException;
import ru.itmo.serverlessorback.repository.UserRepository;
import ru.itmo.serverlessorback.repository.UserRoleRepository;
import ru.itmo.serverlessorback.security.entity.JwtUserDetails;
import ru.itmo.serverlessorback.service.AuthService;
import ru.itmo.serverlessorback.utils.HashUtil;
import ru.itmo.serverlessorback.utils.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final JwtUtil jwtUtil;

    private final HashUtil hashUtil;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public Either<Exception, AuthUserResponse> signUp(String login, String password, Integer refCode) {
        Optional<User> userOption = userRepository.findByLogin(login);
        if (userOption.isPresent()) {
            return Either.left(new AlreadyExistsException("Пользователь с указанным именем уже существует"));
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(hashUtil.hash(password));
        user.setRefCode(Math.abs(new Random().nextInt()));

        Subscription subscription = new Subscription();
        subscription.setOwner(user);
        user.setSubscription(subscription);

        UserRole userRole = userRoleRepository.findByName(Role.USER)
                .orElseThrow(() -> new IllegalStateException("Роль USER не найдена"));

        user.setRoles(List.of(userRole));
        userRepository.save(user);

        Optional<User> refereeUserOpt = userRepository.findByRefCode(refCode);
        refereeUserOpt.ifPresent(
                (refereeUser) -> {
                    Invitation invitation = new Invitation();
                    var key = new Invitation.Key(refereeUser.getId(), user.getId());
                    invitation.setReferral(user);
                    invitation.setReferee(refereeUser);
                    invitation.setStatus(InvitationStatus.REGISTERED);
                    invitation.setId(key);
                    refereeUser.getReferrals().add(invitation);
                    userRepository.save(refereeUser);
                }
        );

        String accessToken = jwtUtil.createToken(JwtUserDetails.fromDomain(user));
        return Either.right(AuthUserResponse.fromDomain(user, accessToken));
    }

    @Transactional
    @Override
    public Either<Exception, AuthUserResponse> login(String login, String password) {
        Optional<User> userOption = userRepository.findByLogin(login);
        if (userOption.isEmpty()) {
            return Either.left(new NotFoundException("Пользователь с указанным именем не найден"));
        }
        User user = userOption.get();
        if (hashUtil.check(password, user.getPassword())) {
            String accessToken = jwtUtil.createToken(JwtUserDetails.fromDomain(user));
            return Either.right(AuthUserResponse.fromDomain(user, accessToken));
        } else {
            return Either.left(new AuthenticationException("Неверный пароль"));
        }
    }
}
