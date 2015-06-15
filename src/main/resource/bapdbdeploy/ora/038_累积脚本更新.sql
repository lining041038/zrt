


CREATE TABLE SYS_LOG (
	sid varchar2(36) NOT NULL, 
	loglevel varchar2(100) NULL, 
	message varchar2(1000) NULL, 
	logclass varchar2(500) NULL, 
	createtime timestamp NULL, 
	PRIMARY KEY (sid)
);



alter table DEMO_SINGLE_DATAGRID add sortfield varchar2(36);



alter table DEMO_LEFT_TREE add sortfield varchar2(36);