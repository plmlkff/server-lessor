CREATE TABLE "users"
(
    "id"       UUID PRIMARY KEY,
    "login"    TEXT    NOT NULL UNIQUE,
    "password" TEXT    NOT NULL,
    "ref_code" INTEGER NOT NULL UNIQUE
);

CREATE TABLE "user_role"
(
    "id"   UUID PRIMARY KEY,
    "name" TEXT NOT NULL UNIQUE
);

CREATE TABLE "user_to_role"
(
    "client_id" UUID NOT NULL,
    "role_id"   UUID NOT NULL
);

CREATE TABLE "server"
(
    "id"            UUID PRIMARY KEY,
    "location_id"   UUID NOT NULL,
    "ip"            TEXT NOT NULL UNIQUE,
    "root_login"    TEXT NOT NULL,
    "root_password" TEXT NOT NULL
);

CREATE TABLE "subscription"
(
    "id"              UUID                           NOT NULL PRIMARY KEY,
    "tariff_id"       UUID                           NULL,
    "user_id"         UUID                           NOT NULL,
    "creation_time"   TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "expiration_time" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "auto_fund"       BOOLEAN                        NULL
);

CREATE TABLE "transaction"
(
    "id"              UUID                           NOT NULL PRIMARY KEY,
    "user_id"         UUID                           NOT NULL,
    "subscription_id" UUID                           NOT NULL,
    "creation_time"   TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "amount"          FLOAT                          NOT NULL,
    "status"          TEXT                           NOT NULL,
    "update_time"     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "tariff_id"       UUID                           NOT NULL
);

CREATE TABLE "location"
(
    "id"      UUID PRIMARY KEY,
    "country" TEXT NOT NULL,
    "city"    TEXT NULL
);

CREATE TABLE "configuration"
(
    "id"              UUID PRIMARY KEY,
    "login"           TEXT                           NOT NULL UNIQUE,
    "password"        TEXT                           NOT NULL,
    "subscription_id" UUID                           NOT NULL,
    "server_id"       UUID                           NOT NULL,
    "protocol_id"     UUID                           NOT NULL,
    "deleted_time"    TIMESTAMP(0) WITHOUT TIME ZONE NULL
);

CREATE TABLE "tariff"
(
    "id"           UUID PRIMARY KEY,
    "name"         TEXT   NOT NULL UNIQUE,
    "config_count" BIGINT NOT NULL,
    "price"        FLOAT  NOT NULL
);

CREATE TABLE "invitation"
(
    "referee_id"  UUID,
    "referral_id" UUID,
    "status"      TEXT NOT NULL,

    PRIMARY KEY ("referee_id", "referral_id")
);

CREATE TABLE "protocol"
(
    "id"   UUID PRIMARY KEY,
    "type" TEXT    NOT NULL,
    "port" INTEGER NOT NULL
);

CREATE TABLE "protocol_to_server"
(
    "protocol_id" UUID NOT NULL,
    "server_id"   UUID NOT NULL
);
