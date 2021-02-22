CREATE TABLE notification_tokens
(
    "user_id"            bigint,
    "token"              varchar,
    "token_type"         varchar,

    CONSTRAINT fk_notification_token_user_id FOREIGN KEY ("user_id") REFERENCES users ("id")
);