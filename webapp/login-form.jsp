<%@page import="antelope.utils.SystemOpts"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.min.jsp"/>
<title>antelope 登录</title>
<style>
.loginstyle {
	font-family: "宋体";
	font-size: 14px;
}
.datacell {
	font-family: "宋体";
	font-size: 9pt;	
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-repeat: repeat-x;
}
</style>
<script language="JavaScript" type="text/JavaScript">

$(function(){
	$("body").onlyTestFlashVersion();
});

<!--
	var titlebar_height=30;
	var border_width=4;
	var availHeight=screen.availHeight + titlebar_height;
	var availWidth=screen.availWidth + 2 * border_width;    
	window.resizeTo(availWidth,availHeight);
	window.moveTo(-border_width,-titlebar_height);
if (window != top) {
	top.location = "<%=request.getContextPath()%>/login-form.jsp";
}
	
var  focusobj= null;
var userClicked = false;
	
	//将登陆页面最大化
	//self.moveTo(0,0);
	//self.resizeTo(screen.availWidth,screen.availHeight);
	//parent.moveTo(0,0);
	//parent.resizeTo(screen.availWidth,screen.availHeight);


function  mouseoverinput( inputobj)
{
inputobj.className = "logininputfocus";
}
function  mouseoutinput( inputobj)
{
if ( inputobj != focusobj)
inputobj.className = "logininput";
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function setCookie(cookieName,cvalue,expiredays,path)
{
	var expireDate=new Date();
	var expireStr="";
	if(expiredays!=null) {
		expireDate.setTime(expireDate.getTime()+(expiredays*24*3600*1000));
		expireStr="; expires="+expireDate.toGMTString();
	}
	pathStr=(path==null)?"; path=/":"; path="+path;
	document.cookie=cookieName+'='+escape(cvalue)+expireStr+pathStr;
}

function getCookie(cookieName)
{
	var index=-1;
 	if(document.cookie) 
 		index=document.cookie.indexOf(cookieName);
 	if(index==-1) {
 		return "";
 	} else {
 	     var iBegin = (document.cookie.indexOf("=", index) +1);
          var iEnd =document.cookie.indexOf(";", index);
          if (iEnd == -1)
          {
              iEnd = document.cookie.length;
          }
          return unescape(document.cookie.substring(iBegin,iEnd));
	}
}

function setFormFocus() {
   var cookie = getCookie('myusername');
    if (cookie != null)
        document.forms[0].username.value=cookie;
    var pwd=getCookie('pwd');
    if(pwd!='')
    {
        document.forms[0].password.value=pwd;
        document.forms[0].savePWD.checked=true;
    }
	document.forms[0].username.focus(); 
	document.forms[0].username.select();
}

function tologin()
{
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
	}	else if(!userClicked){
	    setCookie("myusername",thisform.username.value,10);
	    
	    if (thisform.savePWD.checked) {
	         setCookie("pwd",thisform.password.value,10);
	   }else
		{
		  setCookie("pwd","",-10);
		}
	   
		if(thisform.useCertificate != null && thisform.useCertificate.checked){
			
			if(getDefaultCertificate(thisform.username.value)==null){
			  alert("没有证书，请检查用户名");
			  return false;
			}else{
			  thisform.password.value=signAndEnvelopData(getDefaultCertificate(thisform.username.value),thisform.password.value);  
			}
			
			/*
			if(getDefaultCertificate("cjb")==null){
			  alert("没有证书，请检查用户名");
			  return false;
			}else{
			  thisform.username.value=signAndEnvelopData(getDefaultCertificate("cjb"),thisform.password.value);  
			}
			*/
		}
	    thisform.submit();
	    userClicked = true;
	}
	
}
function setListSize() {
   var cookie = getCookie('myListSize');
   if (cookie != null && cookie.length > 0) {
       document.write("<input type=hidden name='myListSize' value='" + cookie+ "'>");
   }
}

function minIEOCX(){	
	ctlRunIa.MinIEOCX();
}

function enterkey(e){
    if (e==13)
    {
        tologin(); 
    }
}

function register() {
	$.registeruser();
}

</script>


</head>
<body onload="setFormFocus()" bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onkeydown="enterkey(event.keyCode||event.which)">
    <%  
    	out.println("<form target=\"_top\" action=\"" + request.getContextPath() + "/LoginAction.vot\" method=\"post\">");
    %>	
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="100%" align="center" valign="top"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="100%" align="center"  style="background-position:center top; background-repeat:no-repeat;"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="30%">&nbsp;</td>
          </tr>
          <tr>
            <td height="70%" align="center" valign="top"><table width="443" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td></td>
              </tr>
              <tr>
                <td height="139" align="center" ><table width="303" height="92" border="0" cellpadding="0" cellspacing="5" style="font-size:12px;">
          <tr>
            <td width="80" align="right">用户名</td>
            <td width="150" align="left"><input type="text" name="username" style="width:110px; height:20px; border:1px solid #CCCCCC;" /></td>
            <td align="center">&nbsp;</td>
          </tr>
          <tr>
            <td align="right">密&nbsp;&nbsp;码</td>
            <td align="left"><input type="password" name="password" style="width:110px; height:20px; border:1px solid #CCCCCC;" /></td>
            <td align="center">&nbsp;</td>
          </tr>
          <%if ("true".equals(SystemOpts.getProperty("useverifycode"))) { %>
            <tr>
            <td align="right">验证码</td>
            <td align="left"><input name="verifycode" style="width:110px; height:20px; border:1px solid #CCCCCC;" /></td>
            <td align="center">
            <img src="<%=request.getContextPath() %>/common/SystemController/getVerifycodeImage.vot"/>
            </td>
          </tr>
          <%} %>
         	<%if (request.getAttribute("loginerror") != null) { %>
         	<tr>
           <td align="center" colspan="4"><%=request.getAttribute("loginerror")%></td>
         </tr>
         	<%} %>
          <tr>
            <td>&nbsp;</td>
            <td align="left"><input type="checkbox" name="savePWD" value="1" onClick="if(this.checked) this.checked=confirm('保存密码将可能使您的数据不安全。您是否还想保留密码？')"/>
														 记住密码</td>
			<td align="left" valign="top"><input type="submit" value="登录"/><input type="button" onclick="register()" value="${i18n['antelope.register'] }"/></td>
          </tr>
        </table></td>
              </tr>
            </table></td>
          </tr>
        </table>
          </td>
      </tr>
      
    </table>
    </td>
  </tr>
  <tr>
    <td height="26" align="center" style="font-size:12px; color:#006699;">版权</td>
  </tr>
</table>
</form>
</body>
</html>
