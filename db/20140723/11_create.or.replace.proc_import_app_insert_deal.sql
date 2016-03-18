create or replace procedure proc_import_app_insert_deal(
p_providerId in integer, as_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_app_type integer;
   v_sql varchar(1024);
   v_imgUseType integer;
begin
   v_app_type := 7;--资产应用类型

   update ZL_APP_BASE_INFO_IMPORT set import_status=p_doingStatus,c_id=id
      where provider_id=p_providerId and import_status=p_prepareStatus and id in (
         select import.id from ZL_APP_BASE_INFO_IMPORT import left outer join ZL_CONT_APPSTORE app on (import.package_name=app.package_name and app.provider_Id=as_providerId)
            where import.provider_id=p_providerId and app.provider_Id is null
      );
   updateCount := sql%rowcount;
   if (updateCount = 0) then
      dbms_output.put_line('#proc_import_app_insert_deal,found 0 new data!');
   else
      --ZL_IMG
      for ii in 1..6 loop
         v_imgUseType := 1;--1为应用默认图，2为截图
         if (ii > 1)then
            v_imgUseType := 2;--1为应用默认图，2为截图
         end if;
         --insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
         --   select 1 PLATGROUP_ID,c_id,as_providerId,1,pic_url1 from ZL_APP_BASE_INFO_IMPORT
         --      where provider_id=p_providerId and import_status=v_doingStatus and pic_url1 is not null;
         v_sql := 'insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)'
                  ||'   select 1 PLATGROUP_ID,c_id,'||as_providerId||','||v_imgUseType||',pic_url'||ii||' from ZL_APP_BASE_INFO_IMPORT'
                  ||'     where provider_id='||p_providerId||' and import_status='||p_doingStatus||' and pic_url'||ii||' is not null';
         execute immediate v_sql;
      
         <<continue_loop_22_1>>
         null;
      end loop;
      
      --apk ZL_APP_DOWNLOAD_URL
      --统一处理
      
      --ZL_CONT
      insert into ZL_CONT(ID,TYPE,STATUS,NAME,DESCRIPTION,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,PROVIDER_ID,UPDATE_STATUS,pinyin)
         select C_ID,v_app_type,p_contImportStatusValue,APP_NAME,DESCRIPTION,CREATE_TIME,MODIFY_TIME,sysdate-1,sysdate+3650,as_providerId,1 UPDATE_STATUS,pinyin from ZL_APP_BASE_INFO_IMPORT
            where provider_id=p_providerId and import_status=p_doingStatus;
      --ZL_CONT_VIDEO
      insert into ZL_CONT_VIDEO(C_ID,NAME,ALIAS,DESCRIPTION,RATING,PROVIDER_ID,PACKAGE_NAME,UPDATE_STATUS)
         select C_ID,APP_NAME,APP_ALIAS,DESCRIPTION,RATING,as_providerId,PACKAGE_NAME,1 UPDATE_STATUS from ZL_APP_BASE_INFO_IMPORT
            where provider_id=p_providerId and import_status=p_doingStatus;
      --ZL_CONT_APPSTORE
      insert into ZL_CONT_APPSTORE(C_ID,PACKAGE_NAME,PROVIDER_ID,APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,site,md5sum,tags)
         select c_id,PACKAGE_NAME,as_providerId,APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,site,md5sum,tags from ZL_APP_BASE_INFO_IMPORT
            where provider_id=p_providerId and import_status=p_doingStatus;
      --恢复到p_prepareStatus
      update ZL_APP_BASE_INFO_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
      commit;
   end if;
   
end proc_import_app_insert_deal;
