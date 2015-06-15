/* 当前初始化脚本截至sqlserver脚本的第020个文件 */

CREATE TABLE WORKITEMINFO (WORKITEMID bigint(20), CREATIONDATE datetime, NAME varchar(255), PROCESSINSTANCEID bigint(20) NOT NULL, STATE bigint(20) NOT NULL, OPTLOCK int(11), WORKITEMBYTEARRAY blob);
CREATE TABLE TMP_SYS_USER_UNIT_RELATE (usersid varchar(36) NOT NULL, unitsid varchar(36) NOT NULL, CONSTRAINT pkdbrelate_usersid_unitsid PRIMARY KEY (usersid, unitsid));
CREATE TABLE TMP_SYS_USER (sid varchar(200) NOT NULL, username varchar(100), password varchar(100), name varchar(100), mail varchar(200), parentusersid varchar(200), officephone varchar(50), extno varchar(50), CONSTRAINT PK_TMP_SYS_USER PRIMARY KEY (sid));
CREATE TABLE TMP_SYS_UNIT (sid varchar(200) NOT NULL, parentsid varchar(200), name varchar(200), code varchar(200) NOT NULL, leaderusersid varchar(200), unittype varchar(50), lastupdate datetime, flag varchar(1), CONSTRAINT PK_TMP_SYS_UNIT PRIMARY KEY (sid));
CREATE TABLE SYS_USER_UNIT_RELATE (usersid varchar(36) NOT NULL, unitsid varchar(36) NOT NULL, CONSTRAINT pk_sys_user_unit PRIMARY KEY (usersid, unitsid));
CREATE TABLE SYS_USER_ROLE_RELATE (usersid varchar(36) NOT NULL, rolesid varchar(36) NOT NULL, CONSTRAINT PK__SYS_USER_ROLE_RE__62E4AA3C PRIMARY KEY (usersid, rolesid));
CREATE TABLE SYS_USER (sid varchar(200) NOT NULL, username varchar(100), password varchar(100), name varchar(100), mail varchar(200), parentusersid varchar(200), extno varchar(200), officephone varchar(200), theme varchar(100), creatorsid varchar(36), creatorname varchar(36), createtime datetime, pwdquestion varchar(1000), pwdanswer varchar(1000), gender varchar(1), CONSTRAINT PK__SYS_USER__5F141958 PRIMARY KEY (sid));
CREATE TABLE SYS_UNIT (sid varchar(200) NOT NULL, parentsid varchar(200), name varchar(200), code varchar(200), leaderusersid varchar(200), unittype int(11), lastupdate datetime, flag varchar(1), unitsafb varchar(200), CONSTRAINT PK__SYS_UNIT__66B53B20 PRIMARY KEY (sid));
CREATE TABLE SYS_ROLE (sid varchar(200) NOT NULL, name varchar(100), indexpath varchar(500), flag varchar(1), CONSTRAINT PK__SYS_ROLE__60FC61CA PRIMARY KEY (sid));
CREATE TABLE SYS_REP_TMPL (sid varchar(36) NOT NULL, groupsid varchar(36), title varchar(100), htmltable text, dbtablename varchar(100), isrelease int(11), CONSTRAINT PK__sys_rep_tmpl__6CA31EA0 PRIMARY KEY (sid));
CREATE TABLE SYS_REP_DICT_ITEM (sid varchar(36) NOT NULL, title varchar(100), dict_sid varchar(36), CONSTRAINT PK__SYS_REP___DDDFDD361367E606 PRIMARY KEY (sid));
CREATE TABLE SYS_REP_DICT (sid varchar(36) NOT NULL, title varchar(100), selectmode int(11), CONSTRAINT PK__SYS_REP___DDDFDD36173876EA PRIMARY KEY (sid));
CREATE TABLE SYS_OPTS (sid varchar(36) NOT NULL, name varchar(500), key_ varchar(50), value varchar(4000), enumxmlname varchar(50), CONSTRAINT PK__SYS_OPTS__DDDFDD361B0907CE PRIMARY KEY (sid));
CREATE TABLE SYS_MULTI_UNITLEADER_RELATE (usersid varchar(36) NOT NULL, unitsid varchar(36) NOT NULL, CONSTRAINT PK__SYS_MULT__456FA6B21ED998B2 PRIMARY KEY (usersid, unitsid));
CREATE TABLE SYS_MAPREGION_UNIT_RELATE (sid varchar(50) NOT NULL, regionsid varchar(50), regionname varchar(50), unitsid varchar(200), unitname varchar(200), CONSTRAINT PK__SYS_MAPR__DDDFDD3622AA2996 PRIMARY KEY (sid));
CREATE TABLE SYS_FLOATREP_TMPL (sid varchar(32) NOT NULL, groupsid varchar(32), title varchar(100), dbtablename varchar(100), isrelease tinyint(1), CONSTRAINT PK__SYS_FLOA__DDDFDD36267ABA7A PRIMARY KEY (sid));
CREATE TABLE SYS_FLOATREP_DBCOLINFO (sid varchar(32) NOT NULL, colname varchar(32), coltitle varchar(32), colwidth real, edittype varchar(32), sumtype varchar(2000), tmplsid varchar(32), dictsid varchar(32), CONSTRAINT PK__SYS_FLOATREP_DBC__5BCD9859 PRIMARY KEY (sid));
CREATE TABLE SYS_FILES (sid varchar(36) NOT NULL, filegroupsid varchar(36), filename varchar(100), filesize int(11), filedata longblob, uploaddate datetime, ispermanent tinyint(1) DEFAULT 1, uploadtimeid varchar(36), willdelete varchar(1), CONSTRAINT PK__SYS_FILE__DDDFDD362C3393D0 PRIMARY KEY (sid));
CREATE TABLE SYS_AUTHORITY (roleorusersid varchar(36) NOT NULL, functionid varchar(50) NOT NULL, CONSTRAINT PK__SYS_AUTHORITY__64CCF2AE PRIMARY KEY (roleorusersid, functionid));
CREATE TABLE QRTZ_TRIGGERS (TRIGGER_NAME varchar(100) NOT NULL, TRIGGER_GROUP varchar(100) NOT NULL, JOB_NAME varchar(200) NOT NULL, JOB_GROUP varchar(200) NOT NULL, IS_VOLATILE varchar(1) NOT NULL, DESCRIPTION varchar(250), NEXT_FIRE_TIME bigint(20), PREV_FIRE_TIME bigint(20), PRIORITY int(11), TRIGGER_STATE varchar(16) NOT NULL, TRIGGER_TYPE varchar(8) NOT NULL, START_TIME bigint(20) NOT NULL, END_TIME bigint(20), CALENDAR_NAME varchar(200), MISFIRE_INSTR smallint(6), JOB_DATA blob, CONSTRAINT PK_QRTZ_TRIGGERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP));
CREATE TABLE QRTZ_TRIGGER_LISTENERS (TRIGGER_NAME varchar(100) NOT NULL, TRIGGER_GROUP varchar(100) NOT NULL, TRIGGER_LISTENER varchar(100) NOT NULL, CONSTRAINT PK_QRTZ_TRIGGER_LISTENERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_LISTENER));
CREATE TABLE QRTZ_SIMPLE_TRIGGERS (TRIGGER_NAME varchar(150) NOT NULL, TRIGGER_GROUP varchar(150) NOT NULL, REPEAT_COUNT bigint(20) NOT NULL, REPEAT_INTERVAL bigint(20) NOT NULL, TIMES_TRIGGERED bigint(20) NOT NULL, CONSTRAINT PK_QRTZ_SIMPLE_TRIGGERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP));
CREATE TABLE QRTZ_SCHEDULER_STATE (INSTANCE_NAME varchar(200) NOT NULL, LAST_CHECKIN_TIME bigint(20) NOT NULL, CHECKIN_INTERVAL bigint(20) NOT NULL, CONSTRAINT PK_QRTZ_SCHEDULER_STATE PRIMARY KEY (INSTANCE_NAME));
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (TRIGGER_GROUP varchar(200) NOT NULL, CONSTRAINT PK_QRTZ_PAUSED_TRIGGER_GRPS PRIMARY KEY (TRIGGER_GROUP));
CREATE TABLE QRTZ_LOCKS (LOCK_NAME varchar(40) NOT NULL, CONSTRAINT PK_QRTZ_LOCKS PRIMARY KEY (LOCK_NAME));
CREATE TABLE QRTZ_JOB_LISTENERS (JOB_NAME varchar(100) NOT NULL, JOB_GROUP varchar(100) NOT NULL, JOB_LISTENER varchar(100) NOT NULL, CONSTRAINT PK_QRTZ_JOB_LISTENERS PRIMARY KEY (JOB_NAME, JOB_GROUP, JOB_LISTENER));
CREATE TABLE QRTZ_JOB_DETAILS (JOB_NAME varchar(150) NOT NULL, JOB_GROUP varchar(150) NOT NULL, DESCRIPTION varchar(250), JOB_CLASS_NAME varchar(250) NOT NULL, IS_DURABLE varchar(1) NOT NULL, IS_VOLATILE varchar(1) NOT NULL, IS_STATEFUL varchar(1) NOT NULL, REQUESTS_RECOVERY varchar(1) NOT NULL, JOB_DATA blob, CONSTRAINT PK_QRTZ_JOB_DETAILS PRIMARY KEY (JOB_NAME, JOB_GROUP));
CREATE TABLE QRTZ_FIRED_TRIGGERS (ENTRY_ID varchar(95) NOT NULL, TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, IS_VOLATILE varchar(1) NOT NULL, INSTANCE_NAME varchar(200) NOT NULL, FIRED_TIME bigint(20) NOT NULL, PRIORITY int(11) NOT NULL, STATE varchar(16) NOT NULL, JOB_NAME varchar(200), JOB_GROUP varchar(200), IS_STATEFUL varchar(1), REQUESTS_RECOVERY varchar(1), CONSTRAINT PK_QRTZ_FIRED_TRIGGERS PRIMARY KEY (ENTRY_ID));
CREATE TABLE QRTZ_CRON_TRIGGERS (TRIGGER_NAME varchar(150) NOT NULL, TRIGGER_GROUP varchar(150) NOT NULL, CRON_EXPRESSION varchar(120) NOT NULL, TIME_ZONE_ID varchar(80), CONSTRAINT PK_QRTZ_CRON_TRIGGERS PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP));
CREATE TABLE QRTZ_CALENDARS (CALENDAR_NAME varchar(200) NOT NULL, CALENDAR blob NOT NULL, CONSTRAINT PK_QRTZ_CALENDARS PRIMARY KEY (CALENDAR_NAME));
CREATE TABLE QRTZ_BLOB_TRIGGERS (TRIGGER_NAME varchar(200) NOT NULL, TRIGGER_GROUP varchar(200) NOT NULL, BLOB_DATA blob);
CREATE TABLE PROCESSINSTANCELOG (ID bigint(20) NOT NULL AUTO_INCREMENT, PROCESSINSTANCEID bigint(20), PROCESSID varchar(255), START_DATE datetime, END_DATE datetime, CONSTRAINT PK__PROCESSI__3214EC2747DBAE45 PRIMARY KEY (ID));
CREATE TABLE PROCESSINSTANCEINFO (INSTANCEID bigint(20), LASTMODIFICATIONDATE datetime, LASTREADDATE datetime, PROCESSID varchar(255), PROCESSINSTANCEBYTEARRAY blob, STARTDATE datetime, STATE int(11) NOT NULL, OPTLOCK int(11), PROCESSNAME varchar(250));
CREATE TABLE PROCESSDEF_PROCESSPUB_INFO (ID varchar(32) NOT NULL, DEPLOYMENT_ID varchar(32), PROCESSDEF_ID varchar(32), CONSTRAINT PK__PROCESSD__3214EC274CA06362 PRIMARY KEY (ID));
CREATE TABLE PROCESS_TASK_INFO (ID varchar(50) NOT NULL, TASKID varchar(50), TASKNAME varchar(50), ACTORID varchar(50), ACTORNAME varchar(50), CREATETIME datetime, DESCRIPTION varchar(500), COMPLETEDTIME datetime, PROCESSINSTANCEID varchar(50), INCOME varchar(150), OUTCOME varchar(150), ACTIVITYTYPE varchar(120), STATE int(11), ASSIGNNAME varchar(150), FORMRESOURCENAME varchar(400), ORIGINAL_ASSIGN_NAME varchar(250), BUSINESSAGENT varchar(250), COPYTO int(11), COPYPROCESSINSTANCEID varchar(50), COPYPROCESSNAME varchar(100), CONSTRAINT PK__PROCESS_TASK_INF__7B5B524B PRIMARY KEY (ID));
CREATE TABLE PROCESS_NEXTMAN (sid varchar(36) NOT NULL, processid varchar(36), username varchar(50), name varchar(20), taskid varchar(36), CONSTRAINT PK__PROCESS___DDDFDD3652593CB8 PRIMARY KEY (sid));
CREATE TABLE PROCESS_INSTANCE_INFO (ID varchar(50) NOT NULL, PROCESSINSTANCEID varchar(50), CREATOR varchar(50), CREATEDATE datetime, PROCESSNAME varchar(100), PROCESSVERSION int(11), CONSTRAINT PK__PROCESS_INSTANCE__797309D9 PRIMARY KEY (ID));
CREATE TABLE PROCESS_DEFINITION (ID varchar(50) NOT NULL, NAME varchar(50), JSON_STR text, XML_STR text, NODE_JSON_STR text, FLAG char(1), PROCESSVERSION int(11), CONSTRAINT PK__PROCESS_DEFINITI__778AC167 PRIMARY KEY (ID));
CREATE TABLE JBPM4_VARIABLE (DBID_ numeric(19, 0) NOT NULL, CLASS_ varchar(255) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, KEY_ varchar(255), CONVERTER_ varchar(255), HIST_ numeric(1, 0), EXECUTION_ numeric(19, 0), TASK_ numeric(19, 0), LOB_ numeric(19, 0), DATE_VALUE_ datetime, DOUBLE_VALUE_ float, CLASSNAME_ varchar(255), LONG_VALUE_ numeric(19, 0), STRING_VALUE_ varchar(255), TEXT_VALUE_ text, EXESYS_ numeric(19, 0), CONSTRAINT PK__JBPM4_VA__EE2DF1FD59FA5E80 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_TASK (DBID_ numeric(19, 0) NOT NULL, CLASS_ char(1) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, NAME_ varchar(255), DESCR_ text, STATE_ varchar(255), SUSPHISTSTATE_ varchar(255), ASSIGNEE_ varchar(255), FORM_ varchar(2000), PRIORITY_ numeric(10, 0), CREATE_ datetime, DUEDATE_ datetime, PROGRESS_ numeric(10, 0), SIGNALLING_ numeric(1, 0), EXECUTION_ID_ varchar(255), ACTIVITY_NAME_ varchar(255), HASVARS_ numeric(1, 0), SUPERTASK_ numeric(19, 0), EXECUTION_ numeric(19, 0), PROCINST_ numeric(19, 0), SWIMLANE_ numeric(19, 0), TASKDEFNAME_ varchar(255), CONSTRAINT PK__JBPM4_TASK__571DF1D5 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_SWIMLANE (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, NAME_ varchar(255), ASSIGNEE_ varchar(255), EXECUTION_ numeric(19, 0), CONSTRAINT PK__JBPM4_SW__EE2DF1FD5FB337D6 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_PROPERTY (KEY_ varchar(255) NOT NULL, VERSION_ numeric(10, 0) NOT NULL, VALUE_ varchar(255), CONSTRAINT PK__JBPM4_PR__6AF90CE36383C8BA PRIMARY KEY (KEY_));
CREATE TABLE JBPM4_PARTICIPATION (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, GROUPID_ varchar(255), USERID_ varchar(255), TYPE_ varchar(255), TASK_ numeric(19, 0), SWIMLANE_ numeric(19, 0), CONSTRAINT PK__JBPM4_PA__EE2DF1FD6754599E PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_LOB (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, BLOB_VALUE_ blob, DEPLOYMENT_ numeric(19, 0), NAME_ text, CONSTRAINT PK__JBPM4_LO__EE2DF1FD6B24EA82 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_JOB (DBID_ numeric(19, 0) NOT NULL, CLASS_ varchar(255) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, DUEDATE_ datetime, STATE_ varchar(255), ISEXCLUSIVE_ numeric(1, 0), LOCKOWNER_ varchar(255), LOCKEXPTIME_ datetime, EXCEPTION_ text, RETRIES_ numeric(10, 0), PROCESSINSTANCE_ numeric(19, 0), EXECUTION_ numeric(19, 0), CFG_ numeric(19, 0), SIGNAL_ varchar(255), EVENT_ varchar(255), REPEAT_ varchar(255), CONSTRAINT PK__JBPM4_JO__EE2DF1FD6EF57B66 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_ID_USER (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, ID_ varchar(255), PASSWORD_ varchar(255), GIVENNAME_ varchar(255), FAMILYNAME_ varchar(255), BUSINESSEMAIL_ varchar(255), CONSTRAINT PK__JBPM4_ID__EE2DF1FD72C60C4A PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_ID_MEMBERSHIP (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, USER_ numeric(19, 0), GROUP_ numeric(19, 0), NAME_ varchar(255), CONSTRAINT PK__JBPM4_ID__EE2DF1FD76969D2E PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_ID_GROUP (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, ID_ varchar(255), NAME_ varchar(255), TYPE_ varchar(255), PARENT_ numeric(19, 0), CONSTRAINT PK__JBPM4_ID__EE2DF1FD7A672E12 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_HIST_VAR (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, PROCINSTID_ varchar(255), EXECUTIONID_ varchar(255), VARNAME_ varchar(255), VALUE_ varchar(255), HPROCI_ numeric(19, 0), HTASK_ numeric(19, 0), CONSTRAINT PK__JBPM4_HI__EE2DF1FD7E37BEF6 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_HIST_TASK (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, EXECUTION_ varchar(255), OUTCOME_ varchar(255), ASSIGNEE_ varchar(255), PRIORITY_ numeric(10, 0), STATE_ varchar(255), CREATE_ datetime, END_ datetime, DURATION_ numeric(19, 0), NEXTIDX_ numeric(10, 0), SUPERTASK_ numeric(19, 0), CONSTRAINT PK__JBPM4_HI__EE2DF1FD02084FDA PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_HIST_PROCINST (DBID_ numeric(19, 0) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, ID_ varchar(255), PROCDEFID_ varchar(255), KEY_ varchar(255), START_ datetime, END_ datetime, DURATION_ numeric(19, 0), STATE_ varchar(255), ENDACTIVITY_ varchar(255), NEXTIDX_ numeric(10, 0), CONSTRAINT PK__JBPM4_HI__EE2DF1FD05D8E0BE PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_HIST_DETAIL (DBID_ numeric(19, 0) NOT NULL, CLASS_ varchar(255) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, USERID_ varchar(255), TIME_ datetime, HPROCI_ numeric(19, 0), HPROCIIDX_ numeric(10, 0), HACTI_ numeric(19, 0), HACTIIDX_ numeric(10, 0), HTASK_ numeric(19, 0), HTASKIDX_ numeric(10, 0), HVAR_ numeric(19, 0), HVARIDX_ numeric(10, 0), MESSAGE_ varchar(255), OLD_STR_ varchar(255), NEW_STR_ varchar(255), OLD_INT_ numeric(10, 0), NEW_INT_ numeric(10, 0), OLD_TIME_ datetime, NEW_TIME_ datetime, PARENT_ numeric(19, 0), PARENT_IDX_ numeric(10, 0), CONSTRAINT PK__JBPM4_HI__EE2DF1FD09A971A2 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_HIST_ACTINST (DBID_ numeric(19, 0) NOT NULL, CLASS_ varchar(255) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, HPROCI_ numeric(19, 0), TYPE_ varchar(255), EXECUTION_ varchar(255), ACTIVITY_NAME_ varchar(255), START_ datetime, END_ datetime, DURATION_ numeric(19, 0), TRANSITION_ varchar(255), NEXTIDX_ numeric(10, 0), HTASK_ numeric(19, 0), CONSTRAINT PK__JBPM4_HI__EE2DF1FD0D7A0286 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_EXECUTION (DBID_ numeric(19, 0) NOT NULL, CLASS_ varchar(255) NOT NULL, DBVERSION_ numeric(10, 0) NOT NULL, ACTIVITYNAME_ varchar(255), PROCDEFID_ varchar(255), HASVARS_ numeric(1, 0), NAME_ varchar(255), KEY_ varchar(255), ID_ varchar(255), STATE_ varchar(255), SUSPHISTSTATE_ varchar(255), PRIORITY_ numeric(10, 0), HISACTINST_ numeric(19, 0), PARENT_ numeric(19, 0), INSTANCE_ numeric(19, 0), SUPEREXEC_ numeric(19, 0), SUBPROCINST_ numeric(19, 0), PARENT_IDX_ numeric(10, 0), CONSTRAINT PK__JBPM4_EX__EE2DF1FD114A936A PRIMARY KEY (DBID_), CONSTRAINT UQ__JBPM4_EX__C4971C0E14270015 UNIQUE (ID_));
CREATE TABLE JBPM4_DEPLOYPROP (DBID_ numeric(19, 0) NOT NULL, DEPLOYMENT_ numeric(19, 0), OBJNAME_ varchar(255), KEY_ varchar(255), STRINGVAL_ varchar(255), LONGVAL_ numeric(19, 0), CONSTRAINT PK__JBPM4_DE__EE2DF1FD17F790F9 PRIMARY KEY (DBID_));
CREATE TABLE JBPM4_DEPLOYMENT (DBID_ numeric(19, 0) NOT NULL, NAME_ varchar(255), TIMESTAMP_ numeric(19, 0), STATE_ varchar(255), CONSTRAINT PK__JBPM4_DE__EE2DF1FD1BC821DD PRIMARY KEY (DBID_));
CREATE TABLE DEMO_SINGLE_DATAGRID (sid varchar(36) NOT NULL, name varchar(300), treenode_sid varchar(36), gender varchar(1), age_ int(11), CONSTRAINT PK__DEMO_SIN__DDDFDD36236943A5 PRIMARY KEY (sid));
CREATE TABLE DEMO_LEFT_TREE (sid varchar(36) NOT NULL, name varchar(500), parentsid varchar(36) NOT NULL, CONSTRAINT PK__DEMO_LEF__DDDFDD362739D489 PRIMARY KEY (sid));
CREATE TABLE DEMO_BOOKORDER (sid varchar(36) NOT NULL, bookname varchar(400), creatorsid varchar(36), creatorname varchar(300), createtime datetime, proc_inst_id_ varchar(128), CONSTRAINT PK__DEMO_BOO__DDDFDD367A3223E8 PRIMARY KEY (sid));
CREATE TABLE CMS_CATEGORY (sid varchar(50), category_title varchar(100) NOT NULL, category_status varchar(10), category_site varchar(50), category_order int(11), creator varchar(50), creator_dept varchar(50), create_time datetime);
CREATE TABLE CMS_ARTICLE (sid varchar(100), article_title varchar(4000), article_content text, create_date datetime, modify_date datetime, creator_name varchar(200), article_keywords varchar(200), article_resource varchar(200), article_summary varchar(500), creator_id varchar(100), article_category varchar(100), status varchar(100), article_order int(11), clicktime int(11), article_att varchar(50));


create table ACT_GE_PROPERTY (
    NAME_ varchar(64),
    VALUE_ varchar(200),
    REV_ integer,
    primary key (NAME_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


create table ACT_GE_BYTEARRAY (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    DEPLOYMENT_ID_ varchar(64),
    BYTES_ LONGBLOB,
    GENERATED_ TINYINT,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RE_DEPLOYMENT (
    ID_ varchar(64),
    NAME_ varchar(255),
    DEPLOY_TIME_ timestamp,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_EXECUTION (
    ID_ varchar(64),
    REV_ integer,
    PROC_INST_ID_ varchar(64),
    BUSINESS_KEY_ varchar(255),
    PARENT_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    SUPER_EXEC_ varchar(64),
    ACT_ID_ varchar(255),
    IS_ACTIVE_ TINYINT,
    IS_CONCURRENT_ TINYINT,
    IS_SCOPE_ TINYINT,
    IS_EVENT_SCOPE_ TINYINT,
    SUSPENSION_STATE_ integer,
    primary key (ID_),
    unique ACT_UNIQ_RU_BUS_KEY (PROC_DEF_ID_, BUSINESS_KEY_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
    DUEDATE_ timestamp NULL,
    REPEAT_ varchar(255),
    HANDLER_TYPE_ varchar(255),
    HANDLER_CFG_ varchar(4000),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
    HAS_START_FORM_KEY_ TINYINT,
    SUSPENSION_STATE_ integer,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
    DUE_DATE_ datetime,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_IDENTITYLINK (
    ID_ varchar(64),
    REV_ integer,
    GROUP_ID_ varchar(64),
    TYPE_ varchar(255),
    USER_ID_ varchar(64),
    TASK_ID_ varchar(64),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_VARIABLE (
    ID_ varchar(64) not null,
    REV_ integer,
    TYPE_ varchar(255) not null,
    NAME_ varchar(255) not null,
    EXECUTION_ID_ varchar(64),
	  PROC_INST_ID_ varchar(64),
    TASK_ID_ varchar(64),
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
    START_TIME_ datetime not null,
    END_TIME_ datetime,
    DURATION_ bigint,
    START_USER_ID_ varchar(255),
    START_ACT_ID_ varchar(255),
    END_ACT_ID_ varchar(255),
    SUPER_PROCESS_INSTANCE_ID_ varchar(64),
    DELETE_REASON_ varchar(4000),
    primary key (ID_),
    unique (PROC_INST_ID_),
    unique ACT_UNIQ_HI_BUS_KEY (PROC_DEF_ID_, BUSINESS_KEY_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_ACTINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    EXECUTION_ID_ varchar(64) not null,
    ACT_ID_ varchar(255) not null,
    ACT_NAME_ varchar(255),
    ACT_TYPE_ varchar(255) not null,
    ASSIGNEE_ varchar(64),
    START_TIME_ datetime not null,
    END_TIME_ datetime,
    DURATION_ bigint,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
    START_TIME_ datetime not null,
    END_TIME_ datetime,
    DURATION_ bigint,
    DELETE_REASON_ varchar(4000),
    PRIORITY_ integer,
    DUE_DATE_ datetime,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_DETAIL (
    ID_ varchar(64) not null,
    TYPE_ varchar(255) not null,
    PROC_INST_ID_ varchar(64) not null,
    EXECUTION_ID_ varchar(64) not null,
    TASK_ID_ varchar(64),
    ACT_INST_ID_ varchar(64),
    NAME_ varchar(255) not null,
    VAR_TYPE_ varchar(255),
    REV_ integer,
    TIME_ datetime not null,
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_COMMENT (
    ID_ varchar(64) not null,
    TYPE_ varchar(255),
    TIME_ datetime not null,
    USER_ID_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    ACTION_ varchar(255),
    MESSAGE_ varchar(4000),
    FULL_MSG_ LONGBLOB,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_ID_MEMBERSHIP (
    USER_ID_ varchar(64),
    GROUP_ID_ varchar(64),
    primary key (USER_ID_, GROUP_ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_ID_USER (
    ID_ varchar(64),
    REV_ integer,
    FIRST_ varchar(255),
    LAST_ varchar(255),
    EMAIL_ varchar(255),
    PWD_ varchar(255),
    PICTURE_ID_ varchar(64),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_ID_INFO (
    ID_ varchar(64),
    REV_ integer,
    USER_ID_ varchar(64),
    TYPE_ varchar(64),
    KEY_ varchar(255),
    VALUE_ varchar(255),
    PASSWORD_ LONGBLOB,
    PARENT_ID_ varchar(255),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

CREATE TABLE ACT_AN_AUDIT_HIS (sid varchar(36) NOT NULL, proc_inst_id_ varchar(128), unitname varchar(1000), assignee varchar(128), assigneename varchar(128), result varchar(200), taskname varchar(1000), createtime datetime, comment_ varchar(8000), CONSTRAINT PK__ACT_AN_A__DDDFDD367E02B4CC PRIMARY KEY (sid));

CREATE UNIQUE INDEX idx_forusername ON SYS_USER (username);

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
