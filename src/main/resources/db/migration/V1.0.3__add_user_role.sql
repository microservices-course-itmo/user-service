CREATE TABLE dim_role (
    "RoleId"                bigserial         PRIMARY KEY,
    "RoleName"              varchar,
    "AccessToCatalog"       boolean,
    "CanComment"            boolean,
    "CanCreateCatalog"      boolean,
    "CanAddItem"            boolean,
    "CanLike"               boolean

);

ALTER TABLE dim_user ADD COLUMN "RoleId" bigint;
ALTER TABLE dim_user ADD CONSTRAINT "RoleFK" FOREIGN KEY ("RoleId") REFERENCES "dim_role"("RoleId") ON DELETE CASCADE;
