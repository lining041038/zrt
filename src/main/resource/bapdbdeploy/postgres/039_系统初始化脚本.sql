
CREATE TABLE WCM_SITE_PUBLISH_HIS (sid varchar(36) NOT NULL, sitesid varchar(36) NOT NULL, createtime timestamp(7), CONSTRAINT PK__WCM_SITE__DDDFDD3610216507 PRIMARY KEY (sid));
CREATE TABLE WCM_SITE (sid varchar(36) NOT NULL, name varchar(200), CONSTRAINT PK__WCM_SITE__DDDFDD360C50D423 PRIMARY KEY (sid));
CREATE TABLE WCM_PAGE (sid varchar(36) NOT NULL, name varchar(500), templatesid varchar(36), templatename varchar(200), sitesid varchar(36) NOT NULL, pagetype varchar(1), CONSTRAINT PK__WCM_PAGE__DDDFDD3600DF2177 PRIMARY KEY (sid));
CREATE TABLE WCM_CONTAINER_DATA (sid varchar(36) NOT NULL, assetsid varchar(36) NOT NULL, pagesid varchar(36) NOT NULL, containersid varchar(36), assettype varchar(1), CONSTRAINT PK__WCM_CONT__DDDFDD3604AFB25B PRIMARY KEY (sid));
CREATE TABLE WCM_ASSET (sid varchar(36) NOT NULL, name varchar(200), assetdata bytea, assettype varchar(1), CONSTRAINT PK__WCM_ASSE__DDDFDD360880433F PRIMARY KEY (sid));
CREATE TABLE SYS_USER_UNIT_RELATE (usersid varchar(200) NOT NULL, unitsid varchar(200) NOT NULL, CONSTRAINT pk_sys_user_unit PRIMARY KEY (usersid, unitsid));
CREATE TABLE SYS_USER_ROLE_RELATE (usersid varchar(200) NOT NULL, rolesid varchar(200) NOT NULL, CONSTRAINT PK__SYS_USER_ROLE_RE__62E4AA3C PRIMARY KEY (usersid, rolesid));
CREATE TABLE SYS_USER (sid varchar(200) NOT NULL, username varchar(100), password varchar(100), name varchar(100), mail varchar(200), parentusersid varchar(200), extno varchar(200), officephone varchar(200), theme varchar(100), creatorsid varchar(36), creatorname varchar(36), createtime timestamp(7), pwdquestion varchar(1000), pwdanswer varchar(1000), gender varchar(1), CONSTRAINT PK__SYS_USER__5F141958 PRIMARY KEY (sid));
CREATE TABLE SYS_UNIT (sid varchar(200) NOT NULL, parentsid varchar(200), name varchar(200), code varchar(200), leaderusersid varchar(200), unittype int4, lastupdate timestamp(7), flag varchar(1), unitsafb varchar(200), CONSTRAINT PK__SYS_UNIT__66B53B20 PRIMARY KEY (sid));
CREATE TABLE SYS_ROLE (sid varchar(200) NOT NULL, name varchar(100), indexpath varchar(500), flag varchar(1), CONSTRAINT PK__SYS_ROLE__60FC61CA PRIMARY KEY (sid));
CREATE TABLE SYS_REP_TMPL (sid varchar(36) NOT NULL, groupsid varchar(36), title varchar(100), htmltable text, dbtablename varchar(100), isrelease int4, CONSTRAINT PK__sys_rep_tmpl__6CA31EA0 PRIMARY KEY (sid));
CREATE TABLE SYS_REP_DICT_ITEM (sid varchar(36) NOT NULL, title varchar(100), dict_sid varchar(36), CONSTRAINT PK__SYS_REP___DDDFDD361367E606 PRIMARY KEY (sid));
CREATE TABLE SYS_REP_DICT (sid varchar(36) NOT NULL, title varchar(100), selectmode int4, CONSTRAINT PK__SYS_REP___DDDFDD36173876EA PRIMARY KEY (sid));
CREATE TABLE SYS_OPTS (sid varchar(36) NOT NULL, name varchar(500), key_ varchar(50), value varchar(4000), enumxmlname varchar(50), group_name varchar(1000), group_key varchar(1000), formkey varchar(300), CONSTRAINT PK__SYS_OPTS__DDDFDD361B0907CE PRIMARY KEY (sid));
CREATE TABLE SYS_MULTI_UNITLEADER_RELATE (usersid varchar(200) NOT NULL, unitsid varchar(200) NOT NULL, CONSTRAINT PK__SYS_MULT__456FA6B21ED998B2 PRIMARY KEY (usersid, unitsid));
CREATE TABLE SYS_MAPREGION_UNIT_RELATE (sid varchar(50) NOT NULL, regionsid varchar(50), regionname varchar(50), unitsid varchar(200), unitname varchar(200), CONSTRAINT PK__SYS_MAPR__DDDFDD3622AA2996 PRIMARY KEY (sid));
CREATE TABLE SYS_LOG (sid varchar(36) NOT NULL, loglevel varchar(100), message varchar(1000), logclass varchar(500), createtime timestamp(7), CONSTRAINT PK__SYS_LOG__DDDFDD3615DA3E5D PRIMARY KEY (sid));
CREATE TABLE SYS_FLOATREP_TMPL (sid varchar(32) NOT NULL, groupsid varchar(32), title varchar(100), dbtablename varchar(100), isrelease bool, CONSTRAINT PK__SYS_FLOA__DDDFDD36267ABA7A PRIMARY KEY (sid));
CREATE TABLE SYS_FLOATREP_DBCOLINFO (sid varchar(32) NOT NULL, colname varchar(32), coltitle varchar(32), colwidth float4, edittype varchar(32), sumtype varchar(2000), tmplsid varchar(32), dictsid varchar(32), CONSTRAINT PK__SYS_FLOATREP_DBC__5BCD9859 PRIMARY KEY (sid));
CREATE TABLE SYS_FILES (sid varchar(36) NOT NULL, filegroupsid varchar(36), filename varchar(100), filesize int4, filedata bytea, uploaddate timestamp(7), ispermanent bool DEFAULT '1', uploadtimeid varchar(36), willdelete varchar(1), filedesc varchar(100), CONSTRAINT PK__SYS_FILE__DDDFDD362C3393D0 PRIMARY KEY (sid));
CREATE TABLE SYS_AUTHORITY (roleorusersid varchar(200) NOT NULL, functionid varchar(50) NOT NULL, CONSTRAINT PK__SYS_AUTHORITY__64CCF2AE PRIMARY KEY (roleorusersid, functionid));
CREATE TABLE QRTZ_TRIGGERS (TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, JOB_NAME varchar(200) NOT NULL, JOB_GROUP varchar(200) NOT NULL, IS_VOLATILE varchar(1) NOT NULL, DESCRIPTION varchar(250), NEXT_FIRE_TIME int8, PREV_FIRE_TIME int8, PRIORITY int4, TRIGGER_STATE varchar(16) NOT NULL, TRIGGER_TYPE varchar(8) NOT NULL, START_TIME int8 NOT NULL, END_TIME int8, CALENDAR_NAME varchar(200), MISFIRE_INSTR int2, JOB_DATA bytea, CONSTRAINT PK_QRTZ_TRIGGERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP));
CREATE TABLE QRTZ_TRIGGER_LISTENERS (TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, TRIGGER_LISTENER varchar(200) NOT NULL, CONSTRAINT PK_QRTZ_TRIGGER_LISTENERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_LISTENER));
CREATE TABLE QRTZ_SIMPLE_TRIGGERS (TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, REPEAT_COUNT int8 NOT NULL, REPEAT_INTERVAL int8 NOT NULL, TIMES_TRIGGERED int8 NOT NULL, CONSTRAINT PK_QRTZ_SIMPLE_TRIGGERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP));
CREATE TABLE QRTZ_SCHEDULER_STATE (INSTANCE_NAME varchar(200) NOT NULL, LAST_CHECKIN_TIME int8 NOT NULL, CHECKIN_INTERVAL int8 NOT NULL, CONSTRAINT PK_QRTZ_SCHEDULER_STATE PRIMARY KEY (INSTANCE_NAME));
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (TRIGGER_GROUP varchar(200) NOT NULL, CONSTRAINT PK_QRTZ_PAUSED_TRIGGER_GRPS PRIMARY KEY (TRIGGER_GROUP));
CREATE TABLE QRTZ_LOCKS (LOCK_NAME varchar(40) NOT NULL, CONSTRAINT PK_QRTZ_LOCKS PRIMARY KEY (LOCK_NAME));
CREATE TABLE QRTZ_JOB_LISTENERS (JOB_NAME varchar(200) NOT NULL, JOB_GROUP varchar(200) NOT NULL, JOB_LISTENER varchar(200) NOT NULL, CONSTRAINT PK_QRTZ_JOB_LISTENERS PRIMARY KEY (JOB_NAME, JOB_GROUP, JOB_LISTENER));
CREATE TABLE QRTZ_JOB_DETAILS (JOB_NAME varchar(200) NOT NULL, JOB_GROUP varchar(200) NOT NULL, DESCRIPTION varchar(250), JOB_CLASS_NAME varchar(250) NOT NULL, IS_DURABLE varchar(1) NOT NULL, IS_VOLATILE varchar(1) NOT NULL, IS_STATEFUL varchar(1) NOT NULL, REQUESTS_RECOVERY varchar(1) NOT NULL, JOB_DATA bytea, CONSTRAINT PK_QRTZ_JOB_DETAILS PRIMARY KEY (JOB_NAME, JOB_GROUP));
CREATE TABLE QRTZ_FIRED_TRIGGERS (ENTRY_ID varchar(95) NOT NULL, TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, IS_VOLATILE varchar(1) NOT NULL, INSTANCE_NAME varchar(200) NOT NULL, FIRED_TIME int8 NOT NULL, PRIORITY int4 NOT NULL, STATE varchar(16) NOT NULL, JOB_NAME varchar(200), JOB_GROUP varchar(200), IS_STATEFUL varchar(1), REQUESTS_RECOVERY varchar(1), CONSTRAINT PK_QRTZ_FIRED_TRIGGERS PRIMARY KEY (ENTRY_ID));
CREATE TABLE QRTZ_CRON_TRIGGERS (TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, CRON_EXPRESSION varchar(120) NOT NULL, TIME_ZONE_ID varchar(80), CONSTRAINT PK_QRTZ_CRON_TRIGGERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP));
CREATE TABLE QRTZ_CALENDARS (CALENDAR_NAME varchar(200) NOT NULL, CALENDAR bytea NOT NULL, CONSTRAINT PK_QRTZ_CALENDARS PRIMARY KEY (CALENDAR_NAME));
CREATE TABLE QRTZ_BLOB_TRIGGERS (TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, BLOB_DATA bytea);
CREATE TABLE DEMO_SINGLE_DATAGRID (sid varchar(36) NOT NULL, name varchar(300), treenode_sid varchar(36), gender varchar(1), age_ int4, clobtest text, formkey varchar(300), imgpath varchar(500), sortfield varchar(36), CONSTRAINT PK__DEMO_SIN__DDDFDD3622751F6C PRIMARY KEY (sid));
CREATE TABLE DEMO_LEFT_TREE (sid varchar(36) NOT NULL, name varchar(500), parentsid varchar(36) NOT NULL, sortfield varchar(36), CONSTRAINT PK__DEMO_LEF__DDDFDD362645B050 PRIMARY KEY (sid));
CREATE TABLE DEMO_BOOKORDER (sid varchar(36) NOT NULL, bookname varchar(400), creatorsid varchar(36), creatorname varchar(300), createtime timestamp(7), proc_inst_id_ varchar(128), CONSTRAINT PK__DEMO_BOO__DDDFDD36793DFFAF PRIMARY KEY (sid));
CREATE TABLE CMS_CATEGORY (sid varchar(50), category_title varchar(100) NOT NULL, category_status varchar(10), category_site varchar(50), category_order int4, creator varchar(50), creator_dept varchar(50), create_time timestamp(7));
CREATE TABLE CMS_ARTICLE (sid varchar(100), article_title varchar(1000), article_content text, create_date timestamp(7), modify_date timestamp(7), creator_name varchar(200), article_keywords varchar(200), article_resource varchar(200), article_summary varchar(500), creator_id varchar(100), article_category varchar(100), status varchar(100), article_order int4, clicktime int4, article_att varchar(50));
CREATE UNIQUE INDEX idx_forusername ON SYS_USER (username);




