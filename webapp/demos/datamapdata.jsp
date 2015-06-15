<%@page import="java.util.Enumeration"%>
<%@page import="flex.messaging.util.URLDecoder"%>
<%
System.out.println(URLDecoder.decode(request.getParameter("label")));
System.out.println(URLDecoder.decode(request.getParameter("regionsid")));

Enumeration<String> names = request.getParameterNames();
while(names.hasMoreElements()) {
	String nextname = names.nextElement();
	System.out.println(nextname);
}

%>
{
	"xml" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n\t<header>\n\t\t<textformat leading=\"5\">\n\t\t\t<b>\n\t\t\t\t<font size=\"15\">静海  总体情况<\/font>\n\t\t\t<\/b>\n\t\t\t<br/>\n\t\t\t<u>\n\t\t\t\t<b>\n\t\t\t\t\t<font size=\"13\" color=\"#06639e\">组织机构数量：<\/font>\n\t\t\t\t<\/b>\n\t\t\t\t<b>\n\t\t\t\t\t<font id=\"bmje\" size=\"13\" color=\"#06639e\">0<\/font>\n\t\t\t\t<\/b>\n\t\t\t<\/u>\n\t\t\t<br/>\n\t\t\t<u>\n\t\t\t\t<b>\n\t\t\t\t\t<font size=\"13\" color=\"#06639e\">法律法规发布数量：<\/font>\n\t\t\t\t<\/b>\n\t\t\t\t<b>\n\t\t\t\t\t<font id=\"fbsl\" size=\"13\" color=\"#06639e\">0<\/font>\n\t\t\t\t<\/b>\n\t\t\t<\/u>\n\t\t<\/textformat>\n\t<\/header>\n\t<datagrid height=\"200\">\n\t\t<flatdata>\n\t\t\t<col width=\"100\" datafield=\"createuser\" label=\"发布人\"/>\n\t\t\t<col width=\"120\" datafield=\"createdate\" label=\"发布时间\"/>\n\t\t\t<col width=\"250\" datafield=\"title\" label=\"发布主题\"/>\n\t\t<\/flatdata>\n\t<\/datagrid>\n<\/root>",
	"label" : "<%=request.getParameter("label") %>"
}