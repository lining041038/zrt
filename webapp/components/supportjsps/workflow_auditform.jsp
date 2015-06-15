<%--
  工作流审批界面全界面组件支持jsp
 @author lining
 @since 2012-08-03
--%>
<%@page import="antelope.interfaces.components.supportclasses.AuditFormDisplayMode"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.SysWorkflowService"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.interfaces.components.supportclasses.WorkflowDatagridsOptions"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp" />
<%
	String taskid = request.getParameter("taskid");
	String sid = request.getParameter("sid");
	WorkflowDatagridsOptions opts = (WorkflowDatagridsOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
	String urlprefix = opts.getUrlprefix();
	SysWorkflowService workflow = SpringUtils.getBean(SysWorkflowService.class);
	String proc_inst_id_ = request.getParameter("proc_inst_id_");
	String execution_id_ = request.getParameter("execution_id_");
	String startFormKey = workflow.getStartFormKeyByProcessDefinitionKey(opts.processDefinitionKey);
	
	TransactionStatus status = SpringUtils.beginTransaction();
	if (startFormKey != null && startFormKey.indexOf("${") != -1) {
		startFormKey = startFormKey.substring(2, startFormKey.length() - 1);
		startFormKey = workflow.getHisVariable(proc_inst_id_, startFormKey).toString();
	}
	String formkey = workflow.getTaskFormKeyByTaskId(taskid);
	
	if (formkey != null && formkey.indexOf("${") != -1) {
		formkey = formkey.substring(2, formkey.length() - 1);
		formkey = workflow.getHisVariable(proc_inst_id_, formkey).toString();
	}
	SpringUtils.commitTransaction(status);
%>
<style>
#au_form1{height: 100%;}
</style>
<script>
$(function(){
	$("#au_form1").resize(function(){
		$("#tabs-3").height($(window).height()- 35);
	}).resize();
});

var task_def_key_sd = "<%=request.getParameter("task_def_key_")%>";

/*
 * tempsave 表明是否为暂存， 若为 0 则否，若为1则是 
 */
function _submittasks_(callback, tempsave, tarbtn, isautosave) {
	if (!$("#au_form1").validate(tempsave == '1', isautosave)) {
		
		if (isautosave) { // 若为自动暂存则需要进行回调
			callback(false); // 暂时仅区分校验是否出错
		}
		
		return;
	}
	
	$.commonSubmit({
		button: tarbtn,
		isAutoSave:isautosave,
		url:ctx + "<%=urlprefix%>/completeTask.vot?taskid=<%=taskid%>&proc_inst_id_=<%=proc_inst_id_%>&execution_id_=<%=execution_id_%>&task_def_key_=<%=request.getParameter("task_def_key_")%>&tempsave=" + tempsave,
		data:encodeURI($("#au_form1").serialize()),
		callback: function(data) {
			if (data) {
				alert($.i18n(data));
			} else {
				if (isautosave) { // 若为自动暂存则不需要对对话框进行关闭
					callback(true); // 暂时仅区分校验是否出错
					return true;
				}
				var ifrms = top.$("iframe");
				if (ifrms && ifrms.length) {
					for (var i = 0; i < ifrms.length; i++) {
						if (ifrms[i].contentWindow.document === window.document && top.$(ifrms[i]).is(".ifrtotopdialog")) {
							top.$(ifrms[i]).parent().dialog("close");
							callback(true);
							return true;
						}
					}
				}
			}
		}
	});
}
</script>
</head>
<body style="overflow: auto;">
	<form id="au_form1" class="tabs">
			<input type="hidden" name="processdefinitionkey" value="<%=opts.processDefinitionKey%>" default="<%=opts.processDefinitionKey%>"/>
			<%if (AuditFormDisplayMode.AUDIT_HIS_PROCESSCHART_TAB.equals(opts.auditFormDisplayMode)) {%>
				<ul>
					<li><a href="#tabs-1"><%=i18n.get("antelope.basiccontent") %></a></li>
					<li><a href="#tabs-2"><%=i18n.get("antelope.audithistory2") %></a></li>
					<li><a href="#tabs-3"><%=i18n.get("antelope.auditdiagram") %></a></li>
				</ul>
				<div id="tabs-1">
					<%if (TextUtils.stringSet(opts.businessFormKey)) { %>
					<jsp:include page="<%=opts.businessFormKey %>"></jsp:include>
					<%} else if (TextUtils.stringSet(startFormKey)){%>
						<jsp:include page="<%=startFormKey %>"></jsp:include>
					<%} %>
					<%if (TextUtils.stringSet(formkey)){%>
						<jsp:include page="<%=formkey %>"></jsp:include>
					<%} %>
			    </div>
			    <div id="tabs-2">
					<div id="hisdiv"></div>
			    </div>
				<div id="tabs-3" style="height: auto;">
					<img src="${ctx }/common/workflow/WorkflowCommonController/getWorkflowImg.vot?proc_inst_id_=<%=proc_inst_id_%>&rand=<%=Math.random()%>"/>
			    </div>
		    <%} else { %>
		    	<ul>
					<li><a href="#tabs-1"><%=i18n.get("antelope.basiccontent") %></a></li>
					<li><a href="#tabs-2"><%=i18n.get("antelope.audithistory") %></a></li>
					<li><a href="#tabs-3"><%=i18n.get("antelope.auditdiagram") %></a></li>
				</ul>
				<div id="tabs-1">
					<%if (TextUtils.stringSet(opts.businessFormKey)) { %>
					<jsp:include page="<%=opts.businessFormKey %>"></jsp:include>
					<%} else if (TextUtils.stringSet(startFormKey)){%>
						<jsp:include page="<%=startFormKey %>"></jsp:include>
					<%} %>
			    </div>
				<div id="tabs-2">
					<div id="hisdiv"></div>
					<%if (TextUtils.stringSet(formkey)){%>
						<jsp:include page="<%=formkey %>"></jsp:include>
					<%} %>
			    </div>
				<div id="tabs-3" style="height: auto;">
					<img src="${ctx }/common/workflow/WorkflowCommonController/getWorkflowImg.vot?proc_inst_id_=<%=proc_inst_id_%>&rand=<%=Math.random()%>"/>
			    </div>
		    <%} %>
	</form>
	<script>
	// 确认表单中的方法执行结束之后执行初始化
	$(function(){
		
		$("#hisdiv").datagrid("destroy").audithistory({proc_inst_id_:'<%=proc_inst_id_%>'});
		$("#au_form1").setCurrentState("completetask");
		
		$.getJSON(ctx + "<%=urlprefix%>/getFormData.vot?sid=<%=sid%>&proc_inst_id_=<%=proc_inst_id_%>&execution_id_=<%=execution_id_%>&task_def_key_=<%=request.getParameter("task_def_key_")%>&taskid=<%=taskid%>", function(data){
			$(".tabs").resetForm(data);
			data.task_def_key_ = task_def_key_sd;
			$("#tabs-1").trigger("openViewForm", data);
		});
	});
	</script>
</body>
</html>