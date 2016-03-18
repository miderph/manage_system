create or replace trigger trg_zl_cont_video_file before insert on zl_cont_video_file for each row
begin

if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

end;
/