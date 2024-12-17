ALTER TABLE
    "user_to_role"
    ADD CONSTRAINT "user_to_role_role_id_foreign" FOREIGN KEY ("role_id") REFERENCES "user_role" ("id");
ALTER TABLE
    "user_to_role"
    ADD CONSTRAINT "user_to_role_client_id_foreign" FOREIGN KEY ("client_id") REFERENCES "user" ("id");

ALTER TABLE
    "server"
    ADD CONSTRAINT "server_location_id_foreign" FOREIGN KEY ("location_id") REFERENCES "location" ("id");

ALTER TABLE
    "subscription"
    ADD CONSTRAINT "subscription_user_id_foreign" FOREIGN KEY ("user_id") REFERENCES "user" ("id");
ALTER TABLE
    "subscription"
    ADD CONSTRAINT "subscription_tariff_id_foreign" FOREIGN KEY ("tariff_id") REFERENCES "tariff" ("id");

ALTER TABLE
    "transaction"
    ADD CONSTRAINT "transaction_tariff_id_foreign" FOREIGN KEY ("tariff_id") REFERENCES "tariff" ("id");
ALTER TABLE
    "transaction"
    ADD CONSTRAINT "transaction_subscription_id_foreign" FOREIGN KEY ("subscription_id") REFERENCES "subscription" ("id");
ALTER TABLE
    "transaction"
    ADD CONSTRAINT "transaction_user_id_foreign" FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE
    "configuration"
    ADD CONSTRAINT "configuration_protocol_id_foreign" FOREIGN KEY ("protocol_id") REFERENCES "protocol" ("id");
ALTER TABLE
    "configuration"
    ADD CONSTRAINT "configuration_server_id_foreign" FOREIGN KEY ("server_id") REFERENCES "server" ("id");
ALTER TABLE
    "configuration"
    ADD CONSTRAINT "configuration_subscription_id_foreign" FOREIGN KEY ("subscription_id") REFERENCES "subscription" ("id");

ALTER TABLE
    "invitation"
    ADD CONSTRAINT "invitation_refferal_id_foreign" FOREIGN KEY ("refferal_id") REFERENCES "user" ("id");
ALTER TABLE
    "invitation"
    ADD CONSTRAINT "invitation_refferee_id_foreign" FOREIGN KEY ("refferee_id") REFERENCES "user" ("id");

ALTER TABLE
    "protocol_to_server"
    ADD CONSTRAINT "protocol_to_server_protocol_id_foreign" FOREIGN KEY ("protocol_id") REFERENCES "protocol" ("id");