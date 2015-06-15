<%--
	title:通用多树单列表选择组件
	author:lining
	date:2013-07-05
--%>
<%@page import="antelope.interfaces.components.supportclasses.MultipleTreesSelectionOptions"%>
<%@page import="antelope.interfaces.components.supportclasses.Tree4MtlsOption"%>
<%@page import="antelope.interfaces.components.supportclasses.MultipleTreesListSelectionOptions"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.interfaces.components.supportclasses.SelectionFilter"%>
<%@page import="antelope.interfaces.components.supportclasses.TreeListSelectionOptions"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<%
MultipleTreesSelectionOptions opts = (MultipleTreesSelectionOptions) request.getAttribute("opts");
Set<Entry<String, Tree4MtlsOption>> entries = opts.treeOptionMap.entrySet();
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
function dialogTopIframeShowed(){
	// 初始化布局
	var center = $("body");
	
	// 计算嵌套布局宽度
	var bodywidth = $("body").width();
	var westsizes = [];
	for (var i = 0; i < <%=entries.size()%>; ++i) {
		westsizes.push((bodywidth - 100) / <%=entries.size() + 1%>  / bodywidth);
	}
	
	// 操作按钮列
	westsizes.push (100 / bodywidth);
	
	westsizes.push((bodywidth - 100) / <%=entries.size() + 1%>  / bodywidth);
	
	var leaveswidth = bodywidth;
	for (var i = 0; i < westsizes.length; ++i) {
		var subtractedwidth = westsizes[i] * bodywidth;
		westsizes[i] = (westsizes[i] * bodywidth) / leaveswidth;
		leaveswidth = leaveswidth - subtractedwidth;
	}
	
	// 初始化布局
	for (var i = 0; i < <%=entries.size() + 1%>; ++i) {
		var newlayout = center.layout({
			west: {size:westsizes[i] * 100 + '%'},
			west__paneSelector: '.col1' + i, center__paneSelector:'.col2' + i 
		});
		center = newlayout.panes.center;
	}
	
	$("body").resize(function(){
		$(".heightsolidcol").height($("body").height()- uiwidgetheaderheight);
	}).resize();
	
	
	$.getJSON(ctx + "/common/components/MultipleTreesSelectController/getTreesListSelectOptions.vot?component=" + comp, function(data){
		$(".titlespan").text(data.title);
		
		var ncolumns = {};
		for (var i = 0; i < data.columns.length; ++i) {
			ncolumns[data.columns[i].key] = data.columns[i].value; 
		}
		createLeftGridTree0();
		
		$("#leftdatagrid").datagrid({
			dataProvider:ctx + "/common/components/MultipleTreesSelectController/getListSelectItemsToSelect.vot?component=" + comp,
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
			$.post(ctx + "/common/components/MultipleTreesSelectController/getHistSelectedItems.vot?component=" + comp, "selectedItemSids=" + selectedItemSids + "&parentsid=" + parentsid, function (data){
				$("#rightgrid").datagrid("option", "dataProvider", data);
			}, 'json');
		}
	
	});
};

var lefttreesarr = [];

<%
i = 0;
for (Entry<String, Tree4MtlsOption> entry : entries) {
%>
	function createLeftGridTree<%=i%>(prevtreesid) {
		var prevtreesidstr = "";
		if (prevtreesid)
			prevtreesidstr = "&prevtreesid=" + prevtreesid;
		
		if ($("#treeul<%=i%>").data('uiTree')) {
			$("#treeul<%=i%>").tree('destroy');
		}
		
		var oneTree = $("#treeul<%=i%>").tree({
			asyncUrl: ctx + "/common/components/MultipleTreesSelectController/getTreesListSelectItemsToSelect.vot?component=" + comp + "&treekey=<%=entry.getKey()%>" + 
						prevtreesidstr + "&queryval=" + encodeURIComponent(encodeURIComponent($("#queryval").val())) + "&" + formdata,
			click: function() {
				var treeidx = <%=i%>;
				var treeobj = lefttreesarr[<%=i%>];
				if (treeidx == (<%=entries.size()%> - 1)) {
					$("#leftdatagrid").datagrid("option", "dataProvider", ctx + "/common/components/MultipleTreesSelectController/getListSelectItemsToSelect.vot?component=" + comp + "&treenode_sid=" + treeobj.tree("getSelectedNode").sid);
				} else {
					createLeftGridTree<%=i+1%>(treeobj.tree("getSelectedNode").sid);
				}
			}
			
			<%if (i == entries.size() - 1) { %>
			,dblclick: function(e, id, obj) {
				selectitems();
			}
			<%}%>
		});
		
		lefttreesarr.push(oneTree);
	}
<%
	++i;
}%>

function selectitems() {
	var selditems = $("#treeul<%=entries.size() - 1%>").tree("getSelectedNode");
	if (!selditems)
		return;
	addOneSelectItem(selditems);
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

</script>
</head>
<body>

<%
i = 0;
for (Entry<String, Tree4MtlsOption> entry : entries) {
%>
<div class="col1<%=i%>">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;"><%=entry.getValue().label %></span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
			</span>
		</div>
		<ul id="treeul<%=i%>" class="tree heightsolidcol"></ul>
	</div> 
</div>
<div class="col2<%=i%>">

<%if (i >= entries.size() - 1) {%>
	<div class="col1<%=(i+1)%>">
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
	<div class="col2<%=(i+1)%>">
		<div class="ui-widget ui-widget-content" style="height: 100%; margin-top: 2px;">
			<div class="ui-widget-header" style="height: 25px;">
				<span style="margin-top: 5px; display: block; margin-left: 5px; font-size: 14px; font-weight: bold;">${i18n['antelope.listselect.selected'] }<span class="titlespan"></span></span>
			</div>
			<div id="rightgrid" class="heightsolidcol"></div>
		</div>
	</div>
<%} %>
<%
	++i;
}
%>
<% for (Entry<String, Tree4MtlsOption> entry : entries) { %>
</div>
<%} %>

</body>
</html>

