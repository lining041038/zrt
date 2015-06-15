<%--
	title:纯角色权限管理
	author:lining
--%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.services.SessionService"%>
<%@page import="antelope.utils.SystemOpts"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%

SessionService service = (SessionService) request.getSession().getAttribute("service");
String theme = "defaults";
TransactionStatus status = SpringUtils.beginTransaction();
try {
	if (service != null) {
		 theme = service.getTheme();
	}
} finally {
	SpringUtils.commitTransaction(status);
}

if ("fjh".equals(theme)) {
	request.setAttribute("componentVersion", "v2"); 
}

%>
<jsp:include page="/include/header2.jsp"/>
<style>
label{width: 100%;}
#roleul{margin: 0;padding: 0; height: 200px;}
#roleul li{padding: 0 3px 0 1px; margin: 1px 0 0 0;}
#roleaddbtn{width: 100%;}
#userlist{padding: 5px;}
</style>
<script>
$(function(){
	$('body').layout({fxName:'none'});
	$( "#roleul" ).buttonset();
	
	// 获取系统角色
	readroles();
	
	
	$("#rolecol").resize(function(){
		$("#roleouter").height($(this).height() - $("#roleheader").height() - 10);
	}).resize();
	$("#rolecol").resize(function(){
		$("#userlist").height($(this).height() - $("#roleheader").height() - 15);
	}).resize();
	
});

function readroles() {
	$.showBusyState();
	$("#roleul2").empty();
	$.getJSON(ctxPath + "/common/getAllRoles.vot", function(data) {
		// 添加点击事件
		$(data).each(function(){
			$("#roleul").append($('<li><input type="radio" id="' + this.sid + '" value="' + this.sid + '" name="radio" /><label for="'+this.sid+'">'+this.name+'</label></li>')
					.data("item", this));
			$("#roleul2").append($('  <label class="btn btn-sm btn-default">\
				    					<input type="radio" id="' + this.sid + '" name="options"  value="' + this.sid + '" id="option1" autocomplete="off" checked>' + this.name + '\
					 				</label>').data("item",this));
		});
		
		$("body").on("click", "#roleul2 label", function(){
			
			var thisObj = this;
			var thisValue = $(thisObj).find("input").val();
			
			$.getJSON(ctxPath + "/common/getfunctionroledatas.vot?rolesid="+thisValue, function(data){
				var treedata = [{id:'root', open:true, name:"${i18n['antelope.allfunctions']}", nodes:data}];
				var setting = {
					expandSpeed : "",
					showLine : true,
					checkable : true,
					checkedCol :"checked",
					checkType: { "Y": "ps", "N": "ps" }
				};
				$("#tree").empty();
				
				<jsp:include page="/include/inc_plugs.jsp">
					<jsp:param name="file" value="system/role_manage/treedatafilter.jsp"/>
				</jsp:include>
				
				zTree = $("#userlist").zTree(setting, treedata);
			});
		});
		$("#roleul2 :radio:eq(0)").click();
		$( "#roleul" ).buttonset("refresh");
		$("#roleul :radio:eq(0)").click();
		$.hideBusyState();
	});
}

//创建角色
function createRole() {
	$("#roledialog input").val('');
	submitRoleInner();
}

// 修改角色
function updateRole() {
	var selectedrole = $("#roleul2 label.active input").closest("label").data("item");
	$("#roledialog").resetForm(selectedrole);
	submitRoleInner();
}

// 新增或修改对话框提交
function submitRoleInner() {
	$("#roledialog").submitDialog({
		url:ctxPath + "/common/insertOrUpdateRole.vot",width:500,height:230,
		callback:readroles
	});
}

// 删除角色
function deleteRole() {
	$.post(ctxPath + "/common/UserRoleOrgController/getUsersCountByRolesid.vot?rolesid=" + $("#roleul2 label.active input").val(), function(data){
		if(parseInt(data) > 0) {
			alert("${i18n['antelope.rolehasuserinfo']}");
			return;
		} else {
			if (confirm("${i18n['antelope.confirmtodelrole']}")) {
				$.post(ctxPath + "/common/deleterole.vot", "rolesid="+$("#roleul2 label.active input").val(), function(data) {
					if (data) {
						alert(data);
						return;
					}
					alert("${i18n['antelope.deletesuccess']}");
					readroles();
				})
			}
		}
	});
}

