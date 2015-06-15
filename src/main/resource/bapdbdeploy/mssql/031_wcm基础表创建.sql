

CREATE TABLE WCM_PAGE (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	templatesid varchar(36) NULL, 
	templatename varchar(200) NULL, 
	sitesid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);

CREATE TABLE WCM_CONTAINER_DATA (
	sid varchar(36) NOT NULL, 
	assetsid varchar(36) NOT NULL, 
	pagesid varchar(36) NOT NULL, 
	containersid varchar(36) NULL, 
	PRIMARY KEY (sid)
);

CREATE TABLE WCM_ASSET (
	sid varchar(36) NOT NULL, 
	name varchar(200) NULL, 
	assetdata image NULL, 
	assettype varchar(1) NULL, 
	PRIMARY KEY (sid)
);

CREATE TABLE WCM_SITE (
	sid varchar(36) NOT NULL, 
	name varchar(200) NULL, 
	PRIMARY KEY (sid)
);
