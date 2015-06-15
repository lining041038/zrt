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
	String root = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+root+"/";
%>
<% WebPageDataBean webdata = TemplateInfoService.getWebPageData(request); %>
<!DOCTYPE html>
<html><head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<%
	JSPUtilService jsputilservice = SpringUtils.getBean(JSPUtilService.class);
	Header2Params header2params = jsputilservice.initHeader2(request, response);
	String theme = header2params.theme;
%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/antelope/themes/base/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/themes/<%=theme %>/<%=theme %>.min.css">
<script>var ctxPath = '${pageContext.request.contextPath}';</script>
<script>var ctx = '${pageContext.request.contextPath}';</script>
<script>var useAlertToShowValidate = <%=SystemOpts.getProperty("enable_alert_validate")%>;</script>
<script>var tempsaveinterval = <%=GlobalConsts.tempAutoSaveInterval%>;</script>
<script>var nestedbyform = <%=request.getParameter("nestedbyform")%>;</script>
<script>var component = '<%=TextUtils.noNull(request.getParameter("component"))%>'</script>
<script src="${pageContext.request.contextPath}/js/jquery-1.6.2.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-i18n.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.ui.1.10.4.widget.min.js"></script>
<script src="${pageContext.request.contextPath}/js/json2.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>


<title><%=webdata.getPageTitle() %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<base href="<%=basePath%><%=webdata.getTemplatepath()%>/">
<link rel="stylesheet" type="text/css" href="assets/index140516.css"/> 
<link rel="stylesheet" type="text/css" href="assets/master_cn_v1.0.css"/>
<link rel="stylesheet" type="text/css" href="assets/zc.css"/>
<%-- 
<style>
	body {
	padding: 0;
	margin: 0;
	background: #ebebeb;
	font-size:12px;
}
img {border: 0;}
ul {margin: 0;}

#main{
	width:1200px;
   	max-width: 1200px;
  	margin: 0 auto; 
}
ul{
    list-style: none outside none;
}
a{
	text-decoration:none; 
}
.navbar{
	width:100%;
    min-width:1200px;
    _width:expression((document.documentElement.clientWidth||document.body.clientWidth)>1300?"auto":"1200px");
    background-image: url(<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/menubg.png);
	border:0;
	height:52px;
	text-align:left;
	font-size:20px;
	font-weight:bold;
	font-color:white;
}
.navbarul{
	z-index:1;
}

.navbar a{	
	display:block;
	height:35px; 
	color:white;
	float:left;
	width:165px; 
	height: 52px;	
	text-align:center;
	line-height:52px;
}
.navbar a:hover{
	color:#fff;
	background:#39F;
}
.navbars{
	background-image:url(<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/bnht.jpg);
	height:50px;
	width:1200px;
	margin-top:0px;
}

.navbars a{float: left; margin-left: 50px;}

.sub{
	font-size:14px;
	margin: 17px 110px 0 0;
	float: right;
	text-align: right;
	height: 22px;
}
.sub a{
	color: #999;
}
.content{
	width:1200px;
	height:auto;
}
.left{
	width:296px;
	float:left;
}
.left_sub{
	font-size:14px;
	margin: 0px 800px 0 100px;
	width: 70px;
	text-align:left;
}
.left_sub a{
	display:block;
	height:35px; 
	float:left;
	width:165px; 
	height: 50px;	
	text-align:center;
	line-height:50px;
	color:gray;
	font-weight:bold;
}
.left_sub a:hover{
	color:#00F;
}
.right{
	float:left;
	width:900px;
}
.headerdiv1{
	height:86px;
	width:1200px;
	background: url(<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/headerbg.png);
	position: relative;
}

.headerdiv2{
	height:86px;
	width:970px;
	margin-left:100px;
}

.headerdiv1 span{
	color:#FFFFFF;
	font-family: Arial;
}

.headerspan1{
	margin-top:20px;
	position: absolute;
	top:9px;
	left:100px;
}

.headerspan2{
	font-size: 24px;
	position: absolute;
	left:159px;
	top:30px;
}

.headerspan3{
	font-size: 10px;
	position: absolute;
	left:159px;
	top:60px;
}

.headerspan4{
	position: absolute;
	top:50px;
	left:400px;
	font-size:17px;
}

.headerspan5{
	position: absolute;
	top:50px;
	left:760px;
	font-size:15px;
}

.headerspan6{
	position: absolute;
	top:54px;
	font-size:11px;
	left:980px;
	font-size:15px;
}
.foot{
	background:#3b3b3b;
	width:1200px;
	height:220px;
}

.footdivbottom{
	width:100%;
	text-align: center;
	margin-top:10px;
}

.footspan1{
	color:#FFFFFF;
	font-family: Arial;
	font-size: 16px;
	font-weight: bold;
}

.footspan2{
	color:#7a7a7a;
	font-family: Arial;
	font-size: 13px;
}

