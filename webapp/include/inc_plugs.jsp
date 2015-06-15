<%@page import="java.io.File"%><%@page import="org.springframework.transaction.TransactionStatus"%><%@page import="antelope.springmvc.SpringUtils"%><%@ page language="java" pageEncoding="utf-8"
%><%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	String file = request.getParameter("file");
	String pathfile = this.getClass().getResource("/").getFile();
	File fileobj = new File(pathfile);
	File parentfile = fileobj.getParentFile().getParentFile();
	
	File pluginfolder = new File (parentfile.getAbsolutePath()  + "/plugins");
	File[] pluginfolders = pluginfolder.listFiles();
	
	for(int i = 0; i < pluginfolders.length; i++) {
		File includefile = new File(pluginfolders[i].getAbsolutePath() + "/" + file);
		if (includefile.exists()) {
			String filepath = "/plugins/" + pluginfolders[i].getName() + "/" + file;
			%><jsp:include page="<%=filepath %>"/><%
		}
  	} 
%>