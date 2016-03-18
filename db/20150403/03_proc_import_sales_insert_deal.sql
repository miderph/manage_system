create or replace procedure proc_import_sales_insert_deal(
p_providerId in integer, as_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_app_type integer;
   v_sql varchar(1024);
   v_url_field varchar(50);
begin
   v_app_type := 8;--视频购物
   -- p_doingStatus = 7000    p_prepareStatus =9999
   update ZL_CONT_SALES_IMPORT im  set import_status=p_doingStatus,c_id=id
      where provider_id=p_providerId and import_status=p_prepareStatus and not exists (select * from ZL_CONT_SALES  sales where sales.sales_no = im.sales_no  );

   updateCount := sql%rowcount;

   --zl_cont_appstore 的 package name 被改了，处理为新数据
   --update ZL_CONT_SALES_IMPORT import set c_id=ZLSQ_DATA.NEXTVAL where import.provider_id=p_providerId and import_status=p_doingStatus and c_id in (select c_id from zl_cont_sales);

   --ZL_IMG
   --for ii in 1..2 loop

      --insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
      --   select 1 PLATGROUP_ID,c_id,as_providerId,1,pic_url1 from ZL_CONT_SALES_IMPORT
      --      where provider_id=p_providerId and import_status=v_doingStatus and pic_url1 is not null;


      v_sql := 'insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,URL_ICON,URL_4_SQUARES,URL_LITTLE,URL)'
               ||'   select 1 PLATGROUP_ID,c_id,'||as_providerId||',1,icon_url1,icon_url2,icon_url2,icon_url3  from ZL_CONT_SALES_IMPORT'
               ||'     where provider_id='||p_providerId||' and import_status='||p_doingStatus||' and ( icon_url1 is not null or  icon_url2 is not null )';
      dbms_output.put_line(v_sql);
      execute immediate v_sql;
   --end loop;

   --apk ZL_APP_DOWNLOAD_URL
   --统一处理

   --ZL_CONT
   insert into ZL_CONT(ID,TYPE,STATUS,NAME,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,PROVIDER_ID,UPDATE_STATUS,pinyin)
      select C_ID,v_app_type,p_contImportStatusValue,NAME,CREATE_TIME,MODIFY_TIME,sysdate-1,sysdate+3650,as_providerId,1 UPDATE_STATUS,pinyin from ZL_CONT_SALES_IMPORT
         where provider_id=p_providerId and import_status=p_doingStatus;
   --ZL_CONT_VIDEO
   insert into ZL_CONT_VIDEO(C_ID,NAME,ALIAS,PROVIDER_ID,UPDATE_STATUS)
      select C_ID,NAME,NAME,as_providerId,1 UPDATE_STATUS from ZL_CONT_SALES_IMPORT
         where provider_id=p_providerId and import_status=p_doingStatus;
   --ZL_CONT_SALES
   insert into ZL_CONT_SALES(C_ID,SALES_NO,PROVIDER_ID,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,PAY_TYPE_IDS,Has_Video,URL,manage_user,audit_review,shop_type,detail_Pic_File)
      select c_id,SALES_NO,as_providerId,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,PAY_TYPE_IDS,has_video,URL,manage_user,audit_review,shop_type,detail_Pic_File from ZL_CONT_SALES_IMPORT
         where provider_id=p_providerId and import_status=p_doingStatus;
   --恢复到p_prepareStatus
   update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_sales_insert_deal;
/