// 添加角色用户
function adduser() {
	var data = $("#userlist").datagrid("option", "dataProvider");
	var sidstr = [];
	$.getJSON(ctxPath + "/common/getAllUserssidsByRolesid.vot?rolesid="+$("#roleul2 label.active input").val(), function(data){
		$.selectUsers({
			callback:function(users){
				var usersids = [];
				$(users).each(function(){
					usersids.push(this.sid);
				});
				$.post(ctxPath + "/common/saveroleusers.vot", $.param({"usersids":encodeURIComponent(JSON.stringify(usersids)), rolesid:encodeURIComponent($("#roleul2 label.active input").val())}), function(data){
					if (data) {
						alert(data);
						return;
					}	
					$("#roleul2 label.active input").click();
				});
			},
			isUsername: false,
			selectedusers:data
		});
	});
	
}

function deleteroleusers() {
	deleteroleusersinner($("#userlist").datagrid("option", "selectedItems"));
}

function deleteroleusersinner(item) {
	var usersids = [];
	$(item).each(function(){
		usersids.push(this.sid);
	});
	$.post(ctxPath + "/common/delroleusers.vot", $.param({"usersids":encodeURIComponent(JSON.stringify(usersids)), rolesid:encodeURIComponent($("#roleul2 label.active input").val())}), function(data){
		if (data) {
			alert(data);
			return;
		}	
		$("#roleul2 label.active input").click();
	});
}
var zTree = null;
// 权限设置
function setAuthoritys() {
	$("#tree").empty();
	
	var useritems = $("#userlist").datagrid("option","selectedItems");
	
	if (!useritems.length) {
		alert("${i18n['antelope.pleaseseltosetrights']}");
		return;
	}
	
	var tusersid = "";
	if (useritems.length == 1) {
		tusersid = useritems[0].sid; 
	}
	$.showBusyState();
	$.getJSON(ctxPath + "/common/getfunctiondatas.vot", {usersid:tusersid}, function(data){
		var treedata = [{id:'root', open:true, name:"${i18n['antelope.allfunctions']}", nodes:data}];
		var setting = {
			expandSpeed : "",
			showLine : true,
			checkable : true,
			checkedCol :"checked",
			checkType: { "Y": "ps", "N": "ps" }
		};
		
		zTree = $("#tree").zTree(setting, treedata);
		$.hideBusyState();
	});
	useritems = encodeURIComponent(JSON.stringify(useritems));
	
	$("#authordialog").dialog({
		width:700, height:400, title:"${i18n['antelope.setrights']}",
		buttons:{
			'${i18n['antelope.ok']}':function() {
				var nodes = zTree.getCheckedNodes();
				
				var funcids = [];
				$(nodes).each(function(){
					funcids.push(this.id);
				});
				
				$.post(ctxPath + "/common/saveUserAuthoritys.vot", {users:useritems, funcs:encodeURIComponent(JSON.stringify(funcids))}, function(data){
					if (data) {
						alert(data);
						return;						
					}
					alert("${i18n['antelope.setrightssucess']}");
					$("#authordialog").dialog("destroy");
				});
				
			},'${i18n['antelope.cancel']}':function() {
				$("#authordialog").dialog("destroy");
			}
		}
	});
}

function importUser() {
	$.post(ctxPath + "/common/importUser.vot");
}

function importRole() {
	$.post(ctxPath + "/common/importRole.vot");
}

function setroleAuthor() {
	$("#tree").empty();
	
	$.getJSON(ctxPath + "/common/getfunctionroledatas.vot?rolesid="+$("#roleul2 label.active input").val(), function(data){
		var treedata = [{id:'root', open:true, name:"${i18n['antelope.allfunctions']}", nodes:data}];
		var setting = {
			expandSpeed : "",
			showLine : true,
			checkable : true,
			checkedCol :"checked",
			checkType: { "Y": "ps", "N": "ps" }
		};
		$("#tree").empty();
		
		zTree = $("#tree").zTree(setting, treedata);
	});
	
	
	$("#authordialog").dialog({
		width:700, height:400, title:"${i18n['antelope.setrights']}",
		buttons:{
			'${i18n['antelope.ok']}':function() {
				var nodes = zTree.getCheckedNodes();
				
				var funcids = [];
				$(nodes).each(function(){
					funcids.push(this.id);
				});
				
				$.post(ctxPath + "/common/saveRoleAuthoritys.vot", {rolesid:$("#roleul2 label.active input").val(), 
					funcs:encodeURIComponent(JSON.stringify(funcids))}, function(data){
					if (data) {
						alert(data);
						return;						
					}
					alert("${i18n['antelope.setrightssucess']}");
					$("#authordialog").dialog("destroy");
				});
				
			},'${i18n['antelope.cancel']}':function() {
				$("#authordialog").dialog("destroy");
			}
		}
	});
}

