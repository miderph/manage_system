create or replace view v_getcontentdetail_client as
select
    ct.c_id, ct.name, ct.description, ct.publishtime, ct.play_url, ct.tv_play_url
   , ct.play_num, ct.focus_count, ct.comment_count, ct.book_count, ct.rating
   , ct.rating_count, ct.watching_count, ct.seen_count, ct.wanted_count
   , ct.t_show_years, ct.t_show_months, ct.alias
   , ct.duration, ct.actors, ct.director, ct.screenwriter, ct.language, ct.has_volume, ct.vol_total
   , ct.is_hd, ct.is_hot, ct.is_new, ct.t_conttype_id, ct.t_region_id
   , ct.type
   , ct.isp_cont_code,ct.isp_menu_code
   , ct.provider_id
   , cvt.description cont_type
   ,null region--, area.name region
   ,ct.package_name
from v_base_content_client ct
     left join zl_video_conttype cvt on ct.t_conttype_id = cvt.id;
     --left join zl_area area on area.id = ct.t_region_id
/*
create or replace view v_getcontentdetail_client as
select
   cvs.*
   , img.url_little icon_url, img.url img_url
   , cvt.description cont_type
   , area.name region
from (
       select
          cv.c_id, cv.name, cv.description, (cv.t_show_years || 'Äê' || cv.t_show_months || 'ÔÂ') publishtime
          , cv.play_num, cv.focus_count, cv.comment_count, cv.book_count, cv.rating, cv.rating_count
          , cv.watching_count, cv.seen_count, cv.wanted_count, cv.t_show_years, cv.t_show_months, cv.alias
          , cv.duration, cv.actors, cv.director, cv.screenwriter, cv.language, cv.has_volume, cv.vol_total
          , cv.is_hd, cv.is_hot, cv.is_new, cv.t_conttype_id, cv.t_region_id
          , ct.type
       from zl_cont_video cv
            left join zl_cont ct on (cv.c_id = ct.id and sysdate between ct.active_time and ct.deactive_time)
     ) cvs
     left join zl_img img on cvs.c_id = img.target_id and img.use_type='0' and sysdate between img.active_time and img.deactive_time
     left join zl_video_conttype cvt on cvs.t_conttype_id = cvt.id
     left join zl_area area on area.id = cvs.t_region_id
order by cvs.c_id asc
*/

