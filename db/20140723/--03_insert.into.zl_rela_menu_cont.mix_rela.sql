insert into zl_cont(id,name,type,status,provider_id)
   select id,title name,17 type, 11 status,provider_id from zl_menu
     where id in (
        select zl_menu.id from zl_menu,zl_cont
           where zl_menu.id=zl_cont.id(+) and zl_menu.site_id=29 and zl_menu.parent_id>0 and zl_cont.id is null
     );
insert into zl_cont_video(c_id,name,provider_id,HAS_VOLUME)
   select id c_id,title name,provider_id,0 from zl_menu
     where id in (
        select zl_menu.id from zl_menu,zl_cont_video
           where zl_menu.id=zl_cont_video.c_id(+) and zl_menu.site_id=29 and zl_menu.parent_id>0 and zl_cont_video.c_id is null
     );
insert into zl_rela_menu_cont(id,menu_id,c_id,order_num)
   select id,parent_id menu_id,id c_id,order_num-100 from zl_menu
     where id in (
        select zl_menu.id from zl_menu,zl_rela_menu_cont
           where zl_menu.id=zl_rela_menu_cont.c_id(+) and zl_menu.site_id=29 and zl_menu.parent_id>0 and zl_rela_menu_cont.c_id is null
     );

--update zl_cont_video set HAS_VOLUME=0 where c_id in (select id from zl_cont where type=17);