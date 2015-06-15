/**
 * 局域网内部IP组播地址与端口更改
 */
update SYS_OPTS set value='224.0.1.2:30000' where sid='antelope_ipmulticastaddr';
