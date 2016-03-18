create or replace procedure proc_import_sales_delete_deal(
p_providerId in integer
, p_prepareStatus in integer, p_doingStatus integer
, updateCount out integer)
is
   --v_sql varchar(1024);
   --v_url_field varchar(50);
begin
   update ZL_CONT_SALES_IMPORT im  set import_status=p_doingStatus,c_id=id
      where provider_id=p_providerId and import_status=p_prepareStatus and update_status = -1 and exists (select * from ZL_CONT_SALES sales where sales.c_id = im.c_id and sales.sales_no = im.sales_no  );
   updateCount := sql%rowcount;
   commit;
   --ͳһ����
   --�޳�������Ϣ�������ʲ�
   update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus and c_id in (
      select id from zl_cont where locked=1
   );
   updateCount := updateCount - sql%rowcount;
   commit;
   --ZL_CONT ��Ϊ���߱��
   update ZL_CONT zc set status=-1 where zc.id in (
      select C_ID from ZL_CONT_SALES_IMPORT im
         where provider_id=p_providerId and import_status=p_doingStatus and update_status <= 0);
   /*--���в��ڸ����б��ڵĶ����ߣ����ų�����
   update ZL_CONT zc set status=-1 where zc.id in (
      select cont.id from ZL_CONT cont left outer join ZL_CONT_SALES_IMPORT im
         on (cont.id=im.c_id and im.import_status=p_doingStatus and im.update_status >=1)
         where cont.provider_id=p_providerId and im.c_id is null)
      and locked!=1;*/
   updateCount := sql%rowcount;
   
   --׬���İ������޸���������:�ָ������ߵ�����
   if (p_providerId = 215633968) then 
      update zl_cont set status=10 where provider_id=p_providerId and status=-1 and modify_time>sysdate-180 and locked!=1;
   end if;
   
   commit;
   --�ָ���p_prepareStatus
   update ZL_CONT_SALES_IMPORT set import_status=p_prepareStatus where provider_id=p_providerId and import_status=p_doingStatus;
   commit;

end proc_import_sales_delete_deal;
/