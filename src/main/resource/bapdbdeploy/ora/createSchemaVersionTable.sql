declare  
    i integer;
begin
    select count(1) into i from user_tables where table_name=upper('bapchangelog') ;
    if i = 0 
    then
      execute immediate
      'CREATE TABLE bapchangelog (
        change_number NUMBER(22,0) NOT NULL,
        complete_dt TIMESTAMP NOT NULL,
        applied_by VARCHAR2(100) NOT NULL,
        description VARCHAR2(500) NOT NULL
      )';
      execute immediate  'ALTER TABLE bapchangelog ADD CONSTRAINT Pkbapchangelog PRIMARY KEY (change_number)';
     end if;
end;