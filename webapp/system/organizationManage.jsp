<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.utils.SystemOpts"%>
<%@page import="antelope.db.DBUtil"%>
<%@page import="antelope.services.SessionService"%>
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

<script>

var zTree = null;
$(function(){
	
	
	
	readOrgInfo();

	$("#treecol").resize(function(){
		$("#org_tree").height($("#treecol").height()- 40);
	}).resize();
	
	$("#leaderselect").selectUsers({
		idinput : $("[name=parentusersid]"),
		nameinput : $("[name=parentuser]"),
		button:$("#selectparentuser")
	});
	
	
	toCreateTreeNode([],columns_);
});

function readOrgInfo() {
	zTree = $("#org_tree").unitTree({itemClick: zTreeOnClick});
}

function refreshCurrentNode() {
	zTree.reAsyncChildNodes(zTree.getSelectedNode().parentNode, "refresh");
}

// 树节点点击之后 
var dggridurl = "<jsp:include page="/include/inc_plugs.jsp?file=system/organization/dgdataurl.jsp"/>";
function zTreeOnClick() {
	/* if (!$.trim(dggridurl)) {
		dggridurl = "/common/getUsersByUnitsid.vot";
	} */
	
	$.getJSON(ctxPath + "/SysUserManagerController/common/getUsersByUnitsid.vot" + "?unitsid=" + encodeURIComponent(encodeURIComponent(zTree.getSelectedNode().sid)), function(data){
		toCreateTreeNode(data,columns_);
	});
}

var columns_ = {username:"用户名",name:"姓名"};


function selectRow(){
	$.ajaxSettings.async = false;
	var selectRows = $('<div id="checkrows"  style="width:400px">\
					<form id="queryForm">\
						<input type="checkbox" name="rowsname" value="name-姓名" nametext="name"/>姓名\
						<input type="checkbox" name="rowsname" value="username-用户名"  />用户名\
						<input type="checkbox" name="rowsname" value="gender-性别" />性别\
						<input type="checkbox" name="rowsname" value="birthday-出生日期"/>出生日期\
						<input type="checkbox" name="rowsname" value="age-年龄" />年龄\
						<input type="checkbox" name="rowsname" value="nation-民族"/>民族\
						<input type="checkbox" name="rowsname" value="p_affiliation-政治面貌"/>政治面貌\
						<input type="checkbox" name="rowsname" value="education-学历"/>学历\
						<input type="checkbox" name="rowsname" value="a_degree-学位"/>学位\
						<input type="checkbox" name="rowsname" value="professional-所学专业"/>所学专业\
						<input type="checkbox" name="rowsname" value="deptname-部门"/>部门\
						<input type="checkbox" name="rowsname" value="duties-职务"/>职务\
						<input type="checkbox" name="rowsname" value="w_time-参加工作时间"/>参加工作时间\
						<input type="checkbox" name="rowsname" value="e_time-进妇女基金会时间" />进妇女基金会时间\
						<input type="checkbox" name="rowsname" value="r_time-任职时间"/>任职时间\
						<input type="checkbox" name="rowsname" value="idcard-身份证号码" />身份证号码\
					</form>\
				</div>').initWidgets().dialog({
						width: 650,
						height: "auto",
						buttons:{
							"确定":function(){
								
								   columns_ = {};    
								   var a = $("#queryForm").serializeArray();    
								   $.each(a, function() {
								       if (columns_[this.value.split("-")[0]]) {    
								           if (!columns_[this.value.split("-")[0]].push) {    
								        	   columns_[this.value.split("-")[0]] = [columns_[this.value.split("-")[1]]];    
								           }    
								           columns_[this.value.split("-")[0]].push(this.value.split("-")[1] || '');
								       } else {    
								    	   columns_[this.value.split("-")[0]] = this.value.split("-")[1] || '';    
								       }
								   });
								selectRows.dialog("close");
								toCreateTreeNode([],columns_);
								selectRows.dialog( "destroy" ).remove();
								/* location.reload(); */
							}
						}
					})
					
					for (var property in columns_){
						$("#queryForm input[value$='"+columns_[property]+"']").attr("checked",true);
					   }
}


