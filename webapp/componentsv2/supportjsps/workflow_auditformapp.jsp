<%--
  工作流审批界面全界面组件支持jsp
 @author lining
 @since 2012-08-03
--%>
<%@page import="antelope.services.supportclasses.Header2Params"%>
<%@page import="antelope.services.JSPUtilService"%>
<%@page import="antelope.utils.JSONObject"%>
<%@page import="antelope.services.SessionService"%>
<%@page import="antelope.interfaces.components.supportclasses.AuditFormDisplayMode"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.services.SysWorkflowService"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.interfaces.components.supportclasses.WorkflowDatagridsOptions"%>
<%@ page language="java" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<link rel="stylesheet" href="${pageContext.request.contextPath}/themes/fjh/jquery.mobile-1.4.5.min.css">
<script src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.mobile-1.4.5.min.js"></script>
<script>var ctx = '${pageContext.request.contextPath}';</script>

<%
	JSPUtilService jsputilservice = SpringUtils.getBean(JSPUtilService.class);
	Header2Params header2params = jsputilservice.initHeader2(request, response);

	String taskid = request.getParameter("taskid");
	String sid = request.getParameter("sid");
	WorkflowDatagridsOptions opts = (WorkflowDatagridsOptions) request.getAttribute("opts");
	
	
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
	String urlprefix = opts.getUrlprefix();
	SysWorkflowService workflow = SpringUtils.getBean(SysWorkflowService.class);
	String proc_inst_id_ = request.getParameter("proc_inst_id_");
	String execution_id_ = request.getParameter("execution_id_");
	String startFormKey = workflow.getStartFormKeyByProcessDefinitionKey(opts.processDefinitionKey);
	
	TransactionStatus status = SpringUtils.beginTransaction();
	if (startFormKey != null && startFormKey.indexOf("${") != -1) {
		startFormKey = startFormKey.substring(2, startFormKey.length() - 1);
		startFormKey = workflow.getHisVariable(proc_inst_id_, startFormKey).toString();
	}
	String formkey = workflow.getTaskFormKeyByTaskId(taskid);
	
	if (formkey != null && formkey.indexOf("${") != -1) {
		formkey = formkey.substring(2, formkey.length() - 1);
		formkey = workflow.getHisVariable(proc_inst_id_, formkey).toString();
	}
	
	
	SpringUtils.commitTransaction(status);
	
	//System.out.println(request.getParameter("task_def_key_"));
	
%>
<style>
html,body{padding: 0; margin: 0; border: 0; background: white;}
.attachedfile{display: block;}
.attachedfile a{display: block;}
.contentarea{padding-bottom: 0.5em;padding-top: 0;margin-top: 0;}
</style>

<style>
@media only all {
	.ui-table-reflow td .ui-table-cell-label, .ui-table-reflow th .ui-table-cell-label
		{
		padding: .4em 0 .4em .4em;
		/*min-width: 30%;*/
		display: inline-block;
		margin: -.4em 0 -.4em -.4em
	}
}
</style>


<script>

var currenttoken = '';

var filegroupkeys = {};

//典型日期格式化方法 格式为 2011-05-07 直接截断的方式
function typicalDateFormatter(datestr) {
	if (!datestr || datestr.length < 10)
		return datestr;
	return datestr.substring(0,10);
}

// 根据节点的类名包裹数据。通常用于非form节点下数据的包裹过程
jQuery.fn.extend({
	wrapDataByClass: function(data) {
		for (var key in data) {
			var obj = this.find("." + key);
			if (obj.is(".date")) { // 当为日期时
				this.find("." + key).text(typicalDateFormatter(data[key]));
			} else if (obj.is(".attachedfile")) {
				
				var thisObj = this;	
				filegroupkeys[data[key]] = key;
				$.getJSON( ctx + "/upload/getuploadedfileinfos.vot?filegroupsid=" + data[key] + "&token=" + currenttoken, function(data) {
					for (var i = 0; i < data.fileinfos.length; i++) {
						thisObj.find("." + filegroupkeys[data.filegroupsid]).
						append("<a href='javascript:void(0)' onclick=\"gotoviewdoc('token=" + currenttoken + "&sid=" + data.fileinfos[i].sid + "')\" >" + data.fileinfos[i].filename + "</a>");
					}
				});
				
			} else {
				this.find("." + key).text(data[key]);
			}
		}
		return this;
	}
});
var task_def_key_sd = "<%=request.getParameter("task_def_key_")%>";
// ios 本地方法调用
function callIOSNativeFunction(cmd,parameter1) {   
    document.location="objc://"+cmd+"/"+parameter1;  //cmd代表objective-   
}   

