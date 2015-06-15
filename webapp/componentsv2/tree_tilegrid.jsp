<%@page import="antelope.interfaces.components.supportclasses.TreeTilegridOptions"%>
<%@page import="antelope.utils.JSONArray"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.interfaces.components.supportclasses.Button"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.interfaces.components.supportclasses.TreeDatagridOptions"%>
<%@page import="antelope.db.DBUtil"%>
<%@page import="antelope.services.SessionService"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<%
	TreeTilegridOptions opts = (TreeTilegridOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
	String formprefix = "/" + RegExpUtil.getFirstMatched("(?<=/).*(?=\\.jsp)", request.getRequestURI().substring(1));
	String treeformname = formprefix + "treeform.jsp";
	String formname = formprefix + "form.jsp";
%>
<script>

var zTree = null;
$(function(){
	$("#treecol").resize(function(){
		$("#treeul").height($("#treecol").height()- 40);
		$(".datagrid_container").height($("#treecol").height() - 40 - $("#singlegridform_query").height() - 5);
	}).resize();
	
	var locpath = null;
	<%if (opts.treeOptions.locatePath != null){%>
	locpath = <%=new JSONArray(opts.treeOptions.locatePath)%>;
	<%}%>
	
	zTree = $("#treeul").tree({
		asyncUrl: ctx + opts.urlprefix + "/getChildren.vot",
		locatePath:locpath,
		click: function() {
			read.apply(this, arguments);
		}
	});
	
	$(".datagrid_container").tilegrid({
		dataProvider:ctx + opts.urlprefix + "/getSingleGridList.vot?" + encodeURI($("#singlegridform_query").serialize()) + "&treenode_sid=" + getTreenode_sid(),
		tileRenderer: <%=opts.tileRendererFunction%>
	});
});

function getTreenode_sid() {
	if (zTree.tree('getSelectedNode')) {
		return zTree.tree('getSelectedNode').sid;
	}
	return "";
}

function createTreeNode() {
	if (!checkSelectParentNode())
		return;
	
	var theparentsid = zTree.tree('getSelectedNode').sid;
	$("#treeeditdialog").resetForm().submitDialog({
		url:ctx + opts.urlprefix + "/addOrUpdateOneTreeNode.vot?parentsid=" + theparentsid, width:400, height:200, title:"<%=i18n.get("tree_tilegrid." + component + ".addtreenode") %>",
		state: "create",
		callback: refreshCurrTreeNode
	});
}

function updateTreeNode() {
	if (!checkSelectParentNode())
		return;
	
	var theparentsidparams = "";
	if (zTree.tree('getSelectedNode').parentNode) {
		theparentsid = "?parentsid=" + zTree.tree('getSelectedNode').parentNode.sid;
	}
	
	$("#treeeditdialog").resetForm(zTree.tree('getSelectedNode')).submitDialog({
		url:ctx + opts.urlprefix + "/addOrUpdateOneTreeNode.vot" + theparentsid, width:400, height:200, title:"<%=i18n.get("tree_tilegrid." + component + ".addtreenode") %>",
		state: "create",
		callback: refreshCurrTreeparentNode
	});
}

function deletetreeitem() {
	if (!checkSelectParentNode())
		return;
	$.submitDelete({
		url: ctx + opts.urlprefix + "/deleteOneTreeNode.vot?sid=" + zTree.tree('getSelectedNode').sid,
		callback: function(data){
			if(data)
				return;
			refreshCurrTreeparentNode();
		}
	});
}

function refreshCurrTreeNode() {
	zTree.tree('reAsyncChildNodes', zTree.tree('getSelectedNode'), "refresh");
}

function refreshCurrTreeparentNode() {
	zTree.tree('reAsyncChildNodes', zTree.tree('getSelectedNode').parentNode, "refresh");
}

function checkSelectParentNode() {
	if (zTree.tree("getSelectedNode") == null) {
		alert("<%=i18n.get("tree_tilegrid." + component + ".pleaseselectparent")%>");
		return false;
	}
	
	return true;
}

function read() {
	$(".datagrid_container").tilegrid("option", "dataProvider", ctx + opts.urlprefix + "/getSingleGridList.vot?" + encodeURI($("#singlegridform_query").serialize()) + "&treenode_sid=" + getTreenode_sid());
}

</script>
</head>
<body class="sm_main ui-layout">

<!-- 左侧树节点增改 -->
<form id="treeeditdialog" style="display: none;">
	<jsp:include page="<%=treeformname %>"></jsp:include>
</form>

<!-- 主界面 -->
<div class="ui-layout-west">
	<div id="treecol" class="ui-widget ui-widget-content" style="height: 100%;margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;"><%=i18n.get("tree_tilegrid." + component + ".treetitle") %></span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
				<%if (opts.treeOptions.showTreeNodeCreateBtn) { %>
				<button onclick="createTreeNode()" class="button ui-icon-plusthick" style="height: 28px;width:34px;" title="<%=i18n.get("tree_tilegrid." + component + ".addtreenode") %>"></button>
				<%} %>
				<%if (opts.treeOptions.showTreeNodeUpdateBtn) { %>
				<button onclick="updateTreeNode()" class="button ui-icon-pencil treeeditbtn" style="height: 28px;width:34px;" title="<%=i18n.get("tree_tilegrid." + component + ".modifytreenode") %>"></button>
				<%} %>
				<%if (opts.treeOptions.showTreeNodeDeleteBtn) { %>
				<button onclick="deletetreeitem()" class="button ui-icon-trash treeeditbtn" style="height: 28px;width:34px;" title="<%=i18n.get("tree_tilegrid." + component + ".deletetreenode") %>"></button>
				<%} %>
			</span>
		</div>
		<ul id="treeul" class="tree"></ul>
	</div> 
</div>
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;"><%=i18n.get("tree_tilegrid." + component + ".listtitle") %></span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
			</span>
		</div>
		<!-- 主界面  -->
		<form id="singlegridform_query" onsubmit="read(); return false;">
			<h3 class="smm_title"><span class="moduletitle"></span><span class="m_head" id="basicinfo"></span><span class="m_line"></span></h3>
			<div class="condition basicinfo">
				<%if (TextUtils.stringSet(opts.queryformKey)) { %>
				<jsp:include page="<%=opts.queryformKey%>"></jsp:include>
				<%} %>
			</div>
		</form>
		<div class="datagrid_container" style="overflow: auto;"></div>
	</div>
</div>
</body>
</html>