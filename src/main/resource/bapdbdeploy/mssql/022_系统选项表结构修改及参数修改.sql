
alter table SYS_OPTS add group_name varchar(1000);
alter table SYS_OPTS add group_key varchar(1000);

update SYS_OPTS set group_name='视频与组播', group_key='videomcast' where sid in ('antelope_fmspublishpassword','antelope_ipmulticastaddr');
