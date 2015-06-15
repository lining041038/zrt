package antelope.interfaces.components.supportclasses;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.PageItem;
import antelope.workflow.ProcessDiagramGenerator;

@Controller
public class WorkflowCommonController extends BaseController{
	
	private RepositoryService repositoryService;
	private HistoryService historyService;
	private RuntimeService runtimeService;
	public void checkServicesBind() {
		if (repositoryService == null) {
			repositoryService = SpringUtils.getBean(RepositoryService.class);
			runtimeService = SpringUtils.getBean(RuntimeService.class);
			historyService = SpringUtils.getBean(HistoryService.class);
		}
	}

	@RequestMapping("/common/workflow/WorkflowCommonController/getWorkflowImg")
	public void getWorkflowImg(String proc_inst_id_, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		checkServicesBind();
		HistoricProcessInstance inst = historyService.createHistoricProcessInstanceQuery().processInstanceId(proc_inst_id_).singleResult();
		ProcessDefinitionEntity defe = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(inst.getProcessDefinitionId());
		
		int ct = DBUtil.queryCount("select count(*) ct from ACT_RU_EXECUTION where PROC_INST_ID_=?", proc_inst_id_);
		InputStream is = null;
		if (ct > 0) {
			is = ProcessDiagramGenerator.generateDiagram(defe, "png", runtimeService.getActiveActivityIds(proc_inst_id_));
		} else {
			is = ProcessDiagramGenerator.generateDiagram(defe, "png", new ArrayList<String>());
		}
		byte[] byts = new byte[is.available()];
		is.read(byts);
		is.close();
		printBytes(byts, res);
	}
	
	/**
	 * 获取工作流审批历史
	 * @param proc_inst_id_ 工作流实例id
	 */
	@RequestMapping("/common/workflow/WorkflowCommonController/getWorkflowAuditHistory")
	public void getWorkflowAuditHistory(String proc_inst_id_, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		checkServicesBind();
		PageItem pageItem = DBUtil.queryJSON("select * from ACT_AN_AUDIT_HIS where proc_inst_id_=? order by createtime", proc_inst_id_, getPageParams(req));
		print(pageItem, res);
	}
}
