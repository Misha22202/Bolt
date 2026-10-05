create table site_settings (
                               setting_key varchar(100) primary key,
                               setting_value text not null,
                               updated_at timestamptz not null default now()
);

create table services (
                          id bigserial primary key,
                          name varchar(255) not null,
                          description text,
                          price varchar(100),
                          icon varchar(50) not null default 'tools',
                          sort_order integer not null default 0,
                          is_visible boolean not null default true,
                          created_at timestamptz not null default now(),
                          updated_at timestamptz not null default now()
);

create table features (
                          id bigserial primary key,
                          title varchar(255) not null,
                          description text not null,
                          icon varchar(50) not null,
                          sort_order integer not null default 0,
                          is_visible boolean not null default true,
                          created_at timestamptz not null default now(),
                          updated_at timestamptz not null default now()
);

create table cases (
                       id bigserial primary key,
                       title varchar(255) not null,
                       description text,
                       device_type varchar(150),
                       cover_image_url text,
                       sort_order integer not null default 0,
                       is_visible boolean not null default true,
                       created_at timestamptz not null default now(),
                       updated_at timestamptz not null default now()
);

create table media (
                       id bigserial primary key,
                       original_name varchar(500) not null,
                       stored_name varchar(500) not null unique,
                       content_type varchar(150) not null,
                       size_bytes bigint not null,
                       public_url text not null unique,
                       alt_text varchar(255),
                       category varchar(50) not null,
                       sort_order integer not null default 0,
                       created_at timestamptz not null default now()
);

create table requests (
                          id bigserial primary key,
                          customer_name varchar(120) not null,
                          phone varchar(40) not null,
                          description text,
                          status varchar(30) not null default 'NEW',
                          created_at timestamptz not null default now()
);

insert into site_settings (setting_key, setting_value) values
                                                           ('hero.title', 'Чинить с умом, а не с молотком'),
                                                           ('hero.description', 'Ремонтируем пылесосы, роботы-пылесосы, дрели и другую технику. Находим причину поломки, а не меняем детали наугад.'),
                                                           ('contact.address', 'г. Воронеж, укажите адрес'),
                                                           ('contact.phone', '+7 (000) 000-00-00'),
                                                           ('contact.telegram', 'https://t.me/perviyvint'),
                                                           ('contact.working_hours', 'Вт–Вс: 11:00–20:00; Пн: выходной');