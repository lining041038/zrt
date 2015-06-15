<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	$(".fileupload3").loadFileUpDownloader ({wmode:'transparent',  extension:".mp4", resumeEnabled: true, append:true, htmlText: "附件上传"}, function(){
		//alert("dfsfds");
		var thisbtnobj = this;
		
		this.selectFile (function (filelist) {
			alert("fdfdfddf");
			
			thisbtnobj.progress (function (info) {
				$("#processinfodiv").text(JSON.stringify(info));
			}).uploadComplete (function (info) {
				alert("上传完成！");
			});
			
			$("#processinfodiv").text(JSON.stringify(filelist));
		});
		
		
	});
});


//异步回调函数过程
function callfun(i)
{
    alert("callback function in js, ret=" + i);
}


function uploadit () {
	
	$(".fileupload3").upload("192.168.1.128");
	
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<form id="form1">
<div class="fileupload2" required="true" style="width: 100px;float: left;"></div>
<button onclick="uploadit()" type="button">上传</button>
<input name="myfilegroupsid" value="dfsfdsdsffsd"/>

<div class="fileupload3" required="true" style="width: 300px;float: left; height:300px;">
<!-- 
	 <object id="fileuploadobj" name="fileuploadobj" classid="clsid:9796CB88-9F99-4A0F-B4A7-F5D25D21133E" codebase="<%=request.getContextPath() %>/ocx/FileUpload3.ocx" width="100" height="100">
      </object>
 -->      
</div>

<div id="processinfodiv"></div>

</form>
</body>
</html>