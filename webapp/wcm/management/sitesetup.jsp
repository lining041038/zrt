<%@page import="antelope.wcm.consts.WCMSiteSettingConsts"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>

<style>
.ui-tabs .ui-tabs-panel{padding-top:25px;}
</style>

<script>
$(function(){
	$('.tabs').bind('tabsshow', function(event, ui) {
	  
	  	if (ui.index == 1) {
	  		
	  		// logoimgsid
	  		
	  	 if ($(".logofileup").data("uiFileupload")) {
	  		$(".logofileup").fileupload("destroy");	 
	  	 }
	  		
  		 $(".logofileup").fileupload({
			 	autosave : true,
				maxfilecount:1,
				maxfilesize: 100,
				enablePermanent: false,
				extension: "*.png",
				filegroupsidinput:$("[name=logoimgsid]")
		 });
  		 
	  	 if ($(".bannerlogofileup").data("uiFileupload")) {
		  	$(".bannerlogofileup").fileupload("destroy");
		 }
	  		 
	  		// web_bannersid
  		 $(".bannerlogofileup").fileupload({
			 	autosave : true,
				maxfilecount:1,
				maxfilesize: 100,
				enablePermanent: false,
				extension: "*.png",
				filegroupsidinput:$("[name=web_bannersid]")
		 });
	  		 
	  	  // navigationtree
	  	  var treenavjson = [{sid:"root", name:"主菜单", editable: false, open: true, isParent: true , nodes:[]}];
	  	  
	  	  var treenavval = $("[name=navigationtree]").val();
	  	  if (treenavval) {
	  		 treenavjson = JSON.parse(treenavval);
	  		 function setOpenAttr(nodes) {
	  			 for (var i = 0; i < nodes.length; ++i) {
	  				nodes[i].open = true;
	  				if (nodes[i].nodes) {
	  					setOpenAttr(nodes[i].nodes);
	  				}
	  			 }
	  		 }
	  		 
	  		setOpenAttr(treenavjson);
	  	  }
	  	  
	  		$("#treenav").tree({
	  			editable: true, dragMove:true, 
	  			edit_renameBtn: false,
	  			edit_removeBtn: false,
	  			dataProvider: treenavjson,
	  			contextMenu: {"新建子菜单": {
	  				click: function(event, treeId, treenode) {
	  					$("[name=newnavmenuname]").val('');
	  					$("#newsubnav").dialog({
	  						modal:true,
	  						title: '新建',
	  						width:450,
	  						buttons:{
	  							'确定': function(){
	  								$("#treenav").tree("unwrap").addNodes(treenode, [{sid: $.Guid.New(), 
	  									webpageproperty_desc: $("#newsubnav [name=webpageproperty_desc]").val(),
	  									webpageproperty: $("#newsubnav [name=webpageproperty]").val(), name: $("[name=newnavmenuname]").val()}]);
	  								$("#newsubnav").dialog("destroy");
	  							}, '取消': function(){
	  								$("#newsubnav").dialog("destroy");
	  							}
	  						}
	  					});
	  				}
	  			}, "修改": {
	  				click: function(event, treeId, treenode) {
	  					
	  					if (!treenode.parentNode) {
	  						alert("主菜单根节点不允许修改");
	  						return;
	  					}
	  					
	  					$("[name=newnavmenuname]").val(treenode.name);
	  					$("#newsubnav [name=webpageproperty_desc]").val(treenode.webpageproperty_desc);
						$("#newsubnav [name=webpageproperty]").val(treenode.webpageproperty);
	  					
	  					$("#newsubnav").dialog({
	  						width:450,
	  						modal:true,
	  						title: '修改',
	  						buttons: {
	  							'确定': function(){
	  								treenode.name = $("[name=newnavmenuname]").val();
	  								treenode.webpageproperty_desc = $("#newsubnav [name=webpageproperty_desc]").val();
	  								treenode.webpageproperty = $("#newsubnav [name=webpageproperty]").val();
	  								
	  								$("#treenav").tree("unwrap").updateNode(treenode);
	  								$("#newsubnav").dialog("destroy");
	  							}, '取消': function(){
	  								$("#newsubnav").dialog("destroy");
	  							}
	  						}
	  					});
	  				}
	  			}, "删除": {
	  				click: function(event, treeId, treenode) {
	  					
	  					if (!treenode.parentNode) {
	  						alert("主菜单根节点不允许删除");
	  						return;
	  					}
	  					
	  					$("#treenav").tree("unwrap").removeNode(treenode);
	  				}
	  			}}
	  		});
	  	}
	  	
	  	
	  	if (ui.index == 2) {
	  		
	  		// web_indexportlets
	  		var portletprovider = [];
	  		var indexplets = $("#indexpageset [name=web_indexportlets]").val();
	  		if (indexplets) {
	  			portletprovider = JSON.parse(indexplets); 
	  		}
	  		
	  		if ($("#portletsgrid").data("uiDatagrid")) {
	  			$("#portletsgrid").datagrid("destroy");
	  		}
	  		
	  		$("#portletsgrid").datagrid({
	  			columns: {"title":"标题", "portlettype": {headerText:"类型", enumXml:"sys_wcm_portlettype"}, "position": {headerText:"位置", enumXml:"sys_wcm_position"}, "sortfield": "排序"},
	  			buttons: {
	  				'update': function() {
	  					
	  					var thisObj = this;
	  					
	  					$("#indexpageportlet").resetForm(thisObj).dialog({
	  						modal:true,
	  						title: '编辑',
	  						width:473,
	  						buttons: {
	  							'确定': function(){
	  								var provider = $("#portletsgrid").datagrid("option", "dataProvider");
	  								var formdataarr = $("#indexpageportlet").serializeArray();
	  								for (var i = 0; i < formdataarr.length; ++i) {
	  									thisObj[formdataarr[i].name] = formdataarr[i].value;
	  								}
	  								 $("#portletsgrid").datagrid("option", "dataProvider", provider);
	  								$("#indexpageportlet").dialog("destroy");
	  								
	  								$("#indexpageset [name=web_indexportlets]").val(JSON.stringify(provider));
	  							}, '取消': function(){
	  								$("#indexpageportlet").dialog("destroy");
	  							}
	  						}
	  					});
	  					
	  				},'del': function() {
	  					$("#portletsgrid").datagrid("removeRow", this.sid);
						$("#indexpageportlet").dialog("destroy");
						
						$("#indexpageset [name=web_indexportlets]").val(JSON.stringify($("#portletsgrid").datagrid("option", "dataProvider")));
	  				}
	  			},
	  			
	  			dataProvider: portletprovider
	  		});
	  	}
	  	
	  	
	  	if(ui.index==3){
	  		
	  		var indexplets = $("#indeximgset [name=web_imgportlets]").val();
	  		if (indexplets) {
	  			portletprovider = JSON.parse(indexplets); 
	  		}
	  		
	  		if ($("#imgsgrid").data("uiDatagrid")) {
	  			$("#imgsgrid").datagrid("destroy");
	  		}
	  		
	  		$("#imgsgrid").datagrid({
	  			columns: {"title":"标题"},
	  			buttons: {
	  				'update': function() {
	  					
	  					var thisObj = this;
	  					
	  					$("#indeximgportlet").resetForm(thisObj).dialog({
	  						modal:true,
	  						title: '编辑',
	  						width:473,
	  						height:210,
	  						buttons: {
	  							'确定': function(){
	  								var provider = $("#imgsgrid").datagrid("option", "dataProvider");
	  								var formdataarr = $("#indeximgportlet").serializeArray();
	  								for (var i = 0; i < formdataarr.length; ++i) {
	  									thisObj[formdataarr[i].name] = formdataarr[i].value;
	  								}
	  								$("#imgsgrid").datagrid("option", "dataProvider", provider);
	  								$("#indeximgportlet").dialog("destroy");
	  								
	  								$("#indexpageset [name=web_imgportlets]").val(JSON.stringify(provider));
	  								$.post(ctx + "/wcm/management/sitemanage/SiteManageController/permanentfiles.vot", encodeURI($("#indeximgportlet").serialize()), function(){
	  								});
	  							},
	  							'取消': function(){
	  								$("#indeximgportlet").dialog("destroy");
	  							}
	  						}
	  					});
	  					
	  			  		if ($(".indexfileup").data("uiFileupload")) {
	  			  			$(".indexfileup").datagrid("destroy");
	  			  		}
	  					
	  					$(".indexfileup").fileupload({
	  					 	autosave : true,
	  						maxfilecount:1,
	  						maxfilesize: 500,
	  						enablePermanent: false,
	  						extension: "*.png;*.jpg",
	  						filegroupsidinput:$("[name=indeximgsid]")
	  					 });
	  					
	  					
	  				},'del': function() {
	  					$("#imgsgrid").datagrid("removeRow", this.sid);
						$("#indeximgportlet").dialog("destroy");
						
						$("#indexpageset [name=web_imgportlets]").val(JSON.stringify($("#imgsgrid").datagrid("option", "dataProvider")));
	  				}
	  			},
	  			dataProvider: portletprovider
	  		});
	  		
	  	}

	});
});

