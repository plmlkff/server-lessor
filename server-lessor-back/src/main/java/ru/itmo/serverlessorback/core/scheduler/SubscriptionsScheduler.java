package ru.itmo.serverlessorback.core.scheduler;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itmo.serverlessorback.domain.entity.Configuration;
import ru.itmo.serverlessorback.domain.entity.Subscription;
import ru.itmo.serverlessorback.domain.entity.SubscriptionIdProjectionView;
import ru.itmo.serverlessorback.repository.ConfigurationRepository;
import ru.itmo.serverlessorback.repository.SubscriptionRepository;
import ru.itmo.serverlessorback.utils.MailsUtil;
import ru.itmo.serverlessorback.utils.ProtocolCredentials;
import ru.itmo.serverlessorback.utils.factory.ProtocolFacadeFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class SubscriptionsScheduler {
    private final Logger log = LoggerFactory.getLogger(SubscriptionsScheduler.class);

    private static final long SCHEDULER_RATE_SECONDS = 10;

    private final ProtocolFacadeFactory protocolFacadeFactory = new ProtocolFacadeFactory();

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Scheduled(fixedRate = SCHEDULER_RATE_SECONDS, timeUnit = TimeUnit.SECONDS)
    @SneakyThrows
    public void checkSubscriptions() {
        try {
            checkExpiredSubscriptions();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Scheduled(cron = "00 00 09 * * *")
    public void checkOneDayLeftSubscriptions() {
        try {
            notifyOneDayLeftSubscriptionsOwners();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    private void notifyOneDayLeftSubscriptionsOwners() throws Exception {
        List<UUID> oneDayLeftSubscriptionIds = subscriptionRepository.getSubscriptionsWithLessThanOneDayLeft()
                .stream()
                .map(SubscriptionIdProjectionView::getId)
                .toList();

        for (var subscriptionId : oneDayLeftSubscriptionIds) {
            var subscription = subscriptionRepository.findById(subscriptionId).get();
            var body = String.format("""
                        Уважаемый пользователь, напоминаем, что если вы не продлите подписку до %s, ваши данные будут удалены!
                        
                        С уважением,
                        Команда server-lesser
                    """, subscription.getExpirationTime());

            var email = subscription.getOwner().getLogin();

            MailsUtil.sendMail("Ваша подписка скоро закончится", body, email);
        }
    }

    private void checkExpiredSubscriptions() throws Exception {
        List<Configuration> configurations = configurationRepository.deleteExpiredSubscriptionsAndConfigs();

        for (var configuration : configurations) {
            var facade = protocolFacadeFactory.getFacade(configuration.getProtocol().getType());
            var server = configuration.getServer();

            var rootCredentials = new ProtocolCredentials(
                    server.getRootLogin(),
                    server.getRootPassword(),
                    server.getIp(),
                    configuration.getProtocol().getPort()
            );

            facade.remove(rootCredentials, configuration.getLogin());

            var body = String.format("""
                        Данные вашей конфигурации:
                        Хост: %s
                        Порт: %s
                        Логин: %s
                    """, rootCredentials.getHost(), rootCredentials.getPort(), configuration.getLogin());

            var email = configuration.getSubscription().getOwner().getLogin();

            MailsUtil.sendMail("Конфигурация удалена", body, email);
        }
    }
}
