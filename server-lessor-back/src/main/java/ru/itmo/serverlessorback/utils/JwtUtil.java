package ru.itmo.serverlessorback.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Either;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.itmo.serverlessorback.configuration.JwtProperties;
import ru.itmo.serverlessorback.domain.entity.enums.Role;
import ru.itmo.serverlessorback.security.entity.JwtUserDetails;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = getSigningKey();
    }

    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", String.join(",", roles));
        System.out.println(userDetails.getUsername());
        System.out.println(key.toString());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getLifeTime()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Either<JwtException, UserDetails> verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            JwtUserDetails userDetails = new JwtUserDetails();
            String rolesString = claims.getBody().get("roles", String.class);
            Set<Role> roles = stringToRoles(rolesString);

            userDetails.setUserName(claims.getBody().getSubject());
            userDetails.setRoles(roles);

            return Either.right(userDetails);
        } catch (Exception e) {
            return Either.left(new JwtException(e.getMessage()));
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Set<Role> stringToRoles(String roleString) {
        if (roleString == null) return new HashSet<>();
        return Arrays.stream(roleString.split(","))
                .map(Role::valueOf).collect(Collectors.toSet());
    }
}
