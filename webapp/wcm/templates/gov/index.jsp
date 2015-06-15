<%@page import="antelope.wcm.beans.WebPageDataBean"%>
<%@page import="antelope.utils.JSONArray"%>
<%@page import="antelope.wcm.consts.WCMSiteSettingConsts"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.utils.JSONObject"%>
<%@page import="antelope.wcm.services.TemplateInfoService"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.SessionService"%>
<%@page language="java" pageEncoding="utf-8"%>
<%
	String root = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+root+"/";
%>
<% WebPageDataBean webdata = TemplateInfoService.getWebPageData(request); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<title>政府gov</title>
	<base href="<%=basePath%><%=webdata.getTemplatepath()%>/">
 	<link href="assets/master_cn_v1.0.css"  rel="stylesheet" type="text/css">
	<link href="assets/index140516.css"  rel="stylesheet" type="text/css"> 
	<script src="js/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="js/slides.js" type="text/javascript"></script>
	<script src="js/index140516.js" type="text/javascript"></script>
 
<script type="text/javascript">
	<!--
	$(function(){   
	    var sliderElement = $('.slider-carousel');
	    sliderElement.slidesjs({
	        play: {
	            auto: true,
	            effect: 'fade'
	        },
	        navigation: {
	            effect: "fade"
	        },
	        pagination: {
	            effect: "fade"
	        },
	        effect: {
	            slide: {
	                speed: 400
	            }
	        }
	    });
	    var slidernav = sliderElement.find('.slidesjs-navigation');
	    sliderElement.hover(function() {
	        slidernav.stop().show()
	    },function() {
	        slidernav.stop().hide()
	    });
	    
	    /*Tab*/
	    $(".index_tab").each(function(){    
	        $(this).find(".tabmenu li").mouseover(function(){
	            index = $(".index_tab .tabmenu li").index(this);
	            $(this).addClass("selected").siblings().removeClass("selected");
	            console.log($(".tabox .tab_con").size(),index);
	            $(".tabox .tab_con").eq(index).show().siblings().hide();
	        }); 
	    });
	    $(".index_tab .index_list01 li").each(function(){
	        $(this).width(function(){
	            return $(this).width();
	        })
	    });
	    
	   
	});
	-->
	</script>
