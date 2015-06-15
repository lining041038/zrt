<%--
	功能：修改用户信息
	编写：李宁
	日期：2012-03-15
 --%>
<%@page import="antelope.db.DBUtil"%>
<%@page import="antelope.services.SessionService"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
body{padding-top: 20px;}
</style>
<script>
$(function(){
	$("#userdialog").setCurrentState("update");
	$.getJSON(ctxPath + "/common/UserRoleOrgController/getLogonUserinfo.vot", function(data){
		$("#userdialog").resetForm(data);
	});
});

function commit(dialogobj) {
	$("#userdialog").submitForm({
		url:ctxPath + "/common/insertOrUpdateUser.vot",
		callback: function() {
			top.$(dialogobj).dialog("destroy");
		}
	});
}

function onpasswordchange() {
	$("[name=passwordchanged]").val("true");
}

</script>
</head>
<body>
<!-- 用户增改 -->
<form id="userdialog">
	<input name="sid" type="hidden"/>
	<input name="unitsid" type="hidden"/>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.username'] }：</span>
		<span class="cd_val">
			<input name="username" required="true" state='readonly.create:false; readonly.update:true;'/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.password'] }：</span>
		<span class="cd_val">
			<input onchange="onpasswordchange()" name="password" required="true" type="password"/>
			<input name="passwordchanged" type="hidden"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.gender'] }：</span>
		<span class="cd_val">
			<input name="name" required="true"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.email'] }：</span>
		<span class="cd_val">
			<input name="mail" required="true"/>
		</span>
	</div>
</form>
</body>
</html>