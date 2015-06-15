/**
 * 单列表统一界面控件演示数据表
 */
CREATE TABLE DEMO_SINGLE_DATAGRID (
	sid varchar(36) NOT NULL, 
	name varchar(300) NULL, PRIMARY KEY (sid)
);

insert into DEMO_SINGLE_DATAGRID values('1234', '测试记录');