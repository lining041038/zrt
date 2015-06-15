<%@ page language="java" pageEncoding="utf-8"%>
<style>
.datagrid td{word-break:loose;  word-wrap: break-word;}
</style>
<script>
$(function(){
	$("#mysingleform").parent().unbind("resetFormComplete").bind("resetFormComplete", function(){
	});
	
	$("#mysingleform").parent().rebind("openViewForm", function() {
	});
	
	$("#mysingleform").parent().rebind("openedCreateForm", function() {
	});
	
	$("#mysingleform").parent().rebind("openedUpdateForm", function() {
	});
});

</script>
<!-- 权限设置dialog -->
<div id="mysingleform" style="width: 433px; height: 255px;">
</div>
