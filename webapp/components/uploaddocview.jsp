<%@page import="antelope.utils.I18n"%>
<%@page import="com.sun.xml.internal.fastinfoset.sax.Properties"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
html,body{height: 100%;}
</style>
<script>
$(function(){
	$.post(ctx + "/upload/convertDocToSwf.vot?sid=<%=request.getParameter("sid")%>", function(data) {
		//alert('/exportimgs/<%=request.getParameter("sid")%>/<%=request.getParameter("sid")%>_1.swf?{1,' + data + '}')
		$("#videodiv").html('').loadflexpaper({flashvars:{SwfFile: '/exportimgs/<%=request.getParameter("sid")%>/<%=request.getParameter("sid")%>.pdf_[*,*].swf?{1,' + data + '}'}});
	});
});

</script>
<title>预览</title>
</head>
<body style="overflow: hidden;">
	<div id="videodiv" style="height:100%; font-size: 15px; font-weight: bold; text-align: center;">
		<div style="padding-top: 100px;">正在努力为您准备文档，根据文档的不同大小，可能需要等待几秒钟到几分钟不等，请稍后片刻！</div>
	</div>
</body>
</html>