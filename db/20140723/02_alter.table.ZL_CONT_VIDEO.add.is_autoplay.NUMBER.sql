alter table ZL_CONT_VIDEO add is_autoplay NUMBER(1);
comment on column ZL_CONT_VIDEO.is_autoplay is '1支持，其他不支持';