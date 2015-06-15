<%@page import="antelope.consts.GlobalConsts"%>
<%@ page language="java" pageEncoding="utf-8"%>
<script>

function previewSite () {
	if (!zTree.tree('getSelectedNode') || !zTree.tree('getSelectedNode').templatesid) {
		alert("请选择一个站点");
		return;
	}
	
	$.get(ctx + "/wcm/management/sitemanage/SiteManageController/getTemplatePath.vot?templatesid=" + zTree.tree('getSelectedNode').templatesid, function(data){
		 open(ctx + data + "/index.jsp?sitesid=" + zTree.tree('getSelectedNode').sid);
	});
}


function createSiteTreeNode() {
	
	var newSiteSid = $.getNewsid();
	
	var ifram = $.dialogTopIframe({
		url: ctx + "/wcm/management/sitesetup.jsp",
		title: "新建站点", width: 1000, height:600,
		buttons: {
			'保存': function(){
				ifram.find("iframe")[0].contentWindow.saveAllSettings(newSiteSid, function(){
					zTree.tree("reAsyncChildNodes", null, "refresh");
				});
			}, '确定': function(){
				ifram.find("iframe")[0].contentWindow.saveAllSettings(newSiteSid, function(){
					zTree.tree("reAsyncChildNodes", null, "refresh");
					ifram.dialog("destroy");
				});
			}, '取消': function(){
				ifram.dialog("destroy");
			}
		}
	});
	
	/*$(".sbmform").submitDialog({
		submitLabel: "确定",
		url:ctx + opts.urlprefix + "/addOrUpdateOne.vot?a=1", 
		title:"添加站点",
		width: 1000,
		height: 600,
		dialogTopIframeURL: ctx + "/wcm/management/sitesetup.jsp",
		callback: function() {
			
		},
		isinline:false,
		dialogTopLoadedComplete: function() {
			
			var filteredform = this.filter("#singlegridform");
			filteredform.trigger('openUpdateForm', thisObj);
			filteredform.setCurrentState("create");
			filteredform.resetForm(thisObj);
			filteredform.trigger('openedUpdateForm', thisObj);
		}
	});\*/
}

function updateSiteTreeNode() {
	
	if (!checkSelectParentNode())
		return;

	var ifram = $.dialogTopIframe({
		url: ctx + "/wcm/management/sitesetup.jsp",
		title: "修改站点", width: 1000, height:600,
		loadComplete: function() {
			$.get(ctx + "/wcm/management/sitemanage/SiteManageController/getAllSiteSettings.vot?sitesid=" +  zTree.tree('getSelectedNode').sid, function(data){
				ifram.find("iframe")[0].contentWindow.resetAllSettings(data);
			}, "json");
		},
		buttons: {
			'保存': function(){
				ifram.find("iframe")[0].contentWindow.saveAllSettings(null, function(){
					zTree.tree("reAsyncChildNodes", null, "refresh");
				});
			}, '确定': function(){
				ifram.find("iframe")[0].contentWindow.saveAllSettings(null, function(){
					zTree.tree("reAsyncChildNodes", null, "refresh");
					ifram.dialog("destroy");
				});
			}, '取消': function(){
				ifram.dialog("destroy");
			}
		}
	});
	
}

function activatedSite() {
	if (!zTree.tree('getSelectedNode') || !zTree.tree('getSelectedNode').templatesid) {
		alert("请选择一个站点2");
		return;
	}
	
	$.commonSubmit({
		url: ctx + "/wcm/management/sitemanage/SiteManageController/activatedSite.vot?sitesid=" + zTree.tree('getSelectedNode').sid,			
		callback: function(data) {
			data = JSON.parse(data);
			var seldnode = zTree.tree("getSelectedNode");
			seldnode.activated = data.msg;
			if (seldnode.activated == '1') {
				seldnode.icon = ctx + "/antelope/themes/base/assets/runningicon.png";
			} else {
				seldnode.icon = ctx + "/antelope/themes/base/assets/stoppedicon.png";
			}
			zTree.tree("unwrap").updateNode(seldnode);
		}
	});
	
	$.post();
}

</script>






