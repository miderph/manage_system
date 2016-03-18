create or replace trigger trg_zl_user_group before insert or update on zl_user_group for each row
declare
    v_temp_value varchar2(4000);
begin
if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

v_temp_value := ','||NLS_UPPER(TRANSLATE(:NEW.raw_value, '$%:.- ', '$'))||',';
v_temp_value := TRANSLATE(v_temp_value,'$省','$');
v_temp_value := TRANSLATE(v_temp_value,'$市','$');
v_temp_value := TRANSLATE(v_temp_value,'$盟','$');
v_temp_value := replace(v_temp_value,'浩特','');
v_temp_value := replace(v_temp_value,'旗','');
v_temp_value := replace(v_temp_value,'黑龙江','黑龙');
v_temp_value := replace(v_temp_value,'内蒙古','内蒙');
v_temp_value := replace(v_temp_value,'自治区','');
v_temp_value := replace(v_temp_value,'壮族','');
v_temp_value := replace(v_temp_value,'回族','');
v_temp_value := replace(v_temp_value,'维吾尔族','');
v_temp_value := replace(v_temp_value,'维吾尔','');

:NEW.value := v_temp_value;

end;
/