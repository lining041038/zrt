package antelope.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.springmvc.BaseController;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

/**
 * 获取工作流相关数据控制器
 * @author lining
 */
@Controller
public class SysWorkFlowController extends BaseController {
	
//	@Resource
//	public WorkflowService workflowService;  //流程引擎服务类

	/**
	 * 获取审批流程图信息
	 * @throws IOException 
	 */
	@RequestMapping("/common/SysWorkFlowController/getAuditGraphicInfo")
	public void getAuditGraphicInfo(String processid, HttpServletRequest req, HttpServletResponse res) throws IOException {
		// 获取流程图信息
		try {
			//ProcessDefinitionInfo pdi=  workflowService.getProcessDefInfo(processid);
			//print(new JSONObject(pdi), res);
		} catch(Throwable e)  {
			print(new JSONObject(), res);
		}
	}
	
	/**
	 * 根据流程定义id获取流程定义sql文本
	 */
	@RequestMapping("/common/SysWorkFlowController/getWorkflowDefinationSql")
	public void getWorkflowDefinationSql(String definationid, HttpServletResponse res) throws SQLException, Exception {
		String definationsql = getDefinationstr(definationid);
		print(definationsql, res);
	}
	
	/**
	 * 根据流程定义id获取流程定义sql文件
	 */
	@RequestMapping("/common/SysWorkFlowController/exportWorkflowDefinationSql")
	public void exportWorkflowDefinationSql(String definationid, HttpServletResponse res) throws SQLException, Exception {
		JSONObject result = DBUtil.querySingleJSON("select * from PROCESS_DEFINITION where id=?", definationid);
		String definationsql = getDefinationstr(definationid);
		printBytes(definationsql.getBytes("utf-8"),  "_000_" + result.getString("name") + ".sql", res);
	}
	
	/**
	 * 根据流程定义id获取流程定义sql文件
	 */
	@RequestMapping("/common/SysWorkFlowController/exportWorkflowDefinationSqlTo")
	public void exportWorkflowDefinationSqlTo(String apppath, String definationid, HttpServletResponse res) throws SQLException, Exception {
		apppath = decodeAndTrim(apppath);
		JSONObject result = DBUtil.querySingleJSON("select * from PROCESS_DEFINITION where id=?", definationid);
		String definationsql = getDefinationstr(definationid);
		FileOutputStream fis = new FileOutputStream(apppath + "/_000_" + result.getString("name") + ".sql");
		fis.write(definationsql.getBytes("utf-8"));
		fis.flush();
		fis.close();
	}

	private String getDefinationstr(String definationid) throws SQLException,
			Exception, JSONException {
		JSONObject result = DBUtil.querySingleJSON("select * from PROCESS_DEFINITION where id=?", definationid);
		String definationsql = "\n-- 名称为：\"" + result.getString("name") + "\" 的流程定义sql\n";
		definationsql += "-- @author \n";
		definationsql += "-- @since "+getNewSdf().format(today())+"\n\n";
		definationsql += "INSERT [dbo].[PROCESS_DEFINITION] ([ID], [NAME], [JSON_STR], [XML_STR], [NODE_JSON_STR], [FLAG], [PROCESSVERSION]) VALUES (N'";
		definationsql += tidyVal(result, "id") + "', N'";
		definationsql += tidyVal(result, "name") + "', N'";
		definationsql += tidyVal(result, "json_str") + "', N'";
		definationsql += tidyVal(result, "xml_str") + "', N'";
		definationsql += tidyVal(result, "node_json_str") + "', N'";
		definationsql += tidyVal(result, "flag") + "', ";
		definationsql += tidyVal(result, "processversion") + ");\n";
		
		result = DBUtil.querySingleJSON("select * from PROCESSDEF_PROCESSPUB_INFO where PROCESSDEF_ID=?", definationid);
		definationsql += "INSERT [dbo].[PROCESSDEF_PROCESSPUB_INFO] ([ID], [DEPLOYMENT_ID], [PROCESSDEF_ID]) VALUES (N'";
		definationsql += tidyVal(result, "id") + "', N'";
		definationsql += tidyVal(result, "deployment_id") + "', N'";
		definationsql += tidyVal(result, "processdef_id") + "');\n";
		
		String deploymentid = result.getString("deployment_id");
		
		result = DBUtil.querySingleJSON("select * from JBPM4_DEPLOYMENT where DBID_=?", deploymentid);
		definationsql += "INSERT [dbo].[JBPM4_DEPLOYMENT] ([DBID_], [NAME_], [TIMESTAMP_], [STATE_]) VALUES (CAST(";
		definationsql += tidyVal(result, "dbid_") + " AS Numeric(19, 0)), ";
		definationsql += tidyVal(result, "name_") + ", CAST(";
		definationsql += tidyVal(result, "timestamp_") + " AS Numeric(19, 0)), N'";
		definationsql += tidyVal(result, "state_") + "');\n";
		
		result = DBUtil.querySingleJSON("select * from JBPM4_LOB where DEPLOYMENT_=?", deploymentid);
		definationsql += "INSERT [dbo].[JBPM4_LOB] ([DBID_], [DBVERSION_], [BLOB_VALUE_], [DEPLOYMENT_], [NAME_]) VALUES (CAST(";
		definationsql += tidyVal(result, "dbid_") + " AS Numeric(19, 0)), CAST(";
		definationsql += tidyVal(result, "dbversion_") + " AS Numeric(19, 0)), 0x";
		definationsql += tidyVal(result, "blob_value_") + ", CAST(";
		definationsql += tidyVal(result, "deployment_") + " AS Numeric(19, 0)), N'";
		definationsql += tidyVal(result, "name_") + "');\n";
		
		List<JSONObject> jsons = DBUtil.queryJSON("select * from JBPM4_DEPLOYPROP where DEPLOYMENT_=?", deploymentid);
		for (JSONObject jsonObject : jsons) {
			definationsql += "INSERT [dbo].[JBPM4_DEPLOYPROP] ([dbid_], [deployment_], [objname_], [key_], [stringval_], [longval_]) VALUES (CAST(";
			definationsql += tidyVal(jsonObject, "dbid_") + " AS Numeric(19, 0)), CAST(";
			definationsql += tidyVal(jsonObject, "deployment_") + " AS Numeric(19, 0)), N'";
			definationsql += tidyVal(jsonObject, "objname_") + "', N'";
			if (tidyVal(jsonObject, "stringval_") == null) {
				definationsql += tidyVal(jsonObject, "key_") + "', ";
				definationsql += tidyVal(jsonObject, "stringval_") + ", ";
			} else {
				definationsql += tidyVal(jsonObject, "key_") + "', N'";
				definationsql += tidyVal(jsonObject, "stringval_") + "', ";
			}
			definationsql += tidyVal(jsonObject, "longval_") + ");\n";
		}
		return definationsql;
	}
	
	
	public String tidyVal(JSONObject valobj, String key) throws JSONException {
		if (valobj.getString(key) == null)
			return valobj.getString(key);
		return valobj.getString(key).replaceAll("'", "''");
	}
}








