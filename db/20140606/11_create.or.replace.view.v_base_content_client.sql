create or replace view v_base_content_client as
select
   ct.ID,ct.TYPE,ct.AD_TYPE,ct.STATUS,ct.CREATE_TIME,ct.MODIFY_TIME,ct.ACTIVE_TIME,ct.DEACTIVE_TIME--ct.NAME,ct.DESCRIPTION,
   , cv.C_ID,cv.T_REGION_ID,cv.T_CONTTYPE_ID,cv.T_SHOW_YEARS,cv.T_SHOW_MONTHS, decode(nvl(t_show_years,0),0,'未知',t_show_years||'年' || decode(nvl(t_show_months,0),0,'',t_show_months||'月') ) publishtime
   , cv.T_QUALITY_TYPE_ID,cv.NAME,cv.ALIAS,cv.DESCRIPTION,cv.DURATION,cv.ACTORS,cv.DIRECTOR,cv.SCREENWRITER,cv.LANGUAGE,cv.play_url,cv.tv_play_url
   , cv.PLAY_NUM,cv.FOCUS_COUNT,cv.COMMENT_COUNT,cv.BOOK_COUNT,cv.SEEN_COUNT,cv.WATCHING_COUNT,cv.WANTED_COUNT,cv.RATING,cv.RATING_COUNT,cv.LATEST_COMMENT,cv.HAS_VOLUME,cv.VOL_TOTAL,cv.VOL_UPDATE_TIME
   , nvl(cv.IS_HOT,0) IS_HOT,nvl(cv.IS_NEW,0) IS_NEW,nvl(cv.IS_HD,0) IS_HD
   , cv.provider_id, cv.provider_name,cv.isp_cont_code,cv.isp_menu_code
   , cv.package_name
   , cv.superscript_id
   , 1 epg_id
   --, (select id from v_base_epg_client epg where epg.content_id=ct.id and rownum=1) epg_id
   ,ct.pinyin
from zl_cont ct
     join zl_cont_video cv on (ct.status >= 10 and sysdate between ct.active_time and ct.deactive_time and ct.id = cv.c_id );
/*
select
   ct.ID,ct.TYPE,ct.STATUS,ct.CREATE_TIME,ct.MODIFY_TIME,ct.ACTIVE_TIME,ct.DEACTIVE_TIME--ct.NAME,ct.DESCRIPTION,
   , cv.C_ID,cv.T_REGION_ID,cv.T_CONTTYPE_ID,cv.T_SHOW_YEARS,cv.T_SHOW_MONTHS, decode(t_show_years,0,'未知',t_show_years||'年' || decode(t_show_months,0,'',t_show_months||'月') ) publishtime
   , cv.T_QUALITY_TYPE_ID,cv.NAME,cv.ALIAS,cv.DESCRIPTION,cv.DURATION,cv.ACTORS,cv.DIRECTOR,cv.SCREENWRITER,cv.LANGUAGE,cv.play_url,cv.tv_play_url
   , cv.PLAY_NUM,cv.FOCUS_COUNT,cv.COMMENT_COUNT,cv.BOOK_COUNT,cv.SEEN_COUNT,cv.WATCHING_COUNT,cv.WANTED_COUNT,cv.RATING,cv.RATING_COUNT,cv.LATEST_COMMENT,cv.HAS_VOLUME,cv.VOL_TOTAL,cv.VOL_UPDATE_TIME
   , cv.IS_HOT,cv.IS_NEW,cv.IS_HD
   , (
        select epg.id
        from zl_epg epg
        where epg.content_id = ct.id and sysdate between epg.active_time and epg.deactive_time
              and rownum =1
     ) epg_id
from zl_cont ct
     join zl_cont_video cv on (ct.status >= 10 and sysdate between ct.active_time and ct.deactive_time and ct.id = cv.c_id )
*/;

