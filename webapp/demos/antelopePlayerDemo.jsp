<%@page import="antelope.utils.I18n"%>
<%@page import="com.sun.xml.internal.fastinfoset.sax.Properties"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	
	/*
	* $("#videodiv").loadAntelopePlayer({flashvars:{"src":"rtmp://localhost/vod/mp4:mmn.mp4"}});  
	* $("#audiodiv").loadAntelopePlayer({flashvars:{"src":"rtmp://localhost/vod/mp3:mmn"}, playermode:"audio"}); // 流媒体点播时音频目前无法自动读取播放位置
	"autoplay":false,
	*/
	$("#videodiv").loadAntelopePlayer({flashvars:{"src":"rtmp://localhost/vod/mp4:mmn.mp4", 
		"srchigh":"rtmp://localhost/vod/mp4:sample1_1500kbps.f4v","srclow":"rtmp://localhost/vod/mp4:sample1_700kbps.f4v",
		seekTime:25, pauseTime:90 }, wmode:'transparent', 
		keyframes:[{currentTime:10, desc:"测试第一帧描述测试第一帧描述测试第一帧"},{currentTime:110, desc:"测试第一帧描述测试第一帧描述测试第二帧"}]
	});
	//$("#audiodiv").loadAntelopePlayer({flashvars:{"src":ctx + "/demos/assets/mmn.mp3", playermode:"audio", audioname:"音频名称"}, wmode:'transparent', });
	//$("#editvideodiv").loadAntelopePlayer({flashvars:{"src":ctx + "/demos/assets/mmn.mp4", playermode:"video", rangeselectable: true}});
});

function getVideoCurrPlayedTime() {
	alert($("#videodiv").getCurrPlayedTime());
}

function getAudioCurrPlayedTime() {
	alert($("#audiodiv").getCurrPlayedTime());
}

function getVideoCurrPlayedRangeTime() {
	alert(JSON.stringify($("#editvideodiv").getCurrSelectedRangeTime()));
}

</script>
<title>Insert title here</title>
</head>
<body style="background: blue;">
	<div id="videodiv" style="height:300px;">
		
	</div>
	<button type="button" onclick="getVideoCurrPlayedTime()">获取视频当前播放时间(秒)</button>
	<div id="audiodiv" style="height:100px;">
		
	</div>
	<button type="button" onclick="getAudioCurrPlayedTime()">获取音频当前播放时间(秒)</button>
	
	
	<div id="editvideodiv" style="height:300px;">
		
	</div>
	<button type="button" onclick="getVideoCurrPlayedRangeTime()">获取视频当前时间区段(秒)</button>
</body>
</html>