function selectTemplate() {
	$.selectByTile({
		component: "tileselecttemplate",
		width:800,
		height:800,
		callback: function(data) {
			if (data.length == 0) {
				alert("请选择一个模板");
				return;
			}
			$("#basicinfo [name=templatename]").val(data[0].name);
			$("#basicinfo [name=templatesid]").val(data[0].sid);
			return true;
		}
	});
}

function saveAllSettings(sitesid, callback) {
	
	if (!$(".tabs").validate()) {
		return;
	}
	
	// navigationtree
	if ($("#treenav").html()) {
		var nodesdata = $("#treenav").tree("unwrap").getNodes();
		var newnodedata = cloneNewNode(nodesdata);
		$("[name=navigationtree]").val(JSON.stringify(newnodedata));
	}

	// web_indexportlets
	if ($("#portletsgrid").html()) {
		$("#indexpageset [name=web_indexportlets]").val(JSON.stringify($("#portletsgrid").datagrid("option", "dataProvider")));
	}
	
	
	if ($("#imgsgrid").html()) {
		$("#indeximgset [name=web_imgportlets]").val(JSON.stringify($("#imgsgrid").datagrid("option", "dataProvider")));
	}
	
	var sitesidstr = "";
	if (sitesid) {
		sitesidstr = "?sid=" + sitesid;
	}
	
	$.post(ctx + "/wcm/management/sitemanage/SiteManageController/addOrUpdateSiteSettings.vot" + sitesidstr, encodeURI($(".tabs").serialize()), function(){
		alert("保存成功");
		callback();
	});
	
}

