<%--
	title:浮动行报表模板组件的封装
	author:lining
--%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>

<style>
#feedback {font-size:1.4em}
.col_set ol{border-width: 1px 0 0 1px; margin: 0; padding: 0;}
.ui-selecting { background: #FECA40; }
.ui-selected { background: #F39814; color: white; }
.col_set { list-style-type: none; margin: 0; padding: 3px; }
.col_set li{ margin: 0; font-size: 1.4em; height: 18px; border-width:0px 1px 0 0;}
.col_set li{float: left;} 
.ui-state-default{background-image: none; border-color:black; }

/*字典组*/
#dictitems li{margin-top: 5px;}

#iconbtn .ui-icon{cursor: pointer;}
</style>

<script>

// 获取参数 有plugins 功能插件 有 groupsid 标志模板分组
<% String options = request.getParameter("options");%>
var options = <%=TextUtils.stringSet(options)?URLDecoder.decode(options, "utf-8"):"{}"%>;


var edittypebuttons = [{
	value:'dict', label:'可选项'
}, {value:'text', label:'可写文本'
}, {value:'num', label:'可写数字'
}, {value:'date', label:'可选日期'
}, {value:'file', label:'可传附件'
}];

$(function(){
	readTmplList();
	// 根据参数初始化单元格状态设置按钮
	if (options.plugins) {
		edittypebuttons = $.merge(edittypebuttons, options.plugins);
	}
	
	// 拼接按钮列
	var statebuttonhtml = "";
	var i = 0;
	for (i = 0; i < edittypebuttons.length; i++) {
		// 区分编辑类型和汇总分析类型
		if (edittypebuttons[i].issum) {
			statebuttonhtml  += '<input type="checkbox" name="sumtype" onclick="addDictRelate(this, '+i+')" value="'+edittypebuttons[i].value+'" id="check'+i+'"/><label for="check'+i+'">'+edittypebuttons[i].label+'</label> ';
		} else {
			statebuttonhtml  += '<input type="checkbox" name="edittype" onclick="addDictRelate(this, '+i+')" value="'+edittypebuttons[i].value+'" id="check'+i+'"/><label for="check'+i+'">'+edittypebuttons[i].label+'</label> ';
		}
	}
	
	// 单元格设置状态按钮组
	$("#setstatebtn").html(statebuttonhtml).buttonset();
	
	// 初始化组标识
	if (options.groupsid)
		$("[name=groupsid]").val(options.groupsid);
	
	
	// 初始化新增或修改字典界面
	$("#dictitems").selectable();
	$("#dictitems").delegate("li","dblclick", function() {
		var self = $(this);
		var input = $("<input style='width:100%; height:100%; border:0;'/>").val(self.text()).blur(function(){
			$(this).parent().text($(this).val());
		}).keyup(function(e){
			if (e.keyCode == 13)
				$(this).parent().text($(this).val());
		}).dblclick(function(e) {
			e.stopPropagation();
		});
		self.empty().append(input);
		input.select();
	});
});

// 加载模板列表
function readTmplList() {
	$.getJSON(ctxPath + "/floatreport/getFloatReportTmpls.vot?groupsid="+(options.groupsid||''), function(data){
		$(".datagrid_container").datagrid("destroy").datagrid({
			dataProvider:data,
			columns:[{
				dataField:'isrelease', headerText:'是否发布', width:'10%', labelFunction:function(obj){
					return ['未发布','<a href="#">已发布</a>'][obj.isrelease || 0];
				}
			},{
				dataField:'title', headerText:'标题', width:'80%'
			}],
			buttons:{
				'query': function() {
					var thisObj = this;
					$.getJSON(ctxPath + "/floatreport/gettmplinfo.vot?sid=" + this.sid, function(data) {
						$("[name=tmplneweditform]").resetForm(thisObj);
						resethtmltable(data);
						openTmplEditDialog(thisObj.isrelease);
					});
				}, 'release': {
					visibleFunction: function () {
						return this.isrelease != 1;
					}, click: function() {
						if (confirm("模板发布之后将不允许重新修改，确认要发布吗？")) {
							$.get(ctxPath + "/floatreport/release.vot?sid=" + this.sid, function(data){
								if (data) {
									alert(data);
									return;
								}
								alert("发布成功！");
								readTmplList();
							});
						}
					}
				}
			}
		});
	});
}


function create() {
	$("[name=tmplneweditform]").resetForm();
	$("#reportheadertr,#tbcells").empty();
	openTmplEditDialog();
}

// 对htmltable进行整理，仅留下有用的标记，去除无用的标记
function tidyhtmltable(htmltable) {
	var jqtable = htmltable;
	// 去掉不必要的css信息
	jqtable.find("*").removeClass("ui-selectee ui-selected");
	// 为表格单元格添加属性信息，以确保单元格设置得以保存
	jqtable.find("td").each(function(){
		var self = $(this);
		var data = self.data();
		for (var key in data) {
			if (/dict|edittype|sumtype/.test(key)) {
				if ($.isPlainObject(data[key])) {
					self.attr(key, encodeURI(JSON.stringify(data[key])));
				} else
					self.attr(key, data[key]);
			}
		}
	});
	
	jqtable.find("td").removeAttr("selectableItem");
	
	return jqtable[0].outerHTML;
}

// 打开模板编辑对话框
function openTmplEditDialog(isrelease) {
	
	var dialogbtn = {
			// 最终保存模板相关信息
			'确定':function(){
				if ($("[name=tmplneweditform]").validate()) {
					var htmltable = encodeURIComponent(encodeURIComponent(tidyhtmltable($(".floatreptable").clone(true))));
					$.post(ctxPath + "/floatreport/addnewtmpl.vot?", encodeURI($("[name=tmplneweditform]").serialize())+"&htmltable="+htmltable+"&groupsid="+(options.groupsid||''), function(){
						$("#creatediv").dialog("destroy");
						readTmplList();
					});
					$("#creatediv").dialog("destroy");
				}
			},'取消':function() {
				$("#creatediv").dialog("destroy");
			}
		};
	
	// 若已发布，则不允许再次编辑
	if (isrelease) {
		$("#statebuttonbar").hide();
		$("[name=tmplneweditform] [name=title]").prop("readonly", true);
		dialogbtn = {'关闭':function(){
			$("#creatediv").dialog("destroy");
		}};
	} else {
		$("[name=tmplneweditform] [name=title]").prop("readonly", false);
		$("#statebuttonbar").show();
	}
	
	// 初始化模板编辑对话框按钮
	$("[name=edittype]").prop("checked", false).button('refresh');
	
	$("#creatediv").dialog({
		buttons: dialogbtn, width:$(window).width() - 30, height:$(window).height() - 30, title:"新增模板", close:function (){
			$("#creatediv").dialog("destroy");
		}
	});
	
	var guid;
	$("#uploadexcel").loadFileUpDownloader ({wmode:"transparent", htmlText:"<font color='#005590' face='宋体'><u>上传excel</u></font>", single:true, extension:"*.xls"}, function(){
		this.selectFile (function (filelist) {
			guid = $.Guid.New();
			return ctxPath + "/floatreport/uploadexcel.vot?guid=" + guid;
		}).uploadComplete (function (info) {
			$.getJSON(ctxPath + "/floatreport/getcolinfoarr.vot?guid=" + guid, function(data) {
				resethtmltable(data);
			});
		});
	});
}

function resethtmltable(data) {
	$("#reportheadertr,#tbcells").empty();
	$(data).each(function(){
		var attrs = "";
		if (this.edittype) {
			attrs += " edittype='"+this.edittype+"' ";
		}
		
		if (this.edittype == 'dict') {
			attrs += " dict='"+this.dictsid+"' ";
		}
		
		if (this.sumtype) {
			attrs += " sumtype='"+this.sumtype+"' ";
		}
		
		var thedata = {edittype: this.edittype, sumtype: this.sumtype};
		
		if (this.edittype == 'dict') {
			thedata.dict = {title:this.title, sid:this.dictsid, selectmode:1};
		}

		$("#reportheadertr").append($("<th style='width:"+this.colwidth+"px'>" + this.coltitle + "</th>"));
		$("#tbcells").append($("<td  "+attrs+" width='"+this.colwidth+"' style='width:"+this.colwidth+"px'></td>").data(thedata));
	});
	
	$("#tbcells").selectable({
		stop: function() {
			var edittype;
			var sumtype;
			var edittypenotsame = false;
			var sumtypenotsame = false;
			$( ".ui-selected", this ).each(function(index) {
				if(index == 0) {
					edittype = $(this).data("edittype");
					sumtype = $(this).data("sumtype");
				} else {
					if (edittype != $(this).data("edittype")) {
						edittypenotsame = true;
					}
					if (sumtype != $(this).data("sumtype")) {
						sumtypenotsame = true;
					}
				}
			});
			
			$("[name=edittype], [name=sumtype]").prop("checked", false).button('refresh');
			if (!edittypenotsame) 
				$("[name=edittype]").filter("[value="+edittype+"]").prop("checked", true).button("refresh");
			if (!sumtypenotsame) 
				$("[name=sumtype]").filter("[value="+sumtype+"]").prop("checked", true).button("refresh");
			
			resetTableCellInfo();
		}
	});
	resetTableCellInfo();
}

// 更改单元格可编辑状态
function addDictRelate(thisObj, buttonindex) {
	
	var funcbtnobj = edittypebuttons[buttonindex];
	
	var checked = $(thisObj).prop("checked");
	
	// 非汇总类型不需要刷新汇总类型按钮
	if (!funcbtnobj.issum) {
		// 重置按钮状态
		$("[name=edittype]").prop("checked", false).button("refresh");
		$(thisObj).prop("checked", checked).button("refresh");
	}
	
	// 选中的表格单元格
	var selectedcell = $(".floatreptable .ui-selected");
	
	if (funcbtnobj.issum) { // 如果为插件，并且为汇总类型则特殊处理,直接保存类型所对应的功能按钮数据
		if (checked)
			selectedcell.data("sumtype", funcbtnobj);
		else
			selectedcell.removeData("sumtype");
	} else {
		if (thisObj.value == 'dict') {
			if (checked) {
				readDictlist();
				$("#dictlistdialog").dialog({
					buttons:{
						'确定':function() {
							var selecteddict = $("#dictlist").datagrid("option","selectedItem");
							if (selecteddict == null) {
								alert("请选择一个可选项");
								return;
							}
							// 对被选中的单元格进行设置
							selectedcell.data("dict", selecteddict);
							selectedcell.data("edittype", thisObj.value);
							$("#dictlistdialog").dialog("destroy");
							resetTableCellInfo();
						}, '取消':function() {
							$(thisObj).attr("checked", false).button("refresh");
							$("#dictlistdialog").dialog("destroy");
							resetTableCellInfo();
						}
					}, width:800, height:400, modal:true
				});
			} else {
				selectedcell.removeData("dict");
			}
		} else { 
			// 若选择的是其他编辑方式，则去掉挂接的dict
			selectedcell.removeData("dict");
			if (checked)
				selectedcell.data("edittype", thisObj.value);
			else
				selectedcell.removeData("edittype");
		}
	}
	resetTableCellInfo();
}

// 为选中的单元格添加反馈状态
function resetTableCellInfo() {
	$(".floatreptable td").each(function(){
		var self = $(this);
		self.empty();
		var edittype = self.data("edittype"); // 编辑类型
		var sumtype = self.data("sumtype"); // 汇总类型
		// 字典可选项
		if (edittype != null) {
			if (edittype == 'dict') {
				var dict = self.data("dict");
				if (dict) {
					self.append("<div>"+dict.title+"("+(['','单选','复选','下拉'][dict.selectmode])+")"+"</div>");
				}
			} else {
				// 根据按钮设置状态值
				var label = "";
				for (var i = 0; i < edittypebuttons.length; i++) {
					if (edittypebuttons[i].value == edittype) {
						label = edittypebuttons[i].label;
						break;
					}
				}
				self.append("<div>"+label+"</div>");
			}
		}
		if (sumtype != null) { // 汇总类型展示方式
			if (typeof sumtype != 'object')
				sumtype = $.parseJSON(sumtype);
			self.append("<div>"+sumtype.label+"</div>");
		}
		
	});
}

// 重加载字典列表 
function readDictlist() {
	$.getJSON(ctxPath + "/report/getdicts.vot", function(data){
		$("#dictlist").datagrid("destroy").datagrid({
			dataProvider:data,
			columns: {title:'可选项组名称'},
			selectionMode: 'singleRow',
			buttons:{
				'query':function() {
					$.getJSON(ctxPath + "/report/getdictiteminfo.vot?sid="+this['sid'], function(data){
						$("[name=dict_title]").val(data.dict.title);
						$("[name=dict_sid]").val(data.dict.sid);
						$("[name=selectmode]").attr("checked", false).filter("[value="+data.dict.selectmode+"]").attr("checked", true);
						$("#dictitems").empty();
						$(data.dictitems).each(function(){
							$("<li class='ui-widget-content' style='width:98%;'></li>").text(this.title).appendTo("#dictitems");
						});
						openNewOrEditDictDialog();
					});
				}
			}
		});
	});
}

function addNewDict() {
	
	// 清空
	$("#dictitems").empty();
	$("[name=newdictform] :input:not(:radio)").val('');
	openNewOrEditDictDialog();
}

// 打开新增或修改字典界面
function openNewOrEditDictDialog() {
	$("#addnewdictdialog").dialog({
		buttons:{
			'确定':function() {
				// 新增字典项
				var dictitemtitles = [];
				$("#dictitems li").each(function(){
					dictitemtitles.push($(this).text());
				});
				var dicttitles = encodeURIComponent(encodeURIComponent(JSON.stringify(dictitemtitles)));
				$.post(ctxPath + "/report/addOrUpdatenewdict.vot?dicttitles="+dicttitles, encodeURI($("[name=newdictform]").serialize()), function(data){
					if (data) {
						alert(data)
						return;
					}
					readDictlist();
					$("#addnewdictdialog").dialog("destroy");
				});
			},'取消':function() {
				$("#addnewdictdialog").dialog("destroy");
			}
		}, width:305, height:403, modal:true
	});
}

function addOneDictItem() {
	$("#dictitems").append("<li class='ui-widget-content' style='width:98%;'></li>");
}

// 删除字典项目
function deleteDictItems() {
	$( ".ui-selected", "#dictitems" ).remove();
}

// 上移字典项目
function moveupDictItems() {
	if (checkCanmove()) {
		$( ".ui-selected", "#dictitems" ).insertAfter($( ".ui-selected", "#dictitems" ).next());
	}
}
// 下移字典项目
function movedownDictItems() {
	if (checkCanmove()) {
		$( ".ui-selected", "#dictitems" ).insertBefore($( ".ui-selected", "#dictitems" ).prev());
	}
}

// 判断是否允许移动项目！
function checkCanmove() {
	if ($( ".ui-selected", "#dictitems" ).size() != 1) {
		alert("请选择单个项目进行移动！");
		return false;
	}
	return true;
}


</script>
</head>
<body class="sm_main">
	<!-- 添加或修改字典项目对话框内容 -->
	<div id="addnewdictdialog" style="display: none;">
		<form name="newdictform">
			<div>
				标题：<input name="dict_title"/><input name="dict_sid" type="hidden"/>
			</div>
			<div style="display:none;">
				<span>选择模式：</span>
				<input type="radio" name="selectmode" value="1" checked="checked"/><label>单选</label>
				<input type="radio" name="selectmode" value="2"/><label>复选</label>
				<input type="radio" name="selectmode" value="3"/><label>下拉列表</label>
			</div>
			<div id="iconbtn" style="height: 20px;">
				<span class="ui-icon ui-icon-plusthick" onclick="addOneDictItem()" style="float: left;"></span>
				<span class="ui-icon ui-icon-minusthick" onclick="deleteDictItems()" style="float: left;"></span>
				<span class="ui-icon ui-icon-triangle-1-s" onclick="moveupDictItems()" style="float: right;"></span>
				<span class="ui-icon ui-icon-triangle-1-n" onclick="movedownDictItems()" style="float: right;"></span>
			</div>
			<ul id="dictitems"></ul>
		</form>
	</div>
	
	<!-- 字典列表对话框内容 -->
	<div id="dictlistdialog" style="display:none;">
		<div id="dictlist"></div>
		<div style="text-align: right;">
			<input type="button" onclick="addNewDict()" class="button" value="添加"/>
		</div>
	</div>
	
	<!-- 添加或修改模板对话框内容 -->
	<div id="creatediv" style="display: none;">
		<form name="tmplneweditform">
			<div>
				标题：<input name="title" required="true" maxlength="50"/><input name="sid" type="hidden"/>
			</div>
		</form>
		<div id="statebuttonbar">
			<span id="setstatebtn"></span>
			<span style="display: inline-block; border: 1px solid gray; padding-right: 2px;">
				<span id="uploadexcel" style="display: inline-block; width: 70px; height: 20px;"></span>
			</span>
		</div>
		<div id="htmltable_container" class="col_set">
			<table class="floatreptable floatdatagrid" style="table-layout: fixed;">
				<tr id="reportheadertr"></tr>
				<tr id="tbcells"></tr>
			</table>
		</div>
	</div>
	
	<!-- 模板列表 -->
	<div>
		<h3 class="smm_title">报表模板<span class="m_head" id="questionlist"></span><span class="m_line"></span></h3>
		<div class="datagrid_container"></div>
		<div class="pagenav_div">
			<div class="func_span">
				<input onclick="create()" class="func_btn" type="button" value="新增模板">
			</div>
		</div>
	</div>
</body>
</html>