create or replace procedure proc_import_sales_insert_deal(
p_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, p_contImportStatusValue in integer, updateCount out integer)
is
   v_sales_type integer;
   v_sql varchar(1024);
   --v_url_field varchar(50);

   v_startId integer;
   v_loop_count integer;
   v_block_size integer;
begin
   v_block_size := 5000;--5000;
   v_sales_type := 8;--视频购物

   --0,prepare for all,set import_status=p_doingStatus
   update ZL_CONT_SALES_IMPORT im set import_status=p_doingStatus,im.c_id=im.id
      where provider_id=p_providerId and import_status=p_prepareStatus
         and not exists (select 1 from ZL_CONT_SALES sales where sales.c_id=im.id or (sales.sales_no=im.sales_no and provider_id=as_provider(p_providerId, SHOP_TYPE)));
   updateCount := sql%rowcount;
   commit;
   --1,ZL_IMG
   --1.1,ZL_IMG 推荐图
   loop----确保不存在重复的数据
      delete from zl_img t1 where t1.target_id in (
         select id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
      ) and rownum<=v_block_size;
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;
   v_loop_count := 0;
   loop
      select nvl(min(im1.id),0) into v_startid from ZL_CONT_SALES_IMPORT im1,zl_img m1
          where im1.provider_id=p_providerId and im1.import_status=p_doingStatus and (im1.icon_url1 is not null or im1.icon_url2 is not null or im1.icon_url3 is not null)
             and im1.id=m1.id(+) and m1.id is null;
      if (v_startid<=0) then--if (sql%rowcount<=0) then
         exit;
      end if;

      insert into ZL_IMG(id,PLATGROUP_ID,target_id,provider_id,Use_Type,URL_ICON,URL_4_SQUARES--,URL_LITTLE,URL
           ,IS_URL_USED)
          select id,1 PLATGROUP_ID,id,provider_id,1 use_type,icon_url1 url_icon,icon_url1 url_icon_4--,icon_url2 url_little,icon_url3 url
             ,case
                when icon_url2 is not null then 0
                when icon_url3 is not null then 1
                when icon_url1 is not null then 2
              end IS_URL_USED
          from ZL_CONT_SALES_IMPORT im3
          where im3.provider_id=p_providerId and im3.import_status=p_doingStatus and (im3.icon_url1 is not null or im3.icon_url2 is not null or im3.icon_url3 is not null)
              and rownum<=v_block_size and im3.id >=v_startid and id in (
                  select im1.id from ZL_CONT_SALES_IMPORT im1,zl_img m1 where im1.provider_id=p_providerId and im1.id=m1.id(+) and m1.id is null
              );

      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#insert,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_img,v_loop_count='||v_loop_count||',v_startid='||v_startid||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;

   --ZL_IMG 清除可能重复的旧图
   delete from zl_img t1 where provider_id=p_providerId and use_type=1 and exists(select 1 from zl_img t2 where t2.target_id=t1.target_id and t2.use_type=1 and t2.rowid>t1.rowid);
   commit;

   --1.2,ZL_IMG 截图,暂未按v_block_size分段
   delete from zl_img img
      where img.provider_id=p_providerId and use_type=2 and target_id in (
         select c_id from ZL_CONT_SALES_IMPORT import where import.provider_id=p_providerId and import.import_status=p_doingStatus
      );
   commit;
   for ii in 1..6 loop
      v_sql := 'insert into ZL_IMG(PLATGROUP_ID,target_id,provider_id,Use_Type,url)'
               ||'   select 1 PLATGROUP_ID,c_id,provider_id,2,isp_pic_url'||ii||' from ZL_CONT_SALES_IMPORT'--直接用第三方的图片地址isp_pic_url||ii
               ||'     where provider_id='||p_providerId||' and import_status='||p_doingStatus||' and pic_url'||ii||' is not null';
      execute immediate v_sql;
      commit;

      <<continue_loop_22_1>>
      null;
   end loop;

   --2,ZL_CONT
   loop----确保不存在重复的数据
      delete from zl_cont t1 where id in (
         select id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
      ) and rownum<=v_block_size;
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;
   v_loop_count := 0;
   loop
      select nvl(min(im1.id),0) into v_startid from ZL_CONT_SALES_IMPORT im1,zl_cont m1
         where im1.provider_id=p_providerId and im1.import_status=p_doingStatus
            and im1.id=m1.id(+) and m1.id is null;
      if (v_startid<=0) then--if (sql%rowcount<=0) then
         exit;
      end if;

      insert into ZL_CONT(ID,TYPE,STATUS,NAME,CREATE_TIME,MODIFY_TIME,ACTIVE_TIME,DEACTIVE_TIME,PROVIDER_ID,UPDATE_STATUS,pinyin)
         select C_ID,v_sales_type,p_contImportStatusValue,NAME,CREATE_TIME,MODIFY_TIME,nvl(im3.start_time,sysdate-1),nvl(im3.end_time-10/24,sysdate+3650)
            ,as_provider(p_providerId, SHOP_TYPE)
            ,1 UPDATE_STATUS,pinyin
         from ZL_CONT_SALES_IMPORT im3
         where im3.provider_id=p_providerId and im3.import_status=p_doingStatus and rownum<=v_block_size and im3.id >=v_startid
          and id in (
             select im1.id from ZL_CONT_SALES_IMPORT im1,zl_cont m1 where im1.provider_id=p_providerId and im1.id=m1.id(+) and m1.id is null
          );

      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#insert,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_cont,v_loop_count='||v_loop_count||',v_startid='||v_startid||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;

   --3,ZL_CONT_VIDEO
   loop----确保不存在重复的数据
      delete from zl_cont_video t1 where c_id in (
         select id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
      ) and rownum<=v_block_size;
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;
   v_loop_count := 0;
   loop
      select nvl(min(im1.id),0) into v_startid from ZL_CONT_SALES_IMPORT im1,ZL_CONT_VIDEO m1
         where im1.provider_id=p_providerId and im1.import_status=p_doingStatus
            and im1.id=m1.c_id(+) and m1.c_id is null;
      if (v_startid<=0) then--if (sql%rowcount<=0) then
         exit;
      end if;

      insert into ZL_CONT_VIDEO(C_ID,NAME,ALIAS,PROVIDER_ID,UPDATE_STATUS)
         select C_ID,NAME,NAME
            ,as_provider(p_providerId, SHOP_TYPE)
            ,1 UPDATE_STATUS
         from ZL_CONT_SALES_IMPORT im3
         where provider_id=p_providerId and import_status=p_doingStatus and rownum<=v_block_size and im3.id >=v_startid
            and id in (
               select im1.id from ZL_CONT_SALES_IMPORT im1,ZL_CONT_VIDEO m1 where im1.provider_id=p_providerId and im1.id=m1.c_id(+) and m1.c_id is null
            );

      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#insert,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_cont_video,v_loop_count='||v_loop_count||',v_startid='||v_startid||',count='||sql%rowcount);
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;

   --4,ZL_CONT_SALES
   loop----确保不存在重复的数据
      delete from zl_cont_sales t1 where c_id in (
         select id from ZL_CONT_SALES_IMPORT im where provider_id=p_providerId and import_status=p_doingStatus
      ) and rownum<=v_block_size;
      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;
   v_loop_count := 0;
   loop
      select nvl(min(im1.id),0) into v_startid from ZL_CONT_SALES_IMPORT im1,ZL_CONT_SALES m1
         where im1.provider_id=p_providerId and im1.import_status=p_doingStatus
            and im1.id=m1.c_id(+) and m1.c_id is null;
      if (v_startid<=0) then--if (sql%rowcount<=0) then
         exit;
      end if;

      insert into ZL_CONT_SALES(C_ID,SALES_NO,PROVIDER_ID,CHANNEL_ID,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,Has_Video,URL,manage_user,audit_review,shop_type,detail_Pic_File,bitmask_price,Key_Words,Sum_Stock,Sum_sale)
         select c_id,SALES_NO
            ,as_provider(p_providerId, SHOP_TYPE),p_providerId CHANNEL_ID
            ,NAME,HOT_INFO,FAKE_PRICE,SALE_PRICE,real_price,DISACCOUNT,POST_PRICE,CP_NAME,SUB_CP_NAME,has_video,URL,manage_user,audit_review,shop_type,detail_Pic_File,bitmask_price,category||' '||classify,sum_stock,sum_sale
         from ZL_CONT_SALES_IMPORT im3
         where provider_id=p_providerId and import_status=p_doingStatus and rownum<=v_block_size and im3.id >=v_startid
            and id in (
               select im1.id from ZL_CONT_SALES_IMPORT im1,ZL_CONT_SALES m1 where im1.provider_id=p_providerId and im1.id=m1.c_id(+) and m1.c_id is null
            );

      v_loop_count := v_loop_count + 1;
      dbms_output.put_line('#insert,end at:'||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||',zl_cont_sales,v_loop_count='||v_loop_count||',v_startid='||v_startid||',count='||sql%rowcount);

      if (sql%rowcount<=0) then
         exit;
      end if;
      commit;
   end loop ;

   --10,end of all,恢复到p_prepareStatus
   --update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;
end proc_import_sales_insert_deal;
/
