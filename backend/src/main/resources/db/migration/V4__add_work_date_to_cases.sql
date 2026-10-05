alter table cases
    add column work_date date;

update cases
set work_date = created_at::date
where work_date is null;

alter table cases
    alter column work_date set not null;