<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
html,body{margin: 0;padding: 0;width: 100%;height: 100%;}
</style>
<script type="text/javascript">

function autoSizeImg(thisObj) {
	var width = thisObj.width;
	var height = thisObj.height;
	
	$(thisObj).width('auto').height('auto');
	var origWidth = $(thisObj).width();
	var origHeight = $(thisObj).height();
	var radio = origWidth / origHeight;
	
	var scalerration = Math.max(origWidth / width, origHeight / height);
	
	$(thisObj).width( origWidth / scalerration);
	$(thisObj).height(origHeight / scalerration);
}

</script>
</head>
<body>

	<img src="HLSPlayerB.jpg" onload="autoSize(this)" width="300" height="300"/>

</body>
</html>




