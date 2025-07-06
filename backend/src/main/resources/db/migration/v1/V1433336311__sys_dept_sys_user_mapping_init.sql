-- auto-generated definition
create table if not exists "sys_dept_sys_user_mapping"
(
    id          bigint not null
        primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    sys_dept_id bigint,
    sys_user_id bigint
);

comment on column sys_dept_sys_user_mapping.id is '主键';

comment on column sys_dept_sys_user_mapping.create_by is '创建者';

comment on column sys_dept_sys_user_mapping.update_by is '更新者';

comment on column sys_dept_sys_user_mapping.create_time is '创建时间';

comment on column sys_dept_sys_user_mapping.update_time is '更新时间';

alter table sys_dept_sys_user_mapping
    owner to postgres;

