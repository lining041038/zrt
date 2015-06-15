<%@page import="antelope.utils.I18n"%>
<%@page import="com.sun.xml.internal.fastinfoset.sax.Properties"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

var cpudata = [{val:1}, {val:2}];
var memedata = [{val:10}, {val:13}];
$(function(){
	$("#spa").flashchart({
		wmode:'transparent',
		chartsTileWidth:560,
		chartsTileHeight:320,
		field:"val",
		charts:{
			server1: {
				title: "server1title",
				type:"sparkLineChart",
				series:[{
					sid:"cpuseries",
					displayName:'cpu',
					dataProvider:cpudata
				}, {
					sid:"memoryseries",
					displayName:'memory',
					dataProvider:memedata
				}]
			},
			server2: {
				title: "server2title",
				type:"sparkLineChart",
				series:[{
					sid:"cpuseries",
					displayName:'cpu',
					dataProvider:cpudata
				}, {
					sid:"memoryseries",
					displayName:'memory',
					dataProvider:memedata
				}]
			},
			server3: {
				title: "server2title",
				type:"sparkLineChart",
				series:[{
					sid:"cpuseries",
					displayName:'cpu',
					dataProvider:cpudata
				}, {
					sid:"memoryseries",
					displayName:'memory',
					dataProvider:memedata
				}]
			}
		}
	});
	
	setInterval(function() {
		cpudata.push({val:Math.random() * 20});
		memedata.push({val:Math.random() * 20});
		$("#spa").flashchart("option", "charts", {
			server1: {
				series:[{
					sid:"cpuseries", dataProvider:cpudata, displayName:'cpu', threshold:10
				},{
					sid:"memoryseries", dataProvider:memedata, displayName:'memory', threshold:15
				}]
			},server2: {
				series:[{
					sid:"cpuseries", dataProvider:cpudata, displayName:'cpu'
				},{
					sid:"memoryseries", dataProvider:memedata, displayName:'memory'
				}]
			},server3: {
				series:[{
					sid:"cpuseries", dataProvider:cpudata, displayName:'cpu'
				},{
					sid:"memoryseries", dataProvider:memedata, displayName:'memory'
				}]
			}
		});
	}, 1200);
});
</script>
</head>
<body style="height: 800px; overflow: scroll;">
<div style="height: 2000px;">
<div id="spa" style="height: 1000px; width:100%;float: left;">
</div>
<div id="otherinfo" style="position: absolute; left:576px; top:343px;">
	dfsfsd
</div>
</div>
</body>
</html>