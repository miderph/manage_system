create or replace trigger trg_zl_cont_check
  before insert or update
  on ZL_CONT_CHECKS 
  for each row
declare
  -- local variables here
begin
  if (:NEW.ID is null) then
       select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
  end if;
end trg_zl_cont_check;
/
