
CREATE TABLE WCM_SITE_TEMPLATE_SETTING (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	settingcode varchar(100) NULL, 
	settingvalue varchar(2000) NULL, 
	sitesid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_SITE_TEMPLATE (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	tmplpath varchar(500) NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_ANNOUNCEMENT (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	content varchar(1000) NULL, 
	creatorsid varchar(36) NULL, 
	creatorname varchar(200) NULL, 
	createtime datetime NULL, 
	sitesid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_PRODUCT (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	content varchar(8000) NULL, 
	catesid varchar(36) NOT NULL, 
	createtime datetime NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_PEOPLE_MSG (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	content varchar(1000) NULL, 
	creatorsid varchar(36) NULL, 
	creatorname varchar(200) NULL, 
	createtime datetime NULL, 
	sitesid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_PRODUCT_CATE (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	parentsid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_ARTICLE_CATE (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	parentsid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);
CREATE TABLE WCM_ARTICLE (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	content varchar(8000) NULL, 
	catesid varchar(36) NOT NULL, 
	createtime datetime NULL, 
	PRIMARY KEY (sid)
);


alter table WCM_ARTICLE add authorname varchar(200);


alter table WCM_PRODUCT add productimgsid varchar(200);



alter table WCM_ANNOUNCEMENT drop column sitesid;


alter table WCM_SITE add templatesid varchar(36);
alter table WCM_SITE add templatename varchar(200);


alter table WCM_SITE_TEMPLATE_SETTING modify  settingvalue varchar(8000);


alter table WCM_ARTICLE add digest varchar(1000);

alter table WCM_SITE add activated varchar(1);


alter table WCM_PEOPLE_MSG drop column creatorsid;
alter table WCM_PEOPLE_MSG drop column creatorname;
alter table WCM_PEOPLE_MSG add email varchar(500);
alter table WCM_PEOPLE_MSG add phonenum varchar(20);

alter table WCM_PAGE drop column templatesid;
alter table WCM_PAGE drop column templatename;
alter table WCM_PAGE drop column pagetype;
alter table WCM_PAGE add content varchar(8000);



alter table WCM_SITE add domainname varchar(200);



CREATE TABLE DEMO_MY_ORDER (
	sid varchar(36) NOT NULL, 
	name varchar(300) NULL, 
	paycomplete char(1) NULL, 
	orderprice int NULL, 
	PRIMARY KEY (sid)
);



alter table DEMO_MY_ORDER modify orderprice numeric(10, 5);


CREATE TABLE SYS_ORDER_ONLINEPAY_LOG (
	sid varchar(36) NOT NULL, 
	ordersid varchar(200) NULL, 
	orderstatus varchar(1) NULL, 
	PRIMARY KEY (sid)
);


alter table WCM_ARTICLE add source varchar(200);

alter table WCM_PEOPLE_MSG add username varchar(500);
alter table WCM_PEOPLE_MSG add identityCard varchar(500);
alter table WCM_PEOPLE_MSG add topic varchar(500);
alter table WCM_PEOPLE_MSG add profession varchar(500);
alter table WCM_PEOPLE_MSG add address varchar(1000);
alter table WCM_PEOPLE_MSG add title varchar(500);
