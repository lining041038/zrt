<%--
	title:多角色选择
	author:lining
--%>
<%@page import="antelope.springmvc.BaseController"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
.splitdiv{width: 30%; height: 100%;float: left;}
</style>
<script>

$(function(){
	$.getJSON(ctxPath + "/common/getAllRoles.vot", function(data){
		$("body").datagrid({
			dataProvider:data, numPerPage:1000,
			showSeqNum :true, selectionMode:"multipleRows",
			columns:{
				name:"角色名称"
			}
		});
	});
});

function getSelectedRoles() {
	return $("body").datagrid("option", "selectedItems");
}
</script>
</head>
<body class="sm_main" style="overflow-x:hidden;">
</body>
</html>

