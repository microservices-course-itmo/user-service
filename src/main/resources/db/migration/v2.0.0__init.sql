CREATE TABLE company
(
    "id"   bigint PRIMARY KEY,
    "name" varchar
);

CREATE TABLE city
(
    "id"   bigint PRIMARY KEY,
    "name" varchar
);

CREATE TABLE user
(
    "id"           bigint PRIMARY KEY,
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


    CONSTRAINT fk_user_city_id FOREIGN KEY ("city_id") REFERENCES city ("id"),
    CONSTRAINT fk_user_company_id FOREIGN KEY ("company_id") REFERENCES company ("id"),
    CONSTRAINT fk_user_role_id FOREIGN KEY ("role_id") REFERENCES role ("id") ON DELETE CASCADE
);

CREATE TABLE role
(
    "id"   bigint PRIMARY KEY,
    "name" varchar
);

CREATE TABLE catalog
(
  "id" bigint PRIMARY KEY,
  "user_id" bigint,
  "description" varchar,

  CONSTRAINT fk_catalog_user_id FOREIGN KEY ("user_id") REFERENCES user ("id")
);

CREATE TABLE item
(
    "id" bigint PRIMARY KEY,
    "name" varchar,
    "description" varchar
);

CREATE TABLE list_catalog
(
  "catalog_id" bigint,
  "item_id" bigint,

  CONSTRAINT fk_list_catalog_catalog_id FOREIGN KEY ("catalog_id") REFERENCES catalog ("id"),
  CONSTRAINT fk_list_catalog_item_id FOREIGN KEY ("item_id") REFERENCES item ("id")
);

CREATE TABLE token
(
    "user_id" bigint,
    "token_access" varchar,
    "token_refresh" varchar,
    "token_refresh_date" varchar,

    CONSTRAINT fk_token_user_id FOREIGN KEY ("user_id") REFERENCES user ("id")
);

CREATE TABLE list_favorite
(
    "user_id" bigint,
    "item_id" bigint,

    CONSTRAINT fk_list_favorite_user_id FOREIGN KEY ("user_id") REFERENCES user ("id"),
    CONSTRAINT fk_list_favorite_item_id FOREIGN KEY ("item_id") REFERENCES item ("id")
);

CREATE TABLE list_subscription
(
    "user_id" bigint,
    "item_id" bigint,

    CONSTRAINT fk_list_subscription_user_id FOREIGN KEY ("user_id") REFERENCES user ("id"),
    CONSTRAINT fk_list_subscription_item_id FOREIGN KEY ("item_id") REFERENCES item ("id")
);