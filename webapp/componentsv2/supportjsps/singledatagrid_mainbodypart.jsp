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
		<jsp:include page="/componentsv2/supportjsps/grid_querypart.jsp"></jsp:include>
	</div>
</form>
<div class="func_span" style="margin-top: 20px; white-space:nowrap; min-height: 36px;">
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
			<input class="btn btn-sm btn-default" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			<%if (btn.getKey().equals(":antelope.imp") && opts.showImportBtn) {%>
			<input class="btn btn-sm btn-default" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			
			<%if (btn.getKey().equals(":antelope.batchmoveup") && opts.showBatchMoveBtns) {%>
			<input class="btn btn-sm btn-default" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			<%if (btn.getKey().equals(":antelope.batchmovedown") && opts.showBatchMoveBtns) {%>
			<input class="btn btn-sm btn-default" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=i18n.get(key) %>">
			<%} %>
			<%if (btn.getKey().equals(":single_datagrid.${component}.add") && opts.showCreateBtn) {%>
			<button class="btn btn-sm btn-default" style="height: 28px;line-height: 1.3;" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=opts.createBtnClickFunction %>($('.datagrid_container'))" value="">
				<span class="glyphicon glyphicon-plus" style="font-family:  Glyphicons Halflings; font-size:12px; padding-right:3px;"></span>
				<%=i18n.get(key) %>
			</button>
			<%} %>
			<%if (btn.getKey().equals(":antelope.batchdelete") && opts.showBatchDeleteBtn) {%>
			<button class="btn btn-sm btn-default" style="height: 28px;line-height: 1.3;"  type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="">
				<span class="glyphicon glyphicon-remove" style="font-family:  Glyphicons Halflings; font-size:12px; padding-right:3px;"></span>
				<%=i18n.get(key) %>
			</button>
			<%} %>	
		<%} else { %>
			<input class="btn btn-sm btn-default" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('.datagrid_container'))" value="<%=btn.getKey()%>">
		<%} %>
	<%} %>
</div>
<div class="datagrid_container"></div>


