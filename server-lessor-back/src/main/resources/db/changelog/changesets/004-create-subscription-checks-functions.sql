CREATE OR REPLACE FUNCTION get_subscriptions_with_less_than_one_day_left()
    RETURNS TABLE(id INT, expiration_time TIMESTAMP) AS
$$
BEGIN
    RETURN QUERY
        SELECT *
        FROM subscription
        WHERE expiration_time <= NOW() + INTERVAL '1 day'
          AND expiration_time > NOW();
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION delete_expired_subscriptions_and_configs()
    RETURNS TABLE(id uuid, login varchar, deleted_time timestamp, server_id uuid, subscription_id uuid, protocol_id uuid) AS
$$
BEGIN
    -- Устанавливаем tariff_id = NULL для истекших подписок
    UPDATE subscription
    SET tariff_id = NULL
    WHERE subscription.id IN (SELECT subscription.id
                              FROM subscription
                              WHERE subscription.expiration_time <= NOW() AND subscription.tariff_id IS NOT NULL);

    -- Устанавливаем deleted_time для всех связанных конфигураций

    RETURN QUERY
    UPDATE configuration
    SET deleted_time = NOW()
    WHERE configuration.deleted_time IS NULL
    AND configuration.subscription_id IN (SELECT subscription.id
                                          FROM subscription
                                          WHERE subscription.expiration_time <= NOW() AND subscription.tariff_id IS NULL)
    RETURNING configuration.id, configuration.login, configuration.deleted_time, configuration.server_id, configuration.subscription_id, configuration.protocol_id;
END;
$$ LANGUAGE plpgsql;
