<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
function validateit() {
	$.selectUsers({
		selectedusers:['testuser'],
		issingle: false,
		callback: function(selectusers){
			alert (JSON.stringify(selectusers));
		}
	});
}

//选择用户, 若通过角色找不到对应人员，则从全体人员中选取
function selectUserByRolesid() {
	$.selectUserByRolesid({
		rolesid:"",
		unitsid:"1406454438119",
		callback: function(users) {
			alert(JSON.stringify(users));
		}
	}); 
}

function selectUserByDeptWithFilter() {
}

function selectUnits() {
	$.selectUnits({
		callback: function(dataitems) {
			alert(dataitems.length);
		},
		selectedunits:[]
	});
}

$(function(){
	$.selectParentUser({
		username:'02000',
		parentUserTextInput:"[name=selectparentuser]",
		parentUsernameHiddenInput:"[name=selectparentusername]",
		defaultParentUsername:"02003"
	});
	$.selectParentUser({
		username:'02000',
		parentUserTextInput:"[name=selectparentuser2]",
		parentUsernameHiddenInput:"[name=selectparentusername2]"
	});
});
</script>
</head>
<body style="height: 100%;overflow: auto; padding: 5px;">
<button onclick="validateit()">全员选择（单位、角色）</button>
<button onclick="selectUserByRolesid()">固定机构，固定角色选择（角色）</button>
<button onclick="selectUnits()">单位多选</button>

<div id="xuanzhong"></div>

<div id="parentuserdiv">
 含有默认用户的上级领导选择（默认用户为02003)
	<input name="selectparentusername"/>
	<input name="selectparentuser"/>
</div>
<div id="parentuserdiv">
 未含默认用户的上级领导选择（框里默认显示的是对应输入的用户设置的上级领导）
	<input name="selectparentusername2"/>
	<input name="selectparentuser2"/>
</div>

<div id="parentuserdiv">
 	单位多选
	<input name="selectedunits"/>
	<input name="selectedunits"/>
</div>

</body>
</html>