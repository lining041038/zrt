<%--
  单列表界面通用js部分代码
 @author lining
 @since 2012-07-14
--%>
<%@page import="antelope.interfaces.components.supportclasses.CreateUpdateWindowMode"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.interfaces.components.supportclasses.Button"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.interfaces.components.supportclasses.SingleDatagridOptions"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%
	SingleDatagridOptions opts = (SingleDatagridOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = (String) request.getAttribute("component");
	String formname = "/" + RegExpUtil.getFirstMatched("(?<=/).*(?=\\.jsp)", request.getRequestURI().substring(1)) + "form.jsp";
	String gridprovidersuffix = TextUtils.noNull(request.getParameter("gridprovidersuffix"));
	String precreateitem = TextUtils.noNull(request.getParameter("precreateitem"));
%>
<script>
var formwidth = 0;
var formheight = 0;

function singlegrid_view__() {
	createFormInnerByData(this);
	
	var dialogTopParams = getDialogTopParams();
	var thisObj = this;
	$("#singlegridform").trigger('preopenViewForm');
	$("#singlegridform").viewDialog({
		width:dialogTopParams.finalDialogWidth, height:dialogTopParams.finalDialogHeight,
		dialogTopIframeURL: dialogTopParams.dialogTopURL,
		isinline:<%=CreateUpdateWindowMode.DISPLAY_INLINE.equals(opts.createUpdateWindowMode)%>,
		title:"<%=i18n.get("single_datagrid." + component + ".view")%>",
		dialogTopLoadedComplete: function() {
			var filteredform = this.filter("#singlegridform");
			filteredform.resetForm(thisObj);
			filteredform.setCurrentState("view");
			filteredform.trigger('openViewForm', thisObj);
		}
	});
	$("#singlegridform").resetForm(this);
	$("#singlegridform").setCurrentState("view");
	$("#singlegridform").trigger('openViewForm', this);
}

function singlegrid_update__() {
	createFormInnerByData(this);
	
	var dialogTopParams = getDialogTopParams();
	var thisObj = this;
	$("#singlegridform").trigger('openUpdateForm', this);
	$("#singlegridform").submitDialog({
		submitLabel: "<%=i18n.get("single_datagrid." + component + ".oklabel")%>",
		url:ctx + opts.urlprefix + "/addOrUpdateOne.vot?a=1"<%=gridprovidersuffix%>, 
				width:dialogTopParams.finalDialogWidth, height:dialogTopParams.finalDialogHeight, title:"<%=i18n.get("single_datagrid." + component + ".modify")%>",
		dialogTopIframeURL: dialogTopParams.dialogTopURL,
		<%=opts.showTempSaveBtn ? "showTempSave:true," : ""%>
		callback: createUpdateCplt__,
		isinline:<%=CreateUpdateWindowMode.DISPLAY_INLINE.equals(opts.createUpdateWindowMode)%>,
		dialogTopLoadedComplete: function() {
			var filteredform = this.filter("#singlegridform");
			filteredform.trigger('openUpdateForm', thisObj);
			filteredform.setCurrentState("create");
			filteredform.resetForm(thisObj);
			filteredform.trigger('openedUpdateForm', thisObj);
		}
	});
	$("#singlegridform").setCurrentState("update");
	$("#singlegridform").resetForm(this);
	$("#singlegridform").trigger('openedUpdateForm', this);
}

function getDialogTopParams() {
	
	var params = {};
	params.dialogTopURL = null;
	params.finalDialogWidth = formwidth + 26;
	params.finalDialogHeight = formheight + 4;
	<% if (CreateUpdateWindowMode.DIALOG_TOP_IFRAME.equals(opts.createUpdateWindowMode)) {%>
	params.dialogTopURL = ctx + "/componentsv2/supportjsps/singledatagrid_dialogtopiframe.jsp?component=<%=opts.getComponent()%>&ifrformname=<%=formname%>";
	params.finalDialogWidth = <%=opts.windowModeParams.dialogWidth%>;
	params.finalDialogHeight = <%=opts.windowModeParams.dialogHeight%>;
	<%}%>
	
	return params;
}

function singlegrid_del__() {
	$.submitDelete({ url: ctx + opts.urlprefix +"/deleteOneLine.vot?sid=" + this.sid<%=gridprovidersuffix%>, callback: deleteCplt__ });
}

function singlegrid_moveup__() {
	singlegrid_moveupdowninner("?isup=true", this.sid);
}

function singlegrid_movedown__() {
	singlegrid_moveupdowninner("", this.sid);
}

function changeToViewLinkFunction(obj) {
	return "<a href='javascript:void(0)' style='color:#0066cc;' class='grid_viewcoltitle' onclick='clickLineViewTitle(this)'>" + obj[this.viewButtonField] + "</a>";
}

function clickLineViewTitle(thisObj) {
	singlegrid_view__.call($(".datagrid_container").datagrid("getCurrList")[$(thisObj).closest("td").attr("rowIndex")]);
}

$(function(){
	
	resetFormWidthHeight();
	
	builddatagrid__();
	
	<%if(CreateUpdateWindowMode.DISPLAY_INLINE.equals(opts.createUpdateWindowMode)) {%>
	$("body").viewNavigator({
		firstView:$(".list_firstviewdiv")
	});
	<%}%>
	
	$(".querypartquerypage").click(function(){
		return false;
	});
	
	$("html").click(function(){
		$(".querypartquerypage").hide();
		$(".sonbtnquerybtn").css("border-bottom-width", "1px");
	});
});

function builddatagrid__() {
	
	// 重新组织 buttons
	var gridbtns = null;
	for (var btnkey in opts.buttons) {
		if (gridbtns == null)
			gridbtns = {};
		if (btnkey == "i_up" && !opts.showMoveBtns)
			continue;
		if (btnkey == "i_down" && !opts.showMoveBtns)
			continue;
		if (btnkey == "del" && !opts.showDeleteBtn)
			continue;
		if (btnkey == "update" && !opts.showUpdateBtn)
			continue;
		
		gridbtns[btnkey] = {
			click: window[opts.buttons[btnkey].click],
			visibleFunction: window[opts.buttons[btnkey].visibleFunction],
			toolTip: opts.buttons[btnkey].toolTip
		};
		
	}
	
	var selectionModetmp = null;
	if (opts.showBatchDeleteBtn) {
		selectionModetmp = "multipleRows";
	} else {
		if (opts.selectionMode) {
			selectionModetmp = opts.selectionMode;
		}
	}
	
	if ($(".datagrid_container").data('uiDatagrid')) {
		$(".datagrid_container").datagrid("destroy");
	}
	
	$(".datagrid_container").datagrid({
		dataProvider: ctx + opts.urlprefix + "/getSingleGridList.vot?" + encodeURI($(".singlegridform_query").serialize())<%=gridprovidersuffix%>,
		operationHeaderWidth:40,
		locateItemSid: lastCreatedSid,
		selectionMode:selectionModetmp,
		showSeqNum: true,
		viewButtonField: opts.viewButtonField,
		columns:opts.columns, 
		buttons:gridbtns
	});
	lastCreatedSid = "";

}

/**
 * 重新ajax加载列表上方的查询部分，允许传递参数，但要确保传参时添加问号
 */
function rebuildqueryformdiv_(params) {
	$(".condition.basicinfo").load(ctx + "/componentsv2/supportjsps/grid_querypart.jsp" + (params||""));
}

function resetFormWidthHeight() {
	
	var origwidth = $("#singlegridform").data("origwidth");
	var origheight = $("#singlegridform").data("origheight");
	if (!origwidth) {
		origwidth = $("#singlegridform").width();
		origheight = $("#singlegridform").height();
		$("#singlegridform").data("origwidth", origwidth);
		$("#singlegridform").data("origheight", origheight);
	}
	
	formwidth = origwidth + 45;
	formheight = origheight + 85;
	
	formwidth = Math.min ($(window).width() - 10, formwidth);
	formheight = Math.min ($(window).height() - 10, formheight);
}

function create() {
	<%=precreateitem%>
	
	resetFormWidthHeight();
	$("#singlegridform").resetForm();
	$("#singlegridform").setCurrentState("create");
	$("#singlegridform").trigger('openCreateForm');
	
	var dialogTopParams = getDialogTopParams();
	$("#singlegridform").submitDialog({
		title:"<%=i18n.get("single_datagrid." + component + ".add") %>",
		successTip:"<%=i18n.get("single_datagrid." + component + ".successTip") %>"||"<%=i18n.get("antelope.savesuccess") %>",
		dialogTopIframeURL: dialogTopParams.dialogTopURL,
		url:ctxPath + opts.urlprefix + "/addOrUpdateOne.vot?a=1"<%=gridprovidersuffix%>, width:dialogTopParams.finalDialogWidth, height:dialogTopParams.finalDialogHeight,
		<%=opts.showTempSaveBtn ? "showTempSave:true," : ""%>
		callback: createUpdateCplt__,
		isinline:<%=CreateUpdateWindowMode.DISPLAY_INLINE.equals(opts.createUpdateWindowMode)%>,
		dialogTopLoadedComplete: function() {
			this.resetForm();
			this.setCurrentState("create");
			this.trigger('openCreateForm');
			this.trigger('openedCreateForm');
		}
	});
	$("#singlegridform").trigger('openedCreateForm');
	
}

var lastCreatedSid = "";

function createUpdateCplt__() {
	lastCreatedSid = $("#singlegridform").find("[name=sid]").val();
	if (window['antelope_createOrUpdateComplete']) {
		if (window['antelope_createOrUpdateComplete']() === false)
			return;
	}
	read.call(this);
}

function deleteCplt__() {
	if (window['antelope_deleteComplete']) {
		if (window['antelope_deleteComplete']() === false)
			return;
	}
	read.call(this);
}

function read() {
	
	$(".querypartquerypage").hide();
	$(".sonbtnquerybtn").css("border-bottom-width", "1px");
	
	<%if (opts.locatePageBySidAfterCreate) {%>
		if (lastCreatedSid) {
			$(".datagrid_container").datagrid("option", "locateItemSid", lastCreatedSid);
			lastCreatedSid = "";
		}
	<%}%>
	
	$(".datagrid_container").datagrid("option", "dataProvider",
			ctx + opts.urlprefix + "/getSingleGridList.vot?" + encodeURI($(".singlegridform_query").serialize())<%=gridprovidersuffix%>);
}

function createFormInnerByData(data) {
	<%if (TextUtils.stringSet(opts.formKey)  || opts.formfields.length == 0){ %>
		return;
	<%}%>
	var formhtml = "";
	for (var i = 0; i < opts.formfields.length; ++i) {
		var xmlfield = opts.formfields[i].enumXmlField;
		formhtml += '<span class="cd_name">' + opts.formfields[i].label + ':</span>\
		<span class="cd_val">';
		if (xmlfield && data && data[xmlfield]) {
			formhtml += '<select name="' + opts.formfields[i].field + '"  ' +  opts.formfields[i].validate + '\
				dataProvider="' + data[xmlfield] + '" \
				state="disabled:false; disabled.view:true;"></select>';
		} else {
			formhtml += '<input name="' + opts.formfields[i].field + '" ' + opts.formfields[i].validate + ' state="disabled:false; disabled.view:true;"/>';
		}
		formhtml += '</span>';
	}
	$("#singlegridform").html(formhtml).initWidgets();
}


function singlegrid_batchmoveup__() {
	singlegrid_batchmoveupdowninner__("?isup=true");
}

function singlegrid_batchmovedown__() {
	singlegrid_batchmoveupdowninner__("");
}

function singlegrid_batchmoveupdowninner__(upstr) {
	var seledItems = $(".datagrid_container").datagrid("option", "selectedItems");
	if (seledItems.length == 0) {
		alert("<%=i18n.get("antelope.pleaseatleastselectonetomoveupdown")%>");
		return;
	}
	
	var seleditemsids = $.extractSids(seledItems);
	
	singlegrid_moveupdowninner(upstr, seleditemsids);
}


function singlegrid_moveupdowninner(upstr, seleditemsids) {
	var upddialog = $("<div><input name='updowntimes' value='1' int='true' min='1' requried2='true'/></div>");
	upddialog.dialog({
		title: "移动次数",
		buttons: {
			'确定': function() {
				if (!upddialog.validate())
					return;
				
				var updowntimes = upddialog.find("[name=updowntimes]").val();
				$.commonSubmit({
					url: ctx + opts.urlprefix + "/moveUpOrDown.vot" + upstr,
					data: "sid=" + seleditemsids + "&updowntimes=" + updowntimes,
					isAutoSave: true,
					callback: read
				});
				upddialog.dialog("destroy");
			}, '取消': function () {
				upddialog.dialog("destroy");
			}
		}
	});
	
	upddialog.find("[name=updowntimes]")[0].select();
}

function exportit() {
	$(".singlegridform_query").postIframe(ctx + opts.urlprefix + "/exportExcel.vot");
}

function importit() {
	
	var file = $("[name=importexcelfile]");
	file.after(file.clone().val(""));
	file.remove();
	
	var btns = {};
	btns['<%=i18n.get("antelope.ok")%>'] = function() {
		$("#singlegridform_imp").postIframe(ctx + opts.urlprefix + "/importExcelInner.vot", function(data){
			if (data)
				alert(data);
			else {
				alert("<%=i18n.get("antelope.impsuccess")%>");
				$("#singlegridform_imp").dialog("destroy");
				$(".datagrid_container").datagrid("refresh");
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

function onGridpartMouseOver(thisObj) { // querypartquerypage
	$(".querypartquerypage").show();
	$(thisObj).css("border-bottom-width", 0);
}

function batchDeleteLines() {
	var seledItems = $(".datagrid_container").datagrid("option", "selectedItems");
	if (seledItems.length == 0) {
		alert("<%=i18n.get("antelope.pleaseatleastselectonetodelete")%>");
		return;
	}
	
	$.submitDelete({
		url: ctx + opts.urlprefix + "/batchDeleteLines.vot?a=1"<%=gridprovidersuffix%>,
		data: "sids=" + $.extractSids(seledItems),
		callback: read
	});
}

function downloadImpTemp__() {
	$.postIframe(ctx + opts.urlprefix + "/downloadImportExcelTmpl.vot");
}

</script>
