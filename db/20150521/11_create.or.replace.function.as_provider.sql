create or replace function as_provider(p_providerId in integer, p_show_type in varchar) return integer is
  Result integer;
   v_providerid_zhuanbao number(20);
   v_providerid_tianmao  number(20);
   v_providerid_taobao   number(20);
begin
   v_providerid_zhuanbao := 215633968;--部署后需修改此常量
   v_providerid_tianmao  := 216046209;--部署后需修改此常量
   v_providerid_taobao   := 216046208;--部署后需修改此常量

   /*--转换providerid【B天猫C淘宝】
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