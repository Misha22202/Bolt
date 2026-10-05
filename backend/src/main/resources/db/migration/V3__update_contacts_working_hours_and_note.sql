alter table contacts
    add column working_hours_json jsonb not null default '[]'::jsonb;

update contacts
set working_hours_json = '[
        {"day": "Понедельник", "time": "Выходной"},
        {"day": "Вторник — Воскресенье", "time": "11:00 — 20:00"}
    ]'::jsonb
where id = 1;

alter table contacts
drop column working_hours;

alter table contacts
    rename column working_hours_json to working_hours;

alter table contacts
    add column contacts_info_note varchar(500);