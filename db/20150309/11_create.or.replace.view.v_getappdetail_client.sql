create or replace view v_getappdetail_client as
select
    ct.ID c_id, ct.name, ct.description, ct.provider_id, ct.status
   , app.staff , app.version, app.capacity, to_char(app.add_time,'yyyy-mm-dd') add_time, app.download_url, app.md5sum
   , 'ÒæÖÇ' category, '2.2' min_sdk_version, 9 grade, 0 price, 0 download_count
   ,app.package_name,app.tags,app.version_code
from zl_cont ct
     join ZL_CONT_APPSTORE app on (ct.status >= 1 and sysdate between ct.active_time and ct.deactive_time and app.c_id = ct.id );