function cloneNewNode(nodesdata) {
	var newnodes = [];
	for (var i = 0; i < nodesdata.length; ++i) {
		var onenewnode = {};
		for (var key in nodesdata[i]) {
			if (key == 'parentNode')
				continue;
			
			if (key == 'nodes') {
				onenewnode[key] = cloneNewNode(nodesdata[i][key]);
				continue;
			}
			
			if (/(sid|name|webpageproperty_desc|webpageproperty)/.test(key))
				onenewnode[key] = nodesdata[i][key];
		}
		
		newnodes.push(onenewnode);
	}
	
	return newnodes;
}

function resetAllSettings(settingobj) {
	$(".tabs").resetForm(settingobj);
}


function createOnePortlet() {
	$("#indexpageportlet").resetForm().dialog({
		modal:true,
		title: '添加',
		width:473,
		buttons: {
			'确定': function(){
				var provider = $("#portletsgrid").datagrid("option", "dataProvider");
				
				// columns: {"title":"标题", "portlettype": "类型", "position": "位置", "sortfield": "排序"},
				var formdataarr = $("#indexpageportlet").serializeArray();
				var onegridline = {};
				for (var i = 0; i < formdataarr.length; ++i) {
					onegridline[formdataarr[i].name] = formdataarr[i].value; 
				}
				
				provider.push(onegridline);
				 $("#portletsgrid").datagrid("option", "dataProvider", provider);
				
				$("#indexpageportlet").dialog("destroy");
				
				$("#indexpageset [name=web_indexportlets]").val(JSON.stringify(provider));
			}, '取消': function(){
				$("#indexpageportlet").dialog("destroy");
			}
		}
	});
}

