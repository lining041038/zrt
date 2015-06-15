<%@ page language="java" pageEncoding="utf-8"%>

<script>
function customFuncBtn(gridobj) {
	alert(JSON.stringify(gridobj.datagrid("option", "selectedItems")));
}

$(function(){
	//alert("dfsdf");
});

</script>

<input name="name" required="true"/>