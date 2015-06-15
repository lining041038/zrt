<%@page import="antelope.entities.SysOptItem"%>
<%@page import="antelope.utils.JSONObject"%>
<%@page import="antelope.utils.JSONArray"%>
<%@page import="java.util.List"%>
<%@page import="antelope.springmvc.JPABaseDao"%>
<%@page import="antelope.springmvc.BaseComponent" %>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.db.DBUtil"%>
<%@page import="antelope.services.*"%>
<%@page import="antelope.entities.SysUser" %>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.web.min.jsp"/>
<title>涿州中小企业服务平台</title>
</head>
<style type="text/css">
</style>
<script type="text/javascript">
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
	};
	/* function querycontent(){
		 alert($("#query").serialize());
		alert(encodeURI($("#query").serialize()));
		alert(encodeURIComponent($("#query").serialize())) 
	    window.open(ctxPath+"/zzcpportal/sed2.jsp?"+encodeURI($("#query").serialize()));
	} */ 
	
	function AddFavorite(sURL, sTitle){ 
		try{ 
		window.external.addFavorite(sURL, sTitle); 
		}catch (e){ 
			try{ 
			window.sidebar.addPanel(sTitle, sURL, ""); 
			}catch (e) 
			{ 
				alert("加入收藏失败，请使用Ctrl+D进行添加"); 
			} 
		} 
	} 

	function SetHome(obj,vrl){ 
		try{ 
			obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl); 
		}catch(e){ 
			if(window.netscape){ 
				try { 
				netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect"); 
				}catch (e) { 
					alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。"); 
				} 
				var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch); 
				prefs.setCharPref('browser.startup.homepage',vrl); 
			} 
		} 
	}

	/**
	 * url对应的是幻灯片管理类，通过幻灯片管理类生成幻灯片对象，
	 * 前台在通过幻灯片对象的path向后台请求图片信息，但是首页显示不出图片,能显示图片的标题。
	 * 在getFile的打印信息里能够打印file.filedata是：[B@138a3b8 这种格式。
	 * author zll                                    
	 */
	$(function(){
		//alert("--str---");
		//getzznews();
		/*
		$("#flashpic").flashslider("destroy").flashslider({
			dataProvider: ctx + "/zzcp/cms/ArticlePptAddController/getSliderDatas.vot",
			 width:365,
			 height:275 
		});*/
		//这个实现鼠标滑动弹出层
	/*	$('.demo1').Tabs();*/
	}); 
	 
	/*  function getzznews(){
		 $.post(ctxPath+"/zzcp/cms/IndexDataController/getNewsTitle.vot",function(data){
		
			 var  finalhtml="";
		     for(i=0;i<5;i++){
		    	/*  finalhtml + ="<div>sssssssssssss</div>"
				// finalhtml+="<a class='lists' href='"+/zzcp+"/zzcpportal/content.jsp?catesid=1366332631453&selectedcatesid=1366332632313&sid=11"+data.get(i).sid+"'></a>";
		    	 alert(finalhtml); */
		   /*   }
		 },"json"); 
		 $("#zznews").html(finalhtml); 
	 }  */ 
	 /**
	 * 通过post可以访问getFile和getSliderDatas，返回的是幻灯片对象
	 * （其中path：/zzcp/zzcp/cms/ArticlePptAddController/getSliderDatas.vot?imgSid="XXX"）
	 * author zll
			 $(function(){
				alert("str===getSliderDatas");
			 	$.post(ctx+"/zzcp/cms/ArticlePptAddController/getFile.vot?imgsid='1366358904754'", function(data){
					alert(data);
				});
			 });
 	*/

	/* $(function() { $(".ui-tabs-nav> li > a")onmouseover(function(e) { if 
		 (e.target == this) { var tabs = $(this).parent().parent().children("li"); var panels =  
		 $(this).parent().parent().parent().children(".ui-tabs-panel"); var index = $.inArray(this, tabs); if 
		 (panels.eq(index)[0]) { tabs.removeClass("ui-tabs-selected") .eq(index).addClass("ui-tabs-selected"); 
		 panels.addClass("ui-tabs-hide") .eq(index).removeClass("ui-tabs-hide"); } } }); });
	
	 */
	 function registercompany(){
		 location = ctxPath + "/zzcpportal/companyregister.jsp";
	 }
	 
	// 加载登录状态页面
	$(function(){
		$.get(ctx + "/zzcpportal/logoninfo.jsp", function(data){
		/*alert(data);*/ 
			$(".logininfodiv").prepend(data);
		});         
	});
