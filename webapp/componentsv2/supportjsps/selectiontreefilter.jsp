<%--
	title:用户选择
	author:lining
--%>
<%@page import="antelope.interfaces.components.supportclasses.TreeSelectionOptions"%>
<%@page import="antelope.interfaces.components.supportclasses.TreeListSelectionOptions"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<%
TreeSelectionOptions opts = (TreeSelectionOptions) request.getAttribute("opts");
%>


<script>
var comp = '<%=request.getParameter("component")%>';
var formdata = '<%=request.getParameter("formdata")%>';
var filtertree = null;
var treefilterkey = '<%=request.getParameter("treefilterkey")%>';
$(function(){
	filtertree = $("#filtertree").tree({
		asyncUrl: ctx + "/common/componentsv2/TreeListSelectController/getFilterTreeItemsToSelect.vot?component=" + comp + 
				"&treefilterkey=" + treefilterkey + "&" + decodeURIComponent(formdata)
	});
});

function getFilteredData() {
	if (filtertree.tree("getSelectedNode") == null) {
		alert("请选择之后再确定！");
	}
	return filtertree.tree("getSelectedNode").sid;
}

</script>
</head>
<body class="sm_main">
	<ul id="filtertree" class="tree"></ul>
</body>
</html>

