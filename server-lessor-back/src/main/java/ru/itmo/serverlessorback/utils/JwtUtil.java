package ru.itmo.serverlessorback.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Either;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.itmo.serverlessorback.domain.entity.enums.Role;
import ru.itmo.serverlessorback.security.config.JwtConfig;
import ru.itmo.serverlessorback.security.entity.JwtUserDetails;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final String SECRET_KEY;

    private final Long TOKEN_LIFE_TIME;

    private final Key key;

    public JwtUtil(JwtConfig jwtConfig) {
        this.TOKEN_LIFE_TIME = jwtConfig.getLifeTime();
        this.SECRET_KEY = jwtConfig.getSecret();
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", String.join(",", roles));

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Добавляем subject (имя пользователя)
                .setIssuedAt(new Date()) // Устанавливаем дату выпуска
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIFE_TIME)) // Устанавливаем время жизни токена
                .signWith(key) // Подписываем токен с помощью секретного ключа
                .compact();
    }

    public Either<JwtException, UserDetails> verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key) // Устанавливаем секретный ключ для проверки подписи
                    .build()
                    .parseClaimsJws(token); // Парсим и проверяем токен

            JwtUserDetails userDetails = new JwtUserDetails();
            String rolesString = claims.getBody().get("roles", String.class);
            Set<Role> roles = stringToRoles(rolesString);

            userDetails.setUserName(claims.getBody().getSubject());
            userDetails.setRoles(roles);

            return Either.right(userDetails);
        } catch (Exception e){
            return Either.left(new JwtException(e.getMessage()));
        }
    }

    private Set<Role> stringToRoles(String roleString) {
        if (roleString == null) return new HashSet<>();
        return Arrays.stream(roleString.split(","))
                .map(Role::valueOf).collect(Collectors.toSet());
    }
}
