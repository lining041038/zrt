package antelope.quartz;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import antelope.springmvc.SpringUtils;


@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {
	private Scheduler scheduler;
    
    @Resource(name="quartzDao")
	private QuartzDao quartzDao;
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerServiceImpl.class);

	 
	public boolean initScheduler() {
		if (scheduler == null) {
			scheduler = SpringUtils.getBean(Scheduler.class, "quartzScheduler");
		}
		
		return scheduler != null;
	}

	public void setScheduler( Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Autowired	
	public void setQuartzDao(@Qualifier("quartzDao")QuartzDao quartzDao) {
		this.quartzDao = quartzDao;
	}

	
	@Override
	public void schedule(JobDetail jobDetail,String cronExpression) {
		schedule(  jobDetail,"", cronExpression);
	}

	@Override
	public void schedule(JobDetail jobDetail,String name, String cronExpression) {
		schedule( jobDetail, name,  cronExpression,Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,String name, String cronExpression,String group) {
		try {
			schedule(  jobDetail,name, new CronExpression(cronExpression),group);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void schedule(JobDetail jobDetail,CronExpression cronExpression) {
		schedule(  jobDetail,null, cronExpression);
	}

	@Override
	public void schedule(JobDetail jobDetail,String name, CronExpression cronExpression) {
		schedule(   jobDetail,name,  cronExpression,Scheduler.DEFAULT_GROUP) ;
	}
	
	@Override
	public void schedule(JobDetail jobDetail,String name, CronExpression cronExpression,String group) {
		if (!initScheduler())
			return;
		
		
		if (name == null || name.trim().equals("")) {
			name = UUID.randomUUID().toString();
		}else{
			//在名称后添加UUID，保证名称的唯一性
			name +="&"+UUID.randomUUID().toString();
		}

		try {
			scheduler.addJob(jobDetail, true);

			CronTrigger cronTrigger = new CronTrigger(name, group, jobDetail.getName(),
					Scheduler.DEFAULT_GROUP);
			cronTrigger.setCronExpression(cronExpression);
			scheduler.scheduleJob(cronTrigger);
			scheduler.rescheduleJob(cronTrigger.getName(), cronTrigger.getGroup(), cronTrigger);
			
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void schedule(JobDetail jobDetail,Date startTime) {
		schedule( jobDetail,startTime, Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,Date startTime,String group) {
		schedule( jobDetail,startTime, null,group);
	}

	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime) {
		schedule( jobDetail,name, startTime,Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime,String group) {
		schedule( jobDetail,name, startTime, null,group);
	}

	@Override
	public void schedule(JobDetail jobDetail,Date startTime, Date endTime) {
		schedule( jobDetail,startTime, endTime, Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,Date startTime, Date endTime,String group) {
		schedule(  jobDetail,startTime, endTime, 0,group);
	}

	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime, Date endTime) {
		schedule(  jobDetail,name, startTime, endTime,Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime, Date endTime,String group) {
		schedule( jobDetail,name, startTime, endTime, 0,group);
	}

	@Override
	public void schedule(JobDetail jobDetail,Date startTime, Date endTime, int repeatCount) {
		schedule(   jobDetail,startTime, endTime, 0,Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,Date startTime, Date endTime, int repeatCount,String group) {
		schedule(  jobDetail,null, startTime, endTime, 0,group);
	}

	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime, Date endTime, int repeatCount) {
		schedule(  jobDetail,name, startTime, endTime, 0, Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime, Date endTime, int repeatCount,String group) {
		schedule(  jobDetail,name, startTime, endTime, 0, 1L,group);
	}

	@Override
	public void schedule(JobDetail jobDetail,Date startTime, Date endTime, int repeatCount, long repeatInterval) {
		schedule(  jobDetail,startTime, endTime, repeatCount, repeatInterval,Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,Date startTime, Date endTime, int repeatCount, long repeatInterval,String group) {
		schedule(  jobDetail,null, startTime, endTime, repeatCount, repeatInterval,group);
	}

	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime, Date endTime, int repeatCount, long repeatInterval) {
		this.schedule(jobDetail, name , startTime,  endTime,  repeatCount,  repeatInterval,  Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void schedule(JobDetail jobDetail,String name, Date startTime, Date endTime, int repeatCount, long repeatInterval,String group ) {
		
		if (!initScheduler())
			return;
		
		if (name == null || name.trim().equals("")) {
			name = UUID.randomUUID().toString();
		}else{
			//在名称后添加UUID，保证名称的唯一性
			name +="&"+UUID.randomUUID().toString();
		}

		try {
			scheduler.addJob(jobDetail, true);

			SimpleTrigger SimpleTrigger = new SimpleTrigger(name, group, jobDetail.getName(),
					Scheduler.DEFAULT_GROUP, startTime, endTime, repeatCount, repeatInterval);
			scheduler.scheduleJob(SimpleTrigger);
			scheduler.rescheduleJob(SimpleTrigger.getName(), SimpleTrigger.getGroup(), SimpleTrigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void schedule(JobDetail jobDetail,Map<String,Object> map) {
		
		if (!initScheduler())
			return;
		
		String temp = null;
		//实例化SimpleTrigger
		SimpleTrigger SimpleTrigger = new SimpleTrigger();
		
		//这些值的设置也可以从外面传入，这里采用默放值		
		SimpleTrigger.setJobName(jobDetail.getName());		
		SimpleTrigger.setJobGroup(Scheduler.DEFAULT_GROUP);		
		SimpleTrigger.setRepeatInterval(1000L);
		
		//设置名称
		temp =(String) map.get(Constant.TRIGGERNAME);		
		if (StringUtils.isEmpty(StringUtils.trim(temp)) ){
			temp = UUID.randomUUID().toString();
		}else{
			//在名称后添加UUID，保证名称的唯一性
			temp +="&"+UUID.randomUUID().toString();
		}
		SimpleTrigger.setName(temp);
		
		//设置Trigger分组
		temp = (String)map.get(Constant.TRIGGERGROUP);
		if(StringUtils.isEmpty(temp)){
			temp = Scheduler.DEFAULT_GROUP;
		}
		SimpleTrigger.setGroup(temp);
		
		//设置开始时间
		temp =(String) map.get(Constant.STARTTIME);
		if(StringUtils.isNotEmpty(temp)){
			SimpleTrigger.setStartTime(this.parseDate(temp));
		}
		
		//设置结束时间
		temp =(String) map.get(Constant.ENDTIME);
		if(StringUtils.isNotEmpty(temp)){
			SimpleTrigger.setEndTime(this.parseDate(temp));
		}
		
		//设置执行次数
		temp =(String) map.get(Constant.REPEATCOUNT);
		if(StringUtils.isNotEmpty(temp) && NumberUtils.toInt(temp) > 0){
			SimpleTrigger.setRepeatCount(NumberUtils.toInt(temp));
		}
		
		//设置执行时间间隔
		temp = (String)map.get(Constant.REPEATINTERVEL);
		if(StringUtils.isNotEmpty(temp) && NumberUtils.toLong(temp) > 0){
			SimpleTrigger.setRepeatInterval(NumberUtils.toLong(temp)*1000);
		}

		try {
			scheduler.addJob(jobDetail, true);
		
			scheduler.scheduleJob(SimpleTrigger);
			scheduler.rescheduleJob(SimpleTrigger.getName(), SimpleTrigger.getGroup(), SimpleTrigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<Map<String, Object>> getQrtzTriggers(String jobGroup){
		return quartzDao.getQrtzTriggers(jobGroup);
	}
	
	@Override
	public void pauseTrigger(String triggerName,String group){
		
		if (!initScheduler())
			return;
		
		try {
			scheduler.pauseTrigger(triggerName, group);//停止触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void resumeTrigger(String triggerName,String group){
		
		if (!initScheduler())
			return;
		
		try {
			//Trigger trigger = scheduler.getTrigger(triggerName, group);
			
			scheduler.resumeTrigger(triggerName, group);//重启触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean removeTrigdger(String triggerName,String group){
		
		if (!initScheduler())
			return false;
		
		try {
			scheduler.pauseTrigger(triggerName, group);//停止触发器
			return scheduler.unscheduleJob(triggerName, group);//移除触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	 
	private Date parseDate(String time){
		try {
			return DateUtils.parseDate(time, new String[]{"yyyy-MM-dd HH:mm"});
		} catch (ParseException e) {			
			logger.error("日期格式错误{}，正确格式为：yyyy-MM-dd HH:mm",time);
			throw new RuntimeException(e);
		}
	}
}
