TRUNCATE TABLE list_subscriptions, list_catalogs, list_favorites, items;

ALTER TABLE list_subscriptions DROP CONSTRAINT fk_list_subscription_item_id;
ALTER TABLE list_subscriptions ALTER COLUMN item_id TYPE varchar;

ALTER TABLE list_favorites DROP CONSTRAINT fk_list_favorite_item_id;
ALTER TABLE list_favorites ALTER COLUMN item_id TYPE varchar;

ALTER TABLE list_catalogs DROP CONSTRAINT fk_list_catalog_item_id;
ALTER TABLE list_catalogs ALTER COLUMN item_id TYPE varchar;

ALTER TABLE items ALTER COLUMN id DROP DEFAULT;
ALTER TABLE items ALTER COLUMN id TYPE varchar;

ALTER TABLE list_subscriptions ADD CONSTRAINT fk_list_subscription_item_id
    FOREIGN KEY ("item_id") REFERENCES items("id");

ALTER TABLE list_favorites ADD CONSTRAINT fk_list_favorite_item_id
    FOREIGN KEY ("item_id") REFERENCES items("id");

ALTER TABLE list_catalogs ADD CONSTRAINT fk_list_catalog_item_id
    FOREIGN KEY ("item_id") REFERENCES items("id");
