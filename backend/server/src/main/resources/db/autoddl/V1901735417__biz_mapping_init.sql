create table if not exists "biz_mapping"
(
    id          bigint primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    from_id     bigint,
    to_id       bigint

);
comment on table "biz_mapping" is '';
comment on column biz_mapping.id is '主键';
comment on column biz_mapping.create_by is '创建者';
comment on column biz_mapping.create_time is '创建时间';
comment on column biz_mapping.update_by is '更新者';
comment on column biz_mapping.update_time is '更新时间'; 
            
