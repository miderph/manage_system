----------------------------------------------------
-- Export file for user ZLINKTV_QDGH01            --
-- Created by Administrator on 2014-6-3, 11:07:49 --
----------------------------------------------------

spool db_qdgh_app_proc.log

prompt
prompt Creating function CALCULATE_TIME
prompt ================================
prompt
create or replace function calculate_time(p_startTime in date, p_endTime in date) return varchar2 is
  Result varchar2(100);
begin

  Result := to_char(p_endTime-p_startTime+to_date('2012-12-12','yyyy-mm-dd'),'hh24:mi:ss');
  return Result;
end calculate_time;
/

prompt
prompt Creating procedure PROC_IMPORT_APP_APKURL_DEAL
prompt ==============================================
prompt
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
   insert into ZL_APP_DOWNLOAD_URL(ID,C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
      select ID,C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,v_urlType_local URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT
         where provider_id=p_providerId and import_status=v_appInsertStatus;
   insert into ZL_APP_DOWNLOAD_URL(ID,C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
      select ID,C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,ISP_DOWNLOAD_URL,v_urlType_isp URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT
         where provider_id=p_providerId and import_status=v_appInsertStatus;
   --update
   update ZL_APP_DOWNLOAD_URL down
      set (C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
         =(select C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,v_urlType_local URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT import
            where import.id=down.id
         )
      where URL_TYPE=v_urlType_local and id in (select id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_appUpdateStatus);
   update ZL_APP_DOWNLOAD_URL down
      set (C_ID,APP_NAME,C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,URL_TYPE,md5sum)
         =(select C_ID,APP_NAME,1 C_TYPE,PACKAGE_NAME,PROVIDER_ID,VERSION,VERSION_CODE,SITE,CAPACITY,ADD_TIME,CREATE_TIME,MODIFY_TIME,DOWNLOAD_URL,v_urlType_isp URL_TYPE,md5sum from ZL_APP_BASE_INFO_IMPORT import
            where import.id=down.id
         )
      where URL_TYPE=v_urlType_isp and id in (select id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_appUpdateStatus);
   update ZL_APP_BASE_INFO_IMPORT set import_status=p_prepareStatus
      where provider_id=p_providerId and (import_status=v_appUpdateStatus or import_status=v_appInsertStatus);--恢复到p_doingStatus
   updateCount := sql%rowcount;
   commit;

end proc_import_app_apkurl_deal;
/

prompt
prompt Creating procedure PROC_IMPORT_APP_INSERT_DEAL
prompt ==============================================
prompt
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
   insert into ZL_CONT_APPSTORE(C_ID,PACKAGE_NAME,PROVIDER_ID,APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,site,md5sum)
      select c_id,PACKAGE_NAME,as_providerId,APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,site,md5sum from ZL_APP_BASE_INFO_IMPORT
         where provider_id=p_providerId and import_status=p_doingStatus;
   --恢复到p_prepareStatus
   update ZL_APP_BASE_INFO_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_app_insert_deal;
/

prompt
prompt Creating procedure PROC_IMPORT_APP_UPDATE_DEAL
prompt ==============================================
prompt
create or replace procedure proc_import_app_update_deal(
p_providerId in integer, as_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_appUpdateStatus_gt_version integer;
   v_appUpdateStatus_eq_version integer;
   v_sql varchar(1024);
   v_imgUseType integer;
   v_tempStatus integer;
begin
   updateCount := 0;
   v_appUpdateStatus_gt_version := p_doingStatus+100;
   v_appUpdateStatus_eq_version := p_doingStatus+200;

/*
1,聚合apk下载地址
version相同的更新，version不同新加一条。协议返回时，查询版本号最大，优先级最高的。

2，图片聚合
1）	zl_img增加标识，锁定lock   0或空未锁定；  1 锁定
2）	zl_img增加site ，网站来源
3）	usetype =2 包含一张及以上人工修改过的截图时，新抓取的截图不能入库
4）	usetype =1 图标信息人工锁定时，为空的字段能更新
5）	usetype =1图标信息没有人工锁定时，本网站可以更新，
其他网站聚合时,抓取version= 当前version时为空的字段可以更新。
其他网站聚合时,抓取version>当前version时,字段都可以更新。

3，信息聚合
1)	zl_cont_appstore增加 lock锁定标识，   0或空未锁定；  1 锁定
2)	zl_cont_appstore增加site  网站来源
3)	信息人工锁定时，除version字段可以更新，其他不能更新
4)  信息没有人工锁定时，本网站可以都更新，
其他网站聚合时,抓取version= 当前version时为空的字段可以更新。
*/

-->>再做update，版本判断原则：
--  1）小于已有版本：不做update
--   (1)zl_cont,zl_cont_video,zl_cont_appstore
--     不做处理
--   (2)zl_img
--     不做处理
--   (3)ZL_APP_DOWNLOAD_URL：判断是否已有，有则update，无则insert
--     统一处理下载地址
--     无->insert
--     有->update
--
--  2）大于已有版本：
--    先标记，再更新 import_status=
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where id in (
             select import.id from ZL_APP_BASE_INFO_IMPORT import, zl_cont_appstore app
                where import.provider_id=p_providerId and import.import_status=p_prepareStatus and import.package_name=app.package_name and import.version_code>app.version_code and app.provider_id=as_providerId
          );
--    去除已锁定的,这2个有锁定标记zl_img,zl_cont_appstore
--    (1)zl_cont,zl_cont_video,zl_cont_appstore：update
--      标记锁定的，锁定的只能更新version
          v_tempStatus := v_appUpdateStatus_gt_version+1;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select app.c_id from zl_cont_appstore app where app.provider_id=as_providerId and app.LOCKED=1
          );
          update zl_cont_appstore app set (version,version_code)=
            (select version,version_code from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--恢复标记
          updateCount := updateCount + sql%rowcount;
--      标记未锁定的，字段都更新
          v_tempStatus := v_appUpdateStatus_gt_version+2;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select app.c_id from zl_cont_appstore app where app.provider_id=as_providerId and (app.LOCKED is null or app.LOCKED!=1)
          );
          --ZL_CONT
          update ZL_CONT cont set (NAME,DESCRIPTION,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,UPDATE_STATUS,pinyin)=
            (select APP_NAME,DESCRIPTION,CREATE_TIME,MODIFY_TIME,sysdate-1,sysdate+3650,2 UPDATE_STATUS,pinyin from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=cont.id)
            where id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          --ZL_CONT_VIDEO
          update ZL_CONT_VIDEO cont set (NAME,ALIAS,DESCRIPTION,RATING,UPDATE_STATUS)
            =(select APP_NAME,APP_ALIAS,DESCRIPTION,RATING,2 UPDATE_STATUS from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=cont.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          --ZL_CONT_APPSTORE
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum)=
            (select APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--恢复标记
          updateCount := updateCount + sql%rowcount;
--    (2)zl_img：失效已有的，insert新的
--      标记锁定的，锁定的为空的字段能更新
          v_tempStatus := v_appUpdateStatus_gt_version+3;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select img.target_id from zl_img img where img.provider_id=as_providerId and img.LOCKED=1
          );
          update zl_img img set url=
             (select pic_url1 from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=img.target_id)
             where img.provider_id=p_providerId and use_type=1 and url is null and target_id in (
                select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
             );
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--恢复标记
--        大图：抓取的默认为横图，更新横图为空的记录
--        截图：不做处理
--      未锁定的，
          v_tempStatus := v_appUpdateStatus_gt_version+4;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select img.target_id from zl_img img where img.provider_id=p_providerId and (img.LOCKED is null or img.LOCKED!=1)
          );
--        大图：抓取的默认为横图，更新为横图
          v_imgUseType := 1;--:1为应用默认图，2为截图
          update zl_img img set active_time=sysdate-1,deactive_time=sysdate+2000,url=
             (select pic_url1 from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=img.target_id)
             where img.provider_id=p_providerId and use_type=v_imgUseType and target_id in (
                select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
             );
          insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
             select 1 PLATGROUP_ID,c_id,as_providerId,v_imgUseType,pic_url1 from ZL_APP_BASE_INFO_IMPORT import left outer join zl_img img on (import.c_id=img.target_id and img.use_type=1 and img.provider_id=as_providerId)
                where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.pic_url1 is not null and img.id is null;
--        截图：失效原有的，insert新的（新的截图数不为0）
--           标记新的截图数不为0的，失效原有的，insert新的
          v_imgUseType := 2;--:1为应用默认图，2为截图
          update zl_img img set deactive_time=active_time--图片失效
             where img.provider_id=p_providerId and use_type=v_imgUseType and target_id in (
                select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
             );
          --ZL_IMG
          for ii in 2..6 loop
             --insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
             --   select 1 PLATGROUP_ID,c_id,as_providerId,1,pic_url1 from ZL_APP_BASE_INFO_IMPORT
             --      where provider_id=p_providerId and import_status=v_tempStatus and pic_url1 is not null;
             v_sql := 'insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)'
                      ||'   select 1 PLATGROUP_ID,c_id,'||as_providerId||','||v_imgUseType||',pic_url'||ii||' from ZL_APP_BASE_INFO_IMPORT'
                      ||'     where provider_id='||p_providerId||' and import_status='||v_tempStatus||' and pic_url'||ii||' is not null';
             execute immediate v_sql;

             <<continue_loop_22_1>>
             null;
          end loop;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--恢复标记
--    (3)ZL_APP_DOWNLOAD_URL：判断是否已有，有则update，无则insert
--      统一处理下载地址
--
--
--  3）版本相同：
--    先标记，再更新 import_status=6000
--    去除已锁定的,这2个有锁定标记zl_img,zl_cont_appstore
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_eq_version where id in (
             select import.id from ZL_APP_BASE_INFO_IMPORT import, zl_cont_appstore app
                where import.provider_id=p_providerId and import.import_status=p_prepareStatus and import.package_name=app.package_name and import.version_code=app.version_code and app.provider_id=as_providerId
          );
--    (1)zl_cont,zl_cont_video,zl_cont_appstore：补空字段(锁定的和其他网站的)
          v_tempStatus := v_appUpdateStatus_eq_version+1;
          --锁定的
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select app.c_id from zl_cont_appstore app where app.provider_id=as_providerId and app.LOCKED=1
          );
          --其他网站的
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where id in (
             select import.id from ZL_APP_BASE_INFO_IMPORT import, zl_cont_appstore app
                where import.provider_id=p_providerId and import.import_status=v_appUpdateStatus_eq_version and import.package_name=app.package_name and import.version_code=app.version_code and app.provider_id=as_providerId and import.site!=app.site
          );

          --ZL_CONT
          update ZL_CONT cont set (NAME,DESCRIPTION,MODIFY_TIME,UPDATE_STATUS,pinyin)=
            (select nvl(cont.NAME,import.APP_NAME),nvl(cont.DESCRIPTION,import.DESCRIPTION),nvl(cont.MODIFY_TIME,import.MODIFY_TIME),2 UPDATE_STATUS,nvl(cont.pinyin,import.pinyin)
               from ZL_APP_BASE_INFO_IMPORT import
               where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=cont.id)
            where id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          --ZL_CONT_VIDEO
          update ZL_CONT_VIDEO cont set (NAME,ALIAS,DESCRIPTION,RATING,UPDATE_STATUS)
            =(select nvl(cont.NAME,import.APP_NAME),nvl(cont.ALIAS,import.APP_ALIAS),nvl(cont.DESCRIPTION,import.DESCRIPTION),nvl(cont.RATING,import.RATING),2 UPDATE_STATUS
                from ZL_APP_BASE_INFO_IMPORT import
                where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=cont.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          --zl_cont_appstore
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,capacity,download_url,md5sum)=
            (select nvl(app.APP_NAME,import.APP_NAME),nvl(app.STAFF,import.STAFF),nvl(app.ADD_TIME,import.ADD_TIME),nvl(app.version,import.version),nvl(app.capacity,import.capacity),nvl(app.download_url,import.download_url),nvl(app.md5sum,import.md5sum)
               from ZL_APP_BASE_INFO_IMPORT import
               where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
         --本网站的都可以更新
          v_tempStatus := v_appUpdateStatus_eq_version+2;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where id in (
             select import.id from ZL_APP_BASE_INFO_IMPORT import, zl_cont_appstore app
                where import.provider_id=p_providerId and import.import_status=v_appUpdateStatus_eq_version and import.package_name=app.package_name and import.version_code=app.version_code and app.provider_id=as_providerId and import.site=app.site
          );
          --ZL_CONT
          update ZL_CONT cont set (NAME,DESCRIPTION,MODIFY_TIME,UPDATE_STATUS,pinyin)=
            (select APP_NAME,DESCRIPTION,MODIFY_TIME,2 UPDATE_STATUS,pinyin from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=cont.id)
            where id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          --ZL_CONT_VIDEO
          update ZL_CONT_VIDEO cont set (NAME,ALIAS,DESCRIPTION,RATING,UPDATE_STATUS)
            =(select APP_NAME,APP_ALIAS,DESCRIPTION,RATING,2 UPDATE_STATUS from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=cont.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          --ZL_CONT_APPSTORE
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum)=
            (select APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
--    (2)zl_img：不做update
--      标记锁定的，锁定的为空的字段能更新
--        大图：抓取的默认为横图，更新横图为空的记录
--        截图：原有截图数为0的，insert新的
--           标记新的截图数不为0的，并且对应的原有截图数为0的，insert新的
--
--    (3)ZL_APP_DOWNLOAD_URL：判断是否已有，有则update，无则insert
--      统一处理下载地址

          update ZL_APP_BASE_INFO_IMPORT set import_status=p_prepareStatus
             where import_status=v_appUpdateStatus_gt_version or import_status=v_appUpdateStatus_eq_version;--恢复标记
         commit;

end proc_import_app_update_deal;
/

prompt
prompt Creating procedure PROC_IMPORT_APP_DEAL
prompt =======================================
prompt
create or replace procedure proc_import_app_deal(p_providerId in integer, updateCount out varchar2) is
   v_prepareStatus integer;
   v_okStatus integer;
   v_doingStatus integer;
   v_contImportStatusValue integer;
   v_tempId integer;
   as_providerId integer;

   not_found_data EXCEPTION;
   strTemp varchar2(1000);
   v_startTime date;
   v_endTime date;
begin
   --处理原则：
   --1,通过标记import_status来处理insert和update,insert和update分开处理
   --2,统一处理下载地址，分为apk、insert和update三个子过程
   --4,总过程有一个标记v_doingStatus=9999，各子过程有自己的标记，子过程处理完成后需要把标记回复到处理前的值
   --5,各子过程都处理完后，总过程会把标记设置为v_okStatus=1

   as_providerId := 90;--很重要，所有内容（不含下载地址）都做为播亦乐的资产
   v_prepareStatus := 0;
   v_okStatus := 1;
   v_doingStatus := 9999;
   v_contImportStatusValue := 10; --自动导入的状态值

   --是否有没处理的数据，没有则报异常
   v_endTime := sysdate;strTemp := '#proc_import_app_deal,provider_id='||p_providerId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   select count(1) into v_tempId from ZL_APP_BASE_INFO_IMPORT where (update_status >= 1 or update_status is null);
   if v_tempId = 0 then
      raise not_found_data;
   end if;

   --delete from ZL_CONT_APPSTORE
   --where c_id in (
   --   select app.c_id from ZL_CONT_APPSTORE app,zl_cont_video cont
   --   where app.c_id=cont.c_id(+) and cont.c_id is null
   --);

   --标记要处理的数据
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_prepareStatus where provider_id=p_providerId and import_status is null;
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_doingStatus where provider_id=p_providerId and import_status=v_prepareStatus;
   update ZL_APP_BASE_INFO_IMPORT set import_status=-1 where provider_id=p_providerId and import_status=v_doingStatus and DOWNLOAD_URL is null;
   commit;

   --处理insert：ZL_APP_BASE_INFO_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_app_insert_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_import_app_insert_deal(p_providerId, as_providerId, v_doingStatus,v_doingStatus-2999,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_appstore=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_app_insert_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime)||',count='||v_tempId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   if v_tempId = 0 then--双重保护,没有新增数据
      null;--raise not_found_data;
   end if;
   --insert ok
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-2999;
   commit;

   --处理update： ZL_APP_BASE_INFO_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_app_update_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_import_app_update_deal(p_providerId, as_providerId, v_doingStatus,v_doingStatus-3999,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_appstore=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_app_update_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime)||',count='||v_tempId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   --update ok
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-3999;
   commit;

   --统一处理：apk ZL_APP_DOWNLOAD_URL
   v_startTime := sysdate;strTemp := '#proc_import_app_apkurl_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_import_app_apkurl_deal(p_providerId, as_providerId, v_doingStatus,v_doingStatus-1999,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_appstore=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_app_apkurl_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime)||',count='||v_tempId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);

   --all ok
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus;
   commit;
end proc_import_app_deal;
/


spool off
