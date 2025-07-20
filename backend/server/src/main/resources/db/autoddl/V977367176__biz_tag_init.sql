create table if not exists "biz_tag"
(
    id          bigint primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    name        TEXT,
    description TEXT

);
comment on table "biz_tag" is '标签实体类，用于管理笔记的标签系统该实体类映射到数据库表`biz_tag`';
comment on column biz_tag.id is '主键';
comment on column biz_tag.create_by is '创建者';
comment on column biz_tag.create_time is '创建时间';
comment on column biz_tag.update_by is '更新者';
comment on column biz_tag.update_time is '更新时间';
comment on column biz_tag.name is '标签名称';
comment on column biz_tag.description is '标签描述'; 
