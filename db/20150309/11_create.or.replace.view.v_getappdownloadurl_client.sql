create or replace view v_getappdownloadurl_client as
select app.c_id, app.app_name name, app.version, app.version_code, app.package_name, url.id url_id, url.md5sum
  ,url.download_url,url.url_type,url.upgrade_temp_url,url.temp_url_expire_time,url.share_password,url.provider_id
    from zl_cont_appstore app inner join zl_app_download_url url
         on app.c_id = url.c_id and url.c_type =1 and (app.version_code = url.version_code and app.version = url.version and app.md5sum=url.md5sum) and url.download_url is not null
            order by url.url_type desc, url.modify_time desc;