function toCreateTreeNode(data,columns_) {
	if ($(".datagrid_container").data("datagridinited")) {
		$(".datagrid_container").datagrid("destroy");
	}
	
	$(".datagrid_container").data("datagridinited", true);
	$(".datagrid_container").datagrid({
		selectedItem: null,
		selectedItems:[],
		dataProvider:data
		<%if (!"false".equals(SystemOpts.getProperty("enable_modify_userroleorgauthor"))) {%>
		,selectionMode:"multipleRows"
		<%}%>
		,columns:columns_
		<%if (!"false".equals(SystemOpts.getProperty("enable_modify_userroleorgauthor"))) {%>
		,buttons:[{
				text:'update',
				click:function() {
					$("#userdialog").setCurrentState("update");
					$("#userdialog").resetForm(this);
					$("#userdialog [name=unitsid]").val(zTree.getSelectedNode().sid);
					$("#userdialog").submitDialog({
						title:$.i18n("antelope.modifyuserinfo"),
						url:ctxPath + "/SysUserManagerController/common/insertOrUpdateUser.vot",width:500,height:300,
						callback:zTreeOnClick
					});
				}
			},
			
			{ text:'del', click:function() {
					if (confirm($.i18n('antelope.confirmtodeluser'))) {
						$.post(ctxPath + "/common/deleteuser.vot", "users="+encodeURIComponent(encodeURIComponent(JSON.stringify([this]))), function(data) {
							if (data) {
								alert(data);
								return;			
							}
							alert($.i18n('antelope.deletesuccess'));
							zTreeOnClick();
						})
					}
				}, visibleFunction:function (obj){return obj.username != 'admin';}
			}
		]
		<%}%>
	});
	
}

function importOrg() {
	$.post(ctxPath + "/common/importOrgnization.vot");
}

function create() {
	$("#editdialog input").val('');
	if (zTree.getSelectedNode() == null) {
		alert($.i18n('antelope.pleaseselectparentnode'));
		return;
	}
		
	$("#editdialog [name=parentsid]").val(zTree.getSelectedNode().sid);
	submitInner();
}

function update() {
	if (zTree.getSelectedNode() == null) {
		alert($.i18n('antelope.pleaseselnodetomodify'));
		return;
	}
	
	if (zTree.getSelectedNode().id == 'root') {
		alert($.i18n('antelope.rootnodecannotmodify'));
		return;
	}
	
	$("#editdialog").resetForm(zTree.getSelectedNode());
	if($("#unittype").val()==1||$("#unittype").val()==2||$("#unittype").val()==4){
		$("#zjj").show();
	}else{
		$("#zjj").hide();
	}
	submitInner();
}

//新增或修改对话框提交
function submitInner() {
	var editdialogbtns = {};
	editdialogbtns[$.i18n('antelope.ok')] = function(){
		if ($("#editdialog").validate()) {
			$.post(ctxPath + "/common/insertOrUpdateUnit.vot", encodeURI($("#editdialog").serialize()), function(){
				alert($.i18n('antelope.savesuccess'));
				$("#editdialog").dialog("close");
				refreshCurrentNode();
			});
		}
	};
	editdialogbtns[$.i18n('antelope.cancel')] = function() {
		$("#editdialog").dialog("close");
	};
	$("#editdialog").dialog({
		 show: $.dialogeffect.show,
		 hide: $.dialogeffect.hide,
		buttons:editdialogbtns, width:500, height:230, modal:true
	});
}

// 删除组织机构
function deleteorg() {
	if (zTree.getSelectedNode() == null) {
		alert($.i18n('antelope.pleaseseltodelete'));
		return;
	}
	
	if (zTree.getSelectedNode().nodes && zTree.getSelectedNode().nodes.length) {
		alert($.i18n('antelope.cannotdeletehassub'));
		return;
	}
	
	if ($(".datagrid_container").datagrid("option", "dataProvider").length) {
		alert($.i18n('antelope.cannotdelforuserinfo'));
		return;
	}
	
	if (confirm($.i18n('antelope.confirmtodelthisorg'))) {
		$.get(ctxPath + "/common/deleteunit.vot?unitsid="+zTree.getSelectedNode().sid, function(data) {
			if (data) {
				alert(data);
				return;
			}
			
			refreshCurrentNode();
		});
	}
}


