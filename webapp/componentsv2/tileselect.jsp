<%--
	title:通用图片块选择组件
	author:lining
--%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
.splitdiv{width: 100%; height: 100%;float: left;}
.selectedtile{border-color: #e4e4e4 !important; background-color: #eeeeee;}
</style>
<script>
var comp = '<%=request.getParameter("component")%>';
var selectedsid = null;

$(function(){
	$(".tilecontainer").tilegrid({
		dataProvider: ctx + "/common/componentsv2/TileSelectController/getListSelectItemsToSelect.vot?component=" + comp,
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

function getSelectedItems() {
	return $(".tilecontainer").tilegrid("option", "selectedItems");
}

</script>
</head>
<body>
<div class="tilecontainer">
</div>
</body>
</html>

