<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
html,body{margin: 0;padding: 0;width: 100%;height: 100%;}
</style>
<script type="text/javascript">
$(function(){
	$("body").loadDataMap({
		flashvars:{chinaColor:0xff7373},
	//	province:"330000",
		smallProvRollOverColor:0x00ff00, // 鼠标放上去的颜色改变
		highlightxml:'<root color="0xff0000">\
		<p441700 color="0x00ff00"/>\
		<p441800/>\
		<p430000 color="0xff7373"/>\
		<p110000 color="0xff5353"/>\
		<p140000 color="0xffa3a3"/>\
		<p440000/>\
		<p450000 color="0xaf9393"/>\
		<p470000/>\
		<p441600/>\
		</root>',
		datasources:[{
			label:"天津天气预报",
			detailDataProvider: "<%=request.getContextPath()%>/demos/datamap.js"
		}], showDateRegion:false, openSmallRegion: true, showWorld:true,
		onregionclick: "clickfunction", openBigProvince: true, // 若不允许显示大省图，则将此选项设置成 false
		smallProvinceclick:"smallProvinceclick" // 此选项在点击大省地图时回调
	});
});
//330000
function clickfunction(sid) {
	//alert(sid);
}

function smallProvinceclick(resionsid) {
	//alert(resionsid);
}

</script>
</head>
<body>
</body>
</html>

