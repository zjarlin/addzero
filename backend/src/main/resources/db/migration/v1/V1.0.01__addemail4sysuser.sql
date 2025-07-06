alter table sys_user
    add email text;

comment on column sys_user.email is '邮箱';

