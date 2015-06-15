<%@page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<script src="<%=request.getContextPath() %>/js/jquery-1.6.2.js"></script>
<title>Adobe Flash Player下载页面</title>
<script>
$(function () {
	var ctxPath = window.location.href.replace (/http:\/\/[^\/]*\/([^\/]*).*/g,'$1');
	var playername = "install_flash_player_ax.exe";
	if (!jQuery.browser.msie) {
		playername = "install_flash_player.exe";
	}
	
	$("<div style='position:absolute; z-index:200; height:100%; width:100%; background-color:white; font-size:12px; color:blue; text-align:center; padding-top:100px;'>\
			 <a href='/"+ctxPath+"/ocx/"+playername+"'>点击此处下载新版Adobe Flash Player</a>(<font color='red'>安装完成之后请重启浏览器！</font>)<br/>\
			 <a href='/"+ctxPath+"/ocx/install_flash_player_ax_64bit.exe'>若您的系统为64位系统请点击此处下载新版Adobe Flash Player</a>(<font color='red'>安装完成之后请重启浏览器！</font>)\
	   </div>").css ("opacity", 0.8).prependTo ("body");
});
</script>
</head>
<body>
</body>
</html>