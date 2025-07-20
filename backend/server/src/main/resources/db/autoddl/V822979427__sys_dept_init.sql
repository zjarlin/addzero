create table if not exists "sys_dept"
(
    id          bigint primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    name        TEXT

);
comment on table "sys_dept" is 'sys_dept';
comment on column sys_dept.id is '主键';
comment on column sys_dept.create_by is '创建者';
comment on column sys_dept.create_time is '创建时间';
comment on column sys_dept.update_by is '更新者';
comment on column sys_dept.update_time is '更新时间';
comment on column sys_dept.name is '部门名称';
