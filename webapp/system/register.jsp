<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
</style>
<script>
$(function(){
	$("#userdialog").resetForm();
});

function registeruser(callbackfunc) {
	$("#userdialog").submitForm({
		url:ctx + "/common/addRegisterUser.vot",
		callback:callbackfunc
	});
}
</script>
</head>
<body class="sm_main">
<form id="userdialog" style="padding-top: 10px;">
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
		<span class="cd_name">${i18n['antelope.user'] }：</span>
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
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.pwdquestion'] }：</span>
		<span class="cd_val">
			<input name="pwdquestion" required="true"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.pwdquestionanswer'] }：</span>
		<span class="cd_val">
			<input name="pwdanswer" required="true"/>
		</span>
	</div>
</form>
</body>
</html>