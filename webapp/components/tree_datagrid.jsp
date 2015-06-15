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
	TreeDatagridOptions opts = (TreeDatagridOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
	String formprefix = "/" + RegExpUtil.getFirstMatched("(?<=/).*(?=\\.jsp)", request.getRequestURI().substring(1));
	String treeformname = formprefix + "treeform.jsp";
	String formname = formprefix + "form.jsp";
%>
<script>

var zTree = null;

var treediagformwidth = 0;
var treediagformheight = 0;


$(function(){
	$("#treecol").resize(function(){
		$(".autoheightnode").height($("#treecol").height()- 40);
	}).resize();
	
	var locpath = null;
	<%if (opts.locatePath != null){%>
	locpath = <%=new JSONArray(opts.locatePath)%>;
	<%}%>
	
	for (var tkey in opts.treeContextMenu) {
		opts.treeContextMenu[tkey].click = window[opts.treeContextMenu[tkey].click]; 
	}
	
	zTree = $("#treeul").tree({
		asyncUrl: ctx + opts.urlprefix + "/getChildren.vot",
		expandTreeNodeByClick: <%=opts.expandTreeNodeByClick%>,
		contextMenu: opts.treeContextMenu,
		locatePath:locpath,
		click: function() {
			if (<%=opts.reInitDatagridByTreeNodeSid%>) {
				$.getJSON(ctx + opts.urlprefix + "/getOptionsByRequest.vot?treenode_sid=" + getTreenode_sid() + "&<%=request.getQueryString()%>", function(data){
					opts = data;
					builddatagrid__();
					<%if (opts.reLoadQueryFormWhenDatagridReInited){%>
					rebuildqueryformdiv_("?treenode_sid=" + getTreenode_sid() + "&component=<%=component%>");
					<%}%>
				});
			} else {
				read.apply(this, arguments);
			}
			
			var iseditable = true;
			<%if (TextUtils.stringSet(opts.treeNodeEditableFunction)){%>
				if (<%=opts.treeNodeEditableFunction%>.call(this, arguments[0], arguments[1], zTree.tree('getSelectedNode')) === false) {
					iseditable = false;	
				}
			<%}%>
			
			if (!<%=opts.treeRootEditable%> && !zTree.tree('getSelectedNode').parentNode) {
				iseditable = false;
			}
			
			$(".treeeditbtn").button("option", "disabled", !iseditable);
		}
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
	
	resetTreeDialgFormWidthHeight();
	
	var theparentsid = zTree.tree('getSelectedNode').sid;
	$("#treeeditdialog").resetForm().submitDialog({
		url:ctx + opts.urlprefix + "/addOrUpdateOneTreeNode.vot?parentsid=" + theparentsid, width:treediagformwidth, height:treediagformheight, title:"<%=i18n.get("tree_datagrid." + component + ".addtreenode") %>",
		state: "create",
		callback: refreshCurrTreeNode
	});
	
	$("#treeeditdialog").trigger('openedTreeNodeCreateForm');
}

function updateTreeNode() {
	if (!checkSelectParentNode())
		return;
	
	resetTreeDialgFormWidthHeight();
	
	var theparentsidparams = "";
	if (zTree.tree('getSelectedNode').parentNode) {
		theparentsid = "?parentsid=" + zTree.tree('getSelectedNode').parentNode.sid;
	}
	$("#treeeditdialog").trigger('openTreeNodeUpdateForm', zTree.tree('getSelectedNode'));
	$("#treeeditdialog").resetForm(zTree.tree('getSelectedNode')).submitDialog({
		url:ctx + opts.urlprefix + "/addOrUpdateOneTreeNode.vot" + theparentsid, width:treediagformwidth, height:treediagformheight, title:"<%=i18n.get("tree_datagrid." + component + ".modifytreenode") %>",
		state: "create",
		callback: refreshCurrTreeparentNode
	});
	
	$("#treeeditdialog").trigger('openedTreeNodeUpdateForm', zTree.tree('getSelectedNode'));
}

function moveTreeNode(upstr) {
	
	if (zTree.tree("getSelectedNode") == null) {
		alert("<%=i18n.get("tree_datagrid." + component + ".pleaseselectnode")%>");
		return;
	}
	
	var locatepath = [zTree.tree('getSelectedNode').sid];
	
	var parentNode = zTree.tree('getSelectedNode').parentNode;
	
	while(parentNode) {
		locatepath.unshift(parentNode.sid);
		parentNode = parentNode.parentNode;
	}
	
	parentNode = zTree.tree('getSelectedNode').parentNode;
	var parentsidparam = "";
	if (parentNode) {
		parentsidparam += "&parentsid=" + parentNode.sid; 
	}
	$.post(ctx + opts.urlprefix + "/moveUpOrDownTreeNode.vot?sid=" + zTree.tree('getSelectedNode').sid + parentsidparam + upstr, function(){
		zTree.tree("reAsyncChildNodes", parentNode, "refresh");
		zTree.tree("locate", locatepath);
	});
}

function moveUpTreeNode() {
	moveTreeNode('&isup=true');
}

function moveDownTreeNode() {
	moveTreeNode('');
}

function resetTreeDialgFormWidthHeight() {
	
	var origwidth = $("#treeeditdialog").data("origwidth");
	var origheight = $("#treeeditdialog").data("origheight");
	if (!origwidth) {
		origwidth = $("#treeeditdialog").width();
		origheight = $("#treeeditdialog").height();
		$("#treeeditdialog").data("origwidth", origwidth);
		$("#treeeditdialog").data("origheight", origheight);
	}
	
	treediagformwidth = origwidth + 45;
	treediagformheight = origheight + 81;
	
	treediagformwidth = Math.min ($(window).width() - 10, treediagformwidth);
	treediagformheight = Math.min ($(window).height() - 10, treediagformheight);
}

function deletetreeitem() {
	if (!checkSelectParentNode())
		return;
	$.submitDelete({
		url: ctx + opts.urlprefix + "/deleteOneTreeNode.vot?sid=" + zTree.tree('getSelectedNode').sid,
		confirmText: '<%=i18n.get("tree_datagrid." + component + ".deleteconfirm") %>',
		callback: function(data){
			if(data)
				return;
			refreshCurrTreeparentNode(true);
		}
	});
}

function refreshCurrTreeNode() {
	if (window['antelope_createOrUpdateTreeNodeComplete']) {
		if (window['antelope_createOrUpdateTreeNodeComplete']() === false)
			return;
	}
	
	zTree.tree('reAsyncChildNodes', zTree.tree('getSelectedNode'), "refresh");
}

function refreshCurrTreeparentNode(isdelete) {
	if (window['antelope_createOrUpdateTreeNodeComplete']) {
		if (window['antelope_createOrUpdateTreeNodeComplete'](isdelete) === false)
			return;
	}
	
	var locatpath = [];
	var parentNode = zTree.tree('getSelectedNode');
	while (parentNode) {
		locatpath.unshift(parentNode.sid);
		parentNode = parentNode.parentNode;
	}
	// 无论修改还是删除，需要刷新前都重置到该节点或者离所操作的节点最近的节点
	zTree.tree("option", "locatePath", locatpath);
	zTree.tree('reAsyncChildNodes', zTree.tree('getSelectedNode').parentNode, "refresh");
}

function checkSelectParentNode() {
	if (zTree.tree("getSelectedNode") == null) {
		alert("<%=i18n.get("tree_datagrid." + component + ".pleaseselectparent")%>");
		return false;
	}
	
	<%if (TextUtils.stringSet(opts.checkCanAddGridDataByTreeNodeFunction)) {%>
		if (<%=opts.checkCanAddGridDataByTreeNodeFunction%>(zTree.tree('getSelectedNode')) !== true) {
			return false;
		}
	<%}%>
	
	return true;
}

</script>

<%if (opts.alertNeedSelectTreeNode) {%>
<jsp:include page="/components/supportjsps/singledatagridjspart.jsp">
	<jsp:param name="gridprovidersuffix" value="+'&treenode_sid='+getTreenode_sid()"/>
	<jsp:param name="precreateitem" value="if (!checkSelectParentNode())return;"/>
</jsp:include>
<%} else { %>
<jsp:include page="/components/supportjsps/singledatagridjspart.jsp">
	<jsp:param name="gridprovidersuffix" value="+'&treenode_sid='+getTreenode_sid()"/>
</jsp:include>
<%} %>

</head>
<body class="sm_main ui-layout">
<jsp:include page="/components/supportjsps/singledatagrid_dialogformpart.jsp"></jsp:include>

<!-- 左侧树节点增改 -->
<form id="treeeditdialog" style="display: none;">
	<jsp:include page="<%=treeformname %>"></jsp:include>
</form>

<!-- 主界面 -->
<div class="ui-layout-west">
	<div id="treecol" class="ui-widget ui-widget-content" style="height: 99%;margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;"><%=i18n.get("tree_datagrid." + component + ".treetitle") %></span>
			<span class="treegridnodeheader" style="display: block; position: absolute; top: 8px; right: 5px;">
				<style>
				.treegridnodeheader .ui-button-text-icon-primary .ui-button-icon-primary,
				.treegridnodeheader .ui-button-text-icons .ui-button-icon-primary, 
				.treegridnodeheader .ui-button-icons-only .ui-button-icon-primary { left: 3px!important; }
				</style>
				<% 
					Set<Entry<String, Button>> treebtnes = opts.treeButtons.entrySet();
					for (Entry<String, Button> treebtn : treebtnes) {
						String btnkey = treebtn.getKey();
						if ("ui-icon-arrowthick-1-n treeeditbtn".equals(btnkey) && !opts.showTreeNodeMoveBtns
							||"ui-icon-arrowthick-1-s treeeditbtn".equals(btnkey) && !opts.showTreeNodeMoveBtns
							||"ui-icon-plusthick".equals(btnkey) && !opts.showTreeNodeCreateBtn
							||"ui-icon-pencil treeeditbtn".equals(btnkey) && !opts.showTreeNodeUpdateBtn
							||"ui-icon-trash treeeditbtn".equals(btnkey) && !opts.showTreeNodeDeleteBtn)
							continue;
						
				%>
				<button onclick="<%=treebtn.getValue().click %>()" class="button <%=btnkey %>" style="height: 22px;width:24px;" 
					title="<%=treebtn.getValue().toolTip.startsWith(":") ? i18n.get(treebtn.getValue().toolTip) : TextUtils.noNull(treebtn.getValue().toolTip)%>"></button>
				
				<%	} %>
			</span>
		</div>
		<ul id="treeul" class="tree autoheightnode"></ul>
	</div> 
</div>
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 99%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;"><%=i18n.get("tree_datagrid." + component + ".listtitle") %></span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
			</span>
		</div>
		<div class="autoheightnode" style="overflow-x: hidden;overflow-y: auto; position: relative;">
			<jsp:include page="/components/supportjsps/singledatagrid_mainbodypart.jsp"></jsp:include>
		</div>
	</div>
</div>
</body>
</html>