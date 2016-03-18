create or replace procedure proc_import_sales_update_deal(
p_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_sales_type integer;
   v_sql varchar(1024);

   v_loop_count integer;
   v_block_size integer;
   v_tempStatus integer;
begin
   v_block_size := 5000;--1000;
   v_sales_type := 8;--视频购物
   v_tempStatus := 9999;

   --0,prepare for all,状态调整为p_doingStatus
   update ZL_CONT_SALES_IMPORT im  set c_id=(select c_id from ZL_CONT_SALES sales where rownum<=1 and sales.c_id=im.id or (sales.sales_no=im.sales_no and provider_id=as_provider(p_providerId, SHOP_TYPE)))
      where provider_id=p_providerId and import_status=p_prepareStatus and c_id is null
       and exists (select 1 from ZL_CONT_SALES sales where sales.c_id=im.id or (sales.sales_no=im.sales_no and provider_id=as_provider(p_providerId, SHOP_TYPE)) );

   update ZL_CONT_SALES_IMPORT im  set import_status=p_doingStatus
      where provider_id=p_providerId and import_status=p_prepareStatus
       and exists (select 1 from ZL_CONT_SALES sales where sales.c_id=im.id or (sales.sales_no=im.sales_no and provider_id=as_provider(p_providerId, SHOP_TYPE)) );
   updateCount := sql%rowcount;
   commit;

   --1,ZL_IMG:begin

   --1.3更新推荐图
   --update zl_img set UPDATE_STATUS=2 where provider_id=p_providerId and USE_TYPE=1 and locked=1 and UPDATE_STATUS=v_tempStatus;
   update zl_img set UPDATE_STATUS=2 where provider_id=p_providerId and UPDATE_STATUS=v_tempStatus;
   update zl_img set UPDATE_STATUS=v_tempStatus where USE_TYPE=1 and locked=0 and target_id in (
      select c_id from ZL_CONT_SALES_IMPORT im1 where im1.provider_id=p_providerId and im1.import_status=p_doingStatus
         and (im1.icon_url1 is not null or im1.icon_url2 is not null or im1.icon_url3 is not null)
   );
   commit;

   --zl_img:update all fields
   v_loop_count := 0;
   loop
      update zl_img set (
            TARGET_ID,MODIFY_TIME,PROVIDER_ID,UPDATE_STATUS
            ,url_icon,URL_4_SQUARES--,url_little,url--只更新小方图和四格图
            ,IS_URL_USED
         )=(select
            distinct im2.c_id, im2.MODIFY_TIME,provider_id,2 UPDATE_STATUS
            ,im2.icon_url1 url_icon,im2.icon_url1 url_icon_4--,icon_url2 url_little,icon_url3 url--只更新小方图和四格图
            ,case
                when icon_url2 is not null then 0
                when icon_url3 is not null then 1
                when icon_url1 is not null then 2
             end IS_URL_USED
            from ZL_CONT_SALES_IMPORT im2 where im2.id = (
            select max(id) from zl_cont_sales_import im3 where im3.c_id=zl_img.target_id group by im3.c_id
            )
         )
         where update_status=v_tempStatus and USE_TYPE=1 and rownum<=v_block_size and target_id in (
                select c_id from ZL_CONT_SALES_IMPORT im1 where im1.provider_id=p_providerId and im1.import_status=p_doingStatus
                    and (im1.icon_url1 is not null or im1.icon_url2 is not null or im1.icon_url3 is not null)
         );
      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#update,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_img,v_loop_count='||v_loop_count||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop;

   --临时处理图片
   insert into ZL_IMG(id,PLATGROUP_ID,target_id,provider_id,Use_Type,URL_ICON,URL_4_SQUARES,URL_LITTLE,URL,IS_URL_USED)
       select id,1 PLATGROUP_ID,c_id,provider_id,1 use_type,icon_url1 url_icon,icon_url1 url_icon_4,icon_url2 url_little,icon_url3 url
             ,case
                when icon_url2 is not null then 0
                when icon_url3 is not null then 1
                when icon_url1 is not null then 2
              end IS_URL_USED
         from ZL_CONT_SALES_IMPORT im1
            where im1.provider_id=p_providerId and import_status=p_doingStatus and (im1.icon_url1 is not null or im1.icon_url2 is not null or im1.icon_url3 is not null)
                and not exists (select 1 from zl_img img where img.target_id=im1.c_id and img.use_type=1);

   dbms_output.put_line('#insert,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_img,count='||sql%rowcount);

   --ZL_IMG 清除可能重复的旧图
   delete from zl_img t1 where provider_id=p_providerId and use_type=1 and exists(select 1 from zl_img t2 where t2.target_id=t1.target_id and t2.use_type=1 and t2.rowid>t1.rowid);
   commit;

   --1.4,截图：失效原有的，insert新的（新的截图数不为0）,暂未按v_block_size分段
   --1.4.1,v_imgUseType := 2;--:1为应用默认图，2为截图
   delete from zl_img img
      where img.provider_id=p_providerId and use_type=2 and locked=0 and target_id in (
         select c_id from ZL_CONT_SALES_IMPORT import where import.provider_id=p_providerId and import.import_status=p_doingStatus
      );
   commit;
   --1.4.2,ZL_IMG 缩略图的顺序可能乱了
   for ii in 1..6 loop
      --insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)
      --   select 1 PLATGROUP_ID,c_id,as_providerId,1,pic_url1 from ZL_CONT_SALES_IMPORT
      --      where provider_id=p_providerId and import_status=v_tempStatus and pic_url1 is not null;
      v_sql := 'insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)'
               ||'   select 1 PLATGROUP_ID,c_id,provider_id,2,isp_pic_url'||ii||' from ZL_CONT_SALES_IMPORT im1'
               ||'     where provider_id='||p_providerId||' and import_status='||p_doingStatus||' and pic_url'||ii||' is not null'
               ||'       and not exists(select 1 from zl_img img where img.use_type=2 and locked=1 and img.target_id=im1.c_id )';
      execute immediate v_sql;

      <<continue_loop_22_1>>
      null;
   end loop;
   commit;
   --ZL_IMG:end

   --2.2,ZL_CONT
   update zl_cont set UPDATE_STATUS=2 where provider_id=p_providerId and locked=1 and UPDATE_STATUS=v_tempStatus;
   update zl_cont set UPDATE_STATUS=v_tempStatus where provider_id=p_providerId and locked=0 and id in (
      select id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   );
   --赚宝数据的provider_id会替换为天猫和淘宝，需要直接用id判断
   update zl_cont set UPDATE_STATUS=2 where id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   ) and locked=1 and UPDATE_STATUS=v_tempStatus;
   update zl_cont set UPDATE_STATUS=v_tempStatus where locked=0 and id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   );
   commit;

   --zl_cont:update all fields
   v_loop_count := 0;
   loop
      update zl_cont set (
            TYPE,NAME,status,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,PROVIDER_ID,UPDATE_STATUS,pinyin
         )=(select
            v_sales_type,NAME,case when zl_cont.status=11 then 11 else 10 end status, CREATE_TIME,MODIFY_TIME,nvl(im2.start_time,sysdate-1),nvl(im2.end_time-10/24,sysdate+3650)
            ,as_provider(p_providerId, SHOP_TYPE),2 UPDATE_STATUS,pinyin
            from ZL_CONT_SALES_IMPORT im2 where im2.c_id=zl_cont.id
         )
         where id in (
            select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
         ) and rownum<=v_block_size and (update_status=v_tempStatus);
      commit;
      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#update,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_cont,v_loop_count='||v_loop_count||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;

   end loop;

   --2.3,ZL_CONT_VIDEO
   update ZL_CONT_VIDEO set UPDATE_STATUS=2 where provider_id=p_providerId and UPDATE_STATUS=v_tempStatus and c_id in (
       select id from zl_cont where locked=1
   );
   update ZL_CONT_VIDEO set UPDATE_STATUS=v_tempStatus where provider_id=p_providerId and c_id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   ) and c_id in (
      select id from zl_cont where locked=0
   );
   --赚宝数据的provider_id会替换为天猫和淘宝，需要直接用id判断
   update ZL_CONT_VIDEO set UPDATE_STATUS=2 where c_id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   ) and UPDATE_STATUS=v_tempStatus and c_id in (
       select id from zl_cont where locked=1
   );
   update ZL_CONT_VIDEO set UPDATE_STATUS=v_tempStatus where c_id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   ) and c_id in (
      select id from zl_cont where locked=0
   );
   commit;

   --zl_cont_video:update all fields
   v_loop_count := 0;
   loop
      update zl_cont_video set (
            NAME,ALIAS,PROVIDER_ID,UPDATE_STATUS
         )=(select
            NAME,NAME,as_provider(p_providerId, SHOP_TYPE),2 UPDATE_STATUS
            from ZL_CONT_SALES_IMPORT im2 where im2.c_id=zl_cont_video.c_id
         )
         where c_id in (
            select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
         ) and rownum<=v_block_size and update_status=v_tempStatus;
         --and c_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=p_doingStatus )
      commit;
      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#update,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_cont_video,v_loop_count='||v_loop_count||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;

   end loop;

   --2.4,ZL_CONT_SALES
   update ZL_CONT_SALES set UPDATE_STATUS=2 where provider_id=p_providerId and UPDATE_STATUS=v_tempStatus and c_id in (
       select id from zl_cont where locked=1
   );
   update ZL_CONT_SALES set UPDATE_STATUS=v_tempStatus where provider_id=p_providerId and c_id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   ) and c_id in (
      select id from zl_cont where locked=0
   );
   --赚宝数据的provider_id会替换为天猫和淘宝，需要直接用id判断
   update ZL_CONT_SALES set UPDATE_STATUS=2 where c_id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   ) and UPDATE_STATUS=v_tempStatus and c_id in (
       select id from zl_cont where locked=1
   );
   update ZL_CONT_SALES set UPDATE_STATUS=v_tempStatus where c_id in (
      select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
   ) and c_id in (
      select id from zl_cont where locked=0
   );
   commit;

   --zl_cont_sales:update all fields
   v_loop_count := 0;
   loop
      update ZL_CONT_SALES sales set (
            SALES_NO,PROVIDER_ID,UPDATE_STATUS
            ,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,Has_Video,URL,manage_user,audit_review,shop_type,detail_pic_file,bitmask_price,key_words,sum_stock,sum_sale
         )=(select
            SALES_NO,as_provider(p_providerId, SHOP_TYPE) PROVIDER_ID,2 UPDATE_STATUS
            ,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,has_video,URL,manage_user,audit_review,shop_type,detail_pic_file,bitmask_price, category||' '||classify ,
            as_sum_stock(p_providerId , sales.sum_stock, im2.sum_stock),sum_sale
            from ZL_CONT_SALES_IMPORT im2 where im2.c_id=sales.c_id
         )
         where c_id in (
            select c_id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
         ) and rownum<=v_block_size and update_status=v_tempStatus;
         --and c_id in (select c_id from zl_cont_sales_import im2 where im2.provider_id=p_providerId and im2.import_status=p_doingStatus )
      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#update,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_cont_sales,v_loop_count='||v_loop_count||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop;

   --10,end of all,恢复到p_prepareStatus
   --update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;
end proc_import_sales_update_deal;
/
