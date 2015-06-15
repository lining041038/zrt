CREATE TABLE changelog (
	change_number  BIGSERIAL NOT NULL, 
	complete_dt timestamp(7) NOT NULL, 
	applied_by varchar(100) NOT NULL, 
	description varchar(500) NOT NULL, 
	CONSTRAINT Pkchangelog 
	PRIMARY KEY (change_number)
);
