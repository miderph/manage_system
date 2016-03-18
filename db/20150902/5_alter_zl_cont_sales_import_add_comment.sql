-- Add comments to the columns 
comment on column ZL_CONT_SALES_IMPORT.import_status
  is '0 未处理  1 已处理';
comment on column ZL_CONT_SALES_IMPORT.update_status
  is '1 新增 2 更新  -1 待删除';
