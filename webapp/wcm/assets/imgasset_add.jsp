<%--
	title:图片资源添加页面
	author:lining
--%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.interfaces.components.supportclasses.SelectionFilter"%>
<%@page import="antelope.interfaces.components.supportclasses.TreeListSelectionOptions"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

var assetsid = '<%=request.getParameter("assetsid")%>';

var operatetype = '<%=request.getParameter("operatetype")%>';

var filegpsid = null;
$(function(){
	filegpsid = $.getNewsid();
	
	appendfileupload();
	
	if (operatetype == 'update' || operatetype == 'view')
		previewOldImg();
});

function getAssetDataSymbol() {
	return filegpsid;
}

function previewOldImg() {
	$("#previewimg").attr("src", ctx + "/assets/imgasset/ImgAssetController/getImageData.vot?sid=" + assetsid)
	.show();
}

function setPreviewImg() {
	if ($(".assetfile").fileupload("getFileInfos").length) {
		$("#previewimg").attr("src", ctx + "/upload/UploadController/getSingleImageData.vot?imagesid=" + $(".assetfile").fileupload("getFileInfos")[0].sid)
		.show();
	} else {
		setTimeout(setPreviewImg, 500);
	}
}

function appendfileupload() {
	$(".assetfile").fileupload("destroy").fileupload({
		autosave: true,
		filegroupsid: filegpsid,
		maxfilecount: 1,
		extension: "*.png",
		autosave: true,
		uploadComplete: function() {
			setPreviewImg();
		}
	}).initWidgets();
}

</script>
</head>
<body>
	<div class="assetfile"></div>
	<div>预览：</div>
	<img id="previewimg" style="display: none; border: 1px solid black; margin-left: 15px; margin-top: 20px;" width="470" height="350"/>
</body>
</html>