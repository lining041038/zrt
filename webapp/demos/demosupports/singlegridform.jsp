<%@page import="java.util.UUID"%>
<%@ page language="java" pageEncoding="utf-8"%>
<script>
$(function(){
	
	//alert("dsfdfs");
	
	$(".mysinglegridform").parent().unbind("resetFormComplete").bind("resetFormComplete", function(){
		//alert($(".mysinglegridform [name=name]").val($(".mysinglegridform [name=name]").val() + "（js自定义追加）"));
	});
	
	$(".mysinglegridform").closest("form").rebind("openCreateForm", function(){
		$("#eventdiv").append("<span>openCreateForm triggered</span><br/>");
	});
	
	$(".mysinglegridform").closest("form").rebind("openedCreateForm", function(){
		$("#eventdiv").append("<span>openedCreateForm triggered</span><br/>");
	});
	
	$(".mysinglegridform").closest("form").rebind("openUpdateForm", function(e, data){
		$("#eventdiv").append("<span>openUpdateForm triggered data:" + JSON.stringify(data) + "</span><br/>");
	});
	
	$(".mysinglegridform").closest("form").rebind("openedUpdateForm", function(e, data){
		$("#eventdiv").append("<span>openedUpdateForm triggered data:" + JSON.stringify(data) + "</span><br/>");
	});
	
	$(".mysinglegridform").closest("form").rebind("openViewForm", function(e, data){
		$("#eventdiv").append("<span>openViewForm triggered data:" + JSON.stringify(data) + "</span><br/>");
	});
});

function viewValue() {
	alert($("[name=clobtest]").length );
	alert($("[name=clobtest]").val());
}

function customButtonClickHandler() {
	alert("范德萨速读法");
}

function batchadd1(gridobj) {
	$.post(ctx + opts.urlprefix + "/batchAdd1.vot?sids=" + $.extractSids(gridobj.datagrid("option", "selectedItems")), function(){
		gridobj.datagrid("refresh");
	});
}

function testIframeNest() {
	
	$.dialogTopIframe({
		url:"http://www.sina.com",
		
		width:50, height:50
	});
}

</script>
<div class="mysinglegridform" style="width: 730px; height: 1220px;">
<div id="eventdiv">
</div>
<button onclick="viewValue()">查看值</button>
<button onclick="testIframeNest()">测试嵌套</button>
	<div>此处为自定义form,路径为/demos/demosupports/singlegridform.jsp</div>
	<div class="condi_container">
		<span class="cd_name">名称：</span>
		<span class="cd_val">
			<input name="name" tabindex="2" required2="true" state="disabled:false;disabled.view:true;disabled.update:false"/>
		</span>
		<span class="cd_name">性别：</span>
		<span class="cd_val">
			<select name="gender" dataProvider="sys_usergender" tabindex="1"></select>
		</span>
		<span class="cd_name">clob测试：</span>
		<span class="cd_val">
			<textarea name="clobtest2"></textarea>
		</span>
		
		<span class="cd_name">ckeditor测试：</span>
		<span class="cd_val">
			<textarea class="ckeditor4" name="clobtest" id="<%=UUID.randomUUID().toString().substring(0, 6)%>" style="width:500px;" state="disabled:true;disabled.create:false;disabled.update:false"></textarea>
		</span>
	</div>
</div>


