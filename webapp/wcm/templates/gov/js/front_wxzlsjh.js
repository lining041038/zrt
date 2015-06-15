function getValidateCodeAgain() {
	var getimagecode = document.getElementById("vdcpic");
	getimagecode.src = "../../captcha?" + Math.random();
}

function c_valiedCode() {
	var validate = document.getElementById("validate").value;
	// var validateError = document.getElementById("validateError1");
	if (validate != '') {
		// validateError.style.display = "none";
		return true;
	}
	// validateError.style.display = "block";
	// var validateError2 = document.getElementById("validateError2");
	// validateError2.style.display = "none";
	return false;
}

function c_content() {
	var content = document.getElementById("content").value;
	// var contentError = document.getElementById("contentError1");

	if (content == null || content == "" || content == '内容字数不超过200字！') {
		// contentError.style.display = "none";
		alert("留言内容不可为空！");
		return false;
	}
	if (content.length > 200) {
		alert("留言内容不能大于200字！");
		return false;
	}
	var date = new Date();
	if (date.getHours() < 9 || date.getHours() > 19) {
		alert("请您于早9点至晚8点间留言!");
		return false;
	}
	// contentError.style.display = "block";

	// var contentError2 = document.getElementById("contentError2");
	// contentError2.style.display = "none";

	return true;
}

function resetClear() {
	document.getElementById("content").value = "";
	var obj = document.getElementById("span1");
	obj.innerHTML = "<input id='annex' name='annex' type='file' class='jyyhm' size='30' />";
	document.getElementById("validate").value = "";
	var contentError1 = document.getElementById("contentError1");
	contentError1.style.display = "none";

	var validateError1 = document.getElementById("validateError1");
	validateError1.style.display = "none";

	var contentError2 = document.getElementById("contentError2");
	contentError2.style.display = "none";

	var validateError2 = document.getElementById("validateError2");
	validateError2.style.display = "none";

	var fileError = document.getElementById("fileError");
	fileError.style.display = "none";
}

var FormValid = function(frm) {
	this.frm = frm;
	this.errMsg = new Array();
	this.errName = new Array();

	this.telphone = function(inputObj) {
		if (typeof (inputObj) == "undefined" || inputObj.value.trim() == "") {
			return false;
		}

		var len = inputObj.value.length;
		if (len) {
			var minv = inputObj.getAttribute('eos:min');
			var maxv = inputObj.getAttribute('eos:max');
			minv = minv || 0;
			maxv = maxv || Number.MAX_VALUE;
			return minv <= len && len <= maxv;
		}

		return true;
	};

	this.required = function(inputObj) {
		if (typeof (inputObj) == "undefined" || inputObj.value.trim() == "") {
			return false;
		}
		return true;
	};

	this.eqaul = function(inputObj, formElements) {
		var fstObj = inputObj;
		var sndObj = formElements[inputObj.getAttribute('eqaulName')];

		if (fstObj != null && sndObj != null) {
			if (fstObj.value != sndObj.value) {
				return false;
			}
		}
		return true;
	};

	this.gt = function(inputObj, formElements) {
		var fstObj = inputObj;
		var sndObj = formElements[inputObj.getAttribute('eqaulName')];

		if (fstObj != null && sndObj != null && fstObj.value.trim() != '' && sndObj.value.trim() != '') {
			if (fstObj.value <= sndObj.value) {
				return false;
			}
		}
		return true;
	};

	this.compare = function(inputObj, formElements) {
		var fstObj = inputObj;
		var sndObj = formElements[inputObj.getAttribute('objectName')];
		if (fstObj != null && sndObj != null && fstObj.value.trim() != '' && sndObj.value.trim() != '') {
			if (!eval('fstObj.value' + inputObj.getAttribute('operate')
					+ 'sndObj.value')) {
				return false;
			}
		}
		return true;
	};

	this.limit = function(inputObj) {
		var len = inputObj.value.length;
		if (len) {
			var minv = inputObj.getAttribute('eos:min');
			var maxv = inputObj.getAttribute('eos:max');
			minv = minv || 0;
			maxv = maxv || Number.MAX_VALUE;
			return minv <= len && len <= maxv;
		}
		return true;
	};

	this.range = function(inputObj) {
		var val = parseInt(inputObj.value);
		if (inputObj.value) {
			var minv = inputObj.getAttribute('eos:min');
			var maxv = inputObj.getAttribute('eos:max');
			minv = minv || 0;
			maxv = maxv || Number.MAX_VALUE;

			return minv <= val && val <= maxv;
		}
		return true;
	};

	this.requireChecked = function(inputObj) {
		var minv = inputObj.getAttribute('eos:min');
		var maxv = inputObj.getAttribute('eos:max');
		minv = minv || 1;
		maxv = maxv || Number.MAX_VALUE;

		var checked = 0;
		var groups = document.getElementsByName(inputObj.name);

		for (var i = 0; i < groups.length; i++) {
			if (groups[i].checked)
				checked++;

		}
		return minv <= checked && checked <= maxv;
	};

	this.filter = function(inputObj) {
		var value = inputObj.value;
		var allow = inputObj.getAttribute('allow');
		if (value.trim()) {
			return new RegExp("^.+\.(?=EXT)(EXT)$".replace(/EXT/g, allow.split(/\s*,\s*/).join("|")), "gi").test(value);
		}
		return true;
	};

	this.isNo = function(inputObj) {
		var value = inputObj.value;
		var noValue = inputObj.getAttribute('noValue');
		return value != noValue;
	};

	this.checkReg = function(inputObj, reg, msg) {
		inputObj.value = inputObj.value.trim();

		if (inputObj.value == '') {
			return;
		} else {
			if (!reg.test(inputObj.value)) {
				this.addErrorMsg(inputObj.name, msg);
			}
		}
	};

	this.passed = function() {
		if (this.errMsg.length > 0) {
			FormValid.showError(this.errMsg, this.errName);
			frt = document.getElementsByName(this.errName[0])[0];

			if (frt.type != 'radio' && frt.type != 'checkbox') {
				frt.focus();
			}
			return false;
		} else {
			return true;
		}
	};

	this.addErrorMsg = function(name, str) {
		this.errMsg.push(str);
		this.errName.push(name);
	};

	this.addAllName = function(name) {
		FormValid.allName.push(name);
	};

};