create table ACT_GE_PROPERTY (
    NAME_ varchar(64),
    VALUE_ varchar(300),
    REV_ integer,
    primary key (NAME_)
);



create table ACT_GE_BYTEARRAY (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    DEPLOYMENT_ID_ varchar(64),
    BYTES_ bytea,
    GENERATED_ boolean,
    primary key (ID_)
);

create table ACT_RE_DEPLOYMENT (
    ID_ varchar(64),
    NAME_ varchar(255),
    DEPLOY_TIME_ timestamp,
    primary key (ID_)
);

create table ACT_RU_EXECUTION (
    ID_ varchar(64),
    REV_ integer,
    PROC_INST_ID_ varchar(64),
    BUSINESS_KEY_ varchar(255),
    PARENT_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
	  SUPER_EXEC_ varchar(64),
    ACT_ID_ varchar(255),
    IS_ACTIVE_ boolean,
    IS_CONCURRENT_ boolean,
	  IS_SCOPE_ boolean,
	IS_EVENT_SCOPE_ boolean,
	SUSPENSION_STATE_ integer,
    primary key (ID_),
    unique (PROC_DEF_ID_, BUSINESS_KEY_)
);

create table ACT_RU_JOB (
    ID_ varchar(64) NOT NULL,
	  REV_ integer,
    TYPE_ varchar(255) NOT NULL,
    LOCK_EXP_TIME_ timestamp,
    LOCK_OWNER_ varchar(255),
    EXCLUSIVE_ boolean,
    EXECUTION_ID_ varchar(64),
    PROCESS_INSTANCE_ID_ varchar(64),
    RETRIES_ integer,
    EXCEPTION_STACK_ID_ varchar(64),
    EXCEPTION_MSG_ varchar(4000),
    DUEDATE_ timestamp,
    REPEAT_ varchar(255),
    HANDLER_TYPE_ varchar(255),
    HANDLER_CFG_ varchar(4000),
    primary key (ID_)
);

