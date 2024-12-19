package ru.itmo.serverlessorback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.serverlessorback.domain.entity.Tariff;

import java.util.UUID;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, UUID> {
}
