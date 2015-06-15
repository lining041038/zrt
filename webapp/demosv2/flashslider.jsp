<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	$("body").flashslider("destroy").flashslider({
		dataProvider: ctx + "/demos/flashslider/FlashSliderController/getSliderDatas.vot",
		width:500,
		height:300
	});
});
</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
</body>
</html>