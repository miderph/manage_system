create or replace view v_base_menu_client as
select
   menu.id, menu.site_id, menu.parent_id, menu.title, menu.order_num, menu.struct_type, menu.act_type, menu.link_url, menu.islocal
   , menu.provider_id, menu.isp_menu_code, menu.root_menu_path
   ,menu.usergroup_id,menu.is_shortcut,menu.shortcut_contid,menu.version
   ,nvl(is_autoplay,0) is_autoplay
   ,usergroup_ids_mac,usergroup_ids_zone,usergroup_ids_model
from zl_menu menu
where sysdate between menu.ACTIVE_TIME and menu.DEACTIVE_TIME and menu.status >=1;
