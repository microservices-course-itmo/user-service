CREATE TABLE dim_company (
    "CompanyId"       bigserial         PRIMARY KEY,
    "CompanyName"     varchar
);

CREATE TABLE dim_city (
    "CityId"          bigserial         PRIMARY KEY,
    "CityName"        varchar
);

CREATE TABLE dim_user (
    "UserId"          bigserial         PRIMARY KEY,
    "FirstName"       varchar,
    "LastName"        varchar,
    "BirthDate"       date,
    "Sex"             int,
    "Email"           varchar,
    "PhoneNumber"     varchar,
    "CityId"          bigint,
    "isActivated"     boolean,
    "CreateDate"      timestamp,
    "CompanyId"       bigint,

    CONSTRAINT CityIdFK FOREIGN KEY ("CityId") REFERENCES dim_city("CityId"),
    CONSTRAINT CompanyFK FOREIGN KEY ("CompanyId") REFERENCES dim_company("CompanyId")
);
