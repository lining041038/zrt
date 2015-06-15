<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

function exportWord() {
	$.postIframe(ctx + "/demos/wordexport/WordExportDemoController/exportWord.vot");	
}

$(function(){
	$("#fdsfds").loadWorkFlow({});
});
</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="exportWord()">根据模板导出word</button>
<div id="fdsfds">
</div>
</body>
</html>