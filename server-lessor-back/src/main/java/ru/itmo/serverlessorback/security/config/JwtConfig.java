package ru.itmo.serverlessorback.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.jwt")
public class JwtConfig {
    private Long lifeTime;

    private String secret;
}
