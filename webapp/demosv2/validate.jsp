<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	$(".fileuploadspan").fileupload({
		filegroupsid: 'fdsfsdfds',
		filegroupsidinput: null, 
		autosave: true,
		maxfilesize: 51200,
		maxfilecount: 1
	});
	$(".fileuploadspan2").fileupload({
		filegroupsid: 'fdsfsdfds33',
		filegroupsidinput: null, 
		autosave: true,
		maxfilesize: 51200,
		maxfilecount: 1
	});
});
function validateit() {
	alert($("#validatediv").validate());
}
function setReadony() {
	$("body").setCurrentState("view");
}
function setWritable() {
	$("body").setCurrentState("update");
}

function resetFormdata() {
	alert("ffdsdf2");
	$("#validatediv").resetForm({"ckeditorarea":"范德萨分"});
	alert("ffdsdf3");
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="validateit()">验证</button>

<button onclick="resetFormdata()">resetForm</button>

<button onclick="setReadony()">更改只读状态</button>
<button onclick="setWritable()">更改可写状态</button>
<form id="validatediv">

下拉列表<select name="dropdownlist" dataProvider="sys_userage"  state="disabled.view:true;disabled.update:false" >
</select>

内容超长 <input maxlength2="10" required="true" required2="true" state="disabled.view:true;disabled.update:false" label="内容超长"/> </br>
只能为8位的整数 <input nbitint="8" state="disabled.view:true;disabled.update:false" label="8位整数"/> </br>
只能为整数 <input int="true" state="disabled.view:true;disabled.update:false" label="只能为整数"/> </br>
只能为数字 <input num="true" minlength='5' state="disabled.view:true;disabled.update:false" label="只能为数字"/> </br>
不能为空 <input required="true" required2="true" state="disabled.view:true;disabled.update:false" label="不能为空"/> </br>
最大值最小值 <input int="true" max="22" min="0" state="disabled.view:true;disabled.update:false" label="最大值最小值"/> </br>

email地址 <input email="true" state="disabled.view:true;disabled.update:false" label="email"/> </br>

时间前后顺序  必须在...之后  开始日期：<input name='begindate' label="开始时间" class="date" state="disabled.view:true;disabled.update:false"/> 结束日期：<input  state="disabled.view:true;disabled.update:false" label="结束时间" name="enddate" after="begindate" class="date"/></br>
时间前后顺序  不能在...之前  开始日期：<input name='begindate2' label="开始时间" class="date" state="disabled.view:true;disabled.update:false"/> 结束日期：<input  state="disabled.view:true;disabled.update:false" label="结束时间" name="enddate2" notBefore="begindate2" class="date"/></br>

<textarea class="tinymce img"></textarea>

<textarea class="heightadapt" maxlength2="30"></textarea>

<textarea class="ckeditor4" name="ckeditorarea" id="sdffsd" state="disabled.view:true;disabled.update:false" ></textarea>

<span class="fileuploadspan" required="true" state="enabled.view:false;enabled:true;" ></span>
<span class="fileuploadspan2" required="true" state="enabled.view:false;enabled:true;"></span>

<iframe src="${ctx}/demos/validatenestediframe.jsp?nestedbyform=true" width="500" height="400"></iframe>
</form>
</body>
</html>