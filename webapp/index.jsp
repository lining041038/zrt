
<%@page import="antelope.wcm.services.TemplateInfoService"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%
TransactionStatus status = SpringUtils.beginTransaction();
String path = "";
String finalpath = "";
try {
	path = TemplateInfoService.getActivatedTemplatePath(request);
	if (path.indexOf(".jsp") == -1)
		finalpath = path + "/index.jsp";
	else
		finalpath = path;
} finally {
	SpringUtils.commitTransaction(status);
}

// System.out.println(request.getRemoteHost());

%>
<jsp:include page="<%=finalpath%>"/>