// 创建用户
function createUser() {
	$("#userdialog").setCurrentState("create").resetForm();
	//$("#userdialog input").val('');
	if (zTree.getSelectedNode() == null) {
		alert($.i18n('antelope.pleaseselorg'));
		return;
	}
		
	$("#userdialog [name=unitsid]").val(zTree.getSelectedNode().sid);
	
	$("#userdialog").submitDialog({
		url:ctxPath + "/SysUserManagerController/common/insertOrUpdateUser.vot",width:500,height:300,
		callback:zTreeOnClick
	});
}
// 修改用户
function updateUser() {
	$("#userdialog").setCurrentState("update");
	
	var items = $(".datagrid_container").datagrid("option","selectedItems");
	if (items.length == 0) {
		alert($.i18n('antelope.pleaseseloneusertomodify'));
		return;
	}
	if (items.length > 1) {
		alert($.i18n('antelope.onlyoneusercansel'));
		return;
	}
	
	$("#userdialog").resetForm(items[0]);
	
	$("#userdialog [name=unitsid]").val(zTree.getSelectedNode().sid);
	$("#userdialog").submitDialog({
		title:$.i18n('antelope.modifyuserinfo'),
		url:ctxPath + "/SysUserManagerController/common/insertOrUpdateUser.vot",width:500, height:300,
		callback:zTreeOnClick
	});
}

function deleteUser() {
	var items = $(".datagrid_container").datagrid("option","selectedItems");
	if (!$.isArray(items) || items.length == 0) {
		alert($.i18n('antelope.seloneuseratleast'));
		return;
	}
	
	if (confirm($.i18n('antelope.confirmtodelthisuser'))) {
		$.post(ctxPath + "/common/deleteuser.vot", "users="+encodeURIComponent(encodeURIComponent(JSON.stringify(items))), function(data) {
			if (data) {
				alert(data);
				return;
			}
			alert($.i18n('antelope.deletesuccess'));
			zTreeOnClick();
		})
	}
}

function onpasswordchange() {
	$("[name=passwordchanged]").val("true");
}

var authortree;

//权限设置
function setAuthoritys() {
	$("#tree").empty();
	
	var useritems = $(".datagrid_container").datagrid("option","selectedItems");
	
	if (!useritems.length) {
		alert($.i18n('antelope.pleaseseltosetrights'));
		return;
	}
	
	var tusersid = "";
	if (useritems.length == 1) {
		tusersid = useritems[0].sid; 
	}
	$.showBusyState();
	$.getJSON(ctxPath + "/common/getfunctiondatas.vot", {usersid:tusersid}, function(data){
		var treedata = [{id:'root', open:true, name:$.i18n('antelope.allfunctions'), nodes:data}];
		var setting = {
			expandSpeed : "",
			showLine : true,
			checkable : true,
			checkedCol :"checked",
			checkType: { "Y": "ps", "N": "ps" }
		};
		
		authortree = $("#tree").zTree(setting, treedata);
		$.hideBusyState();
	});
	useritems = encodeURIComponent(JSON.stringify(useritems));
	
	
	var setrightsbtn = {};
	setrightsbtn[$.i18n('antelope.ok')] = function() {
		var nodes = authortree.getCheckedNodes();
		
		var funcids = [];
		$(nodes).each(function(){
			funcids.push(this.id);
		});
		
		$.post(ctxPath + "/common/saveUserAuthoritys.vot", {users:useritems, funcs:encodeURIComponent(JSON.stringify(funcids))}, function(data){
			if (data) {
				alert(data);
				return;						
			}
			alert($.i18n('antelope.setrightssucess'));
			$("#authordialog").dialog("destroy");
		});
		
	};
	setrightsbtn[$.i18n('antelope.cancel')] = function() {
		$("#authordialog").dialog("destroy");
	};
	
	
	$("#authordialog").dialog({
		width:700, height:400, title:$.i18n('antelope.setrights'),
		buttons:setrightsbtn
	});
}

function stateChage(obj){
	if(obj.value==1||obj.value==2||obj.value==4){
		$("#zjj").show();
	}else{
		$("#zjj").hide();
	}
}



var removeddialog = null;
var dialogdiv =  $('<div class="excelss"  style="display: none;height: auto;width:600px">\
						<form id="decodedfileform"  method="post"  enctype="multipart/form-data">\
						<div class="condi_container">\
						<span class="cd_name"></span>\
						<span class="cd_val"><button type="button"   class="btn btn-sm btn-default" onclick="downloadImpTemp__()" style="padding:4px 15px 2px;margin-top:3px;font-size:13px">模板下载</button></span>\
						<span class="cd_name"></span>\
						<span class="cd_val"><input name="importexcelfile" type="file" style="height:25px;"/></span>\
						</form>\
						</div>\
					</div>');
