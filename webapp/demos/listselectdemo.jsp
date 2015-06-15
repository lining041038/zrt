<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
function selectByList(selectionModevar) {
	$.selectByList({
		component:"listselectdemo",
		selectionMode:selectionModevar,
		callback:function(data) {
			$("#xuanzhong").text(JSON.stringify(data));
			return true;
		},
		//data:"sid=1234", 此处可以添加参数 
		queryval:"",
		height:600,
		selectedItemSids:"1383122038086" // 逗号分割的字符串
	});
}
</script>
</head>
<body>
<button onclick="selectByList('multipleRows')">selectByList(多选)</button>
<button onclick="selectByList('singleRow')">selectByList(单选)</button>
<div id="xuanzhong"></div>
</body>
</html>