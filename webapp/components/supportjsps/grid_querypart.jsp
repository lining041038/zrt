<%--
  部分全界面组件中数据表上方查询条件部分界面
 @author lining
 @since 2012-11-28
--%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.JSPUtilService"%>
<%@page import="antelope.utils.SpeedIDUtil"%>
<%@page import="antelope.utils.XmlEnumItem"%>
<%@page import="antelope.services.SessionService"%>
<%@page import="antelope.utils.SystemOpts"%>
<%@page import="antelope.interfaces.components.supportclasses.Queryable"%>
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
	I18n i18n = (I18n) request.getAttribute("i18n");
	if (i18n == null) {
		JSPUtilService jsputilservice = SpringUtils.getBean(JSPUtilService.class);
		jsputilservice.initHeader2(request, response);
	}

	Queryable opts = (Queryable) request.getAttribute("opts");
	i18n = (I18n) request.getAttribute("i18n");
	String component = (String) request.getAttribute("component");
	SessionService service = (SessionService) session.getAttribute("service");
	String formname = "/" + RegExpUtil.getFirstMatched("(?<=/).*(?=\\.jsp)", request.getRequestURI().substring(1)) + "form.jsp";
%>
<%if (TextUtils.stringSet(opts.getQueryformKey())) {// 当设置了查询对应jsp文件，则使用%>
<jsp:include page="<%=opts.getQueryformKey()%>"></jsp:include>
<%} else { %>
<div class="condi_container">
<%
for (int i = 0; i < opts.getQueryfields().length; ++i) {
	String[] fieldinfoarr = opts.getQueryfields()[i].split(":");
	String queryfieldname = fieldinfoarr[0];
	String fieldsenum = "";
	if (fieldinfoarr.length > 1) {
		fieldsenum = fieldinfoarr[1];
	}
	if ( i != 0 && i % 3 == 0) {%>
		<div class="condi_container">
	<%}%>
	<span class="cd_name"><%=opts.getColumns().get(queryfieldname).headerText %>：</span> <span class="cd_val">
	<%if (TextUtils.stringSet(opts.getColumns().get(queryfieldname).enumXml)){ 
		 if (TextUtils.stringSet(fieldsenum) && fieldsenum.matches("^checkbox$")){ 
			String locale = SystemOpts.getProperty("locale");
			if (service != null)
				locale = service.getLocale();
			XmlEnumItem[] enumitems = BaseController.getXmlEnumItems(opts.getColumns().get(queryfieldname).enumXml, locale);
			for (int j = 0; j < enumitems.length; ++j) {
				String boxid = "a" + SpeedIDUtil.getId();
	%>
			<input id="<%=boxid%>" style="width: 16px;" type="checkbox" value="<%=enumitems[j].value %>" name="<%=queryfieldname%>"/>
			<label for="<%=boxid%>"><%=enumitems[j].label %></label>
	<%		} 
		  } else {
	%>
		<select name="<%=queryfieldname%>" dataProvider="<%=opts.getColumns().get(queryfieldname).enumXml %>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>">
			<option value="">${i18n['antelope.all'] }</option>
		</select>
	<% 	  }
	  } else if (TextUtils.stringSet(fieldsenum) && fieldsenum.matches("^date|datetime$")){ // 此处控件与枚举均使用:做为分割，所以枚举名称一定不能使用控件的class%>
		<input name="<%=queryfieldname%>" class="<%=fieldsenum %>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>"/>
	<%} else if (TextUtils.stringSet(fieldsenum)){ %>
		<select name="<%=queryfieldname%>" dataProvider="<%=fieldsenum %>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>">
			<option value="">${i18n['antelope.all'] }</option>
		</select>
	<%} else {%>
		<input name="<%=queryfieldname%>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>"/>
	<%} %>
	</span>
	<%if ( i != (opts.getQueryfields().length - 1) && (i + 1) % 3 == 0) {%>
		</div>
	<%}%>
<%} %>
</div>
<%} %>

<%if (TextUtils.stringSet(opts.getQueryformKey()) || opts.getQueryfields().length > 0 ){// 确认是否显示查询按钮 %>
<div class="sonbtn_container"> <span class="sonbtn_m_line"></span>
	<a onclick="read()" class="btnth"><b class="sconbton2">${i18n['antelope.query']}</b></a>
</div>
<%} %> 

