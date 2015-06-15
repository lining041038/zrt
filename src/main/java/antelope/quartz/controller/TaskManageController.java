package antelope.quartz.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBHelper;
import antelope.db.DBUtil;
import antelope.db.Sql;
import antelope.db.SqlLoader;
import antelope.quartz.CronExpression;
import antelope.quartz.SchedulerService;
import antelope.quartz.TaskGroup;
import antelope.quartz.TaskScheduleService;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;

 

/**
 * Task 调度管理
 *
 * @author huanggc
 * @since 2012-2-17
 */
@Controller
public class TaskManageController extends BaseController {
	private SchedulerService schedulerService;
	private Scheduler scheduler;
	
	private static final Map<String, TaskScheduleService> taksMap= new HashMap<String, TaskScheduleService>();
	
	@RequestMapping("/system/quartz/getTaskGroupTree")
	 public void getTaskGroupTree(HttpServletRequest req,HttpServletResponse res)throws Exception{
		TaskGroup[]   taskGroups=  TaskGroup.values();
		JSONArray jsonArray= new JSONArray();
		for(TaskGroup task:taskGroups){
			jsonArray.put(new JSONObject().put("name",task.getName()).put("sid",Integer.toString(task.ordinal())));
		}
		getOut(res).print(jsonArray);
	 }
	
	public boolean initSchedulerService() {
		if (schedulerService == null) {
			schedulerService = SpringUtils.getBean(SchedulerService.class);
		}
		
		if (scheduler == null) {
			scheduler = SpringUtils.getBean(Scheduler.class, "quartzScheduler");
		}
		
		return scheduler != null && schedulerService != null;
	}
	
	@RequestMapping("/system/quartz/setTaskService")
	public void setTaskService(HttpServletRequest request, HttpServletResponse res) throws ServletException, Exception {
		String action = request.getParameter("action");

		 if ("query".equals( action)) {
			this.getQrtzTriggers(request, res);
		} else if ("startAllTask".equals(action)) {
			this.startTaskTrigger(res);
		}else if ("clearAllTask".equals(action)) {
			DataSource ds = DBHelper.getDataSource();
			Connection conn = ds.getConnection();
			conn.setAutoCommit(true);
			for (int i = 0; i < 17; ++i) {
			 	Sql sql=SqlLoader.getInstance().getSql("clearQuartzData" + i);
			 	DBUtil.executeUpdate(conn, sql.toString());
			}
			conn.close();
			getOut(res).print(0);
		}else if ("pause".equals(action)) {
			this.pauseTrigger(request, res);
		} else if ("resume".equals(action)) {
			this.resumeTrigger(request, res);
		} else if ("reset".equals(action)) {
			this.resetTrigdger(request, res);
		}
		 
	}
 

	/**
	 * 根据Cron表达式添加Cron Trigger，
	 * 
	 * @param request
	 * @param res
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private void startTaskTrigger(HttpServletResponse res) throws Exception {
		if (!initSchedulerService())
			return;
		 
		
		List<TaskScheduleService> beans = SpringUtils.getBeans(TaskScheduleService.class);
		for (TaskScheduleService taskSchedule:beans) {
			Class<? extends TaskScheduleService> aClass = taskSchedule.getClass();
			Method method = aClass.getMethod("execute",JobExecutionContext.class);
			 
			CronExpression annotation=	method.getAnnotation(CronExpression.class);
			
			//boolean isValidCronExp=org.quartz.CronExpression.isValidExpression(annotation.value());
			/*if((!isValidCronExp)||(taksMap.get(aClass.getName())!=null)){
				log.error("value:< "+aClass.getName() +"with cron "+ annotation.value()+" >is not well form");
				continue;
			}*/
			log.info("value: " + annotation.value());
			    
