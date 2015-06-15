if not exists (select * from sysobjects where id = object_id('changelog'))
begin CREATE TABLE changelog (
		  change_number BIGINT NOT NULL,
		  complete_dt DATETIME NOT NULL,
		  applied_by VARCHAR(100) NOT NULL,
		  description VARCHAR(500) NOT NULL
 )
ALTER TABLE changelog ADD CONSTRAINT Pkchangelog PRIMARY KEY (change_number)
end
