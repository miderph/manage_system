create or replace function as_sum_stock(p_providerId in integer, p_old_stock in varchar, p_new_stock in varchar) return integer is
   v_providerid_juanpi number(20);
   v_providerid_ghs number(20);
begin
   v_providerid_juanpi := 215848737;--部署后需修改此常量
   v_providerid_ghs := 215518585;--部署后需修改此常量

  return
         case --providerid
            when p_providerId=v_providerid_juanpi then p_new_stock --卷皮有库存字段
            when p_providerId=v_providerid_ghs then p_new_stock    --环球购物有库存字段
            else p_old_stock
         end;
end as_sum_stock;
/