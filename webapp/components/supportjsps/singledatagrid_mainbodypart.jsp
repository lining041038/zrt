<%--
  单列表界面通用js部分代码
 @author lining
 @since 2012-07-14
--%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.interfaces.components.supportclasses.Button"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.interfaces.components.supportclasses.SingleDatagridOptions"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%
	SingleDatagridOptions opts = (SingleDatagridOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = (String)request.getAttribute("component");
	String formname = "/" + RegExpUtil.getFirstMatched("(?<=/).*(?=\\.jsp)", request.getRequestURI().substring(1)) + "form.jsp";
%>
<!-- 主界面  -->
<form class="singlegridform_query" onsubmit="read(); return false;">
	<h3 class="smm_title"><span class="moduletitle"></span><span class="m_head" id="basicinfo"></span><span class="m_line"></span></h3>
	<div class="condition basicinfo">
		<jsp:include page="/components/supportjsps/grid_querypart.jsp"></jsp:include>
	</div>
</form>
<div class="datagrid_container"></div>
<div class="func_span" style="margin-top: 20px; white-space:nowrap;">
	<%for (Entry<String,Button> btn: opts.funcBtns.entrySet()) { 
		if (btn.getKey().equals(":antelope.exp") ||
			btn.getKey().equals(":antelope.imp") ||
			btn.getKey().equals(":single_datagrid.${component}.add") ||
			btn.getKey().equals(":antelope.batchdelete") ||
			btn.getKey().equals(":antelope.batchmoveup") ||
			btn.getKey().equals(":antelope.batchmovedown")) {
			String key = btn.getKey().replaceFirst("^:", "");
			key = key.replaceFirst("\\$\\{component\\}", component);
		%>
			<%if (btn.getKey().equals(":antelope.exp") && opts.showExportBtn) {%>
			<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			<%if (btn.getKey().equals(":antelope.imp") && opts.showImportBtn) {%>
			<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			
			<%if (btn.getKey().equals(":antelope.batchmoveup") && opts.showBatchMoveBtns) {%>
			<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			<%if (btn.getKey().equals(":antelope.batchmovedown") && opts.showBatchMoveBtns) {%>
			<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			
			<%if (btn.getKey().equals(":single_datagrid.${component}.add") && opts.showCreateBtn) {%>
			<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=opts.createBtnClickFunction %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			<%if (btn.getKey().equals(":antelope.batchdelete") && opts.showBatchDeleteBtn) {%>
			<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key)%>">
			<%} %>	
		<%} else { %>
			<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=btn.getKey()%>">
		<%} %>
	<%} %>
</div>


