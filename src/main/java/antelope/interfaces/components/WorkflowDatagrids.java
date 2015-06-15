package antelope.interfaces.components;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.supportclasses.WorkflowDatagridsOptions;

/**
 * 通用工作流全界面组件
 * @author lining
 * @since 2012-7-14
 */
public abstract class WorkflowDatagrids extends BaseUIController{
	
	@Override
	public abstract WorkflowDatagridsOptions getOptions(HttpServletRequest req);
	
	@RequestMapping("/listICreated")
	public abstract void listICreated(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	@RequestMapping("/listMyTodo")
	public abstract void listMyTodo(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	@RequestMapping("/listMyDone")
	public abstract void listMyDone(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	@RequestMapping("/listAll")
	public abstract void listAll(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	@RequestMapping("/listMyToView")
	public void listMyToView(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Assert.isTrue(false, "listMyToView 方法在调用之前必须被子类所覆盖，否则不予支持使用，请确保您打开了WorkflowDatagridsOptions的showToView选项之后覆盖了此方法的实现");
	}
	
	@RequestMapping("/listMyTiewed")
	public void listMyTiewed(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception{
		Assert.isTrue(false, "listMyTiewed 方法在调用之前必须被子类所覆盖，否则不予支持使用，请确保您打开了WorkflowDatagridsOptions的showToView选项之后覆盖了此方法的实现");
	}
	
	@RequestMapping("/completeTask")
	public abstract void completeTask(String taskid, String proc_inst_id_,HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	/**
	 * 对相同任务节点下的任务进行批量审批
	 * @param taskids 逗号分隔的流程引擎id
	 * @param proc_inst_id_s 逗号分隔的流程实例id
	 */
	@RequestMapping("/batchCompleteTasks")
	public void batchCompleteTasks(String taskids, String proc_inst_id_s, HttpServletRequest req, HttpServletResponse res) throws IOException, Exception {
		Assert.isTrue(false, "batchCompleteTasks 方法在调用之前必须被子类所覆盖，否则不予支持使用，请确保您打开了WorkflowDatagridsOptions的showToView选项之后覆盖了此方法的实现");
	}
	
	/**
	 * 在我创建的页签下点击删除按钮时调用
	 */
	@RequestMapping("/deleteOneLine")
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, SQLException, Exception {
		Assert.isTrue(false, "deleteOneLine 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 使用默认行为或者子类覆盖，用于导出excel数据
	 * 前台不会将查询参数传递到后台
	 * @param tabkey 标志是在哪个页签下点击了导出按钮 
	 * 包括如下 icreated 我创建的，mytodo 我的待办，mydone 我的已办， all 所有，mytoview 我的待阅，myviewed 我的已阅
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(String tabkey, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Assert.isTrue(false, "exportExcel 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 获取单表单数据
	 */
	@RequestMapping("/getFormData")
	public abstract void getFormData(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception;
	
	/**
	 * 验证用户输入合法性，并启动新流程，最终使用workflow上下文对象的相关方法执行启动操作
	 */
	@RequestMapping("/startProcessInstance")
	public abstract void startProcessInstance(HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException, SecurityException, NoSuchFieldException, Exception;
}
