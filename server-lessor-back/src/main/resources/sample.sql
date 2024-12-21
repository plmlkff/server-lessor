-- -------------------------------------------------------------------------
-- 0. Убедимся, что в базе подключено расширение pgcrypto (для gen_random_uuid).
--    Если расширение не установлено, раскомментируйте следующие строки (при наличии прав суперпользователя):
CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- -------------------------------------------------------------------------

---------------------------------------------------------------------------
-- 1. Наполним user_role (роли для пользователей)
---------------------------------------------------------------------------
INSERT INTO user_role (id, name)
VALUES
    (gen_random_uuid(), 'ADMIN'),
    (gen_random_uuid(), 'USER'),
    (gen_random_uuid(), 'MANAGER');

---------------------------------------------------------------------------
-- 2. Сгенерируем множество пользователей (таблица users)
--    Сразу 50 пользователей
---------------------------------------------------------------------------
DO $$
DECLARE
    i INT;
BEGIN
    FOR i IN 1..50 LOOP
        INSERT INTO users (id, login, password, ref_code)
        VALUES (
            gen_random_uuid(),
            'user_' || i,
            encode(digest('pass_' || i, 'sha512'), 'hex'),
            i * 100
        );
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 3. Привяжем каждому пользователю случайную роль (или несколько).
--    Для демонстрации назначим каждому пользователю одну роль, но можно рандомить и две.
---------------------------------------------------------------------------
DO $$
DECLARE
    usr RECORD;
    rand_role_id UUID;
BEGIN
    FOR usr IN (SELECT id FROM users) LOOP
        -- Случайным образом возьмём роль из user_role
        SELECT id INTO rand_role_id
          FROM user_role
         ORDER BY random()
         LIMIT 1;
         
        INSERT INTO user_to_role(client_id, role_id)
        VALUES (usr.id, rand_role_id);
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 4. Заполним таблицу location (страна/город)
---------------------------------------------------------------------------
DO $$
DECLARE
    i INT;
    countries TEXT[] := ARRAY['USA', 'Germany', 'France', 'Japan', 'Russia', 'Spain'];
    cities    TEXT[] := ARRAY['New York', 'Berlin', 'Paris', 'Tokyo', 'Moscow', 'Madrid'];
BEGIN
    FOR i IN 1..ARRAY_LENGTH(countries, 1) LOOP
        INSERT INTO location (id, country, city)
        VALUES (
            gen_random_uuid(),
            countries[i],
            cities[i]
        );
    END LOOP;
    -- Добавим ещё несколько рандомных
    FOR i IN 1..5 LOOP
        INSERT INTO location (id, country, city)
        VALUES (
            gen_random_uuid(),
            'Country_' || i,
            'City_' || i
        );
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 5. Заполним таблицу server (привяжем её к случайному location)
--    Создадим, например, 20 серверов.
---------------------------------------------------------------------------
DO $$
DECLARE
    i INT;
    loc_id UUID;
BEGIN
    FOR i IN 1..20 LOOP
        -- Выбираем случайную локацию
        SELECT id INTO loc_id
          FROM location
         ORDER BY random()
         LIMIT 1;
        
        INSERT INTO server (id, location_id, ip, root_login, root_password)
        VALUES (
            gen_random_uuid(),
            loc_id,
            '192.168.' || (i % 255)::TEXT || '.' || (i + 10)::TEXT,  -- условно уникальный IP
            'root_' || i,
            'rootpass_' || i
        );
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 6. Заполним таблицу tariff (несколько тарифов с разными лимитами и ценами)
---------------------------------------------------------------------------
INSERT INTO tariff (id, name, config_count, price) VALUES
    (gen_random_uuid(), 'Basic',    1,   5.99),
    (gen_random_uuid(), 'Standard', 3,  12.50),
    (gen_random_uuid(), 'Premium',  5,  20.00),
    (gen_random_uuid(), 'Ultimate', 10, 35.00);

---------------------------------------------------------------------------
-- 7. Сгенерируем подписки (subscription).
--    Скажем, каждый пользователь получит 0 или 1 или 2 подписки.
---------------------------------------------------------------------------
DO $$
DECLARE
    usr RECORD;
    tarr RECORD;
    i INT;
BEGIN
    FOR usr IN (SELECT id FROM users) LOOP
        -- Сгенерируем от 0 до 2 подписок
        FOR i IN 1..(1 + (random()*2)::INT) LOOP
            -- Выбираем случайный тариф
            SELECT * INTO tarr
              FROM tariff
             ORDER BY random()
             LIMIT 1;
             
            INSERT INTO subscription (id, tariff_id, user_id, creation_time, expiration_time, auto_fund)
            VALUES (
                gen_random_uuid(),
                tarr.id,
                usr.id,
                NOW() - (random() * INTERVAL '30 days'),  -- случайная дата создания в последние 30 дней
                NOW() + (random() * INTERVAL '90 days'),  -- истекает в течение 90 дней
                (random() > 0.5)  -- auto_fund да/нет
            );
        END LOOP;
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 8. Сгенерируем транзакции (transaction).
--    Свяжем их с подписками. Пусть у каждой подписки будет 1-3 транзакции.
---------------------------------------------------------------------------
DO $$
DECLARE
    sub  RECORD;
    i    INT;
    amt  FLOAT;
    st   TEXT;
