create or replace view v_base_img_for_menu_client as
select
    img.target_id, (img.url_little) url_little, (img.url) url, url_icon,img.url_4_squares, img.url_6_squares, img.is_url_used
    , plat.plat
from zl_img img
     join zl_plat_group pg on (img.platgroup_id=pg.id and sysdate between pg.active_time and pg.deactive_time and pg.status >= 1)
     join zl_rela_plat_group rpg on (img.platgroup_id=rpg.g_id)
     join zl_plat plat on (rpg.plat=plat.plat and plat.status >=1)
where sysdate between img.active_time and img.deactive_time and img.use_type='0'
/*
select
    img.target_id, max(img.url_little) url_little, max(img.url) url
    , plat.plat
from zl_img img
     join zl_plat_group pg on (img.platgroup_id=pg.id and sysdate between pg.active_time and pg.deactive_time and pg.status >= 1)
     join zl_rela_plat_group rpg on (img.platgroup_id=rpg.g_id)
     join zl_plat plat on (rpg.plat=plat.plat and plat.status >=1)
where sysdate between img.active_time and img.deactive_time and img.use_type='0'
group by img.target_id, plat.plat
order by img.target_id, plat.plat
*/;
