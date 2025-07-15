    create table if not exists "biz_dotfiles" (
        id bigint primary key,
        create_by bigint ,
        update_by bigint ,
        create_time timestamp ,
        update_time timestamp ,
        os_structure TEXT  ,
def_type TEXT  ,
name TEXT  ,
value TEXT  ,
describtion TEXT  ,
status TEXT  ,
file_url TEXT  ,
location TEXT  
       
    );
    comment on table "biz_dotfiles" is '<p>环境变量管理</p>@authorzjarlin@date2024-10-20';
 comment on column biz_dotfiles.id is '主键';
comment on column biz_dotfiles.create_by is '创建者';
comment on column biz_dotfiles.create_time is '创建时间';
comment on column biz_dotfiles.update_by is '更新者';
comment on column biz_dotfiles.update_time is '更新时间'; 
            comment on column biz_dotfiles.os_structure is '系统架构arm64=arm64x86=x86不限=不限'; 
comment on column biz_dotfiles.def_type is '定义类型alias=aliasexport=exportfunction=functionsh=shvar=var'; 
comment on column biz_dotfiles.name is '名称'; 
comment on column biz_dotfiles.value is '值'; 
comment on column biz_dotfiles.describtion is '注释'; 
comment on column biz_dotfiles.status is '状态1=启用0=未启用'; 
comment on column biz_dotfiles.file_url is '文件地址'; 
comment on column biz_dotfiles.location is '文件位置'; 
