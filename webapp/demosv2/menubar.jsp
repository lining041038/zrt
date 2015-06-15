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
	$("#MenuBar1").menubar({});
});
</script>
<body>
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


