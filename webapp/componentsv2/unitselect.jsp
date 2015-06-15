<%--
	title:单位（组织机构）选择
	author:lining
--%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
.splitdiv{width: 100%; height: 100%;float: left;}
</style>
<script>
var zTree = null;
$(function(){
	zTree = $("#org_tree").unitTree();
});

function getSelectedUnit() {
	var unit = zTree.getSelectedNode();
	return zTree.getSelectedNode();
}

</script>
</head>
<body>
	<div class="splitdiv">
		<div id="org_tree" class="tree"></div>
	</div>
</body>
</html>

