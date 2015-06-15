<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.consts.GlobalConsts"%>
<%@page import="antelope.utils.SystemOpts"%>
<%@page import="antelope.services.supportclasses.Header2Params"%>
<%@page import="antelope.services.JSPUtilService"%>
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
<%
	WebPageDataBean webdata = TemplateInfoService.getWebPageData(request);
%>
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

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=webdata.getPageTitle() %></title>

<base href="<%=basePath%><%=webdata.getTemplatepath()%>/">
<link rel="stylesheet" type="text/css" href="assets/index140516.css"/> 
<link rel="stylesheet" type="text/css" href="assets/master_cn_v1.0.css"/>
<link rel="stylesheet" type="text/css" href="assets/zc.css"/>
<link rel="stylesheet" type="text/css" href="assets/news.css"/>
<link rel="stylesheet" type="text/css" href="assets/tplgd.css"/>

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


#productlist {
	padding: 15px 10px;
}
#productlist li {
	text-align: center; float: left; display: inline;
}
#productlist li a.img {
	text-align: center; font-size: 0px; display: block;
}
#productlist li a.img img {
	margin: auto; border: 1px solid rgb(204, 204, 204);
}
#productlist li h3 {
	padding: 5px 0px 10px; line-height: 1.5; font-weight: normal;
}

</style>
 --%>

<script>
function change(str){
	if(str==1){
	//	document.getElementById("ifr").src = 'edu1.html';
	}else if(str==2){
		//	document.getElementById("ifr").src = 'http://www.sogou.com/';
	}else{
		//	document.getElementById("ifr").src = 'http://www.baidu.com/';
	}
}

$(function(){
	$("#metlist2").tilegrid({
		dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getProductListData.vot?catesid=<%=request.getParameter("sid")%>",
		numPerPage: 10,
		tileRenderer: function(gridinfo) {
			return '<LI class="list" \
			  style="width: 215px; margin-right: 10px; margin-left: 10px; float:left; height:232px; border:1px solid #cccccc; text-align:center;padding-top:5px; margin-top:10px;"><A title="' + this.name + '" \
				  class="img" href="product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=product&webpagepos=<%=request.getParameter("webpagepos")%>" \
				  target="_self"><IMG width="180" height="180" title="' + this.name + '" alt=" '+ this.name + '"\
				  src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=' + this.productimgsid + '" style=" width : 200px; height : 185px;"></A>\
				  <H3 style="text-align:center; font-size:14px;"><A title="' + this.name + '" href="http://www.metinfo.cn/demo/met093/300/show/show10.html" \
				  target="_self">' + this.name + '</A></H3></LI>';
		}
	});	
});

</script>

</head>


