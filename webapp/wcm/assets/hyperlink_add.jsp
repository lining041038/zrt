<%--
	title:图片资源添加页面
	author:lining
--%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.interfaces.components.supportclasses.SelectionFilter"%>
<%@page import="antelope.interfaces.components.supportclasses.TreeListSelectionOptions"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	$(".datagrid_container").datagrid({
		dataProvider: ctx + "/assets/imgasset/HyperLinkAssetController/getSitePagesInfo.vot",
		columns: {"name":"页面名称"},
		selectionMode:"singleRow"
	});
});

function getAssetDataSymbol() {
	if (!$(".datagrid_container").datagrid("option", "selectedItem")) {
		return null;
	}
	return $(".datagrid_container").datagrid("option", "selectedItem").sid;
}

</script>
</head>
<body>
	<div class="datagrid_container">
		
	</div>
</body>
</html>