create or replace procedure proc_import_sales_deal(p_providerId in integer, updateCount out varchar2) is
   v_prepareStatus integer;
   v_okStatus integer;
   v_doingStatus integer;
   v_doingStatus_insert integer;
   v_doingStatus_update integer;
   v_doingStatus_delete integer;
   v_contImportStatusValue integer;
   v_tempId integer;

   not_found_data EXCEPTION;
   strTemp varchar2(1000);
   v_startTime date;
   v_endTime date;
   v_tag varchar2(1000);
begin
   --����ԭ��
   --1,ͨ�����import_status������insert��update,insert��update�ֿ�����
   --4,�ܹ�����һ�����v_doingStatus=9999�����ӹ������Լ��ı�ǣ��ӹ��̴�����ɺ���Ҫ�ѱ�ǻظ�������ǰ��ֵ
   --5,���ӹ��̶���������ܹ��̻�ѱ������Ϊv_okStatus=1

   v_prepareStatus := 0;
   v_okStatus := 1;
   v_doingStatus := 9999;
   v_doingStatus_insert := v_doingStatus-2999;
   v_doingStatus_update := v_doingStatus-3999;
   v_doingStatus_delete := v_doingStatus-4999;--δ�ж�
   v_contImportStatusValue := 10; --�Զ������״ֵ̬
   v_tag := p_providerId||'_proc_import_sales_deal';

   proc_log(v_tag,'starting...');
   --�Ƿ���û��������ݣ�û�����쳣
   v_endTime := sysdate;strTemp := '#proc_import_sales_deal,provider_id='||p_providerId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_log(v_tag,strTemp);
   update ZL_CONT_SALES_IMPORT set update_status=1 where provider_id=p_providerId and update_status is null;
   update ZL_CONT_SALES_IMPORT set import_status=v_prepareStatus where provider_id=p_providerId and import_status is null;
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status!=v_okStatus and update_status<=0;
   --update ZL_CONT_SALES_IMPORT set import_status=v_prepareStatus where provider_id=p_providerId and import_status!=v_okStatus and update_status>=1;
   select count(1) into v_tempId from ZL_CONT_SALES_IMPORT where update_status >= 1 and import_status=v_prepareStatus and provider_id= p_providerId;
   if v_tempId = 0 then
      dbms_output.put_line('#not_found_data');
         proc_log(v_tag,'#not_found_data');
      raise not_found_data;
      --return;
   end if;

   --���Ҫ���������
   update ZL_CONT_SALES_IMPORT set import_status=v_doingStatus where provider_id=p_providerId and import_status=v_prepareStatus;
   --�޳���Ʒ���Ϊ�յ�����
   update ZL_CONT_SALES_IMPORT set import_status=-1 where provider_id=p_providerId and import_status=v_doingStatus and sales_no is null;
   commit;

   --begin: ����insert��ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_insert_deal,begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);

   --call proc_import_sales_insert_deal
   proc_log(v_tag,strTemp);
   proc_import_sales_insert_deal(p_providerId, v_doingStatus,v_doingStatus_insert,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_sales=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_insert_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime);updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_log(v_tag,strTemp);
   if v_tempId = 0 then--˫�ر���,û����������
      null;--raise not_found_data;
      dbms_output.put_line('#insert empty');
      proc_log(v_tag,'#insert empty');
   end if;
   --insert ok
   --update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus_insert;
   commit;
   --end: ����insert

   --begin: ����update�� ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_update_deal,begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_log(v_tag,strTemp);
   --call proc_import_sales_update_deal
   proc_import_sales_update_deal(p_providerId, v_doingStatus,v_doingStatus_update,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_sales=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_update_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime);updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_log(v_tag,strTemp);
   if v_tempId = 0 then
      null;
      dbms_output.put_line('#update empty');
   end if;

   --update ok
   --update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus_update;
   commit;
   --end: ����update�� ZL_CONT_SALES_IMPORT

   --begin: ����delete: ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_delete_deal,begin at:'||to_char(v_starttime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   --call proc_import_sales_delete_deal
   proc_log(v_tag,strTemp);
   proc_import_sales_delete_deal(p_providerId, v_doingStatus,v_doingStatus_delete,v_tempId);
   updateCount :=updateCount || ',zl_cont_sales='||v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_delete_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime);updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   proc_log(v_tag,strTemp);
   if v_tempId = 0 then
      null;
      dbms_output.put_line('#delete empty');
   end if;

   -- delete ok
   --update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus_delete;
   commit;
   --end: ����delete: ZL_CONT_SALES_IMPORT

   --������ɺ���Ϊ���ߣ��ٴ�ץȡ��������״̬��δץȡ������Ȼ�������ˣ�ͬʱҲ��ʶ��������
   select nvl(min(update_status),0)-1 into v_tempId from ZL_CONT_SALES_IMPORT where provider_id=p_providerId;
   v_tempId := case when v_tempId >=0 then -1 else v_tempId end;

   --all ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus,update_status=v_tempId
      where provider_id=p_providerId
         and import_status in (v_doingStatus,v_doingStatus_insert,v_doingStatus_update,v_doingStatus_delete);
   commit;
   proc_log(v_tag,'finished');
end proc_import_sales_deal;
/
