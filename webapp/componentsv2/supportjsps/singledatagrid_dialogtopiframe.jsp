<%--
  单列表界面模版
 @author lining
 @since 2012-07-14
--%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.interfaces.components.supportclasses.Button"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.interfaces.components.supportclasses.SingleDatagridOptions"%>
<%@page import="antelope.utils.I18n"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp" />
<%
	SingleDatagridOptions opts = (SingleDatagridOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = (String) request.getAttribute("component");
	String formname = "/" + RegExpUtil.getFirstMatched(Pattern.compile("(?<=/).*(?=\\.jsp)"), request.getRequestURI().substring(1)) + "form.jsp";
%>
<title>${i18n['antelope.systemname']}</title>
<style></style>
</head><body style="overflow-x:hidden; overflow-y:auto;">
<jsp:include page="/componentsv2/supportjsps/singledatagrid_dialogformpart.jsp"></jsp:include>
</body>
</html>