update zl_cont set modify_time=create_time where type in (7,8) and modify_time is null;

update zl_app_download_url t1 set id=ZLSQ_DATA.NEXTVAL
where exists(select id from zl_app_download_url t2 where t2.c_id=t1.c_id and t2.version=t1.version and t2.version_code=t1.version_code and t2.rowid<t1.rowid);

delete from zl_app_download_url t1
where exists(select id from zl_app_download_url t2 where t2.c_id=t1.c_id and t2.version=t1.version and t2.version_code=t1.version_code and t2.url_type=t1.url_type and t2.rowid<t1.rowid);
