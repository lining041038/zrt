<%@ page language="java" pageEncoding="utf-8"%>
<script>
$(function(){
	$("#mysingleform").parent().unbind("resetFormComplete").bind("resetFormComplete", function(){
	//	alert($(".mysinglegridform [name=name]").val($(".mysinglegridform [name=name]").val() + "（js自定义追加）"));
	});
	
	$("#mysingleform").parent().rebind("openViewForm", function() {
		resetRoleCheckboxs("view");
	});
	
	$("#mysingleform").parent().rebind("openedCreateForm", function() {
		resetRoleCheckboxs("create");
	});
	
	$("#mysingleform").parent().rebind("openedUpdateForm", function() {
		resetRoleCheckboxs("update");
	});
});


function resetRoleCheckboxs(eventtype) {
	$("#rolecheckbox").html("");
	
	
	var seldrolesids = $("#mysingleform").closest("form").data("formdata") ? $("#mysingleform").closest("form").data("formdata").rolesids : "";
	if (!seldrolesids) {
		seldrolesids = [];
		if (zTree.tree("getSelectedNode").sid != 'treeroot') {
			seldrolesids.push(zTree.tree("getSelectedNode").sid);	
		}
	}
	
	var seldrolesidmap = {};
	for (var i = 0; i < seldrolesids.length; ++i) {
		seldrolesidmap[seldrolesids[i]] = true;
	}
	
	$.post(ctx + "/system/usermanage/usermanage/UserManageController/getAllRoles.vot", function(roleinfos) {
		for (var i = 0; i < roleinfos.length; ++i) {
			var checked = "";
			var disabled = "";
			if (seldrolesidmap[roleinfos[i].value]) {
				checked = "checked";
			}
			
			// 超级管理员的系统管理员角色不能去除
			if ($("#mysingleform").closest("form").data("formdata") && $("#mysingleform").closest("form").data("formdata").sid == '1234' && roleinfos[i].value == '153') {
				disabled = "disabled";
			}
			
			if (eventtype == 'view') { // 查看时均为禁用状态
				disabled = "disabled";	
			}
			
			$("#rolecheckbox").append("<input " + disabled + " " + checked + " type='checkbox' style='width:40px;' name='rolesid' value='" + roleinfos[i].value + "'/> " + roleinfos[i].label + "<br/>");
		}
	}, "json");
}


function onpasswordchange() {
	$("[name=passwordchanged]").val("true");
}
var thazTree;
function tosetAuthoritys() {
	$("#tree").empty();
	
	var useritems = [this]
	
	if (!useritems.length) {
		alert("${i18n['antelope.pleaseseltosetrights']}");
		return;
	}
	
	var tusersid = "";
	if (useritems.length == 1) {
		tusersid = useritems[0].sid; 
	}
	
	useritems = encodeURIComponent(JSON.stringify(useritems));
	
	if ($("#theauthordialog").length == 0) {
		$('<form id="theauthordialog" style="display:none;">\
				<ul id="hdtree" class="tree"></ul>\
				</form>').appendTo("body");
	}
	
	$("#theauthordialog").dialog({
		width:700, height:400, title:"${i18n['antelope.setrights']}", modal:true,
		buttons:{
			'${i18n['antelope.ok']}':function() {
				var nodes = thazTree.getCheckedNodes();
				
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
					$("#theauthordialog").dialog("destroy");
				});
				
			},'${i18n['antelope.cancel']}':function() {
				$("#theauthordialog").dialog("destroy");
			}
		}
	});
	
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
		
		thazTree = $("#hdtree").zTree(setting, treedata);
		$.hideBusyState();
	});
}


function configVisibleFunc() {
	return this.sid != '1234';
}

</script>
<!-- 权限设置dialog -->
<div id="mysingleform" style="width: 433px; height: 255px;">
	<input name="sid" type="hidden"/>
	<input name="unitsid" type="hidden"/>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.username'] }：</span>
		<span class="cd_val">
			<input name="username" required="true" state='disabled:false; disabled.view:true;' />
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.password'] }：</span>
		<span class="cd_val">
			<input onchange="onpasswordchange()" name="password" required2="true" type="password" state='disabled:false; disabled.view:true;' />
			<input name="passwordchanged" type="hidden"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.user'] }：</span>
		<span class="cd_val">
			<input name="name" required="true" state='disabled:false; disabled.view:true;' />
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.gender'] }：</span>
		<span class="cd_val">
			<select name="gender" dataProvider="sys_usergender" state='disabled:false; disabled.view:true;' ></select>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.role'] }：</span>
		<span class="cd_val">
			<div id="rolecheckbox" style="width:189px; padding-left:89px; positon:relative; top:-20px;">
			</div>
		</span>
	</div>
	<jsp:include page="/include/inc_plugs.jsp">
		<jsp:param name="file" value="system/organization/registerinfo.jsp"/>
	</jsp:include>
</div>