.footdivcenter{
	width:970px;
	height:150px;
	margin-left:140px;
	border-bottom: 1px #7a7a7a dashed;
	position: relative;
	left:20px;
	border:0;
}

.footdivcenter div{
	display:inline-block;
	width:200px;
	height:100px;
	margin-top:40px;
	padding-left:30px;
	border-right:1px #7a7a7a solid;
}

.footdivcenter .footcenterlast{
	width:210px;
	border-right:0px ;
}

.footdivcenter div span{
	display: block;
	line-height: 20px;
}


#newslist {
	padding: 15px 20px;
}
#newslist li {
	background: url("<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/dt-0.gif") no-repeat left 19px; padding: 6px 10px;
}
#newslist li a {
	font-size: 14px; margin-right: 5px;
}
#newslist li span {
	color: rgb(170, 170, 170); float: right;
}
#newslist li p {
	color: rgb(170, 170, 170);
}
</style> --%>
</head>
<body>
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
			<div class="news-pane">
				<div class="news-colum">
					<div class="news-left">
					<% if (webdata.getSubNavigationTree(2) != null) { 
						int sub_num = 0;
						boolean sub_flag = false;
						for (JSONObject submenu: webdata.getSubNavigationTree(2)) {
							sub_num++;	
						%>
						<div class="yj_main" id="sub_num<%=sub_num%>">
							<div class="pannel-title tit_s01">
								<div class="title-inner">
									<span class="bg01">&nbsp;</span>
									<span class="tabg"><a target="_blank" href="<%=basePath %><%=submenu.getString("href").replaceFirst("(&webpagepos=.*)", "&webpagepos=" + submenu.getString("pagesid"))%>"><%=submenu.getString("name") %></a></span>
									<span class="bg02">&nbsp;</span>
								</div>
								<span class="more"><a target="_blank" href="<%=basePath %><%=submenu.getString("href").replaceFirst("(&webpagepos=.*)", "&webpagepos=" + submenu.getString("pagesid"))%>">更多&gt;&gt;</a></span>
							</div>
							<div class="hr_10"> </div>
							<div class="pubListBox01">
								<ul class="list01">
									<ul class="noindent" id="sub_num<%=sub_num%>ul">
										
									</ul>
								</ul>
							</div>
						</div>
						<%
							if("true".equalsIgnoreCase(webdata.getCheckedMenuStyle(submenu.getString("pagesid"), "true")) == true){//单个列表
								sub_flag = true;
						%>
							<script>
								(function(){
									var num = <%=sub_num%>
									for(var i = 1;i<num;i++){
										$("#sub_num"+i).remove();
									}
									$("#sub_num"+num+"ul").tilegrid({
										dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getArticleListData.vot?catesid=<%=request.getParameter("sid")%>",
										numPerPage: 10,
										tileRenderer: function(gridinfo) {
											return '<li>\
														<a target="_blank" href="<%=basePath%>article.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=article&webpagepos=<%=request.getParameter("webpagepos")%>">'+this.name+'</a>\
														<span>'+this.createtime.substr(0,11)+'</span>\
													</li>'
										}
									});
									//$("#sub_num"+num+"ul").children("div").last().remove();
								})();
							</script>
						<%	
									break;
							     }
						 	}
							if(!sub_flag){//全部列表
								JSONArray	sub_webdata  = webdata.getSubNavigationTree(2);
						%>
								<script>
								
									(function(){
										var num = <%=sub_num%>;
										var ja = <%=sub_webdata%>;
										var catisidmap = [];
										
										for(var i = 1;i<=num;i++){
											var jo = ja[i-1];
											catisidmap[JSON.parse(jo.webpageproperty)[0].sid] = jo.sid;
											$("#sub_num"+i+"ul").tilegrid({
												dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getArticleListData.vot?catesid="+JSON.parse(jo.webpageproperty)[0].sid,
												numPerPage: 6,
												tileRenderer: function(gridinfo) {
													return '<li>\
																<a target="_blank" href="<%=basePath%>article.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=article&webpagepos='+catisidmap[this.catesid]+'">'+this.name+'</a>\
																<span>'+this.createtime.substr(0,11)+'</span>\
															</li>';
												}
											});
											$("#sub_num"+i+"ul").children("div").last().remove();
										}
									})();
								</script>
						 	<%	
							}
						
						%>
					<% 
					}else{//首页链接进来
					%>
					<div class="yj_main" id="sub_num">
							<div class="pannel-title tit_s01">
								<div class="title-inner">
									<span class="bg01">&nbsp;</span>
									<span class="tabg"><a target="_blank" href="<%=basePath.substring(0,basePath.length()-1)+request.getServletPath()+"?"+request.getQueryString() %><%=webdata.getPageHref()%>"><%=webdata.getPageTitle() %></a></span>
									<span class="bg02">&nbsp;</span>
								</div>
								<span class="more"><a target="_blank" href="<%=basePath.substring(0,basePath.length()-1)+request.getServletPath()+"?"+request.getQueryString() %><%=webdata.getPageHref()%>">更多&gt;&gt;</a></span>
							</div>
							<div class="hr_10"> </div>
							<div class="pubListBox01">
								<ul class="list01">
									<ul class="noindent" id="sub_numul">
										<script>
											(function(){
												$("#sub_numul").tilegrid({
													dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getArticleListData.vot?catesid=<%=request.getParameter("sid")%>",
													numPerPage: 10,
													tileRenderer: function(gridinfo) {
														return '<li>\
																	<a target="_blank" href="<%=basePath%>article.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=article&webpagepos=<%=request.getParameter("webpagepos")%>">'+this.name+'</a>\
																	<span>'+this.createtime.substr(0,11)+'</span>\
																</li>'
													}
												});
											})();
										</script>
									</ul>
								</ul>
							</div>
						</div>
					<%} %>
					</div>
				</div>
			</div>
			<div class="frame-bot"> </div>
		</div>
	</div>
	<%@ include file="bottom.jsp" %>
	
