create or replace procedure proc_import_sales_deal(p_providerId in integer, updateCount out varchar2) is
   v_prepareStatus integer;
   v_okStatus integer;
   v_doingStatus integer;
   v_contImportStatusValue integer;
   v_tempId integer;
   as_providerId integer;

   not_found_data EXCEPTION;
   strTemp varchar2(1000);
   v_startTime date;
   v_endTime date;
begin
   --����ԭ��
   --1,ͨ�����import_status������insert��update,insert��update�ֿ�����
   --4,�ܹ�����һ�����v_doingStatus=9999�����ӹ������Լ��ı�ǣ��ӹ��̴�����ɺ���Ҫ�ѱ�ǻظ�������ǰ��ֵ
   --5,���ӹ��̶���������ܹ��̻�ѱ������Ϊv_okStatus=1

   as_providerId := p_providerId; --������Դ provider
   v_prepareStatus := 0;
   v_okStatus := 1;
   v_doingStatus := 9999;
   v_contImportStatusValue := 10; --�Զ������״ֵ̬

   --�Ƿ���û��������ݣ�û�����쳣
   v_endTime := sysdate;strTemp := '#proc_import_sales_deal,provider_id='||p_providerId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   select count(1) into v_tempId from ZL_CONT_SALES_IMPORT where (update_status >= 1 or update_status is null);
   if v_tempId = 0 then
      raise not_found_data;
   end if;

   --delete from ZL_CONT_APPSTORE
   --where c_id in (
   --   select app.c_id from ZL_CONT_APPSTORE app,zl_cont_video cont
   --   where app.c_id=cont.c_id(+) and cont.c_id is null
   --);

   --���Ҫ���������
   update ZL_CONT_SALES_IMPORT set import_status=v_prepareStatus where provider_id=p_providerId and (import_status is null or import_status =0);
   update ZL_CONT_SALES_IMPORT set import_status=v_doingStatus where provider_id=p_providerId and import_status=v_prepareStatus;
   commit;
   update ZL_CONT_SALES_IMPORT set import_status=-1 where provider_id=p_providerId and import_status=v_doingStatus and sales_no is null;

   --����insert��ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_insert_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   
   proc_import_sales_insert_deal(p_providerId, as_providerId, v_doingStatus,v_doingStatus-2999,v_contImportStatusValue,v_tempId);
   
   updateCount := updateCount || ',zl_cont_sales=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_insert_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime)||',count='||v_tempId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   if v_tempId = 0 then--˫�ر���,û����������
      null;--raise not_found_data;
   end if;
   --insert ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-2999;
   commit;

   --����update�� ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_update_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   --�ݲ��������proc_import_app_update_deal(p_providerId, as_providerId, v_doingStatus,v_doingStatus-3999,v_contImportStatusValue,v_tempId);
   v_tempId := 0;
   updateCount := updateCount || ',zl_cont_sales=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_update_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime)||',count='||v_tempId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   --update ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-3999;
   commit;

   --all ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus;
   commit;
end proc_import_sales_deal;
/
