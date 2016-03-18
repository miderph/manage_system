create or replace view v_getmenu_client as
select
		    menu.*, parent_id new_parent_id
		    , img.*
        , hide.plat hide_plat,hide.version_num hide_version_num,hide.status hide_status
		 from v_base_menu_client menu
		      left outer join v_base_img_for_menu_client img on (menu.id = img.target_id)
		      left outer join zl_menu_hide hide on (menu.id = hide.m_id)
		 order by menu.parent_id, order_num asc
;