</script>
<body  style="overflow: auto;">
	<div class="mainqiye">
    	<div class="top">
            	<span class="l">欢迎来到涿州中小企业服务平台!</span>
                <div class="r logininfodiv">
                    <span class="l">|</span>
                    <a href="#" class="l " onclick="$.registeruser();">个人注册</a>
                    <a href="#" class="l " onclick="registercompany()">企业注册</a>
                    <a href="#" class="l" onclick="AddFavorite(window.location,document.title)">加入收藏 </a>
                </div>
                <div class="clear"></div>
        </div>
    	<div class="header">
    		<div class="logoimg1" class="l"></div>
    		<div class="logoimg2 r banner" class="l"></div>
            <div class="clear"></div>
            <div class="center navdiv">
                <a href="/zzcp/" class="first">网站首页</a>
                 <a href="/zzcp/published/1366332631453.html">新闻中心</a>
               	 <a href="/zzcp/published/ep1366332631460.html">企业之窗</a> 
                 <a href="/zzcp/published/ep1366332631465.html">地区.园区</a> 
                 <a href="/zzcp/published/ep1366332631472.html">特色产业</a> 
                 <a href="/zzcp/published/pd1366332631477.html">产品推介</a>
                 <a href="/zzcp/published/1366332631490.html">办事指南</a> 
                 <a href="/zzcp/published/1366332631497.html">服务中心</a> 
            </div>
            <div class="list">
            	<p><span class="ico0"></span><a href="/zzcp/published/1366332631502.html">信息化</a></p>
                <p><span class="ico1"></span><a href="/zzcp/published/1366332631507.html">科技创新</a></p>
                <p><span class="ico2"></span><a href="/zzcp/published/1366332631515.html">投资融资</a></p>
                <p><span class="ico8"></span><a href="/zzcp/published/1366332631548.html">创业辅导</a></p>
                <p><span class="ico3"></span><a href="/zzcp/published/1366332631522.html">企业管理</a></p>
                <p><span class="ico4"></span><a href="/zzcp/published/1366332631528.html">采购销售</a></p>
                <p><span class="ico9"></span><a href="/zzcp/published/1366332631533.html">法律服务</a></p>
                <p><span class="ico6"></span><a href="/zzcp/published/1366332631538.html">行业协会</a></p>
                <p><span class="ico7"></span><a href="/zzcp/zzcpportal/sed.jsp?catesid=1366332631497&selectedcatesid=1366787759525">下载中心</a></p>
            </div>
            <div class="clear"></div>
        </div>
        
        <div class="news l">
            <div  class="l img1" id="flashpic"></div>
            <div class="block l">
                <p class="name ">新闻中心</p>
                <p style="border-bottom:solid 2px #e3e3e3; width:335px"></p>
                 
					 <div>  <a class="lists" href="/zzcp/published/1385456260303.html">&bull;&nbsp;我市组织开展清洗城市灰尘集中行动</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1385456260283.html">&bull;&nbsp;涿州市委召开六届三十九次常委（扩大）会议</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1384854998320.html">&bull;&nbsp;涿州市召开七届人大常委会第十五次会议</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1384854998299.html">&bull;&nbsp;涿州市“洗城净天”行动工作方案（摘要）</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1383286827265.html">&bull;&nbsp;涿州市委召开六届第38次常委（扩大）会议</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1383286827242.html">&bull;&nbsp;涿州召开大气污染防治工作推进会</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1382514832749.html">&bull;&nbsp;涿召开市政府党组会议暨七届十一次政府常务会议</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1382514832727.html">&bull;&nbsp;涿州市委召开六届第37次常委会议</a></div>
					 <div>  <a class="lists" href="/zzcp/published/1381916860606.html">&bull;&nbsp;涿州市城市管理6S行动助推城市清洁</a></div>
               
            </div>
            <div class="column l jishu">
                <div class="title">
                	<div class="zbg4div"></div><span class="l b ">企业动态</span>
                    <a href="/zzcp/published/1366332632324.html" class="r b">MORE+</a>
                </div>
                <div class="c">
					  <a class="lists" href="/zzcp/published/1380006857216.html">&bull;&nbsp;三博再次获得“河北省质量效益型先进企业”称号</a>
					  <a class="lists" href="/zzcp/published/1380006857132.html">&bull;&nbsp;三博为哥伦比亚制造挂篮整装待发</a>
                </div>
            </div>
            <div class="column l jishu" style="margin-right:0">
                <div class="title">
                    <div class="zbg4div"></div><span class="l b ">产品推介</span>
                    <a href="/zzcp/published/pd1366332632339.html" class="r b">MORE+</a>
                </div>
                <div class="c">
                </div>
            </div>
            
        </div>
        <div class="column r tongz">
        	<div class="title">
            <div class="zbg4div"></div><span class="l b ">重要通知</span>
  			<a href="/zzcp/published/1366332632307.html" class="r b">MORE+</a></div>
            <div class="clear"></div>
            <div class="c">
					  <a class="lists" href="/zzcp/published/1383286827600.html">&bull;&nbsp;企业用户使用企业网站说明书</a>
					  <a class="lists" href="/zzcp/published/1379164493025.html">&bull;&nbsp;企业用户使用说明</a>
            	
            </div>
        </div>
        <div class="column l tongz2  "  >
        	<div class="title">
            <div class="zbg4div"></div><span class="l b ">企业推荐 </span>
            <a href="/zzcp/published/ep1366332632333.html" class="r b">MORE+</a>
            </div>
            <div class="clear"></div>
            <div class="c" >
            	 
					  <a class="lists" href="/zzcp/published/ep1384228910511.html">&bull;&nbsp;涿州北方重工设备设计有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1381571285033.html">&bull;&nbsp;涿州市旺达广告有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1381375219869.html">&bull;&nbsp;涿州蓝天网架有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1379843272869.html">&bull;&nbsp;涿州市宇阳包装制品有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1379390179668.html">&bull;&nbsp;涿州市三博桥梁模板制造有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1378347463691.html">&bull;&nbsp;涿州市凯诺计算机职业培训学校</a>
					  <a class="lists" href="/zzcp/published/ep1375944775350.html">&bull;&nbsp;河北凌云工业集团有限公司</a>
             </div>   
        </div>
        <div class="clear"></div>
         <div class="column r tongz" style="height:35px;width:998px;float:left;margin-bottom:0px">
        	<div class="title">
            <div class="zbg4div"></div><span class="l b ">专项资金</span></div>
        </div>
        <ul class="uptop" style='text-align:center;margin-top:5px;'>
        	<li><a href="http://smezj.sme.gov.cn/index.action" target='blank'>
        		</a></li>
        	<li><a href="http://121.28.134.30:8088/zxzjxt/login.jsp"  target='blank'>
        		</a></li>
        	<li><a href="#">
        		</a></li>
        	<li><a href="http://hbgx.ii.gov.cn/"  target='blank'>
        		</a></li>
        </ul>
        <div class="column r tongz" style="height:35px;width:997px;float:left;margin-bottom:0px">
        	<div class="title">
            <div class="zbg4div"></div><span class="l b ">上榜企业</span></div>
        </div>
        <ul class="uptop" style='margin-top:5px;height:86px;' >
        	 
				 <li> <a href="/zzcp/published/ep1375944775350.html"></a></li>
				 <li> <a href="/zzcp/published/ep1378347463691.html"></a></li>
				 <li> <a href="/zzcp/published/ep1379390179668.html"></a></li>
				 <li> <a href="/zzcp/published/ep1379843272869.html"></a></li>
				 <li> <a href="/zzcp/published/ep1381375219869.html"></a></li>
				 <li> <a href="/zzcp/published/ep1381571285033.html"></a></li>
				 <li> <a href="/zzcp/published/ep1384228910511.html"></a></li>
        	
            <li class="clear"></li>
        </ul>
        <div class="clear"></div>
        
        <div class="column l shifan">
            <div class="title">
                <div class="zbg4div"></div><span class="l b ">企业之窗 </span>
                <a href="/zzcp/published/ep1366332631460.html" class="r b">MORE+</a>
            </div>
            <div class="c">
					  <a class="lists" href="/zzcp/published/ep1384228910511.html">&bull;&nbsp;涿州北方重工设备设计有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1381375219869.html">&bull;&nbsp;涿州蓝天网架有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1381571285033.html">&bull;&nbsp;涿州市旺达广告有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1380006858542.html">&bull;&nbsp;涿州明城恒盛集团</a>
					  <a class="lists" href="/zzcp/published/ep1380102853782.html">&bull;&nbsp;涿州市立明装订厂</a>
					  <a class="lists" href="/zzcp/published/ep1379843272869.html">&bull;&nbsp;涿州市宇阳包装制品有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1379390179668.html">&bull;&nbsp;涿州市三博桥梁模板制造有限公司</a>
					  <a class="lists" href="/zzcp/published/ep1379473779948.html">&bull;&nbsp;涿州市双翼金属结构有限责任公司</a>
            </div>
        </div>
        <div class="column l shifan zhiyuan">
            <div class="title">
                <div class="zbg4div"></div><span class="l b ">行业.企业</span>
               	<a href="/zzcp/published/ep1366332632328.html" class="r b">MORE+</a>
            </div>
            <div class="c">
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337182.html" class="l2">信&nbsp;息&nbsp;&nbsp;技&nbsp;&nbsp;术</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337170.html" class="l2">制&nbsp;&nbsp;造&nbsp;&nbsp;业</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337194.html" class="l2">金&nbsp;&nbsp;融</a><br/>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337178.html" class="l2">物&nbsp;流&nbsp;&nbsp;行&nbsp;&nbsp;业</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366468786971.html" class="l2">农林牧渔</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337203.html" class="l2">教&nbsp;&nbsp;育</a><br/>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337186.html" class="l2">批&nbsp;发&nbsp;&nbsp;零&nbsp;&nbsp;售</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337174.html" class="l2">建&nbsp;&nbsp;筑&nbsp;&nbsp;业</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337211.html" class="l2">卫&nbsp;&nbsp;生</a><br/>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337213.html" class="l2">居&nbsp;民&nbsp;&nbsp;服&nbsp;&nbsp;务</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337198.html" class="l2">房&nbsp;&nbsp;地&nbsp;&nbsp;产</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337215.html" class="l2">娱&nbsp;&nbsp;乐</a><br/>
                <a style="margin-left:25px" href="/zzcp/published/ep1366785448961.html" class="l2">律师&nbsp;事务所</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337190.html" class="l2">住宿餐饮</a>
                <a style="margin-left:25px" href="/zzcp/published/ep1366764337217.html" class="l2">其&nbsp;&nbsp;他</a><br/>
                <a style="margin-left:25px" href="/zzcp/published/ep1366785448982.html" class="l2">会计师事务所</a>
            </div>
        </div>
        <div class="column l shifan zhiyuan" style="margin-right:0">
            <div class="title">
                <div class="zbg4div"></div><span class="l b ">地区.园区</span>
                <a href="/zzcp/published/ep1366332631465.html" class="r b">MORE+</a>
            </div>
            <div class="c">
				<a style="margin-left:12px;" href="/zzcp/published/ep1366332631810.html" class="l2">双&nbsp;塔&nbsp;办&nbsp;事&nbsp;处</a>
            	<a style="margin-left:20px;" href="/zzcp/published/ep1366332631816.html" class="l2">高&nbsp;官&nbsp;庄&nbsp;镇</a>
            	<a style="margin-left:12px;" href="/zzcp/published/ep1366332631814.html" class="l2">码&nbsp;&nbsp;头&nbsp;&nbsp;镇</a><br/>
                <a style="margin-left:12px;" href="/zzcp/published/ep1366332631808.html" class="l2">桃&nbsp;园&nbsp;办&nbsp;事&nbsp;处</a>
            	<a style="margin-left:20px;" href="/zzcp/published/ep1366332631818.html" class="l2">东&nbsp;仙&nbsp;坡&nbsp;镇</a>
            	<a style="margin-left:12px;" href="/zzcp/published/ep1366332631822.html" class="l2">林&nbsp;&nbsp;屯&nbsp;&nbsp;乡</a><br/>
                <a style="margin-left:12px;" href="/zzcp/published/ep1366332631812.html" class="l2">清凉寺办事处</a>
            	<a style="margin-left:20px;" href="/zzcp/published/ep1366332631820.html" class="l2">百&nbsp;尺&nbsp;竿&nbsp;镇</a>
            	<a style="margin-left:12px;" href="/zzcp/published/ep1366332631826.html" class="l2">豆&nbsp;&nbsp;庄&nbsp;&nbsp;乡</a><br/>
            	<a style="margin-left:12px;"  href="/zzcp/published/ep1366332631804.html" class="l2">涿&nbsp;州&nbsp;开&nbsp;发&nbsp;区</a>
            	<a style="margin-left:20px;"  href="/zzcp/published/ep1366332631832.html" class="l2">松&nbsp;林&nbsp;店&nbsp;镇</a>
            	<a style="margin-left:12px;" href="/zzcp/published/ep1366332631830.html" class="l2">刁&nbsp;&nbsp;窝&nbsp;&nbsp;乡</a><br/>
            	<a style="margin-left:12px;" href="/zzcp/published/ep1366332631802.html" class="l2">新兴产业示范区</a>
            	<a style="margin-left:8px;" href="/zzcp/published/ep1366332631828.html" class="l2">义&nbsp;和&nbsp;庄&nbsp;乡</a>
                <a style="margin-left:12px;" href="/zzcp/published/ep1366332631824.html" class="l2">孙家庄乡</a>
                <a style="margin-left:12px;" href="/zzcp/published/ep1366332631806.html" class="l2">松林店工业园区</a>
            </div>
        </div>
        
        <div class="column l shifan" >
            <div class="title">
                <div class="zbg4div"></div><span class="l b ">信息化</span>
                <a href="/zzcp/published/ep1366787759560.html" class="r b">MORE+</a>
            </div>
            <div class="c">
					  <a class="lists" href="/zzcp/published/ep1380006858542.html">&bull;&nbsp;涿州明城恒盛集团</a>
					  <a class="lists" href="/zzcp/published/ep1378347463691.html">&bull;&nbsp;涿州市凯诺计算机职业培训学校</a>
            </div>
        </div>
         <div class="column l shifan" >
            <div class="title">
                <div class="zbg4div"></div><span class="l b ">科技创新 </span>
                <a href="/zzcp/published/1366332631507.html" class="r b">MORE+</a>
            </div>
            <div class="c">
					  <a class="lists" href="/zzcp/published/1381571284073.html">&bull;&nbsp;涿州市国家知识产权试点城市通过验收</a>
					  <a class="lists" href="/zzcp/published/1378202995562.html">&bull;&nbsp;什么是知识产权？</a>
					  <a class="lists" href="/zzcp/published/1378202994995.html">&bull;&nbsp;专利技术转让</a>
					  <a class="lists" href="/zzcp/published/1377831780525.html">&bull;&nbsp;中华人民共和国专利法（2008修正）</a>
					  <a class="lists" href="/zzcp/published/1377831780496.html">&bull;&nbsp;中华人民共和国专利法实施细则((2010修订)</a>
					  <a class="lists" href="/zzcp/published/1377831780321.html">&bull;&nbsp;专用集成电路与系统国家重点实验室成功研制出半浮栅晶体管器件</a>
					  <a class="lists" href="/zzcp/published/1377831780287.html">&bull;&nbsp;日本利用皮肤测定人体生物钟可用于缓解失眠症状</a>
					  <a class="lists" href="/zzcp/published/1376277716945.html">&bull;&nbsp;中国北方农业科技成果博览会在内蒙古赤峰市开幕</a>
            </div>
        </div>
         <div class="column l shifan" style="margin-right:0">
            <div class="title">
                <div class="zbg4div"></div><span class="l b">投资融资</span>
               	<a href="/zzcp/published/1366332631515.html" class="r b">MORE+</a>
            </div>
            <div class="c">
					  <a class="lists" href="/zzcp/published/1376465035886.html">&bull;&nbsp;企业筹资的风险防范</a>
					  <a class="lists" href="/zzcp/published/1376465035822.html">&bull;&nbsp;金融租赁谋求新起点 拓宽中小企业融资渠道</a>
					  <a class="lists" href="/zzcp/published/1376465035790.html">&bull;&nbsp;工信部：适当降低中小企业贷款担保收费</a>
					  <a class="lists" href="/zzcp/published/1376465035758.html">&bull;&nbsp;我国已发布金融行业国家标准41项</a>
					  <a class="lists" href="/zzcp/published/1376465035726.html">&bull;&nbsp;工商总局:外企注册资本三千万元可冠名"中国"</a>
					  <a class="lists" href="/zzcp/published/1376465035694.html">&bull;&nbsp;关于印发《河北省省级中小企业信用担保资金管理办法》的通知</a>
					  <a class="lists" href="/zzcp/published/1376381960221.html">&bull;&nbsp;拓展小微企业直接融资渠道</a>
            	
            </div>
        </div>
   
        
           <div class="column l shifan zhiyuan2">
            <div class="title">
                <div class="zbg4div"></div><span class="l b">特色产业 </span>
               <a href="/zzcp/published/ep1366332631472.html" class="r b">MORE+</a>
            </div>
            <div class="c">
            <!-- <a href="#" class="l">家电数码</a>
                <a href="#" class="l">机械设备</a>
                <a href="#" class="l">商务服务</a>
                <a href="#" class="l">陶瓷</a>
                <a href="#" class="l">日用百货</a>
                <a href="#" class="l">高科技</a> -->
                
					  <a href="/zzcp/published/ep1366787759489.html"  class="l">装备制造</a>
					  <a href="/zzcp/published/ep1366787759494.html"  class="l">新型建材</a>
					  <a href="/zzcp/published/ep1366787759498.html"  class="l">包装印刷</a>
					  <a href="/zzcp/published/ep1366787759502.html"  class="l">冶金材料</a>
					  <a href="/zzcp/published/ep1366787759506.html"  class="l">电子信息</a>
					  <a href="/zzcp/published/ep1366787759510.html"  class="l">汽车零部件</a>
                
            </div>
        </div>
        <div class="column l shifan">
            <div class="title">
                <div class="zbg4div"></div><span class="l b">创业辅导</span>
               	<a href="/zzcp/published/1366332631548.html" class="r b">MORE+</a>
               
            </div>
            <div class="c">
					  <a class="lists" href="/zzcp/published/1376465036504.html">&bull;&nbsp;马云教你没钱如何创业</a>
					  <a class="lists" href="/zzcp/published/1376465036454.html">&bull;&nbsp;给有志创业的白领工作者的10大建议</a>
					  <a class="lists" href="/zzcp/published/1376465036388.html">&bull;&nbsp;创业前必要做七大细节规划</a>
					  <a class="lists" href="/zzcp/published/1376465036315.html">&bull;&nbsp;创业必读：发人深省的30句话</a>
					  <a class="lists" href="/zzcp/published/1376381960401.html">&bull;&nbsp;想创业就要保持好奇心</a>
					  <a class="lists" href="/zzcp/published/1376381960372.html">&bull;&nbsp;2013年大学生创业优惠政策</a>
					  <a class="lists" href="/zzcp/published/1376381960120.html">&bull;&nbsp;管理者如何提高整个团队的执行力</a>
					  <a class="lists" href="/zzcp/published/1376381960077.html">&bull;&nbsp;企业管理经营之道：在于六个字</a>
            </div>
        </div>
        <div class="column l shifan" style="margin-right:0">
            <div class="title">
                <div class="zbg4div"></div><span class="l b">企业招聘 </span>
                <a href="/zzcp/zzcpportal/sed.jsp?catesid=1000000000000&selectedcatesid=1000000000000" class="r b" class="r b">MORE+</a>
             </div>
               <div class="c">
                </div>
        </div>
        <div class="column l shifan">
            <div class="title">
                <div class="zbg4div"></div><span class="l b">采购销售 </span>
                <a href="/zzcp/published/1366332631528.html" class="r b">MORE+</a>
            </div>
            <div class="c">
                <!-- <a href="#" class="l">家电数码</a>
                <a href="#" class="l">日用百货</a>
                <a href="#" class="l">高科技</a> -->
            </div>
        </div>
        <div class="column l shifan " >
            <div class="title">
                <div class="zbg4div"></div><span class="l b">法律咨询</span>
                <a  href="/zzcp/published/1366332631533.html" class="r b">MORE+</a>
            </div>
            <div class="c">
            	 
					  <a class="lists" href="/zzcp/published/1376465038681.html">&bull;&nbsp;中华人民共和国循环经济促进法（主席令第四号）</a>
					  <a class="lists" href="/zzcp/published/1376465038652.html">&bull;&nbsp;中华人民共和国专利法（主席令第八号）</a>
					  <a class="lists" href="/zzcp/published/1376465038622.html">&bull;&nbsp;中华人民共和国就业促进法（主席令第七十号）</a>
					  <a class="lists" href="/zzcp/published/1376465038575.html">&bull;&nbsp;中华人民共和国中小企业促进法（主席令第六十九号）</a>
					  <a class="lists" href="/zzcp/published/1376465038495.html">&bull;&nbsp;中华人民共和国统计法（主席令第十五号）</a>
					  <a class="lists" href="/zzcp/published/1376465038459.html">&bull;&nbsp;中华人民共和国侵权责任法（主席令第二十一号）</a>
					  <a class="lists" href="/zzcp/published/1376465038413.html">&bull;&nbsp;关于修改《中华人民共和国劳动合同法》的决定（主席令第七十三号）</a>
					  <a class="lists" href="/zzcp/published/1376465038374.html">&bull;&nbsp;中华人民共和国可再生能源法（主席令第二十三号）</a>
            	
            	<!-- <p class="l"><a href="#">&bull;&nbsp;遵化市</a></p>
                <p class="l"><a href="#">&bull;&nbsp;迁安市</a></p>
                <p class="l"><a href="#">&bull;&nbsp;丰润区</a></p>
                <p class="l"><a href="#">&bull;&nbsp;乐亭县</a></p> -->
            </div>	
        </div>
        <div class="column l shifan" style="margin-right:0">
            <div class="title">
                <div class="zbg4div"></div><span class="l b">下载中心</span>
                <a href="/zzcp/published/1366787759525.html" class="r b">MORE+</a>
            </div>
            <div class="c">
					  <a class="lists" href="/zzcp/published/1376465037601.html">&bull;&nbsp;产业结构调整指导目录（2011年本）</a>
					  <a class="lists" href="/zzcp/published/1376465037565.html">&bull;&nbsp;“十二五”节能环保产业发展规划</a>
					  <a class="lists" href="/zzcp/published/1376465037260.html">&bull;&nbsp;高耗能落后机电设备（产品）淘汰目录(第二批)</a>
					  <a class="lists" href="/zzcp/published/1376465037120.html">&bull;&nbsp;河北省人民政府关于加快推进工业企业技术改造工作的实施意见</a>
					  <a class="lists" href="/zzcp/published/1376465036996.html">&bull;&nbsp;河北省人民政府《关于进一步加快民营经济发展的意见》</a>
					  <a class="lists" href="/zzcp/published/1376465036913.html">&bull;&nbsp;河北省政府《关于支持工业发展提高经济效益的十条意见》</a>
					  <a class="lists" href="/zzcp/published/1376465036832.html">&bull;&nbsp;关于鼓励和促进成长型企业发展的意见</a>
            	
            </div>
        </div>
        <div class="clear"></div>
        <footer>
        	<div class="box demo1">
				<ul class="tab_menu">
					<li class="current"><a >装备制造</a></li>
					<li><a>新型建材</a></li>
					<li><a>包装印刷</a></li>
					<li><a>冶金材料</a></li>
					<li><a>电子信息</a></li>
					<li><a>汽车零部件</a></li>
					<li><a>其他链接</a></li>
				</ul>
			<div class="tab_box">
				<div>
		            <ul >
						  <a href="/zzcp/published/ep1384228910511.html">&bull;&nbsp;涿州北方重工设备设计有限公司</a>
						  <a href="/zzcp/published/ep1376277716330.html">&bull;&nbsp;保定天银机械有限公司</a>
						  <a href="/zzcp/published/ep1375944775350.html">&bull;&nbsp;河北凌云工业集团有限公司</a>
		            </ul>
	            </div>
	            <div class="hide">
	            	<ul>
						  <a href="/zzcp/published/ep1381375219869.html">&bull;&nbsp;涿州蓝天网架有限公司</a>
	                </ul>
	               <!--  <div class="clear"></div> -->
	            </div>
	            <div  class="hide">
	            	<ul>
						  <a href="/zzcp/published/ep1379843272869.html">&bull;&nbsp;涿州市宇阳包装制品有限公司</a>
            		</ul>
	              <!--   <div class="clear"></div> -->
	            </div>
	            <div  class="hide">
	            	<ul>
	            	</ul>
	                <!-- <div class="clear"></div> -->
	            </div>
	            <div class=" hide">
	            	<ul>
						  <a href="/zzcp/published/ep1378347463691.html">&bull;&nbsp;涿州市凯诺计算机职业培训学校</a>
	                </ul>
	            </div>
	            <div class=" hide">
	            	<ul>
	            	</ul>
	            </div>
	            <div class=" hide">
	            	<ul>
	            	</ul>
	            </div>
	            </div>
            </div>
            
            <div class="center font12">
            <a href="/zzcp/pages/questionnaire/allques.jsp">问卷调查</a><span>|</span><a href="/zzcp/pages/message/allmsg.jsp">公众留言</a><span>|</span><a href='#'>关于本站</a><span>|</span><a href='#'>网站地图</a><span>|</span><a href='#'>联系我们</a><span>|</span><a href='#'>法律声明</a><br/>2012-2013涿州中小企业服务平台 <span class="">冀ICP备05005185-2号  版权所有 复制必究</span>
            </div>
        </footer>
    </div>
</body>
</html>