BEGIN
    FOR sub IN (SELECT * FROM subscription) LOOP
        FOR i IN 1..(1 + (random()*2)::INT) LOOP
            -- Определим случайную сумму
            amt := (random()*50 + 5)::numeric(6,2);  -- например, от 5 до 55 долларов
            -- Выбираем случайный статус из набора [NEW, PAID, CANCELED]
            SELECT (ARRAY['NEW', 'PAID', 'CANCELED'])[1 + (random()*2)::INT] INTO st;
            
            INSERT INTO transaction (
                id, user_id, subscription_id,
                creation_time, amount, status, update_time, tariff_id
            )
            VALUES (
                gen_random_uuid(),
                sub.user_id,
                sub.id,
                NOW() - (random() * INTERVAL '20 days'),
                amt,
                st,
                NOW(),
                sub.tariff_id
            );
        END LOOP;
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 9. Заполним таблицу invitation (у некоторых пользователей будет реферал)
--    Пусть каждый 10-й пользователь кого-то пригласил.
---------------------------------------------------------------------------
DO $$
DECLARE
    usr_ref RECORD;  -- тот, кто приглашает
    usr_new RECORD;  -- приглашённый
BEGIN
    -- Выбираем каждые 10-ю запись из списка пользователей в качестве "referee"
    FOR usr_ref IN (
        SELECT u.*
          FROM users u
         WHERE (u.ref_code % 10) = 0  -- условное "каждый 10-й"
    ) LOOP
        -- Выберем случайного другого пользователя и сделаем его referral
        SELECT * INTO usr_new
          FROM users
         WHERE id <> usr_ref.id
         ORDER BY random()
         LIMIT 1;
         
        -- Добавим запись в invitation
        INSERT INTO invitation (referee_id, referral_id, status)
        VALUES (
            usr_ref.id,
            usr_new.id,
            CASE WHEN random() < 0.5 THEN 'REGISTERED' ELSE 'DEPOSITED' END
        )
        ON CONFLICT DO NOTHING;  -- на случай если уже есть такая пара
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 10. Заполним таблицу protocol (несколько типов протоколов)
---------------------------------------------------------------------------
INSERT INTO protocol (id, type, port) VALUES
    (gen_random_uuid(), 'SSH', 22),
    (gen_random_uuid(), 'SSH', 2222),
    (gen_random_uuid(), 'FTP', 21),
    (gen_random_uuid(), 'HTTPS', 443),
    (gen_random_uuid(), 'HTTP', 80);

---------------------------------------------------------------------------
-- 11. Свяжем протоколы с серверами (protocol_to_server)
--     Пусть каждый протокол доступен на случайном наборе серверов
---------------------------------------------------------------------------
DO $$
DECLARE
    prot RECORD;
    srv  RECORD;
BEGIN
    FOR prot IN (SELECT * FROM protocol) LOOP
        -- Свяжем каждый протокол с ~половиной серверов случайно
        FOR srv IN (SELECT * FROM server ORDER BY random() LIMIT 10) LOOP
            INSERT INTO protocol_to_server (protocol_id, server_id)
            VALUES (prot.id, srv.id)
            ON CONFLICT DO NOTHING; -- чтобы при повторных запусках не дублировать
        END LOOP;
    END LOOP;
END;
$$;

---------------------------------------------------------------------------
-- 12. Сгенерируем конфигурации (configuration).
--     Привяжем их к подпискам и серверам, выберем случайные протоколы, логины.
--     Сгенерируем, скажем, до 3 конфигураций на подписку.
---------------------------------------------------------------------------
DO $$
DECLARE
    sub RECORD;
    i INT;
    srv_id UUID;
    prot_id UUID;
    cfg_limit INT;
BEGIN
    FOR sub IN (SELECT * FROM subscription) LOOP

        SELECT t.config_count
          INTO cfg_limit
          FROM tariff t
         WHERE t.id = sub.tariff_id;

        -- От 0 до 3 конфигураций
        FOR i IN 1..cfg_limit LOOP
            -- Выберем случайный server, у которого есть хоть один протокол
            SELECT ps.server_id, ps.protocol_id
              INTO srv_id, prot_id
              FROM protocol_to_server ps
             ORDER BY random()
             LIMIT 1;
            
            INSERT INTO configuration (
                id, login, subscription_id, server_id, protocol_id, deleted_time
            )
            VALUES (
                gen_random_uuid(),
                'config_login_' || (random()*10000)::INT,
                sub.id,
                srv_id,
                prot_id,
                NULL  -- пока не удалено
            )
            ON CONFLICT DO NOTHING;  -- если уже была такая пара (login, server_id)
        END LOOP;
    END LOOP;
END;
$$;

-- -------------------------------------------------------------------------
-- На этом скрипт завершён. В итоге мы получили:
--   • 50 пользователей (с ролями)
--   • Несколько локаций и 20 серверов
--   • Несколько тарифов
--   • Подписки для пользователей
--   • Транзакции, приглашения, конфигурации и т.д.
--
-- Для наглядности можно выполнить SELECT из каждой таблицы и убедиться,
-- что данные действительно сгенерировались.
-- -------------------------------------------------------------------------
