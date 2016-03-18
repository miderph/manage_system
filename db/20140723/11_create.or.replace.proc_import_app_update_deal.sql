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

   update ZL_APP_BASE_INFO_IMPORT main set c_id=(
         select max(c_id) from ZL_CONT_APPSTORE app where (app.package_name=main.package_name and app.provider_Id=as_providerId)
      )
      where provider_id=p_providerId and import_status=p_prepareStatus and id in (
         select import.id from ZL_APP_BASE_INFO_IMPORT import left outer join ZL_CONT_APPSTORE app on (import.package_name=app.package_name and app.provider_Id=as_providerId)
            where import.provider_id=p_providerId and app.provider_Id is not null
      );
   commit;
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
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags)=
            (select APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
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
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,capacity,download_url,md5sum,tags)=
            (select nvl(app.APP_NAME,import.APP_NAME),nvl(app.STAFF,import.STAFF),nvl(app.ADD_TIME,import.ADD_TIME),nvl(app.version,import.version),nvl(app.capacity,import.capacity),nvl(app.download_url,import.download_url),nvl(app.md5sum,import.md5sum),nvl(app.tags,import.tags)
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
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags)=
            (select APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
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