function cancelAudit(thisObj) {
	callIOSNativeFunction("cancelaudit", '<%=proc_inst_id_%>');
}

function gotoviewdoc(downurl) {
	callIOSNativeFunction("gotoviewdoc", downurl);
}

$(function(){
// 	alert(ctx);
<%-- 	alert("<%=urlprefix%>"); --%>
	
	
	
<%-- 	$.getJSON(ctx + "<%=urlprefix%>/getFormData.vot?sid=<%=sid%>&proc_inst_id_=<%=proc_inst_id_%>&execution_id_=<%=execution_id_%>&task_def_key_=<%=request.getParameter("task_def_key_")%>&taskid=<%=taskid%>", function(data){ --%>
// 		$("body").wrapDataByClass(data);
// 	});

	callIOSNativeFunction('startLoadBusiForm');
	
	//startLoadBusiForm('1432528934509');
	
	$("[data-role=listview] span:not(.attachedfile)").click(function(e){
		$(".contentarea").text($(this).text());
		$( "[data-role=popup]" ).popup( "option", "transition", "pop" );
		$("[data-role=popup]").popup("open");
		
		
	});
	
	
	//$( "body>[data-role='panel']" ).panel();
	
	
	$( document ).on( "pagecreate", "#pagetwo", function() {
		$.getJSON(ctx + "/common/UserRoleOrgController/getAsyncOrgnizationOrDeptTreedata.vot?sid=orgroot&filterdept=false&token=" + currenttoken, function(data){
			var html = "";
			$.each( data, function ( i, val ) {
				html += "<li><a href='#pagethree' data-transition=\"flow\" onclick=\"gotoPageThree('" + val.sid + "')\">" + val.name + "</a></li>";
			});
			
			$("#orglist").html( html );
			$("#orglist").listview( "refresh" );
		});
		
		
// 		$( "#autocomplete" ).on( "filterablebeforefilter", function ( e, data ) {
// 			var $ul = $( this ),
// 				$input = $( data.input ),
// 				value = $input.val(),
// 				html = "";
// 			$ul.html( "" );
// 			if ( value && value.length > 2 ) {
// 				$ul.html( "<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>" );
// 				$ul.listview( "refresh" );
// 				$.ajax({
// 					url: "http://gd.geobytes.com/AutoCompleteCity",
// 					dataType: "jsonp",
// 					crossDomain: true,
// 					data: {
// 						q: $input.val()
// 					}
// 				})
// 				.then( function ( response ) {
// 					$.each( response, function ( i, val ) {
// 						html += "<li>" + val + "</li>";
// 					});
// 					$ul.html( html );
// 					$ul.listview( "refresh" );
// 					$ul.trigger( "updatelayout");
// 				});
// 			}
// 		});
	});
	
});

function gotoPageThree(orgsid) {
	
	$.getJSON(ctx + "/common/getUsersByUnitsid.vot?unitsid=" + orgsid + "&token=" + currenttoken, function(data){

		var html = "";
		$.each( data, function ( i, val ) {
			html += "<li><a class='ui-icon-plus' href='#pageone'  data-transition=\"flow\" onclick=\"selectThisUser('" + val.username + "', '" + val.name + "')\">" + val.name + "</a></li>";
		});
		
		$("#userlistview").html( html );
		$("#userlistview").listview( "refresh" );
	});
	
}

function selectThisUser(username, name) {
	$(".selectusername").text(name);
	selectedusername = username;
	
	$("#shenpitab").addClass("ui-btn-active");
}

var busidata = null;
function startLoadBusiForm(token) {
	currenttoken = token;

	$.getJSON(ctx + "<%=urlprefix%>/getFormData.vot?token=" + token + "&sid=<%=sid%>&proc_inst_id_=<%=proc_inst_id_%>&execution_id_=<%=execution_id_%>&task_def_key_=<%=request.getParameter("task_def_key_")%>&taskid=<%=taskid%>", function(data){
		busidata = data;
		$("body").wrapDataByClass(data);
	});
}

function gobackpageOne() {
	$("#shenpitab").addClass("ui-btn-active");
}

var selectedusername;

function auditpassconfirmedcanceld() {
	
}

function auditpassconfirmed() {
	 $.mobile.loading("show");
	$.post(ctx + '<%=urlprefix%>/completeTask.vot?sid=<%=sid%>&token=' + currenttoken + '&taskid=<%=taskid%>&proc_inst_id_=<%=proc_inst_id_%>&execution_id_=<%=execution_id_%>&task_def_key_=<%=request.getParameter("task_def_key_")%>', 
			{var_comment:encodeURIComponent($("#var_comment").val()), var_result: encodeURIComponent("通过"), selectuser: encodeURIComponent(selectedusername),  processdefinitionkey: '<%=opts.processDefinitionKey%>'}, 
		function(data) {
		
		if (data) {
			$("#errorpopup .ui-title").text (data);
			$("#popupDialog").popup('close');
			$("#errorpopup").popup("open");
		} else {
			callIOSNativeFunction("cancelaudit", 'refreshlist');
		}
		
		 $.mobile.loading("hide");
	});
}


function auditnopassconfirmed() {
	
	$.mobile.loading("show");
	$.post(ctx + '<%=urlprefix%>/completeTask.vot?sid=<%=sid%>&token=' + currenttoken + '&taskid=<%=taskid%>&proc_inst_id_=<%=proc_inst_id_%>&execution_id_=<%=execution_id_%>&task_def_key_=<%=request.getParameter("task_def_key_")%>', 
			{var_comment:encodeURIComponent($("#var_comment").val()), var_result: encodeURIComponent("驳回"), selectuser: encodeURIComponent(selectedusername),  processdefinitionkey: '<%=opts.processDefinitionKey%>'}, 
		function(data) {
		
		if (data) {
			$("#errorpopup .ui-title").text (data);
			$("#popupDialog").popup('close');
			$("#errorpopup").popup("open");
		} else {
			callIOSNativeFunction("cancelaudit", 'refreshlist');
		}
		
		 $.mobile.loading("hide");
	});
}

function popupDialogPass() {
	
	<%if (!"usertask5".equals(request.getParameter("task_def_key_"))) {%>
	if (!selectedusername) {
		$("#onpasstitle").text ("请选择下一步审批人!");
		$("#okcancelpop").hide();
		$("#popclosebtn").show();
		return;
	}
	<%}%>
	
	$("#onpasstitle").text ("您确认要审批通过吗？");
	$("#okcancelpop").show();
	$("#popclosebtn").hide();
	
}

function showcontract(thisObj) {
	$("#contractdatap tr").remove();
	$.getJSON(ctx + "/fjh/donatecontractmanage/DonateContractApprovalController/getContractStage.vot?sid=" + busidata.sid + "&token=" + currenttoken, function(dataobj){
		$(dataobj).each(function(){
			var data = '<tr>\
						   <td>'+this.stage_order+'</td>\
						   <td>'+this.stage_money+'</td>\
						   <td>'+this.stage_begin_time+'</td>\
						   <td>'+this.stage_end_time+'</td>\
						   <td>'+this.pay_condition+'</td>\
							</tr>';
			$("#contractdatap").append(data);
		});
		
		$("#movie-table").table( "refresh" );
	});
}

</script>
</head>
<body style="background: white;">

<!-- <div data-role="panel" data-position="right" data-display="overlay" data-theme="a"> -->
<!--      <ul data-role="listview" data-icon="false"> -->
<!--        	<li data-icon="delete"><a href="#" data-rel="close">关闭</a></li> -->
<!--        </ul> -->
<!--        <p class="contentarea"></p> -->
<!-- </div>/panel -->


<div data-role="page" id="pageone" style="background: white; border: 0;">

	<div class="ui-content" id="popupCloseRight" style="max-width: 280px;" data-role="popup">
		<a class="ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right" href="#" data-rel="back">Close</a>
		<p class="contentarea"></p>
	</div>
	
	<div id="errorpopup" style="max-width: 400px;" data-role="popup" data-theme="a" data-overlay-theme="a" data-dismissible="false">
	    <div data-role="header" data-theme="a">
	    	<h1>提示</h1>
	    </div>
	    <div class="ui-content" role="main">
	        <h3 class="ui-title"></h3>
	    </div>
	    <div class="ui-grid-a">
		    <div class="ui-block-a"><a class="ui-btn ui-corner-all ui-shadow ui-btn-a" href="javascript:void(0)" data-rel="back">关闭</a></div>
		</div>
	</div>
	
	<div id="popupDialog" style="max-width: 400px;" data-role="popup" data-theme="a" data-overlay-theme="a" data-dismissible="false">
	    <div data-role="header" data-theme="a">
	    	<h1>提示</h1>
	    </div>
	    <div class="ui-content" role="main">
	        <h3 id="onpasstitle" class="ui-title">您确认要审批通过吗？</h3>
	    </div>
	    <div id="okcancelpop" class="ui-grid-a">
		    <div id="cancellink" class="ui-block-a"><a onclick="auditpassconfirmedcanceld()" id="cancelbutton" class="ui-btn ui-corner-all ui-shadow ui-btn-a" href="javascript:void(0)" data-rel="back">取消</a></div>
		    <div id="okbutton" class="ui-block-b"><a onclick="auditpassconfirmed()" class="ui-btn ui-corner-all ui-shadow ui-btn-a" href="javascript:void(0)" data-transition="flow">确认</a></div>
		</div>
		<a id="popclosebtn" class="ui-btn ui-corner-all ui-shadow ui-btn-a" href="javascript:void(0)" style="margin: 1em;"  data-rel="back">关闭</a>
	</div>
	
	<div id="popupDialog2" style="max-width: 400px;" data-role="popup" data-theme="a" data-overlay-theme="a" data-dismissible="false">
	    <div data-role="header" data-theme="a">
	    	<h1>提示</h1>
	    </div>
	    <div class="ui-content" role="main">
	        <h3 class="ui-title">您确认要审批不通过吗？</h3>
	    </div>
	    <div class="ui-grid-a">
		    <div class="ui-block-a"><a class="ui-btn ui-corner-all ui-shadow ui-btn-a" href="#" data-rel="back">取消</a></div>
		    <div class="ui-block-b"><a onclick="auditnopassconfirmed()" class="ui-btn ui-corner-all ui-shadow ui-btn-a" href="#" data-transition="flow">确认</a></div>
		</div>
	</div>
	
	<div id="tabs" data-role="tabs" >
	  <div data-role="navbar">
	    <ul>
	      <li><a href="#one" data-ajax="false" class="ui-btn-active ui-navbar-btn">基本信息</a></li>
	      <li><a id="shenpitab" href="#two" class="ui-navbar-btn" data-ajax="false">审批</a></li>
	    </ul>
	  </div>
	  <div class="ui-body-d ui-content" id="one">

		<ul data-role="listview" >
	    
		    <li>
		    	<b>合同类别：</b><span class="contract_category" data-rel="popup" data-transition="pop"></span>
			</li>
		    
		    <li>
		    	<b>合作方：</b><span  class="donate_person"></span>
			</li>
			
			<li>
		    	<b>合同名称：</b><span class="contract_name"></span>
			</li>
			<li>
		    	<b>合同地点：</b><span class="place"></span>
			</li>
			<li>
		    	<b>项目名称：</b><span class="projectname"></span>
			</li>
			<li>
		    	<b>总金额（万元）：</b><span class="totalmoney"></span>
			</li>
			<li>
		    	<b>合同编号：</b><span class="contract_order"></span>
			</li>
			<li>
		    	<b>工作经费：</b><span class="work_pay_type"></span><span class="work_pay_money"></span>万元
			</li>
			<li>
		    	<b>合同开始时间：</b><span class="begin_time date"></span>
			</li>
			<li>
		    	<b>合同结束时间：</b><span class="end_time date"></span>
			</li>
			<li>
		    	<b>合同负责人：</b><span class="header"></span>
			</li>
			<li>
		    	<b>合同：</b><span class="cont_uploadfile attachedfile"></span>
			</li>
			<li>
		    	<b>合同附件：</b><span class="uploadfile attachedfile"></span>
			</li>
			<li>
		    	<b>合同目的：</b><span class="contract_pur"></span>
			</li>
			<li>
		    	<b>领导批示附件：</b><span class="enduploadfile attachedfile"></span>
		    	
			</li>
			<li>
		    	<a href="#pagejieduan" onclick="showcontract(this)"><b>合同阶段</b></a>
			</li>
		</ul>
		
	  </div>
	  <div id="two" style="background: white;">
	   <ul data-role="listview">
		    <li>
		    	<textarea id="var_comment" placeholder="审批意见"  rows="3" cols="40"></textarea>
			</li>
			<%
			if (!"usertask5".equals(request.getParameter("task_def_key_"))) {%>
		    <li>
		    	<a href="#pagetwo" class="ui-icon-plus" data-transition="flow">下一步审批人：<span  class="selectusername"></span></a>
			</li>
			<%} %>
		</ul>
   	    <a href="#popupDialog" onclick="popupDialogPass()" data-transition="pop" class="ui-btn" data-position-to="window" data-rel="popup">审批通过</a>
		<a href="#popupDialog2" data-transition="pop" class="ui-btn" data-position-to="window" data-rel="popup">审批不通过</a>
		
	  </div>
	  
	</div>
</div>


<!-- Start of second page: #two -->
<div data-role="page" id="pagetwo" data-theme="a">

	<div data-role="header">
		<a href="#" onclick="gobackpageOne()" data-rel="back" data-icon="arrow-l" data-iconpos="notext" data-ajax="false">返回</a>
		<h1>选择审批人</h1>
	</div><!-- /header -->

	<div role="main" class="ui-content">
	
		<ul id="orglist" data-role="listview" >
		</ul>
	</div><!-- /content -->

	<div data-role="footer">
		<h4>选择审批人</h4>
	</div><!-- /footer -->
</div><!-- /page two -->


<!-- Start of second page: #two -->
<div data-role="page" id="pagejieduan" data-theme="a">

	<div data-role="header">
		<a href="#" data-rel="back" data-icon="arrow-l" data-iconpos="notext" data-ajax="false">返回</a>
		<h1>合同阶段</h1>
	</div><!-- /header -->
	
	<div role="main" class="ui-content">
		<table class="table-stroke" id="movie-table" data-role="table" data-mode="reflow">
		  <thead>
		    <tr>
		      <th data-priority="1">阶段序号：</th>
		      <th data-priority="2">阶段付款金额（万元）：</th>
		      <th data-priority="3">计划开始时间：</th>
		      <th data-priority="4">计划结束时间：</th>
		      <th data-priority="5">付款条件：</th>
		    </tr>
		  </thead>
		  <tbody id="contractdatap">
		  </tbody>
		</table>
	</div><!-- /content -->

	<div data-role="footer">
		<h4>合同阶段</h4>
	</div><!-- /footer -->
</div><!-- /page two -->


<!-- Start of second page: #two -->
<div data-role="page" id="pagethree" data-theme="a">

	<div data-role="header">
		<a href="#" data-rel="back" data-icon="arrow-l" data-iconpos="notext" data-ajax="false">返回</a>
		<h1>选择审批人</h1>
	</div><!-- /header -->

	<div role="main" class="ui-content">

		<ul id="userlistview" data-role="listview" >
		
		</ul>

	</div><!-- /content -->

	<div data-role="footer">
		<h4>选择审批人</h4>
	</div><!-- /footer -->
</div><!-- /page two -->

</body>
</html>