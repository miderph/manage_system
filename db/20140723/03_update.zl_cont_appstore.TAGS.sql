update zl_cont_appstore app set tags=(
      select tags from ZL_APP_BASE_INFO_IMPORT import
         where rownum=1 and import.tags is not null and import.c_id=app.c_id and import.version_code=app.version_code
   )
   where tags is null;