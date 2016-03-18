create or replace view v_getcontentdetail_client as
select
    ct.c_id, ct.name, ct.description, ct.publishtime, ct.play_url, ct.tv_play_url
   , ct.play_num, ct.focus_count, ct.comment_count, ct.book_count, ct.rating
   , ct.rating_count, ct.watching_count, ct.seen_count, ct.wanted_count
   , ct.t_show_years, ct.t_show_months, ct.alias
   , ct.duration, ct.actors, ct.director, ct.screenwriter, ct.language, ct.has_volume, ct.vol_total
   , ct.is_hd, ct.is_hot, ct.is_new, ct.t_conttype_id, ct.t_region_id
   , ct.type, ct.ad_type
   , ct.isp_cont_code,ct.isp_menu_code,ct.isp_cp_code
   , ct.provider_id
   , cvt.description cont_type
   ,null region--, area.name region
   ,ct.package_name,ct.is_autoplay,null rela_time
from v_base_content_client ct
     left join zl_video_conttype cvt on ct.t_conttype_id = cvt.id
;