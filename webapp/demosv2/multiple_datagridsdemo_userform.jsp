<%@ page language="java" pageEncoding="utf-8"%>

<script>
function customFuncBtn(gridobj) {
	alert(JSON.stringify(gridobj.datagrid("option", "selectedItems")));
}

</script>

<input name="name" required="true"/>