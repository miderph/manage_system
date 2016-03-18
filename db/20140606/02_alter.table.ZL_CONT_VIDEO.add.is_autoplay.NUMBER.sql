alter table ZL_CONT_VIDEO add is_autoplay NUMBER(5);
comment on column ZL_CONT_VIDEO.is_autoplay is '是否需要自动播放	1：能0：不能';