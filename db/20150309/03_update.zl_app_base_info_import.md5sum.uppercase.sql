update zl_app_base_info_import set modify_time=create_time where modify_time is null;
update zl_app_base_info_import set md5sum=upper(md5sum);
