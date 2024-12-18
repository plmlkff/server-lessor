CREATE TABLE "user"
(
    "id"       UUID PRIMARY KEY,
    "login"    VARCHAR(64) NOT NULL UNIQUE,
    "password" VARCHAR(64) NOT NULL,
    "ref_code" INTEGER     NOT NULL UNIQUE
);

CREATE TABLE "user_role"
(
    "id"   UUID PRIMARY KEY,
    "name" VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE "user_to_role"
(
    "client_id" UUID NOT NULL,
    "role_id"   UUID NOT NULL
);

CREATE TABLE "server"
(
    "id"            UUID PRIMARY KEY,
    "location_id"   UUID         NOT NULL,
    "ip"            VARCHAR(64)  NOT NULL UNIQUE,
    "root_login"    VARCHAR(255) NOT NULL,
    "root_password" VARCHAR(255) NOT NULL
);

CREATE TABLE "subscription"
(
    "id"              UUID NOT NULL PRIMARY KEY,
    "tariff_id"       UUID NULL,
    "user_id"         UUID NOT NULL,
    "creation_time"   TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "expiration_time" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "auto_fund"       BOOLEAN NULL
);

CREATE TABLE "transaction"
(
    "id"              UUID        NOT NULL PRIMARY KEY,
    "user_id"         UUID        NOT NULL,
    "subscription_id" UUID        NOT NULL,
    "creation_time"   TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "amount"          FLOAT       NOT NULL,
    "status"          VARCHAR(64) NOT NULL,
    "update_time"     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "tariff_id"       UUID        NOT NULL
);

CREATE TABLE "location"
(
    "id"      UUID PRIMARY KEY,
    "country" VARCHAR(255) NOT NULL,
    "city"    VARCHAR(255) NULL
);

CREATE TABLE "configuration"
(
    "id"              UUID PRIMARY KEY,
    "login"           VARCHAR(255) NOT NULL UNIQUE,
    "password"        VARCHAR(255) NOT NULL,
    "subscription_id" UUID         NOT NULL,
    "server_id"       UUID         NOT NULL,
    "protocol_id"     UUID         NOT NULL,
    "deleted_time"    TIMESTAMP(0) WITHOUT TIME ZONE NULL
);

CREATE TABLE "tariff"
(
    "id"           UUID PRIMARY KEY,
    "name"         VARCHAR(64) NOT NULL UNIQUE,
    "config_count" BIGINT      NOT NULL,
    "price"        FLOAT       NOT NULL
);

CREATE TABLE "invitation"
(
    "referee_id"  UUID,
    "referral_id" UUID,
    "status"      VARCHAR(255) NOT NULL,

    PRIMARY KEY ("referee_id", "referral_id")
);

CREATE TABLE "protocol"
(
    "id"   UUID PRIMARY KEY,
    "type" VARCHAR(64) NOT NULL,
    "port" INTEGER     NOT NULL
);

CREATE TABLE "protocol_to_server"
(
    "protocol_id" UUID NOT NULL,
    "server_id"   UUID NOT NULL
);
