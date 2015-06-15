<%@page import="java.util.UUID"%>
<%@page import="java.util.List"%>
<%@page import="antelope.wcm.assets.BaseAsset"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>

<script>

$(function(){
	
	$(".mysinglegridform").closest("form").rebind("openedCreateForm", function(){
		$(".myckeditor").ckeditor4();
	});
	
	$(".mysinglegridform").closest("form").rebind("openCreateForm", function(){
		$(".myckeditor").ckeditor4("destroy").val('');
	});
	
	$(".mysinglegridform").closest("form").rebind("openUpdateForm", function(){
		$(".myckeditor").ckeditor4("destroy");
	});
	
	$(".mysinglegridform").closest("form").rebind("openedUpdateForm", function(){
		$(".myckeditor").ckeditor4();
	});
	
	$(".mysinglegridform").closest("form").rebind("preopenViewForm", function(){
		$(".myckeditor").ckeditor4("destroy");
	});
	
	$(".mysinglegridform").closest("form").rebind("openViewForm", function(){
		$(".myckeditor").ckeditor4({readonly:true});
	});
});


</script>
<div class="mysinglegridform" style="width: 730px; height: 550px;">
	<div class="condi_container">
		<span class="cd_name">昵称：</span>
		<span class="cd_val">
			<input name="name" required2="true" maxlength2="50" state="disabled:false;disabled.view:true;"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">内容：</span>
		<span class="cd_val">
			<textarea name="content" style="width: 700px; height: 400px;" class="myckeditor" id="<%=UUID.randomUUID().toString().substring(0, 6) %>"></textarea>
		</span>
	</div>
</div>






