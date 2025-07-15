create table if not exists "sys_role"
(
    id          bigint primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    role_code   TEXT,
    role_name   TEXT,
    system_flag BOOLEAN,
    status      TEXT

);
comment on table "sys_role" is '系统角色实体对应数据库表';
comment on column sys_role.id is '主键';
comment on column sys_role.create_by is '创建者';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.update_by is '更新者';
comment on column sys_role.update_time is '更新时间';
comment on column sys_role.role_code is '角色编码';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.system_flag is '是否为系统角色';
comment on column sys_role.status is '角色状态';
