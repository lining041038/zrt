<!-- 
	title:quartz console
	author:huanggc
	since:2012/2/17
 -->
<%@page import="antelope.services.SessionService"%>
<%@page import="antelope.db.DBUtil"%>
<%@page import="antelope.services.SessionService"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<style>
#file div, #file a{float: left !important;}
body,html{height: 100% !important;}
</style>
<%
boolean isAdmin = ((SessionService) session.getAttribute("service")).isAdmin();
boolean canPizhun = true ;
%>

<script>
var canPizhun=<%=canPizhun%>;
$(function(){
	
	$("body").viewNavigator({firstView:"#thehomeview",effect:'slide'}); 
	readBOLine();
   $('#thehomeview').layout({fxName:'none'});
});

function showDemo(){
	$("#showDemoDialog").dialog({ width:500, height:290, modal:true});	
}

function showBaseInstruction(){
	$("#baseInstruction").dialog({ width:500, height:260, modal:true});	
}

function startAllTask(){
	 $.ajax({
	        url: ctxPath+"/system/quartz/setTaskService.vot?action=clearAllTask",
	        type: 'post',
	        async:false,
	        error: function(){
	           alert("清除数据执行失败！");		
	        },
	        success: function(xml){
				if (xml == 0) {
					alert("清除数据成功！");
					zTreeOnClick();
				}else{
					alert("清除数据失败！");	
				}		
			    $.ajax({
			        url: ctxPath+"/system/quartz/setTaskService.vot?action=startAllTask",
			        type: 'post',
			        async:false,
			        error: function(){
			           alert("执行失败！");		
			        },
			        success: function(xml){
						if (xml == 0) {
							alert("启动Task成功！");
							zTreeOnClick();
						}else{
							alert("已经启动所有Task！");	
						}		   
			        }
			    });
	        }
	    });
}
function readBOLine() {
	$.showBusyState();
	$.getJSON(ctxPath + "/system/quartz/getTaskGroupTree.vot?", function(data){
	 	   if(data===null)
			{ 
			$.hideBusyState();
			return;
			}
		   
		var treedata = [{id:'root', open:true, name:'TaskGroup', nodes:data}];
		
		var setting = {
				expandSpeed : "",
				showLine : true,
				checkable : false,
				callback : {
				   click: zTreeOnClick
				}
			};
		zTree = $("#boline_tree").zTree(setting, treedata);
		
		$.hideBusyState();
		 	 
	});
	 
}

// 树节点点击之后 
function zTreeOnClick() {
	if(zTree.getSelectedNode().id == 'root'||(zTree.getSelectedNode().isParent))
		return;
	$.getJSON(ctxPath+"/system/quartz/setTaskService.vot?action=query&group="+zTree.getSelectedNode().sid, function(data){
		
		if ($("#userlist").data("datagridinited")) {
			$("#userlist").datagrid("destroy");	
		}
		$("#userlist").data("datagridinited", true);
		
		$(".datagrid_container").datagrid({
			showSeqNum:true,
			dataProvider:data,
			showSeqNum:true, 
			columns:{
				display_name:"任务名称", 
				cron_expression:"CRON表达式",
				next_fire_time:"下次运行时间",
				prev_fire_time:"上次运行时间",
				priority:{headerText:"级别"},
			    statu:"当前状态",
			    description:"上次运行结果",
			    start_time:"开始时间",
			    end_time:"结束时间"
				},
			buttons:[{
					text:'light',
					toolTip:'暂停任务',
					visibleFunction:function(obj) {
						return   (obj.statu!='暂停中'&&canPizhun);
					  },
					click:function() {
						manageTask(this,'pause') 
					}
				},{
					text:'query',
					toolTip:'开始任务',
					visibleFunction:function(obj) {
						return   (obj.statu=='暂停中'&& canPizhun);
					  },
					click:function() {
						manageTask(this,'resume')  
					}
				},
				 
				{
					text:'update',
					toolTip:'重设时间',
					click:function() {
						manageTask(this,'reset')  
					}
					, visibleFunction:function (obj){return canPizhun;}
				}
			]
		});
	});
}
function manageTask(obj,action){
	var triggerName = encodeURIComponent(encodeURIComponent(obj.trigger_name));
	var group = encodeURIComponent(encodeURIComponent(obj.trigger_group));
	var  JOB_NAME=obj.job_name;
    if(action=='reset'){
    	$("#edititemdialog").setCurrentState("create").resetForm();
    	$("#edititemdialog [name=cronExpression]").val(obj.cron_expression);
    	$("#edititemdialog").dialog({
    		buttons:{
    			'确定':function(){
    				if ($("#edititemdialog").validate()) {
    					var cronExpression=$('#edititemdialog [name=cronExpression]').val();
    					$.post(ctxPath+"/system/quartz/setTaskService.vot?jobDetailName="+JOB_NAME+"&action="+action+"&triggerName="+triggerName+"&group="+group+"&cronExpression="+cronExpression+"&taskGroup="+zTree.getSelectedNode().sid, function(xml){
    						if (xml == 0) {
    							alert("执行成功!");
    							$("#edititemdialog").dialog("destroy");
        						zTreeOnClick();
    						}else{
    							alert("请检查CronExpression!");	
    						}	
    						
    					});
    				}
    			}, '取消':function() {
    				$("#edititemdialog").dialog("destroy");
    			}
    		}, width:500, height:310, modal:true
    	});
    }else{
	var triggerName = encodeURIComponent(encodeURIComponent(obj.trigger_name));
	var group = encodeURIComponent(encodeURIComponent(obj.trigger_group));
    $.ajax({
        url: ctxPath+"/system/quartz/setTaskService.vot?jobDetailName="+JOB_NAME+"&action="+action+"&triggerName="+triggerName+"&group="+group+"&taskGroup="+zTree.getSelectedNode().sid,
        type: 'post',
        error: function(){
           alert("执行失败！");		
        },
        success: function(xml){
			if (xml == 0) {
				alert("执行成功！");
				zTreeOnClick();
			}else{
				alert("执行失败！");	
			}		   
        }
    });
   }
	 
}
</script>
</head>
<body class="sm_main" scroll=no style="height: 100%;">
<!-- 任务调度时间修改 -->
<form id="edititemdialog" style="display: none;">
	<div class="condi_container">
		<span class="cd_name">cronExp：</span>
		<span class="cd_val">
			<input name="cronExpression" required="true"/>
		</span>
	</div>
