prompt Importing table ZL_ACCESS_LOG_DICTS...
set feedback off
set define off
insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('GetMenu', '访问栏目列表', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('GetContentTVPlayUrl', '播放内容', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('GetContentList', '访问栏目内容列表', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('GetContentDetail', '访问内容详情', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('GetAppDownloadUrl', '下载应用', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('SearchContentList', '搜索内容', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('TVLogin', '用户登录', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('GetAppDetail', '获取应用详情', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('TVRegistration', '用户注册', 1);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('QDGH_SHOP', '宜可购物', 0);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('BESTV', '半岛蓝媒官方版', 0);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('BOYILE', '播亦乐', 0);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('QDGH_TEACH', '半岛教育', 0);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('QDGH', '半岛蓝媒', 0);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('QDGUANGHAN20', '半岛蓝媒2.0', 0);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('web', '牛人市场Web版', 0);

insert into ZL_ACCESS_LOG_DICTS (KEY, VALUE, TYPE)
values ('android', '其他Android平台', 0);

commit;

prompt Done.
