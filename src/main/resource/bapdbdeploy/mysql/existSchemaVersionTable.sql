select count(*) ct from information_schema.tables where lower(table_name)='bapchangelog' and lower(table_schema)=?;