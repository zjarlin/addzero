    create table if not exists "sys_dict_item" (
        id bigint primary key,
        create_by bigint ,
        update_by bigint ,
        create_time timestamp ,
        update_time timestamp ,
        item_text TEXT  ,
item_value TEXT  ,
description TEXT  ,
sort_order BIGINT  ,
status BIGINT  
       
    );
    comment on table "sys_dict_item" is '<p>sys_dict_item</p>@authorzjarlin@date2024-09-16';
 comment on column sys_dict_item.id is '主键';
comment on column sys_dict_item.create_by is '创建者';
comment on column sys_dict_item.create_time is '创建时间';
comment on column sys_dict_item.update_by is '更新者';
comment on column sys_dict_item.update_time is '更新时间'; 
            comment on column sys_dict_item.item_text is '字典项文本'; 
comment on column sys_dict_item.item_value is '字典项值'; 
comment on column sys_dict_item.description is '描述'; 
comment on column sys_dict_item.sort_order is '排序'; 
comment on column sys_dict_item.status is '状态（1启用0不启用）'; 
