/**
 * 用户表添加性别字段
 * 0 为男 1为女
 */
alter table ACT_AN_AUDIT_HIS drop column comment;

alter table ACT_AN_AUDIT_HIS add comment_ varchar(8000) NULL;