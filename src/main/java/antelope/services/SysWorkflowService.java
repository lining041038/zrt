package antelope.services;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import antelope.db.DBUtil;
import antelope.db.Sql;
import antelope.db.SqlLoader;
import antelope.entities.AuditHistory;
import antelope.entities.SysUnit;
import antelope.entities.SysUser;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.WorkflowDatagridsOptions;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SpringUtils;
import antelope.utils.I18n;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageParams;
import antelope.utils.SpeedIDUtil;
import antelope.utils.UserRoleOrgUtil;
import antelope.workflow.WorkflowExtendedService;

@Service
public class SysWorkflowService extends BaseComponent{
	
	private RepositoryService repositoryService;
	private RuntimeService runtimeService;
	private TaskService taskService;
	private HistoryService historyService;
	private ManagementService managementService;
	private IdentityService identityService;
	private FormService formService;
	private WorkflowExtendedService workflowExtendedService;
	
	public void checkServicesBind() {
		if (repositoryService == null) {
			repositoryService = SpringUtils.getBean(RepositoryService.class);
			runtimeService = SpringUtils.getBean(RuntimeService.class);
			taskService = SpringUtils.getBean(TaskService.class);
			historyService = SpringUtils.getBean(HistoryService.class);
			managementService = SpringUtils.getBean(ManagementService.class);
			identityService = SpringUtils.getBean(IdentityService.class);
			formService = SpringUtils.getBean(FormService.class);
			workflowExtendedService = SpringUtils.getBean(WorkflowExtendedService.class);
		}
	}
	
	/**
	 * 获取开始节点对应formKey表单key
	 * @param key
	 * @return
	 */
	public String getStartFormKeyByProcessDefinitionKey(String key) {
		checkServicesBind();
		return formService.getStartFormData(repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult().getId()).getFormKey();
	}

	/**
	 * 根据任务id获取对应的表单key
	 * @param taskid
	 * @return
	 */
	public String getTaskFormKeyByTaskId(String taskid) {
		checkServicesBind();
		return formService.getTaskFormData(taskid).getFormKey();
	}
	
	/**																																																																																																																																		
	 * 使用前台传入的ProcessParams流程参数启动一个流程
	 * @param params 从应用提交的上下文获取的流程参数
	 * @param entity 需要与流程进行关联的实体变量
	 */
	public <A> TaskCompleteResult startProcessInstance(ProcessParams params, A entity) throws Exception {
		checkServicesBind();
		
		
		Field sidfield = entity.getClass().getField("sid");
		if (sidfield != null) {
			sidfield.setAccessible(true);
			String business_sid = (String) sidfield.get(entity);
			// 暂存不提交
			workflowExtendedService.removePreStartProcessVariables(business_sid);
			if ("1".equals(params.tempsave)) { // 暂存时，存储流程启动前变量
				workflowExtendedService.setPreStartProcessVariables(business_sid, params.variables);
				return null;
			}
		}
		
		params.variables.put("result", params.result);
		
		ProcessInstance procinsts = runtimeService.startProcessInstanceByKey(params.processdefinitionkey, params.variables);
		String proc_inst_id_ = procinsts.getProcessInstanceId();
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(proc_inst_id_).list();
		
		// 记录流程启动时流程历史
		AuditHistory his = new AuditHistory();
		his.assignee = params.assignee;
		List<SysUser> users = dao.getBy("username", params.assignee, SysUser.class);
		if (users.size() > 0) {
			his.assigneename = users.get(0).name;
			SysUnit units = UserRoleOrgUtil.getUnitByUsername(params.assignee);
			if (units == null)
				his.unitname = params.i18n.get("antelope.orgnizationroot");
			else 
				his.unitname = units.name; 
		}
		his.sid = SpeedIDUtil.getId();
		his.createtime = now();
		his.proc_inst_id_ = procinsts.getProcessInstanceId();
		his.taskname = params.i18n.get("antelope.startprocess");
		his.result = params.result;
		dao.insertOrUpdate(his);
		
		// 为实体补充流程信息
		Field field = entity.getClass().getField("proc_inst_id_");
		if (field != null) {
			field.setAccessible(true);
			field.set(entity, procinsts.getProcessInstanceId());
		}
		
		
		// 尝试启动可能存在的抄送
		startCopytoUsersProcesses(params.copytousers, procinsts.getProcessInstanceId(), params.processdefinitionkey);
		
		return new TaskCompleteResult(params.taskid, procinsts, tasks);
	}
	
