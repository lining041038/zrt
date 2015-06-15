<%--
  单列表界面模版
 @author lining
 @since 2012-07-14
--%>
<%@page import="antelope.interfaces.components.supportclasses.CreateUpdateWindowMode"%>
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

<% request.setAttribute("componentVersion", "v2"); %>

<jsp:include page="/include/header2.jsp" />
<%
	SingleDatagridOptions opts = (SingleDatagridOptions) request.getAttribute("opts");
%>
<title>${i18n['antelope.systemname']}</title>
<style></style>
<jsp:include page="/componentsv2/supportjsps/singledatagridjspart.jsp"></jsp:include>
</head><body class="sm_main" style="overflow-x:hidden;">

<%if (CreateUpdateWindowMode.DISPLAY_INLINE.equals(opts.createUpdateWindowMode)) {%>
<div class="list_firstviewdiv">
<%} %>

<%if (CreateUpdateWindowMode.DIALOG.equals(opts.createUpdateWindowMode)) {%>
	<jsp:include page="/componentsv2/supportjsps/singledatagrid_dialogformpart.jsp"></jsp:include>
<%} %>

<jsp:include page="/componentsv2/supportjsps/singledatagrid_mainbodypart.jsp"></jsp:include>

<%if (CreateUpdateWindowMode.DISPLAY_INLINE.equals(opts.createUpdateWindowMode)) {%>
</div>
<jsp:include page="/componentsv2/supportjsps/singledatagrid_dialogformpart.jsp"></jsp:include>
<%} %>
</body>
</html>