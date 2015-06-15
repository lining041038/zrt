<%@ page language="java" pageEncoding="utf-8"%>
<% 
// 若为我的待办或我的已办 则有对应任务节点的key
System.out.println("我的待办或我的已办时：" + request.getParameter("task_def_key_"));
// 若为我发起的和所有，则有对应流程当前已经流转到的所有节点的key，逗号分割
System.out.println("我发起的或所有时：" + request.getParameter("task_def_keys_"));
%>
<script>
function selectassignee() {
	$.selectUsers({
		issingle: true,
		callback: function(users) {
			if (users.length) {
				$("[name=var_candidateusername]").val(users[0].name);
				$("[name=var_candidateusers]").val(users[0].username);
			}
		}
	});
}

function selectcoptyusers2() {
	$.selectUsers({
		issingle: true,
		callback: function(users) {
			if (users.length) {
				$("[name=var_candidateusername]").val(users[0].name);
				$("[name=var_candidateusers]").val(users[0].username);
			}
		}
	});
}

$(function() {
	$("#mybusiform").parent().rebind("openViewForm", function(e, data) {
		alert(JSON.stringify(data));
		alert("您当前的节点定义key为：" + data.task_def_key_);
	});
	$("#mybusiform").parent().rebind("openedUpdateForm", function(e, data) {
		alert(JSON.stringify(data));
	});
});
</script>
<div id="mybusiform" style="width: 430px; height: 120px;">
	<div class="condi_container">
		<span class="cd_name">图书名称：</span>
		<span class="cd_val">
			<input name="bookname" num="true" required="true" state="disabled.view:true"/>
		</span>
		<input type="checkbox" name="mybook" value="范德萨分三大" checked="checked" default="true"/>
	</div>
	<div class="condi_container" includeIn="create">
		<span class="cd_name">提交给：</span>
		<span class="cd_val">
			<input name="var_candidateusername" type="text" readonly="readonly" required="true"/>
			<input type="button" value="选择" onclick="selectassignee()" class="smallbutton" style="width: 20px;"/>
			<input name="var_candidateusers" type="hidden"/>
		</span>
	</div>
	
	<div class="condi_container" includeIn="create">
		<span class="cd_name">抄送给：</span>
		<span class="cd_val">
			<input name="var_copytousers_name" type="text" readonly="readonly" required="true"/>
			<input type="button" value="选择" onclick="selectcoptyusers2()" class="smallbutton" style="width: 20px;"/>
			<input name="var_copytousers" type="hidden"/>
		</span>
	</div>
	
</div>


