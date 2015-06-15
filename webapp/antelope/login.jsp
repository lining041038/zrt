<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	$("body").onlyTestFlashVersion();
});

var userClicked = false;
function tologin() {
	var thisform=document.forms[0];
	//将用户名转为小写，这是CIP特殊要求
	var username=thisform.username.value;
    if(username && $.trim(username)){
	  thisform.username.value=username.toLowerCase();
	}
	if(thisform.username.value=="") {	
		thisform.username.focus();
		alert("请您输入用户名");
	} else if(thisform.password.value=="") {	
		thisform.password.focus();
		alert("请您输入密码");
	} else if(!userClicked){
	    thisform.submit();
	    userClicked = true;
	}
}

function enterkey(e){
    if (e == 13) {
        tologin();
    }
}

</script>
</head>
<body class="ui2-body loginbody" onkeydown="enterkey(event.keyCode||event.which)">
<form target="_top" action="<%=request.getContextPath() %>/LoginAction.vot" method="post">
	<div class="login png">
		<div class="inf">
	    	<div class="p">
	        	<span class="l">用户名：</span>
	            <input name="username" type="text" class="text l" value="" size="22"/>
	            <div class="clear"></div>
	        </div>
	        <div class="p">
	        	<span class="l">密&nbsp;&nbsp;码：</span>
	            <input name="password" type="password" class="text l" value="" size="23" style="width: 161px;"/>
	            <div class="clear"></div>
	        </div>
	        <div class="p">
	        	<span class="l">&nbsp;</span>
	        	<input type="button" onclick="tologin()" class="buttonlogin" value="登录"/>
	            <input type="reset" class="buttonlogin" value="重置"/>
	            <div class="clear"></div>
	        </div>
	    </div>
	</div>
</form>
</body>
</html>