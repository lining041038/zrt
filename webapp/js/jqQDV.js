// author daijun, 2011-9-9
// QDV means Quick Develop Version

//流程快速开发.新建业务对象区段.开始 =====================
// 弹出新建业务表单对话框
function qdv_showCreateBOPage(event){
	//清空表单
	$('#qdv_createBOForm').resetForm();
	adv_loadData("","create");
	var url = event.data.url; 
	
	$('#qdv_createBOForm').dialog({
		    title:"创建",
	        width: 930,
	        height: 420,
	        //beforeClose:event.data.closeCallback,  //窗口关闭后，closeCallback是可以被触发的，但是窗口关闭后，数据不一定已经写入数据库
	        modal:true,
	        buttons: {
	       // 	"暂存": function(){ alert(); qdv_tempSaveCreate.apply(this, arguments);},
		        "确定":function(){
					qdv_submitcreate(url);
				},
				"取消": function() {
					$(this).dialog("close");
				}
			}
		});
	$("#qdv_createBOPage").show();
	$('#qdv_createBOForm').trigger('resetForm');
	//busiFormreset($('#qdv_createBOForm'));
}

//处理新建业务对象弹出框的暂存
function qdv_tempSaveCreate() {
	//alert("222");
	if ($('#qdv_createBOForm').validate()){
		$.post(ctxPath+"/workflow/quickdev/tempSave.vot", encodeURI($("[id=qdv_createBOForm]").serialize()), function(data) {
	    	if(data.success){
	    		$("body").trigger('reload');
	    		alert("暂存成功");
	        }else{
	        	alert("暂存失败");
	    	}
		}, "json");
		$("#qdv_createBOForm").dialog("close");	
	}else{
		//alert(100);
	}
}

// 处理新建业务对象弹出框的提交
function qdv_submitcreate(url) {
	if ($('#qdv_createBOForm').validate()){
		var realUrl=ctxPath+"/workflow/quickdev/submit.vot";
		if (url != null){
			realUrl = url;
		}
		$.post(realUrl, encodeURI($("[id=qdv_createBOForm]").serialize()), function(data) {
	    	if(data.success){
	    		$("body").trigger('reload');
	    		alert("提交成功");
	        }else{
	        	alert("提交失败");
	    	}
		}, "json");
		$("#qdv_createBOForm").dialog("close");
	}else{
		//alert(100);
	}
}

//加载业务表单
function qdv_createBusiForm(target,busiContentUrl,resetFormCallback){
	$('<form name="qdv_createBOForm" id="qdv_createBOForm" class="condition"> <div id="qdv_createBOPage" class="bodydiv" ></div></form>').appendTo(target);
	$("#qdv_createBOPage").load(busiContentUrl,{state:"create"}).hide();
	if (null != resetFormCallback){
		$('#qdv_createBOForm').bind('resetForm',resetFormCallback); // 调用传入的重置函数
	}else {
		$('#qdv_createBOForm').bind('resetForm',busiFormreset); // 调用默认的重置函数
	}
}

//初始化新建合规咨询环境
// target:弹出页面挂接的位置，btn：按钮点击新建，busiContentUrl：页面表单地址
function qdv_initCreateBO(btn,callback,url){

	//$("#qdv_createBOPage").load(busiContentUrl,{"state":"create"},function(){advisorContentLoaded = $("#qdv_createBOPage");}).hide();
	//$("#qdv_createBOForm").data(callback);
	//btn.click({closeCallback:callback},qdv_showCreateBOPage); // 新建合规咨询按钮点击
	btn.bind('click',{closeCallback:callback,'url':url},qdv_showCreateBOPage);
}

//流程快速开发.新建业务对象区段.结束 =====================

