<%@page import="antelope.utils.I18n"%>
<%@page import="com.sun.xml.internal.fastinfoset.sax.Properties"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
    $("#myChartId").fusionchart({
    	title:"测试title",
    	displayName:"单序列图例",
    	labelField: "label",
    	type:"columnChart",
    	columnChartType:'clustered',
    	field:"result",
    	categoryTitle:"横坐标标题",
    	fillColor:0xff0000,
    	dataProvider: [{
			  label: "2012-05-03 03", result:-10, label2: "测试一新", result2: 40
		},{
			  label: "2012-06-03 0", result:300, label2: "测试一新", result2: 55
		}]
    });
});
function updatedata() {
	$("#myChartId").fusionchart("option", "dataProvider", [{
		  label: "2012-05-03 03", result:20, label2: "测试一新", result2: 40
	},{
		  label: "2012-06-03 0", result:50, label2: "测试一新", result2: 55
	}]);
}
</script>
</head>
<body style="height: 800px; overflow: scroll;">
<button onclick="updatedata()">更新数据</button>
<div style="height: 2000px;">
<div id="myChartId" style="float: left; width: 49%; height: 500px;"></div>
</div>
</body>
</html>