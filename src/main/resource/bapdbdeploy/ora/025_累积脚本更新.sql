/**
 * 局域网内部IP组播地址与端口更改
 */
update SYS_OPTS set value='224.0.1.2:30000' where sid='antelope_ipmulticastaddr';


/**
 * 局域网内部IP组播地址与端口更改
 */
alter table SYS_OPTS modify name varchar2(500);


/**
 * 局域网内部IP组播地址与端口更改
 */
update SYS_OPTS set value='234.1.1.1:5003' where sid='antelope_ipmulticastaddr';



alter table SYS_OPTS add group_name varchar2(1000);
alter table SYS_OPTS add group_key varchar2(1000);

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
drop table TMP_SYS_USER;
drop table TMP_SYS_USER_UNIT_RELATE;
drop table WORKITEMINFO;

/**
 * 树相关界面控件演示树数据
 */
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052583', '北京', 'treeroot');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052597', '河北', 'treeroot');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052629', '海淀区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052643', '朝阳区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052657', '大兴区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052671', '通州区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052685', '昌平区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052699', '东城区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052713', '西城区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052727', '丰台区', '1355638052583');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052744', '张家口', '1355638052597');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052758', '邯郸', '1355638052597');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052772', '唐山', '1355638052597');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052786', '石家庄', '1355638052597');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052800', '邢台', '1355638052597');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052820', '怀来', '1355638052744');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052834', '涿鹿', '1355638052744');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052848', '阳原', '1355638052744');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052862', '宣化', '1355638052744');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052882', '沙城', '1355638052820');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052896', '鸡鸣驿', '1355638052820');
INSERT INTO DEMO_LEFT_TREE (sid, name, parentsid) VALUES ('1355638052910', '新保安', '1355638052820');


alter table DEMO_SINGLE_DATAGRID add clobtest clob;