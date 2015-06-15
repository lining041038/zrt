<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

$(function(){
	$("#validatediv").tilegrid({
		dataProvider:ctxPath + "/demos/tilegrid/TileGridController/getTileGridPageItem.vot",
		tileRenderer: function() {
			return '<div onclick="imgclick(\'' + this.sid +  '\')" style="width:100%; padding-top: 5px;">\
				<div style="padding-top:0px; color:#000000;">' + '[' + this.sid + ']' + this.name + '</div>\
				<div style="font-size:12px; padding-top:10px; color:#924f00;">' + this.formkey + '</div>\
				<div style="border-top: 1px dashed #a8a8a8; width:802px; margin-top:15px;"></div>\
				</div>';
		}
	});
});

function imgclick(sid) {
	alert(sid);
}

</script>
</head>
<body style="overflow: auto; padding: 5px; width: 830px; ">
<div id="validatediv" style="width: 800px; margin-left: 13px;"></div>
<div id="tracevaldiv"></div>
</body>
</html>