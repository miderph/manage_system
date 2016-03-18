---------------------------------------------------
-- Export file for user ZLINKTV_QDGH             --
-- Created by miderph.yan on 2014/1/24, 17:31:19 --
---------------------------------------------------

set define off
spool 1.log

prompt
prompt Creating table ZL_MODULES
prompt =========================
prompt
create table ZL_MODULES
(
  id        NUMBER,
  name      VARCHAR2(200),
  url       VARCHAR2(200),
  parent_id NUMBER,
  levels    NUMBER(1),
  leaf      NUMBER(1),
  path      VARCHAR2(200)
)
;
comment on table ZL_MODULES
  is 'ϵͳģ��';
comment on column ZL_MODULES.name
  is 'ģ������';
comment on column ZL_MODULES.url
  is 'ģ��������·��';
comment on column ZL_MODULES.parent_id
  is '��ģ��id';
comment on column ZL_MODULES.levels
  is '�ȼ�';
comment on column ZL_MODULES.leaf
  is 'Ҷ�ӽڵ�(0 �� ��1 ����)';
comment on column ZL_MODULES.path
  is '�̳�·��';

prompt
prompt Creating table ZL_ROLES
prompt =======================
prompt
create table ZL_ROLES
(
  id           NUMBER not null,
  name         VARCHAR2(255) not null,
  site_ids     VARCHAR2(1000),
  provider_ids VARCHAR2(1000),
  create_time  DATE,
  update_time  DATE,
  module_ids   VARCHAR2(1000)
)
;
comment on table ZL_ROLES
  is '��ɫ��';
comment on column ZL_ROLES.name
  is '��ɫ��';
comment on column ZL_ROLES.site_ids
  is '��ά��վ�㣬���վ��id�������,���ŷָ�';
comment on column ZL_ROLES.provider_ids
  is '��ά���ʲ�����Ƶ��Ӧ�ã������վ��provider_id�������,���ŷָ�';
comment on column ZL_ROLES.create_time
  is '����ʱ��';
comment on column ZL_ROLES.update_time
  is '����ʱ��';
comment on column ZL_ROLES.module_ids
  is 'ģ��id,�����,���ŷָ�';
alter table ZL_ROLES
  add constraint PK_ZL_ROLES primary key (ID);

prompt
prompt Creating table ZL_STB_PREFIXES
prompt ==============================
prompt
create table ZL_STB_PREFIXES
(
  id          NUMBER not null,
  code        VARCHAR2(200) not null,
  site_id     VARCHAR2(200),
  provider_id VARCHAR2(200),
  create_time DATE,
  update_time DATE
)
;
comment on table ZL_STB_PREFIXES
  is 'ǰ׺����';
comment on column ZL_STB_PREFIXES.code
  is 'ǰ׺����';
comment on column ZL_STB_PREFIXES.site_id
  is 'վ��id';
comment on column ZL_STB_PREFIXES.provider_id
  is '�����ṩ��id';
alter table ZL_STB_PREFIXES
  add constraint PK_ZL_STB_PREFIXES primary key (ID);


-- Add/modify columns 
alter table ZL_OPERATORS add role_ids VARCHAR2(200);
-- Add comments to the columns 
comment on column ZL_OPERATORS.role_ids
  is '�û���ɫid�������,�ָ�';


prompt
prompt Creating trigger TRG_ZL_ROLES
prompt =============================
prompt
create or replace trigger trg_zl_roles before insert on zl_roles for each row
begin
if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

if :NEW.create_time is null then
   :NEW.create_time := sysdate;
end if;

end;
/

prompt
prompt Creating trigger TRG_ZL_STB_PREFIXES
prompt ====================================
prompt
create or replace trigger trg_zl_stb_prefixes before insert on zl_stb_prefixes for each row
begin
if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

if :NEW.create_time is null then
   :NEW.create_time := sysdate;
end if;

end;
/


spool off
