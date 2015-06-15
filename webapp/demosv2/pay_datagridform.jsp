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

function gotoPay() {
	$.dialogTopIframe({
		url: ctx + "/demos/onlinepay_demo.jsp?ordersid=" + this.sid
	});
}

</script>
<div class="mysinglegridform" style="width: 430px; height: 120px;">
	<div class="condi_container">
		<span class="cd_name">订单名称：</span>
		<span class="cd_val">
			<input name="name" required2="true" maxlength2="20"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">订单价格：</span>
		<span class="cd_val">
			<input name="orderprice" required2="true" maxlength2="10" price="true" min="0.01"/>元
		</span>
	</div>
</div>


