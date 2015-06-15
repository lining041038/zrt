<%--
	title:通用列表选择组件
	author:lining
--%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.interfaces.components.supportclasses.SelectionFilter"%>
<%@page import="antelope.interfaces.components.supportclasses.TreeListSelectionOptions"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%
	request.setAttribute("componentVersion", "v2");
%>
<jsp:include page="/include/header2.jsp"/>
<%
TreeListSelectionOptions opts = (TreeListSelectionOptions) request.getAttribute("opts");
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

var uiwidgetheaderheight = 35;

var zTree = null;
$(function(){
	// 初始化布局
	var center = $("body");
	var westsizes = ['22%','40%','20%'];
	for (var i = 0; i < 3; ++i) {
		var newlayout = center.layout({
			west:{size:westsizes[i]},
			west__paneSelector: '.col1' + i, center__paneSelector:'.col2' + i 
		});
		center = newlayout.panes.center;
	}
	
	$("#treecol").resize(function(){
		$("#leftgrid").height($("#treecol").height()- uiwidgetheaderheight);
	}).resize();
	
	$("#rightgridcol").resize(function(){
		$("#rightgrid").height($("#rightgridcol").height()- uiwidgetheaderheight);
	}).resize();
	
	
	$.getJSON(ctx + "/common/components/TreeListSelectController/getTreeSelectOptions.vot?component=" + comp, function(data){
		$("#treetitlespan").text(data.treetitle);
		$(".titlespan").text(data.title);
		
		var ncolumns = {};
		for (var i = 0; i < data.columns.length; ++i) {
			ncolumns[data.columns[i].key] = data.columns[i].value; 
		}
		
		createLeftGridTree();
		
		$("#leftdatagrid").datagrid({
			dataProvider:ctx + "/common/components/TreeListSelectController/getListSelectItemsToSelect.vot?component=" + comp,
			columns: ncolumns,
			pagebuttonnum: 0,
			usingTinyPageArea:true,
			selectionMode: selectionModevar,
			rowClickEnabled:true
		});
		
		$("#rightgrid").datagrid({
			columns: ncolumns,
			pagebuttonnum: 0,
			usingTinyPageArea:true,
			selectionMode: selectionModevar,
			rowClickEnabled:true
		});
		
		// 获取已选中的项目
		if (selectedItemSids || parentsid) {
			$.post(ctx + "/common/components/TreeListSelectController/getHistSelectedItems.vot?component=" + comp, "selectedItemSids=" + selectedItemSids + "&parentsid=" + parentsid, function (data){
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
	if ($("#leftgrid").data('uiTree')) {
		$("#leftgrid").tree("destroy");
	}
	
	zTree = $("#leftgrid").tree({
		asyncUrl: ctx + "/common/components/TreeListSelectController/getTreeSelectItemsToSelect.vot?component=" + comp + "&queryval=" + encodeURIComponent(encodeURIComponent($("#queryval").val())) + "&" + formdata + "&" + $.param(currtreefilters),
		click: function() {
			if (formdata) {
				$("#leftdatagrid").datagrid("option", "dataProvider", 
						ctx + "/common/components/TreeListSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&treenode_sid=" + 
								zTree.tree("getSelectedNode").sid + "&" + formdata);
			} else {
				$("#leftdatagrid").datagrid("option", "dataProvider", 
						ctx + "/common/components/TreeListSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&treenode_sid=" + 
								zTree.tree("getSelectedNode").sid);
			}
		},
		dblclick: function(e, id, obj) {
			selectitems();
		}
	});
	
}

function selectitems() {
	var selditems = $("#leftdatagrid").datagrid("option", "selectedItems");
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
	$("#leftdatagrid").datagrid("option", "selectedItems", []);
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
		url: ctx + "/componentsv2/supportjsps/<%=treefilter.getSupportjspname()%>?component=" + comp + "&treefilterkey=<%=entry.getKey()%>&formdata=" + encodeURIComponent(encodeURIComponent(formdata)),
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
<div class="col10">
	<div id="treecol" class="ui-widget ui-widget-content" style="height: 100%; margin-top: 2px;">
		<div class="ui-widget-header" style="height: 25px;">
			<span id="treetitlespan" style="margin-top: 5px; display: block; margin-left: 5px; font-size: 14px; font-weight: bold;"></span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
			<%
			i = 0;
			for (Entry<String, SelectionFilter> entry : treefilterentries) {
				SelectionFilter treefilter = entry.getValue();
			%>
				<button class="smallbutton" onclick="treefilterfunc<%=i%>()"><%=treefilter.getTitle() %></button>
			<%} %>
			</span>
		</div>
		<ul id="leftgrid" class="tree"></ul>
	</div>
</div>
<div class="col20">
	<div class="col11">
		<div class="ui-widget ui-widget-content" style="height: 100%; margin-top: 2px;">
			<div class="ui-widget-header" style="height: 25px;">
				<span style="margin-top: 5px; display: block; margin-left: 5px; font-size: 14px; font-weight: bold;">${i18n['antelope.listselect.toselect'] }<span class="titlespan"></span></span>
			</div>
			<div id="leftdatagrid"></div>
		</div>
	</div>
	<div class="col21">
		<div class="col12">
			<div class="ui-widget ui-widget-content" style="height: 100%; margin-top: 2px;">
				<div class="ui-widget-header" style="height: 25px;">
					<span style="margin-top: 5px; display: block; margin-left: 5px; font-size: 14px; font-weight: bold;">${i18n['antelope.operation'] }</span>
				</div>
				<div style="text-align: center;line-height: 30px; padding-top: 70px;">
					<button onclick="selectitems()" class="button" style="width: 65px;">${i18n['antelope.choose'] }&nbsp;></button><br/><br/>
					<button onclick="deleteselected()" class="button" style="width: 65px;">&lt;&nbsp;${i18n['antelope.delete'] }</button><br/><br/>
					<button onclick="clearall()" class="button" style="width: 65px;">&lt;&nbsp;${i18n['antelope.clear']}</button>
				</div>
			</div>
		</div>
		<div class="col22">
			<div class="ui-widget ui-widget-content" style="height: 100%; margin-top: 2px;">
				<div class="ui-widget-header" style="height: 25px;">
					<span style="margin-top: 5px; display: block; margin-left: 5px; font-size: 14px; font-weight: bold;">${i18n['antelope.listselect.selected'] }<span class="titlespan"></span></span>
				</div>
				<div id="rightgrid"></div>
			</div>
		</div>
	</div>
</div>
</body>
</html>

