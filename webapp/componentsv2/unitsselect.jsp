<%--
	title:单位（组织机构）选择
	author:lining
--%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
.splitdiv{width: 100%; height: 100%;float: left;}
</style>
<script>

var selectedunitsids = <%=URLDecoder.decode(request.getParameter("units"), "utf-8")%>;

var zTree = null;

var currtreedata = null;
var selectedunitdata = [];

$(function(){
	$('body').layout({fxName:'none',west:{size:'30%'},center:{size:'15%'}, east:{size:'55%'}});
	
	zTree = $("#org_tree").unitTree({itemDoubleClick:zTreeOndblClick});
	
	$(".datagrid_container").datagrid({
		showSeqNum:true, selectionMode:"multipleRows", pagebuttonnum:3,
		columns:{name:"${i18n['antelope.orgnizationname']}"},
		buttons:{
			'del':function() {
				var thisObj = this;
				selectedunitdata = $.grep(selectedunitdata, function (elem){
					return elem.sid != thisObj.sid;
				});
				var items = $(".datagrid_container").datagrid("option", "selectedItems");
				for (var i = 0; i < items.length; i++) {
					if (items[i] == thisObj) {
						items.splice(i, 1);
						break;
					}
				}
				
				$(".datagrid_container").datagrid("option","dataProvider", selectedunitdata);
			}
		}
	});
	resetselectunitdatas();
});

function resetselectunitdatas() {
	$.post(ctxPath + "/common/UserRoleOrgController/getUnitInfosByUnitsids.vot", "unitsids=" + JSON.stringify(selectedunitsids), function (data){
		selectedunitdata = data;
		$(".datagrid_container").datagrid("option","dataProvider", selectedunitdata);
	}, "json");
}

function getSelectedUnits() {
	return selectedunitdata;
}

function zTreeOndblClick(data) {
	if (zTree.getSelectedNode() == null || zTree.getSelectedNode().id=='root') {
		return "";
	}
	
	var newdata = [zTree.getSelectedNode()];
	mergedata(newdata);
}

function selectdirectchildren() {
	var nodedata = zTree.getSelectedNode();
	if (nodedata == null) {
		alert("请选择一个上级节点！");
		return "";
	}
	$.getJSON(ctxPath + "/common/UserRoleOrgController/getDirectUnitChildren.vot?sid="+nodedata.sid, function(data){
		var newdata = [];
		if (data) {
			for (var i = 0; i < data.length; i++) {
				newdata.push(data[i]);
			}
		}
		mergedata(newdata);
	});
	
}

function selectallchildren() {
	var nodedata = zTree.getSelectedNode();
	if (nodedata == null) {
		alert("请选择一个上级节点！");
		return "";
	}
	
	var newdata = [];
	getchildunitdatas(nodedata, newdata);
	mergedata(newdata);
}

function mergedata(newdata) {
	var unitsids = $.extractSids(selectedunitdata);
	for (var i = 0; i < newdata.length; i++) {
		if ($.inArray(newdata[i].sid, unitsids) == -1) {
			selectedunitdata.push(newdata[i]);
		}
	}
	
	for (var i = 0; i < selectedunitdata.length; ++i) {
		selectedunitdata[i].parentNode = null;
	}
	
	$(".datagrid_container").datagrid("option","dataProvider", selectedunitdata);
}

function getchildunitdatas(parentdata, dataara) {
	if (parentdata.nodes) {
		for (var i = 0; i < parentdata.nodes.length; i++) {
			dataara.push(parentdata.nodes[i]);
			getchildunitdatas(parentdata.nodes[i], dataara);
		}
	}
}

function selectsingle() {
	var nodedata = zTree.getSelectedNode();
	if (nodedata == null || nodedata.id=='root') {
		alert("请选择一个机构！");
		return "";
	}
	var newdata = [nodedata];
	mergedata(newdata);
}

function clearall() {
	selectedunitdata.length = 0;
	$(".datagrid_container").datagrid("option","dataProvider", selectedunitdata);
	$(".datagrid_container").datagrid("option","selectedItems", []);
}

function deleteselected() {
	var items = $(".datagrid_container").datagrid("option", "selectedItems");
	selectedunitdata = $.grep(selectedunitdata, function (elem){
		
		var haselem = false;
		
		for (var i = 0; i < items.length; ++i) {
			if (elem.sid == items[i].sid) {
				return false;
			}
		}
		
		return true;
	});
	items.length = 0;
	$(".datagrid_container").datagrid("option","dataProvider", selectedunitdata);
}

</script>
</head>
<body>
<div class="ui-layout-west">
	<div id="treecol" class="ui-widget ui-widget-content" style="height: 100%;margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">组织机构</span>
		</div>
		<div id="org_tree" class="tree"></div>
	</div>
</div>
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">操作</span>
		</div>
		<div style="text-align: center;line-height: 30px;">
			<button onclick="selectdirectchildren()" class="button" style="margin-top: 70px;">直接下级></button><br/><br/>
			<!-- <button onclick="selectallchildren()" class="button">所有下级></button><br/><br/> -->
			<button onclick="selectsingle()" class="button" style="width: 89px;">选择></button><br/><br/>
			<button onclick="deleteselected()" class="button" style="width: 89px;">&lt;删除</button><br/><br/>
			<button onclick="clearall()" class="button" style="width: 89px;">&lt;清空</button>
		</div>
	</div>
</div>
<div class="ui-layout-east">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">选中的机构</span>
		</div>
		<div class="datagrid_container" style="margin-top: 10px;"></div>
	</div>
</div>
</body>
</html>

