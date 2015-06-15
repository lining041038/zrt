<%@page import="antelope.wcm.assets.HyperLinkAsset"%>
<%@page import="antelope.wcm.assets.ImgAsset"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>
<% HyperLinkAsset imgasset = SpringUtils.getBean(HyperLinkAsset.class); %>
<style>
.selectedtile{border-color: #e4e4e4 !important; background-color: #eeeeee;}
</style>
<script>

$(function(){
	$(".hyperlinks").datagrid({
		dataProvider: ctx + "/assets/imgasset/HyperLinkAssetController/getHyperLinkAssets.vot",
		columns:{"assetdata":"页面名称"},
		selectionMode:"singleRow"
	});
});

function getSelected<%=imgasset.getAssetType()%>() {
	if (!$(".hyperlinks").datagrid("option", "selectedItem")) {
		alert("请选择一个需要链接的页面!");
		return false;
	}
	return $(".hyperlinks").datagrid("option", "selectedItem").sid;
}

function getSelected<%=imgasset.getAssetType()%>html(sid) {
	var pageName = "";
	$.ajaxSettings.async = false;
	$.post(ctx + "/assets/imgasset/HyperLinkAssetController/getPageName.vot?pagesid=" + sid, function(data) {
		pageName = data;
	});
	$.ajaxSettings.async = true;
	return "<span style='cursor:pointer;'>超链接到(" + pageName + ")页面<span>";
}

</script>
<div class="hyperlinks">
</div>

	
