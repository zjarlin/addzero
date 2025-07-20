create table if not exists "sys_dict"
(
    id          bigint primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    dict_name   TEXT,
    dict_code   TEXT,
    description TEXT,
    deleted     INTEGER

);
comment on table "sys_dict" is '<p>sys_dict</p>@authorzjarlin@date2024/11/27@constructor创建[SysDict]';
comment on column sys_dict.id is '主键';
comment on column sys_dict.create_by is '创建者';
comment on column sys_dict.create_time is '创建时间';
comment on column sys_dict.update_by is '更新者';
comment on column sys_dict.update_time is '更新时间';
comment on column sys_dict.dict_name is '字典名称';
comment on column sys_dict.dict_code is '字典编码';
comment on column sys_dict.description is '描述';
comment on column sys_dict.deleted is ''; 
