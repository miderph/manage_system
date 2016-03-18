create or replace view v_getappdownloadurl_client as
select app.c_id ,app.app_name name ,app.version,app.version_code ,url.id url_id,url.download_url,url.md5sum,url.url_type
    from zl_cont_appstore app inner join zl_app_download_url url
         on app.c_id = url.c_id and url.c_type =1  and (app.version_code = url.version_code and app.version = url.version) and url.download_url is not null
            order by url.url_type desc, url.modify_time desc;