<body>
	<%@include file="header.jsp" %>
	
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
			
			<% if (webdata.getSubNavigationTree(2) != null) { 
						int sub_num = 0;
						boolean sub_flag = false;
						for (JSONObject submenu: webdata.getSubNavigationTree(2)) {
							sub_num++;	
			%>
				<div class="news_pics" id="sub_num<%=sub_num %>">
					<div class="pannel-title tit_s01">
						<div class="title-inner">
							<span class="bg01">&nbsp;</span>
							<span class="tabg"><a target="_blank" href="<%=basePath %><%=submenu.getString("href").replaceFirst("(&webpagepos=.*)", "&webpagepos=" + submenu.getString("pagesid"))%>"><%=submenu.getString("name") %></a></span>
							<span class="bg02">&nbsp;</span>
						</div>

 						<span class="more">
							<a target="_blank" href="<%=basePath %><%=submenu.getString("href").replaceFirst("(&webpagepos=.*)", "&webpagepos=" + submenu.getString("pagesid"))%>">更多&gt;&gt;</a>
						</span>
					</div>
					
					<div id="sub_num<%=sub_num %>div"  style="display:none">
						
					</div>
					
					<div class="picList02">
						<ul  id="sub_num<%=sub_num%>ul">
							
						</ul>
						<div class="clear">&nbsp;</div>
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
									$("#sub_num"+num).find("div[class='picList02']").last().remove();
									$("#sub_num"+num+"div").show();
									
									$("#sub_num"+num+"div").tilegrid({
										dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getProductListData.vot?catesid=<%=request.getParameter("sid")%>",
										numPerPage: 16,
										tileRenderer: function(gridinfo) {
											return 	'<div class="tplgd">\
														<div class="tplgd_ul">\
															<a href="<%=basePath%>product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=product&webpagepos=<%=request.getParameter("webpagepos")%>" target="_blank">\
																<img width="180" height="134" border="0" src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=' + this.productimgsid + '">\
															</a>\
														</div>\
														<div class="tplgd_wz">\
															<a href="<%=basePath%>product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=product&webpagepos=<%=request.getParameter("webpagepos")%>" target="_blank">'+this.name+'</a>\
														</div>\
														<div class="tplgd_sj"><span>'+this.createtime.substr(0,11)+'</span></div>\
													</div>';
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
									$("#sub_num"+i+"div").hide();
									$("#sub_num"+i+"ul").tilegrid({
										dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getProductListData.vot?catesid="+JSON.parse(jo.webpageproperty)[0].sid,
										numPerPage: 3,
										tileRenderer: function(gridinfo) {
											return '<li>\
														<div class="aImg" style="background:white;">\
														<div class="pannel-image">\
															<a target="_blank" href="<%=basePath%>product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=product&webpagepos='+catisidmap[this.catesid]+'">\
																<img width="300" height="225" border="0" src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=' + this.productimgsid + '">\
															</a>\
														</div>\
														<div class="subtitle">\
															<a target="_blank" href="<%=basePath%>product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=product&webpagepos='+catisidmap[this.catesid]+'">'+this.name+'</a>\
														</div>\
														<div class="dates">'+this.createtime.substr(0,11)+'</div>\
													</div>\
												</li>';
										}
									});	
									$("#sub_num"+i+"ul").children("div").last().remove();
								}
							})();
						</script>
			<%
					}
				}else{//首页链接进来
			%>
				<div class="news_pics" id="sub_num">
					<div class="pannel-title tit_s01">
						<div class="title-inner">
							<span class="bg01">&nbsp;</span>
							<span class="tabg"><a target="_blank" href="<%=basePath.substring(0,basePath.length()-1)+request.getServletPath()+"?"+request.getQueryString() %><%=webdata.getPageHref()%>"><%=webdata.getPageTitle() %></a></span>
							<span class="bg02">&nbsp;</span>
						</div>

 						<span class="more">
							<a target="_blank" href="<%=basePath.substring(0,basePath.length()-1)+request.getServletPath()+"?"+request.getQueryString() %><%=webdata.getPageHref()%>">更多&gt;&gt;</a>
						</span>
					</div>
					
					<div id="sub_numdiv">
						<script>
								(function(){
									$("#sub_numdiv").tilegrid({
										dataProvider:ctxPath + "/wcm/webpagedata/WebPageDataController/getProductListData.vot?catesid=<%=request.getParameter("sid")%>",
										numPerPage: 16,
										tileRenderer: function(gridinfo) {
											return 	'<div class="tplgd">\
														<div class="tplgd_ul">\
															<a href="<%=basePath%>product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=product&webpagepos=<%=request.getParameter("webpagepos")%>" target="_blank">\
																<img width="180" height="134" border="0" src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=' + this.productimgsid + '">\
															</a>\
														</div>\
														<div class="tplgd_wz">\
															<a href="<%=basePath%>product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=' + this.sid + '&type=product&webpagepos=<%=request.getParameter("webpagepos")%>" target="_blank">'+this.name+'</a>\
														</div>\
														<div class="tplgd_sj"><span>'+this.createtime.substr(0,11)+'</span></div>\
													</div>';
										}
									}); 
									//$("#sub_numul").children("div").last().remove();
								})();
							</script>
					</div>
				</div>	
			<% 		
				}
			%>
			</div>
			<div class="frame-bot"> </div>
		</div>
	</div>
	
	<%@ include file="bottom.jsp" %>
</body>


<%--
<body bgcolor="#ffffff" width="1200">
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
			<div style="float: left; width: 20px;text-align: center; margin-left: 2px; color: #666666;"> &gt; </div><A title="<%=menu.get("name") %>" style="float: left; margin-left: 0px;" href="<%=menu.get("href") %>"><%=menu.get("name") %></A>
			<%} %>
		</div>
	</div>
	<div class="content" style="background-color: #f9f9f9; background-image: url('<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/jiaoyubg.jpg'); background-repeat:  repeat-y;">
		<div class="left" style="height: 100%;">
        	<div class="left_sub" style="width: 196px;">
        		<div  style="border-bottom: 1px solid #e5e4e5;">
        			<% if (webdata.getSubNavigationTree(2) == null) { %>
	    			<a href="<%=webdata.getPageHref()%>"><%=webdata.getPageTitle() %>></a>
	    			<% } else { %>
	    				<% for (JSONObject submenu: webdata.getSubNavigationTree(2)) { %>
	    				   <a style="<%=webdata.getCheckedMenuStyle(submenu.getString("pagesid"), "color:#0085d2") %>" href="<%=submenu.getString("href").replaceFirst("(&webpagepos=.*)", "&webpagepos=" + submenu.getString("pagesid"))%>"><%=submenu.getString("name") %>></a>
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
    	<div style="clear:left;"></div>
	</div>
	<div class="foot">
    	<%=webdata.getFooterInfo() %>
	</div>
</div>
</body>
 --%>
</html>