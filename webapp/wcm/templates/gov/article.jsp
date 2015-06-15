<%@page import="antelope.wcm.beans.WebPageDataBean"%>
<%@page import="antelope.utils.JSONArray"%>
<%@page import="antelope.wcm.consts.WCMSiteSettingConsts"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.utils.JSONObject"%>
<%@page import="antelope.wcm.services.TemplateInfoService"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.SessionService"%>
<%@ page language="java" pageEncoding="utf-8"%>

<%
	String root = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+root+"/";
%>
<% WebPageDataBean webdata = TemplateInfoService.getWebPageData(request); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><%=webdata.getPageTitle() %></title>
	<base href="<%=basePath%><%=webdata.getTemplatepath()%>/">
	<link rel="stylesheet" type="text/css" href="assets/index140516.css"/> 
	<link rel="stylesheet" type="text/css" href="assets/master_cn_v1.0.css"/> 
	<style>
		/* div {
			background: red;
		} */
	</style>
</head>
<body>

		<!--头部-->
	<%@include file="header.jsp"%>
		
	<div class="BreadcrumbNav">
		<div class="main-colum">
			<a class="" target="_blank" href="<%=basePath %>index.jsp?sitesid=<%=webdata.getSitesid()%>">首页</a><font class="">&gt;</font>
			<%for (JSONObject menu: webdata.getBreadcrumbs()) { %>
				<a class="" target="_blank" href="<%=basePath%><%=menu.getString("href")%>"><%=menu.getString("name")%></a><font class="">&gt;</font>
			<%} %>
			
		</div>
	</div>
	<div class="wrap">
		<div class="frame-pane">
			<div class="frame-top"> </div>
			<div class="article-colum">
				<div class="pages-title"><%=webdata.getArticleTitle() %></div>
				<div class="pages-date">
					<span class="font"><%=webdata.getHeaderInfo() %></span> <%=webdata.getArticleCreateTime()==null?"":webdata.getArticleCreateTime().toString().substring(0,19)%>
					 <span class="font">来源： <%=webdata.getArticleSource() %></span>
				</div>
				<div class="pages_print"> 
				</div>
				<div class="pages_content">
					<div class="right" id="right">
			    		<div style="width: 750px; min-height:250px; background: white; padding-top: 39px; padding-bottom: 123px; padding-left: 28px; padding-right: 28px; line-height: 2em;">
			    			<%=webdata.getArticleContent() %>
			    		</div>
			    	</div>
		    		<div class="editor">责任编辑： <%=webdata.getArticleAuthorName() %> </div>	
				</div>
			</div>
			<div class="frame-bot"> </div>
		</div>
	</div>
	<%@ include file="bottom.jsp" %>

</body>
</html>