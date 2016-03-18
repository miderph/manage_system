create or replace procedure proc_rule_by_menu_datasql_deal(i_menu_id in integer, p_sql in varchar2,
       v_new_menu in integer,
       site_id in integer,
       provider_id in integer,
       updateCount in out integer) is

  --  ref CURSOR;
  type c_cont_sale_type is ref cursor;
  c_cont_sale c_cont_sale_type;
  --cousor record
  cont_sale_rd v_getsalesdetail_client%ROWTYPE;
  v_i integer;
  v_count integer;
  v_temp_menu_id integer;
  v_i_menu_id integer;
  v_start integer;
  v_index integer;
  v_category varchar2(255);
  vvv varchar2(255);
  v_menu_count integer :=-1;
begin
  --find contsales
   v_i := 0;
   if p_sql is null or i_menu_id is null then
     return;
   end if;

   -- 查询
   BEGIN
      execute  immediate   'select count(1)  from (' || p_sql || ')' into v_count;
   EXCEPTION
      when others then
        v_count := 0;
   END;
   if v_count = 0 then
     return;
   end if;
   update zl_rela_menu_cont mc set mc.order_num = mc.order_num + v_count where mc.menu_id = i_menu_id;
   OPEN c_cont_sale   for p_sql;
   --delete rela_menu_cont
   --delete from zl_rela_menu_cont rmc where rmc.menu_id = i_menu_id and ( rmc.locked  is null  or  rmc.locked  <>  '1' );
   LOOP
     FETCH c_cont_sale INTO cont_sale_rd;
     EXIT WHEN c_cont_sale%NOTFOUND;
     --是否新建子栏目
     if v_new_menu = 1 and cont_sale_rd.KEY_WORDS is not null and cont_sale_rd.KEY_WORDS <> ' ' then
       vvv := cont_sale_rd.KEY_WORDS;
       v_start := 1;

       -- 空格分割
       v_index := INSTR(cont_sale_rd.KEY_WORDS , ' ', v_start);
       if v_index = 0 then
         -- ,逗号分割
         v_index := INSTR(cont_sale_rd.KEY_WORDS , ',', v_start);
       end if;
       IF v_index = 0 THEN
          --PIPE ROW(SUBSTR(v_string, v_start));
           v_category := SUBSTR(cont_sale_rd.KEY_WORDS, v_start);
       ELSE
          --PIPE ROW(SUBSTR(v_string, v_start, v_index - v_start));
           v_category  := SUBSTR(cont_sale_rd.KEY_WORDS, v_start, v_index - v_start);
       END IF;
       select count(1)  into v_menu_count from zl_menu where title = v_category  and parent_id= i_menu_id;
       if v_menu_count = 0 then
          select  ZLSQ_data.nextval into v_temp_menu_id from dual;
          insert into zl_menu(id,title,parent_id,site_id,status,struct_type,act_type,provider_id,order_num)
                 values(v_temp_menu_id,v_category,i_menu_id,site_id,1,3,'ShowEntries',provider_id,0);
          dbms_output.put_line('inser into zl_menu id :' || v_temp_menu_Id ||' , title : '|| v_category);
       else
          select id into v_temp_menu_id from zl_menu where title = v_category  and parent_id= i_menu_id;
       end if;
       v_i_menu_id := v_temp_menu_id;
     else
       v_i_menu_id := i_menu_id;
     end if;

      --add  rela_menu_cont;
     BEGIN
       select count(1) into v_count from zl_rela_menu_cont mc where mc.menu_id = v_i_menu_id and c_id = cont_sale_rd.c_id;
     EXCEPTION
       when others then
         v_count := 0;
     END;
     if v_count = 0 then
       dbms_output.put_line('insert into zl_rela_menu_cont {menu_id: '|| v_i_menu_id || ', c_id : '|| cont_sale_rd.c_id ||'  }');
       insert into zl_rela_menu_cont(menu_id,c_id,date_time,order_num,update_status)
            values (v_i_menu_id,cont_sale_rd.c_id,sysdate ,v_i ,1);
      else
        dbms_output.put_line('update zl_rela_menu_cont set {menu_id: '|| v_i_menu_id || ', c_id : '|| cont_sale_rd.c_id ||'  }');
        update zl_rela_menu_cont mc set mc.order_num = v_i , mc.date_time = sysdate ,mc.update_status =1 where mc.menu_id = v_i_menu_id and c_id = cont_sale_rd.c_id;
      end if;
     v_i := v_i + 1;
   END LOOP;
   CLOSE c_cont_sale;
   commit;
   updateCount := updateCount + v_i;
end proc_rule_by_menu_datasql_deal;
/
