<%--
	@author lining
	@date 2012-07-15
--%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

$(function(){
	$("#datagrid_container").datagrid({
		dataProvider:ctxPath + "/demos/single_datagrid/SingleDataGridController/getSingleGridList.vot",
		cellEditUrl:ctx + '/demos/DataGridController/cellEdit.vot',
		columns:{name:{headerText:"测试列", editable:true}},
		numPerPage: 100
	});
});

function loaddata() {
	$("#datagrid_container").datagrid("option", "dataProvider", ctxPath + "/demos/DataGridController/loadManyDatas.vot");
}

function selectdropdown() {
	$("#datagrid_container").datagrid("destroy").datagrid({
		dataProvider:[{name:"可编辑", opts:[{label:"aaa", value:"aaa"},{label:"bb", value:"bb"}]}],
		columns:{name:{headerText:"测试列", editable:true, itemEditor:{type:"select", optionField:"opts", labelField:"label", valueField:"value"}}},
		numPerPage: 100
	});
}

function selectdropdownalways() {
	$("#datagrid_container").datagrid("destroy").datagrid({
		dataProvider:[{name:"可编辑", opts:[{label:"aaa", value:"aaa"},{label:"bb", value:"bb"}]}],
		columns:{name:{headerText:"测试列", editable:true, itemEditor:{type:"select", optionField:"opts", labelField:"label", valueField:"value",
			showWhen:'always'}}},
		numPerPage: 100
	});
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="loaddata()">加载大量数据</button>
<button onclick="selectdropdown()">下拉列表可编辑单元格</button>
<button onclick="selectdropdownalways()">直接显示下拉列表</button>
<div id="datagrid_container" class="datagrid_container">

</div>
</body>
</html>