function exportUser(){
	dialogdiv.dialog({
		width: 650,
		height: "auto",
		buttons:{
			"导入":function(){
				$("#decodedfileform").postIframe(ctx + "/SysUserManagerController/UserRoleOrgController/importExcelUser.vot?importnum="+encodeURI(encodeURI($("#importnum").val())), function(data) {
					dialogdiv.dialog("close");
					location.reload();  
				});
			}
		}
	});
}
function downloadImpTemp__() {
	$.postIframe(ctx +  "/SysUserManagerController/downloadImportExcelTmplUser.vot");
}

//分管部门权限设置
function setDeptAllAuthoritys() {
	$("#tree").empty();
	
	var useritems = $(".datagrid_container").datagrid("option","selectedItems");
	
	if (!useritems.length) {
		alert($.i18n('antelope.pleaseseltosetrights'));
		return;
	}
	
	var tusersid = "";
	if (useritems.length == 1) {
		tusersid = useritems[0].sid; 
	}
	$.showBusyState();
	$.getJSON(ctxPath + "/SysUserManagerController/branchmanagementrole/BranchRoleOrgController/geAllDeptData.vot", {usersid:tusersid}, function(data){
		var treedata = [{id:'root', open:true, sid:'orgroot', name:"组织机构", nodes:data}];
		var setting = {
			expandSpeed : "",
			showLine : true,
			checkable : true,
			checkedCol :"checked",
			checkType: { "Y": "ps", "N": "ps" }
		};
		
		authortree = $("#tree").zTree(setting, treedata);
		$.hideBusyState();
	});
	useritems = encodeURIComponent(JSON.stringify(useritems));
	
	
	var setrightsbtn = {};
	setrightsbtn[$.i18n('antelope.ok')] = function() {
		var nodes = authortree.getCheckedNodes();
		
		var funcids = [];
		$(nodes).each(function(){
			funcids.push(this.sid);
		});
		
		$.post(ctxPath + "/SysUserManagerController/branchmanagementrole/BranchRoleOrgController/saveUserDeptAuthoritys.vot", {users:useritems, funcs:encodeURIComponent(JSON.stringify(funcids))}, function(data){
			if (data) {
				alert(data);
				return;						
			}
			alert($.i18n('antelope.setrightssucess'));
			$("#authordialog").dialog("destroy");
		});
		
	};
	setrightsbtn[$.i18n('antelope.cancel')] = function() {
		$("#authordialog").dialog("destroy");
	};
	
	
	$("#authordialog").dialog({
		width:700, height:400, title:$.i18n('antelope.setrights'),
		buttons:setrightsbtn
	});
}


</script>
</head>
<body class="sm_main ui-layout">

<!-- 权限设置dialog -->
<form id="authordialog" style="display:none;">
	<ul id="tree" class="tree"></ul>	
</form>


<!-- 机构增改 -->
<form id="editdialog" style="display: none;">
	<input name="sid" type="hidden"/>
	<input name="parentsid" type="hidden"/>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.orgnizationname'] }：</span>
		<span class="cd_val">
			<input name="name" required="true"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.orgnizationtype'] }：</span>
		<span class="cd_val">
			<select id="unittype" name="unittype" dataProvider="sys_unittype" onChange="stateChage(this)"></select>
		</span>
	</div>
</form>

<!-- 用户增改 -->
<form id="userdialog" style="display: none;">
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
		<span class="cd_name">${i18n['antelope.gender'] }：</span>
		<span class="cd_val">
			<div class="dropdown" style="float: left;" state="disabled:false;disabled.view:true;">
				<button style="width: 170px;" class="btn btn-sm btn-default dropdown-toggle" type="button" data-toggle="dropdown">
				    <span class="buttonlabel" defaultbtnlabel="(未选择)" >(未选择)</span>
				    <span class="caret"></span>
				  </button>
				  <input name="gender" type="hidden"/>
				  <ul class="dropdown-menu" role="menu">
				    <li role="presentation" value="0"><a role="menuitem" tabindex="-1" href="#">男</a></li>
				   	<li role="presentation" value="1"><a role="menuitem" tabindex="-1" href="#">女</a></li>
				  </ul>
			  </div>
		
			<!-- <select name="gender" dataProvider="sys_usergender"></select> -->
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.email'] }：</span>
		<span class="cd_val">
			<input name="mail" />
		</span>
	</div>
	<jsp:include page="/include/inc_plugs.jsp">
		<jsp:param name="file" value="system/organization/registerinfo.jsp"/>
	</jsp:include>
