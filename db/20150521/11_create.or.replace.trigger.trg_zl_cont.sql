create or replace trigger trg_zl_cont before insert or update on zl_cont for each row
begin
if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

if :NEW.create_time is null then
   :NEW.create_time := sysdate;
end if;

:NEW.modify_time := sysdate;
   
end;
/