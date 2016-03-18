create or replace procedure proc_import_sales_delete_deal(
p_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, updateCount out integer)
is
   --v_sql varchar(1024);
   --v_url_field varchar(50);
begin
   --已锁定的，只更新修改时间和update_status
   update zl_cont set update_status=-1 where status=10 and locked=1 and id in (
      select im.id from ZL_CONT_SALES_IMPORT im where im.provider_id=p_providerId and im.update_status<=0
   );

   --未锁定的，下线
   if (p_providerId = 215633968) then--赚宝的隔天无更新再下线
      update zl_cont set status=-1,update_status=-1 where status=10 and modify_time<trunc(sysdate-2) and locked=0
         and id in (
            select id from ZL_CONT_SALES im where im.channel_id=p_providerId
         );
      updateCount := sql%rowcount;
   else
      update zl_cont set status=-1,update_status=-1 where status=10 and locked=0 and id in (
         select im.id from ZL_CONT_SALES_IMPORT im where im.provider_id=p_providerId and im.update_status<=0
      );
      updateCount := sql%rowcount;
   end if;
   dbms_output.put_line('#delete,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_cont,count='||updateCount);
   commit;

   --清除编排关系
   delete from ZL_RELA_MENU_CONT where locked=0 and c_id in (
      select im.id from ZL_CONT_SALES_IMPORT im where im.provider_id=p_providerId and im.update_status<=0
   ) and c_id in (
      select id from zl_cont where status<=0
   );  
   dbms_output.put_line('#delete,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',ZL_RELA_MENU_CONT,count='||updateCount);
   commit;
   --zl_img:update status to invalid,如果没在导入临时表中，则自动下线
   /*忽略图片下线v_loop_count := 0;
   loop
      delete from zl_img where rownum<=v_block_size and (update_status=v_tempStatus) and id in (
         select m1.id from zl_img m1,ZL_CONT_SALES_IMPORT im1 where m1.provider_id=p_providerId and m1.id=im1.id(+) and im1.id is null
      );
      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#invalid,zl_img,'||v_loop_count||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop;*/
   dbms_output.put_line('#delete,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_img,count=0');

   --恢复到p_prepareStatus
   --update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_sales_delete_deal;
/