function qdv_viewBO(processIdValue, twidth, theight){
	var twidth = twidth || 850;
	var theight = theight || 350;
	/*if (advisorContentLoaded == null){ //如果业务对象没有被加载
		$('<div id="qdv_viewBO"></div>').appendTo(target);
		$("#qdv_viewBO").load(busiContentUrl,{processId:processIdValue,state:"view"});
	}
	var tmp = advisorContentLoaded.clone(); //对于已经被加载的，clone
	$('<div id="qdv_viewBO"></div>').appendTo(target);
	var tmp2 =$("#qdv_viewBO");
	tmp.appendTo(tmp2);
	tmp.show();*/
	adv_loadData(processIdValue,"view");
	$("#qdv_createBOForm").dialog({
	 title:"查看具体内容",
     width: twidth,
     height: theight,
     modal:true,
     buttons: {
	      "确定": function() {
				$(this).dialog("close");
				//$("#qdv_viewBO").remove();
			}
		}
	});
	$("#qdv_createBOPage").show();
}

/**
 * GaoWei添加的暂存信息时使用的弹框 代码段开始
 * qdv_viewBOForZC
 */
function qdv_viewBOForZC(processIdValue){
	adv_loadDataForZC(processIdValue,"modify");
	$("#qdv_createBOForm").dialog({
	 title:"修改暂存信息",
     width: 850,
     height: 350,
     modal:true,
     buttons: {
    	 "提交": function() { 
    		//alert(0);
    		 $.post(ctxPath+"/workflow/quickdev/submit.vot", encodeURI($("[id=qdv_createBOForm]").serialize()), function(data) {
    		    	if(data.success){
    		    		$("body").trigger('reload');
    		    		alert("提交成功");
    		        }else{
    		        	alert("提交失败");
    		    	}
    			}, "json");
		  	}, 
    	 "暂存": function() { 
			  	$(this).dialog("close");
		  	},
		  "取消": function() { 
			  	$(this).dialog("close");
		  }
		}
	});
	$("#qdv_createBOPage").show();
}
/**
 * GaoWei添加的暂存信息时使用的弹框 代码段结束
 */

function busiFormreset(formName){
	//alert("resetForm fired!");
	$(':input[id!=busi_BUSI_ID]',formName)
	 .not(':button, :submit, :reset, :hidden')
	 .val('')
	 .removeAttr('checked')
	 .removeAttr('selected');

}
//提交任务
function auditCommitTask(event){
	//alert($("[id=form1]").validate());
	if ($("[id=form1]").validate()) {
		
		$.commonSubmit({
			url: ctxPath+"/workflow/quickdev/submitTask.vot",
			data: encodeURI($("[id=form1]").serialize()),
			callback: function(){
				alert("提交成功");
				if (!$.closeDialogTopIframe()) {
					location= event.data.returnURL;
				}
			},
			button: this,
			confirmText:"确认要提交吗？"
		});
	}
}
//提交任务 定期报告 武洋
function wy_auditCommitTask(returnURL){
	if ($("[id=form1]").validate()) {
		$.post(ctxPath+"/workflow/quickdev/submitTask.vot",encodeURI($("[id=form1]").serialize()),function(data){
			if(data.success){
				alert("提交成功");
				// 若为顶级窗口iframe则关闭
			if(!$.closeDialogTopIframe())
				location=returnURL;
			}else{
				alert(data.msg);
			}
		},"json");
	}
}

