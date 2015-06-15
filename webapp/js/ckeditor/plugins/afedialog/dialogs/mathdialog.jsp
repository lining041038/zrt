<%--
	@author lining
	@date 2013-01-06
--%>
<%@page import="java.util.UUID"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	$("body").loadAntelopeFormulaEditor();
});

function reloadAntelopeFormulaEditor() {
	$("body").empty().loadAntelopeFormulaEditor();
}

function getLatex() {
	return $("body").getFormulaLatex();
}
</script>
</head>
<body style="height: 500px; overflow:hidden;">

</body>
</html>