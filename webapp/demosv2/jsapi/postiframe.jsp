<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

function callPostIframe() {
	$.postIframe(ctx + "/demos/jsapi/postiframe/PostIframeDemoController/postIframeCalled.vot", function(data){
		alert(data);
	});
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<h1>$.postIframe</h1>
<button onclick="callPostIframe()">调用$.postIframe</button>
</body>
</html>