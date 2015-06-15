<%@page import="antelope.wcm.assets.ChannelProgramAsset"%>
<%@page import="antelope.wcm.assets.HyperLinkAsset"%>
<%@page import="antelope.wcm.assets.ImgAsset"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>
<% ChannelProgramAsset asset = SpringUtils.getBean(ChannelProgramAsset.class); %>
<style>
.selectedtile{border-color: #e4e4e4 !important; background-color: #eeeeee;}
</style>
<script>

var selectedparentchannelsid = '';
$(function(){
	
	$("#channeltree").tree("destroy").tree({
		asyncUrl: ctx + "/assets/imgasset/ChannelProgramController/getChannelProgramsByParentsid.vot",
		click: function(event,treeId,treeNode) {
			selectedparentchannelsid = treeNode.sid;
		}
	});
	
});

function getSelected<%=asset.getAssetType()%>() {
	if (isgetdatafromouter) {
		return 'getdatafromouter';
	}
	
	if (!selectedparentchannelsid) {
		alert("请选择一个频道或栏目组的父级频道或栏目!");
		return false;
	}
	return selectedparentchannelsid;
}

function getSelected<%=asset.getAssetType()%>html(sid) {
	return "";
}

function getSelected<%=asset.getAssetType()%>jsfuncname(sid) {
	return "wcm_create_channelorprogram";
}

//标记是否是由外部链接点击进入
var isgetdatafromouter = false;

function setchanneltype(isgetdatafromo) {
	isgetdatafromouter = isgetdatafromo;
}

function getSelected<%=asset.getAssetType()%>jsfuncdata(sid) {
	if (sid == 'getdatafromouter') {
		isgetdatafromouter = true;
	}
	
	var jsfuncdata = null;
	if (isgetdatafromouter) {
		return {
			name: '(父级频道)',
			channels:
			[{name:'(子级频道1)'}, {name:"(子级频道2)"}]
		};
	}
	
	$.ajaxSettings.async = false;
	$.post(ctx + "/assets/imgasset/ChannelProgramController/getChannelProgramsInfosByParentsid.vot?sid=" + sid, function(data) {
		jsfuncdata = data;
	}, "json");
	$.ajaxSettings.async = true;
	return jsfuncdata;
}


</script>

<div class="tabs">
	<ul id="tabsul">
		<li><a href="#gridkeyaa" onclick="setchanneltype(false)">固定频道组</a></li>
		<li><a href="#gridkeybb" onclick="setchanneltype(true)">由外部链接点击进入</a></li>
	</ul>
	<div id="gridkeyaa">
		<ul id="channeltree" class="channeltree tree">
		</ul>
	</div>
	<div id="gridkeybb">
	</div>
</div>


	
