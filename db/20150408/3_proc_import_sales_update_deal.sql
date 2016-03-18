create or replace procedure proc_import_sales_update_deal(
p_providerId in integer, as_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_app_type integer;
   v_sql varchar(1024);
   v_url_field varchar(50);
begin
   v_app_type := 8;--视频购物
   -- p_doingStatus = 6000    p_prepareStatus =9999
   update ZL_CONT_SALES_IMPORT im  set import_status=p_doingStatus
      where provider_id=p_providerId and import_status=p_prepareStatus and update_status=2 and exists (select * from ZL_CONT_SALES  sales where sales.sales_no = im.sales_no  );

   updateCount := sql%rowcount;

   --zl_cont_appstore 的 package name 被改了，处理为新数据
   --update ZL_CONT_SALES_IMPORT import set c_id=ZLSQ_DATA.NEXTVAL where import.provider_id=p_providerId and import_status=p_doingStatus and c_id in (select c_id from zl_cont_sales);

   --ZL_IMG
   --for ii in 1..2 loop

      --insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
      --   select 1 PLATGROUP_ID,c_id,as_providerId,1,pic_url1 from ZL_CONT_SALES_IMPORT
      --      where provider_id=p_providerId and import_status=v_doingStatus and pic_url1 is not null;


      v_sql := 'update ZL_IMG zi set (PLATGROUP_ID,provider_id,Use_Type,URL_ICON,URL_4_SQUARES,URL_LITTLE,URL)='
               ||'   (select 1 PLATGROUP_ID,'||as_providerId||',1,icon_url1,icon_url2,icon_url2,icon_url3  from ZL_CONT_SALES_IMPORT im where zi.target_id = im.c_id )'
               ||'     where zi.target_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id='||p_providerId||' and im2.import_status='||p_doingStatus||' and ( im2.icon_url1 is not null or  im2.icon_url2 is not null ) )';
      dbms_output.put_line(v_sql);
      execute immediate v_sql;
   --end loop;

   --apk ZL_APP_DOWNLOAD_URL
   --统一处理

   --ZL_CONT
   update ZL_CONT zc set ( TYPE,STATUS,NAME,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,PROVIDER_ID,UPDATE_STATUS,pinyin)=
      (select v_app_type,p_contImportStatusValue,NAME,CREATE_TIME,MODIFY_TIME,sysdate-1,sysdate+3650,as_providerId,1 UPDATE_STATUS,pinyin from ZL_CONT_SALES_IMPORT im where zc.id = im.c_id )
         where zc.id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=p_doingStatus );
   --ZL_CONT_VIDEO
   update ZL_CONT_VIDEO  zcv set (NAME,ALIAS,PROVIDER_ID,UPDATE_STATUS)=
      (select NAME,NAME,as_providerId,1 UPDATE_STATUS from ZL_CONT_SALES_IMPORT im where zcv.c_id = im.c_id )
         where zcv.c_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=p_doingStatus );
   --ZL_CONT_SALES
   update ZL_CONT_SALES zcs set (SALES_NO,PROVIDER_ID,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,PAY_TYPE_IDS,Has_Video,URL,manage_user,audit_review,shop_type)=
      (select SALES_NO,as_providerId,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,PAY_TYPE_IDS,has_video,URL,manage_user,audit_review,shop_type from ZL_CONT_SALES_IMPORT im where zcs.c_id = im.c_id )
         where zcs.c_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=p_doingStatus );
   --恢复到p_prepareStatus
   update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_sales_update_deal;
/
