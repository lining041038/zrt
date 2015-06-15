/**
 * 添加密保问题及密保问题答案列
 */
alter table SYS_USER add pwdquestion varchar(1000);
alter table SYS_USER add pwdanswer varchar(1000);