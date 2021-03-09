ALTER TABLE notification_tokens DROP CONSTRAINT fk_notification_token_user_id;
ALTER TABLE notification_tokens ADD CONSTRAINT fk_notification_token_user_id
    FOREIGN KEY ("user_id") REFERENCES users ("id")
    ON DELETE CASCADE;


ALTER TABLE users ADD COLUMN "firebase_id" TEXT DEFAULT NULL;
ALTER TABLE users DROP COLUMN company_id;
ALTER TABLE users DROP COLUMN sex;
ALTER TABLE users DROP COLUMN email;
ALTER TABLE users DROP COLUMN password;
