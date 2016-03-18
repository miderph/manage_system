create or replace function check_user_gids(p_cont_gids in varchar2, p_user_gids in varchar2) return integer is
v_pos1 integer;
v_pos0 integer;
begin
   if p_cont_gids is null then
      return 1;
   end if;
   if p_user_gids is null then
      return 0;
   end if;
   --p_cont_gids p_user_gids
   v_pos1 := instr(p_cont_gids,',');
   if v_pos1=0 then--v_pos=0只一个id
      if instr(','||p_user_gids||',', ','||p_cont_gids||',')>0 then
         return 1;
      else
         return 0;
      end if;
   end if;
   v_pos1 := instr(p_user_gids,',');
   if v_pos1=0 then--v_pos=0只一个id
      if instr(','||p_cont_gids||',', ','||p_user_gids||',')>0 then
         return 1;
      else
         return 0;
      end if;
   end if;
   
   v_pos0 := 1;
   loop
      v_pos1 := instr(p_cont_gids,',', v_pos0);
      if v_pos1=0 then
         v_pos1 := length(p_cont_gids)+1;
      end if;
      dbms_output.put_line('id='||substr(p_cont_gids,v_pos0,v_pos1-v_pos0));
      if instr(','||p_user_gids||',', ','||substr(p_cont_gids,v_pos0,v_pos1-v_pos0)||',')>0 then
         return 1;
      end if;
      
      v_pos0 := v_pos1+1;
   exit when (v_pos0>length(p_cont_gids));
   end loop;

   return 0;
end check_user_gids;
/