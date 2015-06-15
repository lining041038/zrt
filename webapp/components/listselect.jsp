<%--
	title:通用列表选择组件
	author:lining
--%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
html{height: 100%;}
.splitdiv{width: 100%; height: 100%;float: left;}
</style>
<script>
var comp = '<%=request.getParameter("component")%>';
var selectedItemSids = '<%=request.getParameter("selectedItemSids")%>';
var parentsid = '<%=request.getParameter("parentsid")%>';
var selectionModevar = '<%=request.getParameter("selectionMode")%>';
var formdata = "<%=request.getParameter("formdata")%>";
$(function(){
	<%if ("multipleRows".equals(request.getParameter("selectionMode"))) { %>
	$('body').layout({fxName:'none',west:{size:'47%'},center:{size:'6%'}, east:{size:'47%'}});
	<%}%>
	
	$(".ui-layout-west").resize(function(){
		$("#leftgrid").height($(".ui-layout-west").height()- 50);
		$(".listselect-selecttoright").css("top", ($(".ui-layout-west").height()- 85) / 2);
	}).resize();
	
	$("#rightgridcol").resize(function(){
		$("#rightgrid").height($("#rightgridcol").height()- 50);
	}).resize();
	
	$.getJSON(ctx + "/common/components/ListSelectController/getListSelectOptions.vot?component=" + comp, function(data){
		$(".titlespan").text(data.title);
		
		var ncolumns = {};
		for (var i = 0; i < data.columns.length; ++i) {
			ncolumns[data.columns[i].key] = data.columns[i].value; 
		}
		$("#leftgrid").datagrid({
			dataProvider: ctx + "/common/components/ListSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&queryval=" + encodeURIComponent(encodeURIComponent($("#queryval").val())) + "&" + formdata,
			columns: ncolumns,
			pagebuttonnum: 0,
			<%if ("singleRow".equals(request.getParameter("selectionMode")) && TextUtils.stringSet(request.getParameter("selectedItemSids"))) { %>
			locateItemSid: selectedItemSids,
			selectionMode: selectionModevar,
			pageChange: function (currList) {
				var selectedobj = null;
				for (var i = 0; i < currList.length; ++i) {
					if (currList[i].sid == selectedItemSids) {
						selectedobj = currList[i]; 
					}
				}
				$("#leftgrid").datagrid("option", "selectedItem", selectedobj);
			},
			<%}%>
			usingTinyPageArea:true,
			rowClickEnabled:true,
			centerButton: true,
			operationHeaderWidth:"25px"
			<%if ("multipleRows".equals(request.getParameter("selectionMode"))) { %>
			,buttons: {
				add: function() {
					selectitemsInner([this]);
				}
			}
			<%}%>
		});
		
		$("#rightgrid").datagrid({
			columns: ncolumns,
			pagebuttonnum: 0,
			usingTinyPageArea:true,
			centerButton: true,
			rowClickEnabled:true,
			operationHeaderWidth:"25px",
			buttons: {
				remove: function() {
					deleteselectedinner([this]);
				}
			}
		});
		
		// 获取已选中的项目
		if (selectedItemSids || parentsid) {
			<%if ("multipleRows".equals(request.getParameter("selectionMode"))) { %>
			$.post(ctx + "/common/components/ListSelectController/getHistSelectedItems.vot?component=" + comp, "selectedItemSids=" + selectedItemSids + "&parentsid=" + parentsid, function (data){
				$("#rightgrid").datagrid("option", "dataProvider", data);
			}, 'json');
			<%}%>
		}
	});
	
	
	// 添加查询回车事件
	$("#queryval").keydown(function(e){
		if (e.keyCode == 13) {
			leftListQuery();
		}
	});
});

function selectitems() {
	var selditems = $("#leftgrid").datagrid("option", "selectedItems");
	selectitemsInner(selditems);
}

function fullselectCurrPage() {
	selectitemsInner($("#leftgrid").datagrid("getCurrList"));
}

function selectitemsInner(selditems) {
	var currprovider = $("#rightgrid").datagrid("option", "dataProvider");
	
	if (selectionModevar == 'singleRow' && selditems.length) {
		$("#rightgrid").datagrid("option", "dataProvider", selditems);
		return;
	}
	
	var selectedsids = {};
	for (var i = 0; i < currprovider.length; ++i) {
		selectedsids[currprovider[i]['sid']] = true;
	}
	for (var i = 0; i < selditems.length; ++i) {
		if (selectedsids[selditems[i]['sid']])
			continue;
		currprovider.push(selditems[i]);
	}
	$("#rightgrid").datagrid("option", "dataProvider", currprovider);
	$("#leftgrid").datagrid("option", "selectedItems", []);
}