</form>

<div class="ui-layout-west">
	<div id="treecol" class="ui-widget ui-widget-content" style="height: 99%;margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold; ">${i18n['antelope.orgnizationroot'] }</span>
			<%if (!"false".equals(SystemOpts.getProperty("enable_modify_userroleorgauthor"))) {%>
			<span style="display: block; position: absolute; top: 5px; right: 5px; "  class="btn-group">
				<button onclick="create()" class="btn btn-sm btn-default glyphicon glyphicon-plus" style="height: 28px;width:34px;" type="button" title="${i18n['antelope.addorganization'] }"></button>
				<button onclick="update()" class="btn btn-sm btn-default glyphicon glyphicon-pencil" style="height: 28px;width:34px;" type="button" title="${i18n['antelope.modifyorganization'] }"></button>
				<button onclick="deleteorg()" class="btn btn-sm btn-default glyphicon glyphicon-remove" style="height: 28px;width:34px;" type="button" title="${i18n['antelope.deleteorganization'] }"></button>
			</span>
			<%} %>
		</div>
		<ul id="org_tree" class="tree"></ul>
	</div> 
</div>
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 99%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 24px;font-weight: bold;">${i18n['antelope.orguserinfo'] }</span>
		</div>
		<br/>
		<div>
			<span style="display: block; top: 5px; left: 5px;"   class="btn-group">
				<jsp:include page="/include/inc_plugs.jsp">
					<jsp:param name="file" value="system/organization/rightTopBtn.jsp"/>
				</jsp:include>
				<%if (!"false".equals(SystemOpts.getProperty("enable_modify_userroleorgauthor"))) {%>
				<button onclick="setAuthoritys()" class="btn btn-sm btn-default" style="height: 28px; "><span class=" glyphicon glyphicon-wrench" style="font-family:  Glyphicons Halflings;font-size:12px; padding-right:3px;"></span>${i18n['antelope.userright'] }</button>
					<%if (!"false".equals(SystemOpts.getProperty("userorg_showcreatebtn"))) {%>
					<button onclick="createUser()" class="btn btn-sm btn-default " style="height: 28px;"><span class=" glyphicon glyphicon-plus" style="font-family:  Glyphicons Halflings;font-size:12px; padding-right:3px;"></span>${i18n['antelope.adduserinfo'] }</button>
					<%} %>
				<button onclick="updateUser()" class="btn btn-sm btn-default " style="height: 28px;"><span class=" glyphicon glyphicon-pencil" style="font-family:  Glyphicons Halflings;font-size:12px; padding-right:3px;"></span>${i18n['antelope.modifyuserinfo'] }</button>
				<button onclick="deleteUser()" class="btn btn-sm btn-default " style="height: 28px;"><span class=" glyphicon glyphicon-remove" style="font-family:  Glyphicons Halflings;font-size:12px; padding-right:3px;"></span>${i18n['antelope.deleteuserinfo'] }</button>
				<button onclick="selectRow()" class="btn btn-sm btn-default " style="height: 28px;"><span class=" glyphicon glyphicon-plus" style="font-family:  Glyphicons Halflings;font-size:12px; padding-right:3px;"></span>显示列信息</button>
				<button onclick="exportUser()" class="btn btn-sm btn-default " style="height: 28px;"><span class=" glyphicon glyphicon-plus" style="font-family:  Glyphicons Halflings;font-size:12px; padding-right:3px;"></span>导入用户</button>
				<button onclick="setDeptAllAuthoritys()" class="btn btn-sm btn-default glyphicon glyphicon-pencil" style="height: 28px;">分管部门</button>
				<%} %>
			</span>
		</div>
		<div style="clear:left;"></div>
		<div class="datagrid_container" style="margin-top: 10px;"></div>
	</div>
</div>
</body>
</html>