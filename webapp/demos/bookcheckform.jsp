<%@page import="antelope.demos.entites.SingleDataGridItem"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@page import="antelope.springmvc.JPABaseDao"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%
System.out.println("fsddfs");

JPABaseDao dao = SpringUtils.getBean(JPABaseDao.class, "jpabasedao");
SingleDataGridItem item = dao.getBy("1368230851407", SingleDataGridItem.class);
System.out.println(item);
%>
<script>
function selectassignee2() {
	$.selectUsers({
		issingle: false,
		callback: function(users) {
			if (users.length) {
				$("[name=var_name]").val(users[0].name);
				$("[name=var_candidateusers]").val(users[0].username);
			}
		}
	});
}

function selectassignee4() {
	$.selectUsers({
		issingle: false,
		callback: function(users) {
			if (users.length) {
				$("[name=var_hqname]").val(users[0].name);
				$("[name=var_hqcandidateusers]").val(users[0].username);
			}
		}
	});
}

function selectassignee3() {
	$.selectUsers({
		issingle: false,
		callback: function(users) {
			$("[name=var_multinames]").val($.extractField(users,'name'));
			$("[name=var_multiinstusers]").val($.extractField(users,'username'));
		}
	});
}


function selectcoptyusers() {
	$.selectUsers({
		issingle: false,
		callback: function(users) {
			if (users.length) {
				var copytonames = [];
				var copytousernames = [];
				for (var i = 0; i < users.length; ++i) {
					copytonames.push(users[i].name);
					copytousernames.push(users[i].username);
				}
				$("[name=var_copytousers_name]").val(copytonames);
				$("[name=var_copytousers]").val(copytousernames);
			}
		}
	});
}

function dopass(thisObj) {
	//return;
	if (thisObj.value=='通过') {
		$("#submitdiv1").show();
		$("#submitdiv2").hide();
	} else {
		$("#submitdiv1").hide();
		$("#submitdiv2").show();
	}
}

</script>
<div style="width: 430px; height: 120px;">
	<div class="condi_container">
		<span class="cd_name">审批意见：</span>
		<span class="cd_val">
			<input type="radio" name="var_result2fdsafdsa2" value="通过fdsafd"  style="width: 20px;"/>
			<textarea name="var_comment"></textarea>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">审批结果：</span>
		<span class="cd_val">
		<!-- 
			<input type="radio" name="var_result" value="通过fdsafd"  style="width: 20px;"/><span style="color:#000;" >通过</span>
			 -->
			<input type="radio" onclick="dopass(this)" name="var_result" value="通过"  checked="checked" style="width: 10px;"/><span style="color:#000;" >通过</span>
			<input type="radio" onclick="dopass(this)" name="var_result" value="不通过" style="width: 10px;"/><span style="color:#000;">不通过</span>
		</span>
	</div>
	<div class="condi_container">
		<div id="submitdiv1">
			<span class="cd_name">提交给：</span>
			<span class="cd_val">
				<input name="var_name" type="text" readonly="readonly" required="true"/>
				<input type="button" value="选择" onclick="selectassignee2()" class="smallbutton" style="width: 20px;"/>
				<input name="var_candidateusers" type="hidden"/>
			</span>
		</div>
		<div id="submitdiv2" style="display: none;">
			<span class="cd_name">会签提交给(当不通过时)：</span>
			<span class="cd_val" id="huiqianspan">
				<input name="var_multinames" type="text" readonly="readonly" required="true"/>
				<input type="button" value="选择" onclick="selectassignee3()" class="smallbutton" style="width: 20px;"/>
				<input name='var_multiinstusers' type="hidden"/>
			</span>
			
			<span class="cd_name">会签结束后提交给(当不通过时)：</span>
			<span class="cd_val" id="huiqianspan">
				<input name="var_hqname" type="text" readonly="readonly" required="true"/>
				<input type="button" value="选择" onclick="selectassignee4()" class="smallbutton" style="width: 20px;"/>
				<input name="var_hqcandidateusers" type="hidden"/>
			</span>
		</div>
		<span class="cd_name">抄送给：</span>
		<span class="cd_val">
			<input name="var_copytousers_name" type="text" readonly="readonly" required="true"/>
			<input type="button" value="选择" onclick="selectcoptyusers()" class="smallbutton" style="width: 20px;"/>
			<input name="var_copytousers" type="hidden"/>
		</span>
	</div>
</div>


