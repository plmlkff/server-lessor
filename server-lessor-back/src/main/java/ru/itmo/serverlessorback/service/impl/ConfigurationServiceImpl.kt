package ru.itmo.serverlessorback.service.impl

import arrow.core.Either
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.itmo.serverlessorback.controller.model.response.ConfigurationResponse
import ru.itmo.serverlessorback.domain.entity.Configuration
import ru.itmo.serverlessorback.domain.entity.Protocol
import ru.itmo.serverlessorback.domain.entity.Server
import ru.itmo.serverlessorback.domain.entity.User
import ru.itmo.serverlessorback.domain.entity.enums.ProtocolType
import ru.itmo.serverlessorback.domain.entity.enums.Role
import ru.itmo.serverlessorback.exception.ForbiddenException
import ru.itmo.serverlessorback.exception.NotFoundException
import ru.itmo.serverlessorback.repository.ConfigurationRepository
import ru.itmo.serverlessorback.repository.ServerRepository
import ru.itmo.serverlessorback.repository.UserRepository
import ru.itmo.serverlessorback.service.ConfigurationService
import ru.itmo.serverlessorback.utils.MailsUtil
import ru.itmo.serverlessorback.utils.ProtocolCredentials
import ru.itmo.serverlessorback.utils.factory.ProtocolFacadeFactory
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class ConfigurationServiceImpl(
    private val configurationRepository: ConfigurationRepository,
    private val userRepository: UserRepository,
    private val serverRepository: ServerRepository
) : ConfigurationService {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override fun create(
        login: String,
        country: String?,
        protocolType: ProtocolType,
    ): Either<Throwable, ConfigurationResponse> = Either.catch {
        val user = userRepository.findByLogin(login)
            .orElseThrow { NotFoundException("Пользователя с указанным именем не существует") }
        val subscription = user.subscription
        val currentConfigCount = configurationRepository.countBySubscriptionAndDeletedTimeIsNull(subscription)
        val tariff = subscription.tariff

        if (tariff == null || currentConfigCount >= tariff.configCount) {
            throw IllegalStateException("Превышено количество конфигураций для вашего тарифа: максимум ${tariff?.configCount ?: 0}")
        }
        if (LocalDateTime.now(ZoneOffset.UTC) > subscription?.expirationTime) {
            throw IllegalStateException("Ваша подписка истекла после ${subscription?.expirationTime}")
        }

        val server = serverRepository.findFirstByCountryAndProtocolType(
            country = country,
            protocolType = protocolType,
        ).orElseThrow { NotFoundException("Не удалось найти подходящий сервер. Попробуйте выбрать другие параметры") }

        val protocol = server.protocols.firstOrNull {
            it.type == protocolType
        } ?: throw NotFoundException("Не удалось найти подходящий сервер. Попробуйте выбрать другие параметры")

        val userCredentials = createProtocolAndSendEmail(server, protocol, user)

        val configuration = Configuration().apply {
            this.login = userCredentials.username
            this.subscription = subscription
            this.server = server
            this.protocol = protocol
        }   // TODO: отправить пароль на почту и создать и создать пользователя на сервере

        configurationRepository.save(configuration)

        ConfigurationResponse.fromDomain(configuration)
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override fun deleteById(
        login: String,
        configurationId: UUID,
    ): Either<Throwable, Unit> = Either.catch {
        val user = userRepository.findByLogin(login)
            .orElseThrow { NotFoundException("Пользователя с указанным именем не существует") }

        val configuration = configurationRepository.findById(configurationId)
            .orElseThrow { NotFoundException("Конфигурации с указанным идентификатором не существует") }

        val server = configuration.server

        val protocol = configuration.protocol

        if (configuration.subscription.owner.login != login && Role.ADMIN !in user.roles.map { it.name }) {
            throw ForbiddenException("Невозможно удалить чужую конфигурацию")
        }

        deleteProtocolByIdAndSendEmail(server, protocol, user, configuration.login)

        configuration.deletedTime = LocalDateTime.now(ZoneOffset.UTC)
        configurationRepository.save(configuration)
    }

    @Transactional(readOnly = true)
    override fun getAdminConfigurations(
        serverId: UUID?,
        userId: UUID?
    ): Either<Throwable, List<ConfigurationResponse>> = Either.catch {
        val configurations = configurationRepository.findAllByServerIdAndUserId(serverId, userId)
        configurations.map { ConfigurationResponse.fromDomain(it) }
    }

    @Transactional(readOnly = true)
    override fun getConfigurations(
        login: String,
    ): Either<Throwable, List<ConfigurationResponse>> = Either.catch {
        val user = userRepository.findByLogin(login)
            .orElseThrow { NotFoundException("Пользователя с указанным именем не существует") }
        val configurations = user.subscription.configurations
        configurations.map { ConfigurationResponse.fromDomain(it) }
    }

    fun createProtocolAndSendEmail(server: Server, protocol: Protocol, user: User): ProtocolCredentials {
        val protocolFacadeFactory = ProtocolFacadeFactory()
        val protocolFacade = protocolFacadeFactory.getFacade(protocol.type)
        val rootCredentials = ProtocolCredentials(
            server.rootLogin,
            server.rootPassword,
            server.ip,
            protocol.port
        )
        val userCredentials = protocolFacade.create(rootCredentials)

        val body = """
            Данные вашей конфигурации:
            Хост: ${userCredentials.host}
            Порт: ${userCredentials.port}
            Логин: ${userCredentials.username}
            Пароль: ${userCredentials.password}
        """.trimIndent()

        MailsUtil.sendMail("Создана новая конфигурация", body, user.login)

        return userCredentials
    }

    fun deleteProtocolByIdAndSendEmail(server: Server, protocol: Protocol, user: User, username: String) {
        val protocolFacadeFactory = ProtocolFacadeFactory()
        val protocolFacade = protocolFacadeFactory.getFacade(protocol.type)
        val rootCredentials = ProtocolCredentials(
            server.rootLogin,
            server.rootPassword,
            server.ip,
            protocol.port
        )

        val body = """
            Данные вашей конфигурации:
            Хост: ${rootCredentials.host}
            Порт: ${rootCredentials.port}
            Логин: ${username}
        """.trimIndent()

        MailsUtil.sendMail("Конфигурация удалена", body, user.login)

        protocolFacade.remove(rootCredentials, username)
    }
}
