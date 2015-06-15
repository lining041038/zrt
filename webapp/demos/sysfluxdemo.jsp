<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	setInterval(function(){
		$.getJSON(ctx + "/demos/sysfluxdemo/FlowMonitorController/getKPFWFLUX.vot", function(data){
			data.nTotalRecv = data.nTotalRecv/ 1024 / 1024;
			data.nTotalSend = data.nTotalSend/ 1024 / 1024;
			data.nRecvSpeed = data.nRecvSpeed/ 1024;
			data.nSendSpeed = data.nSendSpeed/ 1024;
			$("body").wrapDataByClass(data);
		});
	}, 500);
});
</script>
</head>
<body style="height: 800px; overflow: scroll;">
	<div>已下载流量：<span class="nTotalRecv"></span>MB</div>
	<div>已上传流量：<span class="nTotalSend"></span>MB</div>
	<div>下载速度：<span class="nRecvSpeed"></span>KB</div>
	<div>上传速度：<span class="nSendSpeed"></span>KB</div>
</body>
</html>