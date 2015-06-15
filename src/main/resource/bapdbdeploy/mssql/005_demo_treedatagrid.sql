/**
 * 左树右列表统一界面控件演示树数据表
 */
CREATE TABLE DEMO_LEFT_TREE (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	parentsid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);

alter table DEMO_SINGLE_DATAGRID add treenode_sid varchar(36);