<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="antelope.services.SessionService"%>
<jsp:include page="/include/header2.jsp"/>
<script>
var dataurl = '<%=request.getParameter("dataurl")%>';
$(function(){
	$.post(ctx + dataurl , function(data){
		$(".print").append(data);
	})
})
</script>
<style>
@media print {
	.noprint {
		display: none
	}
}
.print table td{padding:5px 5px 5px 5px;}
</style>  
<html>
<body>
	<div class="noprint">
		<table style="margin: 0 auto; width: 500px;">
			<tr align="center">
				<td><object id="WebBrowser"
						classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0"
						width="0"> </object> <input type="button" value="打印"
					onclick="document.all.WebBrowser.ExecWB(6,1)"><input
					type="button" value="页面设置"
					onclick="document.all.WebBrowser.ExecWB(8,1)"><input
					type="button" value="打印预览"
					onclick="document.all.WebBrowser.ExecWB(7,1)"></td>
			</tr>
		</table>
	</div>
	
	<div class="print" style="margin: 0 auto;width: 700px">
		
	</div>
</html>