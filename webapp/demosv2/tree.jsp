<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	$("#leftgrid").tree({
		checkable: true,
		expandTreeNodeByClick: true,
		asyncUrl: ctx + "/common/components/TreeSelectController/getTreeSelectItemsToSelect.vot?component=treeselectdemo",
		contextMenu : {"右键菜单1": {
			click: function(event, treeId, treenode) {
				if (treenode)
					alert("菜单1" + treenode.sid);
			}
		}, "右键菜单2": {
			click: function(event, treeId, treenode) {
				if (treenode)
					alert("菜单1" + treenode.sid);
			}
		}}
	});
	
	$("#leftgrid").tree("locate", ['1355638052583','1355638052629']);
	
	
	$("#leftgrid2").tree({
		click: function(event, treeId, treeNode) {
			alert(treeId);
			$("[name=mytreeinput]").val(treeNode.sid);
			$("#leftgrid2").hide();
		},
		asyncUrl: ctx + "/common/components/TreeSelectController/getTreeSelectItemsToSelect.vot?component=treeselectdemo"
	});
});

function viewselectnodes() {
	alert($("#leftgrid").tree("getCheckedNodes").length);
}

function showTree() {
	$("#leftgrid2").show();
}

function validateCanSelect(node) {
	if (node.name == '北京') {
		alert("请不要选择北京");
		return false;
	}
}


</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="viewselectnodes()">查看选中节点数量</button>
<form id="form1" style="float: left;">
<ul id="leftgrid" class="datagrid_container tree" style="margin-top: 10px; overflow-y:auto;overflow-x:hidden;"></ul>
</form>
<form id="form2" style="float: left;">
	<input name="mytreeinput" class="ztree" sidfieldname="mytreeinputsid" showfullpath="true" validateFunction="validateCanSelect" dataProvider="/common/components/TreeSelectController/getTreeSelectItemsToSelect.vot?component=treeselectdemo" />
	<input name="mytreeinputsid"/>
	<ul id="leftgrid2" class="datagrid_container tree" style="width:255px; height:235px; margin-top: 10px; overflow-y:auto;overflow-x:hidden; border:1px solid black; display: none;"></ul>
</form>
</body>
</html>


