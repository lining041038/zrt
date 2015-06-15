/**
 * 角色表添加标识字段
 * 为1时，表明为自注册用户角色
 */
alter table SYS_ROLE add flag varchar(1);