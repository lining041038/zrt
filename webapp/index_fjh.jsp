<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="antelope.services.SessionService"%>
<%@ page language="java" pageEncoding="utf-8"%>
<% request.setAttribute("componentVersion", "v2"); %>
<jsp:include page="/include/header2.jsp"/>
<%
SessionService service = (SessionService) session.getAttribute("service");
String bln = "1";
if("基金会领导".equals(service.getDeptname()) || "秘书长助理".equals(service.getDeptname())){
	bln = "0";
}

%>
<title>时光漫步</title>
<style>
.con-1{border-bottom: 1px solid #fff;}
.unchoiced{border-bottom: 1px solid #ddd;border-top: 1px solid #bbb;background-color: rgb(236, 236, 236);}
#daiban-top3 table input{box-sizing:border-box;}
.mynotice{background-image: url("themes/fjh/images/forfooter/tongzhi-1.png");}
.have-notice,.have-daiban{background-image: url("themes/fjh/images/forfooter/tongzhi-2.png");}
</style>
<script>
var blndept = '<%=bln%>'; 
$(function(){
	// IE版本判断
	if ($.browser.msie && parseInt($.browser.version) < 8) {
		$("#downnewbrs").dialog({width:700, height:300, modal:true, title:"请点击下面的链接下载最新版IE浏览器！"})
	}
	$(window).resize(function(){
		$("#iframeautodiv").width($(window).width() - 217);
		$(".right-top").width($(window).width() - 217);
		$("#iframeautodiv").height($(window).height() - 99);
		$(".main .left").height($(window).height() - 59);
		$(".main .left_").height($(window).height() - 59);
		$("body").height($(window).height());
	});  
	
	
	$("#mynotice").hover(function(){
		$("#mynotice-div").show();
	},function(){
		$("#mynotice-div").hide();
		$("#mynotice-div").hover(function(){
			$("#mynotice-div").show();
		},function(){
			$("#mynotice-div").hide();
		})
	})
	
	daiban();
	
	//通知点击后标记已读
	$("#mynotice-div #notice-top3").delegate("a","click",function(){
		 $.getJSON(ctxPath + "/fjh/generalaffairs/mynotice/MyNoticeController/noticeMarkIndex.vot?sid="+$(this).parent().attr("sid"), function(data){
			 tongzhi();
		 });
	});
	
	$.getJSON(ctxPath + "/getMenuInfo.vot", function(data){
		
		// 一级主菜单 
		$(data).each(function(){
			$(".nav").append($("<p ><a href='#'>"+this.title+"</a></p>").data("menu", this));
		});
		
		// 二级子菜单
		$(".nav p").hover(function(){
			var self = $(this); 
			if (!self.hasClass("selected")) 
				self.addClass("menu_over");
		}, function(){
			$(this).removeClass("menu_over");
		}).click(function(){
			
			var self = $(this);
			self.siblings().removeClass("selected");
			self.removeClass("menu_over").addClass("selected");
			$( ".menu_inner .accordion" ).hide();
			
			var submenu_accordion = self.data("submenu_accordion");
			
			$(".navs").empty();
			
			submenu_accordion = $("<div class='accordion' style='clear:both;'></div>");
			submenu_accordion.data("mainmenu", self);
			
			// 建立子菜单
			var menuLevel1Title = self.data("menu").title;
			$($(this).data("menu").submenu).each(function(index){
				
				if(this.title == "项目列表"){
					if("1" == blndept){
						var submenu_ = this.submenu
						$.getJSON(ctx + "/projectAll/ProjectInfoListController/listProject.vot",function(data){
							$(data).each(function(index){
								$('<div class="li"> \
						                <div class="head menu2title">\
						                   <div class="l zyimg4"></div>\
						                   <span class="l" style="width: 140px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">' + this.projectname + '</span>\
						                   <div class="zyimg6 r"></div>\
						                   <div class="clear"></div>\
						                </div>' + creatSubmenu2(submenu_, this.projectname,this.projectcode,this.projectname) + '\
						            </div>').appendTo(submenu_accordion);
								
								
								function creatSubmenu2(submenus, menuLevel2Title,projectcode,proname) {
									var submenudivs = "";
									
										$(submenus).each(function(index){
											var smifirststr = ""; 
											if (index == 0) {
												smifirststr = "smifirst";
											}
											var menuLevel3Title = this.title;                                                                                                                                                       
											submenudivs+='<div onclick="setProject(this)" projectname="'+proname+'" title3="'+menuLevel3Title+'" title2="'+menuLevel2Title+'" title1="'+menuLevel1Title+'" class="li2 ' + smifirststr + '" target="' + (this.target||'') + '" path="'+this.path+'&projectcode='+projectcode+'&projectname='+encodeURI(encodeURI(proname))+'&gridprovidersuffix='+encodeURIComponent( ' +\'&procode='+ encodeURIComponent(projectcode)+'&proname='+encodeURIComponent(proname)+'\'')+'"><div class="lev3-img" style="background-image:url('+ctx+'/themes/fjh/'+this.img+');"></div><a href="#" >'+this.title+'</a></div>';
										});
									
									return submenudivs;
								}
								
							})
						})
					}
				}else if(this.title == "部门项目"){
					if("0" == blndept){
						$.ajaxSettings.async = false;
						$('<div class="li"> \
				                <div class="head menu2title">\
				                   <div class="l zyimg4"></div>\
				                   <span class="l" style="width: 140px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">' + this.title + '</span>\
				                   <div class="zyimg6 r"></div>\
				                   <div class="clear"></div>\
				                </div>' + creatSubmenu2(this.submenu,this.title) + '\
				            </div>').appendTo(submenu_accordion);
						
						function creatSubmenu2(submenu,menuLevel2Title) {
							var submenudivs = "";
							$.getJSON(ctx + "/projectAll/ProjectInfoListController/deptManage.vot",function(data){
								$(data).each(function(){
										var smifirststr = ""; 
										if (index == 0) {
											smifirststr = "smifirst";
										}
										submenudivs+='<div onclick="setdept(this)" deptname = "'+this.name+'" deptsid="'+this.sid+'" title3="'+this.name+'" title2="'+menuLevel2Title+'" class="li2 ' + smifirststr + '"  target="' + (this.target||'') + '" ><div class="lev3-img" ></div><a href="#" >'+this.name+'</a></div>';
								});
								
							})
							return submenudivs;
						}
					
					}
					
				}else{
					$('<div class="li"> \
			                <div class="head menu2title">\
			                   <div class="l zyimg4"></div>\
			                   <span class="l">' + this.title + '</span>\
			                   <div class="zyimg6 r"></div>\
			                   <div class="clear"></div>\
			                </div>' + creatSubmenu(this.submenu, this.title) + '\
			            </div>').appendTo(submenu_accordion);
					
					
					function creatSubmenu(submenus, menuLevel2Title) {
						var submenudivs = "";
						
							$(submenus).each(function(index){
								var smifirststr = ""; 
								if (index == 0) {
									smifirststr = "smifirst";
								}
								var menuLevel3Title = this.title;
								submenudivs+='<div title3="'+menuLevel3Title+'" title2="'+menuLevel2Title+'" title1="'+menuLevel1Title+'" class="li2 ' + smifirststr + '" target="' + (this.target||'') + '" path="'+this.path+'"><div class="lev3-img" style="background-image:url('+ctx+'/themes/fjh/'+this.img+');"></div><a href="#" >'+this.title+'</a></div>';
							});
						
						return submenudivs;
					}
				}
			});
			
			$(".navs").append(submenu_accordion);
			
			self.data("submenu_accordion", submenu_accordion);
			
			// 子菜单效果
			$(".navs").on("click", ".menu2title", function(){
				$(".li2").hide();
				$(this).siblings().show();
				$(".zyimg6").removeClass("zyimg6s");
				$(this).find(".zyimg6").addClass("zyimg6s");
			});
			
			// 显示左侧子菜单
			$(".left_menu").show();
			$(".sys_main").removeClass("fullscreen");
			
			// 显示具体模块功能div 隐藏门户div
			$("#module_div").show();
			$("#portal_div").hide();
			
			$(".smifirst:eq(0)", submenu_accordion).click();
			$(".head:eq(0)", submenu_accordion).click();
			
		}).first().click();
		
	});
	
	// 三级菜单添加转到模块功能
	$(".navs").on("click",".li2", function(){
		if("部门项目" == $(this).attr("title2")){
			$(".left_").show();
			$(".left").hide();
		}else{
			$(".left").show();
			$(".left_").hide();
			if ($(this).attr("target") == '_blank') {
				open(ctxPath + "/"+ $(this).attr("path"));
				return;
			}
			
			$(".li2").removeClass("selected");
			$(this).closest(".accordion").data("mainmenu").data("currmenu", this);
			$(this).addClass("selected");
			
			$("#mainfuncifr").attr("src", ctxPath + $(this).attr("path") + "&autoheightifrmid=mainfuncifr");
			$("#smb_title1").text($(this).attr("title1"));
			$("#smb_title2").text($(this).attr("title1"));
			$("#smb_title3").text($(this).attr("title3"));
			$(".smb_toright").show();
			// 显示具体模块功能div 隐藏门户div
			$("#module_div").show();
			$("#portal_div").hide();
			$(".sys_main").removeClass("fullscreen");
		}
		
		
	});
	
	$(".navs_").on("click",".li2", function(){
		if ($(this).attr("target") == '_blank') {
			open(ctxPath + "/"+ $(this).attr("path"));
			return;
		}
		
		$(".li2").removeClass("selected");
		$(this).closest(".accordion").data("mainmenu").data("currmenu", this);
		$(this).addClass("selected");
		
		$("#mainfuncifr").attr("src", ctxPath + $(this).attr("path") + "&autoheightifrmid=mainfuncifr");
		$("#smb_title1").text($(this).attr("title1"));
		$("#smb_title2").text($(this).attr("title1"));
		$("#smb_title3").text($(this).attr("title3"));
		$(".smb_toright").show();
		// 显示具体模块功能div 隐藏门户div
		$("#module_div").show();
		$("#portal_div").hide();
		$(".sys_main").removeClass("fullscreen");

	});
	
	
	$(window).resize(function(){
		if (isfullscreen) {
			$(".mid_container").height("100%");
		} else
			$(".mid_container").height($(window).height() - $(".sys_banner").height()- $(".sys_menu").height());
		$(".sm_main").height($(".btm").height() - $(".sm_banner").height());
	}).resize();
	
	leftmenuwidth = $(".left_menu").width();
	
	reportPosTop = $("#reportPos").css("top");
	
	// 读取页面右上角弹出信息模版
	$("<div/>").load(ctxPath+'/popout_notice_tmpl.jsp').appendTo("body");
	
	/*
	$.subscribeRoutedMessages({
		dest:"notice",
		beforeAjax:function() {
			if (!isshowing)
				return true;
		},
		afterAjax:function(data) {
			var popdiv = $("#popout_notic_div");
			popdiv.setTemplateElement(data.tmplid).processTemplate(data.data);
			
			$("a", popdiv).each(function(){
				var self = $(this);
				var href = self.attr("href");
				self.attr("clickhref", href).attr("href","#");
			})
			popdiv.animate({marginTop:0}, 1000).delay(20000).animate({marginTop:-40}, 1000, function(){
				isshowing = false;
			});
			isshowing = true;
		}
	});*/
	
	// 更改链接动作
	$("body").on("click", "#popout_notic_div a", function(){
		var thisObj = this;
		var topifram = $.dialogTopIframe({
			url: $(thisObj).attr("clickhref"),
			buttons:{
				'关闭':function(){
					top.$(topifram).dialog("destroy");
				}
			}, width:$(window).width() - 50, height:$(window).height() - 50, title:$(thisObj).attr("title"), modal:true
		});
	});
});

var isfullscreen = false;
var leftmenuwidth;
var reportPosTop;
var isshowing = false;

function fullscreen() {
	isfullscreen = !isfullscreen;
	$(".left_menu,.index_top").toggle();
	if (isfullscreen) {
		$("#fullbtn").text("退出全屏");
		$("#reportPos").css("top", 1);
	} else {
		$("#fullbtn").text("全屏");
		$("#reportPos").css("top", reportPosTop);
	}
	$(".sys_main").toggleClass("fullscreen");
	
	$(window).resize();
}

// 打开首页门户
function open_Portal() {
	
	// 显示门户div 隐藏具体模块div
	$("#module_div").hide();
	$("#portal_div").show();
	$("#portalifr").attr("src", "portal.jsp");
	$(".left_menu").hide();
	$(".sys_main").addClass("fullscreen");
	
	// 取消主菜单选中状态
	$(".main_menu_li").removeClass("menu_sel");
}

// 关闭左侧子菜单
function closeLeftMenu() {
	$(".menu_inner").toggle();
	$(".left_menu").width($(".left_menu").width() == leftmenuwidth ? $(".lm_divider").width() : leftmenuwidth );
	$(".divider_hander").toggleClass("divider_close");
	$(".sys_main").toggleClass("leftmenuclose");
}

function setProject(thisobj){
	 $.post(ctx+"/fjh/avtivepro/activeprocessinfo/proactive/ProjectActiveProcessController/setSession.vot?projectname="+encodeURI(encodeURI(thisobj.projectname)),function(data){
	})
}


function change() {
	 $("[name='old_password']").val('');
	 $("[name='new_password']").val('');
	 $("[name='new_password_too']").val('');
	$('#new').dialog( {
        width:545,
        height:235,
        modal:true,
        title:"修改密码",
        buttons:[
		         { text:"确认",  click:function(){
		        	 if(!$("#new").validate()){    
		        		 return;		
						}else{
		        	 if($("[name='new_password']").val() == $("[name='new_password_too']").val()){
		        		 $.post(ctxPath+"/SysUserManagerController/UserRoleOrgController/changepassword.vot?"+ encodeURI($("#new").serialize()),function(data) {
	        			 if( "" == data){
			     			 alert("旧密码输入错误，请重新输入！");
			     			 $("[name='old_password']").val('');
			     			 $("[name='new_password']").val('');
				    		 $("[name='new_password_too']").val('');
			     		 }else{
			     			 alert("修改成功")
			     			 $('#new').dialog("close");
			     		 }		        		    		
		    	     });			             
		    	     }else{
		    		 alert("两次输入的新密码不一致，请重新填写！");
		    		 $("[name='new_password']").val('');
		    		 $("[name='new_password_too']").val('');
		    	     }}
		        	}
		         },
		         { text:"取消" , click:function(){$(this).dialog("close");} 
		            }
		        ]
					           
	}); 
	
}
function setdept(thisobj){
	$.getJSON(ctx + "/projectAll/ProjectInfoListController/deptProject.vot?deptsid="+thisobj.deptsid,function(data){
		var self = $(this);
		self.siblings().removeClass("selected");
		self.removeClass("menu_over").addClass("selected");
		$( ".menu_inner .accordion" ).hide();
		
		var submenu_accordion = self.data("submenu_accordion");
		
		$(".navs_").empty();
		
		submenu_accordion = $("<div class='accordion' style='clear:both;'></div>");
		submenu_accordion.data("mainmenu", self);
		
		// 建立子菜单
		$(data).each(function(){
			$('<div class="li"> \
	                <div class="head menu2title"  path = "/fjh/basicinfo/processproject/projectprocessbasicinfoBusiFormInfo.jsp?prosid='+this.sid+'">\
	                   <div class="l zyimg4"></div>\
	                   <span class="l">' + this.projectname + '</span>\
	                   <div class="zyimg6 r"></div>\
	                   <div class="clear"></div>\
	                </div>'+creatSubmenu(this)+'\
	                </div>').appendTo(submenu_accordion);
			
			function creatSubmenu(proinfo) {
			
				var submenudivs = "";
				$.getJSON(ctxPath + "/getMenuInfo.vot", function(data){
					var vv = data[0].submenu[2].submenu;
					  $(vv).each(function(index){
					 	var smifirststr = ""; 
						if (index == 0) {
							smifirststr = "smifirst";
						}
						var menuLevel3Title = this.title;
						submenudivs+='<div onclick="setProject(this)" projectname="'+proinfo.projectname+'" title3="'+menuLevel3Title+'"  class="li2 ' + smifirststr + '" target="' + (this.target||'') + '" path="'+this.path+'&projectname='+encodeURI(encodeURI(proinfo.projectname))+'&gridprovidersuffix='+encodeURIComponent( ' +\'&proname='+encodeURIComponent(proinfo.projectname)+'\'')+'"><div class="lev3-img" style="background-image:url('+ctx+'/themes/fjh/'+this.img+');"></div><a href="#" >'+this.title+'</a></div>';
						
					  }) 
				});
				return submenudivs;
			} 
		});
		
		$(".navs_").append(submenu_accordion);
		
		self.data("submenu_accordion", submenu_accordion);
		
		// 子菜单效果
		$(".navs_").on("click", ".menu2title", function(){
			$("#mainfuncifr").attr("src", ctxPath + $(this).attr("path") + "&autoheightifrmid=mainfuncifr");
			$(".li2").hide();
			$(this).siblings().show();
			$(".zyimg6").removeClass("zyimg6s");
			$(this).find(".zyimg6").addClass("zyimg6s");
		});
		
		// 显示左侧子菜单
		$(".left_menu").show();
		$(".sys_main").removeClass("fullscreen");
		
		// 显示具体模块功能div 隐藏门户div
		$("#module_div").show();
		$("#portal_div").hide();
		
		/* $(".smifirst:eq(0)", submenu_accordion).click(); */
		$(".head:eq(0)", submenu_accordion).click();
		
	})
}
function returntitle(thisobj){
	$(".left").show();
	$(".left_").hide();
	$(".navs .menu2title").hover(function(){
		var self = $(this); 
		if (!self.hasClass("selected")) 
			self.addClass("menu_over");
	}, function(){
		$(this).removeClass("menu_over");
	}).click(function(){
	}).last().click();
	$("#mainfuncifr").attr("src", "");
}

function showhide(num){
	if("1"==num){
		$("#daiban-top3").show();
		$("#notice-top3").hide();
		$(".con-1").removeClass("unchoiced");
		$("#notice-button").addClass("unchoiced");
	}else{
		$("#daiban-top3").hide();
		$("#notice-top3").show();
		$(".con-1").removeClass("unchoiced");
		$("#daiban-button").addClass("unchoiced");
	}
}

//页面加载待办事件
function showTodoList() {
	$("#daiban-top3").datagrid({
		showNumperpage:false,
		pagebuttonnum:2,
		visibleHeader:true,
		numPerPage:3,
		showSeqNum:true,
		columns: {
			/* deptName:{width:'25%', headerText:"部门名称"},*/
			moduleName:{width:'20%', headerText:"模块名称"}, 
			title: "待办业务标题"
		},
		buttons:{
			release: {
				toolTip:"处理待办",
				click: function() {
					var thisObj = this;
					if(thisObj.url.indexOf(".ui",0)>0){
						var ifram2 = $.dialogTopIframe({
							url: ctxPath + thisObj.url,
							title:thisObj.moduleName
						});
					}else{
						var ifram = $.dialogTopIframe({
								url: ctxPath + thisObj.url,
								width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:thisObj.moduleName,
								buttons:{
									'提交': function() {
										
										ifram.find("iframe")[0].contentWindow._submittasks_(daiban);
										//ifram.find("iframe")[0].contentWindow._submittasks_(readTodoList);
									}, '取消': function() {
										ifram.dialog("destroy");
									}
								}
					});
   				}
			}
		}
		}
	});
}



function readTodoList() {
	$.getJSON(ctxPath + "/getLogonUserAllTodoList.vot", function(data){
		len=data.length;
		$("#daiban-top3-no").remove();
		if(len > 0){
			$("#mynotice").addClass("have-daiban");//css("background-image",'url("themes/fjh/images/forfooter/tongzhi-2.png")');  
		}else{
			$('<span id="daiban-top3-no" style="margin-left:15px;">暂时没有待办!</span>').prependTo("#daiban-top3");
			$("#mynotice").removeClass("have-daiban");
		}
		
		$("#daiban-top3").datagrid("option", "dataProvider", data);
	});
}

//待办
function daiban() {
	readTodoList();  //读取待办信息 
	showTodoList();  //页面加载代办事件 
	daibanyuenum();
	tongzhi();
}

setInterval(daiban, 60000); 

//待办
function daibanyuenum() {
	$.getJSON(ctxPath + "/getLogonUserAllTodoListmore.vot", function(data){
		$("#daiban-num").text(data.length);
	});
}

function tongzhi(){
	//加载通知
	$.getJSON(ctxPath + "/fjh/generalaffairs/mynotice/MyNoticeController/showNotice.vot", function(data){
		if(data.length > 99){
			$("#notice-num").text("99+");
		}else{
			$("#notice-num").text(data.length);
		}
		$("#notice-top3").empty();
		if(data.length > 0){
			$("#mynotice").addClass("have-notice");//css("background-image",'url("themes/fjh/images/forfooter/tongzhi-2.png")');  
			$(data).each(function(i,notice){
				if(i>=3) return;
				$("<div style='float:left;width: 120px;padding:10px 0 10px 0;border-top: 1px solid rgb(200,200,200);text-align: center;'>"+this.noticetype+"</div><div sid='"+this.sid+"' style='width: 257px;float:left;padding:10px 0 10px 20px;border-top: 1px solid rgb(200,200,200);border-left: 1px dotted rgb(200,200,200);'>"+this.content+"</div>").appendTo("#notice-top3");
			})
		}else{
			$("#mynotice").removeClass("have-notice");
			$('<span style="margin-left:15px;">暂时没有新通知!</span>').appendTo("#notice-top3");
		}
	})
}
</script>
</head>
<body class="ui2-body ui2-main-body">
<div class="header" style="min-width: 1300px;">
	<div class="logo l png"></div>
	<%-- <div class="welcomearea">欢迎<%=service.getUser() %>登陆妇女基金会管理系统  今天是<% 

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
	out.print(sdf.format(new Date()));
	
	
	%></div>
    <div onclick="location = '${pageContext.request.contextPath}/logoff.jsp'" class="close r png" style="cursor: pointer;">
    	消息(+5)   反馈意见   个人设置   帮助     注销
    </div> --%>
    <div class="nav png">
    <div style="clear:left;"></div>
	
	</div>
</div>
<div class="main2" style="height: 0;">
	<div style="position: relative; height: 20px; visibility: hidden;">
		<div style="margin:0; position: absolute; left: 0;">
			<div class="l crumbs"></div>
		    <a href="#" id="smb_title1" style="color: white !important;"></a> > 
		    <a href="#" id="smb_title2" style="color: white !important;"></a> > 
		    <a href="#" id="smb_title3" style="color: white !important;"></a>
	    </div>
    </div>
    <div class="clear"></div>
</div>
<div class="main" style="margin: 0 auto; margin-top: -3px;min-width: 1300px;">
 	<div class="clear"></div>
	<div class="left l" style="overflow-y:auto;overflow-x: hidden; ">
        <div class="navs">
        </div>
    </div>
    <div class="left_ l_"style="overflow-y:auto;overflow-x: hidden;float: left;display: none">
        <div class="navsreturn">
       		 <div class="li">
                <div class="head menu2title">
                   <div class="l zyimg41"></div>
                   <span class="l"><a href="#" onclick="returntitle(this)">返回</a></span>
                   <div class="zyimg6 r"></div>
                   <div class="clear"></div>
                </div>
            </div>
        </div>
        <div class="navs_">
        </div>
    </div>
   <div class="right-top" style="min-width: 1083px;">
   		<div class="username"><span><%=service.getUser()%></span></div>
    	<div id="footer">
    			<a id="help" href="#" title="帮助" class="help"></a>
    			<a id="mynotice" href="#" title="通知" class="mynotice"></a>
    			<a id="password" href="javascript:change();" title="修改密码" class="changepassword"></a>
	        	<a id="zhuxiao" href="${pageContext.request.contextPath}/logoff.jsp" title="退出" class="exit"></a>
        </div>
        <div id="mynotice-div" style="display: none;position: absolute; right: 90px;top: 37px;width: 400px; height:auto;z-index: 999;border-bottom: 1px solid #bbb;background-color: white;">
        	<div style="height: 3px;background-color: rgb(236, 236, 236);"></div>
        	<div style="height: 28px;border-left: 1px solid #bbb;border-right: 1px solid #bbb;text-align: center;">
        		<div id="notice-button" class="con-1" onclick="showhide(2)" style="width: 199px;height: 25px;float: left;display: block;cursor: pointer;">
        			<div style="margin-top: 3px;">
        				<div style="float: left;margin-left: 70px;">通知</div><div id="notice-num" style="color: white;float: left;width: 20px;height: 15px;margin-left: 10px;margin-top: 2px;background: url('<%=request.getContextPath() %>/themes/fjh/images/forfooter/pbg.png') center no-repeat;">0</div>
        			</div>
        		</div>
        		<div id="daiban-button" class="unchoiced con-1" onclick="showhide(1)" style="width: 198px;height: 25px;border-right: 1px solid #bbb;float: left;display: block;cursor: pointer;">
        			<div style="margin-top: 3px;">
        				<div style="float: left;margin-left: 70px;">待办</div><div id="daiban-num" style="color: white;float: left;width: 20px;height: 15px;margin-left: 10px;margin-top: 2px;background: url('<%=request.getContextPath() %>/themes/fjh/images/forfooter/pbg.png') center no-repeat;">0</div>
        			</div>
        		</div>
        	</div>
        	<div id="notice-top3" style="height: auto;min-height: 40px;overflow-y: auto;overflow-x:hidden;border-left: 1px solid #bbb;border-right: 1px solid #bbb;">
        		<span style="margin-left:15px;">暂时没有新通知!</span>
        	</div>
        	<div id="daiban-top3" style="display:none;height: auto;min-height: 40px;overflow-y: auto;overflow-x:hidden;border-left: 1px solid #bbb;border-right: 1px solid #bbb;">
        		<span id="daiban-top3-no" style="margin-left:15px;">暂时没有待办!</span>
        	</div>
        	<div style="height: 25px;padding: 2px 0 5px 0;border-top: 1px solid #bbb;border-left: 1px solid #bbb;border-right: 1px solid #bbb;text-align: center;">
        		<div style="margin-top: 5px;">
        			<a href="<%=request.getContextPath() %>/fjh/todo/total.jsp" target="_blank" style="float: left;margin-left: 170px;">查看全部</a>
        		</div>
        	</div>
        </div>
    </div>
    <div id="iframeautodiv" class="right r" style="padding: 0;min-width: 1083px;z-index: 1">
    	<iframe id="mainfuncifr" frameborder='0' style="width: 100%; border: 0; height:100%;"></iframe>
    </div>
    <div class="clear"></div>
    <form id="new" style="display:none">
	    <div class="condi_container" >
			<span class="cd_name">当前密码:</span>
			<span class="cd_val">
				<input name="old_password" type="password" required2="true"/>
			</span>
		</div>
		<div class="condi_container">
			<span class="cd_name">新密码:</span>
			<span class="cd_val">
				<input name="new_password" type="password"  required2="true"/>
			</span>
		</div>
		<div class="condi_container" >
			<span class="cd_name">确认新密码:</span>
			<span class="cd_val">
				<input name="new_password_too" type="password"  required2="true"/>
			</span>
		</div>
	</form>
</div>

<div id="downnewbrs" style='text-align:center;padding-top:30px; display: none;line-height: 25px;'>
	<div style="font-size: 15px; font-weight: bold;"><img src="icn_no.png" style="float: left;">对不起，您浏览器的版本过低，请下载新版浏览器以便正常使用系统！</div>
	<div>
		<a style="font-size: 14px;" href='<%=request.getContextPath() %>/ocx/IE8-WindowsXP-x86-CHS.exe'><font color="red">点击此处</font>下载新版IE浏览器！</a>(下载后安装请按照安装向导进行操作！)
	</div>
	<div>
		若您的系统为Win2003请<a style="font-size: 14px;" href='<%=request.getContextPath() %>/ocx/IE8-WindowsXP-x86-CHS.exe'><font color="red">点击此处</font>下载新版IE浏览器！</a>(下载后安装请按照安装向导进行操作！)
	</div>
	<div>若安装不成功，请尝试安装此补丁后再次安装<a style="font-size: 12px;" href='<%=request.getContextPath() %>/ocx/ie8update.exe'>新版浏览器安装前补丁程序</a></div>
</div>





</body>
</html>
