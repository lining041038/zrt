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
<link rel="stylesheet" href="${pageContext.request.contextPath}/themes/<%=theme %>/jquery-ui-1.8.<%=theme %>.min.css">
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
<script src="${pageContext.request.contextPath}/js/jquery.ui.1.10.4.core.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.ui.1.10.4.button.min.js"></script>
<script src="${pageContext.request.contextPath}/js/json2.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>


<base href="<%=basePath%><%=webdata.getTemplatepath()%>/">
<link rel="stylesheet" type="text/css" href="assets/master_cn_v1.0.css"/>
<link rel="stylesheet" type="text/css" href="assets/zc.css"/>
<link rel="stylesheet" type="text/css" href="assets/index140516.css"/>
<link rel="stylesheet" type="text/css" href="assets/wxzls.css"/> 
 

<title><%=webdata.getPageTitle() %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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

</style>
--%>
</head>
<script>

function submitPeoplemsg() {
	
	if (!$("#messform").validate())
		return;
	
	
	 $.post(ctx + "/wcm/webpagedata/WebPageDataController/savePeoplemsg.vot", encodeURI($("#messform").serialize()), function(data){
		if (data) {
			alert(data);
		} else {
			alert("保存成功！");
		}
		$("#messform").resetForm();
	}); 
	
}


function resetPeoplemsg(){
	$("#messform").resetForm();
}

</script>
<body bgcolor="#ffffff" width="1200">
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
							<div class="kuandu">
								<div class="tabmenu">
									<ul>
										<li class="selected">
											<span class="bg01">&nbsp;</span> 
											<span class="tabg">网站留言</span> 
											<span class="bg02">&nbsp;</span>
										</li>
									</ul>
									<span class="more">&nbsp;</span>
								</div>
								<div class="news-left">
									 <div class="biaodan">
										<form id="messform"  method="post" >
											<input type="hidden" name="sitesid" value="<%=webdata.getSitesid() %>" default="<%=webdata.getSitesid() %>" />
											<div class="xiaju">
												<div class="bianz">
													<span>*</span> 昵称：
												</div>
												<div class="biany">
													<input type="text"  id="name" name="name" class="inputk" required2="true">
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>*</span> 姓名：
												</div>
												<div class="biany">
													<input type="text" id="username" name="username" class="inputk" required2="true">
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>*</span> 身份证号码：
												</div>
												<div class="biany">
													<input type="text" id="identityCard" name="identityCard" class="inputk" required2="true" int="true">
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>*</span> 联系电话：
												</div>
												<div class="biany">
													<input type="text" id="phonenum"  name="phonenum" class="inputk" required2="true" maxlength="12" minlength="7" int="true">
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>*</span> 电子邮件：
												</div>
												<div class="biany">
													<input type="text"  id="email" name="email" class="inputk" required2="true">
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>*</span> 主题：
												</div>
												<div class="biany">
													<select  id="topic" name="topic" required2="true">
														<option value="">请选择...</option>
														<option value="表达感情">表达感情</option>
														<option value="意见建议">意见建议</option>
														<option value="政策咨询">政策咨询</option>
														<option value="利益诉求">利益诉求</option>
														<option value="揭发控告">揭发控告</option>
														<option value="突发事件">突发事件</option>
													</select>
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>&nbsp;</span> 职业：
												</div>
												<div class="biany">
													<select id="profession" name="profession">
														<option value="工人">工人</option>
														<option value="农民">农民</option>
														<option value="学生">学生</option>
														<option value="教师">教师</option>
														<option value="医生">医生</option>
														<option value="律师">律师</option>
														<option value="公务员">公务员</option>
														<option value="事业单位工作人员">事业单位工作人员</option>
														<option value="专业技术人员">专业技术人员</option>
														<option value="企业管理人员">企业管理人员</option>
														<option value="文体人员">文体人员</option>
														<option value="现役军人">现役军人</option>
														<option value="自由职业者">自由职业者</option>
														<option value="个体经营者">个体经营者</option>
														<option value="无业人员">无业人员</option>
														<option value="退（离）休人员">退（离）休人员</option>
														<option value="其他">其他</option>
													</select>
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>&nbsp;</span> 通讯地址：
												</div>
												<div class="biany">
													<input type="text" id="address" name="address" class="inputc">
												</div>
											</div>
											<div class="xiaju">
												<div class="bianz">
													<span>*</span> 标题：
												</div>
												<div class="biany">
													<input type="text"  id="title" name="title" class="inputc" required2="true"> 
												</div>
											</div>
											<div class="xiaju2">
												<div class="bianz">
													<span>*</span> 留言内容：
												</div>
												<div class="biany">
													<textarea  id="content" name="content" maxlength="200"  rows="3" cols="10" class="inputc2" required2="true"></textarea>
												</div>
											</div>
										<%--
											<div class="xiaju">
												<div class="gao left">
													<div class="bianz">
														<span>*</span> 验证码：
													</div>
													<div class="biany">
														<input type="text" onblur="javascript:c_valiedCode()" maxlength="4" id="validate" name="validate"  class="inputk2">
													</div>
												</div>
												<div class="left" id="imgleft">
													<img height="30px" width="60px" src="<%=request.getContextPath() %>/common/SystemController/getVerifycodeImage.vot" onclick="changeVerifyCodeImage()"/><span style="cursor: pointer;" onclick="changeVerifyCodeImage()">看不清，换一个</span>
													<script>
														function changeVerifyCodeImage(){
															$("#imgleft").find("img").attr("src","<%=request.getContextPath() %>/common/SystemController/getVerifycodeImage.vot?r="+Math.random());
														}
													</script>
												</div>
											</div>
										--%>
											<div class="xiaju3">
												<input type="button" onclick="submitPeoplemsg()"  name="Submit" value="提交" class="sub">
												<input type="button" onclick="resetPeoplemsg()"  value="重置信息" class="res">
											</div>
										</form>
									 </div> 
								</div>
								<div class="tupian"> </div>
							</div>
							<div class="news-right">
								<div class="zli-right">
									<div class="art_bg">
										<p>欢迎您的到来。</p>
										<p>这里是中国政府网为大家开通的信息渠道，是进一步听取您对政府工作意见建议的网络窗口。</p>
										<p>民之所望，施政所向。政府工作离不开大家的宝贵意见。我们将努力办好这一栏目，以帮助中央政府更深入了解社情民意，履行法定职责，更好服务人民。</p>
										<p>您的每一条意见，都将被认真对待。我们会将大家的意见建议汇总整理，并进行专门研究。一些好的意见建议将被直接送到总理的办公桌上。请您珍惜并认真行使这一权利。</p>
										<p>您的意见建议可能被转给各有关方面，并可能以不同的方式向社会公开。如果您希望保留个人隐私，请在留言的同时做出声明，我们将充分尊重您的意愿并妥善处理。</p>
										<p>您看到的，是政府应当看见的；您听到的，是政府需要倾听的；您思考的，是政府希望了解的；您期盼的，是政府为之奋斗的。</p>
										<p>感谢您的支持和参与。您有真知灼见，我们天天在线。</p>
									</div>
									<div class="hr_20">&nbsp;</div>
								</div>
							</div>
							<div class="clear"> </div>
						</div>
					</div>
				<div class="frame-bot"> </div>
			</div>
		</div>
	<!-- 
	</div>
 -->
