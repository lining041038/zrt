<%--
	title:通用列表选择组件
	author:lining
--%>
<%@page import="antelope.interfaces.components.supportclasses.SelectionFilter"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.interfaces.components.supportclasses.TreeSelectionOptions"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<%
TreeSelectionOptions opts = (TreeSelectionOptions) request.getAttribute("opts");
Set<Entry<String, SelectionFilter>> treefilterentries = opts.treefilters.entrySet();
int i = 0;
%>
<style>
.splitdiv{width: 100%; height: 100%;float: left;}
</style>
<script>


var comp = '<%=request.getParameter("component")%>';
var selectedItemSids = '<%=request.getParameter("selectedItemSids")%>';
var parentsid = '<%=request.getParameter("parentsid")%>';
var selectionModevar = '<%=request.getParameter("selectionMode")%>';
var formdata = "<%=request.getParameter("data")%>";
var treecheckable = <%=request.getParameter("checkable")%>;


var zTree = null;
$(function(){
	$('body').layout({fxName:'none',west:{size:'40%'},center:{size:'20%'}, east:{size:'40%'}});
	
	$("#treecol").resize(function(){
		$("#leftgrid").height($("#treecol").height()- 55);
	}).resize();
	
	$("#rightgridcol").resize(function(){
		$("#rightgrid").height($("#rightgridcol").height()- 55);
	}).resize();
	
	
	$.getJSON(ctx + "/common/components/TreeSelectController/getTreeSelectOptions.vot?component=" + comp, function(data){
		$(".titlespan").text(data.title);
		
		var ncolumns = {};
		for (var i = 0; i < data.columns.length; ++i) {
			ncolumns[data.columns[i].key] = data.columns[i].value; 
		}

		createLeftGridTree();
		
		$("#rightgrid").datagrid({
			columns: ncolumns,
			pagebuttonnum: 0,
			selectionMode: selectionModevar,
			rowClickEnabled:true
		});
		
		// 获取已选中的项目
		if (selectedItemSids || parentsid) {
			$.post(ctx + "/common/components/TreeSelectController/getHistSelectedItems.vot?component=" + comp, "selectedItemSids=" + selectedItemSids + "&parentsid=" + parentsid, function (data){
				$("#rightgrid").datagrid("option", "dataProvider", data);
			}, 'json');
		}
	
	});
	
	
	// 添加查询回车事件
	$("#queryval").keydown(function(e){
		if (e.keyCode == 13) {
			leftListQuery();
		}
	});
});

function createLeftGridTree() {
	if ($("#leftgrid").data("uiTree")) {
		$("#leftgrid").tree("destroy");
	}
	
	zTree = $("#leftgrid").tree({
		checkable:treecheckable,
		asyncUrl: ctx + "/common/components/TreeSelectController/getTreeSelectItemsToSelect.vot?component=" + comp + "&queryval=" + encodeURIComponent(encodeURIComponent($("#queryval").val())) + "&" + formdata + "&" + $.param(currtreefilters),
		dblclick: function(e, id, obj) {
			selectitems();
		}
	});
}

function selectitems() {
	if (treecheckable) {
		selditems = $("#leftgrid").tree("getCheckedNodes");
		for(var i = 0; i < selditems.length; ++i) {
			addOneSelectItem(selditems[i]);
		}
	} else {
		var selditems = $("#leftgrid").tree("getSelectedNode");
		if (!selditems)
			return;
		addOneSelectItem(selditems);
	}
}

