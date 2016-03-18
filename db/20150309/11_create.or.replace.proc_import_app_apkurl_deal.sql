create or replace procedure proc_import_app_apkurl_deal(
p_providerId in integer, as_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_appUpdateStatus integer;
   v_appInsertStatus integer;
   v_urlType_local integer;
   v_urlType_isp integer;
begin
   v_appUpdateStatus := p_doingStatus+2;
   v_appInsertStatus := p_doingStatus+1;
   v_urlType_local := 0;--升级地址的类型：0 本地服务器地址,1 第三方应用普通地址,2 baidu云盘,3 360云盘
   v_urlType_isp := 1;--升级地址的类型：0 本地服务器地址,1 第三方应用普通地址,2 baidu云盘,3 360云盘
   updateCount := 0;

   --统一处理：apk ZL_APP_DOWNLOAD_URL
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus--设置update标记
      where provider_id=p_providerId and import_status=p_prepareStatus and id in (
         select import.id from ZL_APP_BASE_INFO_IMPORT import,ZL_APP_DOWNLOAD_URL down
            where import.provider_id=down.provider_id and import.package_name=down.package_name and import.version_code=down.version_code
      );
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_appInsertStatus--设置insert标记
      where provider_id=p_providerId and import_status=p_prepareStatus;
   --insert
   --不在自己服务器上保存下载apk
   --insert into ZL_APP_DOWNLOAD_URL(ID,C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
   --   select ID,C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,v_urlType_local URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT
   --      where provider_id=p_providerId and import_status=v_appInsertStatus;
   insert into ZL_APP_DOWNLOAD_URL(ID,C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
      select ZLSQ_DATA.NEXTVAL ID,C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,ISP_DOWNLOAD_URL,v_urlType_isp URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT
         where provider_id=p_providerId and import_status=v_appInsertStatus;
   --update
   --update ZL_APP_DOWNLOAD_URL down
   --   set (C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
   --      =(select C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,v_urlType_local URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT import
   --         where import.id=down.id
   --      )
   --   where URL_TYPE=v_urlType_local and id in (select id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_appUpdateStatus);
   update ZL_APP_DOWNLOAD_URL down
      set (C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
         =(select C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,ISP_DOWNLOAD_URL,v_urlType_isp URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT import
            where import.id=down.id
         )
      where URL_TYPE=v_urlType_isp and id in (select id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_appUpdateStatus);
   update ZL_APP_BASE_INFO_IMPORT set import_status=p_prepareStatus
      where provider_id=p_providerId and (import_status=v_appUpdateStatus or import_status=v_appInsertStatus);--恢复到p_doingStatus
   updateCount := sql%rowcount;
   commit;

end proc_import_app_apkurl_deal;
