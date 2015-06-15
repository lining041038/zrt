<%@page import="antelope.utils.I18n"%>
<%@page import="com.sun.xml.internal.fastinfoset.sax.Properties"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
// 	$("body").loadAmsComp({type:"livesend", sid:"0"});
	//$.get('http://www.baidu.com', function(data){
	//	alert(data);
	//});
	//$.postIframe('http://www.sina.com', {}, function(data)	{
//		alert(data);
//	});
	
	$.getScript('http://www.baidufdsafdasfdasfdsafdas.com', function(e) {
		var myde = e;
	});
	
});

function fdfdsfds() {
	
	var mycontentwindow = $("#dfdd")[0].contentWindow;
	debugger;
	
	alert(mycontentwindow);
}

</script>
<title>Insert title here</title>
</head>
<body>
<button type="button" onclick="fdfdsfds()">dfdfsfd</button>
<iframe id="dfdd" src="http://www.baidu.com"></iframe>
</body>
</html>