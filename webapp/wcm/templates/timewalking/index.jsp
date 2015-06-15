<%@page import="antelope.wcm.beans.WebPageDataBean"%>
<%@page import="antelope.utils.JSONArray"%>
<%@page import="antelope.wcm.consts.WCMSiteSettingConsts"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.utils.JSONObject"%>
<%@page import="antelope.wcm.services.TemplateInfoService"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.SessionService"%>
<%@ page language="java" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html><head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<% WebPageDataBean webdata = TemplateInfoService.getWebPageData(request); %>
<title>时光漫步首页</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
body {
	padding: 0;
	margin: 0;
	background: #ebebeb;
	font-size:12px;
}
img {border: 0;}
ul {margin: 0;}

#main {
	max-width: 1200px;
	margin: 0 auto;
}

ul {
	list-style: none outside none;
}

li {
	float: left;
	width: auto;
	height: auto;
}

a {
	text-decoration: none;
}

.content {
	margin: 0;
	width: 100%;
	min-width: 1200px;
	_width: expression(( document.documentElement.clientWidth ||
		document.body.clientWidth)>1300?"auto":"1200px" );
}

.contentin {
	height: 170px;
	padding-top: 20px;
	margin:0;
	width: 100%;
}

.mes {
	text-indent: 2em;
	text-align: left;
	line-height: 150%;
}

.mes a {
	color: #999;
	font-size: 12px;
}

.mesli{
	padding: 15px 20px 0 20px;
}
.mesli a {
	text-indent: 2em;
	color: #999;
	font-size: 12px;
	font-style: normal;
}

.mesli li {
	width: 350px;
	line-height: 150%;
}

.mesli ul {
	list-style: disc;
}

.product-img {
	padding-right: 10px;
}

.navbar {
	width: 100%;
	min-width: 1200px;
	_width: expression(( document.documentElement.clientWidth ||
		document.body.clientWidth)>1300?"auto":"1200px" );
	background-image: url(<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/menubg.png);
	border: 0;
	height: 52px;
	text-align: left;
	font-size: 20px;
	font-weight: bold;
	font-color: white;
}

.navbarul {
	z-index: 1;
}

.navbar a {
	display: block;
	height: 35px;
	color: white;
	float: left;
	width: 165px;
	height: 52px;
	text-align: center;
	line-height: 52px;
}

.navbar a:hover {
	color: #fff;
	background:#39F;
}

.products {
	width: 1200px;
	padding-top: 0px;
	margin: 0;
}

.product {
	width: 100%;
	min-width: 1200px;
	_width: expression(( document.documentElement.clientWidth ||
		document.body.clientWidth)>1300?"auto":"1200px" );
}

/************************/
.linkcss1 {
	font-family: Arial;
	font-size: 15px;
	color: #0984c7;
	font-weight: bold;
	display: inline-block;
	margin:0;
}

.linkcss2 {
	font-family: Arial;
	font-size: 10px;
	color: #d0d0d0;
	font-weight: bold;
	display: inline-block;
	margin-left: 2px;
}

.linkcss3 {
	font-family: Arial;
	font-size: 12px;
	color: #0984c7;
	font-weight: bold;
	display: inline-block;
	margin:0;
}

.linklinecss1{
	background:#0984c7;
	width:auto;
	height:2px;
	position: relative;
}

.linkimagecss1{
	width: 10px;
	height: 10px; 
	position: absolute;
	left:25px; 
	top:-4px;
	border:0;
}


.headerdiv1{
	height:86px;
	width:1200px;
}

