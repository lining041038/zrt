<%@page import="antelope.db.DBUtil"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
fieldset label {margin-bottom: 3px;}
	 
</style>
<script>

var zTree = null;
var currprovince = null;
var currcity = null;

$(function(){
	
	readOrgInfo();
	
	$("#treecol").resize(function(){
		$("#org_tree").height($("#treecol").height()- 40);
	}).resize();
	
	$.getJSON(ctxPath + "/common/getprovinceinfos.vot", function(data){
		var probtns = "";
		$(data).each(function(){
			var guid = $.Guid.New();
			probtns += '<input type="radio" label="'+this.name+'" disabled value="'+this.sid+'" name="province" id="'+guid+'" /><label for="'+guid+'">'+this.name+'</label>';
		});
		$("#province").append(probtns);
		$("#province").buttonset();
		
		$("#province").data("buttonsetinited", true);
	});
	
	// 获取城市
	$("body").on("click","[name=province]", function(){
		readCityInfos.call(this);
	});
	
	// 保存对应关系
	$("body").on("click", "[name=province],[name=city]", function(){
		$.post(ctxPath + "/common/saveRegionRelate.vot?unitsid="+encodeURIComponent(encodeURIComponent(zTree.getSelectedNode().sid))+"&regionsid="+this.value
				+"&regionname="+encodeURIComponent(encodeURIComponent($(this).attr("label"))));
	});
	
});

function readCityInfos() {
	$.getJSON(ctxPath + "/common/getCityInProvince.vot?sid="+this.value, function(data){
		$("input,label", "#city").remove();
		var citybtns = "";
		$(data).each(function(){
			var guid = $.Guid.New();
			citybtns += '<input type="radio" label="'+this.name+'" value="'+this.sid+'" name="city" id="'+guid+'" /><label for="'+guid+'">'+this.name+'</label>';
		});
		$("#city").append(citybtns);
		if (currcity)
			$("[value="+currcity.regionsid+"]:radio").prop("checked", true);
		$("#city").buttonset();
		
		$("#city").data("buttonsetinited", true);
		
	});
}

var orgdata = null; 

function readOrgInfo() {
	zTree = $("#org_tree").unitTree({itemClick: zTreeOnClick});
}

var enabled = false;
// 树节点点击之后 
function zTreeOnClick() {

	// zTree.updateNode(zTree.getSelectedNode(), true);
	
	if (!enabled) {
		$("#procitydiv input").attr("disabled", false);
		
		if ($("#province").data("buttonsetinited")) {
			$("#province").buttonset("refresh");
		}
		
		
		if ($("#city").data("buttonsetinited")) {
			$("#city").buttonset("refresh");
		}
		
		enabled = true;
	}
	$.post(ctxPath + "/common/getCurrOrgRelateRegion.vot?unitsid="+encodeURIComponent(encodeURIComponent(zTree.getSelectedNode().sid)), function(data){
		currprovince = data.prov;
		currcity = data.city;
		var onlycity = false;
		if (currcity && !currprovince) {
			currprovince = {regionsid: currcity.regionsid.substring(0,2) + "0000"};
			onlycity = true;
		}
		
		if (currprovince) {
			var provd = $("[value="+currprovince.regionsid+"]:radio");
			provd.prop("checked", !onlycity);
			readCityInfos.call(provd[0]);
			$("#province").buttonset("refresh");
		} else {
			$("#province :radio,#city :radio").prop("checked", false);
			$("#city :radio").attr("disabled", true);
			$("#province, #city").buttonset("refresh");
		}
	}, "json");
}

function clearRelate() {
	$.post(ctx + "/common/removeRelateMapData.vot?unitsid=" + zTree.getSelectedNode().sid, function(){
		zTreeOnClick();
	});
}

function clearRelateProvince() {
	$.post(ctx + "/common/removeProvinceRelateMapData.vot?unitsid=" + zTree.getSelectedNode().sid, function(){
		zTreeOnClick();
	});
}

</script>
</head>
<body class="sm_main ui-layout">
<div class="ui-layout-west">
	<div id="treecol" class="ui-widget ui-widget-content" style="height: 100%;margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 14px;font-weight: bold;">组织机构</span>
			<span style="display: block; position: absolute; top: 5px; right: 5px;">
				<button onclick="clearRelate()" class="button ui-icon-trash" style="height: 28px;width:34px;" title="移除关联"></button>
				<button onclick="clearRelateProvince()" class="button ui-icon-closethick" style="height: 28px;width:34px;" title="移除省关联"></button>
			</span>
		</div>
		<div id="org_tree" class="tree"></div>
	</div> 
</div>
<div class="ui-layout-center">
	<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
		<div class="ui-widget-header" style="height: 30px;">
			<input style="height: 20px; margin:0px 3px 3px 3px;"/> <button onclick="createUser()" class="button ui-icon-zoomout" style="height: 28px;">查询</button>
		</div>
		<div id="procitydiv" style="padding: 4px;">
			<fieldset id="province">
				<legend>全国各省市自治区直辖市</legend>
			</fieldset>
			<hr/>
			<fieldset id="city">
				<legend>省内各个市</legend>
			</fieldset>
		</div>
	</div>
</div>
</body>
</html>



