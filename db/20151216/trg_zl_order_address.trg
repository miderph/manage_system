create or replace trigger trg_zl_order_address before insert on zl_order_address for each row
begin
if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

end;
/
