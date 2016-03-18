update zl_img set LOCKED=0 where locked is null;
alter table ZL_img modify LOCKED default 0 not null;
