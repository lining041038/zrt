<%@page import="antelope.utils.SystemOpts"%>
<%
session.setAttribute("user", null);
session.invalidate();
response.sendRedirect(request.getContextPath() + SystemOpts.getProperty("loginpagepos"));
%> 