create table ACT_RE_PROCDEF (
    ID_ varchar(64),
    REV_ integer,
    CATEGORY_ varchar(255),
    NAME_ varchar(255),
    KEY_ varchar(255),
    VERSION_ integer,
    DEPLOYMENT_ID_ varchar(64),
    RESOURCE_NAME_ varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    HAS_START_FORM_KEY_ boolean,
    SUSPENSION_STATE_ integer,
    primary key (ID_)
);

create table ACT_RU_TASK (
    ID_ varchar(64),
    REV_ integer,
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    NAME_ varchar(255),
    PARENT_TASK_ID_ varchar(64),
    DESCRIPTION_ varchar(4000),
    TASK_DEF_KEY_ varchar(255),
    OWNER_ varchar(64),
    ASSIGNEE_ varchar(64),
    DELEGATION_ varchar(64),
    PRIORITY_ integer,
    CREATE_TIME_ timestamp,
    DUE_DATE_ timestamp,
    primary key (ID_)
);

create table ACT_RU_IDENTITYLINK (
    ID_ varchar(64),
    REV_ integer,
    GROUP_ID_ varchar(64),
    TYPE_ varchar(255),
    USER_ID_ varchar(64),
    TASK_ID_ varchar(64),
    primary key (ID_)
);

create table ACT_RU_VARIABLE (
    ID_ varchar(64) not null,
    REV_ integer,
    TYPE_ varchar(255) not null,
    NAME_ varchar(255) not null,
    EXECUTION_ID_ varchar(64),
	  PROC_INST_ID_ varchar(64),
    TASK_ID_ varchar(64),
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double precision,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
);

