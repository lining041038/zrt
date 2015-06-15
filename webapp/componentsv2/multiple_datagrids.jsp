<%--
 多列表界面模版
 @author lining
 @since 2012-07-14
--%>
<%@page import="antelope.interfaces.components.supportclasses.SingleDatagridOptionsExtended"%>
<%@page import="antelope.interfaces.components.supportclasses.MultipleDatagridsOptions"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.interfaces.components.supportclasses.Button"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.interfaces.components.supportclasses.SingleDatagridOptions"%>
<%@page import="antelope.utils.I18n"%>
<%@ page language="java" pageEncoding="utf-8"%>
<% request.setAttribute("componentVersion", "v2"); %>
<jsp:include page="/include/header2.jsp" />
<%
	MultipleDatagridsOptions opts = (MultipleDatagridsOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
%>
<title>${i18n['antelope.systemname']}</title>
<style></style>
<script>
var formwidth = 0;
var formheight = 0;

var currentmgridkey = "";

function singlegrid_view__() {
	var formkeyfield = opts.singleDatagridOptionMap[currentmgridkey].formKeyField;
	var thisObj = this;
	if (formkeyfield && this[formkeyfield]) {
		$("#othereditform").load(ctx + this[formkeyfield], function(){
			resetFormWidthHeight(true);
			$("#othereditform").resetForm(thisObj).viewDialog({
				state:"view", width:formwidth, height:formheight, title:"<%=i18n.get("single_datagrid." + component + ".view")%>"
			});
			$("#othereditform").trigger('openViewForm', thisObj);
		});
	} else {
		createFormInnerByData(this);
		resetFormWidthHeight();
		$(".mgridform" + currentmgridkey).resetForm(this).viewDialog({
			state:"view", width:formwidth, height:formheight, title:"<%=i18n.get("single_datagrid." + component + ".view")%>"
		});
		$(".mgridform" + currentmgridkey).trigger('openViewForm', this);
	}
}

function singlegrid_update__() {
	
	var gridopts = opts.singleDatagridOptionMap[currentmgridkey];
	
	var formkeyfield = opts.singleDatagridOptionMap[currentmgridkey].formKeyField;
	var thisObj = this;
	if (formkeyfield && this[formkeyfield]) {
		$("#othereditform").load(ctx + this[formkeyfield], function(){
			setTimeout(function(){
				resetFormWidthHeight(true);
				$("#othereditform").resetForm(thisObj)
				$("#othereditform").setCurrentState("create");
				$("#othereditform").trigger('openUpdateForm', thisObj);
				$("#othereditform").submitDialog({
					url:ctx + opts.urlprefix + "/addOrUpdateOne.vot?gridkey=" + currentmgridkey, width:formwidth, height:formheight, title:"<%=i18n.get("single_datagrid." + component + ".modify")%>",
					showTempSave: gridopts.showTempSaveBtn,
					callback: read
				});
				$("#othereditform").trigger('openedUpdateForm', thisObj);
			}, 500);
		});
	} else {
		createFormInnerByData(this);
		resetFormWidthHeight();
		$(".mgridform" + currentmgridkey).resetForm(this)
		$(".mgridform" + currentmgridkey).setCurrentState("create");
		$(".mgridform" + currentmgridkey).trigger('openUpdateForm', this);
		$(".mgridform" + currentmgridkey).submitDialog({
			url:ctx + opts.urlprefix + "/addOrUpdateOne.vot?gridkey=" + currentmgridkey, width:formwidth, height:formheight, title:"<%=i18n.get("single_datagrid." + component + ".modify")%>",
			showTempSave: gridopts.showTempSaveBtn,
			callback: read
		});
		$(".mgridform" + currentmgridkey).trigger('openedUpdateForm', this);
	}
}

function resetFormWidthHeight(isotherform) {
	if (isotherform) {
		formwidth = $("#othereditform").width() + 45;
		formheight = $("#othereditform").height() + 101;
	} else {
		var origwidth = $(".mgridform" + currentmgridkey).data("origwidth");
		var origheight = $(".mgridform" + currentmgridkey).data("origheight");
		if (!origwidth) {
			origwidth = $(".mgridform" + currentmgridkey).width();
			origheight = $(".mgridform" + currentmgridkey).height();
			$(".mgridform" + currentmgridkey).data("origwidth", origwidth);
			$(".mgridform" + currentmgridkey).data("origheight", origheight);
		}
		
		formwidth = origwidth + 45;
		formheight = origheight + 101;
	}
	
	formwidth = Math.min ($(window).width() - 10, formwidth);
	formheight = Math.min ($(window).height() - 10, formheight);
}

function singlegrid_del__() {
	$.submitDelete({ url: ctx + opts.urlprefix +"/deleteOneLine.vot?gridkey=" + currentmgridkey + "&sid=" + this.sid, callback: read });
}

function createFormInnerByData(data) {
	var gridopts = opts.singleDatagridOptionMap[currentmgridkey];
	if (gridopts.formKey || gridopts.formfields.length == 0)
		return;
	
	var formhtml = "";
	for (var i = 0; i < gridopts.formfields.length; ++i) {
		var xmlfield = gridopts.formfields[i].enumXmlField;
		formhtml += '<span class="cd_name">' + gridopts.formfields[i].label + ':</span>\
		<span class="cd_val">';
		if (xmlfield && data && data[xmlfield]) {
			formhtml += '<select name="' + gridopts.formfields[i].field + '"  ' +  gridopts.formfields[i].validate + '\
				dataProvider="' + data[xmlfield] + '" \
				state="disabled:false; disabled.view:true;"></select>';
		} else {
			formhtml += '<input name="' + gridopts.formfields[i].field + '" ' + gridopts.formfields[i].validate + ' state="disabled:false; disabled.view:true;"/>';
		}
		formhtml += '</span>';
	}
	$(".mgridform" + currentmgridkey).html(formhtml).initWidgets();
}

$(function(){
	
	$("#tabsul a:eq(0)").click();
	
	return;
});


function create() {
	resetFormWidthHeight();
	
	var gridopts = opts.singleDatagridOptionMap[currentmgridkey];
	
	$(".mgridform" + currentmgridkey).resetForm()
	$(".mgridform" + currentmgridkey).setCurrentState("create");
	$(".mgridform" + currentmgridkey).trigger('openCreateForm', this);
	$(".mgridform" + currentmgridkey).submitDialog({
		title:"<%=i18n.get("single_datagrid." + component + ".add") %>",
		successTip:"<%=i18n.get("single_datagrid." + component + ".successTip") %>"||"<%=i18n.get("antelope.savesuccess") %>",
		url:ctxPath + opts.urlprefix + "/addOrUpdateOne.vot?gridkey=" + currentmgridkey, width:formwidth + 26, height:formheight,
		showTempSave: gridopts.showTempSaveBtn,
		callback: read
	});
	$(".mgridform" + currentmgridkey).trigger('openedCreateForm', this);
}

function read(thisObj) {
	
	
	var self = null;
	
	if (thisObj) {
		self = $(thisObj);
		if ($(".formgridkey" + self.attr('gridkey')).is(":hidden")) {
			$(".queryform").hide();
			$(".formgridkey" + self.attr('gridkey')).show();
		}
		currentmgridkey = self.attr('gridkey');
	}
	
	if (!thisObj || self.attr("created")) {
		$("#gridkey" + currentmgridkey + " .datagrid_container").datagrid("option", "dataProvider",
				ctx + opts.urlprefix + "/getSingleGridList.vot?gridkey=" + currentmgridkey + "&" + encodeURI($(".formgridkey" + currentmgridkey).serialize()));
	} else {
		var gridopts = opts.singleDatagridOptionMap[currentmgridkey];
		var thegridbtns = null;
		
		for (var key in gridopts.buttons) {
			if (thegridbtns == null)
				thegridbtns = {};
			
			if (key == 'del' && !gridopts.showDeleteBtn)
				continue;
			if ((key == 'i_up' || key == 'i_down') && !gridopts.showMoveBtns)
				continue;
			
			if (key == 'update' && !gridopts.showUpdateBtn)
				continue;
			
			var thegridbtn = {};
			thegridbtn['click'] = eval(gridopts.buttons[key].click);
			
			if (gridopts.buttons[key].visibleFunction) {
				thegridbtn['visibleFunction'] = eval(gridopts.buttons[key].visibleFunction);
			}
			
			if (gridopts.buttons[key].toolTip) {
				thegridbtn['toolTip'] = gridopts.buttons[key].toolTip;
			}
			
			thegridbtns[key] = thegridbtn;
		}
		
		$("#gridkey" + currentmgridkey + " .datagrid_container").datagrid({
			dataProvider: ctx + opts.urlprefix + "/getSingleGridList.vot?gridkey=" + currentmgridkey + "&" + encodeURI($(".formgridkey" + currentmgridkey).serialize()),
			operationHeaderWidth:40,
			showSeqNum: true,
			selectionMode: gridopts.selectionMode,
			viewButtonField: gridopts.viewButtonField, 
			columns:gridopts.columns,
			buttons:thegridbtns
		});
	}
}

function changeToViewLinkFunction(obj) {
	return "<a href='javascript:void(0)' style='color:#0066cc;' onclick='clickLineViewTitle(this)'>" + obj[this.viewButtonField] + "</a>";
}

function clickLineViewTitle(thisObj) {
	singlegrid_view__.call($("#gridkey" + currentmgridkey + " .datagrid_container").datagrid("getCurrList")[$(thisObj).closest("td").attr("rowIndex")]);
}

function exportit() {
	$(".formgridkey" + currentmgridkey).postIframe(ctx + opts.urlprefix + "/exportExcel.vot?gridkey=" + currentmgridkey);
}

function importit() {
	
	var file = $("[name=importexcelfile]");
	file.after(file.clone().val(""));
	file.remove();
	
	var btns = {};
	btns['<%=i18n.get("antelope.ok")%>'] = function() {
		$("#singlegridform_imp").postIframe(ctx + opts.urlprefix + "/importExcelInner.vot?gridkey=" + currentmgridkey, function(data){
			if (data)
				alert(data);
			else {
				alert("<%=i18n.get("antelope.impsuccess")%>");
				$("#singlegridform_imp").dialog("destroy");
				$("#gridkey" + currentmgridkey + " .datagrid_container").datagrid("refresh");
			}
		});
	}
	
	btns['<%=i18n.get("antelope.cancel")%>'] = function() {
		$("#singlegridform_imp").dialog("destroy");
	}
	
	$("#singlegridform_imp").dialog({
		title:"<%=i18n.get("antelope.imp") %>",
		buttons:btns
	});
}

</script>
</head><body class="sm_main" style="overflow-x:hidden;">
<h3 class="smm_title"><span class="moduletitle"></span><span class="m_head" id="basicinfo"></span><span class="m_line"></span></h3>
<!-- 主界面  -->
<%for (Entry<String, SingleDatagridOptionsExtended> opt: opts.singleDatagridOptionMap.entrySet()) {%>
<!-- 新建修改查看对话框内容 -->
<form class="mgridform<%=opt.getKey() %>" style="display: none;">
	<%if (TextUtils.stringSet(opt.getValue().formKey)){ %>
		<jsp:include page="<%=opt.getValue().formKey %>"></jsp:include>
	<% } else if (opt.getValue().formfields.length == 0) {
		String formname = "/" + RegExpUtil.getFirstMatched("(?<=/).*(?=\\.jsp)", request.getRequestURI().substring(1)) + "_" + opt.getKey() + "form.jsp";	
	%>
		<jsp:include page="<%=formname %>"></jsp:include>
	<% } else { %>
	<div class="condi_container">
		<%for (int i = 0; i < opt.getValue().formfields.length; ++i) { %>
		<span class="cd_name"><%=opt.getValue().formfields[i].label%>：</span>
		<span class="cd_val">
			<input name="<%=opt.getValue().formfields[i].field%>" <%=opt.getValue().formfields[i].validate%> state='disabled:false; disabled.view:true;'/>
		</span>
		<%} %>
	</div>
	<%} %>
</form>
<form onsubmit="read(); return false;" style="display: none;" class="singlegridform_query queryform formgridkey<%=opt.getKey() %>">
	<div class="condition basicinfo">
	<%if (TextUtils.stringSet(opt.getValue().queryformKey)) {// 当设置了查询对应jsp文件，则使用%>
	<jsp:include page="<%=opt.getValue().queryformKey%>"></jsp:include>
	<%} else { %>
		<div class="condi_container">
		<%
		for (int i = 0; i < opt.getValue().queryfields.length; ++i) {
			String[] fieldinfoarr = opt.getValue().queryfields[i].split(":");
			String queryfieldname = fieldinfoarr[0];
			String fieldsenum = "";
			if (fieldinfoarr.length > 1) {
				fieldsenum = fieldinfoarr[1];
			}
			if ( i != 0 && i % 3 == 0) {
				%><div class="condi_container"><%
			}
			%>
			<span class="cd_name"><%=opt.getValue().columns.get(queryfieldname).headerText %>：</span> <span class="cd_val">
			<%if (TextUtils.stringSet(opt.getValue().columns.get(queryfieldname).enumXml)){ %>
				<select name="<%=queryfieldname%>" dataProvider="<%=opt.getValue().columns.get(queryfieldname).enumXml %>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>">
					<option value="">${i18n['antelope.all'] }</option>
				</select>
			<%} else if (TextUtils.stringSet(fieldsenum) && fieldsenum.matches("^date|datetime$")){ // 此处控件与枚举均使用:做为分割，所以枚举名称一定不能使用控件的class%>
				<input name="<%=queryfieldname%>" class="<%=fieldsenum %>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>"/>
			<%} else if (TextUtils.stringSet(fieldsenum)){ %>
				<select name="<%=queryfieldname%>" dataProvider="<%=fieldsenum %>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>">
					<option value="">${i18n['antelope.all'] }</option>
				</select>
			<%} else {%>
				<input name="<%=queryfieldname%>" value="<%=BaseController.d(TextUtils.noNull(request.getParameter(queryfieldname)))%>"/>
			<%} %>
			</span>
			<%if ( i != (opt.getValue().queryfields.length - 1) && (i + 1) % 3 == 0) {
				%></div><%
			}%>
		<%} %>
		</div>
		<%} %>
	</div>
	<div class="sonbtn_container"> <span class="sonbtn_m_line"></span>
		<a onclick="read(this)"  gridkey="<%=opt.getKey() %>" class="btnth"><b class="sconbton2">${i18n['antelope.query']}</b></a>
	</div>
</form>
<%} %>
<form id="othereditform" style="display:none">
</form>
<div class="tabs">
	<ul id="tabsul">
		<%for (Entry<String, SingleDatagridOptionsExtended> opt: opts.singleDatagridOptionMap.entrySet()) {%>
		<li><a href="#gridkey<%=opt.getKey() %>" gridkey="<%=opt.getKey() %>" onclick="read(this)"><%=opt.getValue().label %></a></li>
		<%} %>
	</ul>
	<%for (Entry<String, SingleDatagridOptionsExtended> opt: opts.singleDatagridOptionMap.entrySet()) {%>
	<div id="gridkey<%=opt.getKey() %>">
		<div class="datagrid_container" style="margin-left: 0;padding-left: 0;"></div>
		<div class="func_span" style="margin-top: 33px;">
		<%for (Entry<String,Button> btn: opt.getValue().funcBtns.entrySet()) { 
			if (btn.getKey().equals(":antelope.exp") ||
				btn.getKey().equals(":antelope.imp") ||
				btn.getKey().equals(":antelope.batchmoveup") ||
				btn.getKey().equals(":antelope.batchmovedown") ||
				btn.getKey().equals(":single_datagrid.${component}.add") ||
				btn.getKey().equals(":antelope.batchdelete")) {
				String key = btn.getKey().replaceFirst("^:", "");
				key = key.replaceFirst("\\$\\{component\\}", component);
			%>
				<%if (btn.getKey().equals(":antelope.exp") && opt.getValue().showExportBtn) {%>
				<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('#gridkey<%=opt.getKey() %> .datagrid_container'))" value="<%=i18n.get(key) %>">
				<%} %>
				<%if (btn.getKey().equals(":antelope.imp") && opt.getValue().showImportBtn) {%>
				<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('#gridkey<%=opt.getKey() %> .datagrid_container'))" value="<%=i18n.get(key) %>">
				<%} %>
				<%if (btn.getKey().equals(":antelope.batchmoveup") && opt.getValue().showBatchMoveBtns) {%>
				<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('#gridkey<%=opt.getKey() %> .datagrid_container'))" value="<%=i18n.get(key) %>">
				<%} %>
				<%if (btn.getKey().equals(":antelope.batchmovedown") && opt.getValue().showBatchMoveBtns) {%>
				<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('#gridkey<%=opt.getKey() %> .datagrid_container'))" value="<%=i18n.get(key) %>">
				<%} %>
				<%if (btn.getKey().equals(":single_datagrid.${component}.add") && opt.getValue().showCreateBtn) {%>
				<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=opt.getValue().createBtnClickFunction %>($('#gridkey<%=opt.getKey() %> .datagrid_container'))" value="<%=i18n.get(key) %>">
				<%} %>
			<%} else { %>
				<input class="button" type="button" title="<%=TextUtils.noNull(btn.getValue().toolTip) %>" onClick="<%=btn.getValue().click %>($('#gridkey<%=opt.getKey() %> .datagrid_container'))" value="<%=btn.getKey()%>">
			<%} %>
		<%} %>
		</div>
	</div>
	<%} %>
</div>
<!-- 数据导入对话框 -->
<form id="singlegridform_imp" style="display: none;" enctype="multipart/form-data">
	<input type="file" name="importexcelfile"/>
</form>
</body>
</html>




