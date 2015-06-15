


CREATE TABLE SYS_LOG (
	sid varchar(36) NOT NULL, 
	loglevel varchar(100) NULL, 
	message varchar(1000) NULL, 
	logclass varchar(500) NULL, 
	createtime datetime NULL, 
	PRIMARY KEY (sid)
);


alter table DEMO_SINGLE_DATAGRID add sortfield varchar(36);


alter table DEMO_LEFT_TREE add sortfield varchar(36);