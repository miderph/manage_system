update zl_app_download_url set id=ZLSQ_DATA.Nextval,site=null where provider_id=90 and id in (select id from zl_app_base_info_import);
update zl_app_download_url set modify_time=create_time where modify_time is null;
update zl_app_download_url set md5sum=upper(md5sum);
delete from zl_app_download_url where provider_id in (91,92,215591962) and url_type=0;

