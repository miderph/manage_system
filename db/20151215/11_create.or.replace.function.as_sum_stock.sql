create or replace function as_sum_stock(p_providerId in integer, p_old_stock in varchar, p_new_stock in varchar) return integer is
   v_providerid_juanpi number(20);
   v_providerid_ghs number(20);
begin
   v_providerid_juanpi := 215848737;--��������޸Ĵ˳���
   v_providerid_ghs := 215518585;--��������޸Ĵ˳���

  return
         case --providerid
            when p_providerId=v_providerid_juanpi then p_new_stock --��Ƥ�п���ֶ�
            when p_providerId=v_providerid_ghs then p_new_stock    --�������п���ֶ�
            else p_old_stock
         end;
end as_sum_stock;
/