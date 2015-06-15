<%--
  单列表界面通用弹出表单部分代码
 @author lining
 @since 2012-07-14
--%>
<%@page import="antelope.interfaces.components.supportclasses.CreateUpdateWindowMode"%>
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
	
	String ifrformname = request.getParameter("ifrformname");
	if (TextUtils.stringSet(ifrformname)) {
		formname = ifrformname; 
	}
	
%>
<!-- 数据导入对话框 -->
<form id="singlegridform_imp" style="display: none;" enctype="multipart/form-data">
	<input type="file" name="importexcelfile"/>
	<button onclick="downloadImpTemp__()" class="btn btn-sm btn-default" type="button" style="float: right;"><%=i18n.get("antelope.templatedownload") %></button>
</form>
<!-- 新建修改查看对话框内容 -->
<form id="singlegridform" class="viewudialog" <%=CreateUpdateWindowMode.DIALOG_TOP_IFRAME.equals(opts.createUpdateWindowMode)?" ":" style=\"display:none;\"" %> >
	<div style="height: 100%; position: relative; left: 2px; top: 2px; border-right: 1px solid #cfcfcf; border-bottom: 1px solid #e9e9e9; background: none;">
		<div style="height: 100%; position: relative; left: 2px; top: 2px; border-right: 1px solid #e9e9e9; border-bottom: 1px solid #e9e9e9; background: none; ">
		<%if (TextUtils.stringSet(opts.formKey)){ %>
			<jsp:include page="<%=opts.formKey%>" ></jsp:include>
		<% } else if (opts.formfields.length == 0) {%>
			<jsp:include page="<%=formname%>" ></jsp:include>
		<% } else { %>
		<div class="condi_container">
			<%for (int i = 0; i < opts.formfields.length; ++i) { %>
			<span class="cd_name"><%=opts.formfields[i].label%>：</span>
			<span class="cd_val">
				<input name="<%=opts.formfields[i].field%>" <%=opts.formfields[i].validate%> state='disabled:false; disabled.view:true;'/>
			</span>
			<%} %>
		</div>
		<%} %>
		</div>
	</div>
</form>