insert into ZL_STB_MODEL (ID, MODEL, NAME, PROVIDER_ID, CREATE_TIME, UPDATE_TIME, DESCRIPTION)
   select * from (select 1 ID, 'sw-F140d' MODEL, '≥Ø∏Ë–°–°∞◊' NAME, 29 PROVIDER_ID, to_date('15-08-2014 12:11:25', 'dd-mm-yyyy hh24:mi:ss') CREATE_TIME, null UPDATE_TIME, null DESCRIPTION from dual) import
     where not exists(select id from ZL_STB_MODEL model where model.MODEL=import.MODEL);

insert into ZL_STB_MODEL (ID, MODEL, NAME, PROVIDER_ID, CREATE_TIME, UPDATE_TIME, DESCRIPTION)
   select * from (select 2 ID, 'BoYiLe-F240c' MODEL, '≤•“‡¿÷' NAME, 29 PROVIDER_ID, to_date('15-08-2014 12:11:25', 'dd-mm-yyyy hh24:mi:ss') CREATE_TIME, null UPDATE_TIME, null DESCRIPTION from dual) import
     where not exists(select id from ZL_STB_MODEL model where model.MODEL=import.MODEL);