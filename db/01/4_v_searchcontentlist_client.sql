create or replace view v_searchcontentlist_client as
select
     ct.c_id, ct.name,ct.pinyin, ct.description, ct.publishtime, ct.play_url, ct.tv_play_url
   , ct.play_num, ct.focus_count, ct.comment_count, ct.book_count, ct.rating
   , ct.rating_count, ct.watching_count, ct.seen_count, ct.wanted_count
   , ct.is_hd, ct.is_hot, ct.is_new, ct.type, ct.has_volume
   , ct.provider_id, ct.provider_name,ct.isp_cont_code,ct.isp_menu_code,package_name
from v_base_content_client ct;
