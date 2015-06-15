<%--
 基本请求响应演示
 @author lining
 @since 2012-09-23
--%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<link rel="stylesheet" href="demosupports/SpryMenuBarHorizontal.css">
<script>
$(function() {
	alert(document.getElementById('mymfcactivex1').GetLastElement);
	alert(document.getElementById('mymfcactivex1').GetLastElement([]));
});
</script>
<body>
<!--  0x7FC9E0B4, 0x60BB, 0x4757, { 0xBA, 0xA3, 0x4F, 0x7F, 0x70, 0xA, 0xE2, 0x2A } -->
<OBJECT ID="mymfcactivex1" WIDTH=100 HEIGHT=51
 CLASSID="CLSID:7FC9E0B4-60BB-4757-BAA3-4F7F700AE22A" codebase="mymfcactivex.ocx">
    <PARAM NAME="_Version" VALUE="65536">
    <PARAM NAME="_ExtentX" VALUE="2646">
    <PARAM NAME="_ExtentY" VALUE="1323">
    <PARAM NAME="_StockProps" VALUE="0">
</OBJECT>

<ul id="MenuBar1" class="MenuBarHorizontal">
  <li><a class="MenuBarItemSubmenu" href="#">Item 1</a>
    <ul>
      <li><a href="#">Item 1.1</a></li>
      <li><a href="#">Item 1.2</a></li>
      <li><a href="#">Item 1.3</a></li>
    </ul>
  </li>
  <li><a href="#">Item 2</a></li>
  <li>
  	<a class="MenuBarItemSubmenu" href="#">教务在线教务在</a>
    <ul>
      <li><a class="MenuBarItemSubmenu" href="#">Item 3.1</a>
        <ul>
          <li><a href="#">Item 3.1.1</a></li>
          <li><a href="#">Item 3.1.2</a>
          	    <ul>
			      <li><a class="MenuBarItemSubmenu" href="#">Item 3.1</a>
			        <ul>
			          <li><a href="#">Item 3.1.1</a></li>
			          <li><a href="#">Item 3.1.2</a></li>
			        </ul>
			      </li>
			      <li><a href="#">教务在线教务在线教务在线教务在线教务在线教务在线教务在线教务在线教务在线教务在线</a></li>
			      <li><a href="#">Item 3.3</a></li>
			    </ul>
          </li>
        </ul>
      </li>
      <li><a href="#">教务在线教务在线教务在线教务在</a>
         <ul>
          <li><a href="#">Item 3.1.1</a></li>
          <li><a href="#">Item 3.1.2</a>
         	  <ul>
		      <li><a class="MenuBarItemSubmenu" href="#">Item 3.1</a>
		        <ul>
		          <li><a href="#">Item 3.1.1</a></li>
		          <li><a href="#">Item 3.1.2</a>
		          	<ul>
				      <li><a class="MenuBarItemSubmenu" href="#">Item 3.1</a>
				        <ul>
				          <li><a href="#">Item 3.1.1</a></li>
				          <li><a href="#">Item 3.1.2</a></li>
				        </ul>
				      </li>
				      <li><a href="#">教务在线教务</a></li>
				      <li><a href="#">Item 3.3</a></li>
				    </ul>
		          </li>
		          <li><a href="#">Item 3.1.2</a>
		          	<ul>
				      <li><a class="MenuBarItemSubmenu" href="#">Item 3.1</a>
				        <ul>
				          <li><a href="#">Item 3.1.1</a></li>
				          <li><a href="#">Item 3.1.2</a></li>
				        </ul>
				      </li>
				      <li><a href="#">教务在线教务</a></li>
				      <li><a href="#">Item 3.3</a></li>
				    </ul>
		          </li>
		        </ul>
		      </li>
		      <li><a href="#">教务在线教务在线教务在线教务在线教务在线教务在</a></li>
		      <li><a href="#">Item 3.3</a></li>
		    </ul>
          </li>
        </ul>
      </li>
      <li><a href="#">Item 3.3</a>
       <ul>
          <li><a href="#">Item 3.1.1</a></li>
          <li><a href="#">Item 3.1.2</a>
          	    <ul>
			      <li><a class="MenuBarItemSubmenu" href="#">Item 3.1</a>
			        <ul>
			          <li><a href="#">Item 3.1.1</a></li>
			          <li><a href="#">Item 3.1.2</a></li>
			        </ul>
			      </li>
			      <li><a href="#">教务在线教务在线教务在线教务在线教务在线教务在线教务在线教务在线教务在线教务在线</a></li>
			      <li><a href="#">Item 3.3</a></li>
			    </ul>
          </li>
        </ul>
      </li>
    </ul>
  </li>
  <li><a href="#">Item 4</a></li>
</ul>
</body>
</html>


