<%@ page language="java" pageEncoding="utf-8"%>
<script>
function testTreeEditable(e, id, treenode) {
	if (treenode.sid == '1355638052597')
		return false;
}

function customFuncBtn(gridobj) {
	alert(JSON.stringify(gridobj.datagrid("option", "selectedItems")));
}

</script>
<div class="mysinglegridform" style="width: 430px; height: 120px;">
	<div class="condi_container">
		<span class="cd_name">名称：</span>
		<span class="cd_val">
			<input name="name" state="disabled:true;disabled.create:false;disabled.update:false">
		</span>
	</div>
</div>