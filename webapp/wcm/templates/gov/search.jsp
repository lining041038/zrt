<%@page import="antelope.services.supportclasses.Header2Params"%>
<%@page import="antelope.services.JSPUtilService"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.consts.GlobalConsts"%>
<%@page import="antelope.utils.SystemOpts"%>
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
	WebPageDataBean webdata = TemplateInfoService.getWebPageData(request);
	JSPUtilService jsputilservice = SpringUtils.getBean(JSPUtilService.class);
	Header2Params header2params = jsputilservice.initHeader2(request, response);
	String theme = header2params.theme;
	
	request.setCharacterEncoding("utf-8");
	
	String root = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+root+"/";
	
	String searchkey = request.getParameter("key");
	if("GET".equalsIgnoreCase(request.getMethod()))
	searchkey = new String(searchkey.getBytes("ISO-8859-1"),"utf-8");
	String timekey =  request.getParameter("t");
	String starttime = request.getParameter("mintime");
	String endtime = request.getParameter("maxtime");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/antelope/themes/base/base.css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/themes/<%=theme %>/jquery-ui-1.8.his.min.css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/themes/<%=theme %>/<%=theme %>.min.css">
		<script>var ctxPath = '${pageContext.request.contextPath}';</script>
		<script>var ctx = '${pageContext.request.contextPath}';</script>
		<script>var useAlertToShowValidate = <%=SystemOpts.getProperty("enable_alert_validate")%>;</script>
		<script>var tempsaveinterval = <%=GlobalConsts.tempAutoSaveInterval%>;</script>
		<script>var nestedbyform = <%=request.getParameter("nestedbyform")%>;</script>
		<script>var component = '<%=TextUtils.noNull(request.getParameter("component"))%>'</script>
		<script src="${pageContext.request.contextPath}/js/jquery-1.6.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery-i18n.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery-ui-1.8.17.custom.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/json2.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
		
		<title><%=searchkey %>-全文检索</title>
		
		<base href="<%=basePath%><%=webdata.getTemplatepath()%>/">
		<link rel="stylesheet" type="text/css" href="assets/index140516.css"/> 
		<link rel="stylesheet" type="text/css" href="assets/master_cn_v1.0.css"/> 
		<link rel="stylesheet" type="text/css" href="assets/search.css"/> 
		
		
		<script src="js/search.js"></script>
		<script type="text/javascript">
			var searchKey = '<%=searchkey%>';
			var timeKey = '<%=timekey%>';
			$(function(){
				$("#search_ul").tilegrid({
					dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getSearchContent.vot?"+$("#qform").serialize(),
					numPerPage: 15,
					tileRenderer: function(gridinfo) {
						return '<li class="res-list">\
									<h3 class="res-title"><a target="_blank" href="<%=basePath%>'+this.pageinfo+'?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type='+this.pagetype+'&webpagepos=search">'+this.name+'</a></h3>\
									<p class="res-sub">'+this.digest+'</p>\
									<p class="res-other"><span class="sp">发布时间：'+this.createtime.substr(0,11)+'</span></p>\
								</li>';
					}
				});
			});
		</script>
	
	</head>
<body>

	<%@include file="header.jsp" %>
		<div class="wrap">
			<div class="frame-pane">
				<div class="frame-top"> </div>
				<div class="news-pane">
					<div class="gov-left">
						<div class="searchbox clearfix">
			                <form class="search-form" method="get" action="" id="qform" target="_self">
			        	    	<div class="sim-select">
			        	    		<h3>本网站搜索</h3>
			        	    	</div> 
			
			                    <span class="round">
			                        <input autocomplete="off" maxlength="100" value="<%=searchkey %>" class="input-key search-q ac_input" tabindex="1" id="searchKey" name="key">
			                        <input name="t" value="<%=timekey %>" type="hidden"/>
			                        <input name="mintime" value="<%=starttime %>" type="hidden"/>
			                        <input name="maxtime" value="<%=endtime %>" type="hidden"/>
			                        <em id="delKey" class="del-key">删除</em>
			                    </span>
			                    
								<span class="sub_button">
									<input type="submit" class="ui-btn" value="搜&nbsp;&nbsp;索" id="su" >
								</span>
								
			                </form>
			            </div>
						
						<div class="result">
							<ul id="search_ul">
							<%--
								<%for(int i=0;i<8;i++){ %>
								<li class="res-list">
									<h3 class="res-title"><a target="_blank" href="http://www.gov.cn/xinwen/2014-06/09/content_2696864.htm">发展改革委谈实现十二五节能减排目标的三种路径和三种手段|访谈回放</a></h3>
									<p class="res-sub">发展改革委谈实现十二五节能减排目标的三种路径和三种手段|访谈回放发展改革委谈实现十二五节能减排目标的三种路径和三种手段|访谈回放</p>
									<p class="res-other"><span class="sp">发布时间：2014.06.09</span></p>
								</li>
								<%} %>
								 --%>
							</ul>
						</div>
					</div> 
					<div class="gov-right">
						<div class="search-nav-sort time-sort">
		                    <h2>时间</h2>
		                    <ul class="sort-list">
		                        <li class="cur"><a type="timeqb" class="tyl" target="_self">全部</a></li>
		                        <li><a type="timeyt" class="tyl" target="_self">一天内</a></li>
		                        <li><a type="timeyz" class="tyl" target="_self">一周内</a></li>
		                        <li><a type="timeyy" class="tyl" target="_self">一月内</a></li>
		                        <li><a type="timeyn" class="tyl" target="_self">一年内</a></li>
		                        <li><a type="timecs" id="special">指定日期</a></li>
		                        <li style="display:none" class="fixed-date" id="specialDate">
		                            <p>从：<input type="text" value="" class="date" id="fromDate" readonly="readonly"></p>
		                            <p>到：<input type="text" value="" class="date" id="toDate" readonly="readonly"></p>
		                            <p class="btn">
		                                <input type="button" class="ui-btn" value="搜索" id="rjs">
		                            </p>
		                        </li>
		                    </ul>
		                </div>
					</div>
				</div>
				<div class="frame-bot"> </div>
			</div>
		</div>	
		<%@include file="bottom.jsp" %>
</body>
</html>