create table ACT_RU_EVENT_SUBSCR (
    ID_ varchar(64) not null,
    REV_ integer,
    EVENT_TYPE_ varchar(255) not null,
    EVENT_NAME_ varchar(255),
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    ACTIVITY_ID_ varchar(64),
    CONFIGURATION_ varchar(255),
    CREATED_ timestamp not null,
    primary key (ID_)
);

create index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION(BUSINESS_KEY_);
create index ACT_IDX_TASK_CREATE on ACT_RU_TASK(CREATE_TIME_);
create index ACT_IDX_IDENT_LNK_USER on ACT_RU_IDENTITYLINK(USER_ID_);
create index ACT_IDX_IDENT_LNK_GROUP on ACT_RU_IDENTITYLINK(GROUP_ID_);
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on ACT_RU_EVENT_SUBSCR(CONFIGURATION_);
    

create table ACT_HI_PROCINST (
    ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    BUSINESS_KEY_ varchar(255),
    PROC_DEF_ID_ varchar(64) not null,
    START_TIME_ timestamp not null,
    END_TIME_ timestamp,
    DURATION_ bigint,
    START_USER_ID_ varchar(255),
    START_ACT_ID_ varchar(255),
    END_ACT_ID_ varchar(255),
    SUPER_PROCESS_INSTANCE_ID_ varchar(64),
    DELETE_REASON_ varchar(4000),
    primary key (ID_),
    unique (PROC_INST_ID_),
    unique (PROC_DEF_ID_, BUSINESS_KEY_)
);