			    taksMap.put(aClass.getName(),taskSchedule);
			    String jobName=aClass.getSimpleName();
			    JobDetail jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP,aClass);//任务名，任务组，任务执行类
			    jobDetail.getJobDataMap().put("Class",aClass);             
			    CronTrigger  trigger =new CronTrigger(jobName, taskSchedule.getTaskGroupName().name());//触发器名,触发器组
			    trigger.setCronExpression(annotation.value());//触发器时间设定
			    scheduler.addGlobalJobListener(new JobListener() {
					
					@Override
					public void jobWasExecuted(JobExecutionContext context,
							JobExecutionException jobException) {
						try {
							TransactionStatus beginTransaction = SpringUtils.beginTransaction();
							if (jobException != null) {
								DBUtil.executeUpdate("update QRTZ_JOB_DETAILS set DESCRIPTION='执行失败' where job_name='" + context.getJobDetail().getName() + "'");
							} else {
								DBUtil.executeUpdate("update QRTZ_JOB_DETAILS set DESCRIPTION='执行成功' where job_name='" + context.getJobDetail().getName() + "'");
							}
							SpringUtils.commitTransaction(beginTransaction);
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println(context.getJobDetail().getName());
						System.out.println(jobException);
					}
					
					@Override
					public void jobToBeExecuted(JobExecutionContext context) {
						
					}
					
					@Override
					public void jobExecutionVetoed(JobExecutionContext context) {
						
					}
					
					@Override
					public String getName() {
						return "joblistener";
					}
				});
			    scheduler.scheduleJob(jobDetail,trigger);
			    if(!scheduler.isShutdown())
			    	scheduler.start();
		}
		getOut(res).print(0);

	}

 

	/**
	 * 分组取得所有Trigger
	 * 
	 * @param request
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getQrtzTriggers(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
		if (!initSchedulerService())
			return;
		
		String group= request.getParameter("group");
		TaskGroup taskGroup =TaskGroup.values()[Integer.parseInt(group)];
		List<Map<String, Object>> results = this.schedulerService.getQrtzTriggers(taskGroup.name());
		 getOut(res).print(new JSONArray(results)) ;
	}

	/**
	 * 根据名称和组别暂停Tigger
	 * 
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	private void pauseTrigger(HttpServletRequest request, HttpServletResponse res) throws IOException {
		if (!initSchedulerService())
			return;
		
		String triggerName = URLDecoder.decode(request.getParameter("triggerName"), "UTF-8");
		String group = URLDecoder.decode(request.getParameter("group"), "UTF-8");

		schedulerService.pauseTrigger(triggerName, group);
		getOut(res).print(0);
	}

	/**
	 * 根据名称和组别恢复Tigger
	 * 
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	private void resumeTrigger(HttpServletRequest request, HttpServletResponse res) throws IOException {
		if (!initSchedulerService())
			return;
		
		String triggerName = URLDecoder.decode(request.getParameter("triggerName"), "UTF-8");
		String group = URLDecoder.decode(request.getParameter("group"), "UTF-8");

		schedulerService.resumeTrigger(triggerName, group);
		getOut(res).print(0);
	}

	/**
	 * 根据名称和组别重设Tigger的Cron
	 * 
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	private void resetTrigdger(HttpServletRequest request, HttpServletResponse res) throws Exception {
		
		if (!initSchedulerService())
			return;
		
		String triggerName = URLDecoder.decode(request.getParameter("triggerName"), "UTF-8");
		String group = URLDecoder.decode(request.getParameter("group"), "UTF-8");
		String cronExpression = request.getParameter("cronExpression");
		String jobDetailName = request.getParameter("jobDetailName");
		boolean isValidCronExp=org.quartz.CronExpression.isValidExpression(cronExpression); 
		if(isValidCronExp){
			CronTrigger cronTrigger = new CronTrigger(triggerName, group, jobDetailName,Scheduler.DEFAULT_GROUP);
			cronTrigger.setCronExpression(cronExpression);
			schedulerService.pauseTrigger(triggerName, group);
			scheduler.rescheduleJob(cronTrigger.getName(), cronTrigger.getGroup(), cronTrigger);
			getOut(res).print(0);
		}else {
			getOut(res).print(1);
		}
		
	   
		 
	}
	 

}
