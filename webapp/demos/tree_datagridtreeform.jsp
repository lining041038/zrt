<%@ page language="java" pageEncoding="utf-8"%>
<script>

function testTreeEditable(e, id, treenode) {
	if (treenode.sid == '1355389788010')
		return false;
}

$(function(){
	$(".mysinglegridform2").closest('form').rebind("openedTreeNodeCreateForm", function(){
		alert("创建树节点窗口打开！");
	});
	$(".mysinglegridform2").closest('form').rebind("openedTreeNodeUpdateForm", function(e, data){
		alert("修改树节点窗口打开！");
		alert(data.sid);
	});
});

function refreshIt() {
	alert("执行了刷新");
	
	zTree.tree("reAsyncChildNodes", null, "refresh");
}

function onTreeCtxMenu(e,id, node) {
	alert(node.sid);
} 


</script>
<div class="mysinglegridform2" style="width: 430px; height: 120px;">
	<div class="condi_container">
		<span class="cd_name">名称：</span>
		<span class="cd_val">
			<textarea name="name"></textarea>
		</span>
	</div>
</div>


