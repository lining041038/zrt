<%@page import="antelope.interfaces.components.supportclasses.TreeOptions"%>
<%@page import="antelope.interfaces.components.supportclasses.MultipleTreesDatagridOptions"%>
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
<% request.setAttribute("componentVersion", "v2"); %>
<jsp:include page="/include/header2.jsp"/>
<%
	MultipleTreesDatagridOptions opts = (MultipleTreesDatagridOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
	String formprefix = "/" + RegExpUtil.getFirstMatched("(?<=/).*(?=\\.jsp)", request.getRequestURI().substring(1));
	
	Set<Entry<String, TreeOptions>> entries = opts.treeOptionMap.entrySet();
	int i = 0;
%>
<script>

var multipletrees = {};
$(function(){
	// 初始化布局
	var center = $("body");
	for (var i = 0; i < <%=entries.size()%>; ++i) {
		var newlayout = center.layout({
			west__paneSelector: '.col1' + i, center__paneSelector:'.col2' + i 
		});
		center = newlayout.panes.center;
	}
	
	// 动态调整树容器的高
	$(".col10").resize(function(){
		$(".tree").height($(".col10").height()- 40);
	}).resize();
	
	// 初始化树结构
	<%if (!opts.isIsolatedTrees) {//如果树都为孤立获取数据，则初始化时，需要所有的树都是都进行初始化%>
	if (rebuildNextTree0) {
		rebuildNextTree0();
	}
	<%}%>
	
	<%if (opts.isIsolatedTrees) {//如果树都为孤立获取数据，则初始化时，需要所有的树都是都进行初始化%>
		<%
		i = 0;
		for (Entry<String, TreeOptions> entry : entries) {
		%>
		rebuildNextTree<%=i%>();
			<%
			++i;
		}
		%>
	<%}%>
	
});
var treekeys = [];
<%
i = 0;
for (Entry<String, TreeOptions> entry : entries) {
%>

treekeys.push('<%=entry.getKey()%>');
function rebuildNextTree<%=i%>(prevtreesid) {
	var locpath = null;
	<%if (entry.getValue().locatePath != null){%>
	locpath = <%=new JSONArray(entry.getValue().locatePath)%>;
	<%}%>
	
	if ($("#treeul<%=i%>").data('uiTree')) {
		$("#treeul<%=i%>").tree('destroy');
	}
	
	multipletrees['tree<%=i%>'] = $("#treeul<%=i%>").tree({
		asyncUrl: ctx + opts.urlprefix + "/getChildren.vot?treekey=<%=entry.getKey()%>&prevtreesid=" + (prevtreesid || ''),
		locatePath:locpath,
		click: function() {
			var singletree = multipletrees['tree<%=i%>'];
			
			<%if (i < entries.size() - 1) {%>
				<% if (!opts.isIsolatedTrees) {//如果树都为孤立获取数据，则不进行树之间的联动 %>
				rebuildNextTree<%=i + 1%>(singletree.tree('getSelectedNode').sid);
				<%}%>
			<%} else {%>
			read.apply(this, arguments);
			<%}%>
			
			var iseditable = true;
			
			<%if (TextUtils.stringSet(entry.getValue().treeNodeEditableFunction)){%>
			if (<%=entry.getValue().treeNodeEditableFunction%>.apply(this, arguments) === false) {
				iseditable = false;	
			}
			<%}%>
			
			if (!<%=entry.getValue().treeRootEditable%> && !singletree.tree('getSelectedNode').parentNode) {
				iseditable = false;
			}
			
			$(".treeeditbtn<%=i %>").button("option", "disabled", !iseditable);
		}
	});
}
<%
	++i;
}
%>

function getTreenode_sid() {
	if (multipletrees['tree<%=entries.size() - 1%>'] && multipletrees['tree<%=entries.size() - 1%>'].tree('getSelectedNode')) {
		return multipletrees['tree<%=entries.size() - 1%>'].tree('getSelectedNode').sid;
	}
	return "";
}

function createTreeNode(idx) {
	if (!checkSelectParentNode(idx))
		return;
	
	var theparentsid = multipletrees['tree' + idx].tree('getSelectedNode').sid;
	$("#treeeditdialog" + idx).resetForm().submitDialog({
		url:ctx + opts.urlprefix + "/addOrUpdateOneTreeNode.vot?treekey=" + treekeys[idx] + "&parentsid=" + theparentsid, width:400, height:200, title:"<%=i18n.get("antelope.add") %>",
		state: "create",
		callback: function(){
			refreshCurrTreeNode(idx);
		}
	});
}

function updateTreeNode(idx) {
	if (!checkSelectParentNode(idx))
		return;
	var theparentsidparams = "";
	
	var theparentsid = "";
	if (multipletrees['tree' + idx].tree('getSelectedNode').parentNode) {
		theparentsid = "?parentsid=" + multipletrees['tree' + idx].tree('getSelectedNode').parentNode.sid;
	} else {
		if (idx > 0) {
			theparentsid = "?parentsid=" + multipletrees['tree' + (idx - 1)].tree('getSelectedNode').sid;
		}
	}
	
	$("#treeeditdialog" + idx).resetForm(multipletrees['tree' + idx].tree('getSelectedNode')).submitDialog({
		url:ctx + opts.urlprefix + "/addOrUpdateOneTreeNode.vot" + theparentsid + "&treekey=" + treekeys[idx], width:400, height:200, title:"<%=i18n.get("antelope.modify") %>",
		state: "create",
		callback: function(){
			refreshCurrTreeparentNode(idx);
		}
	});
}

function deletetreeitem(idx) {
	if (!checkSelectParentNode(idx))
		return;
	$.submitDelete({
		url: ctx + opts.urlprefix + "/deleteOneTreeNode.vot?treekey=" + treekeys[idx] + "&sid=" + multipletrees['tree' + idx].tree('getSelectedNode').sid,
		callback: function(data){
			if(data)
				return;
			refreshCurrTreeparentNode(idx);
		}
	});
}

function refreshCurrTreeNode(idx) {
	multipletrees['tree' + idx].tree('reAsyncChildNodes', multipletrees['tree' + idx].tree('getSelectedNode'), "refresh");
}

function refreshCurrTreeparentNode(idx) {
	multipletrees['tree' + idx].tree('reAsyncChildNodes', multipletrees['tree' + idx].tree('getSelectedNode').parentNode, "refresh");
}

function checkSelectParentNode(idx) {
	if (typeof idx == 'undefined') {
		if (!multipletrees['tree<%=entries.size() - 1%>'] || !(multipletrees['tree<%=entries.size() - 1%>'].tree('getSelectedNode'))) {
			alert("<%=i18n.get("multiple_trees_datagrid." + component + ".pleaseselectparent")%>");
			return false;
		}
	} else {
		if (!multipletrees['tree' + idx] || !(multipletrees['tree' + idx].tree('getSelectedNode'))) {
			alert("<%=i18n.get("multiple_trees_datagrid." + component + ".pleaseselectparent")%>");
			return false;
		}
	}
	
	return true;
}


</script>

<jsp:include page="/componentsv2/supportjsps/singledatagridjspart.jsp">
	<jsp:param name="gridprovidersuffix" value="+'&treenode_sid='+getTreenode_sid()"/>
	<jsp:param name="precreateitem" value="if (!checkSelectParentNode())return;"/>
</jsp:include>

</head>
<body class="sm_main">

<!-- 表单开始 -->
<jsp:include page="/componentsv2/supportjsps/singledatagrid_dialogformpart.jsp"></jsp:include>

<!-- 左侧树节点增改表单 -->
<%
i = 0;
for (Entry<String, TreeOptions> entry : entries) {
	String formname = formprefix + entry.getKey() + "form.jsp";
%>
<form id="treeeditdialog<%=i %>" style="display: none;">
	<jsp:include page="<%=formname%>"/>
</form>
<%
	++i;
} 
%>
<!-- 表单结束 -->
<!-- 主界面 -->
<%
i = 0;
for (Entry<String, TreeOptions> entry : entries) {
%>
<div class="col1<%=i%>">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;"><%=entry.getValue().label %></span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
				<%if (entry.getValue().showTreeNodeCreateBtn) { %>
				<button onclick="createTreeNode(<%=i %>)" class="button ui-icon-plusthick" style="height: 28px;width:34px;" title="<%=i18n.get("antelope.add") %>"></button>
				<%} %>
				<%if (entry.getValue().showTreeNodeUpdateBtn) { %>
				<button onclick="updateTreeNode(<%=i %>)" class="button ui-icon-pencil treeeditbtn<%=i %>" style="height: 28px;width:34px;" title="<%=i18n.get("antelope.modify") %>"></button>
				<%} %>
				<%if (entry.getValue().showTreeNodeDeleteBtn) { %>
				<button onclick="deletetreeitem(<%=i %>)" class="button ui-icon-trash treeeditbtn<%=i %>" style="height: 28px;width:34px;" title="<%=i18n.get("antelope.delete") %>"></button>
				<%} %>
			</span>
		</div>
		<ul id="treeul<%=i%>" class="tree"></ul>
	</div> 
</div>
<div class="col2<%=i%>">

<%if (i >= entries.size() - 1) {%>
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;"><%=i18n.get("multiple_trees_datagrid." + component + ".listtitle") %></span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
			</span>
		</div>
		<jsp:include page="/componentsv2/supportjsps/singledatagrid_mainbodypart.jsp"></jsp:include>
	</div>
<%} %>

<%
	++i;
}
%>
<% for (Entry<String, TreeOptions> entry : entries) { %>
</div>
<%} %>
</body>
</html>