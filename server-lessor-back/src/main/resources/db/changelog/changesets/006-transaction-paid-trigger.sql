CREATE OR REPLACE FUNCTION on_transaction_paid()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.status = 'PAID' THEN

        UPDATE subscription
        SET tariff_id       = NEW.tariff_id,
            creation_time   = NOW(),
            expiration_time = creation_time + interval '1 month'
        WHERE id = NEW.subscription_id
          AND user_id = NEW.user_id;

        UPDATE invitation
        SET status = 'DEPOSITED'
        WHERE referral_id = NEW.user_id;

    END IF;

    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

CREATE TRIGGER trg_on_transaction_paid
    AFTER UPDATE OF status
    ON transaction
    FOR EACH ROW
EXECUTE FUNCTION on_transaction_paid();
