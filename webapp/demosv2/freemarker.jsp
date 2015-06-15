<%@page import="antelope.springmvc.BaseController"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>

<% System.out.println(session.getId()); 

	System.out.println(request.getParameter("param1"));
	System.out.println(request.getParameter("param2"));
	System.out.println(BaseController.d(request.getParameter("param2")));
%>
<script>

function publishAll() {
	$.post(ctx + "/demos/freemarker/FreeMarkerDemoController/publishAll.vot", function(data) {
		alert("发布完成");
	});
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
	<button onclick="publishAll()">freemarker全部静态发布</button>
	<a href="${pageContext.request.contextPath}/published/demo.html?ran=<%=Math.random() %>" target="_blank">静态页面访问</a>
</body>
</html>