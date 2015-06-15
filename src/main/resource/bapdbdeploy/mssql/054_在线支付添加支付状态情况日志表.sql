
CREATE TABLE SYS_ORDER_ONLINEPAY_LOG (
	sid varchar(36) NOT NULL, 
	ordersid varchar(200) NULL, 
	orderstatus varchar(1) NULL, 
	PRIMARY KEY (sid)
);