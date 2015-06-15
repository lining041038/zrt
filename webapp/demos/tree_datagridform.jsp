<%@ page language="java" pageEncoding="utf-8"%>
<script>
$(function(){
	$(".mysinglegridform").parent().unbind("resetFormComplete").bind("resetFormComplete", function(){
	//	alert($(".mysinglegridform [name=name]").val($(".mysinglegridform [name=name]").val() + "（js自定义追加）"));
	});
});

function customFuncBtn(gridobj) {
	alert(JSON.stringify(gridobj.datagrid("option", "selectedItem")));
}

function myVisibleFunction() {
	return Math.random() > 0.5;
}

</script>
<div class="mysinglegridform" style="width: 430px; height: 120px;">
	<div>此处为自定义form,路径为/demos/demosupports/singlegridform.jsp</div>
	<div class="condi_container">
		<span class="cd_name">名称：</span>
		<span class="cd_val">
			<textarea name="name"></textarea>
		</span>
		<span class="cd_name">性别：</span>
		<span class="cd_val">
			<select name="gender" dataProvider="sys_usergender"></select>
		</span>
	</div>
</div>


