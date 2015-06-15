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
			$(".nav").append($("<p><a href='#'>"+this.title+"</a></p>").data("menu", this));
		});
		
		// 二级子菜单
		$(".nav p").hover(function(){
			var self = $(this); 
			if (!self.hasClass("selected")) 
				self.addClass("menu_over");
		}, function(){
			$(this).removeClass("menu_over");
		}).click(function(){
			var self = $(this);
			self.siblings().removeClass("selected");
			self.removeClass("menu_over").addClass("selected");
			$( ".menu_inner .accordion" ).hide();
			
			var submenu_accordion = self.data("submenu_accordion");
			
			$(".navs").empty();
			
			submenu_accordion = $("<div class='accordion' style='clear:both;'></div>");
			submenu_accordion.data("mainmenu", self);
			
			// 建立子菜单
			var menuLevel1Title = self.data("menu").title;
			$($(this).data("menu").submenu).each(function(index){
				$('<div class="li"> \
		                <div class="head menu2title">\
		                   <div class="l zyimg4"></div>\
		                   <span class="l">' + this.title + '</span>\
		                   <div class="zyimg6 r"></div>\
		                   <div class="clear"></div>\
		                </div>' + creatSubmenu(this.submenu, this.title) + '\
		            </div>').appendTo(submenu_accordion);
				function creatSubmenu(submenus, menuLevel2Title) {
					var submenudivs = "";
					$(submenus).each(function(index){
						var smifirststr = ""; 
						if (index == 0) {
							smifirststr = "smifirst";
						}
						var menuLevel3Title = this.title;
						submenudivs+='<div title3="'+menuLevel3Title+'" title2="'+menuLevel2Title+'" title1="'+menuLevel1Title+'" class="li2 ' + smifirststr + '" target="' + (this.target||'') + '" path="'+this.path+'"><a href="#">'+this.title+'</a></div>';
					});
					return submenudivs;
				}
			});
			
			$(".navs").append(submenu_accordion);
			
			self.data("submenu_accordion", submenu_accordion);
			
			// 子菜单效果
			$(".menu2title").click(function(){
				$(".li2").hide();
				$(this).siblings().show();
				$(".zyimg6").removeClass("zyimg6s");
				$(this).find(".zyimg6").addClass("zyimg6s");
			});
		
			// 显示左侧子菜单
			$(".left_menu").show();
			$(".sys_main").removeClass("fullscreen");
			
			// 显示具体模块功能div 隐藏门户div
			$("#module_div").show();
			$("#portal_div").hide();
			
			$(".smifirst:eq(0)", submenu_accordion).click();
			
	
		}).first().click();
		
	});
	
	// 三级菜单添加转到模块功能
	$(".li2").live("click", function(){
		
		if ($(this).attr("target") == '_blank') {
			open(ctxPath + "/"+ $(this).attr("path"));
			return;
		}
		
		$(".li2").removeClass("selected");
		$(this).closest(".accordion").data("mainmenu").data("currmenu", this);
		$(this).addClass("selected");
		
		$("#mainfuncifr").attr("src", ctxPath + $(this).attr("path") + "&autoheightifrmid=mainfuncifr");
		$("#smb_title1").text($(this).attr("title1"));
		$("#smb_title2").text($(this).attr("title1"));
		$("#smb_title3").text($(this).attr("title3"));
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

</script>
</head>
<body class="ui2-body ui2-main-body">

<div class="header">
	<div class="logo l png"></div>
    <div onclick="location = '${pageContext.request.contextPath}/logoff.jsp'" class="close r png" style="cursor: pointer;">
    	<div class="closebtn png"></div>
    </div>
</div>
<div class="nav png">
</div>
<div class="main2">
	<div style="position: relative; height: 20px;">
		<div style="margin:0; position: absolute; left: 0;">
			<div class="l crumbs"></div>
		    <a href="#" id="smb_title1" style="color: white !important;"></a> > 
		    <a href="#" id="smb_title2" style="color: white !important;"></a> > 
		    <a href="#" id="smb_title3" style="color: white !important;"></a>
	    </div>
    </div>
    <div class="clear"></div>
</div>
<div class="main">
	<div class="left l">
		<div class="l controlpanel"></div>
        <div class="navs">
        </div>
    </div>
    <div id="iframeautodiv" class="right r" style="padding: 0;">
    	<iframe id="mainfuncifr" frameborder='0' style="width: 100%; border: 0;"></iframe>
    </div>
    <div class="clear"></div>
    <div class="linebottom"></div>
</div>

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
</body>
</html>
