alter table ZL_MENU add is_autoplay NUMBER(5);
comment on column ZL_MENU.is_autoplay is '是否需要自动播放	1需要，0不需要';