function selectCates() {
	
	var compname = "productcateselect"
	if (/^[01]$/.test($("#indexpageportlet [name=portlettype]").val()))
		compname = "articlecateselect";
	
	if (/^[4]$/.test($("#indexpageportlet [name=portlettype]").val()))
		compname = "articleselect";
	
	var sids = $.extractSids(JSON.parse($("#indexpageportlet [name=portletcontent_values]").val()));
	
	if (/^[4]$/.test($("#indexpageportlet [name=portlettype]").val())){
		$.selectByTreeList({
			component: compname,
			callback: function(data){
				$("#indexpageportlet [name=portletcontent_values]").val(JSON.stringify(data));
				$("#indexpageportlet [name=portletcontent_names]").val($.extractField(data, "name"));
				return true;
			},
			selectedItemSids: sids// 数组或逗号分割的已选中项sid列表
		});
	} else {
		$.selectByTree({
			component: compname,
			callback: function(data){
				$("#indexpageportlet [name=portletcontent_values]").val(JSON.stringify(data));
				$("#indexpageportlet [name=portletcontent_names]").val($.extractField(data, "name"));
				return true;
			},
			selectedItemSids: sids// 数组或逗号分割的已选中项sid列表
		});
	}
	
}

function portlettypeChange(thisObj) {
	$("#indexpageportlet [name=portletcontent_values]").val("[]");
	$("#indexpageportlet [name=portletcontent_names]").val("");
}


function selectPageProperty() {
	$.selectByTreeList({
		component:"pagepropertyselect",
		selectionMode:"singleRow",
		data: "sitesid=" + $(".tabs [name=sid]").val(),
		callback:function(data) {
			if (data.length == 0) {
				alert("请选择网页属性！");
				return false;
			}
			
			//<input name="webpageproperty_desc" required="true"/>
			//<input name="webpageproperty" required="true"/>
			$("#newsubnav [name=webpageproperty_desc]").val("(" + data[0].typename + ")" + data[0].name);
			$("#newsubnav [name=webpageproperty]").val(JSON.stringify(data));
			return true;
		}
	});
}



//首页幻灯片

function createImagePortlet(){
	$("#indeximgportlet").resetForm().dialog({
		modal:true,
		title: '添加',
		width:473,
		buttons: {
			'确定': function(){
				var provider = $("#imgsgrid").datagrid("option", "dataProvider");
				
				// columns: {"title":"标题", "portlettype": "类型", "position": "位置", "sortfield": "排序"},
				var formdataarr = $("#indeximgportlet").serializeArray();
				var onegridline = {};
				for (var i = 0; i < formdataarr.length; ++i) {
					onegridline[formdataarr[i].name] = formdataarr[i].value; 
				}
				
				provider.push(onegridline);
				 $("#imgsgrid").datagrid("option", "dataProvider", provider);
				
				$("#indeximgportlet").dialog("destroy");
				$.post(ctx + "/wcm/management/sitemanage/SiteManageController/permanentfiles.vot", encodeURI($("#indeximgportlet").serialize()), function(){
				});
				$("#indeximgset [name=web_imgportlets]").val(JSON.stringify(provider));
			}, 
			'取消': function(){
				$("#indeximgportlet").dialog("destroy");
			}
		}
	});
	
	if ($(".indexfileup").data("uiFileupload")) {
  			$(".indexfileup").datagrid("destroy");
  		}
	
	$(".indexfileup").fileupload({
		 	autosave : true,
			maxfilecount:1,
			maxfilesize: 500,
			enablePermanent: false,
			extension: "*.png;*.jpg",
			filegroupsidinput:$("[name=indeximgsid]")
	 });
	
	
}


