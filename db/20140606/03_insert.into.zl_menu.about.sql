insert into zl_menu(parent_id,title,site_id,order_num,status,struct_type,resource_type,act_type,provider_id)
  select 0 parent_id,'����' title,site_id,0 order_num,-1 status,2 struct_type,'CONTENT' resource_type,'ShowEntries' act_type,provider_id
    from zl_menu import where parent_id=0 and title='���' and not exists(select id from zl_menu menu where menu.site_id=import.site_id and menu.title='����')
    order by site_id;