update zl_cont set LOCKED=0 where locked is null;
alter table ZL_CONT modify LOCKED default 0 not null;
