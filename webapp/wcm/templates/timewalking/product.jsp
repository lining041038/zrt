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
%>
<!DOCTYPE html>
<html><head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title><%=webdata.getPageTitle() %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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

</style>
</head>
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
</script>
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
    		<div style="width: 750px; min-height:250px; background: white; padding-top: 39px; padding-bottom: 123px; padding-left: 28px; padding-right: 28px; line-height: 2em;">
    			
    			<STYLE>.spic{margin-right:5px;}
				.spic a img{-moz-opacity:0.5; filter:alpha(opacity=50);border:0px;}
				.spic a:hover{font-size:9px;}
				.spic a:hover img{-moz-opacity:0.5; filter:alpha(opacity=100);cursor:hand;}
				#view_bigimg{ display:block; margin:0px auto; font-size:0px;}
				.smallimg{ margin-top:5px;}
				</STYLE>
				   
				<SCRIPT language="JavaScript">
				function metseeBig(nowimg,mgrc) {
				document.getElementById('view_img').src=document.getElementById(nowimg).src;
				$('#view_bigimg').attr('href',mgrc);
				}
				</SCRIPT>
				   <SPAN class="info_img" id="imgqwe"><A title="查看大图" id="view_bigimg" 
				   href="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=webdata.getProductImgSid() %>" 
				  target="_blank"><IMG width="380" height="350" title="牛黄消炎片" id="view_img" alt="牛黄消炎片" 
				  src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=webdata.getProductImgSid() %>" 
				  border="0"></A></SPAN> 
				<SCRIPT type="text/javascript">var zoomImagesURI   = '../public/assets/zoom/';</SCRIPT>
				   
				<SCRIPT language="JavaScript" src="<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/metzoom.js" type="text/javascript"></SCRIPT>
				   
				<SCRIPT language="JavaScript" src="<%=request.getContextPath() + webdata.getTemplatepath()%>/assets/metzoomHTML.js" type="text/javascript"></SCRIPT>
				   
				   
				  <DIV class="smallimg" style="width: 380px;">
				  
				  <% for (JSONObject sidobj : webdata.getProductImgSids()) { %>
				  <SPAN class="spic"><A title="<%=webdata.getProductName() %>" 
				  style="cursor: pointer;" onclick="metseeBig('smallimg1<%=sidobj.get("sid")%>','<%=request.getContextPath()%>/upload/UploadController/getSingleImageData.vot?imagesid=<%=sidobj.get("sid")%>')" 
				  href="javascript:;"><IMG width="50" height="50" title="牛黄消炎片" id="smallimg1<%=sidobj.get("sid")%>" 
				  alt="<%=webdata.getProductName() %>" src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageData.vot?imagesid=<%=sidobj.get("sid")%>" border="0"></A></SPAN>
				  <%} %>
				   
				  </DIV>
    			
    			<div style="font-size: 16px; font-weight: bold; text-align: center;"><%=webdata.getProductName() %></div>
    			<%=webdata.getProductContent() %>
    		</div>
    	</div>
    	<div style="clear:left;"></div>
	</div>
	<div class="foot">
    	<%=webdata.getFooterInfo() %>
	</div>
</div>
</body>
</html>