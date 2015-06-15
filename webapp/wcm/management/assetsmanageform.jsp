<%@page import="java.util.List"%>
<%@page import="antelope.wcm.assets.BaseAsset"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>

<%
	TransactionStatus status = SpringUtils.beginTransaction();
	List<BaseAsset> assettypes = SpringUtils.getBeans(BaseAsset.class);	
	for (int i = 0; i < assettypes.size();) {
		if (!assettypes.get(i).needToAdd()) {
			assettypes.remove(i);
		} else {
			++i;
		}
	}
	SpringUtils.commitTransaction(status);
%>

<script>

var operatetype = "";

$(function(){
	$(".mysinglegridform").parent().rebind("openedCreateForm", function() {
		appendfileupload();
		operatetype = "create";
	});
	
	$(".mysinglegridform").parent().rebind("openedUpdateForm", function() {
		appendfileupload();
		previewOldImg();
		operatetype = "update";
	});
	
	$(".mysinglegridform").parent().rebind("openViewForm", function() {
		previewOldImg();
		operatetype = "view";
	});
});

function previewOldImg() {
	$("#previewimg").attr("src", ctx + "/wcm/management/assetsmanage/AssetsManageController/getImageData.vot?sid=" + $(".mysinglegridform").closest("form").data("formdata").sid)
	.show();
}

function setPreviewImg() {
	if ($(".assetfile").fileupload("getFileInfos").length) {
		$("#previewimg").attr("src", ctx + "/upload/UploadController/getSingleImageData.vot?imagesid=" + $(".assetfile").fileupload("getFileInfos")[0].sid)
		.show();
	} else {
		setTimeout(setPreviewImg, 500);
	}
}

function appendfileupload() {
	$(".assetfile").fileupload("destroy").fileupload({
		filegroupsidinput: $("[name=filegroupsid]"),
		autosave: true,
		enablePermanent: false,
		filegroupsid: $.getNewsid(),
		maxfilecount: 1,
		extension: "*.png",
		autosave: true,
		uploadComplete: function() {
			setPreviewImg();
		}
	}).initWidgets();
}

<%for (int i = 0; i < assettypes.size(); ++i) { %>
	function addAssetType<%=assettypes.get(i).getAssetType()%>() {
		$(".mysinglegridform [name=assettype]").val('<%=assettypes.get(i).getAssetType()%>');
		var formsid = $(".mysinglegridform").closest("form").find("[name=sid]").val();
		var ifrm = $.dialogTopIframe({
			url: ctx + "/wcm/assets/<%=assettypes.get(i).getAssetRelativePath()%>_add.jsp?assetsid=" + formsid + "&operatetype=" + operatetype,
			width:<%=assettypes.get(i).getAddPageWidth()%> || top.$(top.window).width() - 20, 
			height: <%=assettypes.get(i).getAddPageHeight()%>  || top.$(top.window).height() - 20, 
			buttons: {
				'确定': function() {
					$("[name=assetdatasymbol]").val(ifrm.find("iframe")[0].contentWindow.getAssetDataSymbol());
					ifrm.dialog("destroy");
					$.hideBusyState();
				}, '取消': function() {
					ifrm.dialog("destroy");
					$.hideBusyState();
				}
			}
		});
	}
<%}%>

</script>
<div class="mysinglegridform" style="width: 900px; height: 600px;">
	<div class="condi_container">
		<span class="cd_name">名称：</span>
		<span class="cd_val">
			<input name="name" required="true" maxlength2="50" state="disabled:false;disabled.view:true;"/>
		</span>
	</div>
	<div class="condi_container" includeIn="create">
		<span class="cd_name" style="float: left; ">资源(点击请类型添加)：</span>
		<%for (int i = 0; i < assettypes.size(); ++i) { %>
		<button class="button" onclick="addAssetType<%=assettypes.get(i).getAssetType()%>()"><%=assettypes.get(i).getAssetTypeName()%></button>
		<%} %>
		<input type="hidden" name="assetdatasymbol"/>
		<input type="hidden" name="assettype"/>
	</div>
</div>






