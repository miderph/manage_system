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

   update ZL_APP_BASE_INFO_IMPORT main set c_id=(
         select max(c_id) from ZL_CONT_APPSTORE app where (app.package_name=main.package_name and app.provider_Id=as_providerId)
      )
      where provider_id=p_providerId and import_status=p_prepareStatus and id in (
         select import.id from ZL_APP_BASE_INFO_IMPORT import left outer join ZL_CONT_APPSTORE app on (import.package_name=app.package_name and app.provider_Id=as_providerId)
            where import.provider_id=p_providerId and app.provider_Id is not null
      );
   commit;
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
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags)=
            (select APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
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
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,capacity,download_url,md5sum,tags)=
            (select nvl(app.APP_NAME,import.APP_NAME),nvl(app.STAFF,import.STAFF),nvl(app.ADD_TIME,import.ADD_TIME),nvl(app.version,import.version),nvl(app.capacity,import.capacity),nvl(app.download_url,import.download_url),nvl(app.md5sum,import.md5sum),nvl(app.tags,import.tags)
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
          update zl_cont_appstore app set (APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags)=
            (select APP_NAME,STAFF,ADD_TIME,version,version_code,capacity,download_url,md5sum,tags from ZL_APP_BASE_INFO_IMPORT import where import.provider_id=p_providerId and import.import_status=v_tempStatus and import.c_id=app.c_id)
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