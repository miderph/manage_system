create or replace procedure proc_rule_all_deal( updateCount in out integer) is
  --v_contStatus integer;
  v_sql  varchar(2048);

  --  ref CURSOR;
  type c_menu_type is ref cursor;
  c_menu c_menu_type;
  --cousor record
  menu_rd zl_menu%ROWTYPE;
begin
  --savepoint A;
  updateCount := 0;
  v_sql := 'select * from zl_menu m where m.status >= 1 and m.import_rule_ids is not null';
  OPEN c_menu for v_sql;
  LOOP
    FETCH c_menu INTO menu_rd;
    EXIT WHEN c_menu%NOTFOUND;
    proc_rule_by_menu_deal(menu_rd.id,menu_rd.site_id,menu_rd.provider_id,updateCount);
    dbms_output.put_line('proc_rule_by_menu '|| menu_rd.id);
  END LOOP;
  CLOSE c_menu;
  commit;
EXCEPTION
  WHEN others then
    dbms_output.put_line(substr(sqlerrm,0,500));
    rollback;
end proc_rule_all_deal;
/
