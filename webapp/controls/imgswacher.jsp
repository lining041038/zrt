<%@page import="org.apache.commons.fileupload.FileUploadBase.IOFileUploadException"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page language="java" pageEncoding="utf-8"%>
<html>
<head>
<style>
	* {font-size: 12px;}
	body {text-align: center;  padding-top: 80px; background-position:center; background-position:top; overflow: auto;}
	#tabs {width:870px; height: 300px;}
	.content{height: 300px; text-align: left;}
	b{color: #005590}
</style>
<script src="<%=request.getContextPath() %>/js/jquery-1.6.2.js"></script>
<script src="<%=request.getContextPath() %>/controls/js/jquery-controls-1.0.js"></script>
<script>
	$(function() {
		alert("fdsfd");
		$("div").loadfxImgSwatcher({
			imgs:[{
				src: "<%=request.getContextPath()%>/controls/dddd.png",
				link:'http://www.sohu.com'
			},{
				src: "<%=request.getContextPath()%>/controls/eeee.png",
				link:'http://www.sina.com'
			}]
		});
		
		alert("fdsfd");
	});
</script>
</head>
<body >
<button onclick="showHide();"></button>
<div style="width: 100%; height: 100%; float: left;padding: 2px;"></div>
<div style="width: 49%; height: 100%; float: left;padding: 2px;"></div>
<div style="width: 49%; height: 100%; float: left;padding: 2px;"></div>
</body>
</html>
