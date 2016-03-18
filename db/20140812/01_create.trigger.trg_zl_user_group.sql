create or replace trigger trg_zl_user_group before insert or update on zl_user_group for each row
declare
    v_temp_value varchar2(4000);
begin
if (:NEW.ID is null) then
   select ZLSQ_DATA.NEXTVAL into :NEW.ID from dual;
end if;

v_temp_value := ','||NLS_UPPER(TRANSLATE(:NEW.raw_value, '$%:.- ', '$'))||',';
v_temp_value := TRANSLATE(v_temp_value,'$ʡ','$');
v_temp_value := TRANSLATE(v_temp_value,'$��','$');
v_temp_value := TRANSLATE(v_temp_value,'$��','$');
v_temp_value := replace(v_temp_value,'����','');
v_temp_value := replace(v_temp_value,'��','');
v_temp_value := replace(v_temp_value,'������','����');
v_temp_value := replace(v_temp_value,'���ɹ�','����');
v_temp_value := replace(v_temp_value,'������','');
v_temp_value := replace(v_temp_value,'׳��','');
v_temp_value := replace(v_temp_value,'����','');
v_temp_value := replace(v_temp_value,'ά�����','');
v_temp_value := replace(v_temp_value,'ά���','');

:NEW.value := v_temp_value;

end;
/