create table ACT_HI_ACTINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    EXECUTION_ID_ varchar(64) not null,
    ACT_ID_ varchar(255) not null,
    ACT_NAME_ varchar(255),
    ACT_TYPE_ varchar(255) not null,
    ASSIGNEE_ varchar(64),
    START_TIME_ timestamp not null,
    END_TIME_ timestamp,
    DURATION_ bigint,
    primary key (ID_)
);

create table ACT_HI_TASKINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64),
    TASK_DEF_KEY_ varchar(255),
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    NAME_ varchar(255),
    PARENT_TASK_ID_ varchar(64),
    DESCRIPTION_ varchar(4000),
    OWNER_ varchar(64),
    ASSIGNEE_ varchar(64),
    START_TIME_ timestamp not null,
    END_TIME_ timestamp,
    DURATION_ bigint,
    DELETE_REASON_ varchar(4000),
    PRIORITY_ integer,
    DUE_DATE_ timestamp,
    primary key (ID_)
);

create table ACT_HI_DETAIL (
    ID_ varchar(64) not null,
    TYPE_ varchar(255) not null,
    PROC_INST_ID_ varchar(64) not null,
    EXECUTION_ID_ varchar(64) not null,
    TASK_ID_ varchar(64),
    ACT_INST_ID_ varchar(64),
    NAME_ varchar(255) not null,
    VAR_TYPE_ varchar(64),
    REV_ integer,
    TIME_ timestamp not null,
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double precision,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
);

create table ACT_HI_COMMENT (
    ID_ varchar(64) not null,
    TYPE_ varchar(255),
    TIME_ timestamp not null,
    USER_ID_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    ACTION_ varchar(255),
    MESSAGE_ varchar(4000),
    FULL_MSG_ bytea,
    primary key (ID_)
);

