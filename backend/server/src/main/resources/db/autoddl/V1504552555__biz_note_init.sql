create table if not exists "biz_note"
(
    id          bigint primary key,
    create_by   bigint,
    update_by   bigint,
    create_time timestamp,
    update_time timestamp,
    parent      TEXT,
    title       TEXT,
    content     TEXT,
    type        TEXT,
    path        TEXT,
    file_url    TEXT

);
comment on table "biz_note" is '笔记实体类，用于表示笔记的基本信息和结构。该实体类映射到数据库表`biz_note`。';
comment on column biz_note.id is '主键';
comment on column biz_note.create_by is '创建者';
comment on column biz_note.create_time is '创建时间';
comment on column biz_note.update_by is '更新者';
comment on column biz_note.update_time is '更新时间';
comment on column biz_note.parent is '笔记的父节点，表示当前笔记的父笔记。通过{@linkManyToOne}注解与子笔记关联。@return父笔记，如果没有父笔记则返回null';
comment on column biz_note.title is '标题';
comment on column biz_note.content is '内容';
comment on column biz_note.type is '类型1=markdown2=pdf3=word4=excel@return笔记类型';
comment on column biz_note.path is '笔记的路径@return笔记路径';
comment on column biz_note.file_url is '笔记关联的文件链接（可选）。'; 
