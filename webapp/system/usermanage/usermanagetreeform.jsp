<%@ page language="java" pageEncoding="utf-8"%>
<script>

function testTreeEditable(e, id, treenode) {
	if (treenode.sid == '1355389788010')
		return false;
}

function checkDelVisible(obj)　{
	if (obj.sid != '1234')
		return true;
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


