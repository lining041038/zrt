/**
 * 局域网内部IP组播地址与端口更改
 */
update SYS_OPTS set value='234.1.1.1:5003' where sid='antelope_ipmulticastaddr';


alter table SYS_OPTS add group_name varchar(1000);
alter table SYS_OPTS add group_key varchar(1000);

update SYS_OPTS set group_name='视频与组播', group_key='videomcast' where sid in ('antelope_fmspublishpassword','antelope_ipmulticastaddr');

drop table JBPM4_DEPLOYMENT;
drop table JBPM4_DEPLOYPROP;
drop table JBPM4_EXECUTION;
drop table JBPM4_HIST_ACTINST;
drop table JBPM4_HIST_DETAIL;
drop table JBPM4_HIST_PROCINST;
drop table JBPM4_HIST_TASK;
drop table JBPM4_HIST_VAR;
drop table JBPM4_ID_GROUP;
drop table JBPM4_ID_MEMBERSHIP;
drop table JBPM4_ID_USER;
drop table JBPM4_JOB;
drop table JBPM4_LOB;
drop table JBPM4_PARTICIPATION;
drop table JBPM4_PROPERTY;
drop table JBPM4_SWIMLANE;
drop table JBPM4_TASK;
drop table JBPM4_VARIABLE;
drop table PROCESS_DEFINITION;
drop table PROCESS_INSTANCE_INFO;
drop table PROCESS_NEXTMAN;
drop table PROCESS_TASK_INFO;
drop table PROCESSDEF_PROCESSPUB_INFO;
drop table PROCESSINSTANCEINFO;
drop table PROCESSINSTANCELOG;
drop table TMP_SYS_UNIT;
drop table TMP_SYS_USER;
drop table TMP_SYS_USER_UNIT_RELATE;
drop table WORKITEMINFO;
