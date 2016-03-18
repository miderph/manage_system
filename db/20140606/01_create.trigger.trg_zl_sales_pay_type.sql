create or replace trigger trg_zl_sales_pay_type before insert on zl_sales_pay_type for each row
begin

if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

end;
/