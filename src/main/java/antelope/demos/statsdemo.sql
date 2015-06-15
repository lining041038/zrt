
-- id=statsuserrole desc=按角色统计用户数量 ---
select COUNT(t.sid) ct, sum(cast (gender as int)) genderct, t3.name rolename
  from SYS_USER t
 inner join SYS_USER_ROLE_RELATE t2
    on t.sid = t2.usersid
 inner join SYS_ROLE t3
    on t2.rolesid = t3.sid
 group by t3.sid, t3.name order by t3.sid;
 
 -- id=statsuserorg desc=按组织机构统计用户数量 ---
select 1 ct, t.name rolename
  from DEMO_SINGLE_DATAGRID t where t.name is not null and t.name != '';
 
 -- id=statsusergender desc=按性别统计用户数量 ---
select COUNT(t.sid) ct, t.gender gender
  from SYS_USER t group by gender;

  
 -- id=statsfilm desc=demos表，按照动漫名称做统计 ---
select COUNT(t.sid) ct, t.name filename
  from DEMO_SINGLE_DATAGRID t where t.name is not null group by name;
 
 
 