------------------------------------------------
-- Export file for user ZLINKTV               --
-- Created by finlay.yan on 2016/2/2, 1:28:32 --
------------------------------------------------

set define off

prompt
prompt Creating table ZL_AUTORUN_TASK
prompt ==============================
prompt
create table ZL_AUTORUN_TASK
(
  id          NUMBER(20) not null,
  name        VARCHAR2(100),
  create_time DATE default sysdate not null,
  modify_time DATE default sysdate not null,
  start_time  DATE,
  end_time    DATE,
  used_secs   VARCHAR2(100),
  status      NUMBER(5),
  task_result NUMBER(5),
  result_desc VARCHAR2(4000),
  what        VARCHAR2(100),
  param_count NUMBER(5),
  param1      VARCHAR2(100),
  param2      VARCHAR2(100),
  param3      VARCHAR2(100)
)
;
comment on column ZL_AUTORUN_TASK.used_secs
  is '耗时秒，形如01:11:22';
comment on column ZL_AUTORUN_TASK.status
  is '1 unrun,2 running,3,run complete';
comment on column ZL_AUTORUN_TASK.task_result
  is '0 ok,others fail number';
comment on column ZL_AUTORUN_TASK.param_count
  is 'param count,max is 10';
alter table ZL_AUTORUN_TASK
  add constraint PK_ZL_AUTORUN_TASK primary key (ID);

prompt
prompt Creating table ZL_AUTORUN_TASK_LOG
prompt ==================================
prompt
create table ZL_AUTORUN_TASK_LOG
(
  log_id      NUMBER(20) not null,
  log_type    VARCHAR2(10),
  log_time    DATE default sysdate not null,
  task_id     NUMBER(20),
  name        VARCHAR2(100),
  create_time DATE default sysdate not null,
  modify_time DATE default sysdate not null,
  start_time  DATE,
  end_time    DATE,
  used_secs   VARCHAR2(100),
  status      NUMBER(5),
  task_result NUMBER(5),
  result_desc VARCHAR2(4000),
  what        VARCHAR2(100),
  param_count NUMBER(5),
  param1      VARCHAR2(100),
  param2      VARCHAR2(100),
  param3      VARCHAR2(100)
)
;
comment on column ZL_AUTORUN_TASK_LOG.log_type
  is 'start,end';
alter table ZL_AUTORUN_TASK_LOG
  add constraint PK_ZL_AUTORUN_TASK_LOG primary key (LOG_ID);