.headerdiv2{
	position:relative;
	height:86px;
	width:1000px;
	margin-left:100px;
	border-bottom: 1px #f9fcfd dashed;
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
	right:0px;
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
	color:#999999;
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


/** 新css  */
#header{background-image: url("<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/bannerbg.jpg"); }
.mesli2 li a{text-indent: 0; line-height: 2em; color: #666666;}
.mesli.mesli2{padding-left: 0;}
a.desca{line-height: 2em; color: #666666;}
img{border:none;}
</style>

</head>
<body bgcolor="#ffffff" width="1200">
	<div id="main">
		<div id="header">
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
			<div class="head">
				<img style="background: none;" src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=webdata.getBannerSid()%>"/>
			</div>
		</div>
		<div class="navbar" style="text-align: center;">
			<div style="margin-left: 100px; border-left:1px solid #006dac;">
			 <% for (JSONObject onemenu : webdata.getNavigationTree()) { %>
			   <a href="<%=onemenu.get("href")%>" style="<%=webdata.getCheckedMenuStyle(onemenu.getString("pagesid"), "background-color:#00a3d2") %> ; border-left: 1px solid #2d9ad2; border-right: 1px solid #006dac; margin-top: 2px; height: 50px;"><%=onemenu.get("name") %></a>
			  <%} %>
		  	<div style="clear: left;"></div>
		  	</div>
		</div>
		<div class="content" style="background: white;">
			<ul class="contentin">
				<li style="padding-left: 60px; width: 330px; float: left;">
					<% for (JSONObject portlet : webdata.getLeftPortlets()) { %>
						<div class="imgbox" style="margin-top: 10px;">
							<a href="#" title="关于我们">
								<p class="linkcss1" style="margin-bottom: 10px;"><%=portlet.get("title") %></p>
								<div class="linklinecss1">
									<img  class="linkimagecss1" src="<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/bluecircle.png"/>
								</div>
							</a>
						</div>
						
						<%if ("4".equals(portlet.getString("portlettype"))) {// 摘要 %>
						<div class="mes">
							<p class="desc" style="margin-top: 15px; width: 305px;">
								<a class="desca" title="介绍" href="#"><%=portlet.getString("digest") %></a>
								<a href="article.jsp?sitesid=<%=webdata.getSitesid()%>&type=article&sid=<%=portlet.getString("articlesid") %>&webpagepos=index" style="color: #33F">>>了解更多</a>
							</p>
						</div>
						<%} else { %>
							<div class="mesli mesli2">
								<ul>
									<%	for (JSONObject prodarticle: portlet.getJSONArray2("portletcontent_values")) {  %>
									<li><a href="<%=prodarticle.get("href")%>"><%=prodarticle.get("name") %></a></li>
								 	<%	}%>
								</ul>
							</div>
						<%} %>
					<% } %>
				</li>
				<li style="width: 359px;  float: left;">
						<% for (JSONObject portlet : webdata.getCenterPortlets()) { %>
						<div class="imgbox" style="margin-top: 10px;">
							<a href="#" title="<%=portlet.get("title") %>">
								<p class="linkcss1" style="margin-bottom: 10px;"><%=portlet.get("title") %></p>
								<div class="linklinecss1">
									<img  class="linkimagecss1" src="<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/bluecircle.png"/>
								</div>
							</a>
						</div>
						
						<%if ("4".equals(portlet.getString("portlettype"))) {// 摘要 %>
						<div class="mes">
							<p class="desc" style="margin-top: 15px; width: 305px;">
								<a title="介绍" href="#"><%=portlet.getString("digest") %></a>
								<a href="article.jsp?sitesid=<%=webdata.getSitesid()%>&type=article&sid=<%=portlet.getString("articlesid") %>&webpagepos=index" style="color: #33F">>>了解更多</a>
							</p>
						</div>
						<%} else { %>
							<div class="mesli mesli2">
								<ul>
									<%	for (JSONObject prodarticle: portlet.getJSONArray2("portletcontent_values")) {  %>
									<li><a href="<%=prodarticle.get("href")%>"><%=prodarticle.get("name") %></a></li>
								 	<%	}%>
								</ul>
							</div>
						<% } %>
					<% } %>
				</li>
				<li style="width: 359px;  float: left;">
					<% for (JSONObject portlet : webdata.getRightPortlets()) { %>
						<div class="imgbox" style="margin-top: 10px;">
							<a href="#" title="<%=portlet.get("title") %>">
								<p class="linkcss1" style="margin-bottom: 10px;"><%=portlet.get("title") %></p>
								<div class="linklinecss1">
									<img  class="linkimagecss1" src="<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/bluecircle.png"/>
								</div>
							</a>
						</div>
						<%if ("4".equals(portlet.getString("portlettype"))) {// 摘要 %>
						<div class="mes">
							<p class="desc" style="margin-top: 15px; width: 305px;">
								<a title="介绍" href="#"><%=portlet.getString("digest") %></a>
								<a href="article.jsp?sitesid=<%=webdata.getSitesid()%>&type=article&sid=<%=portlet.getString("articlesid") %>&webpagepos=index" style="color: #33F">>>了解更多</a>
							</p>
						</div>
						<%} else { %>
							<div class="mesli mesli2">
								<ul>
									<%	for (JSONObject prodarticle: portlet.getJSONArray2("portletcontent_values")) {  %>
									<li><a href="<%=prodarticle.get("href")%>"><%=prodarticle.get("name") %></a></li>
								 	<%	}%>
								</ul>
							</div>
						<% } %>
					<% } %>
				</li>
			</ul>
		</div>
		<div class="products" style="background: white;">
			<% for (JSONObject portlet : webdata.getBottomPortlets()) { %>
			<ul class="product" id="product" style="height: 170px; margin: 0;">
				<li style="width:50px; height: 100px;"></li>
				 <%	for (JSONObject proproduct: portlet.getJSONArray2("portletcontent_values")) {%>
					<li style="width: 260px;"><div
						class="imgbox" style="position: relative; background: white;">
						<a href="product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=<%=proproduct.getString("sid") %>&type=product&webpagepos=<%=request.getParameter("webpagepos")%>" class="product-img" ><img name="index3_r4_c2"
							src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=proproduct.getString("productimgsid") %>" width="248px" height="153px"
							border="0" alt="">
						</a>
						<div style="position: absolute; bottom: 0; height: 39px; width:254px; background: url('<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/indeximgbg.png');">
							<div style="text-align: left; color: white; padding: 13px 55px;"><a style="color: white;" href="product.jsp?sitesid=<%=webdata.getSitesid()%>&sid=<%=proproduct.getString("sid") %>&type=product&webpagepos=<%=request.getParameter("webpagepos")%>"><%=proproduct.getString("name") %></a></div>
						</div>
					</div></li>
				 <%} %>
			</ul>
			<%} %>
		</div>
		<div class="foot">
			<%=webdata.getFooterInfo() %>
		</div>
	</div>
</body>
</html>
