create or replace procedure proc_autorun_task is
   v_taskid number;
   v_proc_name varchar2(100);
   v_param1 number;
   v_param2 varchar2(10000);
   a_sql varchar2(1024);
   v_status_unrun number;
   v_status_running number;
   v_status_run_complete number;
begin
   v_status_unrun := 1;
   v_status_running := 2;
   v_status_run_complete := 3;
  select nvl(min(id),0) into v_taskid from ZL_AUTORUN_TASK where rownum=1 and STATUS=v_status_unrun;
  if v_taskid > 0 then
     update ZL_AUTORUN_TASK set STATUS=v_status_running,TASK_RESULT=null,RESULT_DESC=null,start_time=sysdate,end_time=null,used_secs=null where id=v_taskid;
     insert into zl_autorun_task_log
       (log_id, log_type, task_id, name, status, task_result, what, param_count, param1, param2, param3, result_desc, create_time, modify_time, start_time, end_time, used_secs)
        select (select nvl(max(log_id),0)+1 from zl_autorun_task_log), 'start', id, name, status, task_result, what, param_count, param1, param2, param3, result_desc, create_time, modify_time, start_time, end_time, used_secs
           from ZL_AUTORUN_TASK where id=v_taskid;
     commit;
     
     select id,lower(what),param1,param2 into v_taskid,v_proc_name,v_param1,v_param2 from ZL_AUTORUN_TASK where id=v_taskid;
     --a_sql := proc_name||'(param1,param2)';
     --execute immediate a_sql;
     
     if v_proc_name = 'proc_test' then
        null;--proc_test(v_param1,v_param2);
     elsif v_proc_name = 'proc_ott_import_deal' then
         proc_ott_import_deal(v_param1,v_param2);
     elsif v_proc_name = 'proc_zl_epg_import_deal' then
         proc_zl_epg_import_deal(v_param1,v_param2);
     end if;
  
     update ZL_AUTORUN_TASK set STATUS=v_status_run_complete,TASK_RESULT=0,RESULT_DESC=v_param2,end_time=sysdate,used_secs=to_char(sysdate-start_time+to_date('1970-01-01','yyyy-mm-dd'),'hh24:mi:ss') where id=v_taskid;
     insert into zl_autorun_task_log
       (log_id, log_type, task_id, name, status, task_result, what, param_count, param1, param2, param3, result_desc, create_time, modify_time, start_time, end_time, used_secs)
        select (select nvl(max(log_id),0)+1 from zl_autorun_task_log), 'end', id, name, status, task_result, what, param_count, param1, param2, param3, result_desc, create_time, modify_time, start_time, end_time, used_secs
           from ZL_AUTORUN_TASK where id=v_taskid;
     commit;
     
     --for test
     --update ZL_AUTORUN_TASK set STATUS=v_status_unrun where id=v_taskid; commit;
  end if;
  
  EXCEPTION
  WHEN OTHERS THEN
     v_param2 := SQLCODE||'==>'||SQLERRM;
     update ZL_AUTORUN_TASK set STATUS=v_status_run_complete,TASK_RESULT=-1,RESULT_DESC=v_param2,end_time=sysdate,used_secs=to_char(sysdate-start_time+to_date('1970-01-01','yyyy-mm-dd'),'hh24:mi:ss') where id=v_taskid;
     insert into zl_autorun_task_log
       (log_id, log_type, task_id, name, status, task_result, what, param_count, param1, param2, param3, result_desc, create_time, modify_time, start_time, end_time, used_secs)
        select (select nvl(max(log_id),0)+1 from zl_autorun_task_log), 'end', id, name, status, task_result, what, param_count, param1, param2, param3, result_desc, create_time, modify_time, start_time, end_time, used_secs
           from zl_autorun_task where id=v_taskid;
     commit;
     
     --for test
     --update ZL_AUTORUN_TASK set STATUS=v_status_unrun where id=v_taskid; commit;
end proc_autorun_task;
/
