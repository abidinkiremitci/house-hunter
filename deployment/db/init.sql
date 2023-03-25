create table if not exists public.house_entity
(
    id                   bigint not null
        primary key,
    balcony              boolean,
    city                 varchar(255),
    date                 date,
    energy_class         varchar(255),
    energy_efficient     boolean,
    garden               boolean,
    house_number         varchar(255),
    living_area          bigint,
    neighborhood         varchar(255),
    number_of_rooms      bigint,
    postcode             varchar(255),
    price                bigint,
    price_range          bigint,
    status               varchar(255),
    street               varchar(255),
    url                  varchar(255),
    year_of_construction bigint
);

alter table public.house_entity
    owner to house_hunter_app;

create table if not exists public.house_filter_entity
(
    id         bigint not null
        primary key,
    base_url   varchar(255),
    district   varchar(255),
    max        bigint,
    min        bigint,
    province   varchar(255),
    sort_order varchar(255)
);

alter table public.house_filter_entity
    owner to house_hunter_app;
