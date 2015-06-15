<%--
	title:用户选择
	author:lining
	
	modified by xcc
	added opts{filterdept：false }根据登录用户所属部门过滤组织机构,即只显示本部门人员及子部门
--%>
<%@page import="antelope.springmvc.BaseComponent"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
.splitdiv{width: 30%; height: 100%;float: left;}
.rolelist{overflow: auto;}
#roleul{width: 100%;}
#roleul label{width: 100%;}
.pagearea span{float:right;cursor: pointer;}
html,body{height: 100%;}
</style>
<script>
var selectedusers = '<%=request.getParameter("users")%>';
var isusername = '<%=request.getParameter("isusername")%>';
var issingle = <%=request.getParameter("issingle")%>; // 是否为单选人
var filterrole = '<%=request.getParameter("filterrole")%>';
var filterdept = '<%=request.getParameter("filterdept")%>';//是否过滤部门
var zTree = null;
$(function(){
	selectedusers = decodeURIComponent(selectedusers);
	$("body").resize(function(){
		$("#rolelist,#org_tree").height($(this).height() - 50);
	}).resize();
	
	$( "#roleul" ).buttonset();
	
	// 添加点击事件
	$("#roleul li input").live("click", function(){
		rolepageurl = ctxPath + "/common/getUsersByRolesid.vot?rolesid=" + $(this).val();
		gotoPage();
		$('.pagearea').show();
	});
	
	/*读取角色信息*/
	$.getJSON(ctxPath + "/common/getAllRoles.vot", function(data) {
		$(data).each(function(){
			$("#roleul").append($('<li><input type="radio" id="' + this.sid + '" value="' + this.sid + '" name="radio" /><label for="'+this.sid+'">'+this.name+'</label></li>')
					.data("item",this));
		});
		$( "#roleul" ).buttonset("refresh");
	});
	 
	zTree = $("#org_tree").tree({
		asyncUrl: ctxPath + "/common/UserRoleOrgController/getAsyncOrgnizationOrDeptTreedata.vot?filterdept="+filterdept,
		locateUrl: ctxPath + "/common/UserRoleOrgController/getCurrentUsersDeptsPath.vot",
		click: zTreeBeforeAsync
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

var rolecurrPage;
var rolepageurl;
// 通过角色选择人员时翻页
function gotoPage(pagecurr) {
	if (pagecurr) {
		$.getJSON(rolepageurl, {numPerPage:30, page:rolecurrPage + pagecurr}, function(data){
			rolecurrPage = data.currPage;
			$("#pageinfospan").text(data.currPage + "/" + data.totalPage);
			recreateMiddleUserOption(data.currList);
		});	
	} else {
		$.getJSON(rolepageurl, {numPerPage:30}, function(data){
			rolecurrPage = data.currPage;
			$("#pageinfospan").text(data.currPage + "/" + data.totalPage);
			recreateMiddleUserOption(data.currList);
		});	
	}
}



function zTreeBeforeAsync() {
	$.getJSON(ctxPath + "/common/getUsersByUnitsid.vot?unitsid=" + encodeURIComponent(encodeURIComponent(zTree.tree("getSelectedNode").sid)) + "&filterrole="+encodeURIComponent(filterrole), function(data){
		recreateMiddleUserOption(data);
		$('.pagearea').hide();
	});
}

function recreateMiddleUserOption(data) {
	$("#usertosel").empty();
	$(data).each(function() {
		$("<option value='"+this.sid+"'>"+this.name+"</option>").data("item", this).appendTo($("#usertosel"));
	});
	$("#usertosel").css("width", function(){
		return $(this).parent().width();
	}).css("height",function(){
		return $(this).parent().height() - 20;
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
		<div class="tabs">
			<ul> 
				<li><a href="#org_tree" id="tabs-1">单位选择</a></li>
				<li><a href="#rolelist" id="tabs-2">角色选择</a></li>
			</ul>
			<ul id="org_tree" class="tree" style="clear:both;"></ul>
			<div id="rolelist" class="rolelist" style="position: relative;clear:both;">
				<ul id="roleul"></ul>
			</div>
		</div>
	</div>
	<div class="splitdiv">
		<div class="pagearea" style="text-align: right; display: none;">
			<span onclick="gotoPage(1)" class="ui-icon ui-icon-triangle-1-e"></span>
			<span id="pageinfospan">
			</span><span class="ui-icon ui-icon-triangle-1-w" onclick="gotoPage(-1)"></span>
		</div>
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

