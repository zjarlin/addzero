create table if not exists "sys_user"
(
    id          bigint primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    phone       TEXT,
    username    TEXT,
    password    TEXT,
    avatar      TEXT,
    nickname    TEXT,
    gender      TEXT

);
comment on table "sys_user" is '@authorzjarlin@date2024/11/03@constructor创建[SysUser]';
comment on column sys_user.id is '主键';
comment on column sys_user.create_by is '创建者';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_by is '更新者';
comment on column sys_user.update_time is '更新时间';
comment on column sys_user.phone is '手机号';
comment on column sys_user.username is '';
comment on column sys_user.password is '密码';
comment on column sys_user.avatar is '头像';
comment on column sys_user.nickname is '昵称';
comment on column sys_user.gender is '性别0：男1=女2-未知'; 
