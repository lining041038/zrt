<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>

$(function(){
	$("#validatediv").datagrid({
		dataProvider:ctxPath + "/demos/DataGridController/loadManyDatas.vot",
		columns:{test:"测试列"},
		numPerPage: 100,
		operationHeaderWidth:"700px",
		centerButton: true,
		buttons: {
			i_handbook: $.noop,
			i_query: $.noop,
			i_queryfolder: $.noop,
			i_release:  $.noop,
			i_releasebook:  $.noop,
			i_send:  $.noop,
			i_ignore:  $.noop,
			i_reply:  $.noop,
			i_money:  $.noop,
			i_chat:  $.noop,
			i_gate:  $.noop,
			i_play:  $.noop,
			i_input:  $.noop,
			i_config:  $.noop,
			i_configclass:  $.noop,
			i_configgrid:  $.noop,
			i_configreply:  $.noop,
			i_del:  $.noop,
			i_audit:  $.noop,
			i_auditpass:  $.noop,
			i_viewvideo:  $.noop,
			i_stopchat:  $.noop,
			i_submit:  $.noop,
			i_synchronize:  $.noop,
			i_modify:  $.noop,
			i_viewpaper:  $.noop,
			i_up:  $.noop,
			i_down:  $.noop,
			i_recall:  $.noop,
			i_live:  function() {
				alert(JSON.stringify(this));
			}
		},
		pageChange: function(currlist) {
			alert("页面数据发生变化!");
		//	alert(JSON.stringify(currlist));
		}
	});
	
	function alertCurrList() {
		if ($("#validatediv").datagrid("getCurrList") == null) {
			setTimeout(alertCurrList, 1000);
		} else {
			$("#tracevaldiv").text($("#validatediv").datagrid("getCurrList"));
		}
	}
	
	alertCurrList();
});

function loaddata() {
	$("#validatediv").datagrid("option", "dataProvider", ctxPath + "/demos/DataGridController/loadManyDatas.vot");
}

</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="loaddata()">加载大量数据</button>

<input type="radio" name="var_result2fdsafdsa2" value="通过fdsafd"  style="width: 20px;"/>
<div id="validatediv">

</div>
<div id="tracevaldiv"></div>
</body>
</html>