function gsauditCommitTask(returnURL,btn){
	if ($("[id=form1]").validate()) {
		btn.disable();
		$.post(ctxPath+"/workflow/quickdev/submitTask.vot",{taskId:$("#form1 [name=taskId]").val(),
		processId:$("#form1 [name=processId]").val(),
		auditResult:encodeURI($("#form1 [name=auditResult]").val()),
		audit_content:encodeURI($("#processform [name=audit_content]").val()),
		busiTitle:encodeURI(encodeURI($("#form1 [name=busiTitle]").val())),
		todo_moduleName:encodeURIComponent($("#form1 [name=todo_moduleName]").val()),
		todo_titleField:encodeURIComponent($("#form1 [name=todo_titleField]").val()),
		todo_titleFieldVal:encodeURIComponent($("[name=todo_titleFieldVal]").val())
		},function(data){
			if(data.success){
				alert("提交成功");
				btn.enable();
			if(!$.closeDialogTopIframe())
				location=returnURL;
			}else{
				alert(data.msg);
			}
		},"json");
	}
}
//人员选择
function peopleSelCommon(currentSel){
	var regPanels = new Array();
	var three  = new Array(2);
	three  [0] = "部门下人员";
	three  [1] = "2";
	regPanels[regPanels.length] = three ;

	var one = new Array(2);
	one [0] = "人员";
	one [1] = "1";
	regPanels[regPanels.length] = one ;

	//因为还要传递别的信息,所以这里把regPanels再次放到panel中进行传递,并且,数组的第一个是regpanels.
	var allInfo = new Array(3);
	allInfo[0] = regPanels;
	allInfo[1] = currentSel;

	var result = showModalDialog(ctxPath+"/admin/user/group/sc/sc-select-people.jsp?selectType=single", allInfo,  "dialogWidth=800px;dialogHeight=520px;dialogLeft=100px;dialogTop=100px;status=no;");
	if(result){
		var tmp = result[0]
		if(tmp||tmp.trim()!=""){
			alert(tmp);			
			var tmp2 = tmp.split('/');
			if (tmp2.length >= 2){
				return tmp2;
			}
		}
	}
	return null;
}

//审计用人员选择
function peopleSel(event, tt){
	var targetSelectInput = $(".peopleSeletedInput", tt);
	var targetSelectInputid = $(".peopleSeletedInputHidden", tt);

	if ("true" == $.getSysprop("login.useldap")) {
		var result = peopleSelCommon(targetSelectInput.val(), tt);
		if(result){
			targetSelectInput.val(result[1]);
			targetSelectInputid.val(result[0]);
		}
	} else {
		var selectedresult = null;
		$.selectUsers({
			callback: function(users){
				if(users.length){
					targetSelectInput.val(users[0].name);
					targetSelectInputid.val(users[0].username);
				}
			},
			selectedusers:[targetSelectInputid.val()], issingle:true, isUsername:true
		});
	}
}

//审计用人员选择控件

function fw_wk_setPeopleState(target,defaultManName,defaultManId,isSelectable,nextManLabel){
	fw_wk_setPeopleState3(target,defaultManName,defaultManId,isSelectable,nextManLabel,"nextMan");
}

//通用用人员选择控件
function fw_wk_setPeopleState3(target,defaultManName,defaultManId,isSelectable,nextManLabel, manName){
	var tt = $('<span class="cd_item" style="width: 100%;"> \
			<span class="cd_name" style="color:#000;">'+nextManLabel+'：</span> \
			<span class="cd_val" style="width:800px;"> \
			<input type="text" class="peopleSeletedInput" name="nextManShow"  readonly/> \
			<input type="hidden" class="peopleSeletedInputHidden" name="'+manName+'" value="admin" readonly/> \
			<input type="button" style="width:100px;" class="peopleSelBtn sconbtn" value="选择"/> \
			</span> \
		</span> \
	').appendTo(target); // 加载内容
		if (defaultManName != null){ //有默认选择人
		$('.peopleSeletedInput', tt).val(defaultManName);
		$('.peopleSeletedInputHidden', tt).val(defaultManId);
	}
	$('.peopleSelBtn',tt).click(function(event){
		peopleSel.call(this, event, tt);
	});
	if (isSelectable == "false"){ //如果不可以选择
		$('.peopleSelBtn',tt).hide();
	}else if (isSelectable == "none"){ // 如果完全不让显示
		$('.peopleSelBtn',tt).hide();
		$('.peopleSeletedInput',tt).hide();
	}
}

