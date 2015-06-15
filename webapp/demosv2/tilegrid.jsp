<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

$(function(){
	$("#validatediv").tilegrid({
		dataProvider:ctxPath + "/demos/tilegrid/TileGridController/getTileGridPageItem.vot",
		numPerPage: 10,
		tileRenderer: function(gridinfo) {
			
			return '<div onclick="imgclick(\'' + this.sid +  '\')" style="width:200; height: 157px; float:left; padding-top: 30px; ">第' + gridinfo.currPage + '页\
				<img src="' + (ctx + '/demos/assets/' + this.imgpath) + '"/>\
				<div style="font-size:14px; padding-top:10px;">' + this.name + '</div>\
				<div style="font-size:12px; padding-top:10px; color:gray;">' + this.formkey + '</div>\
				<div style="font-size:12px; padding-top:10px; color:gray;"><span style="color:red; font-size:15px;">8.2</span>分</div>\
				</div>';
		}
	});
});

function imgclick(sid) {
	alert(sid);
}

</script>
</head>
<body style="overflow: auto; padding: 5px; width: 830px;">
<div id="validatediv" style="width: 800px; margin-left: 13px;"></div>
<div id="tracevaldiv"></div>
</body>
</html>