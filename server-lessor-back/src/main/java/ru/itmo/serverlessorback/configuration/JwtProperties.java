package ru.itmo.serverlessorback.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.jwt")
public class JwtProperties {
    private Long lifeTime;
    private String secret;
}
