alter table ZL_CONT add video_seg_time VARCHAR2(1024);

comment on column ZL_CONT.video_seg_time is '视频打点信息';
