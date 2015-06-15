<%@page import="java.util.Enumeration"%><%@page import="flex.messaging.util.URLDecoder"%><%
System.out.println(URLDecoder.decode(request.getParameter("regionsid")));

Enumeration<String> names = request.getParameterNames();
while(names.hasMoreElements()) {
	String nextname = names.nextElement();
	System.out.println(nextname);
}

%>{"regionsid":"<%=request.getParameter("regionsid") %>","ct":30,"color":<%=0xff00f0%>}