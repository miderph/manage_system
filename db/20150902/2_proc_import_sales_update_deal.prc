create or replace procedure proc_import_sales_update_deal(
p_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_sales_type integer;
   v_sql varchar(1024);
   v_p_doingStatus_img integer;
   v_p_doingStatus_baseinfo integer;
begin
   v_sales_type := 8;--视频购物

   v_p_doingStatus_img := p_doingStatus+100;--需更新图片的状态
   v_p_doingStatus_baseinfo := p_doingStatus+200;--需更新基本信息的状态

   --0,prepare for all,状态调整为p_doingStatus
   update ZL_CONT_SALES_IMPORT im  set import_status=p_doingStatus
      where provider_id=p_providerId and import_status=p_prepareStatus and update_status=2
       and exists (select * from ZL_CONT_SALES  sales where sales.c_id = im.c_id or sales.sales_no = im.sales_no  );
   updateCount := sql%rowcount;

   --1,ZL_IMG:begin
   --1.1,状态调整为v_p_doingStatus_img
   update ZL_CONT_SALES_IMPORT im set import_status=v_p_doingStatus_img where provider_id=p_providerId and import_status=p_doingStatus;
   --1.2,剔除图片锁定的资产
   update ZL_CONT_SALES_IMPORT set import_status=p_doingStatus
      where provider_id=p_providerId and import_status=v_p_doingStatus_img and c_id in (
            select target_id from zl_img where locked=1
      );

   --1.3更新推荐图，处理同截图，在1.6处理
   /*update ZL_IMG zi set (PLATGROUP_ID,provider_id,Use_Type,URL_ICON,URL_4_SQUARES,URL_LITTLE,URL)=
         (select 1 PLATGROUP_ID,provider_id,1,icon_url1,icon_url1,icon_url2,icon_url3  from ZL_CONT_SALES_IMPORT im where zi.target_id = im.c_id )
     where Use_Type=1 and zi.target_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=v_p_doingStatus_img 
     and ( im2.icon_url1 is not null or  im2.icon_url2 is not null or im2.icon_url3 is not null ) );
   commit;*/
   --1.4补漏推荐图--v_imgUseType := 1;--:1为应用默认图，2为截图
   if( p_providerId = '216099916' ) then --卷皮
       update ZL_IMG img set (PLATGROUP_ID,target_id,provider_id,Use_Type,url_icon,Url_4_Squares,modify_time,IS_URL_USED) =
          (select 1 PLATGROUP_ID,c_id,import.provider_id,1 Use_Type,pic_url1 , pic_url1,sysdate,2 --2 小方图
           from ZL_CONT_SALES_IMPORT import where import.c_id = img.target_id)
       where img.use_type=1 and img.provider_id=p_providerId 
          and exists ( select * from ZL_CONT_SALES_IMPORT im where im.provider_id=p_providerId and im.import_status=v_p_doingStatus_img and im.pic_url1 is not null ); 

          
       insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url_icon,Url_4_Squares,IS_URL_USED)
        select 1 PLATGROUP_ID,c_id,import.provider_id,1 Use_Type,pic_url1 , pic_url1,2 --2 小方图
           from ZL_CONT_SALES_IMPORT import left outer join zl_img img on (import.c_id=img.target_id and img.use_type=1 and img.provider_id=p_providerId)
           where import.provider_id=p_providerId and import.import_status=v_p_doingStatus_img and import.pic_url1 is not null and img.id is null;
                      
                            
       commit;
   else
       insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url,IS_URL_USED)
        select 1 PLATGROUP_ID,c_id,import.provider_id,1 Use_Type,pic_url1
           ,case when icon_url2 is not null then 0 when icon_url3 is not null then 1 when icon_url1 is not null then 2 end IS_URL_USED
           from ZL_CONT_SALES_IMPORT import left outer join zl_img img on (import.c_id=img.target_id and img.use_type=1 and img.provider_id=p_providerId)
           where import.provider_id=p_providerId and import.import_status=v_p_doingStatus_img and import.pic_url1 is not null and img.id is null;
       commit;
   end if;
   --1.5,ZL_IMG 删除重复的旧的
   /*
   delete from zl_img t1
       where provider_id=p_providerId and use_type=1
          and exists(select 1 from zl_img t2 where t2.target_id=t1.target_id and t2.use_type=1 and t2.rowid>t1.rowid);
   */
   commit;

   --1.6,截图：失效原有的，insert新的（新的截图数不为0）
   --1.6.1,v_imgUseType := 2;--:1为应用默认图，2为截图
   delete from zl_img img
   --update zl_img img set deactive_time=active_time--图片失效
      where img.provider_id=p_providerId and use_type=2 and target_id in (
         select c_id from ZL_CONT_SALES_IMPORT import where import.provider_id=p_providerId and import.import_status=v_p_doingStatus_img
      );
   --1.6.2,ZL_IMG 缩略图的顺序可能乱了
   for ii in 1..6 loop
      --insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
      --   select 1 PLATGROUP_ID,c_id,as_providerId,1,pic_url1 from ZL_CONT_SALES_IMPORT
      --      where provider_id=p_providerId and import_status=v_tempStatus and pic_url1 is not null;
      v_sql := 'insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)'
               ||'   select 1 PLATGROUP_ID,c_id,provider_id,2,pic_url'||ii||' from ZL_CONT_SALES_IMPORT'
               ||'     where provider_id='||p_providerId||' and import_status='||v_p_doingStatus_img||' and pic_url'||ii||' is not null';
      execute immediate v_sql;

      <<continue_loop_22_1>>
      null;
   end loop;

   --1.7,状态恢复到p_doingStatus
   update ZL_CONT_SALES_IMPORT im set import_status=p_doingStatus where provider_id=p_providerId and import_status=v_p_doingStatus_img;
   --ZL_IMG:end

   --2,状态调整为v_p_doingStatus_baseinfo
   update ZL_CONT_SALES_IMPORT im set import_status=v_p_doingStatus_baseinfo where provider_id=p_providerId and import_status=p_doingStatus;

   --2.1,剔除基本信息锁定的资产
   update ZL_CONT_SALES_IMPORT set import_status=p_doingStatus where provider_id=p_providerId and import_status=v_p_doingStatus_baseinfo and c_id in (
      select id from zl_cont where locked=1
   );

   --2.2,ZL_CONT
   update ZL_CONT zc set ( TYPE,STATUS,NAME,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,PROVIDER_ID,UPDATE_STATUS,pinyin)=
      (select v_sales_type,p_contImportStatusValue,NAME,CREATE_TIME,MODIFY_TIME,sysdate-1,sysdate+3650
         ,as_provider(p_providerId, SHOP_TYPE)
         ,1 UPDATE_STATUS,pinyin from ZL_CONT_SALES_IMPORT im where zc.id = im.c_id )
     where zc.id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=v_p_doingStatus_baseinfo );
   --2.3,ZL_CONT_VIDEO
   update ZL_CONT_VIDEO  zcv set (NAME,ALIAS,PROVIDER_ID,UPDATE_STATUS)=
      (select NAME,NAME
         ,as_provider(p_providerId, SHOP_TYPE)
         ,1 UPDATE_STATUS from ZL_CONT_SALES_IMPORT im where zcv.c_id = im.c_id )
    where zcv.c_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=v_p_doingStatus_baseinfo );
   --2.4,ZL_CONT_SALES
   update ZL_CONT_SALES zcs set (SALES_NO,PROVIDER_ID,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,Has_Video,URL,manage_user,audit_review,shop_type,detail_pic_file,bitmask_price,key_words,Sum_stock,sum_sale)=
      (select SALES_NO
         ,as_provider(p_providerId, SHOP_TYPE)
         ,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,has_video,URL,manage_user,audit_review,shop_type,detail_pic_file,bitmask_price, category||' '||classify ,sum_stock,sum_sale from ZL_CONT_SALES_IMPORT im where zcs.c_id = im.c_id )
     where zcs.c_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=v_p_doingStatus_baseinfo );
   --2.5,状态恢复到p_doingStatus
   update ZL_CONT_SALES_IMPORT im set import_status=p_doingStatus where provider_id=p_providerId and import_status=v_p_doingStatus_baseinfo;

   --2.6,状态恢复到p_prepareStatus
   update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_sales_update_deal;
/