</form>
<div style="height: 100%; " id="thehomeview">
	<div class="ui-layout-west" name="homeview">
		<div id="treeboline" class="ui-widget ui-widget-content" style="height: 100%;margin: 2px;">
			<div class="ui-widget-header" style="height: 30px;">
				<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 12px;font-weight: bold;">Task分组</span>
			</div>
			<div id="boline_tree" class="tree"></div>
		</div> 
	</div>
	<div class="ui-layout-center" name="homeview">
		<div class="ui-widget ui-widget-content" style="height: 100%; margin: 2px;">
			<div class="ui-widget-header" style="height: 30px;">
				<span style="margin-top:8px;display:block; margin-left: 5px; font-size: 12px;font-weight: bold;">TaskDetail列表</span>
				<span style="display: block; position: absolute; top: 5px; right: 5px;height:18px;">
					<button onclick="showBaseInstruction()"    style="height:28px;">基本说明</button>
					<button onclick="showDemo()"    style="height:28px;">参考Demo</button>
					<button onclick="startAllTask()"    style="height:28px;">启动任务调度</button>
				</span> 
				<span style="display: block; position: absolute; top: 5px; right: 5px;">
				</span>
			</div>
			<div class="datagrid_container" style="margin-top: 10px;"></div>
		
			<div style="margin-left:10px;display:none" id="showDemoDialog">
				<table cellspacing="1" cellpadding="1" border="1"><tbody><tr><td width="235"><p>表示式</p></td><td width="324"><p>说明</p></td></tr><tr><td width="235" valign="top"><p>"0 0 12 * * ? "</p></td><td width="324" valign="top"><p>每天12点运行</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 ? * *"</p></td><td width="324" valign="top"><p>每天10:15运行</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 * * ?"</p></td><td width="324" valign="top"><p>每天10:15运行</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 * * ? *"</p></td><td width="324" valign="top"><p>每天10:15运行</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 * * ? 2008"</p></td><td width="324" valign="top"><p>在2008年的每天10：15运行</p></td></tr><tr><td width="235" valign="top"><p>"0 * 14 * * ?"</p></td><td width="324" valign="top"><p>每天14点到15点之间每分钟运行一次，开始于14:00，结束于14:59。</p></td></tr><tr><td width="235" valign="top"><p>"0 0/5 14 * * ?"</p></td><td width="324" valign="top"><p>每天14点到15点每5分钟运行一次，开始于14:00，结束于14:55。</p></td></tr><tr><td width="235" valign="top"><p>"0 0/5 14,18 * * ?"</p></td><td width="324" valign="top"><p>每天14点到15点每5分钟运行一次，此外每天18点到19点每5钟也运行一次。</p></td></tr><tr><td width="235" valign="top"><p>"0 0-5 14 * * ?"</p></td><td width="324" valign="top"><p>每天14:00点到14:05，每分钟运行一次。</p></td></tr><tr><td width="235" valign="top"><p>"0 10,44 14 ? 3 WED"</p></td><td width="324" valign="top"><p>3月每周三的14:10分到14:44，每分钟运行一次。</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 ? * MON-FRI"</p></td><td width="324" valign="top"><p>每周一，二，三，四，五的10:15分运行。</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 15 * ?"</p></td><td width="324" valign="top"><p>每月15日10:15分运行。</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 L * ?"</p></td><td width="324" valign="top"><p>每月最后一天10:15分运行。</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 ? * 6L"</p></td><td width="324" valign="top"><p>每月最后一个星期五10:15分运行。</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 ? * 6L 2007-2009"</p></td><td width="324" valign="top"><p>在2007,2008,2009年每个月的最后一个星期五的10:15分运行。</p></td></tr><tr><td width="235" valign="top"><p>"0 15 10 ? * 6#3"</p></td><td width="324" valign="top"><p>每月第三个星期五的10:15分运行。</p></td></tr></tbody></table>
			</div>
			
		  </div>
		<div style="margin-left:10px;display:none;overflow-y:hidden" id="baseInstruction">
			<table cellspacing="1" cellpadding="1" border="1"><tbody><tr><td width="54" valign="top"><p>位置</p></td><td width="82" valign="top"><p>时间域名</p></td><td width="133"><p>允许值</p></td><td width="290"><p>允许的特殊字符</p></td></tr><tr><td width="54" valign="top"><p>1</p></td><td width="82" valign="top"><p>秒</p></td><td width="133" valign="top"><p>0-59</p></td><td width="290" valign="top"><p>, - * /</p></td></tr><tr><td width="54" valign="top"><p>2</p></td><td width="82" valign="top"><p>分钟</p></td><td width="133" valign="top"><p>0-59</p></td><td width="290" valign="top"><p>, - * /</p></td></tr><tr><td width="54" valign="top"><p>3</p></td><td width="82" valign="top"><p>小时</p></td><td width="133" valign="top"><p>0-23</p></td><td width="290" valign="top"><p>, - * /</p></td></tr><tr><td width="54" valign="top"><p>4</p></td><td width="82" valign="top"><p>日期</p></td><td width="133" valign="top"><p>1-31</p></td><td width="290" valign="top"><p>, - * ? / L W C</p></td></tr><tr><td width="54" valign="top"><p>5</p></td><td width="82" valign="top"><p>月份</p></td><td width="133" valign="top"><p>1-12</p></td><td width="290" valign="top"><p>, - * /</p></td></tr><tr><td width="54" valign="top"><p>6</p></td><td width="82" valign="top"><p>星期</p></td><td width="133" valign="top"><p>1-7</p></td><td width="290" valign="top"><p>, - * ? / L C #</p></td></tr><tr><td width="54" valign="top"><p>7</p></td><td width="82" valign="top"><p>年(可选)</p></td><td width="133" valign="top"><p>空值1970-2099</p></td><td width="290" valign="top"><p>, - * /</p></td></tr></tbody></table>
			<p>星号(*)：可用在所有字段中，表示对应时间域的每一个时刻，例如，*在分钟字段时，表示“每分钟”</p>
			<p>问号（?）：该字符只在日期和星期字段中使用，它通常指定为“无意义的值”，相当于点位符</p>
			<p>减号(-)：表达一个范围，如在小时字段中使用“10-12”，则表示从10到12点，即10,11,12</p>
			<p>逗号(,)：表达一个列表值，如在星期字段中使用“MON,WED,FRI”，则表示星期一，星期三和星期五</p>
			<p>斜杠(/)：x/y表达一个等步长序列，x为起始值，y为增量步长值。如在分钟字段中使用0/15，则表示为0,15,30和45秒，而5/15在分钟字段中表示5,20,35,50，你也可以使用*/y，它等同于0/y</p>
			<p>L：该字符只在日期和星期字段中使用，代表“Last”的意思，但它在两个字段中意思不同。L在日期字段中，表示这个月份的最后一天，如一月的31号，非闰年二月的28号；如果L用在星期中，则表示星期六，等同于7。但是，如果L出现在星期字段里，而且在前面有一个数值X，则表示“这个月的最后X天”，例如，6L表示该月的最后星期五</p>
			<p>W：该字符只能出现在日期字段里，是对前导日期的修饰，表示离该日期最近的工作日。例如15W表示离该月15号最近的工作日，如果该月15号是星期六，则匹配14号星期五；如果15日是星期日，则匹配16号星期一；如果15号是星期二，那结果就是15号星期二。但必须注意关联的匹配日期不能够跨月，如你指定1W，如果1号是星期六，结果匹配的是3号星期一，而非上个月最后的那天。W字符串只能指定单一日期，而不能指定日期范围</p>
			<p>LW组合：在日期字段可以组合使用LW，它的意思是当月的最后一个工作日</p>
			<p>井号(#)：该字符只能在星期字段中使用，表示当月某个工作日。如6#3表示当月的第三个星期五(6表示星期五，#3表示当前的第三个)，而4#5表示当月的第五个星期三，假设当月没有第五个星期三，忽略不触发</p>
			<p>C：该字符只在日期和星期字段中使用，代表“Calendar”的意思。它的意思是计划所关联的日期，如果日期没有被关联，则相当于日历中所有日期。例如5C在日期字段中就相当于日历5日以后的第一天。1C在星期字段中相当于星期日后的第一天</p>
		</div>	
		<div>
	</div>
   </div>
 </div>
 
</body>
</html>