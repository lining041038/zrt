<%--
	title:用户选择
	author:lining
	
	modified by xcc
	added opts{filterdept：false }根据登录用户所属部门过滤组织机构,即只显示本部门人员及子部门
	added opts{deptheader:false}向上找部门负责人，如果没有，则整个组织机构人员
--%>
<%@page import="antelope.springmvc.BaseController"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
.splitdiv{width: 30%; height: 100%;float: left;}
</style>
<script>
var selectedusers = '<%=request.getParameter("users")%>';
var isusername = '<%=request.getParameter("isusername")%>';
var issingle = <%=request.getParameter("issingle")%>; // 是否为单选人
var filterrole = '<%=request.getParameter("filterrole")%>';
var filterdept = '<%=request.getParameter("filterdept")%>';//是否过滤部门
var deptheader = '<%=request.getParameter("deptheader")%>';//向上找部门负责人，如果没有，则整个组织机构人员
var zTree = null;
$(function(){
	selectedusers = decodeURIComponent(selectedusers);
	 
	
	$.getJSON(ctxPath + "/common/hasDeptHeader.vot", function(hasDeptHeader){
		//是否设置部门管理员
    if(hasDeptHeader&&hasDeptHeader.hasDeptHeader){
		$.getJSON(ctxPath + "/common/getOrgnizationTreedata.vot?filterdept="+filterdept+"&deptheader="+deptheader, function(data){
			var treedata = [{id:'root', open:true, sid:deptheader?'role':'orgroot', name:deptheader?'负责人':'组织机构', nodes:data}];
			var setting = {
				expandSpeed : "",
				showLine : true,
				checkable : false,
				callback : {
				   click: zTreeOnClick
				}
			};
			zTree = $("#org_tree").zTree(setting, treedata);
			 
		});
	  }else{
		  var setting = {
					expandSpeed : "",
					showLine : true,
					checkable : false,
					async: true,
					asyncUrl: ctxPath + "/common/UserRoleOrgController/getAsyncOrgnizationOrDeptTreedata.vot?filterdept="+filterdept,
					asyncParam: ['sid'],
					callback : {
					   click: zTreeOnClick
					}
				};
				zTree = $("#org_tree").zTree(setting);
			
		}
	});
	
	$.post(ctxPath + "/common/getSelectedUserInfos.vot?isusername="+isusername,encodeURI($.param({usersids:selectedusers})), function(data){
		$(data).each(function() {
			$("<option value='"+this.sid+"'>"+this.name+"</option>").data("item", this).appendTo($("#selectedusers"));
		});
	},"json");
	
	$("#usertosel, #selectedusers").css("width", function(){
		return $(this).parent().width();
	}).css("height",function(){
		return $(this).parent().height();
	});
});

function zTreeOnClick() {
	 
	$.getJSON(ctxPath + "/common/getUsersByUnitsid.vot?unitsid=" + encodeURIComponent(encodeURIComponent(zTree.getSelectedNode().sid)) + "&filterrole="+encodeURIComponent(filterrole), function(data){
		$("#usertosel").empty();
		$(data).each(function() {
			$("<option value='"+this.sid+"'>"+this.name+"</option>").data("item", this).appendTo($("#usertosel"));
		});
		$("#usertosel").css("width", function(){
			return $(this).parent().width();
		}).css("height",function(){
			return $(this).parent().height();
		});
	});
}

function selectone() {
	var selected = $("#usertosel option:selected");
	if (issingle) {
		$("#selectedusers option").remove();
	}
	if (!$("#selectedusers option").filter(function(){
		return this.value == selected.val(); 
	}).size()) {
		setTimeout(function(){
			$("#usertosel option:selected").clone(true).appendTo("#selectedusers");
		}, 100);
	}
	
	//$("#selectedusers").mouseover();
}

function adduser() {
	var selected = $("#usertosel option:selected");
	
	if (issingle && selected.length > 1) {
		alert("只能选择一个用户！");
		return;
	}
	
	if (issingle)
		$("#selectedusers option").remove();
	
	selected.each(function() {
		var thisObj = this;
		if (!$("#selectedusers option").filter(function(){
			return $(thisObj).val() == $(this).val();
		}).size()) {
			setTimeout(function(){
				$(thisObj).clone(true).appendTo("#selectedusers");
			}, 100);
		}
	});
}

function removeUser() {
	$("#selectedusers option:selected").remove();
}

function removeOne() {
	$("#selectedusers option:selected").remove();
}

function getSelectedUser() {
	var seldopts = $("#selectedusers option");
	var items = [];
	for (var i = 0; i < seldopts.length; i++){
		items.push($(seldopts[i]).data("item"));
	}
	return items;
}

</script>
</head>
<body>
	<div class="splitdiv">
		<div id="org_tree" class="tree"></div>
	</div>
	<div class="splitdiv">
		<select ondblclick="selectone()" id="usertosel" style="width: 100%; height: 100%;" multiple="multiple"></select>
	</div>
	<div class="splitdiv" style="width: 9%; padding-top: 140px; text-align: center;">
		<button onclick="adduser()" class="button" style="width: 70px;margin-bottom: 20px;">添加&gt;</button>
		<button onclick="removeUser()" class="button" style="width: 70px;">&lt;删除</button>
	</div>
	<div class="splitdiv">
		<select ondblclick="removeOne()" id="selectedusers" style="width: 100%; height: 100%;" multiple="multiple"></select>
	</div>
</body>
</html>