	/**
	 * 设置变量值
	 * @param execution_id_ 流程执行实例id，子流程与父流程不相等，可以设置子流程单独的变量
	 * @param variableName 变量名称
	 * @param value 变量值
	 */
	public SysWorkflowService setVariable(String execution_id_, String variableName, String value) {
		checkServicesBind();
		runtimeService.setVariableLocal(execution_id_, variableName, value);
		return this;
	}
	
	/**
	 * 获取变量值
	 * @param execution_id_ 流程执行实例id，子流程与父流程不相等，可以获取到子流程单独的变量
	 * @param variableName 变量名称
	 * @param value 变量值
	 */
	public Object getVariable(String execution_id_, String variableName) {
		if (execution_id_ == null)
			return null;
		checkServicesBind();
		return runtimeService.getVariable(execution_id_, variableName);
	}
	
	/**
	 * 根据sql分页查询我的待办任务
	 * @param candidateuser 候选任务完成人
	 */
	public PageItem getTodoTasksBySql(String sql, String candidateuser, String processDefinitionKey, List<Object> params, PageParams pageParams) throws SQLException, Exception {
		checkServicesBind();
		return getTasksBySqlInner(sql, candidateuser, processDefinitionKey, params, pageParams, "activiti_querytodotasks");
	}
	
	/**
	 * 根据sql查询单条业务表数据，并添加流程状态信息(当前流程节点key:task_def_key_即流程图绘制时所添加的节点id)
	 */
	public JSONObject querySingleJSON(String sql, Object param, HttpServletRequest req) throws SQLException, Exception {
		JSONObject obj = DBUtil.querySingleJSON(sql, param);
		if (obj != null) {
			obj.put("task_def_key_", req.getParameter("task_def_key_"));
			
			// 查询暂存变量值
			if (stringSet(req.getParameter("execution_id_"))) {
				Map<String, Object> variables = taskService.getVariablesLocal(req.getParameter("taskid"));
				Set<Entry<String, Object>> entries = variables.entrySet();
				for (Entry<String, Object> entry : entries) {
					obj.put("var_" + entry.getKey(), entry.getValue());
				}
			}
		}
		return obj;
	}
	
	/**
	 * 根据sql分页查询业务表数据，并添加流程状态信息（包括当前阶段、当前待办处理人）
	 * @return
	 */
	public PageItem queryJSON(String sql, List<Object> params, PageParams pageparams) throws SQLException, Exception {
		checkServicesBind();
		
		PageItem pageItem = DBUtil.queryJSON(sql, params, pageparams);
		
		List<JSONObject> list = pageItem.getCurrList();
		if (list.isEmpty())
			return pageItem;
		
		// 查询状态信息
		for (int i = 0; i < list.size(); ++i) {
			JSONObject obj = list.get(i);
			
			List<JSONObject> tasks2 = DBUtil.queryJSON("select ID_, NAME_, TASK_DEF_KEY_ from ACT_RU_TASK where PROC_INST_ID_=?", obj.getString("proc_inst_id_"));
			
			String currtasknames = "";
			List<String> taskids = new ArrayList<String>();
			List<String> taskdefkeys = new ArrayList<String>();
			
			for (int j = 0; j < tasks2.size(); ++j) {
				JSONObject task = tasks2.get(j);
				currtasknames += task.getString("name_");
				if (j != tasks2.size() - 1) {
					currtasknames += ",";
				}
				taskids.add(task.getString("id_"));
				taskdefkeys.add(task.getString("task_def_key_"));
			}
			
			// 当前所在流程节点的所有节点定义key
			obj.put("task_def_keys_", join(",", taskdefkeys));
			
			// 查询所有待办人员
			List<String> candidateusers = DBUtil.queryStrings("select distinct t2.name from ACT_RU_IDENTITYLINK t inner join SYS_USER t2 on t.USER_ID_=t2.username where t.TASK_ID_ in ('" + join("','", taskids)+ "')");
			obj.put("activetasknames", currtasknames);
			obj.put("candidateusers", join(",", candidateusers));
			
			// 查询暂存变量值
			Map<String, Object> variables = workflowExtendedService.getPreStartProcessVariables(obj.getString("sid"));
			Set<Entry<String, Object>> entries = variables.entrySet();
			for (Entry<String, Object> entry : entries) {
				obj.put("var_" + entry.getKey(), entry.getValue());
			}
			
		}
		
		return pageItem;
	}
	
