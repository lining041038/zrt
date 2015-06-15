<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

$(function(){
	$("body").viewNavigator({
		firstView:$("#fview"),
		effect:'slide'
	});
});

function gotoNextView() {
	$("body").viewNavigator("pushView", $("#nfview"));
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="gotoNextView()">切换到下一视图</button>
<div id="fview">
视图一
</div>
<div id="nfview" style="display: none;">
视图二
</div>
</body>
</html>