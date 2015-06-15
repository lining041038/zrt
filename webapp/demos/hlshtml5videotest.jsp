<%--
 基本请求响应演示
 @author lining
 @since 2012-09-23
--%>
<%@ page language="java" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<body>
<!--HLSPlayer代码开始-->
<div class="video" id="HLSPlayer" >
<SCRIPT LANGUAGE=JavaScript>
<!--
/*
* HLSPlayer参数应用=========================
* @param {Object} vID        ID
* @param {Object} vWidth     播放器宽度设置
* @param {Object} vHeight    播放器宽度设置
* @param {Object} vPlayer    播放器文件
* @param {Object} vHLSset    HLS配置
* @param {Object} vPic       视频缩略图
* @param {Object} vCssurl    移动端CSS应用文件
* @param {Object} vHLSurl    HLS(m3u8)地址
* ==========================================
*/
var vID        = ""; 
var vWidth     = "100%";                //播放器宽度设置
var vHeight    = 400;                   //播放器宽度设置
var vPlayer    = "hlsplayer.swf?v=1.5"; //播放器文件
var vHLSset    = "hls.swf";             //HLS配置
var vPic       = "images/start.jpg";    //视频缩略图
var vCssurl    = "images/mini.css";     //移动端CSS应用文件

//HLS(m3u8)地址,适配PC,安卓,iOS,WP
var vHLSurl    = "assets/test.m3u8";

//-->
</SCRIPT> 
<script type="text/javascript" src="js/hls.min.js?rand=20141217"></script>
</div>
<!--HLSPlayer代码结束-->
</body>
</html>