// 添加所有用户到当前选中的角色当中
function addalluser(thisObj) {
	$.commonSubmit({
		button: thisObj,
		url: ctxPath + "/common/UserRoleOrgController/addAllUserToRole.vot?rolesid=" + $("#roleul2 label.active input").val(),
		callback: function (){ 
			$("#roleul2 label.active input").click();
		}
	});
}

function saveCurrSetting() {
	var nodes = zTree.getCheckedNodes();
	
	var funcids = [];
	$(nodes).each(function(){
		funcids.push(this.id);
	});
	
	$.post(ctxPath + "/common/saveRoleAuthoritys.vot", {rolesid:$("#roleul2 label.active input").val(), 
		funcs:encodeURIComponent(JSON.stringify(funcids))}, function(data){
		if (data) {
			alert(data);
			return;						
		}
		alert("${i18n['antelope.setrightssucess']}");
		$("#authordialog").dialog("destroy");
	});
}

//创建角色
function createRole() {
	$("#roledialog input").val('');
	submitRoleInner();
}

// 修改角色
function updateRole() {
	var selectedrole = $("#roleul2 label.active input").closest("label").data("item");
	$("#roledialog").resetForm(selectedrole);
	submitRoleInner();
}

// 新增或修改对话框提交
function submitRoleInner() {
	$("#roledialog").submitDialog({
		url:ctxPath + "/common/insertOrUpdateRole.vot",width:500,height:230,
		callback:readroles
	});
}

// 删除角色
function deleteRole() {
	$.post(ctxPath + "/common/UserRoleOrgController/getUsersCountByRolesid.vot?rolesid=" + $("#roleul2 label.active input").val(), function(data){
		if(parseInt(data) > 0) {
			alert("${i18n['antelope.rolehasuserinfo']}");
			return;
		} else {
			if (confirm("${i18n['antelope.confirmtodelrole']}")) {
				$.post(ctxPath + "/common/deleterole.vot", "rolesid="+$("#roleul2 label.active input").val(), function(data) {
					if (data) {
						alert(data);
						return;
					}
					alert("${i18n['antelope.deletesuccess']}");
					readroles();
				})
			}
		}
	});
}


</script>
</head>
<body class="sm_main">

<!-- 角色增改 -->
<form id="roledialog" style="display: none;">
	<input name="sid" type="hidden"/>
	<input name="parentsid" type="hidden"/>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.rolename']}：</span>
		<span class="cd_val">
			<input name="name" required="true"/>
		</span>
	</div>
</form>

<!-- 权限设置dialog -->
<form id="authordialog" style="display:none;">
	<ul id="tree" class="tree"></ul>	
</form>

<div class="ui-layout-west">
	<div id="rolecol" class="ui-widget ui-widget-content" style="height: 99%;margin: 2px;">
		<div id="roleheader" class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">${i18n['antelope.role']}</span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
				<%if (!"false".equals(SystemOpts.getProperty("enable_modify_userroleorgauthor"))) {%>
					<button onclick="createRole()" class="btn btn-sm btn-default glyphicon glyphicon-plus" style="height: 28px;width:34px;" title="${i18n['antelope.addrole']}"></button>
					<button onclick="updateRole()" class="btn btn-sm btn-default glyphicon glyphicon-pencil" style="height: 28px;width:34px;" title="${i18n['antelope.modifyrole']}"></button>
					<button onclick="deleteRole()" class="btn btn-sm btn-default glyphicon glyphicon-remove" style="height: 28px;width:34px;" title="${i18n['antelope.deleterole']}"></button>
				<%} %>
			</span>
		</div>
		<div id="roleouter" style="width: 100%; overflow: auto; position: relative;">
			<ul id="roleul" style="display:none;"></ul>
			
			<div id="roleul2" data-toggle="buttons">
			
			</div>
			
		</div>
	</div>
</div>
<div class="ui-layout-center" style="overflow-y:hidden; ">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px; overflow-y:auto; overflow-x:hidden;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">${i18n['antelope.rolerights']}</span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
				<button onclick="saveCurrSetting(this)" class="btn btn-sm btn-default" style="height: 28px; border:1px solid #cccccc;"><span class="glyphicon glyphicon-ok" style="padding-right:3px;"></span>${i18n['antelope.save']}</button>
			</span>
		</div>
		<ul id="userlist" class="tree"></ul>
	</div>
</div>
</body>
</html>