function deleteselected() {
	var selditems = $("#rightgrid").datagrid("option", "selectedItems");
	deleteselectedinner(selditems);
}

function deleteselectedinner(selditems) {
	var newprivder = [];
	var currprovider = $("#rightgrid").datagrid("option", "dataProvider");
	var selecteditemsid = {};
	for (var i = 0; i < selditems.length; ++i) {
		selecteditemsid[selditems[i]['sid']] = true;
	}
	for (var i = 0; i < currprovider.length; ++i) {
		if (!selecteditemsid[currprovider[i]['sid']]) {
			newprivder.push(currprovider[i]);
		}
	}
	$("#rightgrid").datagrid("option", "dataProvider", newprivder);
	$("#rightgrid").datagrid("option", "selectedItems", []);
}

function clearall() {
	$("#rightgrid").datagrid("option", "dataProvider", []);
	$("#rightgrid").datagrid("option", "selectedItems", []);
}

function getSelectedItems() {
	
	<%if ("multipleRows".equals(request.getParameter("selectionMode"))) { %>
	return $("#rightgrid").datagrid("option", "dataProvider");
	<%} else {%>
	return [$("#leftgrid").datagrid("option", "selectedItem")];
	<%}%>
}

function leftListQuery() {
	var queryval = encodeURIComponent(encodeURIComponent($("#queryval").val()));
	if (queryval) {
		$("#leftgrid").datagrid("option", "dataProvider", ctx + "/common/components/ListSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&queryval=" + encodeURIComponent(encodeURIComponent($("#queryval").val())) + "&" + formdata);
	} else {
		$("#leftgrid").datagrid("option", "dataProvider", ctx + "/common/components/ListSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&" + formdata);
	}
}

</script>
</head>
<body>
<%if ("multipleRows".equals(request.getParameter("selectionMode"))) { %>
<div class="ui-layout-west">
<%} %>
	<div id="treecol" class="ui-widget ui-widget-content" style="margin: 2px; height:495px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span id="" style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold; float: left;">${i18n['antelope.listselect.toselect'] }<span class="titlespan"></span></span>
			<span style="float: right;">
				<input id="queryval" value='<%=request.getParameter("queryval") %>' default="<%=request.getParameter("queryval") %>" style="margin-right: 5px; width: 110px;"/>
				<button onclick="leftListQuery()" class="smallbutton" style="margin-right: 10px;">${i18n['antelope.query'] }</button>
			</span>
		</div>
		<div id="leftgrid" class="datagrid_container" style="margin-top: 10px; overflow-y:auto; overflow-x:hidden;"></div>
	</div>
<%if ("multipleRows".equals(request.getParameter("selectionMode"))) { %>
</div>
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px; text-align: center;">
			<button class="fullpagesel" onclick="fullselectCurrPage()"></button>
			<%--<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">
			${i18n['antelope.operation'] } 
			</span>--%>
		</div>
		<div style="text-align: center;line-height: 30px;">
			<div class="listselect-selecttoright">
			</div>
			<%--
			<button onclick="selectitems()" class="button" style="width: 89px;">${i18n['antelope.choose'] }&nbsp;></button><br/><br/>
			<button onclick="deleteselected()" class="button" style="width: 89px;">&lt;&nbsp;${i18n['antelope.delete'] }</button><br/><br/>
			<button onclick="clearall()" class="button" style="width: 89px;">&lt;&nbsp;${i18n['antelope.clear']}</button>
			 --%>
		</div>
	</div>
</div>
<div class="ui-layout-east">
	<div id="rightgridcol" class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;float: left;">${i18n['antelope.listselect.selected'] }<span class="titlespan"></span></span>
			<span style="float: right;">
				<button onclick="clearall()" class="smallbutton" style="margin-right: 10px;margin-top:3px;">${i18n['antelope.clear'] }</button>
			</span>
		</div>
		<div id="rightgrid" class="datagrid_container" style="margin-top: 10px; overflow-y:auto;overflow-x:hidden;"></div>
	</div>
</div>
<%} %>
</body>
</html>