function permanentfiles() {
	if ($("#indeximgportlet").validate()) {
		$("#indeximgportlet").submitForm({
			url:ctx + "/demo/setFilePermanent.vot"
		});
	}
}

function selectimgCates() {
	$.selectByTreeList({
		component:"pagepropertyselect",
		selectionMode:"singleRow",
		data: "sitesid=" + $(".tabs [name=sid]").val(),
		callback:function(data) {
			if (data.length == 0) {
				alert("请选择网页属性！");
				return false;
			}
			
			$("#indeximgportlet [name=portletcontent_names]").val("(" + data[0].typename + ")" + data[0].name);
			$("#indeximgportlet [name=portletcontent_values]").val(JSON.stringify(data));
			$("#indeximgportlet [name=title]").val(data[0].name);
			return true;
		}
	});
}
</script>
</head>
<body style="height: 100%;overflow: auto;">

<form class="tabs">
	<ul id="tabsul">
		<li><a href="#basicinfo">基础信息</a></li>
		<li><a href="#basicpageset">页面设置</a></li>
		<li><a href="#indexpageset">首页模块设置</a></li>
		<li><a href="#indeximgset">首页幻灯片设置</a></li>
	</ul>
	<div id="basicinfo" class="basictabdiv">
		<div class="condi_container">
			<span class="cd_name">名称：</span>
			<span class="cd_val">
				<input name="name" required2="true"/>
			</span>
		</div>
		<div class="condi_container">
			<span class="cd_name">模板：</span>
			<span class="cd_val">
				<input name="templatename" readonly="readonly" required2="true"/>
				<input class="smallbutton" type="button" style="width: 90px" value="选择模板" onclick="selectTemplate()" />
				<input name="templatesid" type="hidden"/>
			</span>
		</div>
		<div class="condi_container">
			<span class="cd_name">域名：</span>
			<span class="cd_val">
				<input name="domainname" required2="true" value="localhost" default="localhost"/>
			</span>
		</div>
	</div>
	<div id="basicpageset" class="basictabdiv">
		<div class="condi_container">
			<span class="cd_name">LOGO图片：</span>
			<span class="cd_val">
				<input name="<%=WCMSiteSettingConsts.LOGO_IMG_SID %>" type="hidden"/>
				<span class="logofileup" style="display: inline-block; width: 100px;"></span>
			</span>
		</div>
		
		<div class="condi_container">
			<span class="cd_name">导航结构(右键编辑操作)：</span>
			<span class="cd_val">
				<input name="<%=WCMSiteSettingConsts.NAVIGATION_TREE %>" type="hidden" value='[{"sid":"root", "name":"主菜单", "editable": false, "open": true, "isParent": true, "nodes":[] }]' 
				default='[{"sid":"root", "name":"主菜单", "editable": false, "open": true, "isParent": true , "nodes":[]}]'/>
				<ul id="treenav" class="tree" style="margin-left: 100px;">
				</ul>
			</span>
		</div>
		
		<div class="condi_container">
			<span class="cd_name">banner图片：</span>
			<span class="cd_val">
				<input name="<%=WCMSiteSettingConsts.WEB_BANNERSID %>" type="hidden"/>
				<span class="bannerlogofileup" style="display: inline-block; width: 100px;"></span>
			</span>
		</div>
		
		<div class="condi_container">
			<span class="cd_name" style="position: relative; top: -70px;">网站顶部信息：</span>
			<span class="cd_val">
				<textarea name="<%=WCMSiteSettingConsts.WEB_HEADERINFO %>" style="width: 459px; height: 134px;"></textarea>
			</span>
		</div>
		
		<div class="condi_container">
			<span class="cd_name" style="position: relative; top: -70px;">网站底部信息：</span>
			<span class="cd_val">
				<textarea name="<%=WCMSiteSettingConsts.WEB_FOOTERINFO %>" style="width: 459px; height: 134px;"></textarea>
			</span>
		</div>
	</div>
	
	
	<div id="indexpageset" class="basictabdiv">
		<input name="web_indexportlets" type="hidden"  value="[]" default="[]"/>
		<div id="portletsgrid"></div>
		<div class="func_span" style="margin-top: 20px; white-space:nowrap;">
			<input class="button" type="button" title="新建" onClick="createOnePortlet()" value="新建">
		</div>
	</div>
	
	<div id="indeximgset" class="basictabdiv">
		<input name="web_imgportlets" type="hidden"  value="[]" default="[]"/>
		<div id="imgsgrid"></div>
		<div class="func_span" style="margin-top: 20px; white-space:nowrap;">
			<input class="button" type="button" title="新建" onClick="createImagePortlet()" value="新建">
		</div>
	</div>
	
