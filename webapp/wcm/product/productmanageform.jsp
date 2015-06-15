<%@page import="antelope.services.SessionService"%>
<%@page import="java.util.UUID"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%	SessionService service = (SessionService) session.getAttribute("service"); %>
<script>
$(function(){
	
	$(".mysinglegridform").closest("form").rebind("openedCreateForm", function(e, data){
		$(".myckeditor").ckeditor4();
		
		
		if ($(".fileup").data("uiFileupload")) {
			$(".fileup").fileupload("destroy");
		}
		
		 $(".fileup").fileupload({
			 	autosave : true,
				maxfilecount: 3,
				maxfilesize: 100,
				enablePermanent: false,
				extension: "*.jpg",
				filegroupsidinput:$("[name=productimgsid]")
		 });
	});
	
	$(".mysinglegridform").closest("form").rebind("openCreateForm", function(){
		
		if ($(".myckeditor").data('uiCkeditor4')) {
			$(".myckeditor").ckeditor4("destroy").val('');
		}
		
	});
	
	$(".mysinglegridform").closest("form").rebind("openUpdateForm", function(){
		if ($(".myckeditor").data('uiCkeditor4')) {
			$(".myckeditor").ckeditor4("destroy");
		}
	});
	
	$(".mysinglegridform").closest("form").rebind("openedUpdateForm", function(e, data){
		$(".myckeditor").ckeditor4();
		
		if ($(".fileup").data("uiFileupload")) {
			$(".fileup").fileupload("destroy");
		}
		
		 $(".fileup").fileupload({
			 	autosave : true,
			 	filegroupsid: data.productimgsid,
				maxfilecount:3,
				maxfilesize: 100,
				enablePermanent: false,
				extension: "*.jpg",
				filegroupsidinput:$("[name=productimgsid]")
		 });
	});
	
	$(".mysinglegridform").closest("form").rebind("preopenViewForm", function(){
		$(".myckeditor").ckeditor4("destroy");
	});
	
	$(".mysinglegridform").closest("form").rebind("openViewForm", function(e, data){
		$(".myckeditor").ckeditor4({readonly:true});
		 $(".fileup").fileupload("destroy").fileupload({
			 	autosave : true,
			 	filegroupsid: data.productimgsid,
				maxfilecount:3,
				maxfilesize: 100,
				enablePermanent: false,
				extension: "*.jpg",
				readonly: true,
				filegroupsidinput:$("[name=productimgsid]")
		 });
	});
	
});


</script>
<div class="mysinglegridform" style="width: 730px; height: 550px;">
	<div class="condi_container">
		<span class="cd_name">产品名称：</span>
		<span class="cd_val">
			<input name="name" maxlength2="200" required2="true"/>
		</span>
	</div>
	
	<div class="condi_container">
		<span class="cd_name">产品价格：</span>
		<span class="cd_val">
			<input name="productprice" maxlength2="200" required2="true"/>
		</span>
	</div>
	
	<div class="condi_container">
		<span class="cd_name">图片：</span>
		<span class="cd_val">
			<input name="productimgsid" type="hidden"/>
			<span class="fileup" style="display: inline-block; width: 100px;"></span>
		</span>
	</div>
	
	<div class="condi_container">
		<span class="cd_name">内容：</span>
		<span class="cd_val">
			<textarea name="content" style="width: 700px; height: 400px;" class="myckeditor" id="<%=UUID.randomUUID().toString().substring(0, 6) %>"></textarea>
		</span>
	</div>
</div>