	/**
	 * 根据sql分页查询我的已办任务
	 * @param assignee 经办人
	 */
	public PageItem getDoneTasksBySql(String sql, String assignee, String processDefinitionKey, List<Object> params, PageParams pageParams) throws SQLException, Exception {
		checkServicesBind();
		return getTasksBySqlInner(sql, assignee, processDefinitionKey, params, pageParams, "activiti_querydonetasks");
	}
	
	/**
	 * 根据sql分页查询我的待阅任务
	 * @return
	 */
	public PageItem getToViewTasksBySql(String sql, String assignee, String processDefinitionKey, List<Object> params, PageParams pageParams) throws SQLException, Exception {
		checkServicesBind();
		return getViewTasksBySqlInner(sql, assignee, processDefinitionKey, params, pageParams, "activiti_querytoviewtasks");
	}
	
	/**
	 * 根据sql分页查询我的待阅任务
	 * @return
	 */
	public PageItem getViewedTasksBySql(String sql, String assignee, String processDefinitionKey, List<Object> params, PageParams pageParams) throws SQLException, Exception {
		checkServicesBind();
		return getViewTasksBySqlInner(sql, assignee, processDefinitionKey, params, pageParams, "activiti_queryviewedtasks");
	}
	
	/**
	 * 导出流程excel,根据workflowdatagrid选项
	 * @param tabkey 标志是在哪个页签下点击了导出按钮 
	 * 包括如下 icreated 我创建的，mytodo 我的待办，mydone 我的已办， all 所有，mytoview 我的待阅，myviewed 我的已阅
	 */
	public void exportExcel(String sql, List<Object> params, String candidateuser, WorkflowDatagridsOptions opts, String tabkey, I18n i18n, HttpServletResponse res) throws SQLException, Exception {
		Assert.notNull(opts, "导出选项不能为null，请进行初始化！");
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		
		String excelname = i18n.get("workflow_datagrids." + opts.getComponent() + "." + tabkey);
		
		res.setHeader("Content-Disposition","attachment; filename="+new String(excelname.getBytes("gb2312"), "ISO8859-1" ) + ".xls");
		
		HSSFWorkbook hbook = new HSSFWorkbook();
		HSSFSheet sheet = hbook.createSheet(excelname);
		HSSFRow row = sheet.createRow(0);
		
		Map<String, GridColumn> gridcolsMaps = getGridColumnByTabkey(opts, tabkey);
		Set<Entry<String, GridColumn>> entries = gridcolsMaps.entrySet();
		int i = 0;
		List<String> colkeys = new ArrayList<String>();
		for (Entry<String, GridColumn> entry : entries) {
			HSSFCell createCell = row.createCell(i++);
			createCell.setCellValue(new HSSFRichTextString(entry.getValue().headerText));
			colkeys.add(entry.getKey());
		}
		
		if ("icreated".equals(tabkey) || "all".equals(tabkey)) {
			row.createCell(i++).setCellValue(new HSSFRichTextString(i18n.get("antelope.currentphase")));
			row.createCell(i++).setCellValue(new HSSFRichTextString(i18n.get("antelope.currentcandidateuser")));
			colkeys.add("activetasknames");
			colkeys.add("candidateusers");
		} else if ("mytodo".equals(tabkey)) {
			row.createCell(i++).setCellValue(new HSSFRichTextString(i18n.get("antelope.currentphase")));
			colkeys.add("name_");
		} else if ("mydone".equals(tabkey)) {
			row.createCell(i++).setCellValue(new HSSFRichTextString(i18n.get("antelope.mycompletephase")));
			colkeys.add("name_");
		}
		
		for (int j = 0; j < i; ++j) {
			sheet.setColumnWidth(j, 7000);
			HSSFCell cell = row.getCell(j);
			setCellStyle(hbook, cell, true);
		}
		
		row.setHeight((short) 600);
		PageParams pageParams= new PageParams();
		pageParams.numPerPage = Integer.MAX_VALUE;
		pageParams.page = "1";
		
		List<JSONObject> list = null; 
		if ("icreated".equals(tabkey) || "all".equals(tabkey)) {
			list = queryJSON(sql, params, pageParams).getCurrList();
		} else if ("mytodo".equals(tabkey)) {
			list = getTodoTasksBySql(sql, candidateuser, opts.processDefinitionKey, params, pageParams).getCurrList();
		} else if ("mydone".equals(tabkey)) {
			list = getDoneTasksBySql(sql, candidateuser, opts.processDefinitionKey, params, pageParams).getCurrList();
		} else if ("mytoview".equals(tabkey)) {
			list = getToViewTasksBySql(sql, candidateuser, opts.processDefinitionKey, params, pageParams).getCurrList();
		} else if ("myviewed".equals(tabkey)) {
			list = getViewedTasksBySql(sql, candidateuser, opts.processDefinitionKey, params, pageParams).getCurrList();
		}
		
		for (int j = 0; j < list.size(); ++j) {
			JSONObject obj = list.get(j);
			
			// 我创建的当前阶段若不存在，则需要显示暂存中或已结束
			if ("icreated".equals(tabkey)) {
				if (!stringSet(obj.getString("activetasknames"))) {
					if (stringSet(obj.getString("proc_inst_id_"))) {
						obj.put("activetasknames", "(" + i18n.get("workflow_datagrids." + opts.getComponent() + ".hasover") + ")");
					} else {
						obj.put("activetasknames", "(暂存中)");	
					}
				}
			}
			
			HSSFRow datarow = sheet.createRow(j + 1);
			datarow.setHeight((short) 600);
			for (int k = 0; k < colkeys.size(); ++k) {
				HSSFCell createCell = datarow.createCell(k);
				createCell.setCellValue(new HSSFRichTextString(obj.getString(colkeys.get(k))));
				setCellStyle(hbook, createCell, false);
			}
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hbook.write(outputStream);
		ServletOutputStream streamOut = res.getOutputStream();
		try {
			outputStream.writeTo(streamOut);
		} catch( Exception e) { // 用户终止文件的下载操作，异常不进行处理
		}
		streamOut.close();
	}

	private void setCellStyle(HSSFWorkbook hbook, HSSFCell cell, boolean isheader) {
		HSSFCellStyle cellst = cell.getCellStyle();
		HSSFCellStyle createCellStyle = hbook.createCellStyle();
		createCellStyle.cloneStyleFrom(cellst);
		
		if (isheader) {
			createCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			createCellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
		}
		
		createCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		createCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		createCellStyle.setLeftBorderColor(HSSFColor.CORNFLOWER_BLUE.index);
		createCellStyle.setRightBorderColor(HSSFColor.CORNFLOWER_BLUE.index);
		createCellStyle.setTopBorderColor(HSSFColor.CORNFLOWER_BLUE.index);
		createCellStyle.setBottomBorderColor(HSSFColor.CORNFLOWER_BLUE.index);
		createCellStyle.setBorderLeft((short)1);
		createCellStyle.setBorderRight((short)1);
		createCellStyle.setBorderTop((short)1);
		createCellStyle.setWrapText(true);
		createCellStyle.setBorderBottom((short)1);
		cell.setCellStyle(createCellStyle);
	}
	
	private Map<String, GridColumn> getGridColumnByTabkey(WorkflowDatagridsOptions opts, String tabkey) {
		
		if ("icreated".equals(tabkey))
			return opts.iCreatedColumns;
		if ("mytodo".equals(tabkey))
			return opts.myTodoColumns;
		if ("mydone".equals(tabkey))
			return opts.myDoneColumns;
		if ("all".equals(tabkey))
			return opts.allColumns;
		if ("mytoview".equals(tabkey))
			return opts.toViewColumns;
		if ("myviewed".equals(tabkey))
			return opts.viewedColumns;
		
		return new HashMap<String, GridColumn>();
	}
	
	/**
	 * 获取执行流程变量值
	 * @param executionId 可以为proc_inst_id_
	 * @param variableName 变量名称
	 */
	public Object getRuntimeVariable(String executionId, String variableName) {
		return runtimeService.getVariable(executionId, variableName);
	}
	
	/**
	 * 获取历史流程变量值
	 * @param executionId  可以为proc_inst_id_
	 * @param variableName 变量名称
	 */
	public String getHisVariable(String executionId, String variableName) throws SQLException, Exception {
		return DBUtil.querySingleString("select TEXT_ from ACT_HI_DETAIL where EXECUTION_ID_=? and NAME_=? order by TIME_ desc", new Object[]{executionId, variableName});
	}
	
	/**
	 * 批量完成任务
	 * @param paramsList 完成任务时的批量参数
	 * @return
	 */
	public String completeTasks(List<ProcessParams> paramsList) throws Exception {
		checkServicesBind();
		for (ProcessParams processParams : paramsList) {
			String retval = completeTask(processParams);
			if (stringSet(retval)) {
				throw new Exception(retval);
			}
		}
		return "";
	}
	
	/**
	 * 完成任务
	 * @param taskid 任务id
	 * @param userid 用户id
	 * @return 返回错误信息的国际化key字符串
	 */
	public String completeTask(ProcessParams params) throws Exception {
		checkServicesBind();
		
		
		Task task = taskService.createTaskQuery().taskId(params.taskid).singleResult();
		
		// 暂存不提交
		if ("1".equals(params.tempsave)) {
			taskService.setVariablesLocal(params.taskid, params.variables);
			return "";
		}
		
		if (task == null)
			return "workflow_datagrids.workflowdatagridsdemo.hascompleted";
		
		AuditHistory his = new AuditHistory();
		his.assignee = params.assignee;
		List<SysUser> users = dao.getBy("username", params.assignee, SysUser.class);
		if (users.size() > 0) {
			his.assigneename = users.get(0).name;
			SysUnit unit = UserRoleOrgUtil.getUnitByUsername(params.assignee);
			if (unit == null) {
				his.unitname = params.i18n.get("antelope.orgnizationroot");
			} else {
				his.unitname = unit.name;
			}
		}
		his.sid = SpeedIDUtil.getId();
		his.createtime = now();
		his.proc_inst_id_ = task.getProcessInstanceId();
		his.taskname = task.getName();
		his.comment = params.comment;
		his.result = params.result;
		dao.insertOrUpdate(his);
		identityService.setAuthenticatedUserId(params.assignee);
		taskService.setAssignee(params.taskid, params.assignee);
		runtimeService.setVariables(task.getProcessInstanceId(), params.variables);
		runtimeService.setVariable(task.getProcessInstanceId(), "result", params.result);
		taskService.complete(task.getId());
	
		// 尝试启动可能存在的抄送
		startCopytoUsersProcesses(params.copytousers, task.getProcessInstanceId(), params.processdefinitionkey);
		
		return "";
	}
	
	/**
	 * 抄送
	 * @param copytousers
	 * @param processInstanceId
	 * @param processdefinitionkey
	 */
	private void startCopytoUsersProcesses(String[] copytousers, String processInstanceId, String processdefinitionkey) {
		if (copytousers != null && copytousers.length > 0) {
			for (int i = 0; i < copytousers.length; ++i) {
				ProcessInstance procinst = runtimeService.startProcessInstanceByKey("antelopecopyto", processdefinitionkey);
				Task copytotask = taskService.createTaskQuery().processInstanceId(procinst.getProcessInstanceId()).singleResult();
				copytotask.setDescription(processInstanceId);
				taskService.addCandidateUser(copytotask.getId(), copytousers[i]);
				taskService.saveTask(copytotask);
			}
		}
	}
	
	
	private PageItem getViewTasksBySqlInner(String sql, String usernameparam, String processDefinitionKey, List<Object> params, PageParams pageParams, String sqlid) throws SQLException, Exception {
		Sql actsql = SqlLoader.getInstance().getSql(sqlid);
		actsql.setParam("busitable", sql);
		params.add(0, "antelopecopyto");
		params.add(0, processDefinitionKey);
		params.add(0, usernameparam);
		return DBUtil.queryJSON(actsql.toString(), params, pageParams);
	}

	private PageItem getTasksBySqlInner(String sql, String usernameparam, String processDefinitionKey, List<Object> params, PageParams pageParams, String sqlid) throws SQLException, Exception {
		Sql actsql = SqlLoader.getInstance().getSql(sqlid);
		actsql.setParam("busitable", sql);
		params.add(0, processDefinitionKey);
		params.add(0, usernameparam);
		return DBUtil.queryJSON(actsql.toString(), params, pageParams);
	}
}

