alter table WCM_PAGE drop column templatesid;
alter table WCM_PAGE drop column templatename;
alter table WCM_PAGE drop column pagetype;
alter table WCM_PAGE add content varchar(max);
