
-- id=querysitepublish desc=查询各站点发布状态 ---
select * from (select t.*, max(t2.createtime) createtime
  from WCM_SITE t
  left join WCM_SITE_PUBLISH_HIS t2
    on t.sid = t2.sitesid group by t.sid, t.name) m where 1=1 ;