<%--
	title:用户选择
	author:lining
--%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
label{width: 100%;}
#roleul{margin: 0;padding: 0; height: 200px;}
#roleul li{padding: 0 3px 0 1px; margin: 1px 0 0 0;}
</style>
<script>
var rolesid = '<%=request.getParameter("rolesid")%>';
var unitsid = '<%=URLDecoder.decode(request.getParameter("unitsid"), "utf-8")%>';
var includeSubunits = <%=request.getParameter("includeSubunits")%>;
$(function(){
	$("body").buttonset();
	if (unitsid) {
		$.getJSON(ctxPath + "/common/getUsersByRolesidAndUnitsid.vot?rolesid=" + rolesid+"&unitsid="+encodeURIComponent(encodeURIComponent(unitsid))
				 + "&includeSubunits="+includeSubunits, function(data){
			$(data).each(function(){
				$("#roleul").append($('<li><input type="radio" id="' + this.sid + '" value="' + this.sid + '" name="radio" /><label for="'+this.sid+'">'+this.name+'</label></li>')
						.data("item", this));
			});
			$("body").buttonset("refresh");
			if (data.length == 0) {
				$("body").append("对不起，未查询到相应的用户！");
			}
		});
	} else {
		$.getJSON(ctxPath + "/common/getAllUsersByRolesid.vot?rolesid=" + rolesid, function(data){
			$(data).each(function(){
				$("#roleul").append($('<li><input type="radio" id="' + this.sid + '" value="' + this.sid + '" name="radio" /><label for="'+this.sid+'">'+this.name+'</label></li>')
						.data("item", this));
			});
			$("body").buttonset("refresh");
			if (data.length == 0) {
				$("body").append("对不起，未查询到相应的用户！");
			}
		});
	}
});

function getSelectedUser() {
	if ($(":checked").length == 0) {
		alert("请选择一个用户！");
		return null;
	}
	return $(":checked").parent().data("item");
}
</script>
</head>
<body class="sm_main">
	<ul id="roleul"></ul>
</body>
</html>

