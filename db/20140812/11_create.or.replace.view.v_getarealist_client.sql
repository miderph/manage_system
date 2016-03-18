create or replace view v_getarealist_client as
select id, parent_id, name, code, description, status, to_char(create_time,'yyyy-mm-dd hh24:mi:ss') create_time, to_char(modify_time,'yyyy-mm-dd hh24:mi:ss') modify_time
,area_level
from zl_area
where id>0 and status >= 1
order by id asc;

