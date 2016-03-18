alter table zl_user_group add ids_value varchar2(2048);
comment on column zl_user_group.ids_value is 'ids值（值得类型由type确定：地区或型号）';
