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
	content varchar(max) NULL, 
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
	content varchar(max) NULL, 
	catesid varchar(36) NOT NULL, 
	createtime datetime NULL, 
	PRIMARY KEY (sid)
);