<!-- -----------以下作为参考------------- -->
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
    			<form name="messform" id="messform" method="post">
					<div id="mess_main">
					<div class="mess_list">
						<input type="hidden" name="sitesid" value="<%=webdata.getSitesid() %>" default="<%=webdata.getSitesid() %>" />
						<div class="mess_title">昵称</div><div class="mess_input"><input required2="true" type="text" name="name" id="mess_username_" value=""  /></div>
					 </div>
					 <div class="mess_list">
						<div class="mess_title">电子邮件</div><divclass="mess_input"><input email="true"  type="text" name="email" id="mess_email_" value=""  /></div>
					<div class="mess_list">
						<div class="mess_title">电话</div><div class="mess_input"><input  int="true" maxlength2="11" type="text" name="phonenum" id="mess_tele_" value=""  /></div>
					</div>
					<div class="mess_list">
						<div class="mess_title">正文</div><div class="mess_textarea"><textarea maxlength2="500" required2="true" name="content" id="mess_message_" rows="6" cols="36"></textarea></div>
					</div>
					<div class="mess_list">
					<br/>
					<div class="mess_submit">
						<input name="sub" onclick="submitPeoplemsg()" type="button" class="button" value="提交" /></div>
					</div>
					<div class="message_bg"></div>
					</div>
					<!--mess_main end-->
					<div class="list_bot"></div>
				</form>
    		</div>
    	</div>
    	<div style="clear:left;"></div>
	</div>
	<div class="foot">
    	<%=webdata.getFooterInfo() %>
	</div>
</div> --%>
</body>
</html>