create table ACT_HI_ATTACHMENT (
    ID_ varchar(64) not null,
    REV_ integer,
    USER_ID_ varchar(255),
    NAME_ varchar(255),
    DESCRIPTION_ varchar(4000),
    TYPE_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    URL_ varchar(4000),
    CONTENT_ID_ varchar(64),
    primary key (ID_)
);


create index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST(END_TIME_);
create index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST(BUSINESS_KEY_);
create index ACT_IDX_HI_ACT_INST_START on ACT_HI_ACTINST(START_TIME_);
create index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST(END_TIME_);
create index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL(PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL(ACT_INST_ID_);
create index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL(TIME_);
create index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL(NAME_);





create table ACT_ID_GROUP (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    TYPE_ varchar(255),
    primary key (ID_)
);

create table ACT_ID_MEMBERSHIP (
    USER_ID_ varchar(64),
    GROUP_ID_ varchar(64),
    primary key (USER_ID_, GROUP_ID_)
);

create table ACT_ID_USER (
    ID_ varchar(64),
    REV_ integer,
    FIRST_ varchar(255),
    LAST_ varchar(255),
    EMAIL_ varchar(255),
    PWD_ varchar(255),
    PICTURE_ID_ varchar(64),
    primary key (ID_)
);

create table ACT_ID_INFO (
    ID_ varchar(64),
    REV_ integer,
    USER_ID_ varchar(64),
    TYPE_ varchar(64),
    KEY_ varchar(255),
    VALUE_ varchar(255),
    PASSWORD_ bytea,
    PARENT_ID_ varchar(255),
    primary key (ID_)
);

CREATE TABLE ACT_AN_AUDIT_HIS (sid varchar(36) NOT NULL, proc_inst_id_ varchar(128), unitname varchar(1000), assignee varchar(128), assigneename varchar(128), result varchar(200), taskname varchar(1000), createtime timestamp(7), comment_ varchar(8000), CONSTRAINT PK__ACT_AN_A__DDDFDD367D0E9093 PRIMARY KEY (sid));

/****** Object:  Table SYS_USER_UNIT_RELATE   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO SYS_USER_UNIT_RELATE(usersid, unitsid) VALUES ('1234', 'orgroot');
/****** Object:  Table SYS_USER_ROLE_RELATE   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO SYS_USER_ROLE_RELATE(usersid, rolesid) VALUES ('1234', '153');
/****** Object:  Table SYS_USER   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO SYS_USER(sid, username, password, name, mail, parentusersid, extno, officephone, theme, creatorsid, creatorname, createtime, pwdquestion, pwdanswer, gender) VALUES ('1234', 'admin', '0DPiKuNIrrVmD8IUCuw1hQxNqZc=', '系统管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
/****** Object:  Table SYS_ROLE   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO SYS_ROLE(sid, name, indexpath, flag) VALUES ('153', '系统管理员', NULL, NULL);
/****** Object:  Table SYS_OPTS   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO SYS_OPTS(sid, name, key_, value, enumxmlname) VALUES ('antelope_fmspublishpassword', '视频流发布用授权密码', 'antelope_fmspublishpassword', 'password', NULL);
INSERT INTO SYS_OPTS(sid, name, key_, value, enumxmlname) VALUES ('antelope_ipmulticastaddr', '局域网内部IP组播地址与端口', 'antelope_ipmulticastaddr', '224.0.1.2:30000', NULL);
/****** Object:  Table QRTZ_LOCKS   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO QRTZ_LOCKS(LOCK_NAME) VALUES ('CALENDAR_ACCESS');
INSERT INTO QRTZ_LOCKS(LOCK_NAME) VALUES ('JOB_ACCESS');
INSERT INTO QRTZ_LOCKS(LOCK_NAME) VALUES ('MISFIRE_ACCESS');
INSERT INTO QRTZ_LOCKS(LOCK_NAME) VALUES ('STATE_ACCESS');
INSERT INTO QRTZ_LOCKS(LOCK_NAME) VALUES ('TRIGGER_ACCESS');
/****** Object:  Table DEMO_SINGLE_DATAGRID   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO DEMO_SINGLE_DATAGRID(sid, name, treenode_sid, gender, age_) VALUES ('1234', '测试记录', NULL, NULL, NULL);
/****** Object:  Table ACT_GE_PROPERTY   Script Date: 11/01/2012 15:33:52 ******/
INSERT INTO ACT_GE_PROPERTY(NAME_, VALUE_, REV_) VALUES ('historyLevel', '3', 1);
INSERT INTO ACT_GE_PROPERTY(NAME_, VALUE_, REV_) VALUES ('schema.history', 'create(5.9)', 1);
INSERT INTO ACT_GE_PROPERTY(NAME_, VALUE_, REV_) VALUES ('schema.version', '5.9', 1);
insert into ACT_GE_PROPERTY(NAME_, VALUE_, REV_) values ('next.dbid', '1', 1);
/****** Object:  Table ACT_AN_AUDIT_HIS   Script Date: 11/01/2012 15:33:51 ******/



/**
 * 局域网内部IP组播地址与端口更改
 */
update SYS_OPTS set value='234.1.1.1:5003' where sid='antelope_ipmulticastaddr';

update SYS_OPTS set group_name='视频与组播', group_key='videomcast' where sid in ('antelope_fmspublishpassword','antelope_ipmulticastaddr');


/**
 * 树相关界面控件演示树数据
 */
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052583', '北京', 'treeroot');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052597', '河北', 'treeroot');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052629', '海淀区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052643', '朝阳区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052657', '大兴区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052671', '通州区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052685', '昌平区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052699', '东城区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052713', '西城区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052727', '丰台区', '1355638052583');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052744', '张家口', '1355638052597');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052758', '邯郸', '1355638052597');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052772', '唐山', '1355638052597');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052786', '石家庄', '1355638052597');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052800', '邢台', '1355638052597');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052820', '怀来', '1355638052744');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052834', '涿鹿', '1355638052744');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052848', '阳原', '1355638052744');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052862', '宣化', '1355638052744');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052882', '沙城', '1355638052820');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052896', '鸡鸣驿', '1355638052820');
INSERT into DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052910', '新保安', '1355638052820');



