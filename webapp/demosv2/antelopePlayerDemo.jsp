<%@page import="antelope.utils.I18n"%>
<%@page import="com.sun.xml.internal.fastinfoset.sax.Properties"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	
	$("#videodiv").loadAntelopePlayer({flashvars:{"src":ctx + "/demos/assets/mmn.mp4"}});
	
	$("#audiodiv").loadAntelopePlayer({flashvars:{"src":ctx + "/demos/assets/mmn.mp4"}, playermode:"audio"});
});
</script>
<title>Insert title here</title>
</head>
<body>
	<div id="videodiv" style="height:300px;">
		
	</div>
	
	<div id="audiodiv" style="height:300px;">
		
	</div>
	
</body>
</html>