create or replace view v_base_img_for_cont_client as
select
   img.platgroup_id, img.target_id, img.url_little, img.url, url_icon,img.is_url_used,img.is_url_used img_is_url_used ,img.url_4_squares,img.url_6_squares
   , plat.plat
from zl_img img
     join zl_plat_group pg on (img.platgroup_id=pg.id and sysdate between pg.active_time and pg.deactive_time and pg.status >= 1)
     join zl_rela_plat_group rpg on (img.platgroup_id=rpg.g_id)
     join zl_plat plat on (rpg.plat=plat.plat and plat.status >=1)
where sysdate between img.active_time and img.deactive_time and img.use_type='1';
