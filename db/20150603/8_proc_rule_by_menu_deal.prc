create or replace procedure proc_rule_by_menu_deal(menu_id in integer,
       site_id in integer, 
       provider_id in integer,
       updateCount in out integer) is
   --v_contStatus integer;
   v_sql varchar2(2048);
   v_sql_tmp varchar2(1024);
   v_rule_ids varchar2(1024);
   v_where varchar2(1024);
   --  ref CURSOR;
   type c_rule_type  is ref cursor;
   c_rule c_rule_type ;
   --c_rule1 ref cursor;
   rule_rd zl_cont_rule%ROWTYPE;
   v_temp_menu_id integer;
   v_new_menu integer;
   v_tmp varchar2(255);
    
    v_string varchar2(255);
    v_length   NUMBER ;
    v_start    NUMBER ;
    v_index    NUMBER;
    v_keywords varchar2(255);
begin
   --savepoint A;
   /**
   find ruleid
   */
   v_new_menu := -1;
   select import_rule_ids into v_rule_ids from zl_menu m where m.id= menu_id;
   dbms_output.put_line('id ' || v_rule_ids);
   --find rule
   if v_rule_ids is null then
     return;
   end if;
   v_sql:= 'select * from zl_cont_rule where id in ('||v_rule_ids||')';
   --execute immediate v_sql;

   OPEN c_rule for v_sql;
   --清空v_sql 复用变量
   v_sql := '';
   LOOP
     FETCH c_rule INTO rule_rd;
     EXIT WHEN c_rule%NOTFOUND;
      v_sql_tmp := 'select * from v_getsalesdetail_client zcs  where ';
      v_where :='';
      -- price
      if rule_rd.price is not null then
      if rule_rd.price_rela = '=' or rule_rd.price_rela ='<'
          or rule_rd.price_rela ='<=' or rule_rd.price_rela = '>'
          or rule_rd.price_rela  = '>='  then
            if v_where is null then
              v_where := ' zcs.sale_price '|| rule_rd.price_rela || rule_rd.price;
            else
              v_where := v_where || ' and  zcs.sale_price '|| rule_rd.price_rela || rule_rd.price;
            end if;
      elsif rule_rd.price_rela = 'in' then
           if v_where is null then
              v_where :=  ' zcs.sale_price in ('|| rule_rd.price || ' )';
            else
              v_where :=  v_where || ' and  zcs.sale_price in ('|| rule_rd.price || ' )';
            end if;
      elsif rule_rd.price_rela ='between' and rule_rd.price_right is not null then
        if v_where is null then
          v_where :=  ' zcs.sale_price between '|| rule_rd.price || ' and '|| rule_rd.price_right;
        else
          v_where :=  v_where || ' and  zcs.sale_price between '|| rule_rd.price || ' and '|| rule_rd.price_right;
        end if;
      end if;
      end if;
      -- provider id
      if rule_rd.provider_ids is not null then
      if rule_rd.provider_rela = '=' then
         if v_where is null then
           v_where :=  ' zcs.provider_id='|| rule_rd.provider_ids;
         else
           v_where := v_where || ' and zcs.provider_id='|| rule_rd.provider_ids;
         end if;
      elsif  rule_rd.provider_rela = 'in' then
         if v_where is null then
           v_where := ' zcs.provider_id in (' || rule_rd.provider_ids|| ')';
         else
           v_where := v_where || ' and zcs.provider_id in (' || rule_rd.provider_ids|| ')';
         end if;
      end if;
      end if;
      -- shop_id
      if rule_rd.shop_ids is not null then
      if rule_rd.shop_rela = '=' then
         if v_where is null then
           v_where := ' zcs.shop_id=' || rule_rd.shop_ids;
         else
           v_where := v_where ||' and zcs.shop_id=' || rule_rd.shop_ids;
         end if;
      elsif rule_rd.shop_rela = 'in' then
        if v_where is null then
          v_where := ' zcs.shop_id in (' || rule_rd.shop_ids || ')';
        else
          v_where := v_where || ' and zcs.shop_id in (' || rule_rd.shop_ids || ')';
        end if;
      end if;
      end if;
      --category
      if rule_rd.category is not null then
      --过滤掉 '单引号
      select  replace (rule_rd.category,'''','') into v_tmp from dual;
      if rule_rd.category_rela = '=' then
        --过滤空格
        select  replace (v_tmp,' ','') into v_tmp from dual; 
        if v_where is null then
          v_where := ' zcs.key_words like  ''%' || v_tmp || '%''' ;
        else
          v_where := v_where || ' and zcs.key_words like ''%' || v_tmp || '%''' ;
        end if;
      elsif rule_rd.category_rela = 'in' then
        -- splite category
        v_keywords := '';
        v_string := rule_rd.category;
        v_length := LENGTH(v_string);
        v_start := 1;
        WHILE(v_start <= v_length)
        LOOP
          --空格或者，逗号分割
          v_index := INSTR(v_string, ' ', v_start);
          if v_index = 0 then
            v_index := INSTR(v_string, ',', v_start);
          end if;
          IF v_index = 0
          THEN
            --PIPE ROW(SUBSTR(v_string, v_start));
            if v_keywords is null then 
               v_keywords := ' zcs.key_words like ''%'|| SUBSTR(v_string, v_start) ||  '%''';
            else
               v_keywords := v_keywords ||' or zcs.key_words like ''%'|| SUBSTR(v_string, v_start) || '%''';
            end if;
            v_start := v_length + 1;
          ELSE
            --PIPE ROW(SUBSTR(v_string, v_start, v_index - v_start));
            if v_keywords is null then
               v_keywords := ' zcs.key_words like  ''%'|| SUBSTR(v_string, v_start, v_index - v_start) || '%''';
            else
               v_keywords := v_keywords ||' or zcs.key_words like  ''%'|| SUBSTR(v_string, v_start, v_index - v_start) || '%''';
            end if;
            v_start := v_index + 1;
          END IF;
        END LOOP;
        if v_keywords is not null then
           if v_where is null then
             v_where := '( ' || v_keywords || ' )';
           else
             v_where := v_where || ' and ( ' || v_keywords || ' )';
           end if; 
        end if;
      end if;
      end if;
      -- categorty new menu
      if rule_rd.category_new_menu =1 then
         v_new_menu := 1;
      end if;
      
      if v_where is not null then
        if v_sql is not null then
          v_sql := v_sql || ' union ' || v_sql_tmp || v_where;
        else
          v_sql := v_sql_tmp || v_where;
        end if;
      end if;

     --dbms_output.put_line('rule is '||rule_rd.id);
   END LOOP;
   CLOSE c_rule;

   /**
   import sales to menu;
   */
    dbms_output.put_line('menu_id: '||menu_id || ' ,v_sql: '|| v_sql);
   proc_rule_by_menu_datasql_deal(menu_id,v_sql,v_new_menu,site_id,provider_id,updateCount);
   EXCEPTION
     WHEN others then
       dbms_output.put_line(substr(sqlerrm,0,500));
       rollback;
end proc_rule_by_menu_deal;
/
