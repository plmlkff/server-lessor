package ru.itmo.serverlessorback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ServerLessorBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerLessorBackApplication.class, args);
    }

}
