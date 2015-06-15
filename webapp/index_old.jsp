<%@page import="antelope.services.SessionService"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<%
SessionService service = (SessionService) session.getAttribute("service");
%>
<title>时光漫步</title>
<script>
$(function(){
	// IE版本判断
	if ($.browser.msie && parseInt($.browser.version) < 7) {
		$("#downnewbrs").dialog({width:700, height:300, modal:true, title:"请点击下面的链接下载最新版IE浏览器！"})
	}
	

	$.getJSON(ctxPath + "/getMenuInfo.vot", function(data){
		
		// 一级主菜单 
		$(data).each(function(){
			$(".sys_menu").append($("<li class='main_menu_li'>"+this.title+"</li>").data("menu", this));
		});
		
		// 二级子菜单
		$(".main_menu_li").hover(function(){
			var self = $(this);
			if (!self.hasClass("menu_sel")) 
				self.addClass("menu_over");
		}, function(){
			$(this).removeClass("menu_over");
		}).click(function(){
			var self = $(this);
			self.siblings().removeClass("menu_sel");
			self.removeClass("menu_over").addClass("menu_sel");
			$( ".menu_inner .accordion" ).hide();
			var submenu_accordion = self.data("submenu_accordion");
			// 已创建子菜单
			
			if (submenu_accordion)
				submenu_accordion.show();
			else {// 未创建则创建
				submenu_accordion = $("<div class='accordion'></div>");
				submenu_accordion.data("mainmenu", self);
				
				// 建立子菜单
				var menuLevel1Title = self.data("menu").title;
				$($(this).data("menu").submenu).each(function(index){
					if (index == 0) {
						$('<h3 class="main_func"> \
							<a href="#">'+this.title+'</a> \
						</h3> \
						<div class="sub_menu_item_container">' + creatSubmenu(this.submenu, true, this.title) + '</div>').appendTo(submenu_accordion);
					} else {
						$('<h3> \
								<a href="#">'+this.title+'</a> \
							</h3> \
						<div class="sub_menu_item_container">' + creatSubmenu(this.submenu, false, this.title) + '</div>').appendTo(submenu_accordion);
					}
					function creatSubmenu(submenus, isindexzero, menuLevel2Title) {
						var submenudivs = "";
						$(submenus).each(function(index){
							var menuLevel3Title = this.title;
							if (isindexzero && index == 0)
								submenudivs+='<div title3="'+menuLevel3Title+'" title2="'+menuLevel2Title+'" title1="'+menuLevel1Title+'" class="sub_menu_item smifirst" target="' + (this.target||'') + '" path="'+this.path+'"><span></span><a href="#">'+this.title+'</a></div>';
							else
								submenudivs+='<div title3="'+menuLevel3Title+'" title2="'+menuLevel2Title+'" title1="'+menuLevel1Title+'" class="sub_menu_item" target="' + (this.target||'') + '" path="'+this.path+'"><span></span><a href="#">'+this.title+'</a></div>';
						});
						return submenudivs;
					}
				});
				
				// 子菜单效果
				$( submenu_accordion ).accordion({
					autoHeight: false,
					icons:{ 'header': 'ui_submenu_icon', 'headerSelected': 'ui_submenu_icon' }
				});
				
				$(".menu_inner").append(submenu_accordion);
				
				self.data("submenu_accordion", submenu_accordion);
			}
		
			// 显示左侧子菜单
			$(".left_menu").show();
			$(".sys_main").removeClass("fullscreen");
			
			// 显示具体模块功能div 隐藏门户div
			$("#module_div").show();
			$("#portal_div").hide();
			
			// 默认点击第一个功能
			if (self.data("currmenu")) {
				$(self.data("currmenu")).click();
			} else {
				$(".smifirst", submenu_accordion).click();
			}
			
	
		});
		
	});
	
	// 三级菜单添加转到模块功能
	$(".sub_menu_item").live("click", function(){
		$(this).closest(".accordion").data("mainmenu").data("currmenu", this);
		
		if ($(this).attr("target") == '_blank') {
			open(ctxPath + "/"+ $(this).attr("path"));
			return;
		}
		
		$("#mainfuncifr").attr("src", ctxPath + $(this).attr("path"));
		$("#smb_title1").text(this.title1);
		$("#smb_title2").text(this.title2);
		$("#smb_title3").text(this.title3);
		$(".smb_toright").show();
		// 显示具体模块功能div 隐藏门户div
		$("#module_div").show();
		$("#portal_div").hide();
		$(".sys_main").removeClass("fullscreen");
	});
	
	$(window).resize(function(){
		if (isfullscreen) {
			$(".mid_container").height("100%");
		} else
			$(".mid_container").height($(window).height() - $(".sys_banner").height()- $(".sys_menu").height());
		$(".sm_main").height($(".btm").height() - $(".sm_banner").height());
	}).resize();
	
	leftmenuwidth = $(".left_menu").width();
	
	reportPosTop = $("#reportPos").css("top");
	
	// 读取页面右上角弹出信息模版
	$("<div/>").load(ctxPath+'/popout_notice_tmpl.jsp').appendTo("body");
	
	/*
	$.subscribeRoutedMessages({
		dest:"notice",
		beforeAjax:function() {
			if (!isshowing)
				return true;
		},
		afterAjax:function(data) {
			var popdiv = $("#popout_notic_div");
			popdiv.setTemplateElement(data.tmplid).processTemplate(data.data);
			
			$("a", popdiv).each(function(){
				var self = $(this);
				var href = self.attr("href");
				self.attr("clickhref", href).attr("href","#");
			})
			popdiv.animate({marginTop:0}, 1000).delay(20000).animate({marginTop:-40}, 1000, function(){
				isshowing = false;
			});
			isshowing = true;
		}
	});*/
	
	// 更改链接动作
	$("#popout_notic_div a").live("click", function(){
		var thisObj = this;
		var topifram = $.dialogTopIframe({
			url: $(thisObj).attr("clickhref"),
			buttons:{
				'关闭':function(){
					top.$(topifram).dialog("destroy");
				}
			}, width:$(window).width() - 50, height:$(window).height() - 50, title:$(thisObj).attr("title"), modal:true
		});
	});
});

