<%@ page language="java" pageEncoding="utf-8"%>
<script>

function publishSite(datagrid) {
	$.showBusyState();
	$.post(ctx + "/wcm/management/sitepublishmanage/SitePublishManageController/doPublishTheSite.vot?sitesid=" + this.sid, function() {
		alert("发布成功！");
		datagrid.datagrid("refresh");
		$.hideBusyState();
	});
}

function viewTheSite() {
	var thisObj = this;
	$.get(ctx + "/wcm/management/sitepublishmanage/SitePublishManageController/getHomepagesid.vot?sitesid=" + this.sid, function(pagesid) {
		if (pagesid) {
			open(ctx + "/wcm/published/" + thisObj.sid + "/" + pagesid + ".html");
		} else {
			alert("请在站点管理功能中为此站点设置一个首页！");
		}
	});
}

</script>