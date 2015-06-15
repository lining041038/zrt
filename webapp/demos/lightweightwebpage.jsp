<%@ page language="java" pageEncoding="utf-8"%><jsp:include page="/include/header2.web.min.jsp"/>
<script>
</script>
</head>
<body>
<h1>此页面为轻量web界面，不包含管理端众多代码 jsp头引用时使用如下的代码</h1>
<%="&lt;jsp:include page=\"/include/header2.web.min.jsp\"/&gt;" %>

<h1>多浏览器兼容性css书写最佳实践为当css能够兼容多浏览器则直接写兼容性css否则在不同的css文件中书写修补css</h1>

<div>
经检测，区分IE版本可见性css测试规则如下
</div>
<br/>
<div class="compatiblediv" >
	<div class="ie7">xxx-ie7.css 文件 ie7及其以下版本浏览器可见</div>
	<div class="ie8">xxx-ie8.css ie8-9可见</div>
	<div class="ie9">xxx-ie9.css ie9可见</div>
	<div style="display: block;">若上方未出现可见性div检测，则为IE10及其以上版本</div>
</div>
<br/>

<div>
开发时以Chromium v28+（2013-05-06发布;Chrome及其他基于webkit引擎的浏览器通用内核）以及当前机器上安装的IE浏览器版本进行开发测试，其余IE浏览器版本若存在问题则进行独立css修补
</div>

<div>
测试时注意，若要测试IE兼容性，通过调整高版本IE开发工具中的文档模式不能达到测试目的  <br/>
（因为例如使用IE10浏览器调整了IE文档模式到IE7 xxx-ie7.css修补css依然不可见，即IE会按照最终IE版本来控制css可见性）
必须使用IE兼容性测试工具如IETester，或者直接使用对应版本的浏览器进行测试。
</div>
</body>
</html>