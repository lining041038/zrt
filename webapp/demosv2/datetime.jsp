<%@page import="java.util.Date"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
	<input class="datetime" style="margin-left: 300px; margin-top: 300px;" value="<%=BaseController.getNewTimeSdf().format(new Date())%>" default="<%=BaseController.getNewTimeSdf().format(new Date())%>"/>
</body>
</html>