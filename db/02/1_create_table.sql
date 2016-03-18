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
  is '系统模块';
comment on column ZL_MODULES.name
  is '模块名称';
comment on column ZL_MODULES.url
  is '模块访问相对路径';
comment on column ZL_MODULES.parent_id
  is '父模块id';
comment on column ZL_MODULES.levels
  is '等级';
comment on column ZL_MODULES.leaf
  is '叶子节点(0 是 ；1 不是)';
comment on column ZL_MODULES.path
  is '继承路径';

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
  is '角色表';
comment on column ZL_ROLES.name
  is '角色名';
comment on column ZL_ROLES.site_ids
  is '能维护站点，存放站点id，多个用,逗号分隔';
comment on column ZL_ROLES.provider_ids
  is '能维护资产（视频和应用），存放站点provider_id，多个用,逗号分隔';
comment on column ZL_ROLES.create_time
  is '创建时间';
comment on column ZL_ROLES.update_time
  is '更新时间';
comment on column ZL_ROLES.module_ids
  is '模块id,多个用,逗号分隔';
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
  is '前缀管理';
comment on column ZL_STB_PREFIXES.code
  is '前缀编码';
comment on column ZL_STB_PREFIXES.site_id
  is '站点id';
comment on column ZL_STB_PREFIXES.provider_id
  is '内容提供商id';
alter table ZL_STB_PREFIXES
  add constraint PK_ZL_STB_PREFIXES primary key (ID);


-- Add/modify columns 
alter table ZL_OPERATORS add role_ids VARCHAR2(200);
-- Add comments to the columns 
comment on column ZL_OPERATORS.role_ids
  is '用户角色id，多个用,分隔';


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
