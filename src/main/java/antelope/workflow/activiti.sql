-- id=activiti_querytodotasks desc=查询待办 ---
select t.ID_,
       t.REV_,
       t.EXECUTION_ID_,
       t.PROC_DEF_ID_,
       t.NAME_,
       t.PARENT_TASK_ID_,
       t.DESCRIPTION_,
       t.TASK_DEF_KEY_,
       t.OWNER_,
       t.ASSIGNEE_,
       t.DELEGATION_,
       t.PRIORITY_,
       t.CREATE_TIME_,
       t.DUE_DATE_,
       t2.*
  from (select t3.*
          from ACT_RU_TASK t3
         inner join ACT_RU_IDENTITYLINK t4
            on t3.ID_ = t4.TASK_ID_
         where t4.USER_ID_ = ?
           and t3.PROC_DEF_ID_ in
               (select ID_ from ACT_RE_PROCDEF where KEY_ = ?)) t
 inner join(${busitable}) t2
    on t.PROC_INST_ID_ = t2.PROC_INST_ID_;

-- id=activiti_querydonetasks desc=查询已办 ---
select t.ID_,
       t.PROC_DEF_ID_,
       t.TASK_DEF_KEY_,
       t.EXECUTION_ID_,
       t.NAME_,
       t.PARENT_TASK_ID_,
       t.DESCRIPTION_,
       t.OWNER_,
       t.ASSIGNEE_,
       t.START_TIME_,
       t.END_TIME_,
       t.DURATION_,
       t.DELETE_REASON_,
       t.PRIORITY_,
       t.DUE_DATE_,
       t2.*
  from (select *
          from ACT_HI_TASKINST t3
         where t3.ASSIGNEE_ = ?
           and t3.DELETE_REASON_ = 'completed'
           and t3.PROC_DEF_ID_ in
               (select ID_ from ACT_RE_PROCDEF where KEY_ = ?)) t
 inner join(${busitable}) t2
    on t.PROC_INST_ID_ = t2.PROC_INST_ID_;
   
-- id=activiti_querytoviewtasks desc=查询待阅 ---
select t.ID_,
       t.REV_,
       t.EXECUTION_ID_,
       t.PROC_DEF_ID_,
       t.NAME_,
       t.PARENT_TASK_ID_,
       t.DESCRIPTION_,
       t.TASK_DEF_KEY_,
       t.OWNER_,
       t.ASSIGNEE_,
       t.DELEGATION_,
       t.PRIORITY_,
       t.CREATE_TIME_,
       t.DUE_DATE_,
       t2.*
  from (select t3.*
          from ACT_RU_TASK t3
         inner join ACT_RU_IDENTITYLINK t4
            on t3.ID_ = t4.TASK_ID_
         where t4.USER_ID_ = ?
           and t3.PROC_INST_ID_ in
               (select PROC_INST_ID_
                  from ACT_RU_EXECUTION
                 where BUSINESS_KEY_ = ?)
           and t3.PROC_DEF_ID_ in
               (select ID_ from ACT_RE_PROCDEF where KEY_ = ?)) t
 inner join(${busitable}) t2
    on t.DESCRIPTION_ = t2.PROC_INST_ID_;

-- id=activiti_queryviewedtasks desc=查询已阅 ---
select t.ID_,
       t.PROC_DEF_ID_,
       t.TASK_DEF_KEY_,
       t.EXECUTION_ID_,
       t.NAME_,
       t.PARENT_TASK_ID_,
       t.DESCRIPTION_,
       t.OWNER_,
       t.ASSIGNEE_,
       t.START_TIME_,
       t.END_TIME_,
       t.DURATION_,
       t.DELETE_REASON_,
       t.PRIORITY_,
       t.DUE_DATE_,
       t2.*
  from (select *
          from ACT_HI_TASKINST t3
         where t3.ASSIGNEE_ = ?
           and t3.DELETE_REASON_ = 'completed'
           and t3.PROC_INST_ID_ in
               (select PROC_INST_ID_
                  from ACT_HI_PROCINST
                 where BUSINESS_KEY_ = ?)
           and t3.PROC_DEF_ID_ in
               (select ID_ from ACT_RE_PROCDEF where KEY_ = ?)) t
 inner join(${busitable}) t2
    on t.DESCRIPTION_ = t2.PROC_INST_ID_;
