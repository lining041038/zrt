<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<jsp:include page="/include/header2.jsp"/>
<%	
	response.setHeader("Content-Disposition","attachment; filename="+new String("导出日报.xls".getBytes("gb2312"), "ISO8859-1" ));
%>
</head>
<body class="sm_main" style="overflow-x:hidden;">
<%=request.getParameter("exphtml") %>
</body>
</html>
