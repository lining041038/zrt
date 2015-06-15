<%@ page language="java" pageEncoding="utf-8"%>

<script>
function myrenderer() {
	return '<div onclick="imgclick(\'' + this.sid +  '\')" style="width:150; height: 157px; float:left; padding-top: 30px; padding-left:25px; padding-right:25px;">\
	<img src="' + (ctx + '/demos/assets/' + this.imgpath) + '"/>\
	<div style="font-size:14px; padding-top:10px;">' + this.name + '</div>\
	<div style="font-size:12px; padding-top:10px; color:gray;">' + this.formkey + '</div>\
	<div style="font-size:12px; padding-top:10px; color:gray;"><span style="color:red; font-size:15px;">8.2</span>分</div>\
	</div>';
}

function imgclick(sid) {
	alert(sid);
}

</script>