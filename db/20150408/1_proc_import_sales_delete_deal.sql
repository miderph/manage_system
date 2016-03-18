create or replace procedure proc_import_sales_delete_deal(
p_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, updateCount out integer)
is
   v_app_type integer;
   v_sql varchar(1024);
   v_url_field varchar(50);
begin
   v_app_type := 8;--视频购物
   -- p_doingStatus = 5000    p_prepareStatus =9999
   update ZL_CONT_SALES_IMPORT im  set import_status=p_doingStatus,c_id=id
      where provider_id=p_providerId and import_status=p_prepareStatus and update_status = -1 and exists (select * from ZL_CONT_SALES  sales where sales.sales_no = im.sales_no  );

   updateCount := sql%rowcount;

   --zl_cont_appstore 的 package name 被改了，处理为新数据
   --update ZL_CONT_SALES_IMPORT import set c_id=ZLSQ_DATA.NEXTVAL where import.provider_id=p_providerId and import_status=p_doingStatus and c_id in (select c_id from zl_cont_sales);

   --ZL_IMG
   --for ii in 1..2 loop

      --insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
      --   select 1 PLATGROUP_ID,c_id,as_providerId,1,pic_url1 from ZL_CONT_SALES_IMPORT
      --      where provider_id=p_providerId and import_status=v_doingStatus and pic_url1 is not null;


      v_sql := 'delete from  ZL_IMG zi where zi .target_id in ('
               ||'   select c_id from ZL_CONT_SALES_IMPORT im '
               ||'     where provider_id='||p_providerId||' and import_status='||p_doingStatus||' and update_status = -1 )';
      dbms_output.put_line(v_sql);
      execute immediate v_sql;
   --end loop;

   --apk ZL_APP_DOWNLOAD_URL
   --统一处理

   --ZL_CONT
   delete from  ZL_CONT zc where zc.id in (
      select C_ID from ZL_CONT_SALES_IMPORT im
         where provider_id=p_providerId and import_status=p_doingStatus and update_status = -1);
   --ZL_CONT_VIDEO
   delete from  ZL_CONT_VIDEO zcv where zcv.c_id in (
      select C_ID from ZL_CONT_SALES_IMPORT im
         where provider_id=p_providerId and import_status=p_doingStatus and update_status = -1);
   --ZL_CONT_SALES
   delete from  ZL_CONT_SALES zcs where zcs.c_id in (
      select C_ID from ZL_CONT_SALES_IMPORT im
         where provider_id=p_providerId and import_status=p_doingStatus and update_status = -1);
   --恢复到p_prepareStatus
   update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_sales_delete_deal;
/
