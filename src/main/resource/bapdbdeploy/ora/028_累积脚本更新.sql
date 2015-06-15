
alter table DEMO_SINGLE_DATAGRID add formkey varchar2(300);



insert into DEMO_SINGLE_DATAGRID(sid, name, treenode_sid, gender, age_, formkey) values('2345', '测试formkeyfield', null, null, null, '/demos/multiple_datagridsdemo_formkeyform.jsp');



alter table SYS_OPTS add formkey varchar2(300);