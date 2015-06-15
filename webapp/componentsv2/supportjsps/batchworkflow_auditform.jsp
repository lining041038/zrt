<%--
  工作流审批界面全界面组件支持jsp
 @author lining
 @since 2012-08-03
--%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.SysWorkflowService"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.interfaces.components.supportclasses.WorkflowDatagridsOptions"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp" />
<%
	
	String alltasksids = request.getParameter("alltasksids");
	String allprocinstsids = request.getParameter("allprocinstsids");
	String allexecutionids = request.getParameter("allexecutionids");
	String proc_inst_id_ = allprocinstsids.split(",")[0];
	String taskid = alltasksids.split(",")[0];
	
	WorkflowDatagridsOptions opts = (WorkflowDatagridsOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
	String urlprefix = opts.getUrlprefix();
	SysWorkflowService workflow = SpringUtils.getBean(SysWorkflowService.class);
	
	TransactionStatus status = SpringUtils.beginTransaction();
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
function _submittasks_(callback, tarbtn) {
	if (!$("#au_form1").validate())
		return;
	$.commonSubmit({
		button: tarbtn,
		url:ctx + "<%=urlprefix%>/batchCompleteTasks.vot?taskid=<%=taskid%>&proc_inst_id_=<%=proc_inst_id_%>",
		data:encodeURI($("#au_form1").serialize()) + "&allexecutionids=<%=allexecutionids%>&taskids=<%=alltasksids%>&proc_inst_id_s=<%=allprocinstsids%>&task_def_key_=<%=request.getParameter("task_def_key_")%>",
		callback: function(data) {
			if (data) {
				alert($.i18n(data));
			} else {
				var ifrms = top.$("iframe");
				if (ifrms && ifrms.length) {
					for (var i = 0; i < ifrms.length; i++) {
						if (ifrms[i].contentWindow.document === window.document && top.$(ifrms[i]).is(".ifrtotopdialog")) {
							top.$(ifrms[i]).parent().dialog("close");
							callback();
							return true;
						}
					}
				}
			}
		}
	});
}
$(function(){
	$("#au_form1").setCurrentState("completetask");
});
</script>
</head>
<body style="overflow: auto;">
	<form id="au_form1" class="tabs">
			<input type="hidden" name="processdefinitionkey" value="<%=opts.processDefinitionKey%>" default="<%=opts.processDefinitionKey%>"/>
			<ul>
				<li><a href="#tabs-2"><%=i18n.get("antelope.audithistory") %></a></li>
				<li><a href="#tabs-3"><%=i18n.get("antelope.auditdiagram") %></a></li>
			</ul>
			<div id="tabs-2">
				<%if (TextUtils.stringSet(formkey)){%>
					<jsp:include page="<%=formkey %>"></jsp:include>
				<%} %>
		    </div>
			<div id="tabs-3" style="overflow: auto;">
				<img src="${ctx }/common/workflow/WorkflowCommonController/getWorkflowImg.vot?proc_inst_id_=<%=proc_inst_id_%>"/>
		    </div>
	</form>
</body>
</html>