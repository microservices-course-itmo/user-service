CREATE TABLE companies
(
    "id"   bigint PRIMARY KEY,
    "name" varchar
);

CREATE TABLE cities
(
    "id"   bigint PRIMARY KEY,
    "name" varchar
);

CREATE TABLE user_roles
(
    "id"   bigserial PRIMARY KEY,
    "name" varchar
);

CREATE TABLE users
(
    "id"           bigserial PRIMARY KEY,
    "role_id"      bigint,
    "city_id"      bigint,
    "company_id"   bigint,
    "phone_number" varchar,
    "birth_date"   date,
    "sex"          int,
    "email"        varchar,
    "activated"    boolean,
    "password"     varchar,
    "created_at"   timestamp,


    CONSTRAINT fk_user_city_id FOREIGN KEY ("city_id") REFERENCES cities ("id"),
    CONSTRAINT fk_user_company_id FOREIGN KEY ("company_id") REFERENCES companies ("id"),
    CONSTRAINT fk_user_role_id FOREIGN KEY ("role_id") REFERENCES user_roles ("id")
);

CREATE TABLE catalogs
(
    "id"          bigserial PRIMARY KEY,
    "user_id"     bigint,
    "description" varchar,

    CONSTRAINT fk_catalog_user_id FOREIGN KEY ("user_id") REFERENCES users ("id")
);

CREATE TABLE items
(
    "id"          bigserial PRIMARY KEY,
    "name"        varchar,
    "description" varchar
);

CREATE TABLE list_catalogs
(
    "catalog_id" bigint,
    "item_id"    bigint,

    CONSTRAINT fk_list_catalog_catalog_id FOREIGN KEY ("catalog_id") REFERENCES catalogs ("id"),
    CONSTRAINT fk_list_catalog_item_id FOREIGN KEY ("item_id") REFERENCES items ("id")
);

CREATE TABLE tokens
(
    "user_id"            bigint,
    "token_access"       varchar,
    "token_refresh"      varchar,
    "token_refresh_date" varchar,

    CONSTRAINT fk_token_user_id FOREIGN KEY ("user_id") REFERENCES users ("id")
);

CREATE TABLE list_favorites
(
    "user_id" bigint,
    "item_id" bigint,

    CONSTRAINT fk_list_favorite_user_id FOREIGN KEY ("user_id") REFERENCES users ("id"),
    CONSTRAINT fk_list_favorite_item_id FOREIGN KEY ("item_id") REFERENCES items ("id")
);

CREATE TABLE list_subscriptions
(
    "user_id" bigint,
    "item_id" bigint,

    CONSTRAINT fk_list_subscription_user_id FOREIGN KEY ("user_id") REFERENCES users ("id"),
    CONSTRAINT fk_list_subscription_item_id FOREIGN KEY ("item_id") REFERENCES items ("id")
);
