create or replace function as_provider(p_providerId in integer, p_show_type in varchar) return integer is
  Result integer;
   v_providerid_zhuanbao number(20);
   v_providerid_tianmao  number(20);
   v_providerid_taobao   number(20);
begin
   v_providerid_zhuanbao := 215633968;--��������޸Ĵ˳���
   v_providerid_tianmao  := 216046209;--��������޸Ĵ˳���
   v_providerid_taobao   := 216046208;--��������޸Ĵ˳���

   /*--ת��providerid��B��èC�Ա���
   case
      when p_providerId=v_providerid_zhuanbao and SHOP_TYPE='B' then v_providerid_tianmao
      when p_providerId=v_providerid_zhuanbao and SHOP_TYPE='C' then v_providerid_taobao
      else p_providerId
   end
   */
  return
         case --providerid
            when p_providerId=v_providerid_zhuanbao and p_show_type='B' then v_providerid_tianmao
            when p_providerId=v_providerid_zhuanbao and p_show_type='C' then v_providerid_taobao
            else p_providerId
         end;
end as_provider;
/