/**
 * 流程引擎演示程序用表 买书表
 */
CREATE TABLE DEMO_BOOKORDER (
	sid varchar(36) NOT NULL, 
	bookname varchar(400) NULL, 
	creatorsid varchar(36) NULL, 
	creatorname varchar(300) NULL, 
	createtime datetime NULL, 
	proc_inst_id_ varchar(128) NULL, 
	PRIMARY KEY (sid)
);

