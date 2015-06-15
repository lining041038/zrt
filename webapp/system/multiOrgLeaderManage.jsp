<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
label{width: 100%;}
#leaderul{margin: 0;padding: 0; height: 200px;}
#leaderul li{padding: 0 3px 0 1px; margin: 1px 0 0 0;}
#leaderaddbtn{width: 100%;}
</style>
<script>
$(function(){
	$('body').layout({fxName:'none'});
	
	$( "#leaderul" ).buttonset();
	readLeaderInfos();
	
	$("#leadercol").resize(function(){
		$("#leaderouter").height($(this).height() - $("#leaderheader").height() - 10);
	}).resize();
	$("#orgcol").resize(function(){
		$("#org_tree").height($(this).height() - $("#orgheader").height() - 20);
	}).resize();
	
	readOrgInfo();
});

function readLeaderInfos() {
	// 查询存在所领导部门的人员
	$.getJSON(ctxPath + "/common/getMultiLeaderusers.vot", function(data){
		$("#leaderul").html("");
		$(data).each(function(){
			$("#leaderul").append($('<li usersid="'+this.sid+'"><input onclick="resetLeaderOrgs(this)" type="radio" id="' + this.sid + '" value="' + this.sid + '" name="radio" /><label for="'+this.sid+'">'+this.name+'</label></li>')
					.data("item",this));
		});
		$( "#leaderul" ).buttonset("refresh");
	});
}

var zTree;
var treedata = null;

function readOrgInfo() {
	$.showBusyState();
	$.getJSON(ctxPath + "/common/getOrgnizationTreedata.vot", function(data){
		treedata = [{id:'root', open:true, sid:'orgroot', name:"组织机构", nodes:data}];
		var setting = {
			expandSpeed : "",
			showLine : true,
			checkable : true,
			checkedCol: "checked",
			checkType: { "Y": "s", "N": "s" },
			callback : {
				change: zTreeOnChange
			}
		};
		zTree = $("#org_tree").zTree(setting, treedata);
		$.hideBusyState();
	});
}

var cussusersid = '';

function zTreeOnChange(event, treeId, treeNode) {
	if (!cussusersid) {
		alert("请选择一个用户！");
		return;
	}
	
	var nodes = zTree.getCheckedNodes(true);
	var nodesid = [];
	$(nodes).each(function(){
		nodesid.push(this.sid);
	});
	
	$.post(ctxPath + "/common/saveCheckedLeaderOrgs.vot?usersid="+cussusersid, "orgs=" + encodeURIComponent(encodeURIComponent(JSON.stringify(nodesid))), function(data){
		if (data) {
			alert(data);
			return;
		}	
	});
	
	//alert(nodes.length);
	//alert(treeNode.sid + ", " + treeNode.name);
}

function zTreeOnClick() {
	
}

function resetLeaderOrgs(thisObj) {
	cussusersid = thisObj.value;
	$.showBusyState();
	$.getJSON(ctxPath+"/common/getMultiOrgLeaderOrgs.vot?usersid="+thisObj.value, function(data){
		var nodedata = zTree.getNodes();
		for (var i = 0; i < nodedata.length; i++) {
			setcheckbiaozhi(nodedata[i], data);
		}
		zTree.refresh();
		$.hideBusyState();
	});
}

function setcheckbiaozhi(parantnode, checkeddata) {
	var has = false;
	for (var i = 0; i < checkeddata.length; i++) {
		if (parantnode.sid == checkeddata[i].sid) {
			has = true;
			break;
		}
	}
	if (has) {
		parantnode.checked = true;
	} else {
		parantnode.checked = false;
	}
	if (parantnode.nodes && parantnode.nodes.length) {
		for (var i = 0; i < parantnode.nodes.length; i++) {
			setcheckbiaozhi(parantnode.nodes[i], checkeddata);
		}
	}
}

var selectedleaders = [];
function createLeader() {
	var selectusersids = [];
	$(selectedleaders).each(function(){
		selectusersids.push(this.sid);
	});
	
	$.selectUsers({
		callback:function(users){
			selectedleaders = users;
			
			var newselectedleadersids = [];
			
			$(selectedleaders).each(function(){
				newselectedleadersids.push(this.sid);
				if ($.inArray(this.sid, selectusersids) != -1)
					return;
				$("#leaderul").append($('<li usersid="'+this.sid+'"><input onclick="resetLeaderOrgs(this)" type="radio" id="' + this.sid + '" value="' + this.sid + '" name="radio" /><label for="'+this.sid+'">'+this.name+'</label></li>')
						.data("item",this));
			});
			$(selectusersids).each(function(){
				var isin = false;
				for (var i = 0; i < newselectedleadersids.length; i++) {
					if (this == newselectedleadersids[i]) {
						isin = true;
					}
				}
				
				if (!isin) {
					var thisObj = this;
					$("[usersid]").each(function(){
						if ($(this).attr("usersid") == thisObj) {
							$(this).remove();
							return false;
						}
					});
				}
			});
			
			$("#leaderul :radio:eq(0)").click().prop("checked", true);
			$( "#leaderul" ).buttonset("refresh");
		},
		selectedusers:selectusersids
	});
	
}

/**
 * 删除领导
 */
function deleteLeader() {
	$.submitDelete({
		url:ctxPath + "/common/deleteMultiOrgLeader.vot?usersid="+cussusersid,
		callback:readLeaderInfos,
		confirmText:"确认要删除此领导吗？"
	});
}

</script>
</head>
<body class="sm_main">

<div class="ui-layout-west">
	<div id="leadercol" class="ui-widget ui-widget-content" style="height: 100%;margin: 2px;">
		<div id="leaderheader" class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">合规归口人</span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
				<button onclick="createLeader()" class="button ui-icon-plusthick" style="height: 28px;width:34px;" title="添加领导"></button>
				<button onclick="deleteLeader()" class="button ui-icon-closethick" style="height: 28px;width:34px;" title="删除领导"></button>
			</span>
		</div>
		<div id="leaderouter" style="width: 100%; overflow: auto; position: relative;">
			<ul id="leaderul"></ul>
		</div>
	</div>
</div>
<div class="ui-layout-center">
	<div id="orgcol" class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div id="orgheader" class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">部门树</span>
		</div>
		<div id="org_tree" style="margin-top: 10px;height: 100%;" class="tree"></div>
	</div>
</div>
</body>
</html>