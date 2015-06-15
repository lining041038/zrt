<%@page import="antelope.wcm.assets.ImgAsset"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>
<% ImgAsset imgasset = SpringUtils.getBean(ImgAsset.class); %>
<style>
.selectedtile{border-color: #e4e4e4 !important; background-color: #eeeeee;}
</style>
<script>

var comp = 'tileselectasset';
var selectedsid = null;

$(function(){
	$(".imgtilecontainer").tilegrid({
		dataProvider: ctx + "/common/components/TileSelectController/getListSelectItemsToSelect.vot?component=" + comp,
		selectionMode:"singleRow",
		tileRenderer: function() {
			return '<div onclick="imgclick(\'' + this.sid +  '\', this)" style="width:180; height: 155px; float:left; padding-top: 30px; padding-left:16px; border: 1px solid; border-color:white;">\
				<img src="' + (this.imgpath) + '" width="168" height="102"/>\
				<div style="font-size:14px; padding-top:10px;">' + this.name + '</div>\
				</div>';
		}
	});
});

function imgclick(sid, thisObj) {
	selectedsid = sid;
	$("*").removeClass("selectedtile");
	$(thisObj).addClass("selectedtile");
}

function getSelected<%=imgasset.getAssetType()%>() {
	if (!$(".imgtilecontainer").tilegrid("option", "selectedItems").length) {
		alert("请选择一张图片!");
		return false;
	}
	return $(".imgtilecontainer").tilegrid("option", "selectedItems")[0].sid;
}

function getSelected<%=imgasset.getAssetType()%>html(sid) {
	return '<img src="${ctx}/wcm/management/assetsmanage/AssetsManageController/getImageData.vot?sid='+sid+'"/>';
}

</script>
<div class="imgtilecontainer">
</div>

	
