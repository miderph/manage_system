create or replace procedure proc_import_sales_insert_deal(
p_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_sales_type integer;
   v_sql varchar(1024);
   --v_url_field varchar(50);
begin
   v_sales_type := 8;--视频购物

   --0,prepare for all,set import_status=p_doingStatus
   update ZL_CONT_SALES_IMPORT im set import_status=p_doingStatus,c_id= im.id
      where provider_id=p_providerId and import_status=p_prepareStatus and not exists (select 1 from ZL_CONT_SALES  sales where sales.c_id = im.c_id or sales.sales_no = im.sales_no  );
   updateCount := sql%rowcount;
   --剔除重复的数据
   update ZL_CONT_SALES_IMPORT im set import_status=p_prepareStatus
      where provider_id=p_providerId and exists (select 1 from zl_cont cont where cont.id=im.c_id);

   --1,ZL_IMG
     insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,URL_ICON,URL_4_SQUARES,URL_LITTLE,URL,IS_URL_USED)
       select 1 PLATGROUP_ID,c_id,provider_id,1,icon_url1,icon_url1,icon_url2,icon_url3
             ,case when icon_url2 is not null then 0 when icon_url3 is not null then 1 when icon_url1 is not null then 2 end IS_URL_USED
          from ZL_CONT_SALES_IMPORT
          where provider_id=p_providerId and import_status=p_doingStatus and (icon_url1 is not null or icon_url2 is not null or icon_url3 is not null);
   if( p_providerId = '216099916' ) then --卷皮
       insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url_icon,Url_4_Squares,IS_URL_USED)
        select 1 PLATGROUP_ID,c_id,import.provider_id,1 Use_Type,pic_url1 , pic_url1,2 --2 小方图
           from ZL_CONT_SALES_IMPORT import 
           where import.provider_id=p_providerId and import.import_status=p_doingStatus and import.pic_url1 is not null;
   end if;
   commit;
   
   --1.1,ZL_IMG 清除重复的旧的
   delete from zl_img t1 where provider_id=p_providerId and use_type=1 and exists(select 1 from zl_img t2 where t2.target_id=t1.target_id and t2.use_type=1 and t2.rowid>t1.rowid);
   commit;

   delete from zl_img img
   --update zl_img img set deactive_time=active_time--图片失效
      where img.provider_id=p_providerId and use_type=2 and target_id in (
         select c_id from ZL_CONT_SALES_IMPORT import where import.provider_id=p_providerId and import.import_status=p_doingStatus
      );
   --1.2,ZL_IMG 截图
   for ii in 1..6 loop
      v_sql := 'insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)'
               ||'   select 1 PLATGROUP_ID,c_id,provider_id,2,pic_url'||ii||' from ZL_CONT_SALES_IMPORT'
               ||'     where provider_id='||p_providerId||' and import_status='||p_doingStatus||' and pic_url'||ii||' is not null';
      execute immediate v_sql;

      <<continue_loop_22_1>>
      null;
   end loop;

   --2,ZL_CONT
   insert into ZL_CONT(ID,TYPE,STATUS,NAME,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,PROVIDER_ID,UPDATE_STATUS,pinyin)
      select C_ID,v_sales_type,p_contImportStatusValue,NAME,CREATE_TIME,MODIFY_TIME,sysdate-1,sysdate+3650
         ,as_provider(p_providerId, SHOP_TYPE)
         ,1 UPDATE_STATUS,pinyin from ZL_CONT_SALES_IMPORT
         where provider_id=p_providerId and import_status=p_doingStatus;
   --3,ZL_CONT_VIDEO
   insert into ZL_CONT_VIDEO(C_ID,NAME,ALIAS,PROVIDER_ID,UPDATE_STATUS)
      select C_ID,NAME,NAME
         ,as_provider(p_providerId, SHOP_TYPE)
         ,1 UPDATE_STATUS from ZL_CONT_SALES_IMPORT
         where provider_id=p_providerId and import_status=p_doingStatus;
   --4,ZL_CONT_SALES
   insert into ZL_CONT_SALES(C_ID,SALES_NO,PROVIDER_ID,CHANNEL_ID,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,Has_Video,URL,manage_user,audit_review,shop_type,detail_Pic_File,bitmask_price,Key_Words,Sum_Stock,Sum_sale)
      select c_id,SALES_NO
         ,as_provider(p_providerId, SHOP_TYPE),p_providerId
         ,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,has_video,URL,manage_user,audit_review,shop_type,detail_Pic_File,bitmask_price,category||' '||classify,sum_stock,sum_sale from ZL_CONT_SALES_IMPORT
         where provider_id=p_providerId and import_status=p_doingStatus;

   --10,end of all,恢复到p_prepareStatus
   --update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_sales_insert_deal;
/