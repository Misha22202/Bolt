create table contacts (
                               id bigint primary key,
                               phone varchar(40) not null,
                               address varchar(255) not null,
                               telegram_url varchar(255),
                               whatsapp_url varchar(255),
                               email varchar(255),
                               working_hours varchar(255) not null,
                               updated_at timestamptz not null default now()
);

insert into contacts (
    id,
    phone,
    address,
    telegram_url,
    working_hours
) values (
             1,
             '+7 (000) 000-00-00',
             'г. Воронеж, укажите адрес',
             'https://t.me/perviyvint',
             'Вт–Вс: 11:00–20:00; Пн: выходной'
         );