insert into DEMO_SINGLE_DATAGRID(sid, name, treenode_sid, gender, age_, formkey) values('2345', '测试formkeyfield', null, null, null, '/demos/multiple_datagridsdemo_formkeyform.jsp');




/**
 * demo表添加imgpath字段
 */


insert into DEMO_SINGLE_DATAGRID 
values('13622776068292', '哆啦A梦', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a111634u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('1362277606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a128335u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('14462277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134153u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('1562277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134394u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622757606929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a138731u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776006929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a214107_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136221277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a218261u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227712606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a262783_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227237606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a304007_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227763406929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a306705_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a150907u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776076929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a151177u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13682277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a167419u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622779606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a203937_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776068929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/vrsa8848u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('11362277606829', '哆啦A梦', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a111634u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('21362277606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a128335u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('314462277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134153u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('41562277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134394u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('513622757606929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a138731u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('613622776606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a150907u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('713622776076929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a151177u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('813682277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a167419u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('913622779606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a203937_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('2113622776006929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a214107_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('22136221277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a218261u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('23136227712606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a262783_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('231136227237606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a304007_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('34136227763406929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a306705_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('45136227745606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332192_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('56136227756606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332321_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('67136227767606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a8821u2_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('78136227767806929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9273_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('8913622776076929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9982u2_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('9013622776068929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/vrsa8848u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227745606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332192_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227756606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332321_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227767606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a8821u2_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227767806929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9273_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776076929a', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9982u2_160_90.jpg');


/**
 * demogrid表tile数据附加挂接到左侧树节点_
 */

update DEMO_SINGLE_DATAGRID set treenode_sid= '1355638052583' where formkey is not null;

update SYS_USER set name='超级管理员' where sid='1234';

update SYS_ROLE set name='超级管理员' where sid='153';


