function addOneSelectItem(selditems) {
	var newselditems = {};
	
	for (var key in selditems) {
		if (key.match(/^(check_False_Full)|(check_Focus)|(check_True_Full)|(checked)|(checkedOld)|(editNameStatus)|(isAjaxing)|(isFirstNode)|(isHover)|(isLastNode)|(isParent)|(level)|(nodes)|(open)|(parentNode)|(tId)$/))
			continue;
		
		newselditems[key] = selditems[key];
	}
	selditems = [newselditems];
	
	var currprovider = $("#rightgrid").datagrid("option", "dataProvider");
	
	if (selectionModevar == 'singleRow' && selditems) {
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
}

function deleteselected() {
	var newprivder = [];
	var currprovider = $("#rightgrid").datagrid("option", "dataProvider");
	var selditems = $("#rightgrid").datagrid("option", "selectedItems");
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
	return $("#rightgrid").datagrid("option", "dataProvider");
}

function leftListQuery() {
	var queryval = encodeURIComponent(encodeURIComponent($("#queryval").val()));
	if (queryval) {
		$("#leftgrid").datagrid("option", "dataProvider", ctx + "/common/components/ListSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&queryval=" + encodeURIComponent(encodeURIComponent($("#queryval").val())) + "&" + formdata);
	} else {
		$("#leftgrid").datagrid("option", "dataProvider", ctx + "/common/components/ListSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&" + formdata);
	}
}


var currtreefilters = {};

<%
i = 0;
for (Entry<String, SelectionFilter> entry : treefilterentries) {
	SelectionFilter treefilter = entry.getValue();
%>

currtreefilters['<%=entry.getKey()%>'] = '<%=treefilter.getDefaultParamValue()%>';

function treefilterfunc<%=i%>() {
	var ifram = $.dialogTopIframe({
		url: ctx + "/components/supportjsps/<%=treefilter.getSupportjspname()%>?component=" + comp + "&treefilterkey=<%=entry.getKey()%>&formdata=" + encodeURIComponent(encodeURIComponent(formdata)),
		width: <%=treefilter.getWidth()%>,
		height: <%=treefilter.getHeight()%>,
		title: "<%=treefilter.getTitle()%>",
		data: formdata,
		buttons: {
			'确定': function() {
				currtreefilters['<%=entry.getKey()%>'] = ifram.find("iframe")[0].contentWindow.getFilteredData();
				ifram.dialog("destroy");
				createLeftGridTree();
			}, '取消': function() {
				ifram.dialog("destroy");
			}
		}
	});
}

<%}%>


</script>
</head>
<body>
<div class="ui-layout-west">
	<div id="treecol" class="ui-widget ui-widget-content" style="height: 100%;margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span id="" style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold; float: left;">${i18n['antelope.listselect.toselect'] }<span class="titlespan"></span></span>
			<span style="float: right;">
			<%
			i = 0;
			for (Entry<String, SelectionFilter> entry : treefilterentries) {
				SelectionFilter treefilter = entry.getValue();
			%>
				<button class="smallbutton" onclick="treefilterfunc<%=i%>()"><%=treefilter.getTitle() %></button>
			<%} %>
			</span>
			<!-- 
			<span style="float: right;">
				<input id="queryval" value='<%=request.getParameter("queryval") %>' default="<%=request.getParameter("queryval") %>" style="margin-right: 5px; width: 110px;"/>
				<button onclick="leftListQuery()" class="smallbutton" style="margin-right: 10px;">${i18n['antelope.query'] }</button>
			</span>
			 -->
		</div>
		<ul id="leftgrid" class="datagrid_container tree" style="margin-top: 10px; overflow-y:auto;overflow-x:hidden;"></ul>
	</div>
</div>
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">${i18n['antelope.operation'] }</span>
		</div>
		<div style="text-align: center;line-height: 30px; padding-top: 70px;">
			<button onclick="selectitems()" class="button" style="width: 89px;">${i18n['antelope.choose'] }&nbsp;></button><br/><br/>
			<button onclick="deleteselected()" class="button" style="width: 89px;">&lt;&nbsp;${i18n['antelope.delete'] }</button><br/><br/>
			<button onclick="clearall()" class="button" style="width: 89px;">&lt;&nbsp;${i18n['antelope.clear']}</button>
		</div>
	</div>
</div>
<div class="ui-layout-east">
	<div id="rightgridcol" class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">${i18n['antelope.listselect.selected'] }<span class="titlespan"></span></span>
		</div>
		<div id="rightgrid" class="datagrid_container" style="margin-top: 10px; overflow-y:auto;overflow-x:hidden;"></div>
	</div>
</div>
</body>
</html>

