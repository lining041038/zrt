/**
 * 添加activiti扩展审批历史表ACT_AN_AUDIT_HIS
 */
CREATE TABLE ACT_AN_AUDIT_HIS (
	sid varchar(36) NOT NULL, 
	proc_inst_id_ varchar(128) NULL, 
	unitname varchar(1000) NULL, 
	assignee varchar(128) NULL, 
	assigneename varchar(128) NULL, 
	result varchar(200) NULL, 
	taskname varchar(1000) NULL, 
	comment varchar(8000) NULL, 
	createtime datetime NULL, PRIMARY KEY (sid)
);
