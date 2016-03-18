alter table zl_cont_provider add is_apk_prior number(1);
comment on column zl_cont_provider.is_apk_prior is '是否优先返回本供应商应用下载：1是，0否';


