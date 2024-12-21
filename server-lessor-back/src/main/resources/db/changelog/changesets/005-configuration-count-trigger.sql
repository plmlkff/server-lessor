CREATE OR REPLACE FUNCTION check_configuration_limit()
    RETURNS TRIGGER AS
$$
DECLARE
    max_config_count BIGINT;
    current_config_count BIGINT;
BEGIN
    SELECT t.config_count
    INTO max_config_count
    FROM subscription s
             JOIN tariff t ON t.id = s.tariff_id
    WHERE s.id = NEW.subscription_id;

    SELECT COUNT(*)
    INTO current_config_count
    FROM configuration c
    WHERE c.subscription_id = NEW.subscription_id
      AND c.deleted_time IS NULL;  -- учитываем только не удалённые

    IF current_config_count >= max_config_count THEN
        RAISE EXCEPTION 'Превышен лимит конфигураций для данной подписки. Максимально допустимо: %', max_config_count;
    END IF;

    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

CREATE TRIGGER trg_check_configuration_limit
    BEFORE INSERT ON configuration
    FOR EACH ROW
EXECUTE FUNCTION check_configuration_limit();
