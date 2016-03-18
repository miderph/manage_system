alter table zl_cont_provider add icon_url VARCHAR2(255);
comment on column zl_cont_provider.icon_url is '图标';

alter table zl_cont_provider add hot_info VARCHAR2(1000);
comment on column zl_cont_provider.hot_info is '促销信息';