</head>
<body>
	<%@include file="header.jsp" %>
		<div class="main-colum">
			<div class="index_focus">
				<div class="lft_pic">
					<div class="slider-carousel">
					
					<% for (JSONObject portlet : webdata.getIndexImg()) {%> 
						<div class="item" style="display: block;">
							<div class="pannel-image">
								<a href="<%=basePath%><%=portlet.getString("href")%>"  target="_blank">
									<img width="720" height="328" src="<%=basePath %>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=portlet.getString("indeximgsid")%>"  border="0"></a>
							</div>
							<DIV class="titBg">&nbsp;</DIV>
							<div class="subtitle">
								<h6><a href="#" target="<%=basePath%><%=portlet.getString("href")%>"><%=portlet.getString("title") %></a></h6>
							</div>
						</div>
						<%} %>
						<!-- 
						<div class="item" style="display: none;">
							<div class="pannel-image">
								<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683343.htm" target="_blank">
								<img width="720" height="328" src="assets/gov_files/782bcb888ba414e5e3d901.jpg"  border="0"></a>
							</div>
							<div class="titBg">&nbsp;</div>
							<div class="subtitle">
								<h6><a href="http://www.gov.cn/xinwen/2014-05/21/content_2683343.htm" target="_blank">习近平会见阿塞拜疆总统阿利耶夫</a></h6>
							</div> 
						</div>
						<div class="item" style="display: none;">
							<div class="pannel-image">
								<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683339.htm"  target="_blank">
									<img width="720" height="328" src="assets/gov_files/782bcb888ba414e5da9e01.jpg" border="0"></a>
							</div>
							<div class="titBg">&nbsp;</div>
							<div class="subtitle">
								<h6><a href="http://www.gov.cn/xinwen/2014-05/21/content_2683339.htm" target="_blank">习近平和普京共同出席中俄海上联合军事演习开始仪式</a></h6>
							</div>
						</div>
						<div class="item" style="display: none;"> 
						<div class="pannel-image"><a href="http://www.gov.cn/xinwen/2014-05/19/content_2682330.htm" 
						target="_blank"><img width="720" height="328" src="assets/gov_files/a41f726a4b0614e4481501.jpg" 
						border="0"></a></div>
						<div class="titBg">&nbsp;</div>
						<div class="subtitle">
						<h6><a href="http://www.gov.cn/xinwen/2014-05/19/content_2682330.htm" target="_blank">李克强会见联合国秘书长潘基文</a></h6></div>
						</div>
						<div class="item" style="display: none;"> 
						<div class="pannel-image"><a href="http://www.gov.cn/xinwen/2014-05/21/content_2683656.htm" 
						target="_blank"><img width="720" height="328" src="assets/gov_files/782bcb888b1614e66fd901.jpg" 
						border="0"></a></div>
						<div class="titBg">&nbsp;</div>
						<div class="subtitle">
						<h6><a href="http://www.gov.cn/xinwen/2014-05/21/content_2683656.htm" target="_blank">亚信第四次峰会在上海举行</a></h6></div>
						</div>
						 -->
					</div>
				</div>
				<div class="rig_list">
					<div class="tit">
						<a href="http://www.gov.cn/xinwen/xw_yw.htm" target="_blank">要闻</a> 
					</div>
					<div class="lastNews">
						<ul>
						<% for (JSONObject portlet : webdata.getIndexImg()) {%> 
						  <li>
							  <div class="txt">
							  	<a href="<%=basePath%><%=portlet.getString("href")%>" target="_blank"><%=portlet.getString("title") %></a>
							  	<span>(<%=portlet.getString("createtime").substring(5,10)%>)</span>
							  </div>
							  <div class="line">&nbsp;</div>
						  </li>
						  <%} %>
						  <!-- 
						  <li>
							  <div class="txt">
							  	<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683512.htm" target="_blank">习近平主持亚信峰会第一阶段会议并作主旨讲话</a>|
							 	<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683791.htm" target="_blank">讲话全文</a> <span>(05-21)</span>
							  </div>
							  <div class="line">&nbsp;</div>
						  </li>
						  <li>
							  <div class="txt">
							  	<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683512.htm" target="_blank">习近平主持亚信峰会第一阶段会议并作主旨讲话</a>|
							 	<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683791.htm" target="_blank">讲话全文</a> <span>(05-21)</span>
							  </div>
							  <div class="line">&nbsp;</div>
						  </li>
						  <li>
							  <div class="txt">
							  	<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683512.htm" target="_blank">习近平主持亚信峰会第一阶段会议并作主旨讲话</a>|
							 	<a href="http://www.gov.cn/xinwen/2014-05/21/content_2683791.htm" target="_blank">讲话全文</a> <span>(05-21)</span>
							  </div>
							  <div class="line">&nbsp;</div>
						  </li>
						   -->
						  </ul>
					  </div>
			  </div>
			  <div class="clear">&nbsp;</div>
		   </div>
		</div>
		
		<!-- 顶部 -->		
		<div class="hr_index">&nbsp;</div>
		<div class="main-colum">
		<!-- 左中 -->
			<div class="index_lft01 left">
				<div class="index_w1 left">
					<div class="index_tab">
						<div class="tabmenu" id="tabmenu_1">
							<ul>
							<% for (JSONObject portlet : webdata.getLeftPortlets()) { 
							%>	
								<li class=""><span class="bg01">&nbsp;</span><span class="tabg"><!-- lianjie --><%=portlet.get("title") %><!-- lianjie --></span><span class="bg02">&nbsp;</span></li>
							<%}%>
							  
							</ul>
						<script>
							$(function(){
								$("#tabmenu_1").find("ul").children("li").first().addClass("selected");
							});
						</script>
						<div class="clear">&nbsp;</div></div>
						<div class="hr_10">&nbsp;</div>
						<div class="tabox">
							<% for (JSONObject portlet : webdata.getLeftPortlets()) { 
							%>
							<div class="tab_con h243" style="display: none;">
								<ul class="index_list01">
								<% 
									JSONArray ja = (JSONArray) portlet.get("portletcontent_values");
									for(Object oo:ja){
										JSONObject jo = (JSONObject)oo;
						 		%>
									  <li style="width: 334px;"><a target="_blank" href="<%=basePath%><%=jo.getString("href")==null?"":jo.getString("href").replaceFirst("webpagepos=index","webpagepos=null")%>"><%=jo.get("name") %></a><span>(<%=jo.get("createtime")==null?"":jo.get("createtime").toString().substring(5, 10) %>)</span></li>
								<%} %>	  
								  </ul>
							</div>
							<%} %>
						</div>
					</div>
				</div>
				<!-- 中 -->
				<div class="index_w2 right">
					<div class="index_tab">
						<div class="tabmenu" id="tabmenu_2">
							<ul>
							<% for (JSONObject portlet : webdata.getCenterPortlets()) { 
							%>
							  <li class=""><span class="bg01">&nbsp;</span><span class="tabg"><!-- href --><%=portlet.get("title")%><!-- href --></span><span class="bg02">&nbsp;</span></li>
							<%} %>
							</ul>
							<script>
							$(function(){
								$("#tabmenu_2").find("ul").children("li").first().addClass("selected");
							});
						</script>
							<div class="clear">&nbsp;</div>
						</div>
					<div class="hr_10">&nbsp;</div>
						<div class="tabox">
						<% for (JSONObject portlet : webdata.getCenterPortlets()) { 
						%>
						
							<div style="display: none;" class="tab_con h243">
								<ul  class="index_list01">
						<% 
							JSONArray ja = (JSONArray) portlet.get("portletcontent_values");
							for(Object oo:ja){
								JSONObject jo = (JSONObject)oo;
				 		%>
								  <li style="width: 225px;"><a target="_blank" href="<%=basePath%><%=jo.getString("href")==null?"":jo.getString("href").replaceFirst("webpagepos=index","webpagepos=null")%>"><%=jo.get("name") %></a><span>(<%=jo.get("createtime")==null?"":jo.get("createtime").toString().substring(5, 10) %>)</span></li>
						<%} %>
								</ul>
							</div>
						<%} %>
						</div>
					</div>
				</div>
			</div>
			<!-- 左中 -->
			<!-- 右 -->
			<div class="index_w4 right">
				<div class="index_tab">
				<div class="tabmenu" id="tabmenu_3">
					<ul>
					<% for (JSONObject portlet : webdata.getRightPortlets()) { 
					%>
					  <li class=""><span class="bg01">&nbsp;</span><span class="tabg"><%=portlet.get("title") %></span><span class="bg02">&nbsp;</span></li>
					<%} %>
					</ul>
					<script>
							$(function(){
								$("#tabmenu_3").find("ul").children("li").first().addClass("selected");
							});
						</script>
					<div class="clear">&nbsp;</div>
				</div>
				<div class="tabox">
					<% for (JSONObject portlet : webdata.getRightPortlets()) { 
					%>
					<div style="display: none;" class="tab_con h243">
								<ul  class="index_list01">
						<% 
							JSONArray ja = (JSONArray) portlet.get("portletcontent_values");
							for(Object oo:ja){
								JSONObject jo = (JSONObject)oo;
				 		%>
								  <li style="width: 225px;"><a target="_blank" href="<%=basePath%><%=jo.getString("href")==null?"":jo.getString("href").replaceFirst("webpagepos=index","webpagepos=null")%>"><%=jo.get("name") %></a><span>(<%=jo.get("createtime")==null?"":jo.get("createtime").toString().substring(5, 10) %>)</span></li>
						<%} %>
								</ul>
					</div>
						<%} %>
				</div>
			</div>
 		</div>
		<!-- 右 -->
		<script>
			//初始化所有tab的状态  第一个默认显示
			$(function(){
				$("div[class='tabox']").each(function(){
					$(this).children("div").first().show();
				});
			});
		</script>
	</div>
	
	<!-- 下 -->
	<div class="main-colum rel">
		<div class="index_video_pic">
			<div class="index_tab">
				<div class="tabmenu" id="tabmenu_4">
					<ul>
					<% for (JSONObject portlet : webdata.getBottomPortlets()) { 
					%>
						<li class="">
								<span class="bg01">&nbsp;</span> 
								<span class="tabg">
									<%=portlet.getString("title") %>
								</span> 
								<span class="bg02">&nbsp;</span>
						</li>
		
					<%} %>
					</ul>
					<script>
							$(function(){
								$("#tabmenu_4").find("ul").children("li").first().addClass("selected");
							});
					</script>
					<div class="clear">&nbsp;</div>
				</div>
		
				<div class="hr_10">&nbsp;</div>
				<div class="hr_5">&nbsp;</div>
		
				<div class="tabox">
					<% 
						for (JSONObject portlet : webdata.getBottomPortlets()) { 
					%>
					<div style="display: none;" class="tab_con h204">
						<div class="index_video">
							<div class="index_picList2">
								<% 
									JSONArray ja = (JSONArray) portlet.get("portletcontent_values");
									for(Object oo:ja){
										JSONObject jo = (JSONObject)oo;
						 		%>
								<div class="big" style="width:248px;height:190px">
									<div class="pannel-image">
										<a target="_blank" href="<%=basePath%><%=jo.getString("href")==null?"":jo.getString("href").replaceFirst("webpagepos=index","webpagepos=null")%>">
											<img width="248" height="190" border="0" src="<%=request.getContextPath()%>/upload/UploadController/getSingleImageDataByFilegroupsid.vot?filegroupsid=<%=jo.getString("productimgsid") %>">
										</a>
									</div>
									
									<div class="subtitle">
										<a target="_blank" href="<%=basePath%><%=jo.getString("href")==null?"":jo.getString("href").replaceFirst("webpagepos=index","webpagepos=null")%>"><%=jo.get("name") %></a>
									</div>
									
									<div class="titBg">&nbsp;</div>
								</div>
								<%
									}
								%>
							</div>
						</div>
					</div>
				<%
						}
				%>
				</div>
			</div>
		</div>	
	</div>
	<!-- 下 -->
	
	<%@ include file="bottom.jsp" %>

<%-- 
<%
	for (JSONObject menu: webdata.getBreadcrumbs()) {
		out.println(menu);
	}

	if (webdata.getSubNavigationTree(2) != null) { 
		for (JSONObject submenu: webdata.getSubNavigationTree(2)) {
			out.println(submenu);
		}
	}
%>
--%>
</body>
</html>