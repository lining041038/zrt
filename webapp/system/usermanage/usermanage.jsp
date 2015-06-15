<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.SessionService"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@ page language="java" pageEncoding="utf-8"%>

<%

SessionService service = (SessionService) request.getSession().getAttribute("service");
String theme = "defaults";
TransactionStatus status = SpringUtils.beginTransaction();
try {
	if (service != null) {
		 theme = service.getTheme();
	}
} finally {
	SpringUtils.commitTransaction(status);
}

if ("fjh".equals(theme)) {
	
%>
<jsp:include page="/componentsv2/tree_datagrid.jsp">
	<jsp:param name="component" value="usermanage"/>
</jsp:include>
<%
} else {
%>
<jsp:include page="/components/tree_datagrid.jsp">
	<jsp:param name="component" value="usermanage"/>
</jsp:include>
<%
}
%>

