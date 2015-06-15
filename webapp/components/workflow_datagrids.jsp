<%--
  工作流界面模版
 @author lining
 @since 2012-07-14
--%>
<%@page import="antelope.services.SysWorkflowService"%>
<%@page import="org.springframework.web.bind.annotation.RequestMapping"%>
<%@page import="antelope.interfaces.components.WorkflowDatagrids"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.interfaces.components.supportclasses.WorkflowDatagridsOptions"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.utils.I18n"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp" />
<%
	WorkflowDatagridsOptions opts = (WorkflowDatagridsOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
	String urlprefix = opts.getUrlprefix();
	SysWorkflowService workflow = SpringUtils.getBean(SysWorkflowService.class);
	String formkey = workflow.getStartFormKeyByProcessDefinitionKey(opts.processDefinitionKey);
%>
<title>${i18n['antelope.systemname']}</title>
<style></style>
<script>

var formwidth = 0;
var formheight = 0;
var oldtopoverflowy = null;

var tempsaveIntervalid = null;

$(function(){
	var selecteidex = $(".tabs").tabs("option", "selected");
	showMyTodolist();
	oldtopoverflowy = top.$("body").css("overflow-y");
});

var myicreatedcreated = false;

// linkfunction对应columns
// 0 : iCreatedColumns, 1 : myDoneColumns, 2 : allColumns, 3 : viewedColumns
function changeToViewLinkFunction0(obj) {
	return "<a href='javascript:void(0)' style='color:#0066cc;' class='grid_viewcoltitle' onclick='clickLineViewTitle(this, \"icreateViewCall\", \"icreated\")'>" + obj[this.viewButtonField] + "</a>";
}
function changeToViewLinkFunction1(obj) {
	return "<a href='javascript:void(0)' style='color:#0066cc;' class='grid_viewcoltitle' onclick='clickLineViewTitle(this, \"myDoneViewCall\", \"mydonelist\")'>" + obj[this.viewButtonField] + "</a>";
}
function changeToViewLinkFunction2(obj) {
	return "<a href='javascript:void(0)' style='color:#0066cc;' class='grid_viewcoltitle' onclick='clickLineViewTitle(this, \"showAllViewCall\", \"alllist\")'>" + obj[this.viewButtonField] + "</a>";
}
function changeToViewLinkFunction3(obj) {
	return "<a href='javascript:void(0)' style='color:#0066cc;' class='grid_viewcoltitle' onclick='clickLineViewTitle(this, \"showMyViewedViewCall\", \"mydoviewlist\")'>" + obj[this.viewButtonField] + "</a>";
}

function clickLineViewTitle(thisObj, winfuncname, gridclassname) {
	window[winfuncname].call($("." + gridclassname).datagrid("getCurrList")[$(thisObj).closest("td").attr("rowIndex")]);
}

function icreateViewCall() {
	top.$("body").css("overflow-y",'hidden');
	var ifram = $.dialogTopIframe({
		url: ctx + "/components/supportjsps/workflow_view.jsp?sid=" + this.sid + "&task_def_keys_=" + this.task_def_keys_ + "&proc_inst_id_=" + this.proc_inst_id_ + "&component=<%=component%>",
		width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:"<%=i18n.get("antelope.view")%>",
		close: function() {
			top.$("body").css("overflow-y", oldtopoverflowy);
		},
		buttons:{
			'<%=i18n.get("antelope.close")%>': function() {
				ifram.dialog("destroy");
				top.$("body").css("overflow-y", oldtopoverflowy);
			}
		}
	});
}

function showICreatlist() {
	$("#batchcompletebtn").hide();
	opts.iCreatedColumns['activetasknames'] = {headerText:'<%=i18n.get("antelope.currentphase")%>', sortable:false,
			labelFunction: function(obj){ return obj.activetasknames || (obj.proc_inst_id_? "(${i18n['workflow_datagrids.default.hasover']})" : "(暂存中)");}};
	opts.iCreatedColumns['candidateusers'] = {headerText:'<%=i18n.get("antelope.currentcandidateuser")%>', sortable:false};
	
	if (myicreatedcreated) {
		$(".icreated").datagrid("option", "dataProvider", ctx + "<%=urlprefix%>/listICreated.vot?" + encodeURI($("#form1").serialize()));
	} else {
		$(".icreated").datagrid({
			dataProvider: ctx + "<%=urlprefix%>/listICreated.vot?" + encodeURI($("#form1").serialize()),
			columns:opts.iCreatedColumns,
			<%if (TextUtils.stringSet(opts.selectionMode)) {%>
			selectionMode:'<%=opts.selectionMode%>',
			<%} %>
			viewButtonField: opts.viewButtonField,
			showSeqNum: true,
			buttons: { 
				query: {
					visibleFunction: function(obj) {
			 			return obj.proc_inst_id_ != null;
			 		},
					click:icreateViewCall
			 	},
			 	update: { // 暂存按钮
			 		visibleFunction: function(obj) {
			 			return obj.proc_inst_id_ == null;
			 		},
			 		click: function() {
			 			resetFormWidthHeight();
			 			$("#form2").trigger('openUpdateForm', this);
			 			$("#form2").resetForm(this).submitDialog({
			 				title:"<%=i18n.get("workflow_datagrids." + component + ".add") %>",
			 				url:ctx + "<%=urlprefix%>/startProcessInstance.vot", width:formwidth, height:formheight,
			 				state:"create",
			 				callback: read,
			 				autoTempSaveEnabled: <%=opts.autoTempSaveEnabled%>,
			 				showTempSave:true
			 			});
			 			$("#form2").trigger('openedUpdateForm', this);
			 		}
			 	}
			 	<%if (opts.showDeleteBtn) {%>
			 	,del: {
			 		visibleFunction: function(obj) {
			 			return obj.proc_inst_id_ == null;
			 		}, click: function() {
			 			$.submitDelete({ url: ctx + opts.urlprefix +"/deleteOneLine.vot?sid=" + this.sid, callback: read });
			 		}
			 	}
			 	<%}%>
			}
		});
		myicreatedcreated = true;
	}
}

var myTodoGridcreated = false;

var autotempsaveid = null;
function showMyTodolist() {
	$("#batchcompletebtn").show();
	if (myTodoGridcreated) {
		$(".mytodolist").datagrid("option", "dataProvider", ctx + "<%=urlprefix%>/listMyTodo.vot?" + encodeURI($("#form1").serialize()));
	} else {
		opts.myTodoColumns['name_']= '<%=i18n.get("antelope.currentphase")%>';
		$(".mytodolist").datagrid({
			dataProvider: ctx + "<%=urlprefix%>/listMyTodo.vot?" + encodeURI($("#form1").serialize()),
			columns:opts.myTodoColumns,
			<% if (opts.batchCompleteTasksEnabled) {%>
			selectionMode:"multipleRows",
			<%} else if (TextUtils.stringSet(opts.selectionMode)) {%>
			selectionMode:'<%=opts.selectionMode%>',
			<%} %>
			showSeqNum: true,
			buttons: { 
				release: {
					toolTip:"<%=i18n.get("workflow_datagrids." + component + ".completetask")%>",
					click:function() {
						top.$("body").css("overflow-y",'hidden');
						var ifram = $.dialogTopIframe({
							url: ctx + "/components/supportjsps/workflow_auditform.jsp?sid=" + this.sid + "&task_def_key_=" + this.task_def_key_ + 
									"&taskid=" + this.id_ + "&proc_inst_id_=" + this.proc_inst_id_ + "&component=<%=component%>&execution_id_=" + this.execution_id_,
							width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:"<%=i18n.get("workflow_datagrids." + component + ".completetask")%>",
							close: function() {
								top.$("body").css("overflow-y", oldtopoverflowy);
								<%if (opts.showTempSaveBtn && opts.autoTempSaveEnabled) {%> // 如果存在暂存，则点击取消或者关闭时也需要刷新界面%>
								read();
								<%}%>
							},
							buttons:{
								<%if (opts.showTempSaveBtn) {%>
								'暂存': function(e) {
									ifram.find("iframe")[0].contentWindow._submittasks_(read, "1", e.target);
								},
								<%}%>
								'<%=i18n.get("workflow_datagrids." + component + ".submittask")%>': function(e) {
									ifram.find("iframe")[0].contentWindow._submittasks_(read, null, e.target);
								}, '<%=i18n.get("antelope.cancel")%>': function() {
									ifram.dialog("destroy");
									top.$("body").css("overflow-y", oldtopoverflowy);
									<%if (opts.showTempSaveBtn && opts.autoTempSaveEnabled) {%> // 如果存在暂存，则点击取消或者关闭时也需要刷新界面%>
									read();
									<%}%>
								}
							}
						});
						
						<%if (opts.showTempSaveBtn && opts.autoTempSaveEnabled) {%>
							autotempsaveid = setInterval(function(){
								ifram.find("iframe")[0].contentWindow._submittasks_(function(isok){
									autoSaveTipShow(ifram, isok ? "自动暂存成功！": "自动暂存失败，原因为：表单验证失败！");
								}, "1", null, true);
							}, tempsaveinterval);
						<%}%>
				 	}
			 	}
			}
		});
		myTodoGridcreated = true;
	}
}

function autoSaveTipShow(ifram, text) {
	var autosavediv = ifram.parent().find(".autosave2");
	if (autosavediv.length == 0){
		autosavediv = $("<div class='autosave2'></div>");
		ifram.parent().append(autosavediv);
	}
	autosavediv.text(text);
	if (autotempsaveid) { // 点击了取消之后不在出现暂存成功的字样
		autosavediv.fadeIn("fast");
		setTimeout(function(){
			autosavediv.fadeOut();
		}, 3000);
	}
}

function clearTempsaveIntervel() {
	clearInterval(autotempsaveid);
	tempsaveinterid = null;
	top.$("body").find(".autosave2").remove();
}

var myDoneGridcreated = false;

function myDoneViewCall() {
	top.$("body").css("overflow-y",'hidden');
	var ifram = $.dialogTopIframe({
		url: ctx + "/components/supportjsps/workflow_view.jsp?sid=" + this.sid + "&task_def_keys_=" + this.task_def_keys_ + "&taskid=" + this.id_ + "&task_def_key_=" + this.task_def_key_ +
				"&proc_inst_id_=" + this.proc_inst_id_ + "&component=<%=component%>",
		width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:"<%=i18n.get("antelope.view")%>",
		close: function() {
			top.$("body").css("overflow-y", oldtopoverflowy);
		}, buttons:{
			'<%=i18n.get("antelope.close")%>': function() {
				ifram.dialog("destroy");
				top.$("body").css("overflow-y", oldtopoverflowy);
			}
		}
	});
}

function showMyDonelist() {
	$("#batchcompletebtn").hide();
	opts.myDoneColumns['name_']= '<%=i18n.get("antelope.mycompletephase")%>';
	if (myDoneGridcreated) {
		$(".mydonelist").datagrid("option", "dataProvider", ctx + "<%=urlprefix%>/listMyDone.vot?" + encodeURI($("#form1").serialize()));
	} else {
		$(".mydonelist").datagrid({
			dataProvider: ctx + "<%=urlprefix%>/listMyDone.vot?" + encodeURI($("#form1").serialize()),
			columns:opts.myDoneColumns,
			<%if (TextUtils.stringSet(opts.selectionMode)) {%>
			selectionMode:'<%=opts.selectionMode%>',
			<%} %>
			viewButtonField: opts.viewButtonField,
			showSeqNum: true,
			buttons: { 
				query: {
					click:myDoneViewCall
			 	}
			}
		});
		myDoneGridcreated = true;
	}
}

function showAllViewCall() {
	top.$("body").css("overflow-y",'hidden');
	var ifram = $.dialogTopIframe({
		url: ctx + "/components/supportjsps/workflow_view.jsp?sid=" + this.sid + "&task_def_keys_=" + this.task_def_keys_ + "&proc_inst_id_=" + this.proc_inst_id_ + "&component=<%=component%>",
		width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:"<%=i18n.get("antelope.view")%>",
		close: function() {
			top.$("body").css("overflow-y", oldtopoverflowy);
		},
		buttons:{
			'<%=i18n.get("antelope.close")%>': function() {
				ifram.dialog("destroy");
				top.$("body").css("overflow-y", oldtopoverflowy);
			}
		}
	});
}

var allcreated = false;
function showAlllist() {
	$("#batchcompletebtn").hide();
	opts.allColumns['activetasknames'] = {headerText:'<%=i18n.get("antelope.currentphase")%>', sortable:false,
			labelFunction: function(obj){ return obj.activetasknames || (obj.proc_inst_id_? "(${i18n['workflow_datagrids.default.hasover']})" : "(暂存中)");}};
	opts.allColumns['candidateusers'] = {headerText:'<%=i18n.get("antelope.currentcandidateuser")%>', sortable:false};
	if (allcreated) {
		$(".alllist").datagrid("option", "dataProvider", ctx + "<%=urlprefix%>/listAll.vot?" + encodeURI($("#form1").serialize()));
	} else {
		$(".alllist").datagrid({
			dataProvider: ctx + "<%=urlprefix%>/listAll.vot?" + encodeURI($("#form1").serialize()),
			columns:opts.allColumns,
			<%if (TextUtils.stringSet(opts.selectionMode)) {%>
			selectionMode:'<%=opts.selectionMode%>',
			<%} %>
			viewButtonField: opts.viewButtonField,
			showSeqNum: true,
			buttons: { 
				query: {
					click:showAllViewCall
			 	}
			}
		});
		allcreated = true;
	}
}

<%if(opts.showToView){ %>
var toviewcreated = false ;
function showMyToViewlist() {
	$("#batchcompletebtn").hide();
	if (toviewcreated) {
		$(".mytoviewlist").datagrid("option", "dataProvider", ctx + "<%=urlprefix%>/listMyToView.vot?" + encodeURI($("#form1").serialize()));
	} else {
		$(".mytoviewlist").datagrid({
			dataProvider: ctx + "<%=urlprefix%>/listMyToView.vot?" + encodeURI($("#form1").serialize()),
			columns:opts.toViewColumns,
			<%if (TextUtils.stringSet(opts.selectionMode)) {%>
			selectionMode:'<%=opts.selectionMode%>',
			<%} %>
			showSeqNum: true,
			buttons: { 
				release: {
					toolTip:"<%=i18n.get("workflow_datagrids." + component + ".completeviewtask")%>",
					click:function() {
						top.$("body").css("overflow-y",'hidden');
						var ifram = $.dialogTopIframe({
							url: ctx + "/components/supportjsps/workflow_view.jsp?sid=" + this.sid + "&taskid=" + this.id_ + "&proc_inst_id_=" + this.proc_inst_id_ + "&component=<%=component%>",
							width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:"<%=i18n.get("workflow_datagrids." + component + ".completeviewtask")%>",
							close: function() {
								top.$("body").css("overflow-y", oldtopoverflowy);								
							},
							buttons:{
								'<%=i18n.get("workflow_datagrids." + component + ".submitviewtask")%>': function(e) {
									ifram.find("iframe")[0].contentWindow._submittasks_(read, e.target);
								}, '<%=i18n.get("antelope.cancel")%>': function() {
									ifram.dialog("destroy");
									top.$("body").css("overflow-y", oldtopoverflowy);
								}
							}
						});
				 	}
			 	}
			}
		});
		toviewcreated = true;
	}
}
var viewedcreated = false;

function showMyViewedViewCall() {
	top.$("body").css("overflow-y",'hidden');
	var ifram = $.dialogTopIframe({
		url: ctx + "/components/supportjsps/workflow_view.jsp?sid=" + this.sid + "&taskid=" + this.id_ + "&proc_inst_id_=" + this.proc_inst_id_ + "&component=<%=component%>",
		width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:"<%=i18n.get("antelope.view")%>",
		close: function() {
			top.$("body").css("overflow-y", oldtopoverflowy);
		},
		buttons:{
			'<%=i18n.get("antelope.close")%>': function() {
				ifram.dialog("destroy");
				top.$("body").css("overflow-y", oldtopoverflowy);
			}
		}
	});
}

function showMyViewedlist() {
	$("#batchcompletebtn").hide();
	if (viewedcreated) {
		$(".mydoviewlist").datagrid("option", "dataProvider", ctx + "<%=urlprefix%>/listMyTiewed.vot?" + encodeURI($("#form1").serialize()));
	} else {
		$(".mydoviewlist").datagrid({
			dataProvider: ctx + "<%=urlprefix%>/listMyTiewed.vot?" + encodeURI($("#form1").serialize()),
			columns:opts.viewedColumns,
			<%if (TextUtils.stringSet(opts.selectionMode)) {%>
			selectionMode:'<%=opts.selectionMode%>',
			<%} %>
			viewButtonField: opts.viewButtonField,
			showSeqNum: true,
			buttons: { 
				query: {
					click:showMyViewedViewCall
			 	}
			}
		});
		viewedcreated = true;
	}
}
<%}%>

function create() {
	resetFormWidthHeight();
	
	$("#form2").trigger('openCreateForm');
	$("#form2").resetForm().submitDialog({
		title:"<%=i18n.get("workflow_datagrids." + component + ".add") %>",
		url:ctx + "<%=urlprefix%>/startProcessInstance.vot", width:formwidth, height:formheight,
		state:"create",
		successTip:"<%=i18n.get("antelope.submitsuccess")%>",
		callback: read
		<%if (opts.showTempSaveBtn) {%>
		,showTempSave:true
		<%}%>
	});
	$("#form2").trigger('openedCreateForm');
}

function resetFormWidthHeight() {
	
	var origwidth = $("#form2").data("origwidth");
	var origheight = $("#form2").data("origheight");
	if (!origwidth) {
		origwidth = $("#form2").width();
		origheight = $("#form2").height();
		$("#form2").data("origwidth", origwidth);
		$("#form2").data("origheight", origheight);
	}
	
	formwidth = origwidth + 26;
	formheight = origheight + 81;

	formwidth = Math.min ($(window).width() - 10, formwidth);
	formheight = Math.min ($(window).height() - 10, formheight);
}

function read() {
	if (autotempsaveid) {
		clearTempsaveIntervel();
	}
	var selecteidex = $(".workflowtabdiv").tabs("option", "selected");
	$("." + $(".workflowtabsul a:eq(" + selecteidex + ")").prop("listclasses")).datagrid("option", "dataProvider", ctx + "<%=urlprefix%>/" + $("#tabsul a:eq(" + selecteidex + ")").prop("voturls") + ".vot?" + encodeURI($("#form1").serialize()));
}

function batchComplete() {
	var selecteds = $(".mytodolist").datagrid("option", "selectedItems");
	
	if (selecteds.length == 0) {
		alert("${i18n['antelope.selectatlistone']}");
		return;
	}
	
	var prevtaskkey = null;
	for (var i = 0; i < selecteds.length; ++i) {
		if (prevtaskkey == null) {
			prevtaskkey = selecteds[i].task_def_key_;
		} else {
			if (prevtaskkey != selecteds[i].task_def_key_) {
				alert("${i18n['antelope.alertnotsametaskkey']}");
				return;
			}
		}
	}
	top.$("body").css("overflow-y",'hidden');
	var ifram = $.dialogTopIframe({
		url: ctx + "/components/supportjsps/batchworkflow_auditform.jsp?&component=<%=component%>",
		data: "task_def_key_=" + prevtaskkey + "&allsids=" + $.extractSids(selecteds) + "&alltasksids=" + $.extractField(selecteds, "id_") 
		+ "&allprocinstsids=" + $.extractField(selecteds, "proc_inst_id_") +　"&allexecutionids=" + $.extractField(selecteds, "execution_id_"),
		width:top.$(top.window).width() - 20, height:top.$(top.window).height() - 20, title:"<%=i18n.get("workflow_datagrids." + component + ".completetask")%>",
		close: function() {
			top.$("body").css("overflow-y", oldtopoverflowy);
		},
		buttons:{
			'<%=i18n.get("workflow_datagrids." + component + ".submittask")%>': function(e) {
				ifram.find("iframe")[0].contentWindow._submittasks_(read, e.target);
			}, '<%=i18n.get("antelope.cancel")%>': function() {
				ifram.dialog("destroy");
				top.$("body").css("overflow-y", oldtopoverflowy);
			}
		}
	});
}

/**
 * 批量导出列表
 */
function batchExportList(){
	var selecteidex = $(".workflowtabdiv").tabs("option", "selected");
	var selditemsids = '';
	if (<%=TextUtils.stringSet(opts.selectionMode) %>){
		selditemsids = $.extractSids($("." + $(".workflowtabsul a:eq(" + selecteidex + ")").prop("listclasses")).datagrid("option", "selectedItems"));
	}
	$.postIframe(ctx + "<%=urlprefix%>/exportExcel.vot?tabkey=" + $(".workflowtabsul a:eq(" + selecteidex + ")").prop("i18nkey"), 
			{selectedItemSids:selditemsids});
}

</script>
</head><body class="sm_main" style="overflow-x:hidden;">
<!-- 新建修改查看对话框内容 -->
<form id="form2" style="display: none; padding: 0;">
	<input type="hidden" name="processdefinitionkey" value="<%=opts.processDefinitionKey%>" default="<%=opts.processDefinitionKey%>"/>
	<%if (TextUtils.stringSet(opts.businessFormKey)) {%>
	<jsp:include page="<%=opts.businessFormKey %>"></jsp:include>
	<%} else if (formkey.indexOf("${") == -1) {// 确保无未替换参数 %>
	<jsp:include page="<%=formkey %>"></jsp:include>
	<%} %>
</form> 
<!-- 主界面  -->
<form id="form1" class="singlegridform_query" onsubmit="read(); return false;">
	<h3 class="smm_title"><span class="moduletitle"></span><span class="m_head" id="basicinfo"></span><span class="m_line"></span></h3>
	<div class="condition basicinfo">
		<jsp:include page="/components/supportjsps/grid_querypart.jsp"></jsp:include>
	</div>
</form>

<div class="tabs workflowtabdiv">
	<ul id="tabsul" class="workflowtabsul">
		<%if (opts.showICreatedList){ %>
		<li><a href="#tabs-myStartdo" id="tabs-1" createfuncname="showICreatlist" listclasses="icreated" voturls="listICreated" i18nkey="icreated" onclick="showICreatlist()"><%=i18n.get("workflow_datagrids." + component + ".icreated") %></a></li>
		<%} %>
		<li><a href="#tabs-myTodo" selected2="true" id="tabs-2" createfuncname="showMyTodolist" listclasses="mytodolist" voturls="listMyTodo" i18nkey="mytodo"  onclick="showMyTodolist()"><%=i18n.get("workflow_datagrids." + component + ".mytodo") %></a></li>
		<li><a href="#tabs-myCompleteTodo" id="tabs-3" createfuncname="showMyDonelist" listclasses="mydonelist" voturls="listMyDone" i18nkey="mydone"  onclick="showMyDonelist()"><%=i18n.get("workflow_datagrids." + component + ".mydone") %></a></li>
		<%if (opts.showAllList) { %>
		<li><a href="#tabs-myAll" id="allcheck" createfuncname="showAlllist" listclasses="alllist" voturls="listAll"  i18nkey="all" onclick="showAlllist()"><%=i18n.get("workflow_datagrids." + component + ".all") %></a></li>
		<%} %>
		<%if(opts.showToView){ %>
			<li><a href="#tabs-myToView" id="tabs-4" createfuncname="showMyToViewlist" listclasses="mytoviewlist" voturls="listMyToView"  i18nkey="mytoview" onclick="showMyToViewlist()"><%=i18n.get("workflow_datagrids." + component + ".mytoview") %></a></li>
			<li><a href="#tabs-myCompleteView" id="tabs-5" createfuncname="showMyViewedlist" listclasses="mydoviewlist" voturls="listMyTiewed"  i18nkey="myviewed" onclick="showMyViewedlist()"><%=i18n.get("workflow_datagrids." + component + ".myviewed") %></a></li>
		<%} %>
	</ul>
	<%if (opts.showICreatedList){ %>
	<div id="tabs-myStartdo" class="tabs-myStartdo">
		<div class="icreated"></div>
	</div>
	<%} %>
	<div id="tabs-myTodo" class="tabs-myTodo">
		<div class="mytodolist"></div>
	</div>
	<div id="tabs-myCompleteTodo" class="tabs-myCompleteTodo">
		<div class="mydonelist"></div>
	</div>
	<%if (opts.showAllList) { %>
	<div id="tabs-myAll" class="tabs-myAll">
		<div class="alllist"></div>
	</div>
	<%} %>
	<%if(opts.showToView){ %>
	<div id="tabs-myToView">
		<div class="mytoviewlist"></div>
	</div>
	<div id="tabs-myCompleteView">
		<div class="mydoviewlist"></div>
	</div>
	<%} %>
</div>
<div class="func_span" style="margin-top: 20px;">
	<%if (opts.showExportBtn) {%>
	<input id="batchexportbtn" class="button" type="button" onClick="batchExportList()" value="<%=i18n.get("workflow_datagrids." + component + ".batchexport") %>">
	<%} %>
	<%if (opts.batchCompleteTasksEnabled) {%>
	<input id="batchcompletebtn" class="button" type="button" onClick="batchComplete()" value="<%=i18n.get("workflow_datagrids." + component + ".batchcomplete") %>">
	<%} %>
	<%if (opts.showCreateBtn) {%>
	<input class="button" type="button" onClick="create()" value="<%=i18n.get("workflow_datagrids." + component + ".add") %>">
	<%} %>
</div>
</body>
</html>