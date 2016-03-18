create or replace procedure proc_import_sales_deal(p_providerId in integer, updateCount out varchar2) is
   v_prepareStatus integer;
   v_okStatus integer;
   v_doingStatus integer;
   v_contImportStatusValue integer;
   v_tempId integer;

   not_found_data EXCEPTION;
   strTemp varchar2(1000);
   v_startTime date;
   v_endTime date;
   v_paytypes varchar2(1000);
begin
   --����ԭ��
   --1,ͨ�����import_status������insert��update,insert��update�ֿ�����
   --4,�ܹ�����һ�����v_doingStatus=9999�����ӹ������Լ��ı�ǣ��ӹ��̴�����ɺ���Ҫ�ѱ�ǻظ�������ǰ��ֵ
   --5,���ӹ��̶���������ܹ��̻�ѱ������Ϊv_okStatus=1
   
   v_prepareStatus := 0;
   v_okStatus := 1;
   v_doingStatus := 9999;
   v_contImportStatusValue := 10; --�Զ������״ֵ̬

   --�Ƿ���û��������ݣ�û�����쳣
   v_endTime := sysdate;strTemp := '#proc_import_sales_deal,provider_id='||p_providerId;updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   select count(1) into v_tempId from ZL_CONT_SALES_IMPORT where (update_status >= 1 or update_status is null or update_status = -1 ) and import_status=0 and provider_id= p_providerId;
   if v_tempId = 0 then
      dbms_output.put_line('not_found_data');
      raise not_found_data;
      --return;
   end if;

   --���Ҫ���������
   update ZL_CONT_SALES_IMPORT set import_status=v_prepareStatus where provider_id=p_providerId and (import_status is null or import_status =0);
   update ZL_CONT_SALES_IMPORT set import_status=v_doingStatus where provider_id=p_providerId and import_status=v_prepareStatus;
   --�޳���Ʒ���Ϊ�յ�����
   update ZL_CONT_SALES_IMPORT set import_status=-1 where provider_id=p_providerId and import_status=v_doingStatus and sales_no is null;
   commit;

   --begin: ����insert��ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_insert_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);

   --call proc_import_sales_insert_deal
   proc_import_sales_insert_deal(p_providerId, v_doingStatus,v_doingStatus-2999,v_contImportStatusValue,v_tempId);  
   updateCount := updateCount || ',zl_cont_sales=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_insert_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime);updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   if v_tempId = 0 then--˫�ر���,û����������
      null;--raise not_found_data;
      dbms_output.put_line('insert empty');
   end if;
   --insert ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-2999;
   commit;
   --end: ����insert

   --begin: ����update�� ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_update_deal:begin at:'||to_char(v_startTime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);

   --call proc_import_sales_update_deal
   proc_import_sales_update_deal(p_providerId, v_doingStatus,v_doingStatus-3999,v_contImportStatusValue,v_tempId);
   updateCount := updateCount || ',zl_cont_sales=' || v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_update_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime);updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   if v_tempId = 0 then
      null;
      dbms_output.put_line('update empty');
   end if;
   
   --update ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-3999;
   commit;
   --end: ����update�� ZL_CONT_SALES_IMPORT

   --begin: ����delete: ZL_CONT_SALES_IMPORT
   v_startTime := sysdate;strTemp := '#proc_import_sales_delete_deal: begin at:'||to_char(v_starttime,'yyyy-mm-dd hh24:mi:ss');updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   --call proc_import_sales_delete_deal
   proc_import_sales_delete_deal(p_providerId, v_doingStatus,v_doingStatus-4999,v_tempId);
   v_tempId :=0;
   updateCount :=updateCount || ',zl_cont_sales='||v_tempId;
   v_endTime := sysdate;strTemp := '#proc_import_sales_delete_deal,end at:'||to_char(v_endTime,'yyyy-mm-dd hh24:mi:ss')||',used time='||calculate_time(v_startTime,v_endTime);updateCount := updateCount || strTemp;dbms_output.put_line(strTemp);
   if v_tempId = 0 then
      null;
      dbms_output.put_line('delete empty');
   end if;

   -- delete ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus where provider_id=p_providerId and import_status=v_doingStatus-4999;
   commit;
   --end: ����delete: ZL_CONT_SALES_IMPORT

   --��������֧����ʽ
   v_paytypes := '';
   if (p_providerId = 215518585) then --������
      v_paytypes := '0,6';--select wm_concat(id) into v_paytypes from ZL_SALES_PAY_TYPE where pay_type in (0,4);
   elsif (p_providerId = 215633968) then --����׬��
      v_paytypes := '2,5';--select wm_concat(id) into v_paytypes from ZL_SALES_PAY_TYPE where pay_type in (1,3);
   elsif (p_providerId = 216099916) then --��Ƥ
      v_paytypes := '2,5';--select wm_concat(id) into v_paytypes from ZL_SALES_PAY_TYPE where pay_type in (0,4);
   end if;
   if (v_paytypes is not null) then
      update ZL_CONT_SALES set PAY_TYPE_IDS=v_paytypes where provider_id=p_providerId and PAY_TYPE_IDS is null and c_id in (select id from zl_cont where type=8 and locked!=1);
      commit;
   end if;

   --������ɺ���Ϊ���ߣ�ͬʱҲ��ʶ��������
   select nvl(min(update_status),-1) into v_tempId from ZL_CONT_SALES_IMPORT where provider_id=p_providerId;
   v_tempId := case when v_tempId >=0 then -1 else v_tempId-1 end;

   --all ok
   update ZL_CONT_SALES_IMPORT set import_status=v_okStatus,update_status=v_tempId where provider_id=p_providerId and import_status=v_doingStatus;
   commit;
end proc_import_sales_deal;
/