<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
function selectByTree(selectionModevar, treecheckable) {
	$.selectByTreesList({
		component:"treeslistselectdemo",
		selectionMode:selectionModevar,
		callback:function(data) {
			$("#xuanzhong").text(JSON.stringify(data));
			return true;
		},
		selectedItemSids:"1372475439648,1372475439652" // 逗号分割的字符串
	});
}
</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="selectByTree('multipleRows')">selectByTreesList(多选)</button>
<button onclick="selectByTree('singleRow')">selectByTreesList(单选)</button>
<div id="xuanzhong"></div>
</body>
</html>