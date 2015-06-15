<%@ page language="java" pageEncoding="utf-8"%>

<script>
function customFuncBtn2(gridobj) {
	alert(JSON.stringify(gridobj.datagrid("option", "selectedItem")));
}
</script>