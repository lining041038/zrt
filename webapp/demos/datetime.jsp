<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:00"); 
%>
<script>
	$(function(){
		
		$(".datetime2").datetimepicker({
			secondEditable: false
		});
	});
</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
	<input class="datetime" style="margin-left: 300px; margin-top: 300px;" value="<%=BaseController.getNewTimeSdf().format(new Date())%>" default="<%=BaseController.getNewTimeSdf().format(new Date())%>"/>
	<input class="datetime2" style="margin-left: 300px; margin-top: 300px;" value="<%=sdf.format(new Date())%>" default="<%=sdf.format(new Date())%>"/>
</body>
</html>