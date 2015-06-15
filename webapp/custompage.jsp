
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.wcm.services.TemplateInfoService"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%
TransactionStatus status = SpringUtils.beginTransaction();
String path = "";
String finalpath = "";
try {
	path = TemplateInfoService.getActivatedTemplatePath(request);
	String jspname = "/" + RegExpUtil.getFirstMatched("(?<=/).*", request.getRequestURI().substring(1));
	finalpath = path + jspname;
} finally {
	SpringUtils.commitTransaction(status);
}
%>
<jsp:include page="<%=finalpath%>"/>
