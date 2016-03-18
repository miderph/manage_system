-- Add/modify columns 
alter table ZL_CONT_CHECKS add create_time date default sysdate;
alter table ZL_CONT_CHECKS add update_time date default sysdate;