</form>


<!-- 导航 -->
<form id="newsubnav" style="display: none;">
	<div class="condi_container">
		<span class="cd_name">名称：</span>
		<span class="cd_val">
			<input name="newnavmenuname" required="true"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">网页属性：</span>
		<span class="cd_val">
			<input name="webpageproperty_desc" required2="true" readonly="readonly"/>
			<input name="webpageproperty" type="hidden"/>
			<button class="smallbutton" onclick="selectPageProperty()">选择</button>
		</span>
	</div>
</form>


<!-- 首页模块  -->
<form id="indexpageportlet" style="display: none; width:473px;">
	<div class="condi_container">
		<span class="cd_name">标题：</span>
		<span class="cd_val">
			<input name="title" required2="true"/>
		</span>
	</div>
	
	<div class="condi_container">
		<span class="cd_name">模块类型：</span>
		<span class="cd_val">
			<select name="portlettype" dataProvider="sys_wcm_portlettype" onchange="portlettypeChange(this)"></select>
		</span>
	</div>	
	
	<div class="condi_container">
		<span class="cd_name">位置：</span>
		<span class="cd_val">
			<select name="position" dataProvider="sys_wcm_position"></select>
		</span>
	</div>
	
	<div class="condi_container">
		<span class="cd_name">显示条数：</span>
		<span class="cd_val">
			<input name="shownum" int="true" maxlength2="2" max="50" required2="true"/>
		</span>
	</div>	
	
	<div class="condi_container">
		<span class="cd_name">排序：</span>
		<span class="cd_val">
			<input name="sortfield" int="true" maxlength2="5" required2="true"/>
		</span>
	</div>
	
	<div class="condi_container">
		<span class="cd_name">模块内容：</span>
		<span class="cd_val">
			<input name="portletcontent_names" default="" readonly="readonly"/> 
			<input name="portletcontent_values" value="[]" default="[]" type="hidden"/> <button class="smallbutton" onclick="selectCates()">选择</button>
		</span>
	</div>
</form>

<!-- 首页幻灯片  -->
<form id="indeximgportlet" style="display: none; width:473px;">
	<div class="condi_container">
		<span class="cd_name">标题：</span>
		<span class="cd_val">
			<input name="title" required2="true"/>
		</span>
	</div>
	
	<div class="condi_container">
			<span class="cd_name">幻灯片图片：</span>
			<span class="cd_val">
				<input name="indeximgsid" type="hidden"/>
				<span class="indexfileup" style="display: inline-block; width: 100px;"></span>
			</span>
	</div>
	
	
	<div class="condi_container">
		<span class="cd_name">模块内容：</span>
		<span class="cd_val">
			<input name="portletcontent_names" default="" readonly="readonly"/> 
			<input name="portletcontent_values" value="" default="" type="hidden"/> 
			<button class="smallbutton" type="button" onclick="selectimgCates()">选择</button>
		</span>
	</div>
	
	<input name="createtime" default="<%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())%>" type="hidden"/>
	
</form>

</body>
</html>