var isfullscreen = false;
var leftmenuwidth;
var reportPosTop;
var isshowing = false;

function fullscreen() {
	isfullscreen = !isfullscreen;
	$(".left_menu,.index_top").toggle();
	if (isfullscreen) {
		$("#fullbtn").text("退出全屏");
		$("#reportPos").css("top", 1);
	} else {
		$("#fullbtn").text("全屏");
		$("#reportPos").css("top", reportPosTop);
	}
	$(".sys_main").toggleClass("fullscreen");
	
	$(window).resize();
}

// 打开首页门户
function open_Portal() {
	
	// 显示门户div 隐藏具体模块div
	$("#module_div").hide();
	$("#portal_div").show();
	$("#portalifr").attr("src", "portal.jsp");
	$(".left_menu").hide();
	$(".sys_main").addClass("fullscreen");
	
	// 取消主菜单选中状态
	$(".main_menu_li").removeClass("menu_sel");
}

// 关闭左侧子菜单
function closeLeftMenu() {
	$(".menu_inner").toggle();
	$(".left_menu").width($(".left_menu").width() == leftmenuwidth ? $(".lm_divider").width() : leftmenuwidth );
	$(".divider_hander").toggleClass("divider_close");
	$(".sys_main").toggleClass("leftmenuclose");
}

// 显示总矩阵
function showMainMatrix() {
	$.dialogTopIframe({
		url:ctxPath+"/hg/matrix/matrix.jsp",
		width:$(window).width() - 20, height:$(window).height() - 20, title:"总矩阵"
	});
}

</script>
</head>
<body>
<div id="downnewbrs" style='text-align:center;padding-top:30px; display: none;line-height: 25px;'>
	<div style="font-size: 15px; font-weight: bold;"><img src="icn_no.png" style="float: left;">对不起，您浏览器的版本过低，请下载新版浏览器以便正常使用系统！</div>
	<div>
		<a style="font-size: 14px;" href='<%=request.getContextPath() %>/ocx/IE8-WindowsXP-x86-CHS.exe'><font color="red">点击此处</font>下载新版IE浏览器！</a>(下载后安装请按照安装向导进行操作！)
	</div>
	<div>
		若您的系统为Win2003请<a style="font-size: 14px;" href='<%=request.getContextPath() %>/ocx/IE8-WindowsXP-x86-CHS.exe'><font color="red">点击此处</font>下载新版IE浏览器！</a>(下载后安装请按照安装向导进行操作！)
	</div>
	<div>若安装不成功，请尝试安装此补丁后再次安装<a style="font-size: 12px;" href='<%=request.getContextPath() %>/ocx/ie8update.exe'>新版浏览器安装前补丁程序</a></div>
</div>

<div class="index_top">
	<div class="sys_banner" onclick="open_Portal()" title="点击进入首页门户">
		<div class="sys_loginfo">
			  <%=service.getUser() %>  <a id="zhuxiaoa" href="${pageContext.request.contextPath}/logoff.jsp">注销</a>  
		</div>
	</div>
	<ul class="sys_menu">
		<li class="smu_lastli" style="width: 200px;">
			<input class="show_matrix" onclick="$.changeLanguage()" type="button" value="${i18n['antelope.changelanguage']}" />
			<input class="show_matrix" onclick="$.changeTheme()" type="button" value="${i18n['antelope.changetheme']}" />
		</li>
	</ul>
</div>
<div id="reportPos">
	<div id="popout_notic_div"></div>
</div>
<div class="mid_container">
	<div class="left_menu">
		<div class="menu_inner"></div>
		<div class="lm_divider">
			<table style="height: 100%; width: 100%; padding: 0; margin: 0;" cellpadding="0" cellspacing="0">
				<tr>
					<td style="vertical-align: middle; cursor: pointer;" onclick="closeLeftMenu()">
						<div class="divider_hander"></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="sys_main btm fullscreen">
		<div id="module_div" style="display: none;">
			<div class="sm_banner" ondblclick="fullscreen()" title="双击全屏">
				<span class="smb_title" id="smb_title1"></span><span class="smb_toright"></span>
				<span class="smb_title2" id="smb_title2"></span><span class="smb_toright"></span>
				<span class="smb_title2" id="smb_title3"></span>
				<a id="fullbtn" onclick="fullscreen(this)" href="#" style="float: right; margin-top: 7px; margin-right: 10px;color: #0066cc;">全屏</a>
			</div>
			<div class="sm_main" style="overflow: hidden;">
				<iframe id="mainfuncifr" frameborder='0' style="width: 100%; height: 100%; border: 0;"></iframe>
			</div>
		</div>
		<div id="portal_div">
			<iframe id="portalifr" src="portal.jsp" frameborder='0' style="width: 100%; height: 100%; border: 0;"></iframe>
		</div>
	</div>
</div>
</body>
</html>
