create or replace view v_batchcheckupdate_client as
select
   distinct ct.c_id,ct.name, ct.package_name,ct.version,ct.version_code ,menu.site_id,menu.usergroup_id
from v_getappdetail_client ct
     join zl_rela_menu_cont mc on (mc.c_id = ct.c_id)
     join v_base_menu_client menu on (menu.id = mc.menu_id);
