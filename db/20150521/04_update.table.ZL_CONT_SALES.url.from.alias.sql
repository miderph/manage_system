update zl_cont_sales t set url=(select alias from zl_cont_video video where video.c_id=t.c_id)
  where url is null and provider_id=215804695;
