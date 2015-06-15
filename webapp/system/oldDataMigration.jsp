<%@page import="antelope.db.DBUtil"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
fieldset label {margin-bottom: 3px;}
</style>
<script>
$(function(){
	$.getJSON(ctxPath + "/common/DataMigrationController/getMigrationInfos.vot", function(data){
		for(var i = 0; i < data.length; i++) {
			$("#migraitem").append(
			'<span class="cd_item">\
				<span class="cd_name">'+data[i].title+'：</span>\
				<span class="cd_val">\
					<input type="button" onclick="migrationIt(this)" mititle="'+data[i].title+'" class="smallbutton" value="开始迁移" style="width:200px;"/>\
				</span>\
			</span>\
			<br/>');
		}
	});
	
	// 若数据迁移时间过长，防止session过期
	$.noSessionTimeout();
});

function migrationIt(thisObj) {
	if ($("#form1").validate()) {
		if (confirm("进行数据迁移将删除当前所需迁移表中的所有数据，是否继续？")) {
			$(thisObj).val("迁移中，请稍后...");
			$.post(ctxPath + "/common/DataMigrationController/startMigratData.vot?title="+encodeURIComponent(encodeURIComponent($(thisObj).attr("mititle"))), $("#form1").serialize(), function(data){
				if (data.success) {
					if (data.msg) {
						window[data.msg]();
						$(thisObj).val("迁移成功！点击将重新迁移");
					} else {
						alert("数据迁移成功！");
						$(thisObj).val("迁移成功！点击将重新迁移");
					}
				} else {
					alert(data);
				}
			}, "json");
		}
	}
}

function testconnect() {
	if ($("#form1").validate()) {
		$.post(ctxPath + "/common/DataMigrationController/testConnection.vot", $("#form1").serialize(), function(data){
			if (data) {
				alert(data);
			} else {
				alert("测试连接成功！");
			}
		});
	}
}

function executeUpdateDb() {
	if ($("#form1").validate()) {
		$.post(ctxPath + "/common/DBController/testCallAnt.vot", $("#form1").serialize(), function(data){
			if (data) {
				alert(data);
			} else {
				alert("数据库升级成功！");
			}
		});
	}
}

</script>
</head>
<body class="sm_main ui-layout">
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 15px;padding: 5px;">
			数据库版本更新及老系统数据迁移
		</div>
		<div id="procitydiv" style="padding: 4px;">
			<form id="form1">
				<fieldset id="province">
					<legend>系统连接信息</legend>
					<span class="cd_item">
						<span class="cd_name">数据库连接字符串：</span>
						<span class="cd_val">
							<input name="connectionurl" style="width: 500px;" required="true"/>
						</span>
					</span>
					<br/>
					<span class="cd_item">
						<span class="cd_name">用户名：</span>
						<span class="cd_val">
							<input name="username" required="true"/>
						</span>
					</span>
					<span class="cd_item">
						<span class="cd_name">密码：</span>
						<span class="cd_val">
							<input name="password" required="true"/>
						</span>
					</span>
					<span class="cd_item">
						<span class="cd_name"><input onclick="testconnect()" type="button" class="smallbutton" value="测试连接"/></span>
						<button onclick="executeUpdateDb()" class="smallbutton" style="margin-left: 10px;">升级数据库</button>
					</span>
				</fieldset>
			</form>
			<hr/>
			<fieldset id="migraitem">
				<legend>数据迁移项</legend>
					
			</fieldset>
		</div>
	</div>
</div>
</body>
</html>



