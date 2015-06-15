<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	var mysid = "dfsfdsdsffsd";
	$(".fileupload2").fileupload({
		autosave: true,
		enablePermanent: false,
		checklogon: true,
		readDocOnline: true,
		filegroupsid: mysid,
		filegroupsidinput: $("[name=myfilegroupsid]")
	}).initWidgets();
	
	
	$(".fileupload3").fileupload({
		autosave: true,
		enablePermanent: true,
		filegroupsid: mysid,
		readDocOnline: true, // 文档阅读开关 
		showDescription:true,
		filegroupsidinput: $("[name=myfilegroupsid]")
	}).initWidgets();
});

function permanentfiles() {
	if ($("#form1").validate()) {
		$("#form1").submitForm({
			url:ctx + "/demo/setFilePermanent.vot"
		});
	}
}


function reloadFileUpDownlaoder() {
	$(".fileupload2").fileupload("reloadFileUpDownloader");
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<form id="form1">
<div class="fileupload2" required="true" style="width: 100px;float: left;"></div>
<button onclick="permanentfiles()">持久化</button>
<button onclick="reloadFileUpDownlaoder()" style="width:700px;">重新加载按钮（在IE下，若包含有flash的dom节点曾经被去掉过有可能出现无法上传的问题，需要通过它来重新加载按钮）</button>
<input name="myfilegroupsid" value="dfsfdsdsffsd"/>

		<div>fdsaafdas3232322</div>
		<div class="fileupload3" required="true" style="width: 300px;float: left;"></div>

</form>
</body>
</html>