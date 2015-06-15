alter table WCM_PEOPLE_MSG drop column creatorsid;
alter table WCM_PEOPLE_MSG drop column creatorname;
alter table WCM_PEOPLE_MSG add email varchar(500);
alter table WCM_PEOPLE_MSG add phonenum varchar(20);