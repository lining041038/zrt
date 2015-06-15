package antelope.services;

import java.util.HashMap;
import java.util.Map;

import antelope.utils.I18n;


public class ProcessParams {
	
	/**
	 * 若为提交任务则传递taskid
	 */
	public String taskid;
	
	/**
	 * 若为新启动流程则将传递流程key
	 */
	public String processdefinitionkey;
	
	/**
	 * 完成任务时的任务评价（一般为审批意见）
	 */
	public String comment;
	
	/**
	 * 完成任务时的分支条件参数（一般为审批结果）
	 */
	public String result;
	
	/**
	 * 任务定义key（即流程图中任务的id)
	 */
	public String task_def_key_;
	
	/**
	 * 当前任务所在流程（或子流程的）执行id
	 */
	public String execution_id_;
	
	/**
	 * 任务受让人（一般为当前提交审批的审批人）
	 */
	public String assignee;

	/**
	 * 流程相关信息需要国际化
	 */
	public I18n i18n;
	
	/**
	 * 是否为暂存，1为是，其他的值均为否
	 */
	public String tempsave;
	
	/**
	 * 存用户名username，将要发送的所有抄送待阅人员
	 */
	public String[] copytousers = new String[0];
	
	/**
	 * 全局变量（一般为下级审批人）
	 * 此map的key前台以var_为前缀传递，对应流程全局变量名称为后缀
	 * 如var_mytask 其中mytask为流程全局变量
	 */
	public Map<String, Object> variables = new HashMap<String, Object>();
	
}
