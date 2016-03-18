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
   v_urlType_local := 0;--������ַ�����ͣ�0 ���ط�������ַ,1 ������Ӧ����ͨ��ַ,2 baidu����,3 360����
   v_urlType_isp := 1;--������ַ�����ͣ�0 ���ط�������ַ,1 ������Ӧ����ͨ��ַ,2 baidu����,3 360����
   updateCount := 0;

   --ͳһ����apk ZL_APP_DOWNLOAD_URL
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus--����update���
      where provider_id=p_providerId and import_status=p_prepareStatus and id in (
         select import.id from ZL_APP_BASE_INFO_IMPORT import,ZL_APP_DOWNLOAD_URL down
            where import.provider_id=down.provider_id and import.package_name=down.package_name and import.version_code=down.version_code
      );
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_appInsertStatus--����insert���
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
      where provider_id=p_providerId and (import_status=v_appUpdateStatus or import_status=v_appInsertStatus);--�ָ���p_doingStatus
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
   v_app_type := 7;--�ʲ�Ӧ������

   update ZL_APP_BASE_INFO_IMPORT set import_status=p_doingStatus,c_id=id
      where provider_id=p_providerId and import_status=p_prepareStatus and id in (
         select import.id from ZL_APP_BASE_INFO_IMPORT import left outer join ZL_CONT_APPSTORE app on (import.package_name=app.package_name and app.provider_Id=as_providerId)
            where import.provider_id=p_providerId and app.provider_Id is null
      );
   updateCount := sql%rowcount;

   --ZL_IMG
   for ii in 1..6 loop
      v_imgUseType := 1;--1ΪӦ��Ĭ��ͼ��2Ϊ��ͼ
      if (ii > 1)then
         v_imgUseType := 2;--1ΪӦ��Ĭ��ͼ��2Ϊ��ͼ
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
   --ͳһ����

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
   --�ָ���p_prepareStatus
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
1,�ۺ�apk���ص�ַ
version��ͬ�ĸ��£�version��ͬ�¼�һ����Э�鷵��ʱ����ѯ�汾��������ȼ���ߵġ�

2��ͼƬ�ۺ�
1��	zl_img���ӱ�ʶ������lock   0���δ������  1 ����
2��	zl_img����site ����վ��Դ
3��	usetype =2 ����һ�ż������˹��޸Ĺ��Ľ�ͼʱ����ץȡ�Ľ�ͼ�������
4��	usetype =1 ͼ����Ϣ�˹�����ʱ��Ϊ�յ��ֶ��ܸ���
5��	usetype =1ͼ����Ϣû���˹�����ʱ������վ���Ը��£�
������վ�ۺ�ʱ,ץȡversion= ��ǰversionʱΪ�յ��ֶο��Ը��¡�
������վ�ۺ�ʱ,ץȡversion>��ǰversionʱ,�ֶζ����Ը��¡�

3����Ϣ�ۺ�
1)	zl_cont_appstore���� lock������ʶ��   0���δ������  1 ����
2)	zl_cont_appstore����site  ��վ��Դ
3)	��Ϣ�˹�����ʱ����version�ֶο��Ը��£��������ܸ���
4)  ��Ϣû���˹�����ʱ������վ���Զ����£�
������վ�ۺ�ʱ,ץȡversion= ��ǰversionʱΪ�յ��ֶο��Ը��¡�
*/

-->>����update���汾�ж�ԭ��
--  1��С�����а汾������update
--   (1)zl_cont,zl_cont_video,zl_cont_appstore
--     ��������
--   (2)zl_img
--     ��������
--   (3)ZL_APP_DOWNLOAD_URL���ж��Ƿ����У�����update������insert
--     ͳһ�������ص�ַ
--     ��->insert
--     ��->update
--
--  2���������а汾��
--    �ȱ�ǣ��ٸ��� import_status=
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where id in (
             select import.id from ZL_APP_BASE_INFO_IMPORT import, zl_cont_appstore app
                where import.provider_id=p_providerId and import.import_status=p_prepareStatus and import.package_name=app.package_name and import.version_code>app.version_code and app.provider_id=as_providerId
          );
--    ȥ����������,��2�����������zl_img,zl_cont_appstore
--    (1)zl_cont,zl_cont_video,zl_cont_appstore��update
--      ��������ģ�������ֻ�ܸ���version
          v_tempStatus := v_appUpdateStatus_gt_version+1;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select app.c_id from zl_cont_appstore app where app.provider_id=as_providerId and app.LOCKED=1
          );
          update zl_cont_appstore app set (version,version_code)=
            (select version,version_code from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
            where c_id in (
               select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
            );
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--�ָ����
          updateCount := updateCount + sql%rowcount;
--      ���δ�����ģ��ֶζ�����
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
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--�ָ����
          updateCount := updateCount + sql%rowcount;
--    (2)zl_img��ʧЧ���еģ�insert�µ�
--      ��������ģ�������Ϊ�յ��ֶ��ܸ���
          v_tempStatus := v_appUpdateStatus_gt_version+3;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select img.target_id from zl_img img where img.provider_id=as_providerId and img.LOCKED=1
          );
          update zl_img img set url=
             (select pic_url1 from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=img.target_id)
             where img.provider_id=p_providerId and use_type=1 and url is null and target_id in (
                select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
             );
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--�ָ����
--        ��ͼ��ץȡ��Ĭ��Ϊ��ͼ�����º�ͼΪ�յļ�¼
--        ��ͼ����������
--      δ�����ģ�
          v_tempStatus := v_appUpdateStatus_gt_version+4;
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select img.target_id from zl_img img where img.provider_id=p_providerId and (img.LOCKED is null or img.LOCKED!=1)
          );