FormValid.allName = new Array();
FormValid.showError = function(errMsg) {
	var i = 0;
	var msg = "";
	for (i = 0; i < errMsg.length; i++) {
		msg += "- " + errMsg[i] + "\n";
	}
	alert(msg);
};

function validator(frm) {
	var formElements = frm.elements;
	var fv = new FormValid(frm);

	for (var i = 0; i < formElements.length; i++) {
		var validType = formElements[i].getAttribute('eos:valid');
		var errorMsg = formElements[i].getAttribute('eos:errmsg');
		if (validType == null)
			continue;
		fv.addAllName(formElements[i].name);

		var vts = validType.split('|');
		var ems = errorMsg.split('|');
		for (var j = 0; j < vts.length; j++) {
			var curValidType = vts[j];
			var curErrorMsg = ems[j];

			switch (curValidType) {
			case 'isNumber':
			case 'isEmail':
			case 'isPhone':
			case 'isMobile':
			case 'isIdCard':
			case 'isMoney':
			case 'isZip':
			case 'isQQ':
			case 'isInt':
			case 'isEnglish':
			case 'isChinese':
			case 'isUrl':
			case 'isDate':
			case 'isTelphone':
			case 'isTime':
				fv.checkReg(formElements[i], RegExps[curValidType], curErrorMsg);
				break;
			case 'regexp':
				fv.checkReg(formElements[i], new RegExp(formElements[i].getAttribute('regexp'), "g"), curErrorMsg);
				break;
			case 'custom':
				if (!eval(formElements[i].getAttribute('custom') + '(formElements[i],formElements)')) {
					fv.addErrorMsg(formElements[i].name, curErrorMsg);
				}
				break;
			default:
				if (!eval('fv.' + curValidType + '(formElements[i],formElements)')) {
					fv.addErrorMsg(formElements[i].name, curErrorMsg);
				}
				break;
			}
		}
	}

	var su = c_content();
	if (su == false) {
		return false;
	}
	if (fv.passed() == true) {
		$.ajax({
			cache : true,
			type : "POST",
			url : $('#zlhd').attr('action'),
			data : $('#zlhd').serialize(),
			async : false,
			error : function(request) {
				alert("您的留言已成功提交，感谢您的留言！");
				return "error";
			},
			success : function(data) {
				// //判断返回值不是JSON格式
				if (!data.match("^\{(.+:.+,*){1,}\}$")) {
					// 普通字符串处理
					if (data == "您的留言已成功提交，感谢您的留言!") {
						clear_form();
						blank_form();
						return true;
					} else {
						alert(data);
					}
					getValidateCodeAgain();
				} else {
					// 将字符串转换为JSON对象
					data = eval("("+data+")");
					if (data.success) {
						// 只有提交成功时，才清除表单
						clear_form();
						blank_form();
					} else {
						// 只有提交失败，才提示信息，以及刷新验证码。提交成功时，重定向页面
						alert(data.message);
						getValidateCodeAgain();
					}
				}
			}
		});
	}
}

function clear_form() {
	$('#zlhd')[0].reset();
}

function blank_form() {
	$('#qkbd').submit();
}

String.prototype.trim = function() {
	return this.replace(/^\s*|\s*$/g, "");
};

function v_content() {
	var content = document.getElementById("content");
	if (content.value == '内容字数不超过200字！') {
		content.value = '';
	}
	content.style.color = '#333';
	var date = new Date();
	if (date.getHours() < 9 || date.getHours() > 19) {
		alert("请您于早9点至晚8点间留言!");
	}
}

var RegExps = function() {
};

RegExps.isTelphone = /^[-\+]?\d+(\.\d+)?$/;
RegExps.isNumber = /^[-\+]?\d+(\.\d+)?$/;
RegExps.isEmail = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)/;
RegExps.isPhone = /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
RegExps.isMobile = /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
RegExps.isIdCard = /(^\d{15}$)|(^\d{17}[0-9Xx]$)/;
RegExps.isMoney = /^\d+(\.\d+)?$/;
RegExps.isZip = /^[1-9]\d{5}$/;
RegExps.isQQ = /^[1-9]\d{4,10}$/;
RegExps.isInt = /^[-\+]?\d+$/;
RegExps.isEnglish = /^[A-Za-z]+$/;
RegExps.isChinese = /^[\u0391-\uFFE5]+$/;
RegExps.isUrl = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
RegExps.isDate = /^\d{4}-\d{1,2}-\d{1,2}$/;
RegExps.isTime = /^\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}:\d{1,2}$/;
