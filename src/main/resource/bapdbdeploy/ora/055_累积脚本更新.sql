update SYS_USER set name='超级管理员' where sid='1234';

update SYS_ROLE set name='超级管理员' where sid='153';


CREATE TABLE SYS_FORMULA_LATEX(
	sid varchar2(36) NOT NULL, 
	latex varchar2(1000) NULL, PRIMARY KEY (sid)
);



CREATE TABLE WCM_SITE_TEMPLATE_SETTING (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	settingcode varchar2(100) NULL, 
	settingvalue varchar2(2000) NULL, 
	sitesid varchar2(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_SITE_TEMPLATE (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	tmplpath varchar2(500) NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_ANNOUNCEMENT (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	content varchar2(1000) NULL, 
	creatorsid varchar2(36) NULL, 
	creatorname varchar2(200) NULL, 
	createtime timestamp NULL, 
	sitesid varchar2(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_PRODUCT (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	content varchar2(4000) NULL, 
	catesid varchar2(36) NOT NULL, 
	createtime timestamp NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_PEOPLE_MSG (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	content varchar2(1000) NULL, 
	creatorsid varchar2(36) NULL, 
	creatorname varchar2(200) NULL, 
	createtime timestamp NULL, 
	sitesid varchar2(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_PRODUCT_CATE (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	parentsid varchar2(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_ARTICLE_CATE (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	parentsid varchar2(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_ARTICLE (
	sid varchar2(36) NOT NULL, 
	name varchar2(500) NULL, 
	content varchar2(4000) NULL, 
	catesid varchar2(36) NOT NULL, 
	createtime timestamp NULL, 
	PRIMARY KEY (sid)
);


alter table WCM_ARTICLE add authorname varchar2(200);


alter table WCM_PRODUCT add productimgsid varchar2(200);



alter table WCM_ANNOUNCEMENT drop column sitesid;


alter table WCM_SITE add templatesid varchar2(36);
alter table WCM_SITE add templatename varchar2(200);


alter table WCM_SITE_TEMPLATE_SETTING modify  settingvalue varchar2(4000);


alter table WCM_ARTICLE add digest varchar2(1000);

alter table WCM_SITE add activated varchar2(1);


alter table WCM_PEOPLE_MSG drop column creatorsid;
alter table WCM_PEOPLE_MSG drop column creatorname;
alter table WCM_PEOPLE_MSG add email varchar2(500);
alter table WCM_PEOPLE_MSG add phonenum varchar2(20);

alter table WCM_PAGE drop column templatesid;
alter table WCM_PAGE drop column templatename;
alter table WCM_PAGE drop column pagetype;
alter table WCM_PAGE add content varchar2(4000);



alter table WCM_SITE add domainname varchar2(200);



CREATE TABLE DEMO_MY_ORDER (
	sid varchar2(36) NOT NULL, 
	name varchar2(300) NULL, 
	paycomplete char(1) NULL, 
	orderprice int NULL, 
	PRIMARY KEY (sid)
);



alter table DEMO_MY_ORDER modify orderprice numeric(10, 5);


CREATE TABLE SYS_ORDER_ONLINEPAY_LOG (
	sid varchar2(36) NOT NULL, 
	ordersid varchar2(200) NULL, 
	orderstatus varchar2(1) NULL, 
	PRIMARY KEY (sid)
);


alter table WCM_ARTICLE add source varchar2(200);

alter table WCM_PEOPLE_MSG add username varchar2(500);
alter table WCM_PEOPLE_MSG add identityCard varchar2(500);
alter table WCM_PEOPLE_MSG add topic varchar2(500);
alter table WCM_PEOPLE_MSG add profession varchar2(500);
alter table WCM_PEOPLE_MSG add address varchar2(1000);
alter table WCM_PEOPLE_MSG add title varchar2(500);