--        ��ͼ��ץȡ��Ĭ��Ϊ��ͼ������Ϊ��ͼ
          v_imgUseType := 1;--:1ΪӦ��Ĭ��ͼ��2Ϊ��ͼ
          update zl_img img set active_time=sysdate-1,deactive_time=sysdate+2000,url=
             (select pic_url1 from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=img.target_id)
             where img.provider_id=p_providerId and use_type=v_imgUseType and target_id in (
                select c_id from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus
             );
          insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
             select 1 PLATGROUP_ID,c_id,as_providerId,v_imgUseType,pic_url1 from ZL_APP_BASE_INFO_IMPORT import left outer join zl_img img on (import.c_id=img.target_id and img.use_type=1 and img.provider_id=as_providerId)
                where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.pic_url1 is not null and img.id is null;
--        ��ͼ��ʧЧԭ�еģ�insert�µģ��µĽ�ͼ����Ϊ0��
--           ����µĽ�ͼ����Ϊ0�ģ�ʧЧԭ�еģ�insert�µ�
          v_imgUseType := 2;--:1ΪӦ��Ĭ��ͼ��2Ϊ��ͼ
          update zl_img img set deactive_time=active_time--ͼƬʧЧ
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
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_gt_version where import_status=v_tempStatus;--�ָ����
--    (3)ZL_APP_DOWNLOAD_URL���ж��Ƿ����У�����update������insert
--      ͳһ�������ص�ַ
--
--
--  3���汾��ͬ��
--    �ȱ�ǣ��ٸ��� import_status=6000
--    ȥ����������,��2�����������zl_img,zl_cont_appstore
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_appUpdateStatus_eq_version where id in (
             select import.id from ZL_APP_BASE_INFO_IMPORT import, zl_cont_appstore app
                where import.provider_id=p_providerId and import.import_status=p_prepareStatus and import.package_name=app.package_name and import.version_code=app.version_code and app.provider_id=as_providerId
          );
--    (1)zl_cont,zl_cont_video,zl_cont_appstore�������ֶ�(�����ĺ�������վ��)
          v_tempStatus := v_appUpdateStatus_eq_version+1;
          --������
          update ZL_APP_BASE_INFO_IMPORT set import_status=v_tempStatus where provider_id=p_providerId and import_status=v_appUpdateStatus_gt_version and c_id in (
             select app.c_id from zl_cont_appstore app where app.provider_id=as_providerId and app.LOCKED=1
          );
          --������վ��
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
         --����վ�Ķ����Ը���
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
--    (2)zl_img������update
--      ��������ģ�������Ϊ�յ��ֶ��ܸ���
--        ��ͼ��ץȡ��Ĭ��Ϊ��ͼ�����º�ͼΪ�յļ�¼
--        ��ͼ��ԭ�н�ͼ��Ϊ0�ģ�insert�µ�
--           ����µĽ�ͼ����Ϊ0�ģ����Ҷ�Ӧ��ԭ�н�ͼ��Ϊ0�ģ�insert�µ�
--
--    (3)ZL_APP_DOWNLOAD_URL���ж��Ƿ����У�����update������insert
--      ͳһ�������ص�ַ

          update ZL_APP_BASE_INFO_IMPORT set import_status=p_prepareStatus
             where import_status=v_appUpdateStatus_gt_version or import_status=v_appUpdateStatus_eq_version;--�ָ����
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
   --����ԭ��
   --1,ͨ�����import_status������insert��update,insert��update�ֿ�����
   --2,ͳһ�������ص�ַ����Ϊapk��insert��update�����ӹ���
   --4,�ܹ�����һ�����v_doingStatus=9999�����ӹ������Լ��ı�ǣ��ӹ��̴�����ɺ���Ҫ�ѱ�ǻظ�������ǰ��ֵ
   --5,���ӹ��̶���������ܹ��̻�ѱ������Ϊv_okStatus=1

   as_providerId := 90;--����Ҫ���������ݣ��������ص�ַ������Ϊ�����ֵ��ʲ�
   v_prepareStatus := 0;
   v_okStatus := 1;
   v_doingStatus := 9999;
   v_contImportStatusValue := 10; --�Զ������״ֵ̬

   --�Ƿ���û��������ݣ�û�����쳣
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

   --���Ҫ���������
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_prepareStatus where provider_id=p_providerId and import_status is null;
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_doingStatus where provider_id=p_providerId and import_status=v_prepareStatus;
   update ZL_APP_BASE_INFO_IMPORT set import_status=-1 where provider_id=p_providerId and import_status=v_doingStatus and DOWNLOAD_URL is null;
   commit;

   --����insert��ZL_APP_BASE_INFO_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_app_insert_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_import_app_insert_deal(p_providerId, as_providerId, v_doingStatus,v_doingStatus-2999,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_appstore=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_app_insert_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime)||',count='||v_tempId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   if v_tempId = 0 then--˫�ر���,û����������
      null;--raise not_found_data;
   end if;
   --insert ok
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-2999;
   commit;

   --����update�� ZL_APP_BASE_INFO_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_app_update_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_import_app_update_deal(p_providerId, as_providerId, v_doingStatus,v_doingStatus-3999,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_appstore=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_app_update_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime)||',count='||v_tempId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   --update ok
   update ZL_APP_BASE_INFO_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-3999;
   commit;

   --ͳһ����apk ZL_APP_DOWNLOAD_URL
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
