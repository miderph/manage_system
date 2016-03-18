declare  
 job pls_integer;
begin
  sys.dbms_job.submit(job => job,
                      what => 'proc_autorun_task;',
                      next_date => sysdate,
                      interval => 'sysdate+1/1440');
  dbms_output.put_line(job);
  commit;
end;
/