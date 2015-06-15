package antelope.demos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.demos.entites.BookOrderItem;
import antelope.interfaces.components.WorkflowDatagrids;
import antelope.interfaces.components.supportclasses.AuditFormDisplayMode;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.WorkflowDatagridsOptions;
import antelope.services.ProcessParams;
import antelope.springmvc.SqlWhere;
import antelope.springmvc.validators.Validator;
import antelope.utils.MailUtil;
import antelope.utils.SpeedIDUtil;

@Controller("workflowdatagridsdemo")
@RequestMapping("/demos/workflow_datagrids_demo/WorkflowDatagridsDemo")
public class WorkflowDatagridsController extends WorkflowDatagrids {
	
	private List<Validator> candidateusers = Arrays.asList(validators.required());
	
	/**
	 * 获取工作流选项
	 */
	@Override
	public WorkflowDatagridsOptions getOptions(HttpServletRequest req) {
		WorkflowDatagridsOptions opts = new WorkflowDatagridsOptions("bookorder", this);
		LinkedHashMap<String, GridColumn> map = new LinkedHashMap<String, GridColumn>();
		map.put("bookname", new GridColumn("书名", null));
		map.put("createtime", new GridColumn("创建时间", null));
		opts.setAllColumnFields(map);
		opts.batchCompleteTasksEnabled = true;
		
		opts.showExportBtn = true; // 显示导出按钮
		
		opts.showDeleteBtn = true; // 我创建的页签下暂存中的条目显示删除按钮
		

		opts.auditFormDisplayMode = AuditFormDisplayMode.AUDIT_HIS_PROCESSCHART_TAB;
		
		opts.selectionMode = "multipleRows"; // 显示复选框
		
		opts.autoTempSaveEnabled = true; // 启用当我创建的下暂存状态的条目以及待办条目打开对话框时自动暂存的功能。
		
		opts.viewButtonField = "bookname"; // 点击标题进入查看页面demo
		
		// 不显示暂存按钮接口
		// opts.showTempSaveBtn = true;
		
		opts.businessFormKey ="/demos/bookorderform.jsp";
		opts.queryformKey = "/demos/workflowcustomqueryform.jsp";
		opts.showToView = true;
		opts.queryfields = new String[]{"bookname"};
		return opts;
	}
	
