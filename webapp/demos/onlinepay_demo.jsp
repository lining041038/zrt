<%--
 系统在线支付演示
 @author lining
 @since 2014-05-04
--%>
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.springmvc.FormatterFactory"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="antelope.demos.entites.MyOrderItem"%>
<%@page import="antelope.springmvc.JPABaseDao"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<body>

支付订单前可定制显示信息

<jsp:include page="/components/onlinepay.jsp">
	<jsp:param name="component" value="onlinepaydemo"/>
</jsp:include>

支付订单后 可定制显示信息
</body>
</html>