create or replace view v_getcontentlist_client as
select
   mc.is_url_used rela_is_url_used,
   mc.menu_id,mc.order_num,menu.isp_menu_code,menu.root_menu_path
   , ct.c_id, ct.name/*, ct.description*/, ct.publishtime, ct.play_url, ct.tv_play_url
   , ct.play_num, ct.focus_count, ct.comment_count, ct.book_count, ct.rating
   , ct.rating_count, ct.watching_count, ct.seen_count, ct.wanted_count
   , ct.is_hd, ct.is_hot, ct.is_new
   , ct.provider_id, ct.provider_name,ct.isp_cont_code
   , ct.type, ct.ad_type, ct.package_name,ct.superscript_id
   --, epg.id epgid, epg.title epgtitle, epg.duration, epg.channel_id, to_char(epg.begin_time,'yyyy-mm-dd hh24:mi:ss') begin_time, to_char(epg.end_time,'yyyy-mm-dd hh24:mi:ss') end_time
   , null epgid, null epgtitle, null duration, null channel_id, null begin_time, null end_time
from v_base_content_client ct
     join zl_rela_menu_cont mc on (mc.c_id = ct.c_id)
     join zl_menu menu on (menu.id = mc.menu_id)
     --left join zl_epg epg on ct.epg_id = epg.id
--order by mc.menu_id,mc.order_num
/*
select rownum row_num
, ct.*
				 , img.*
                 from v_getcontentlist_client ct
			     left outer join v_base_img_for_cont_client img on (ct.c_id = img.target_id and plat='android')
where ct.menu_id=6
order by mc.menu_id,mc.order_num
*/
/*
select
   rmc.*
   , img.url_little icon_url, img.url img_url
   , epg.id epgid, epg.title epgtitle, epg.duration, epg.channel_id, to_char(epg.begin_time,'yyyy-mm-dd hh24:mi:ss') begin_time, to_char(epg.end_time,'yyyy-mm-dd hh24:mi:ss') end_time
from (
        select
           mc.menu_id,mc.order_num
           , cv.c_id, cv.name, decode(cv.t_show_years,0,'Î´Öª',cv.t_show_years||'Äê' || decode(cv.t_show_months,0,'',cv.t_show_months||'ÔÂ') ) publishtime
           , cv.play_num, cv.focus_count, cv.comment_count, cv.book_count, cv.rating
           , cv.rating_count, cv.watching_count, cv.seen_count, cv.wanted_count
           , cv.is_hd, cv.is_hot, cv.is_new
           , ct.type
        from zl_rela_menu_cont mc
             left join zl_cont_video cv on (mc.c_id = cv.c_id)
             left join zl_cont ct on (cv.c_id = ct.id and sysdate between ct.active_time and ct.deactive_time)
     ) rmc
     left join zl_img img on rmc.c_id = img.target_id and img.use_type='1' and sysdate between img.active_time and img.deactive_time
     left join zl_epg epg on rmc.c_id = epg.content_id and sysdate between epg.active_time and epg.deactive_time
order by rmc.order_num asc
*/;
