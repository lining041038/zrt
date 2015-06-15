<%@ page language="java" pageEncoding="utf-8"%>
<%-- 
<div id="IndexPage" style="background:url('<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=webdata.getLogoImgSid()%>') no-repeat center 30px;">
--%>
<div id="IndexPage" style="background:url('assets/images/index_bg.png') no-repeat center 30px;">
 		<div class="spe-top">
			<div class="main-colum">
				<div class="language">
					<a href="javascript:void(0);"
						onclick="return j2gb('http://www.gov.cn');">简体</a> | <a
						href="http://big5.gov.cn/" target="_blank">繁体</a> | <a
						href="http://english.gov.cn/" target="_blank">English</a>
				</div>

				<div class="topDate">
					<script type="text/javascript" language="JavaScript" src="js/gov_dates.js"></script><br>
				</div>

				<div class="oldLink" style="width: 200px;">
					<a href="#" target="_blank">公务邮箱</a> | <a href="#" target="_blank">旧版回顾</a>
				</div>

				<div class="clear">&nbsp;</div>
			</div>
		</div>
		
		<div class="public_imginfo">
			<div class="headerImg">
				<img src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=webdata.getLogoImgSid()%>"/>
			</div>
			<div class="headerInfo">
				<%=webdata.getHeaderInfo() %>
			</div>
		</div>

		<div class="public_head">
			<div class="main-colum">
				<div class="topSpace">
					<div class="public_search">
						<form method="get" action="<%=basePath %>search.jsp" id="topsearchForm" target="_blank">
							<input name="t" value="timeqb" type="hidden"> 
							<input class="text" name="key" value="" type="text">
							<input class="image" value="&nbsp;" type="submit">
						</form>
					</div>

					<div class="clear">&nbsp;</div>
				</div>

				<div class="topNav">
					<div class="menuList">
						<div style="display: none; left: 500px;" class="nav-tabg">&nbsp;</div>
							<ul>
								<%
								int num = 0;
								for (JSONObject onemenu : webdata.getNavigationTree()) { 
									num++;
								%>
								<%--
			   						<%=onemenu.get("href")%> ==<%=webdata.getCheckedMenuStyle(onemenu.getString("pagesid"), "background-color:#00a3d2") %> 
								  --%>
								<li class=""><span class="m0<%=num%>"><a
										href="<%=basePath %><%=onemenu.get("href")%>" target="_blank"><%=onemenu.get("name") %></a></span></li>
								 <%} %>
								 <!-- 
								<li class=""><span class="m02"><a
										href="#" target="_blank">新闻</a></span></li>
	
								<li class=""><span class="m03"><a
										href="#" target="_blank">专题</a></span></li>
	
								<li class=""><span class="m04"><a
										href="#" target="_blank">政策</a></span></li>
	
								<li class=""><span class="m05"><a
										href="#" target="_blank">服务</a></span></li>
	
								<li class=""><span class="m06"><a
										href="#" target="_blank">问政</a></span></li>
	
								<li class=""><span class="m07"><a
										href="#" target="_blank">数据</a></span></li>
	
								<li class=""><span class="m08"><a
										href="#" target="_blank">国情</a></span></li>
						-->
							</ul>
						<div class="clear">&nbsp;</div>
					</div>
				</div>
			</div>
		</div>