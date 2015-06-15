/**
 * 用户表添加性别字段
 * 0 为男 1为女
 */

alter table DEMO_SINGLE_DATAGRID add gender varchar2(1) NULL;


/**
 * 局域网内部IP组播地址与端口
 */
insert into SYS_OPTS(sid,name,key_,value) values('antelope_ipmulticastaddr', '局域网内部IP组播地址与端口', 'antelope_ipmulticastaddr', '224.0.0.254:30000');

/**
 * 视频流发布用授权密码
 */
insert into SYS_OPTS(sid,name,key_,value) values('antelope_fmspublishpassword', '视频流发布用授权密码', 'antelope_fmspublishpassword', 'password');


/**
 * 添加年龄字段，以演示自定义
 */
alter table DEMO_SINGLE_DATAGRID add age_ number;
