<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
function selectByTile(selectionModevar) {
	$.selectByTile({
		component:"tileselectdemo",
		selectionMode:selectionModevar,
		callback:function(data) {
			$("#xuanzhong").text(JSON.stringify(data));
			return true;
		},
		data:"sid=1234",
		width: 800,
		height: 800,
		queryval:"人",
		selectedItemSids:"1234" // 逗号分割的字符串
	});
}
</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="selectByTile('singleRow')">selectByTile(单选)</button>
<div id="xuanzhong"></div>
</body>
</html>