<%-- 
<div id="main">
	<div class="head">
    	<div class="headerdiv1">
        	<div class="headerdiv2">
				<div style="padding-top: 32px;">
					<a href="index.jsp?sitesid=<%=webdata.getSitesid()%>">
						<img src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=webdata.getLogoImgSid()%>"/>
					</a>								
				</div>
				<span class="headerspan6">
					<%=webdata.getHeaderInfo() %>
				</span>
			</div>
        </div>
	</div>
	<div class="navbar">
		<div style="margin-left: 100px; border-left:1px solid #006dac;">
    	 <% for (JSONObject onemenu : webdata.getNavigationTree()) { %>
			   <a href="<%=onemenu.get("href")%>" style="<%=webdata.getCheckedMenuStyle(onemenu.getString("pagesid"), "background-color:#00a3d2") %> ; border-left: 1px solid #2d9ad2; border-right: 1px solid #006dac; margin-top: 2px; height: 50px;"><%=onemenu.get("name") %></a>
			  <%} %>
		  <div style="clear: left;"></div>
		</div>
	</div>
    <div class="header2">
    	<div class="head2">
   	   		<img  src="<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/nav.jpg" width="1200px" height="316px" border="0" alt="">
        </div>
	</div>
	<div class="navbars" style="border-bottom: 1px solid #e0dee0;">
		<div style="float: left; position: relative; left: 150px; top: 15px; font-size: 16px; color: white; font-weight: bold;"><%=webdata.getPageTitle(1)%></div>
    	<div class="sub">
    		<A title="时光漫步" style="float: left;" href="index.jsp?sitesid=<%=webdata.getSitesid()%>">时光漫步</A> 
			<%for (JSONObject menu: webdata.getBreadcrumbs()) { %>
			SSSSS<div style="float: left; width: 20px;text-align: center; margin-left: 2px; color: #666666;"> &gt; </div><A title="<%=menu.get("name") %>" style="float: left; margin-left: 0px;" href="<%=menu.get("href") %>"><%=menu.get("name") %></A>
			<%} %>
		</div>
	</div>
	<div class="content" style="background-color: #f9f9f9; background-image: url('<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/jiaoyubg.jpg'); background-repeat:  repeat-y;">
		<div class="left" style="height: 100%;">
        	<div class="left_sub" style="width: 196px;">
        		<div  style="border-bottom: 1px solid #e5e4e5;">
        			<% if (webdata.getSubNavigationTree(2) == null) { %>
	    			@@@@@<a  href="<%=webdata.getPageHref()%>"><%=webdata.getPageTitle() %>></a>dddddddddddddddddddddddddddd
	    			<% } else { %>
	    				<% for (JSONObject submenu: webdata.getSubNavigationTree(2)) { %>
	    				  ##### <a style="<%=webdata.getCheckedMenuStyle(submenu.getString("pagesid"), "color:#0085d2") %>" href="<%=submenu.getString("href").replaceFirst("(&webpagepos=.*)", "&webpagepos=" + submenu.getString("pagesid"))%>"><%=submenu.getString("name") %>></a>
	    				 <%} %>
	    			<% }%>
	    			 <div style="clear:both;"></div>
	    		</div>
		        <div style="clear:both;"></div>
        	</div>
    	</div>
    	<div class="right" id="right">
    		<div style="min-height:400px; width: 750px; background: white; padding-top: 39px; padding-bottom: 123px; padding-left: 28px; padding-right: 28px; line-height: 2em;">
    			<DIV class="active" id="newslist">
					<UL class="list-none metlist" id="metlist2" style="width: 717px;">
					</UL>
				</DIV>
    		</div>
    	</div>
	</div>
</div> 
--%>
</body>
</html>