// 业务表单用人员选择
function peopleSel2(event, tt){
	var targetSelectInput = $(".peopleSeletedInput2", tt);
	var targetSelectInputid = $(".peopleSeletedInputHidden2", tt);

	if ("true" == $.getSysprop("login.useldap")) {
		var result = peopleSelCommon(targetSelectInput.val(), targetSelectInputid.val());
		if(result){
			targetSelectInput.val(result[1]);
			$('.peopleSeletedInputHidden2', tt).val(result[0]);
		}
	} else {
		var selectedresult = null;
		alert("");
		$.selectUsers({
			callback: function(users){
				if(users.length){
					targetSelectInput.val(users[0].name);
					targetSelectInputid.val(users[0].username);
				}
			},
			selectedusers:[targetSelectInputid.val()], issingle:true, isUsername:true
		});
	}
}
//合规检查专用人员选择
function peopleSelc(thisObj){
	var targetSelectInput = $(".selmanname", $(thisObj).parent());
	var targetSelectInputid = $(".selmanid", $(thisObj).parent());

	if ("true" == $.getSysprop("login.useldap")) {
		var result = peopleSelCommon(targetSelectInput.val(), targetSelectInputid.val());
		if(result){
			targetSelectInput.val(result[1]);
			targetSelectInputid.val(result[0]);
		}
	} else {
		var selectedresult = null;
		$.selectUsers({
			callback: function(users){
				if(users.length){
					targetSelectInput.val(users[0].name);
					targetSelectInputid.val(users[0].username);
				}
			},
			selectedusers:[targetSelectInputid.val()], issingle:true, isUsername:true
		});
	}
}
//合规检查专用 人员选择控件
function CKselectman(thisObj){
	peopleSelc(thisObj);
}
//业务表单用人员选择控件
function fw_wk_setPeopleState2(target,defaultManName,defaultManId,isSelectable, nextManLabel){
	if (!nextManLabel) {
		nextManLabel = "部门合规岗";
	}	
	var tt = $('<span class="cd_item" style="width: 100%;"> \
			<span class="cd_name" style="color:#000;">'+nextManLabel+'：</span> \
			<span class="cd_val" style="width:800px;"> \
			<input type="text" class="peopleSeletedInput2" name="nextManShow"  readonly/> \
			<input  type="hidden" class="peopleSeletedInputHidden2" name="nextMan2" value="admin" readonly/> \
			<input type="button" style="width:100px;" class="peopleSelBtn2 sconbtn" value="选择"/> \
			</span> \
		</span> \
	');
	tt.appendTo(target); // 加载内容
		if (defaultManName != null){ //有默认选择人
		$('.peopleSeletedInput2', tt).val(defaultManName);
		$('.peopleSeletedInputHidden2', tt).val(defaultManId);
	}
	$('.peopleSelBtn2', tt).click(
		function(event) {
			peopleSel2.call(this, event, tt);
		}
	);
	if (isSelectable == "false"){ //如果可以选择
		$('.peopleSelBtn2', tt).hide();
	}
}
//部门选择通用组件
function deptMultiSelComm(){
 var popwindow_height="400";
 var popwindow_width="200";
 var popwindow_left=(screen.availWidth-800)/2;
 var popwindow_top=(screen.availHeight-200)/2;
 var result =showModalDialog (ctxPath+"/admin/user/people/people-organization-poptree.jsp?mutiSelect=true", document,  "directories=no,toolbar=no,menubar=no,scrollbars=yes,dialogHeight:"+popwindow_height+";dialogWidth:"+popwindow_width+",dialogLeft:"+popwindow_left+";dialogTop:"+popwindow_top);
 var depts = result[0].split("\n");
 var deptIds = result[1].split("&%$");
 var resultDepts = new Array();
 $.each(depts,function(index,value){
  if (index != depts.length-1){
   var curDept = {};
   curDept.name=value.substring(4);
   curDept.id=deptIds[index];
   resultDepts[index] = curDept;
  }
 }
 );
 	$.each(resultDepts,function(index,value){
 //	alert(index+"::"+value.name+"|"+value.id);
 });
 return resultDepts;
}