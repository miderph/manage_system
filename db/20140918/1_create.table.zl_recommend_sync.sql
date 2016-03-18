-- Create table
create table ZL_RECOMMEND_SYNC
(
  c_id         VARCHAR2(20),
  status       NUMBER not null,
  create_time  DATE not null,
  update_time  DATE not null,
  sp_code      VARCHAR2(50) not null,
  name         VARCHAR2(200) not null,
  id           NUMBER not null,
  extra_params VARCHAR2(1000)
);
-- Add comments to the table 
comment on table ZL_RECOMMEND_SYNC
  is ' �Ƽ�����ͬ��';
-- Add comments to the columns 
comment on column ZL_RECOMMEND_SYNC.c_id
  is '����id';
comment on column ZL_RECOMMEND_SYNC.status
  is '״̬';
comment on column ZL_RECOMMEND_SYNC.create_time
  is '����ʱ��';
comment on column ZL_RECOMMEND_SYNC.update_time
  is '����ʱ��';
comment on column ZL_RECOMMEND_SYNC.sp_code
  is 'sp����';
comment on column ZL_RECOMMEND_SYNC.name
  is '��������';
comment on column ZL_RECOMMEND_SYNC.id
  is 'id';
comment on column ZL_RECOMMEND_SYNC.extra_params
  is '��չ��������';


create or replace trigger trg_zl_recommend_sync before insert on zl_recommend_sync for each row
begin
if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

if :NEW.create_time is null then
   :NEW.create_time := sysdate;
end if;

end;
