    create table if not exists "sys_area" (
        id bigint primary key,
        create_by bigint ,
        update_by bigint ,
        create_time timestamp ,
        update_time timestamp ,
        node_type TEXT  ,
name TEXT  ,
area_code TEXT  
       
    );
    comment on table "sys_area" is '<p>区域表</p>@authorzjarlin@date2025-02-26';
 comment on column sys_area.id is '主键';
comment on column sys_area.create_by is '创建者';
comment on column sys_area.create_time is '创建时间';
comment on column sys_area.update_by is '更新者';
comment on column sys_area.update_time is '更新时间'; 
            comment on column sys_area.node_type is '1省,2市,3区数据库列名:node_type数据类型:text可空:是默认值:NULL::charactervarying'; 
comment on column sys_area.name is 'name数据库列名:name数据类型:text可空:是默认值:NULL::charactervarying'; 
comment on column sys_area.area_code is '区域编码数据库列名:area_code数据类型:text可空:是默认值:NULL::charactervarying'; 