	/**
	 * 我发起的
	 */
	@Override
	public void listICreated(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = cmsqlwhere(req);
		sqlwhere.outParams.add(0, getService(req).getUsersid());
		print(workflow.queryJSON("select * from DEMO_BOOKORDER where creatorsid=?" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req, "createtime")), res);
	}
	
	/**
	 * 我创建的页签下删除没有提交的暂存阶段的待办
	 */
	@Override
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, SQLException, Exception {
		dao.deleteBy(sid, BookOrderItem.class);
	}
	
	/**
	 * 新建流程
	 * @throws Exception 
	 */
	@Override
	public void startProcessInstance(HttpServletRequest req, HttpServletResponse res) throws Exception {
		BookOrderItem book = wrapToEntity(req, newInstanceWithCreateInfo(BookOrderItem.class, req));
		ProcessParams params = getProcessParams(req);
		workflow.startProcessInstance(params, book);
		dao.insertOrUpdate(book);
		MailUtil.sendMail("timewalking123@163.com","李四", "timewalking123@163.com", "买书", "http://www.baidu.com");
	}
	
	/**
	 * 我的待办
	 */
	@Override
	public void listMyTodo(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = cmsqlwhere(req);
		WorkflowDatagridsOptions opts = getOptions(req);
		print(workflow.getTodoTasksBySql("select * from DEMO_BOOKORDER where 1=1" + sqlwhere.wherePart, getService(req).getUsername(), opts.processDefinitionKey, sqlwhere.outParams, getPageParams(req, "createtime")), res);
	}
	
	/**
	 * 我的已办
	 */
	@Override
	public void listMyDone(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = cmsqlwhere(req);
		WorkflowDatagridsOptions opts = getOptions(req);
		print(workflow.getDoneTasksBySql("select * from DEMO_BOOKORDER where 1=1" + sqlwhere.wherePart, getService(req).getUsername(), opts.processDefinitionKey, sqlwhere.outParams, getPageParams(req, "createtime")), res);
	}
	
	/**
	 * 所有
	 */
	@Override
	public void listAll(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = cmsqlwhere(req);
		print(workflow.queryJSON("select * from DEMO_BOOKORDER where 1=1" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req, "createtime")), res);
	}
	
	/**
	 * 完成任务
	 */
	@Override
	public void completeTask(String taskid, String proc_inst_id_, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
//		String result = validate("var_candidateusers", "下级待办人", candidateusers, req);
//		if (stringSet(result)) {
//			print(result, res);
//			return;
//		}
		
		String sid = req.getParameter("sid");
		BookOrderItem book = dao.getBy(sid, BookOrderItem.class);
		wrapToEntity(req, book);
		dao.insertOrUpdate(book);
		
		ProcessParams params = getProcessParams(req);
		
		// 获取到可能涉及的多实例子流程关联数据对象
		WorkflowMultiUserItem  candidateusers = (WorkflowMultiUserItem) workflow.getVariable(params.execution_id_, "candidatelist");
		
		if ("payment".equals(params.task_def_key_) && "不通过".equals(params.result)) {
			List<String> mulusers = (List<String>) params.variables.get("multiinstusers");
			List<WorkflowMultiUserItem> userItems = new ArrayList<WorkflowMultiUserItem>();
			for (String muluser : mulusers) {
				WorkflowMultiUserItem item = new WorkflowMultiUserItem();
				item.candidateusers = muluser;
				item.businesssid = SpeedIDUtil.getId();
				userItems.add(item);
			}
			params.variables.put("multiinstusers", userItems);
		}
		
		print(workflow.completeTask(params), res);
	}
	
	/**
	 * 批量完成任务
	 */
	@Override
	public void batchCompleteTasks(String taskids, String proc_inst_id_s, HttpServletRequest req, HttpServletResponse res) throws IOException, Exception {
		print(workflow.completeTasks(getBatchProcessParams(req)), res);
	}
	
	/**
	 * 导出
	 */
	@Override
	public void exportExcel(String tabkey, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		String sids = req.getParameter("selectedItemSids");
		System.out.println(sids);
		
		String sql = "select * from DEMO_BOOKORDER";
		List<Object> params = new ArrayList<Object>();
		// 此处可以通过tabkey来决定使用sql的形式
		if ("icreate".equals(tabkey)) {
			sql = "select * from DEMO_BOOKORDER where creatorsid=?";
			params.add(getService(req).getUsersid());
		}
		workflow.exportExcel(sql, params, getService(req).getUsername(), getOptions(req), tabkey, getI18n(req), res);
	}
	
	/**
	 * 我的待阅
	 */
	@Override
	public void listMyToView(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = cmsqlwhere(req);
		WorkflowDatagridsOptions opts = getOptions(req);
		print(workflow.getToViewTasksBySql("select * from DEMO_BOOKORDER where 1=1" + sqlwhere.wherePart, getService(req).getUsername(), opts.processDefinitionKey, sqlwhere.outParams, getPageParams(req, "createtime")), res);
	}

	/**
	 * 我的已阅
	 */
	@Override
	public void listMyTiewed(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = cmsqlwhere(req);
		WorkflowDatagridsOptions opts = getOptions(req);
		print(workflow.getViewedTasksBySql("select * from DEMO_BOOKORDER where 1=1" + sqlwhere.wherePart, getService(req).getUsername(), opts.processDefinitionKey, sqlwhere.outParams, getPageParams(req, "createtime")), res);
	}
	
	/**
	 * 获取单表单数据
	 */
	@Override
	public void getFormData(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 此处当前能获取到流程参数中的 task_def_key_和 execution_id_两个参数
		ProcessParams params = getProcessParams(req);
		
		// 获取到可能涉及的多实例子流程关联数据对象
		WorkflowMultiUserItem candidateusers =  (WorkflowMultiUserItem) workflow.getVariable(params.execution_id_, "candidatelist");
		
		print(workflow.querySingleJSON("select * from DEMO_BOOKORDER where sid=?", sid, req), res);
	}
	
	private SqlWhere cmsqlwhere(HttpServletRequest req) {
		return tidySqlWhere(new String[]{"bookname like ?"}, new Object[]{"%" + d(req.getParameter("bookname")) + "%"});
	}
	

}



