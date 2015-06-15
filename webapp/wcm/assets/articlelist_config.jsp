<%@page import="antelope.wcm.assets.ArticleListAsset"%>
<%@page import="antelope.wcm.assets.ArticlePortalAsset"%>
<%@page import="antelope.wcm.assets.ChannelProgramAsset"%>
<%@page import="antelope.wcm.assets.HyperLinkAsset"%>
<%@page import="antelope.wcm.assets.ImgAsset"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>
<% ArticleListAsset asset = SpringUtils.getBean(ArticleListAsset.class); %>
<style>
.selectedtile{border-color: #e4e4e4 !important; background-color: #eeeeee;}
</style>
<script>

var selectedparentchannelsid3 = '';
$(function(){
	
	$("#channelforarticletree2").tree("destroy").tree({
		asyncUrl: ctx + "/assets/imgasset/ChannelProgramController/getChannelProgramsByParentsid.vot",
		click: function(event,treeId,treeNode) {
			selectedparentchannelsid3 = treeNode.sid;
		}
	});
	
});

function getSelected<%=asset.getAssetType()%>() {
	if (!selectedparentchannelsid3) {
		alert("请选择一个频道或栏目!");
		return false;
	}
	return selectedparentchannelsid3;
}

function getSelected<%=asset.getAssetType()%>html(sid) {
	return "";
}

function getSelected<%=asset.getAssetType()%>jsfuncname(sid) {
	return "wcm_create_articlelist";
}

function getSelected<%=asset.getAssetType()%>jsfuncdata(sid) {
	var jsfuncdata = null;
	$.ajaxSettings.async = false;
	$.post(ctx + "/assets/imgasset/ChannelProgramController/getChannelPortalArticleDatas.vot?sid=" + sid, function(data) {
		jsfuncdata = data;
	}, "json");
	$.ajaxSettings.async = true;
	return jsfuncdata;
}

</script>
<ul id="channelforarticletree2" class="channelforarticletree2 tree">
</ul>

	
