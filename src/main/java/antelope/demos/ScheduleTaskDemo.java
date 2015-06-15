package antelope.demos;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import antelope.quartz.CronExpression;
import antelope.quartz.TaskGroup;
import antelope.quartz.TaskScheduleService;
import antelope.springmvc.JPABaseDao;
import antelope.springmvc.SpringUtils;

//@Component
public class ScheduleTaskDemo implements TaskScheduleService {

	@Override
	@CronExpression("0/5 * * ? * *")
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("计划任务demo执行完毕");
		JPABaseDao dao = SpringUtils.getBean(JPABaseDao.class);
	}

	@Override
	public TaskGroup getTaskGroupName() {
		return TaskGroup.DEFAULT;
	}

}
