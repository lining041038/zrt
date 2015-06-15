<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
</style>
<script>
$(function(){
	// 获取组播发送者相关参数
	$.getJSON(ctx + "/modules/amsmulticastlive/AmsMulticastLiveController/getSenderOpts.vot", function(data) {
		$("body").loadAmsMulticastLive({
			wmode:'transparent',
			ipMulticastAddress: data.ipMulticastAddress,
			publishPassword: data.publishPassword,
			user: data.user,
			liveOrRecord:"live",
			type:"sender",
			streamName:"z1",
			videotoselecturl: "/modules/amsmulticastlive/AmsMulticastLiveController/getVideoSelectTreeChildren.vot"
		});
	});
});
</script>
</head>
<body>
</body>
</html>