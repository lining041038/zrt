package antelope.quartz;

import org.quartz.Job;


/**
 *
 * @author huanggc
 * @since 2012-2-17
 */
public interface TaskScheduleService extends  Job 
{
 
	/**
	 * 所属分组名称
	 * @return
	 */
	public TaskGroup getTaskGroupName();
}
