

/* 无 i18n js文件时防止js报错*/
if (!$['i18n']) {
	$.i18n = function() {return '';}
	$.i18nCache = {"locale":"zh_CN"};
}

$.ajaxSetup({
		error:function(e) {
			// alert("后台数据发生错误或服务器停止运行，请联系管理员   ！");  
		}
});

// 对jQuery核心库进行修正 开始
jQuery.extend({
	// url特殊处理，防止浏览器缓存
	_getJSON: jQuery.getJSON,
	
	getJSON: function( ) {
		return $._getJSON.apply(this, $.stopcacheinner(arguments));
	},
	
	// url特殊处理，防止浏览器缓存
	_get: jQuery.get,
	
	get: function( ) {
		return $._get.apply(this, $.stopcacheinner(arguments));
	},

	// url特殊处理，防止浏览器缓存
	_post: jQuery.post,
	
	post: function( ) {
		return $._post.apply(this, $.stopcacheinner(arguments));
	},
	
	stopcacheinner: function(arguments) {
		if(arguments[0].indexOf('?') != -1) {
			arguments[0] = arguments[0] + "&stopcache=" + Math.random();
		} else {
			arguments[0] = arguments[0] + "?stopcache=" + Math.random();
		}
		return arguments;
	},
	
	browser: {
		msie: (/(msie) ([\w.]+)/.exec(navigator.userAgent.toLowerCase()) != null),
		version: (/(msie) ([\w.]+)/.exec(navigator.userAgent.toLowerCase()) == null ? 8 :/(msie) ([\w.]+)/.exec(navigator.userAgent.toLowerCase())[2])
	},
	
	dialogeffect: {
		 show: {
		       effect: "drop",
		       duration: 200,
		       direction:'up'
		 },
		 hide: {
			    effect: "drop",
			    duration: 200,
			    direction:'up'
		 }
	}
});

jQuery.fn.extend({
	_serialize: jQuery.fn.serialize,
	
	serialize: function() {
		// 文件上传下载特殊处理
		var origin = this._serialize.apply(this, arguments);
		var fileuploaddatas = {};
		this.find(".fileupload").each(function(){
			if ($(this).attr("name"))
				fileuploaddatas[$(this).attr("name")] = $(this).val();
		});
		
		var fupresults = $.param(fileuploaddatas);
		if ($.param(fileuploaddatas)) {
			origin += ("&" + fupresults);
		} 
		
		return origin;
	},
	
	rebind: function( type, fn ) {
		return this.unbind(type).bind(type, fn);
	}
});
// 对jQuery核心库进行修正 结束

//系统通用功能
jQuery.extend({
	changeTheme: function() {
		$.getJSON(ctxPath + "/common/UserThemeController/getThemesToSelect.vot", function(data){
			var selectdiv = "";
			for(var i = 0; i < data.length; ++i) {
				selectdiv += "<input id='_themeid"+i+"' type='radio' name='theme' " + (data[i].selected ? 'checked':'') + " style='cursor:pointer' value='"+data[i].theme+"'/>\
						<label for='_themeid"+i+"' style='cursor:pointer' >"  + data[i].themelabel + "</label>"
			}
			$("<form>" + selectdiv + "</form>").submitDialog({
				url: ctxPath + "/common/UserThemeController/saveSelectedTheme.vot",
				width: 550, title: $.i18n('antelope.changetheme'),
				height: 150,
				callback: function(){
					location = ctxPath + $.getIndexPagePos();
				}
			})
		});
	},
	changeLanguage: function() {
		$.getJSON(ctxPath + "/common/UserThemeController/getAllLocales.vot", function(data){
			var selectdiv = "";
			for(var i = 0; i < data.length; ++i) {
				selectdiv += "<input id='_localeid"+i+"' type='radio' name='locale' " + (data[i].selected ? 'checked':'') + " style='cursor:pointer' value='"+data[i].value+"'/>\
						<label for='_localeid"+i+"' style='cursor:pointer' >"  + data[i].label + "</label>"
			}
			$("<form>" + selectdiv + "</form>").submitDialog({
				url: ctxPath + "/common/UserThemeController/saveSelectedLocale.vot",
				width: 550, title: $.i18n('antelope.changelanguage'),
				height: 150,
				callback: function(){
					location = ctxPath + $.getIndexPagePos();
				}
			})
		});
	},
	registeruser:function() {
		var btns = {};
		var theifram = null;
		btns[$.i18n('antelope.register')] = function() {
			theifram.find("iframe")[0].contentWindow.registeruser(function(){
				theifram.dialog("destroy");
			});
		}
		btns[$.i18n('antelope.cancel')] = function() {
			theifram.dialog("destroy");
		}
		theifram = $.dialogTopIframe({
			title:$.i18n('antelope.register'),
			width:400, height:300,
			url:ctx + "/system/register.jsp",
			buttons:btns
		});
	}
});

// 表单验证插件信息的封装
jQuery.extend({
	validators: [{
		name: 'required', // 表单验证名称，用于属性值的名称如 required=true
		validate: function(inputobj) {
			// 文件上传特殊处理
			if (inputobj.hasClass("fileupload")) {
				if (inputobj.fileupload("getFileInfos").length <= 0)
					return $.i18n('antelope.validate.pleaseupload');
				return;
			}
			if (!$.trim(inputobj.val()))
				return $.i18n('antelope.validate.pleaseinput');
		}
	}, {
		name: 'required2', // 表单验证名称，用于属性值的名称如 required=true
		validate: function(inputobj) {
			// 文件上传特殊处理
			if (inputobj.hasClass("fileupload")) {
				if (inputobj.fileupload("getFileInfos").length <= 0)
					return $.i18n('antelope.validate.pleaseupload');
				return;
			}
			if (!$.trim(inputobj.val()))
				return $.i18n('antelope.validate.pleaseinput');
		}
	}, {name: 'maxlength2', // 由于maxlength已经被浏览器的默认行为占用，此处使用maxlength2来代替
		validate: function(inputobj) {
			var mxlen = parseInt(inputobj.attr("maxlength2"));
			if ($.trim(inputobj.val()).length > mxlen)
				return $.i18n('antelope.validate.onlyinputprefix') + mxlen +  $.i18n('antelope.validate.onlyinputsuffix');
		}
	}, {name: 'minlength', // 
		validate: function(inputobj) {
			var mxlen = parseInt(inputobj.attr("minlength"));
			if ($.trim(inputobj.val()).length < mxlen)
				return $.i18n('antelope.validate.onlymininputprefix') + mxlen +  $.i18n('antelope.validate.onlyinputsuffix');
		}
	}, {name: 'nbitint', // 仅允许填写n位数字
		validate: function(inputobj) {
			var nbitint = parseInt(inputobj.attr("nbitint"));
			if ($.trim(inputobj.val()).length == 0)
				return;
			
			if ($.trim(inputobj.val()).length != nbitint || !/^\d*$/.test(inputobj.val()))
				return $.i18n('antelope.validate.pleaseinputprefix') + nbitint + $.i18n('antelope.validate.inputintvalsuffix');
		}
	}, {name: 'date', // 仅允许填写2012-01-15类型的日期
		validate: function(inputobj) {
			try {
				$.datepicker.parseDate('yy-mm-dd', inputobj.val());
			} catch (e) {
				return $.i18n('antelope.validate.pleaseinputlikedate');
			}
		}
	}, {name: 'num',
		validate: function(inputobj) {
			if ($.trim(inputobj.val())) {
				if (isNaN(inputobj.val()))
					return $.i18n('antelope.validate.onlyinputnum');
			}
		}
	}, {name: 'int',
		validate: function(inputobj) {
			if ($.trim(inputobj.val())) {
				if(!/^-?\d*$/.test(inputobj.val()))
					return $.i18n('antelope.validate.onlyinputint');
			}
		}
	}, {name: 'max',
		validate: function(inputobj) {
			var max = parseFloat(inputobj.attr("max"));
			if ($.trim(inputobj.val())) {
				var val = parseFloat(inputobj.val());
				if (!isNaN(val) && val > max)
					return $.i18n('antelope.validate.pleaseinputsmallprefix') + max + $.i18n('antelope.validate.pleaseinputsuffix');
			}
		}
	}, {name: 'min',
		validate: function(inputobj) {
			var min = parseFloat(inputobj.attr("min"));
			if ($.trim(inputobj.val())) {
				var val = parseFloat(inputobj.val());
				if (!isNaN(val) && val < min)
					return $.i18n('antelope.validate.pleaseinputbigprefix') + min + $.i18n('antelope.validate.pleaseinputsuffix');
			}
		}
	}, {name: 'email',
		validate: function(inputobj) {
			var min = parseFloat(inputobj.attr("min"));
			var isEmailRegEx = /^[A-Z0-9._%+\-]+@[A-Z0-9.\-]+\.[A-Z]{2,4}$/i;
			if ($.trim(inputobj.val())) {
				var val = parseFloat(inputobj.val());
				if (!isEmailRegEx.test($.trim(inputobj.val())))
					return $.i18n('antelope.validate.pleaseinputemail');
			}
		}
	}, {name: 'after',
		validate: function(inputobj) {
			var afterformfield = inputobj.closest("form").find("[name=" + inputobj.attr("after") + "]");
			var afterdate = afterformfield.val();

			if (inputobj.val() && afterdate) {
				var thislabel = inputobj.attr("label");
				var afterlabel = afterformfield.attr("label");
				if (inputobj.val() <= afterdate)
					return thislabel + $.i18n('antelope.validate.mustbe') + afterlabel + $.i18n('antelope.validate.mustbesuffix');
			}
		}
	}, {name: 'notBefore',
		validate: function(inputobj) {
			var afterformfield = inputobj.closest("form").find("[name=" + inputobj.attr("notBefore") + "]");
			var afterdate = afterformfield.val();

			if (inputobj.val() && afterdate) {
				var thislabel = inputobj.attr("label");
				var afterlabel = afterformfield.attr("label");
				if (inputobj.val() < afterdate)
					return thislabel + $.i18n('antelope.validate.mustnotbe') + afterlabel + $.i18n('antelope.validate.mustnotbesuffix');
			}
		}
	}, {name: 'price', // 中国价格，到分，保留两位有效数字
		validate: function(inputobj) {
			var min = parseFloat(inputobj.attr("min"));
			var isPriceRegEx = /^[0-9]+(\.[0-9]{1,2})?$/i;
			if ($.trim(inputobj.val())) {
				if (!isPriceRegEx.test($.trim(inputobj.val())))
					return "请输入有效的价格，如3、 3.1、3.12！";
			}
		}
	}]
});

//ckeditor富文本编辑器封装
(function( $, undefined ) {
	$.widget( "ui.ckeditor4", {
		options: {
			readonly:false
		},
		
		_elemid: null,

		_create: function() {
			
			var thisObj = this;
			this._elemid = this.element.attr("id");
			var element = CKEDITOR.dom.element.get(this.element.attr("id"));
			
			var theight = 131;
			var twidth = 515;
			if (theight < this.element.height()) {
				theight = this.element.height();
			}
			if (twidth < this.element.width()) {
				twidth = this.element.width();
			}
			this.element.wrap("<div style='width:" + twidth + "px;'/>");
			this.element.data('ckeditorheight', theight);
			
			var thelocal = $.i18nCache['locale'];
			if (thelocal == 'en_US')
				thelocal = 'en';
			if (thelocal == 'zh_CN')
				thelocal = 'zh-cn';
			
			CKEDITOR.replace(this.element.attr("id"), {height:theight, autoUpdateElement: false, autoGrow_bottomSpace:1, autoGrow_onStartup: true,
					language : thelocal,
					readOnly: thisObj.options.readonly,
					toolbar:[{ 
					name: 'allbtns', 
					items: [ 'Bold', 'Italic', 'Underline', 
					         'JustifyLeft','JustifyCenter', 'JustifyRight', 'JustifyBlock', 
					         'Font', 'FontSize', 'TextColor', 'AfeDialog', 'Image']
			}]});
			
			this.element.rebind("attrchange", function(){
				attrchangefunc.apply(this, arguments);
			});
			
			
			// 添加时时更新textarea域
			
			this.addUpdateTextarea();
			
			function attrchangefunc(e, attname, attval){
				
				var thisObj = this;
				var thisArguments = arguments;
				
				var iframeobj = $(this).next().find(".cke_wysiwyg_frame")[0];
				if (iframeobj && !$(iframeobj).is(":hidden") && iframeobj.contentWindow && iframeobj.contentWindow.document && iframeobj.contentWindow.document.body) {
					
					var editor = $(this).data("editorobj");
					
					
					if (attname == 'readonly' || attname == 'disabled') {
						if (attval) {
							$(this).next().find(".cke_top,.cke_bottom").hide();
							//$(this).next().find(".cke_contents,.cke_wysiwyg_frame").css("height", $(this).data('ckeditorheight'));
						} else {
							$(this).next().find(".cke_top,.cke_bottom").show();
							//	$(this).next().find(".cke_contents,.cke_wysiwyg_frame").css("height", $(this).data('ckeditorheight'));
						}
					}
					
					if (attname == 'readonly' || attname == 'disabled') {
						try { editor.setReadOnly(attval); } catch(e){}
					}
					
				} else {
					setTimeout(function(){
						attrchangefunc.apply(thisObj, thisArguments);
					}, 50);
				}
			}
		},
		
		refresh: function() {
			this._refreshInner();
		},
		
		_refreshInner:function() {
			var thisObj = this;
			var iframeobj = this.element.next().find(".cke_wysiwyg_frame");
		
			if (iframeobj.length && iframeobj[0].contentWindow && iframeobj[0].contentWindow.document && iframeobj[0].contentWindow.document.body) {
				var element = CKEDITOR.dom.element.get(thisObj.element.attr("id"));
				
				if ( element.getEditor() ) {
					var eleeditor = element.getEditor();
					resetData(iframeobj, eleeditor, thisObj.element.val());
				}
			} else {
				var thisArgs = arguments;
				setTimeout(function(){
					thisObj._refreshInner.apply(thisObj, thisArgs);
				}, 100);
			}
			
			function resetData(iframeobj, eleeditor, data) {
				if (!$(iframeobj).is(":hidden")) {
					eleeditor.setData(data);
					thisObj.addUpdateTextarea();
				} else {
					setTimeout(function(){
						resetData(iframeobj, eleeditor, data);
					}, 300);
				}
			}
		},
		
		addUpdateTextarea: function() {
			var thisObj = this;
			var iframeobj = $("#" + this.element.attr("id")).next().find(".cke_wysiwyg_frame");
			if (iframeobj.length && iframeobj[0].contentWindow && iframeobj[0].contentWindow.document && iframeobj[0].contentWindow.document.body) {
				$(iframeobj[0].contentWindow.document.body).data("textareaid", this.element.attr("id"));
				$(iframeobj[0].contentWindow.document.body).blur(function(){
					var tareaid = $(this).data("textareaid");
					var element = CKEDITOR.dom.element.get(tareaid);
					if ( element.getEditor() ) {
						var eleeditor = element.getEditor();
						eleeditor.updateElement();
					}
				});
			} else {
				var thisArgs = arguments;
				setTimeout(function(){
					thisObj.addUpdateTextarea.apply(thisObj, thisArgs);
				}, 100);
			}
		},

		destroy: function(callback) {
			try {
				var element = CKEDITOR.dom.element.get(this.element.attr("id"));
				element.getEditor().destroy();
			} catch(e){}
			
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );

// 常用控件的封装
jQuery.fn.extend({

	// 用数据重置表单
	resetForm: function(data, prefix) {
		var usetolower = 0;
		var formInputSeletors = ":input:not(:button),.tinymce,.fileupload";
		
		this.find(formInputSeletors).removeClass("ui-state-error");
		prefix = prefix || '';
		var sidform = $("[name="+prefix+"sid]", this);
		if (sidform.length == 0) {
			this.prepend("<input name='"+prefix+"sid' type='hidden'/>");
			sidform = $("[name="+prefix+"sid]", this);
		}

		if (data) {
			this.data('formdata', data);
			this.find(formInputSeletors).each(function(){
				var ifieldname = $(this).attr("name");
				if (usetolower == '1' && ifieldname) {
					ifieldname = ifieldname.toLowerCase();
				}
				if (ifieldname && !$.isPlainObject(data[ifieldname])) {
					var tval = data[ifieldname.replace(new RegExp("^"+prefix), '')];
					$(this).data("formdata", tval);
					if (tval != null) {
						if ($(this).hasClass("date") || $(this).hasClass("typicalDate")) {
							this.value = typicalDateFormatter(tval);
						} else {
							// 对单选框和复选框的处理
							if ($(this).is(":radio") || $(this).is(":checkbox")) {
								$(this).prop("checked", false);
								if (this.value == tval) {
									$(this).prop("checked", true);
									$(this).click();
									$(this).prop("checked", true);
								}
							} else {
								this.value = (tval == null||tval=='null'?'':tval);
								// 对下拉列表处理
								if ($(this).is("select")) {
									$(this).change();
								}
							}
							
							// 对bootstrap dropdown特殊处理
							var closestdropdown = $(this).closest(".dropdown");
							if (closestdropdown.length) {
								var currval = closestdropdown.find("li[value=" + this.value + "]");
								if (currval.length) {
									closestdropdown.find(".buttonlabel").text(currval.text());
								}
							}
						}
						
					} else {
						if ($(this).is(":radio") || $(this).is(":checkbox")) {
							if ($(this).attr("default"))
								$(this).prop("checked", true);
							else
								$(this).prop("checked", false);
						} else {
							this.value = "";
							if ( $(this).attr("default") )
								this.value = $(this).attr("default");
							$(this).data("formdata", "");
						}
						
						// 对bootstrap dropdown特殊处理
						var closestdropdown = $(this).closest(".dropdown");
						if (closestdropdown.length) {
							var tbtn = closestdropdown.find(".buttonlabel")
							tbtn.text(tbtn.attr("defaultbtnlabel"));
						}
					}
					
					// 文件上传下载控件处理
					if ($(this).hasClass("fileupload")) {
						$(this).fileupload("option", "filegroupsid", tval);
					}
				}
			});

		} else {
			var newsid = $.getNewsid();
			var newdata = {};
			newdata[prefix + "sid"] = newsid;
			this.data('formdata', newdata);
			this.find(formInputSeletors).each(function(){
				if ($(this).is(":radio") || $(this).is(":checkbox")) {
					if ($(this).attr("default"))
						$(this).prop("checked", true);
					else
						$(this).prop("checked", false);
				} else if ($(this).is("select") && $(this).attr("default")) {
					$(this).val($(this).attr("default"));
				} else if ( $(this).attr("default") ) {
					this.value = $(this).attr("default");
				} else {
					// 如果设置了默认显示今天的日期
					this.value = "";
					if ($(this).hasClass("date")) {
						if ($(this).hasClass("today")) {
							this.value = $.datepicker.formatDate('yy-mm-dd', new Date());
						}

						if ($(this).hasClass("yearbegin")) {
							this.value = $.datepicker.formatDate('yy-01-01', new Date());
						}

						if ($(this).hasClass("yearend")) {
							this.value = $.datepicker.formatDate('yy-12-31', new Date());
						}
					}
					$(this).data("formdata", this.value);
				}
				// 重置新的sid
				sidform.val(newsid).data(prefix + "sid", newsid);
				
				// 文件上传下载控件处理
				if ($(this).hasClass("fileupload")) {
					var newsid4file = $.getNewsid();
					$(this).val(newsid4file);
					$(this).fileupload("option", "filegroupsid", newsid4file);
				}
				
				// 对bootstrap dropdown特殊处理
				var closestdropdown = $(this).closest(".dropdown");
				if (closestdropdown.length) {
					var tbtn = closestdropdown.find(".buttonlabel")
					tbtn.text(tbtn.attr("defaultbtnlabel"));
				}
			});

		}
		// 对于富文本编辑器来说，需要重置
		this.initTinyMce();
		this.initCkeditor();
		
		// 表单重置完成后，抛出重置完成事件resetFormComplete，以方便自定义特殊表单的后续操作
		this.trigger("resetFormComplete");
		return this;
	},

	// 根据节点的类名包裹数据。通常用于非form节点下数据的包裹过程
	wrapDataByClass: function(data) {
		for (var key in data) {
			var obj = this.find("." + key);
			if (obj.is(".date")) { // 当为日期时
				this.find("." + key).text(typicalDateFormatter(data[key]));
			} else {
				this.find("." + key).text(data[key]);
			}
		}
		return this;
	},

	// 表单验证工具
	// isautosave 表明是否为暂存，若是的话，即使多页签存在问题也不提示
	validate: function(excludeRequired, isautosave) {
		var noerrored = {isnoerror:true};
		
		var allnoerroreds = [];
		var allinputs = this.find(":input,.fileupload");
		if (this.is(":input,.fileupload")) {
			allinputs = this;
		}
		
		var otheriframeinputs = [];
		// 搜寻嵌套iframe中的表单
		this.find("iframe").each(function() {
			if (this.contentWindow.$ && this.contentWindow.nestedbyform) {
				this.contentWindow.$(":input,.fileupload").each(function(){
					otheriframeinputs.push(this);
				});
			}
		});
		
		allinputs.each(function(){
			var errorinfo = revalidateformcell(this, excludeRequired);
			if (!errorinfo.isnoerror)
				allnoerroreds.push(errorinfo);
			noerrored.isnoerror = (errorinfo.isnoerror && noerrored.isnoerror);
			noerrored.errorscrope = (noerrored.errorscrope || errorinfo.errorscrope);
		});
		
		$(otheriframeinputs).each(function(){
			var errorinfo = revalidateformcell(this, excludeRequired);
			if (!errorinfo.isnoerror)
				allnoerroreds.push(errorinfo);
			noerrored.isnoerror = (errorinfo.isnoerror && noerrored.isnoerror);
			noerrored.errorscrope = (noerrored.errorscrope || errorinfo.errorscrope);
		});
		
		if (noerrored.errorscrope) {
			if (isautosave) {
				return noerrored.isnoerror;
			}
			alert("请将(" + noerrored.errorscrope + ")下的表单补充完整！");
			return noerrored.isnoerror;
		}
		
		if (useAlertToShowValidate && allnoerroreds.length) {
			var alertinfo = "请您完善如下表单域后再操作：\n";
			for (var i = 0; i < allnoerroreds.length; ++i) {
				alertinfo += allnoerroreds[i].fieldlabel;
				if (i != allnoerroreds.length -1)
					alertinfo += "、 \n"
			}
			alert(alertinfo);
		}
		
		return noerrored.isnoerror;
	},

	// 设置某选择器下当前dom元素状态
	setCurrentState:function (state) {
		state = state || '';
		this.data("currentState", state);
		$("[includeIn]", this).hide().each(function() {
			if ($.inArray(state, $(this).attr("includeIn").split(",")) != -1) {
				$(this).show();
			}
		});
		$("[excludeFrom]", this).show().each(function(){
			if ($.inArray(state, $(this).attr("excludeFrom").split(",")) != -1) {
				$(this).hide();
			}
		});
		
	
		$("[state]", this).each(function() {
			var states = $(this).attr("state").split(";");

			states = $.grep(states, function(elem){
				return elem != null && elem != '';
			});

			var defprop = [];
			var defstyle = [];
			for (var i = 0; i < states.length; ++i) {
				states[i] = states[i].replace(/\s/gi,'');
				if (states[i].indexOf(".") == -1) {
					var val = states[i].split(":")[1];
					if (val == 'false')
						val = false;
					if (val == 'true')
						val = true;
					if (states[i].charAt(0) == '$') {
						defstyle[states[i].split(":")[0].substring(1)] = val;
					} else {
						defprop[states[i].split(":")[0]] = val;
					}
				}
			}
			var settedattrstyle = [];
			for (var i = 0; i < states.length; i++) {
				if (states[i]) {
					var key = states[i].split(":")[0];
					var val = states[i].split(":")[1];
					if (val == 'false')
						val = false;

					if (val == 'true')
						val = true;

					var keyleft = key.split(".")[0];

					if (settedattrstyle[keyleft])
						continue;

					var keyright = key.split(".").length>1 ? key.split(".")[1] : "";
					if (state) {
						if (keyright == state) {
							if (keyleft.charAt(0) == '$') {
								$(this).css(keyleft.substring(1), val);
							} else {
								changePropInner(this, keyleft, val);
							}
							settedattrstyle[keyleft] = true;
						} else {
							if (keyleft.charAt(0) == '$') {
								if (defstyle[keyleft.substring(1)] != null)
									$(this).css(keyleft.substring(1), defstyle[keyleft.substring(1)]);
							} else {
								changePropInner(this, keyleft, defprop[keyleft]);
							}
						}
					} else {
						if (keyright)
							continue;

						if (keyleft.charAt(0) == '$') {
							if (defstyle[keyleft.substring(1)] != null)
								$(this).css(keyleft.substring(1), defstyle[keyleft.substring(1)]);
						} else {
							changePropInner(this, keyleft, defprop[keyleft]);
						}
						settedattrstyle[keyleft] = true;
					}
				}
			}
		});

		function changePropInner(thisObj, kleft, kright) {
			if (kright != null) {
				if (kleft == 'disabled' && !$(thisObj).is(":radio,:checkbox,.tinymce")) {
					if (!kright) {
						if ($(thisObj).is(".date")) {
							$(thisObj).datepicker({changeYear:true,changeMonth:true, yearRange:'c-50:c+50'});
						}
					} else {
						if ($(thisObj).data('datepicker'))
							$(thisObj).datepicker("destroy");
					}
					
					if (!kright) {
						if ($(thisObj).is(".datetime")) {
							$(thisObj).datetimepicker();
						}
					} else {
						if ($(thisObj).data('uiDatetimepicker'))
							$(thisObj).datetimepicker("destroy");
					}
					
					// 只读模式文本框处理
					var origreadonly = $(thisObj).data('origreadonly');
					if (origreadonly == null) {
						origreadonly = $(thisObj).prop("readonly");
						$(thisObj).data('origreadonly', origreadonly);
					}
					
					$(thisObj).prop('readonly', origreadonly || kright);
					
					if(kright) {
						if (!$(thisObj).is(".noteditable")) {
							$(thisObj).filter(":text,:password").addClass("noteditable").blur();
							$(thisObj).next(".requiredsource").addClass("requiredsource_trans");
						}
					} else {
						if ($(thisObj).is(".noteditable")) {
							$(thisObj).filter(":text,:password").removeClass("noteditable");
							$(thisObj).next(".requiredsource").removeClass("requiredsource_trans");
						}
					}
					// 下拉列表处理
					if ($(thisObj).is("select")) {
						
						// 为没有设置容器的下拉列表框设置容器
						var disabledspan = $(thisObj).prev(".disabledselect");
						if (disabledspan.length == 0) {
							disabledspan = $("<span class='disabledselect' style='display:none; display:inline-block;'/>");
							disabledspan.insertBefore(thisObj);
						}
						
						// 添加属性更改事件监听
						if (!$.browser.msie) {
							$(thisObj).unbind("change.disabledselect");
							$(thisObj).bind("change.disabledselect", function(){
								$(this).prev(".disabledselect").text($(this).find("option[selected=true]").text());
							});
						} else {
							$(thisObj).unbind("propertychange.disabledselect");
							$(thisObj).bind("propertychange.disabledselect", function(){
								if (event.propertyName != 'value')
									return;
								$(this).prev(".disabledselect").text($(this).find("option[selected=true]").text());
							});
							
							$(thisObj).unbind("change.disabledselect");
							$(thisObj).bind("change.disabledselect", function(){
								$(this).prev(".disabledselect").text($(this).find("option[value=" + $(this).val() + "]").text());
							});
						}
						
						// 初始使用当前值
						var currtext = null;
						if (!$.browser.msie) {
							$(thisObj).find("option").each(function() {
								if ($(this).prop("selected")) {
									currtext = $(this).text();
									return false;
								}
							});
							
							currtext = currtext || '';
						} else {
							currtext = $(thisObj).find("option[value=" + $(thisObj).val() + "]").text(); 
						}
						
						if (kright) {
							var width = $(thisObj).width();
							if (width != 0)
								disabledspan.width($(thisObj).width());
							disabledspan.text(currtext).css({marginRight:$(thisObj).css("marginRight"), marginLeft:$(thisObj).css("marginLeft")}).show();
							$(thisObj).hide();
						} else {
							disabledspan.hide();
							$(thisObj).show();
						}
					}
					
					
					// bootstrap下拉列表处理
					if ($(thisObj).is(".dropdown")) {
						
						var dropdownhiddeninput = $(thisObj).find("input:hidden");
						if (!$.browser.msie) {
							dropdownhiddeninput.unbind("change.disabledselect");
							dropdownhiddeninput.bind("change.disabledselect", function(){
								$(this).closest(".dropdown").prev(".disabledselect")
								.text($(this).closest(".dropdown").find("li[role=presentation][value=" + this.value +  "]").text());
							});
						} else {
							dropdownhiddeninput.unbind("propertychange.disabledselect");
							dropdownhiddeninput.bind("propertychange.disabledselect", function(){
								if (event.propertyName != 'value')
									return;
								$(this).closest(".dropdown").prev(".disabledselect")
								.text($(this).closest(".dropdown").find("li[role=presentation][value=" + this.value +  "]").text());
							});
						}
						
						// 为没有设置容器的下拉列表框设置容器
						var disabledspan = $(thisObj).prev(".disabledselect");
						if (disabledspan.length == 0) {
							disabledspan = $("<span class='disabledselect' style='display:none; display:inline-block;'/>");
							disabledspan.insertBefore(thisObj);
						}
						
						// 初始使用当前值
						var currtext = null;
						if (!$.browser.msie) {
							$(thisObj).find("option").each(function() {
								if ($(this).prop("selected")) {
									currtext = $(this).text();
									return false;
								}
							});
							
							currtext = currtext || '';
						} else {
							currtext = $(thisObj).find("li[role=presentation][value=" + dropdownhiddeninput.val() +  "]").text(); 
						}
						
						if (kright) {
							var width = $(thisObj).width();
							if (width != 0)
								disabledspan.width($(thisObj).width());
							disabledspan.text(currtext).css({marginRight:$(thisObj).css("marginRight"), marginLeft:$(thisObj).css("marginLeft")}).show();
							$(thisObj).hide();
						} else {
							disabledspan.hide();
							$(thisObj).show();
						}
					}
					
					$(thisObj).trigger("attrchange",[kleft, kright]);
				} else {
					$(thisObj).prop(kleft, kright).trigger("attrchange",[kleft, kright]);
				}
			}
		}

		return this;
	},

	getCurrentState: function() {
		return this.data("currentState") || '';
	},

	exportForm: function(origOptions) {
		var options = {
				callback:$.noop
			}
		var opts = $.extend (true, {}, options, origOptions);
		var thisObj = this;
		opts.callback();

		var newForm = this.clone();
		newForm.find(":input").filter(function(){
			return $(this).css("display") != 'none';
		}).each(function(){

			var self = $(this);
			if (self.is("select")) {
				self[0].outerHTML = self.find("option[selected=true]").html();
			} else if (self.is(":checkbox,:radio")){
				self.remove();
			} else {
				this.outerHTML = self.val();
			}
		});

		$.postIframe(ctxPath + '/components/export.jsp', {exphtml:newForm.html()});
	},

	initTinyMce: function(isfrominitWidget) {
		var tinymss = this.filter(".tinymce, textarea");
		if (isfrominitWidget)
			tinymss = this.filter(".tinymce");
		
		/* 测试用百度付文本
		tinymss.add($(".tinymce", this)).each(function(){
			if (!$(this).data("isinitingmce")) {
				$(this).data("isinitingmce", true);
				var ue = new baidu.editor.ui.Editor();
				var neweditorname = $.Guid.New();
				var thisname = $(this).attr("name");
				$(this).attr("id", neweditorname);
				
				ue.render(neweditorname);
				ue.setContent($(this).val());
				ue.addListener("selectionchange",function(){
					/*
					var state = ue.queryCommandState("source");
					var btndiv = document.getElementById("btns");
					if(btndiv){
						if(state){
							btndiv.style.display = "none";
						}else{
							btndiv.style.display = "";
						}
					}
				});
				$("#" + neweditorname).attr("name", thisname);
				$("#" + neweditorname).data("isinitingmce", true);
				$("#" + neweditorname).data("fulleditor", ue);
				
			} else {
				$(this).data("fulleditor").setContent($(this).data("formdata"));
			}
		});
		return;*/

		// 初始化富文本编辑器
		tinymss.add($(".tinymce", this)).each(function(){
			if ($(this).next().hasClass("mceEditor")) {
				$("iframe", $(this).next())[0].contentWindow.document.body.innerHTML = ($(this).data("formdata") == null?'':$(this).data("formdata"));
				$(this).val($(this).data("formdata"));

				if ($(this).prop("disabled")) {
					$("iframe", $(this).next())[0].contentWindow.document.body.contentEditable = false;
					$("iframe", $(this).next())[0].contentWindow.document.body.onselectstart = function() {return false;};
				} else {
					$("iframe", $(this).next())[0].contentWindow.document.body.contentEditable = true;
					$("iframe", $(this).next())[0].contentWindow.document.body.onselectstart = function() {return true;};
				}

			} else {
				if (!$(this).data("isinitingmce")) {
					
					var imgbtn = "";
					if ($(this).is(".img"))
						imgbtn = "image,";
					
					$(this).tinymce({
						// Location of TinyMCE script
						script_url : ctxPath + '/js/tiny_mce/tiny_mce.js',

						// General options
						theme : "advanced",

						// Theme options
						theme_advanced_buttons1 : "cut,copy,paste,pastetext,pasteword,bold,italic,pasteword,underline,|," + imgbtn + "justifyleft,justifycenter,justifyright,justifyfull,fontselect,fontsizeselect,forecolor",
						theme_advanced_toolbar_location : "top",
						//readonly: true,
						theme_advanced_toolbar_align : "left"
					});
					$(this).data("isinitingmce", true);
				}
				var thisObj = this;
				setTimeout(function(){
					$(thisObj).initTinyMce();
				}, 100);
			}

			$(this).unbind("attrchange").bind("attrchange", function() {
				$(this).initTinyMce();
			});
		});
		$(".tinymce", this).parent().find("a").attr("tabIndex",-1);
	},
	
	// 注意此组件仅支持到IE8以上
	initCkeditor: function (isfrominitWidget) {
		var tinymss = this.filter(".ckeditor4, textarea");
		if (isfrominitWidget) {
			tinymss = this.filter(".ckeditor4");
			if (window['CKEDITOR']) {
				CKEDITOR.on( 'instanceReady', function( ev ) {
					$("#" + ev.editor.name).data("editorobj", ev.editor);
				});
			}
		}
		// 初始化富文本编辑器
		tinymss.add($(".ckeditor4", this)).each(function(){
			var ckeditorready = $(this).data("ckeditorready");
			if (ckeditorready) {
				$(this).ckeditor4("refresh");
			} else {
				$(this).ckeditor4();
				$(this).data("ckeditorready", true);
			}
		});
	},

	// 初始化界面组件
	initWidgets: function(isinit) {
		// 切换标题
		if (/moduleid=.*/.exec(location.href)) {
			var moduleid = (/moduleid=.*/.exec(location.href) + "").split("=")[1];
			$(".moduletitle").load(ctxPath+'/common/SystemParamsController/getModuleTitleById.vot?moduleid='+moduleid);
		}

		// 统一输入框颜色高度
		$("input:text", this).addClass("textcss");
	
		// 添加按钮样式切换事件监听
		// 将下拉列表排除在外
		if (isinit) {
			this.on("mouseenter", "input", function(){
				var self = $(this);
				var thisclass = self.attr("class");
				var hoverclass = "";
				var focusclass = "";
				if (thisclass && thisclass.indexOf(" ") == -1) {
					hoverclass = thisclass+"_hover";
					focusclass = thisclass+"_focus";
				} else if (thisclass) {
					hoverclass = thisclass.split(" ")[0]+"_hover";
					focusclass = thisclass.split(" ")[0]+"_focus";
				}

				if (hoverclass)
					self.addClass(hoverclass);
			}).on("mouseleave", "input", function(){
				var self = $(this);
				var thisclass = self.attr("class");
				var hoverclass = "";
				var focusclass = "";
				if (thisclass && thisclass.indexOf(" ") == -1) {
					hoverclass = thisclass+"_hover";
					focusclass = thisclass+"_focus";
				} else if (thisclass) {
					hoverclass = thisclass.split(" ")[0]+"_hover";
					focusclass = thisclass.split(" ")[0]+"_focus";
				}
				if (hoverclass)
					self.removeClass(hoverclass);
			}).on("mousedown", "input", function(){
				var self = $(this);
				var thisclass = self.attr("class");
				var hoverclass = "";
				var focusclass = "";
				if (thisclass && thisclass.indexOf(" ") == -1) {
					hoverclass = thisclass+"_hover";
					focusclass = thisclass+"_focus";
				} else if (thisclass) {
					hoverclass = thisclass.split(" ")[0]+"_hover";
					focusclass = thisclass.split(" ")[0]+"_focus";
				}
				if (focusclass)
					self.addClass(focusclass);
			}).on("mouseup", "input", function(){
				var self = $(this);
				var thisclass = self.attr("class");
				var hoverclass = "";
				var focusclass = "";
				if (thisclass && thisclass.indexOf(" ") == -1) {
					hoverclass = thisclass+"_hover";
					focusclass = thisclass+"_focus";
				} else if (thisclass) {
					hoverclass = thisclass.split(" ")[0]+"_hover";
					focusclass = thisclass.split(" ")[0]+"_focus";
				}
				if (focusclass)
					self.removeClass(focusclass);
			});
		}
		
		// 初始化基础界面控件
		$(".tabs", this).each(function(){
			var self = $(this);

			tabnodes = $.makeArray(self.find("a[href^=#]"));
			var selectedindex = $.inArray(self.find("a[selected],a[selected2]")[0], tabnodes);
			if (selectedindex == -1)
				selectedindex = 0;
			
			self.tabs({load:function(event, ui){
				$(ui.panel).initWidgets();
			}, cache:true,  ajaxOptions: { async: false }, selected: selectedindex, active: selectedindex});
		});
		
		if ($(".date", this).datepicker) { // 日期控件组件存在时调用
			$(".date", this).datepicker({changeYear:true, changeMonth:true, yearRange:'c-50:c+50'}).each(function(){
				if ($(this).hasClass("today")) {
					this.value = $.datepicker.formatDate('yy-mm-dd', new Date());
				}
	
				if ($(this).hasClass("yearbegin")) {
					this.value = $.datepicker.formatDate('yy-01-01', new Date());
				}
	
				if ($(this).hasClass("yearend")) {
					this.value = $.datepicker.formatDate('yy-12-31', new Date());
				}
			}).attr("date", "true");
		}
		// 初始化日期时间
		$(".datetime", this).datetimepicker();
			
		$(".button", this).each(function(){
			var classes = $(this).attr("class").split(" ");
			var hasbuttoned = false;
			for (var i = 0; i < classes.length; i++) {
				var clas = $.trim(classes[i]);
				if (clas.indexOf("ui-icon-") != -1) {
					$(this).button({
						icons: { primary:clas }
					})
					hasbuttoned = true;
					break;
				}
				if (!hasbuttoned) {
					$(this).button();
				}
			}
		});
		
		
		// 初始化 dropdown
		$(".dropdown", this).each(function(){
			$(this).find("[role=presentation]").click(function(){
				$(this).closest(".dropdown").find(".buttonlabel").text($(this).text());
				$(this).closest(".dropdown").find("input:hidden").val($(this).attr("value"));
			});
		});
		
		/*
		$(".fileupload", this).andSelf().filter(".fileupload").each(function(){
			var self = $(this);
			self.fileupload({
				filegroupsid: self.prop("value"),
				readonly: !!self.prop("readonly2"),
				autosave: !!self.prop("autosave")
			});
		});*/

		$(".m_head", this).unbind("click").click(function(){
			$("."+this.id).toggle();
			$(this).toggleClass("m_head_close");
		});

		// 初始化布局
		$('.ui-layout').each(function(){
			var tthisObj = this;
			setTimeout(function(){
				$(tthisObj).layout({fxName:'none'});
			}, 200);
		});
		
		/**
		 * Fn 初始textarea最大字数限制
		 * Simple Demo <textarea maxlength="1000"
		 */
		$('textarea[maxlength]',this).each(function(){
			if ($(this).attr("textarea_maxlength_inited"))
				return;

			$(this).parent().append("<div></div>");

			$(this).attr("textarea_maxlength_inited", true);
		});
		$('textarea[maxlength]~div').css({
		position:'absolute',
		fontSize:'9pt',
		color:'#4586b5',
		width:'80px',
		textAlign:'right',
		display:'none'
		});
	
		 $('textarea[maxlength]').bind('propertychange', function() {
			 if (event.propertyName == 'value' && $.trim(this.value)) {
					var val = $(this).data("oldvalforvalidate");
					if (val == null || val != this.value) {
						$(this).data("oldvalforvalidate", this.value);
						 $(this).showWordCount();
					}
				}
		  });
		  $('textarea[maxlength]').focus(function(){
		      $(this).showWordCount();
		      $(this).parent().find('div').fadeIn('slow');
		  });

		  $('textarea[maxlength]').blur(function(){
			  $(this).parent().find('div').fadeOut('slow');
		  });
		  
		  /**
			 * Fn 初始input最大字数限制
			 * Simple Demo <input maxlength="1000"
			 */
			
			 $('input[maxlength]').bind('keydown',function(){
				 var self=$(this);
				 if ($.trim(this.value)) {
							checkmaxlength(this); 
					}
			  });
			  function checkmaxlength(thisObj){
				     var self=$(thisObj);
					 var mlength=self.attr("maxlength");
					 var clength= self.val().length;
					 if(mlength===clength){
							self.addClass("ui-state-error").attr("title", "已经达到最大字数"+mlength);
						}else{
							self.removeClass("ui-state-error").attr("title","");
						}
			  }

		// 全局表单提示性验证
		this.find(":input,.fileupload").andSelf().each(function(){
			var self = $(this);
			self.removeClass("ui-state-error");
			
			if (self.attr("required2") != 'true' && self.attr("required2") != true && self.prop("required") != 'true' && self.prop("required") != true) { // 若未设置required属性
				self.removeAttr("requiredwidgetinited");
				while(self.next(".requiredsource").length)
					self.next(".requiredsource").remove();
			} else {
				if (self.attr("requiredwidgetinited"))
					return;
				self.after("<span class='requiredsource'/>");
				self.attr("requiredwidgetinited", true);
			}
			
			if (self.attr("validatebinded"))
				return;
			
			self.bind("blur.validate", function(){
				revalidateformcell(this);
			});
			
			// 如果为input文本框，并且为IE浏览器则直接赋值事件，修补IE10退格键引起的输入框消失的问题
			if ((self.is("input:text") || self.is(":password"))&& $.browser.msie) {
				
				if (self.is(":password")) {
					$(this).change(function(){
						var val = $(this).data("oldvalforvalidate");
						if (val == null || val != this.value) {
							$(this).data("oldvalforvalidate", this.value);
							revalidateformcell(this);
						}
					});
				} else {
					this.onpropertychange = function(){
						if (event.propertyName == 'value' && $.trim(this.value)) {
							var val = $(this).data("oldvalforvalidate");
							if (val == null || val != this.value) {
								$(this).data("oldvalforvalidate", this.value);
								revalidateformcell(this);
							}
						}
					}
				}
				
			} else {
				self.bind("propertychange.validate", function(){
					if (event.propertyName == 'value' && $.trim(this.value)) {
						var val = $(this).data("oldvalforvalidate");
						if (val == null || val != this.value) {
							$(this).data("oldvalforvalidate", this.value);
							revalidateformcell(this);
						}
					}
				});
			}
			
			self.attr("validatebinded", true);
		});

		// 初始化富文本编辑器
		this.initTinyMce(true);// tinymce版本
		this.initCkeditor(true); // Ckeditor版本
		
		// 初始化树结构选择组件
		$("input.ztree", this).each(function(){
			var self = $(this);
			if (self.attr("ztreeselectinited"))
				return;
			
			self.attr("ztreeselectinited", true).attr("readonly", true);
			
			var ztrecontainer = $('<ul class="tree" style="width:265px; height:245px; background-color:white; position:absolute; overflow-y:auto; overflow-x:auto; border:1px solid black; display: none;"></ul>');
			self.after(ztrecontainer);
			
			ztrecontainer.click(function(e) {
				e.stopPropagation();
			});
			
			self.click(function(e){
				var selfofset = self.position();
				ztrecontainer.css({left:selfofset.left, top:selfofset.top + self.height()});
				if (ztrecontainer.data("uiTree"))
					ztrecontainer.tree("destroy");
				ztrecontainer.tree({
					click: function(event, treeId, treeNode) {
						if (self.attr("validateFunction")) {
							if (window[self.attr("validateFunction")](treeNode) === false) {
								return;
							}
						}
						
						if (self.attr("showfullpath")) {
							var pathnamearr = [];
							var pnode = treeNode; 
							while(pnode) {
								pathnamearr.unshift(pnode.name);
								pnode = pnode.parentNode;
							}
							self.val(pathnamearr.join(" > "));
						} else {
							self.val(treeNode.name);
						}
						
						if (self.attr("sidfieldname")) {
							self.closest("form").find("[name=" + self.attr("sidfieldname") + "]").val(treeNode.sid);
						}
						ztrecontainer.hide();
						
						self.change();
					},
					asyncUrl: ctx + self.attr("dataProvider")
				});
				ztrecontainer.show();
				e.stopPropagation();
			});
			
			hideWidgetPartsUtilFunction_(ztrecontainer, $.Guid.New().substring(0,8));
		});
		
		// 初始化变高textarea
		$("textarea.heightadapt", this).andSelf().filter("textarea.heightadapt").each(function(){
			var self = $(this);
			if (self.attr("heightadaptwidgetinited"))
				return;
			
			var leastheight = self.data("leastheight");
			if (!leastheight) {
				leastheight = Math.max(self.attr("scrollHeight"), self.height());
				self.data("leastheight", leastheight);
			}
			
			self.keydown(function(e){
				var tleastheight = $(this).data("leastheight");
				
				var entersum = 0;
				for(var i = 0; i < this.value.length; i++) {
					if (this.value.charAt(i) == '\n') {
						entersum++ ;
					}
				}
				var scrollheight = $(this).attr("scrollHeight");
				if (e.keyCode == 13) {
					scrollheight += 7;
				}
				if (scrollheight <= tleastheight)
					scrollheight = tleastheight;
				else
					scrollheight += 2;
	
				$(this).height(scrollheight);
			});
			
			self.bind("propertychange.heightadapt", function(){
				if (event.propertyName == 'value') {
					var val = $(this).data("oldvalforheightadapt");
					if (val == null || val != this.value) {
						$(this).data("oldvalforheightadapt", this.value);
						$(this).keyup();
					}
				}
			});
			
			self.keyup(function(e){
				var thisObj = this;
				var tleastheight = $(this).data("leastheight");
				var entersum = 0;
				var scrollheight = $(this).attr("scrollHeight");
				
				if (scrollheight == 0) {  // 隐藏变显示的时候可能为0
					setTimeout(function() {
						$(thisObj).keyup();
					}, 20);
					return;
				}
				
				if (scrollheight < tleastheight)
					scrollheight = tleastheight;
				
				if (scrollheight <= tleastheight)
					scrollheight = tleastheight;
				else
					scrollheight += 2;
				
				
				
				$(this).height(scrollheight);
			}).keyup();
			
			self.attr("heightadaptwidgetinited", true);
		});
		
		// 初始化下拉列表选择
		$("select[dataProvider]", this).andSelf().filter("select[dataProvider]").each(function() {
			var thisObj = $(this);
			var dataProvider = $(this).attr("dataProvider");
			if (!/.vot/.test(dataProvider)) {// 不以 .vot结尾的应该是从系统 resource中enums中寻找对应枚举类型
				$.ajax({
					url:ctxPath + "/getsystemenumdatas.vot?xmlname="+dataProvider,
					async:false,
					dataType:"json",
					success:function(data) {
						var opts = "";
						$(data).each(function(){
							if (thisObj.attr("default") == this.value || this.selected)
								opts += "<option enum='true' value='"+this.value+"' selected>"+this.label+"</option>";
							else
								opts += "<option enum='true' value='"+this.value+"'>"+this.label+"</option>";
						});
						thisObj.find("[enum]").remove().end().append(opts);
					}
				});
			} else {
				$.ajax({
					url:ctxPath + dataProvider,
					async:false,
					dataType:"json",
					success:function(data) {
						var opts = "";
						$(data).each(function(){
							if (thisObj.attr("default") == this.value || this.selected)
								opts += "<option enum='true' value='"+this.value+"' selected>"+this.label+"</option>";
							else
								opts += "<option enum='true' value='"+this.value+"'>"+this.label+"</option>";
						});
						thisObj.find("[enum]").remove().end().append(opts);
					}
				});
			}
		});
		
		// 初始化文件上传下载
		/*
		$(".fileupload", this).andSelf().filter(".fileupload").each(function() {
			var self = $(this);
			if (self.attr("fileuploadinited")) {
				return;
			}
		
			var orignsets = {
				filegroupsid: self.val(),
				autosave: true,
				readonly: false
			}
			
			for(var key in fileuploadorginoptions) {
				var optval = self.attr(key);
				if (optval != null) {
					if (optval == 'false')
						optval = false;
					orignsets[key] = optval;
				}
			}
			
			self.fileupload(orignsets);
			
			self.attr("fileuploadinited", true);
		});*/
		
		if (isinit && typeof creationComplete != 'undefined' && $.isFunction(creationComplete)) {
			creationComplete();
		}

		return this;
	},
	
	// 通用表单提交对话框
	submitDialog:function(origOptions) {
		var options = {
			url:null,
			callback:$.noop,
			width: $(window).width() - 50,
			height: $(window).height() - 50,
			title:"",
			 show: $.dialogeffect.show,
			 hide: $.dialogeffect.hide,
			state:null,
			resizable: true,
			close: $.noop,
			
			/* 顶级窗口模式下参数begin ->*/
			dialogTopIframeURL: null, // 顶级窗口形式的dialog的url，若未空则不使用顶级窗口模式，若含有值则使用，顶级窗口模式下，某些参数将不可用，如自动暂存功能
			dialogTopLoadedComplete: $.noop, // 顶级窗口模式下表单jsp完成加载时调用
			/*<- 顶级窗口模式下参数end*/
			
			isinline: false, // 是否为行内对话框 
			
			submitLabel:$.i18n('antelope.ok'),
			successTip:$.i18n('antelope.savesuccess'),
			validateFunction:$.noop,
			tempSaveLabel: "暂存",
			autoTempSaveEnabled: false, // 是否开启自动暂存，5分钟暂存一次
			showTempSave: false // 是否显示暂存按钮，此接口目前仅工作流全界面组件使用，其他功能暂禁止使用
		}
		var opts = $.extend (true, {}, options, origOptions);
		
		if (opts.width == null) {
			if (opts.dialogTopIframeURL)
				opts.width = top.$(top).width() - 50;
			else
				opts.width = $(window).width() - 50;
		}
		
		if (opts.height == null) {
			if (opts.dialogTopIframeURL)
				opts.height = top.$(top).height() - 50;
			else
				opts.height = $(window).height() - 50;
		}
		
		var thisObj = this;
		
		var tempsaveinterid = null;
		
		var topifarmedialogobj = null; // 顶级窗口对象
		
		var thisObjContainer = null;
		var btnbardiv = null;
		
		function autoSaveTipShow(text) {
			var autosavediv = thisObj.parent().find(".autosave2");
			if (autosavediv.length == 0){
				autosavediv = $("<div class='autosave2'></div>");
				thisObj.parent().append(autosavediv);
			}
			autosavediv.text(text);
			if (tempsaveinterid) { // 点击了取消之后不在出现暂存成功的字样
				autosavediv.fadeIn("fast");
				setTimeout(function(){
					autosavediv.fadeOut();
				}, 3000);
			}
		}
	
		function getSubmitFunction(istempsave, isauto) {
			return function(e){
				if ($(e.target).data("beginsubmit")) {
					return;
				}
				
				$(e.target).data("beginsubmit", true);
				
				var submitFuncThisObj = thisObj;
				
				if (opts.dialogTopIframeURL) { // 处理顶级窗口模式
					var iframecontwin = topifarmedialogobj.find("iframe")[0].contentWindow;
					submitFuncThisObj = iframecontwin.$("body").find("form");
				}
				
				if (opts.validateFunction() != false && submitFuncThisObj.validate(istempsave) && (istempsave || confirm($.i18n('antelope.confirmok')))) {
					if (!isauto) {
						$(e.target)._setButtonCommitting(true);
					}
					
					var turlparams = encodeURI(submitFuncThisObj.serialize());
					if (turlparams) 
						turlparams += "&tempsave=" + istempsave;
					else
						turlparams = "tempsave=" + istempsave;
					
					$.post(opts.url, turlparams, function(data){
						if (data) {
							if (data.length < 400) {
								alert(data);
								$(e.target)._setButtonCommitting(false);
								$(e.target).data("beginsubmit", false);
							} else {
								return;
							}
							 return;
						}
						
						if (isauto) { // 自动暂存时
							autoSaveTipShow('自动暂存成功！');
						} else {
							alert(opts.successTip);
							$(e.target)._setButtonCommitting(false);
							clearTempsaveIntervel();
							
							if (opts.dialogTopIframeURL) { // 处理顶级窗口模式
								if (topifarmedialogobj.data('uiDialog'))
									topifarmedialogobj.dialog("close");
							} else {
								if (thisObj.data('uiDialog'))
									thisObj.dialog("close");
							}
							
							opts.callback.call(submitFuncThisObj);
						}
						$(e.target).data("beginsubmit", false);
						
						if (opts.isinline){
							goBackToList();
						}
						
					});
				} else {
					if (isauto) {
						autoSaveTipShow('自动暂存失败，原因为：表单验证失败！');
					}
					$(e.target).data("beginsubmit", false);
				}
			};
		}
		var basicbtns = [{
			text:opts.submitLabel,
			click: getSubmitFunction(0, false)
		},{
			text:$.i18n('antelope.cancel'),
			click:function() {

				if (opts.dialogTopIframeURL) {	// 处理顶级窗口模式
					topifarmedialogobj.dialog("close");
					opts.close();
					return;
				}
				
				if (opts.autoTempSaveEnabled && opts.showTempSave) { // 若开启自动暂存，则点击取消也应该回调提交完成的回调
					clearTempsaveIntervel();
					opts.callback.call(thisObj);
				}
				
				if (opts.isinline){
					goBackToList();
				} else {
					if (thisObj.data('uiDialog'))
						thisObj.dialog("close");
					opts.close();
				}
			}
		}];
		
		function clearTempsaveIntervel() {
			clearInterval(tempsaveinterid);
			tempsaveinterid = null;
			thisObj.parent().find(".autosave2").remove();
		}
		
		if (opts.showTempSave) {
			basicbtns.unshift({text:opts.tempSaveLabel, click:getSubmitFunction(1, false)});
		}
		
		function goBackToList() {
			$("body").viewNavigator("popView");
			btnbardiv.remove();
			thisObj.unwrap().hide();
		}
		
		if (opts.dialogTopIframeURL) {
			topifarmedialogobj = $.dialogTopIframe({
				url: opts.dialogTopIframeURL, width: opts.width, height: opts.height, title: opts.title,
				buttons: basicbtns,
				loadComplete: function(thisdialogifr) {
					var topwinfrom = thisdialogifr.$("body").find("form");
					opts.dialogTopLoadedComplete.call(topwinfrom);
				}
			});
		} else if (opts.isinline){// 行内形式
			thisObjContainer = thisObj.wrap("<div></div>").parent();
			if (window['componentsv2']) {
				btnbardiv = $("<div style='padding:7px; background:white;'></div>");
			} else {
				btnbardiv = $("<div style='padding:7px; background:white; border-bottom:1px dotted;'></div>");
			}
			thisObjContainer.prepend(btnbardiv);
			thisObj.show();
			$("body").viewNavigator("pushView", thisObjContainer);
			
			if (window['componentsv2']) {
				$("<button class='btn btn-default'><span class='glyphicon glyphicon-chevron-left'></span></button>").click(goBackToList).appendTo(btnbardiv).css("margin-right", 6);
			} else {
				$("<button class='button'>&lt; 返回列表</button>").click(goBackToList).appendTo(btnbardiv).button();
			}
			
			if (window['componentsv2']) {
				var buttongroup = $("<div class='btn-group'/>");
				for (var i = 0; i < basicbtns.length; ++i) {
					var tbasicbtn = basicbtns[i];
					$("<label class='btn btn-default'>" + tbasicbtn.text + "</label>").click(tbasicbtn.click).appendTo(buttongroup);
				}
				buttongroup.appendTo(btnbardiv);
			} else {
				for (var i = 0; i < basicbtns.length; ++i) {
					var tbasicbtn = basicbtns[i];
					if (window['componentsv2']) {
						$("<button class='btn btn-sm btn-default'>" + tbasicbtn.text + "</button>").click(tbasicbtn.click).appendTo(btnbardiv).css("margin-left", 6);
					} else {
						$("<button class='button'>" + tbasicbtn.text + "</button>").click(tbasicbtn.click).appendTo(btnbardiv).button().css("margin-left", 6);
					}
				}
			}

		} else {
			this.dialog({
				buttons: basicbtns, width:opts.width, height:opts.height, modal:true, title:opts.title, resizable: opts.resizable,
				 show: opts.show,
				 hide: opts.hide,
				close: function() {
					if (opts.autoTempSaveEnabled && opts.showTempSave) { // 若开启自动暂存，则点击取消也应该回调提交完成的回调
						clearTempsaveIntervel();
						opts.callback.call(thisObj);
					}
					
					opts.close();
				}
			});
			
			// 修复  jquery-ui 1.11.2缺陷  在modal:true时，在dialog中的datepicker点击时将会反复弹出
			$(document).unbind("focusin");
			
		}
		
		if (opts.state) {
			this.setCurrentState(opts.state);
		}
		
		if (opts.autoTempSaveEnabled && opts.showTempSave) {
			var tempsavefunct = getSubmitFunction(1, true); 
			tempsaveinterid = setInterval(function(){
				tempsavefunct({target: thisObj.parent().find("span:contains('" + opts.tempSaveLabel + "')")[0]});
			}, tempsaveinterval);
		}
		
		// 屏蔽默认回车提交
		this.submit(function(){
			$(this).parent().find(".ui-dialog-buttonpane button:first").click();
			return false;
		});
		
		// 对文件上传组件进行一下特殊处理，打开对话框之后重新进行初始化
		$(".fileupload", this).each(function(){
			var self = $(this);
			if (self.data("uiFileupload")) { //  标志着fileupload已经初始化，则均使用原来的值进行重新初始化
				
				var filegsid = self.fileupload("option", "filegroupsid");
				var filegreadonly = self.fileupload("option", "readonly");
				var filegautosave = self.fileupload("option", "autosave");
				self.fileupload("destroy");
				self.fileupload({
					filegroupsid: filegsid,
					readonly: filegreadonly,
					autosave: filegautosave
				});
			} else {
				if (self.data("uiFileupload"))
					self.fileupload("destroy");
				
				self.fileupload({
					filegroupsid: self.prop("value"),
					readonly: !!self.prop("readonly2"),
					autosave: !!self.prop("autosave")
				});
			}
		});
		
		return this;
	},

	// 通用查看对话框
	viewDialog: function(origOptions) {
		var options = {
			dataProvider: null,
			width: $(window).width() - 50,
			height: $(window).height() - 50,
			
			/* 顶级窗口模式下参数begin ->*/
			dialogTopIframeURL: null, // 顶级窗口形式的dialog的url，若未空则不使用顶级窗口模式，若含有值则使用，顶级窗口模式下，某些参数将不可用，如自动暂存功能
			dialogTopLoadedComplete: $.noop, // 顶级窗口模式下表单jsp完成加载时调用
			/*<- 顶级窗口模式下参数end*/
			
			isinline: false,
			
			state: null,
			callback: $.noop,
			title: ""
		}
		var opts = $.extend (true, {}, options, origOptions);
		
		if (opts.width == null) {
			if (opts.dialogTopIframeURL)
				opts.width = top.$(top).width() - 50;
			else
				opts.width = $(window).width() - 50;
		}
		
		if (opts.height == null) {
			if (opts.dialogTopIframeURL)
				opts.height = top.$(top).height() - 50;
			else
				opts.height = $(window).height() - 50;
		}
		
		var thisObj = this;
		var topifarmedialogobj = null;
		
		var thisObjContainer = null;
		var btnbardiv = null;
		
		if (opts.dataProvider) {
			this.resetForm(opts.dataProvider);
		}


		function goBackToList() {
			$("body").viewNavigator("popView");
			btnbardiv.remove();
			thisObj.unwrap().hide();
		}

		if (opts.dialogTopIframeURL) {
			topifarmedialogobj = $.dialogTopIframe({
				url: opts.dialogTopIframeURL, width: opts.width, height: opts.height, title: opts.title,
				buttons:[{
					text: $.i18n('antelope.close'),
					click:function() {
						
						if (opts.dialogTopIframeURL) {	// 处理顶级窗口模式
							topifarmedialogobj.dialog("destroy");
							return;
						}
						
						opts.callback();
						thisObj.dialog("destroy");
					}
				}],
				loadComplete: function() {
					var topiconwin = topifarmedialogobj.find("iframe")[0].contentWindow; 
					var topwinfrom = topiconwin.$("body").find("form");
					opts.dialogTopLoadedComplete.call(topwinfrom);
				}
			});
		} else if (opts.isinline){// 行内形式
			thisObjContainer = thisObj.wrap("<div></div>").parent();
			if (window['componentsv2']) {
				btnbardiv = $("<div style='padding:7px; background:white;'></div>");
			} else {
				btnbardiv = $("<div style='padding:7px; background:white; border-bottom:1px dotted;'></div>");
			}
			thisObjContainer.prepend(btnbardiv);
			thisObj.show();
			$("body").viewNavigator("pushView", thisObjContainer);
			
			if (window['componentsv2']) {
				$("<button class='btn btn-default'><span class='glyphicon glyphicon-chevron-left'></span></button>").click(goBackToList).appendTo(btnbardiv).css("margin-right", 6);
			} else {
				$("<button class='button'>&lt; 返回列表</button>").click(goBackToList).appendTo(btnbardiv).button();
			}
			
		} else {
			this.dialog({
				buttons:[{
					text: $.i18n('antelope.close'),
					click:function() {
						opts.callback();
						thisObj.dialog("destroy");
					}
				}], width:opts.width, height:opts.height, modal:true, title:opts.title
			});
		}
		
		if (opts.state) {
			this.setCurrentState(opts.state);
		}
	},

	// 通用表单提交方法
	submitForm: function(origOptions) {
		var options = {
			url:null,
			confirmText:$.i18n('antelope.form.confirmoperat'),
			callback:$.noop,
			button:null,
			successTip:$.i18n('antelope.savesuccess')
		}
		var opts = $.extend (true, {}, options, origOptions);
		
		var thisObj = this;
		
		opts.data = encodeURI(thisObj.serialize());
		var thisCallback = opts.callback;
		opts.callback = function() {
			
			// 将所有需要持久化保存的文件持久化保存
			thisObj.find(".fileupload").each(function() {
				var figroupsid = $(this).fileupload("option", "filegroupsid");
				var uploadtiid = $(this).fileupload("option", "_uploadtimeid");
				$.post(ctxPath + "/upload/UploadController/setPermanent.vot?ispermanent=1&filegroupsid="+figroupsid+"&uploadtimeid="+uploadtiid);
			});
			
			alert(opts.successTip);
			thisCallback.call(thisObj);
		};
		
		if (thisObj.validate()) {
			$.commonSubmit(opts);
		}
	},
	
	// 内部使用，设置按钮正在进行某种操作
	_setButtonCommitting: function(iscommitting) {
		if (iscommitting) {
			this.data("tbuttonval", this.val());
			this.prop("disabled", true);
			var theval = $.i18n('antelope.form.nowprefix') + this.val() + $.i18n('antelope.form.nowsuffix');
			// jQuery-ui dialog下方按钮
			if (/^<span.*/i.test(this.val())) {
				theval = $("<span>" + this.val() + "</span>").text();
				theval = $("<span>" + this.val() + "</span>").find(".ui-button-text").text($.i18n('antelope.form.nowprefix') + theval + $.i18n('antelope.form.nowsuffix')).parent().html();
			}
			this.val(theval);
		} else {
			this.prop("disabled", false);
			this.val(this.data("tbuttonval"));
		}
	},

	// 通用异步单位树
	unitTree: function(origOptions) {
		var options = {
			itemClick: $.noop, // 当树节点被点击时触发
			itemDoubleClick: $.noop
		}
		var opts = $.extend (true, {}, options, origOptions);
		var setting = {
			expandSpeed : "",
			showLine : true,
			checkable : false,
			async: true,
			asyncUrl: ctxPath + "/common/UserRoleOrgController/getAsyncOrgnizationTreedata.vot",
			asyncParam: ['sid'],
			callback : {
			   click: opts.itemClick,
			   dblclick: opts.itemDoubleClick
			}
		};
		return this.zTree(setting);
	}
});

//通用人员选择
(function( $, undefined ) {
	$.widget( "ui.selectUsers", {
		options: {
			idinput:null,
			nameinput:null,
			button:null,
			selectionMode:"single", // 或multiple
			isUsername:true, // 默认为用户名方式获取已选人员
			filterrole:""
		},

		_create: function() {
			var thisObj = this;
			this.options.button.click(function(){
				var theselectedusers = [];
				if (thisObj.options.idinput.val()) {
					if (thisObj.options.selectionMode == 'single') {
						theselectedusers.push(thisObj.options.idinput.val());
					} else {
						theselectedusers = $.parseJSON(thisObj.options.idinput.val());
					}
				}
				$.selectUsers({
					callback:function(users) {
						if (users.length) {
							if (thisObj.options.isUsername) {
								if (thisObj.options.selectionMode == 'single') {
									thisObj.options.idinput.val(users[0].username);
									thisObj.options.nameinput.val(users[0].name);
								} else {
									var selectedUsers = [];
									var selectedNames = [];
									$(users).each(function(){
										selectedUsers.push(this.username);
										selectedNames.push(this.name);
									});
									thisObj.options.idinput.val(JSON.stringify(selectedUsers));
									thisObj.options.nameinput.val(selectedNames);
								}
							} else {
								if (thisObj.options.selectionMode == 'single') {
									thisObj.options.idinput.val(users[0].sid);
									thisObj.options.nameinput.val(users[0].name);
								} else {
									var selectedUsers = [];
									var selectedNames = [];
									$(users).each(function(){
										selectedUsers.push(this.sid);
										selectedNames.push(this.name);
									});
									thisObj.options.idinput.val(JSON.stringify(selectedUsers));
									thisObj.options.nameinput.val(selectedNames);
								}
							}
						}
					},
					selectedusers:theselectedusers,
					isUsername:thisObj.options.isUsername,
					issingle:thisObj.options.selectionMode == 'single',
					filterrole:thisObj.options.filterrole
				});
			});
		}
	});
})( jQuery );

//通用人员选择, 若通过角色找不到对应人员，则从全体人员中选取
(function( $, undefined ) {
	$.widget( "ui.selectUserByRolesid", {
		options: {
			idinput:null,
			nameinput:null,
			button:null,
			rolesid:null, // 必填，根据角色sid选择人员
			isUsername:true,
			unitsid:"" // 可选对应单位unitsid
		},

		_create: function() {
			var thisObj = this;
			this.options.button.click(function(){
				$.selectUserByRolesid({
					callback:function(user) {
						if (thisObj.options.isUsername) {
							thisObj.options.idinput.val(user.username);
						} else {
							thisObj.options.idinput.val(user.sid);
						}
						thisObj.options.nameinput.val(user.name);
					},
					isUsername:thisObj.options.isUsername,
					rolesid:thisObj.options.rolesid,
					unitsid:thisObj.options.unitsid
				});
			});
		}
	});
})( jQuery );


//通用时间选择
(function( $, undefined ) {
	$.widget( "ui.timepicker", {
		options: {
			currentDate: new Date(),
			secondEditable: true,
			change:jQuery.noop // 时间在初始化时，或发生变化时触发
		},

		_selectedInput:null,

		_create: function() {
			var thisObj = this;
			var hours = this.options.currentDate.getHours();
			var minutes = this.options.currentDate.getMinutes();
			var secondes = this.options.currentDate.getSeconds();

			if (hours <= 9)
				hours = '0' + hours;
			if (minutes <= 9)
				minutes = '0' + minutes;
			if (secondes <= 9)
				secondes = '0' + secondes;
			
			var disabledstr = "";
			
			if (!this.options.secondEditable) {
				disabledstr = " disabled='disabled' ";
			}
			

			this.element.append(
			'<div class="timepicker">\
				<input maxlength="2" class="timppicker_hour" value="'+hours+'">:<input maxlength="2" class="timppicker_min" value="'+minutes+'">:<input ' +  disabledstr + ' maxlength="2" class="timppicker_sec" value="'+secondes+'">\
				<div class="pickerbuttonout">\
					<div class="timeup"></div>\
					<div class="timedown"></div>\
				</div>\
			</div>');
			this.element.find(".timppicker_hour,.timppicker_min,.timppicker_sec").focus(function(){
				thisObj._selectedInput = this;
				$(this).select();
			}).keydown(function(e){
				if ((e.keyCode < 48 || e.keyCode > 57 && e.keyCode < 96 || e.keyCode > 105) && e.keyCode != 8)
					return false;

			}).blur(function(){
				if (thisObj._selectedInput) {
					var theval = eval(thisObj._selectedInput.value);
					tidyTimeCell(theval);
				}
			});

			this.element.find(".timeup").click(function(){
				if (thisObj._selectedInput) {
					var theval = eval(thisObj._selectedInput.value) + 1;
					tidyTimeCell(theval);
				}
			});

			function tidyTimeCell(theval) {
				
				if (!theval)
					theval = 0;
				
				var maxval = 23;
				if ($(thisObj._selectedInput).is(".timppicker_min,.timppicker_sec")) {
					maxval = 59;
				}

				if (theval > maxval)
					theval = 0;

				if (theval < 0)
					theval = maxval;

				if (theval <= 9)
					theval = '0' + theval;
				thisObj._selectedInput.value = theval;
				
				thisObj.options.change();
			}

			this.element.find(".timedown").click(function(){
				if (thisObj._selectedInput) {
					var theval = eval(thisObj._selectedInput.value) - 1;
					tidyTimeCell(theval);
				}
			});
			
			thisObj.options.change();
		},

		getTimeStr: function() {
			return this.element.find(".timppicker_hour").val() + ":"
			+ this.element.find(".timppicker_min").val() + ":" +this.element.find(".timppicker_sec").val();
		},
		
		setTimeStr: function(timestr) {
			timestr = timestr.split(':');
			this.element.find(".timppicker_hour").val(timestr[0]);
			this.element.find(".timppicker_min").val(timestr[1]);
			this.element.find(".timppicker_sec").val(timestr[2]);
		}
	});
})( jQuery );

//通用日期时间选择
(function( $, undefined ) {
	$.widget( "ui.datetimepicker", {
		options: {
			currentDate: new Date(),
			secondEditable: true
		},
		
		_pickerdiv:null,
		
		_pos:null,
		
		_okbtn:null,
		
		_cancelbtn:null,
		
		_datediv:null,
		
		_timediv:null,
		
		_create: function() {
			//创建日期时间选择div
			var parentdiv = $("<div style='position:absolute; display:none; border:1px solid gray; padding:2px; z-index:10000000'/>");
			this._datediv = $("<div/>").datepicker({changeYear:true,changeMonth:true, yearRange:'c-50:c+50'});
			this._timediv = $("<div style='margin-top:3px; float:left;'/>").timepicker({secondEditable: this.options.secondEditable});
			this._okbtn = $("<input type='button' value='" +$.i18n("antelope.ok")+ "' class='smallbutton' style=' float:left; margin-top:3px;margin-left:2px;'/>");
			this._cancelbtn = $("<input type='button' value='" +$.i18n("antelope.cancel")+ "' class='smallbutton' style=' float:left;margin-top:3px; margin-left:2px;'/>");
			
			//确认时，取得日期时间
			this._okbtn.click(function(){
				thisObj.element.val($.datepicker.formatDate('yy-mm-dd', thisObj._datediv.datepicker("getDate")) + " " +  thisObj._timediv.timepicker('getTimeStr'));
				thisObj._pickerdiv.hide();
			});
			
			//取消时，不进行获取
			this._cancelbtn.click(function(){
				thisObj._pickerdiv.hide();
			});
			
			parentdiv.append(this._datediv).append(this._timediv).append(this._okbtn).append(this._cancelbtn);//.append("<div style='clear:both;'/>");
			this._pickerdiv = parentdiv;
			
			this._pickerdiv.mousedown(function(e){
				e.stopPropagation();
			});
			
			$("body").append(this._pickerdiv);
			var thisObj = this;
			this.element.rebind("click.timpicker", function(){
				var offst = thisObj.element.offset();
				thisObj._pickerdiv.css('left', offst.left + 'px').css('top', offst.top + thisObj.element.height() + 3 + 'px');
				// 恢复日期时间
				if (thisObj.element.val()) {
					thisObj._datediv.datepicker("setDate", $.datepicker.parseDate('yy-mm-dd', thisObj.element.val().substring(0,10)));
					thisObj._timediv.timepicker("setTimeStr", thisObj.element.val().substring(11));
				}
				
				thisObj._pickerdiv.show();
			});
			
			this.element.attr("readonly", true);
			$(document).mousedown(function(){
				thisObj._pickerdiv.hide();
			});
		},
		
		destroy: function() {
			this.element.unbind("click.timpicker");
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );

var chartCommonOptions = {
	// 通用属性
	title:"", // 图表标题，位于图表上方位置
	itemClick: "", // 点击时触发的方法
	chartItemClickParamFields: [],
	
	// 单序列属性 begin ->
	displayName: "", // 显示名称 单序列图时的图例名称
	dataProvider: null, // json字符串即可，不需要转换为对象，
	field: "value",
	labelField:"label",
	categoryTitle:"", // 横坐标标题 (当使用flashchart时若为lineChart2则为纵坐标)
	/**
	 * plotChart, pieChart, lineChart, lineChart2, blowDirection(风向指针) sparkLineChart(从左向右一直走的线图)等其中lineChart2表示line旋转90度后的样式
	 * barChart(columnChart的横向形式) 
	 * 本属性虽为fusionchart以及flashchart共用，但并非所有待选属性值都通用，使用时，请测试
	 */
	type: "columnChart", 
	columnChartType:null, // 可以为100%
	fillColor: null, // 柱形图、散点图时点的颜色
	//<-单序列属性 end
	
	series: null // 多序列，优先级高于单序列，若为非空数组，则使用它，值可以为[{displayName:"ss", field:"a", labelField:"dffds", labelEnum: "aa", dataProvider:data}]
	
};

//fusionchart控件易用性接入
(function( $, undefined ) {
	$.widget( "ui.fusionchart", {
		options: $.extend(true, {}, chartCommonOptions, {
			
		}),
		
		_chart: null,
		
		_chartTypeSwfMap: {columnChart:{stacked: "StackedColumn3D.swf", clustered: "MSColumn3D.swf", single: "Column3D.swf"}, lineChart:"MSLine.swf", pieChart:"Pie3D.swf", barChart:"MSBar3D.swf"},
		
		_chartId: null,
		
		_basicChartJson: {
			"chart" : {
				exportEnabled:1,
				exportAtClient:0,
				exportAction:'save',
				exportHandler: ctx + "/JSP/ExportExample/FCExporter",
				"palette" : "2",
				"subcaption" : "",
				"showvalues" : 1,
				"divlinedecimalprecision" : "1",
				"limitsdecimalprecision" : "1",
				"numberprefix" : "",
				"formatnumberscale" : "0",
				"legendIconScale":1,
				"animation":0
			},
			"styles" : [ {
				"definition" : [ {
					"style" : [ {
						"name" : "myLegendFont",
						"type" : "font",
						"size" : "12",
						"bold" : "1"
					} ]
				} ],
				"application" : [ {
					"apply" : [ {
						"toobject" : "Legend",
						"styles" : "myLegendFont"
					},{
						"toobject" : "DataLabels",
						"styles" : "myLegendFont"
					},{
						"toobject" : "ToolTip",
						"styles" : "myLegendFont"
					} ]
				} ]
			} ]
		},
		
		_create: function() {
			var thisObj = this;
			
			if (thisObj.options.dataProvider == null && (this.options.series == null || this.options.series.length == 0)) // 未找到数据直接返回
				return;
			
			this._chartId = 'fc' + $.getNewsid();
					
            this._chart = new FusionCharts(ctx + "/controls/flash/fusionchart/" + this._getChartTypeSwf(this.options.type), this._chartId, "100%", "100%", "0", "1");
            this._chart.setJSONData(this._createChartJson());
            this._chart.render(this.element[0]);
		},
		
		_getChartTypeSwf: function(thetype) {
			var swfobj = this._chartTypeSwfMap[thetype];
			if ($.isPlainObject(swfobj)) {
				return swfobj[this.options.columnChartType];
			} else {
				return swfobj;
			}
		},
		
		_createChartJson: function() {
			var thisObj = this;
			var origChartJson = null;
			if (thisObj.options.series == null || thisObj.options.series.length == 0) { // 多序列长度为0时，使用单序列模式
				
				var labelvals = this._getLabelValuesByOneSeries(thisObj.options);
				
				var labelvalforpie = [];
				
				for (var i = 0; i < thisObj.options.dataProvider.length; ++i) {
					
					var obj = {label:thisObj.options.dataProvider[i][this.options.labelField], value:thisObj.options.dataProvider[i][this.options.field]};
					if (thisObj.options.labelEnum) {
						obj.label = typicalEnumFormatter(obj.label, thisObj.options.labelEnum);
					}
					
					if (thisObj.options.itemClick) {
						obj.link = "JavaScript:" + thisObj.options.itemClick + "(" + JSON.stringify(this._filteredItemClickParam(thisObj.options.dataProvider[i])) + ");";
					}
					
					labelvalforpie.push(obj);
				}
				
				// 创建chart数据
				origChartJson = this._createChartJsonInner({
					labels:labelvals.labels,
					valueseries:  [ {
						"seriesname" : thisObj.options.displayName,
						"data" : labelvals.values
					} ],
					data: labelvalforpie
				});
			} else { // 多序列
				
				if (this.options.type == 'pieChart') {
					alert("pieChart不支持多序列！");
					return;
				}
				
				// 整理数据
				// 收集label及value
				var finallabelvals = {labels:[]};
				
				var tmpvalueseries = [];
				
				var labelskeys = {};
				
				// 合并labelvals
				for (var i = 0; i < thisObj.options.series.length; ++i) {
					var labelvals = this._getLabelValuesByOneSeries(thisObj.options.series[i]);
					for (var j = 0; j < labelvals.labels.length; ++j) {
						if (labelskeys[labelvals.labels[j].label])
							continue;
						finallabelvals.labels.push(labelvals.labels[j]);
						labelskeys[labelvals.labels[j].label] = true;
					}
					tmpvalueseries.push({seriesname:thisObj.options.series[i].displayName, data:labelvals.values});
				}
				
				// 创建chart数据
				origChartJson = this._createChartJsonInner({
					labels:finallabelvals.labels,
					valueseries: tmpvalueseries
				});
				
			}
			
			return origChartJson;
		},
		
		_filteredItemClickParam: function(origParam) {
			var thisObj = this;
			var newParam = {};
			for (var n = 0; n < thisObj.options.chartItemClickParamFields.length; ++n) {
				newParam[thisObj.options.chartItemClickParamFields[n]] = origParam[thisObj.options.chartItemClickParamFields[n]];
			}
			return newParam;
		},
		
		_createChartJsonInner: function(data) {
			var thisObj = this;
			return $.extend(true, {}, thisObj._basicChartJson, {
				"chart" : {
					"caption" : thisObj.options.title,
					"xaxisname": thisObj.options.categoryTitle
				},
				"categories" : [ {
					"category" : data.labels
				} ],
				"dataset" : data.valueseries,
				data: data.data
			});
		},
		
		// 整理数据
		// 收集label及value
		_getLabelValuesByOneSeries: function(seriesval) {
			var thisObj = this;
			var labalandvals = {labels:[], values:[], displayName:"", labelEnum:seriesval.labelEnum};
			for (var i = 0; i < seriesval.dataProvider.length; ++i) {
				labalandvals.labels[i] = {label:seriesval.dataProvider[i][this.options.labelField]};
				
				if (labalandvals.labelEnum) {
					labalandvals.labels[i].label = typicalEnumFormatter(labalandvals.labels[i].label, labalandvals.labelEnum);
				}
				
				labalandvals.values[i] = {value:seriesval.dataProvider[i][seriesval.field]};
				labalandvals.displayName = seriesval.displayName;
				
				if (thisObj.options.itemClick) {
					for(var key in seriesval.dataProvider[i]) {
						//seriesval.dataProvider[i][key] = seriesval.dataProvider[i][key];
					}
					labalandvals.values[i].link = "JavaScript:" + thisObj.options.itemClick + "(" + JSON.stringify(this._filteredItemClickParam(seriesval.dataProvider[i])) + ");";
				}
			}
			return labalandvals;
		},
		
		_setOption: function(key, value) {
			$.Widget.prototype._setOption.apply( this, arguments );
			var thisObj = this;
			if (key == 'dataProvider' || key == 'series') {
				 this._chart.setJSONData(this._createChartJson());
			}
		},
		
		showImage: function(callbackname, width, height) {
			 var chartObject = getChartFromId(this._chartId);
			 if(chartObject.hasRendered())
				 chartObject.exportChart({exportFormat:'png', exportFileName: "fc" + $.getNewsid(), exportCallback:callbackname}); 
		},
		
		destroy: function() {
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );

//通用多功能flash图表控件
(function( $, undefined ) {
	$.widget( "ui.flashchart", {

		options: $.extend(true, {}, chartCommonOptions, {
			
			/** 多图表组件 */
			charts:null,
			
			/*** blowDirection 专用属性 begin ->*/
			tileWidth:150, 
			tileHeight:160, 
			blowBgColorField: null, 
			blowLabelFontSize: 12, 
			/*<- blowDirection 专用属性 end *****/
			
			chartsTileWidth: 300, // 当使用多图表组件时，单个图表的宽度
			chartsTileHeight: 300, // 当使用多图表组件时，单个图表的高度
			
			valueAxisMinimum: null, // 值坐标最小值
			valueAxisMaximum: null, // 值坐标最大值
			
			titleFontSize: 14,
			titleVisible: true,
			legendVisible: true,
			axisVisible: true,
			labelEnum:null,
			interval:null,
			markerHeight:15,
			markerWidth:10,
			lineColor:0xff0000,
			axisLabelPadding:0,
			fillColor:null, // @override 柱形图、散点图时点的颜色 
			numToVertCategoryLabel:0, // 设置在数量为多少的时候将分类label竖立起来 
			showChartSelect: true, // 标明是否显示图表切换列
			style:'gs', // 风格，若使用气象局风格则为 qxj
			valueTitle:"" // 纵坐标标题 (若为lineChart2则为横坐标
		}),

		_setOption: function(key, value) {
			$.Widget.prototype._setOption.apply( this, arguments );
			this._labelEnumTidy();
			if (key == 'dataProvider') {
				this.element.commonChartReload('commonChartReloadData', this.options.dataProvider);
			}

			if (key == 'series') {
				this.element.commonChartReload('commonChartReloadSeries', this.options.series);
			}

			if (key == 'labelField') {
				this.element.commonChartReload('commonChartReloadCategoryField', this.options.labelField);
			}

			if (key == 'field') {
				this.element.commonChartReload('commonChartReloadField', this.options.field);
			}

			if (key == 'displayName') {
				this.element.commonChartReload('commonChartReloadDisplayName', this.options.displayName);
			}
			
			this.element.commonChartKeyReload('commonChartReloadOptionAttribute', key, value);
		},

		_create: function() {
			// 加载charts之前整理
			if (this.options.charts != null) {
				var newarrchart = [];
				for(var key in this.options.charts) {
					this.options.charts[key].sid = key;
					newarrchart.push(this.options.charts[key]);
				}
				this.options.charts = newarrchart;
			}
			
			this._labelEnumTidy();
			this.element.loadJsCommonChart(this.options);
		},

		_labelEnumTidy:function() {
			var thisObj = this;
			if (this.options.labelEnum) {
				this.options.dataProvider = $.parseJSON(JSON.stringify(this.options.dataProvider));
				$(this.options.dataProvider).each(function(){
					this[thisObj.options.labelField] = typicalEnumFormatter.call({enumXml:thisObj.options.labelEnum}, this[thisObj.options.labelField]);
				});
			}
		},

		showImage: function(callbackname, width, height) {
			this.element.showJsCommonChartImage(ctxPath, callbackname, width, height);
		}

	});
})( jQuery );

// 通用多功能搜索框控件
(function( $, undefined ) {
	$.widget( "ui.searchbox", {
		options: {
			url:null,
			callback:jQuery.noop
		},

		_searchinput:null,
		_searchbutton:null,
		_searchcondiul:null,

		_create: function() {
			var thisObj = this;

			this._searchinput = $('<input style="width: 300px; height: 21px; ">').appendTo(this.element);
			this._searchbutton = $('<input type="button" class="sconbtn" value="搜索" style="margin-left:10px;"/>').appendTo(this.element);
			this._searchcondiul = $('<ul class="searchcondiul"></ul>').appendTo(this.element);
			this.element.css("height", 55);

			this._searchbutton.click(function(){
				$.post(thisObj.options.url, "keyword=" + encodeURIComponent(encodeURIComponent(thisObj._searchinput.val())), function(data){
					thisObj.options.callback(data.resultData);
					if (thisObj._searchinput.val()) {
						thisObj._searchcondiul.empty();
						var existcol = [];
						$(data.colinfos).each(function(){
							var colobjt = this;
							var hascol = false;
							$(data.resultData).each(function(){
								for (var key in this) {
									var val = this[key] === null?null:this[key] + "";
									if (this[key] != null && key.toLowerCase() == colobjt.colname.toLowerCase()) {
										if (colobjt.enums) {
											for (var i = 0; i < colobjt.enums.length; i++) {
												if (colobjt.enums[i].value == val && colobjt.enums[i].label.indexOf(thisObj._searchinput.val()) != -1) {
													hascol = true;
												}
											}
										} else if (val.indexOf(thisObj._searchinput.val()) != -1) {
											hascol = true;
										}

										return false;
									}
								}
							});

							if (hascol) {
								existcol.push(this);
							}

						});


						$(existcol).each(function(){
							$('<li class="ui-state-default" style="display:none" colname="'+this.colname+'">'+this.coltitle+'</li>').appendTo(thisObj._searchcondiul).click(function(){
								$(this).toggleClass("ui-state-active");
								var mustcols = [];
								thisObj._searchcondiul.find("li").each(function(){
									if ($(this).hasClass("ui-state-active"))
										mustcols.push($(this).attr("colname"));
								});
								$.post(thisObj.options.url, "filterkeys=" + mustcols.join("&filterkeys=") + "&keyword=" + encodeURIComponent(encodeURIComponent(thisObj._searchinput.val())), function(data){
									thisObj.options.callback(data.resultData);
								},"json");
							}).fadeIn();
						});

					} else {
						thisObj._searchcondiul.find("li").fadeOut(function(){
							thisObj._searchcondiul.empty();
						});
					}
				}, "json");
			});
		},

		search:function() {
			this._searchbutton.click();
		}
	});
})( jQuery );

// 通用视图导航组件
(function( $, undefined ) {
	$.widget( "ui.viewNavigator", {
		options: {
			firstView:null,
			effect:null
		},

		_viewstacks: [],

		_create: function() {
			var thisObj = this;
			if (!this.options.firstView) {
				alert("请设置首视图");
			}
			this._viewstacks.push($(this.options.firstView, this.element));
			this._viewstacks[this._viewstacks.length - 1].show();
		},	//navigator.popToFirstView();

		pushView: function(viewselector) {
			var thisObj = this;
			this._viewstacks[this._viewstacks.length - 1].hide(this.options.effect,{direction:"left"});
			thisObj._viewstacks.push($(viewselector, thisObj.element));
			thisObj._viewstacks[thisObj._viewstacks.length - 1].show(thisObj.options.effect, {direction:"right"});
			BrowserHistory.setBrowserURL("viewid="+thisObj._viewstacks.length);
		},

		popView: function(isbrowseraction) {
			var thisObj = this;
			if (isbrowseraction && location.href.indexOf("#viewid=") != -1) {
				var idpart = /#viewid=[\d]*/g.exec(location.href) + "";
				var viewindex = idpart.split("=")[1];
				if (viewindex == thisObj._viewstacks.length)
					return;
			}
			
			if (this._viewstacks.length == 1)
				return;
			
			//alert(thisObj._viewstacks.length)
			
			var thisObj = this;
			this._viewstacks[this._viewstacks.length - 1].hide(this.options.effect,{direction:"right"});
			if (thisObj._viewstacks.length) {
				thisObj._viewstacks.pop();
				thisObj._viewstacks[thisObj._viewstacks.length - 1].show(thisObj.options.effect,{direction:"left"});
			}
			BrowserHistory.setBrowserURL("viewid="+thisObj._viewstacks.length);
		},

		popToFirstView: function() {
			var thisObj = this;
			this._viewstacks[this._viewstacks.length - 1].hide(this.options.effect,{direction:"right"});
			thisObj._viewstacks.length = 1;
			thisObj._viewstacks[thisObj._viewstacks.length - 1].show(thisObj.options.effect, {direction:"left"});
		},

		replaceView: function (viewselector) {
			var thisObj = this;
			this._viewstacks[this._viewstacks.length - 1].hide(this.options.effect);
			thisObj._viewstacks.pop();
			thisObj._viewstacks.push($(viewselector, thisObj.element));
			thisObj._viewstacks[thisObj._viewstacks.length - 1].show(thisObj.options.effect);
		}
	});
})( jQuery );

// 通用自定义报表组件的封装
(function( $, undefined ) {
	$.widget( "ui.report", {
		options: {
			tmpl_sid:null,
			data_sid:null,
			data_sids:null, // data_sid优先级高于data_sids，所有当有data_sid时，使用它，否则使用data_sids, data_sids将会对所提供的data_sids进行汇总
			readonly:false,
			webmode:true,
			labelFunction:$.noop
		},

		_jqhtmltb:null,


		_create: function() {
			var thisObj = this;
			// 到后台加载报表模板
			var reaonlystr = this.options.readonly?"readonly":"";

			$.get(ctxPath + "/report/getreporthtmltable.vot?sid=" + this.options.tmpl_sid + "&webmode=" + this.options.webmode, function(data) {

				thisObj.element.html(data);
				thisObj._jqhtmltb = thisObj.element.find("table");

				thisObj._jqhtmltb.find("td").each(function(){
					var self = $(this);
					var edittype = self.attr("edittype");
					if (edittype == null)
						return;


					if (edittype == 'text' || edittype == 'num') {
						self.empty();
						if (reaonlystr) {
							$("<textarea edittype="+edittype+" "+reaonlystr+" name='"+self.attr("dbcol")+"' class='reptextarea readoly heightadapt'></textarea>").appendTo(self);
						} else {
							$("<textarea edittype="+edittype+" "+reaonlystr+" name='"+self.attr("dbcol")+"' class='reptextarea heightadapt'></textarea>").appendTo(self);
						}
						return;
					}

					if (edittype == 'date') {
						self.empty();
						var dtinput = $("<input edittype="+edittype+" readonly name='"+self.attr("dbcol")+"' class='date'/>").width(67).appendTo(self);
						if (!thisObj.options.readonly)
							dtinput.datepicker({changeYear:true,changeMonth:true, yearRange:'c-50:c+50'});
						return;
					}

					if (edittype == 'dict') {
						self.empty();
						var dictinfo = $.parseJSON(decodeURIComponent(self.attr("dict")));
						var selectdom = $("<select "+(thisObj.options.readonly?"disabled":"")+" name='" + self.attr("dbcol") + "'></select>");

						for (var i = 0; i < dictinfo.dictitems.length; i++) {
							var dictitemtitle = dictinfo.dictitems[i].title;
							$("<option></option>").val(dictitemtitle).text(dictitemtitle).appendTo(selectdom);
						}
						selectdom.appendTo(self);
						return;
					}

					if (edittype == 'file') {
						self.empty();
						self.fileupload({readonly:thisObj.options.readonly, picturePreview:true});
						self.append("<input type='hidden' name='" + self.attr("dbcol")+"' />")
						return;
					}

					// 自定义插件部分
					if (edittype) {
						self.empty();
						self.append("<span dbcolname='" + self.attr("dbcol") + "'></span>");
						self.append("<input type='hidden' name='" + self.attr("dbcol")+"' />");
						return;
					}
				});

				thisObj._initData(); // 挂上表样之后初始化对应数据
				thisObj._resetTextAreaHeight();


			});
		},

		// 初始化报表数据
		_initData: function() {
			var thisObj = this;
			// 没有指向数据id则不仅回调labelFunction 不初始化数据
			if (!this.options.data_sid && !this.options.data_sids) {
				$("[dbcolname]", this._jqhtmltb).each(function(){
					var self = $(this);
					self.html(thisObj.options.labelFunction(self.parent().attr("edittype"), self.next().val()));
					self.next().val(self.html());
				});
				return;
			}

			var sidparam = "";
			if (this.options.data_sid) {
				sidparam = "sid=" + this.options.data_sid;
			} else {
				sidparam = "sids=" + JSON.stringify(this.options.data_sids);
			}

			$.getJSON(ctxPath + "/report/getreportdata.vot?" + sidparam + "&tmpl_sid=" + this.options.tmpl_sid, function(data){
				for (var key in data) {

					if (thisObj.options.readonly) {
						var dbcell = $("[dbcol=" + key + "]", this._jqhtmltb);

						if (dbcell.attr("edittype") == 'date') {
							dbcell.text(data[key]?data[key].substring(0,10):'');
						} else if (dbcell.attr("edittype") == 'text' || dbcell.attr("edittype") == 'num'){
							dbcell.empty();
							$("<div style='font-weight:normal;padding:0;'></div>").html($.tidyTextToHtml($.trim(data[key]))).appendTo(dbcell);
						} else if (dbcell.attr("edittype") == 'dict') {
							dbcell.text(data[key]);
						} else if (dbcell.attr("edittype") == 'file') {
							dbcell.fileupload("option", "filegroupsid", data[key]).fileupload("appendUpdatedFiles", function(){
								thisObj._resetTextAreaHeight();
							});
						} else { // 其他为自定义
							dbcell.empty();
							$("<div style='font-weight:normal;padding:0;'><pre style='margin:0;padding:0'></pre></div>").find("pre").text($.trim(data[key])).end().appendTo(dbcell);
						}



					} else {
						var formitem = $("[name=" + key + "]", this._jqhtmltb).val(data[key]);
						if (formitem.hasClass("date")) {
							formitem.val(data[key]?data[key].substring(0,10):'');
						} else if (formitem.attr("type") == 'hidden' && formitem.parent().attr("edittype") != 'file'){
							formitem.val(data[key]);
							formitem.prev().text(data[key]);
						} else {
							formitem.val(data[key]);
						}

						if ($("[name=" + key + "]", this._jqhtmltb).parent().attr("edittype") == 'file') {
							$("[name=" + key + "]", this._jqhtmltb).parent().fileupload("option", "filegroupsid", data[key]).fileupload("appendUpdatedFiles", function(){
								thisObj._resetTextAreaHeight();
							});
						}
					}

				}

				$("[dbcolname]", this._jqhtmltb).each(function(){
					var self = $(this);
					self.html(thisObj.options.labelFunction(self.parent().attr("edittype"), self.next().val()));
					self.next().val(self.html());
				});

				thisObj._resetTextAreaHeight();
			});
		},
		// 根据当前各个单元格高度重置文本域高度
		_resetTextAreaHeight:function() {
			$("textarea", this._jqhtmltb).each(function(){
				if ($(this).attr("resetedheight"))
					return;
				
				if ($(this).parent().height() == 0)
					$(this).height($(this).parent().parent().height() - 4);
				else
					$(this).height($(this).parent().height() - 4);
				
				$(this).attr("resetedheight", true);
			});
			
			$("textarea", this._jqhtmltb).initWidgets();
		},

		// 保存当前数据
		save: function(callback) {
			var thisObj = this;

			// 保存前先验证
			var iserror = false;

			this._jqhtmltb.find(":input").each(function(){
				var self = $(this);
				var edittype = self.attr("edittype");
				if (edittype == 'num') {
					if (isNaN(self.val())) {
						iserror = true;
						self.addClass("ui-state-error");
						self.attr("title", "请输入数字！");
					} else {
						self.removeClass("ui-state-error");
					}
				}
			});
			if (iserror)
				return;

			var count = 0;
			
			thisObj._jqhtmltb.find("[edittype=file]").each(function(){
				var guid = $(this).find("input").val();
				if (!guid) {
					guid = $.Guid.New();
					$(this).find("input").val(guid);
				}
				if($(this).fileupload("hasFileToUpload"))
					count++;
				
				$(this).fileupload("option", "filegroupsid", guid).fileupload("upload", function() {
					count--;
					if (count == 0) {
						if (!thisObj._jqhtmltb.parent().is("form")) {
							thisObj._jqhtmltb.wrap("<form/>");
						}
						$.post(ctxPath + "/report/savereportdata.vot?tmpl_sid="+thisObj.options.tmpl_sid+"&data_sid=" + (thisObj.options.data_sid||''),
								encodeURI(thisObj._jqhtmltb.parent().serialize()), function(data){
							thisObj.options.data_sid = data;
							callback(thisObj.options.data_sid);
						});
					}
				});
			});
			if (count == 0) {
				if (!this._jqhtmltb.parent().is("form")) {
					this._jqhtmltb.wrap("<form/>");
				}
				$.post(ctxPath + "/report/savereportdata.vot?tmpl_sid="+thisObj.options.tmpl_sid+"&data_sid=" + (thisObj.options.data_sid||''),
						encodeURI(thisObj._jqhtmltb.parent().serialize()), function(data){
					thisObj.options.data_sid = data;
					callback(thisObj.options.data_sid);
				});
			}
		},

		serializeReportData: function() {

		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );


//通用浮动自定义报表组件的封装
(function( $, undefined ) {
	$.widget( "ui.floatreport", {
		options: {
			tmpl_sid: null,
			groupsid: null,
			groupsids: [], // groupsid优先级优于groupsids,所以当groupsid中有数据时，使用它，否则使用groupsids
			datasids:null, // 数据项sid数组，用于显示对应的数据项，优先级高于groupsid和groupsids,显示的行数与datasids长度不一定一致，若存在则显示，不存在则不显示
			ismodify: true,
			initgroupsid: null,
			readonly: false,
			rowheight:100,
			rowappendable: true,
			labelFunction: $.noop,
			uploadDataBtn: null, // 传入用于excel数据上传的jquery按钮容器对象
			button: null // {value:'ddd' click:function(datainfo){}}在首列允许添加一个操作按钮
		},

		_reportdata:null,

		_reporttable:null,

		_coldata:[],

		_create: function() {
			var thisObj = this;
			// 到后台加载报表模板
			$.getJSON(ctxPath + "/floatreport/gettmplinfowithdict.vot?sid=" + this.options.tmpl_sid, function (coldata){
				thisObj._coldata = coldata;
				thisObj._reporttable = $('<table class="floatreptable floatdatagrid" style="table-layout: fixed;">\
											<tr id="reportheadertr"></tr>\
										</table>');

				if (thisObj.options.button) {// 首列添加操作按钮
					thisObj._reporttable.find("#reportheadertr").append($("<th style='white-space:nowrap;width:100px;'>操作</th>"));
				}

				$(coldata).each(function(){
					thisObj._reporttable.find("#reportheadertr").append($("<th style='width:"+this.colwidth+"px'>" + this.coltitle + "</th>"));
				});

				if (thisObj.options.ismodify) { //修改
					if (thisObj.options.datasids) {
						$.getJSON(ctxPath + "/floatreport/getreportdata.vot?datasids=" + JSON.stringify(thisObj.options.datasids) + "&tmplsid=" + thisObj.options.tmpl_sid, function (data){
							thisObj._reportdata = data;
							thisObj._appenddata(data);
						});
					} else if (thisObj.options.groupsid) {
						$.getJSON(ctxPath + "/floatreport/getreportdata.vot?groupsid=" + thisObj.options.groupsid + "&tmplsid=" + thisObj.options.tmpl_sid, function (data){
							thisObj._reportdata = data;
							thisObj._appenddata(data);
						});
					} else {
						$.getJSON(ctxPath + "/floatreport/getreportdata.vot?groupsids=" + JSON.stringify(thisObj.options.groupsids) + "&tmplsid=" + thisObj.options.tmpl_sid,
								function(data){
							thisObj._reportdata = data;
							thisObj._appenddata(data);
						});
					}
				} else {
					if (thisObj.options.initgroupsid) {
						$.getJSON(ctxPath + "/floatreport/getreportdata.vot?groupsid=" + thisObj.options.initgroupsid + "&tmplsid=" + thisObj.options.tmpl_sid, function (data){
							thisObj._reportdata = data;
							thisObj._appenddata(data, true);
						});
					}
				}

				var justgroupsid = thisObj.options.groupsid || thisObj.options.initgroupsid;

				thisObj.element.append(thisObj._reporttable);

				if (thisObj.options.uploadDataBtn) {
					thisObj.options.uploadDataBtn.loadFileUpDownloader ({wmode:"transparent", htmlText:"<font color='#005590' face='宋体'><u>上传数据excel</u></font>", single:true, extension:"*.xls"}, function(){
						this.selectFile (function (filelist) {
							if (thisObj.options.groupsid == null) {
								thisObj.options.groupsid = $.getNewsid();
							}
							return ctxPath + "/floatreport/uploaddataexcel.vot?groupsid=" + thisObj.options.groupsid+"&tmplsid=" + thisObj.options.tmpl_sid;
						}).uploadComplete (function (info) {
							$.getJSON(ctxPath + "/floatreport/getreportdata.vot?groupsid=" + thisObj.options.groupsid+"&tmplsid=" + thisObj.options.tmpl_sid, function (data){
								thisObj._reporttable.find("tr:gt(0)").remove();
								thisObj._reportdata = data;
								thisObj._appenddata(data);
							});
						}).uploadFail(function(e) {
							alert("数据excel上传失败！请确保数据文件与报表模板表头一致！");
						});
					});
				}

			});
		},

		_appenddata: function(data, isinit) {
			var thisObj = this;
			var line = "";
			$(data).each(function(index){
				var linedata = this;
				line += "<tr style='height:"+thisObj.options.rowheight+"px;'>";

				if (thisObj.options.button) {// 首列添加操作按钮
					line += "<td><input type='button' class='smallbutton' value='"+thisObj.options.button.value+"' rowindx='"+index+"'/></td>";
				}

				$(thisObj._coldata).each(function(){
					line += "<td>" + thisObj._createTableCell(this, linedata[this.colname]) + "</td>";
				});
				if (isinit) { // 如果为初始化则需要保存initsid
					line += "<td style='display:none'>" + $("<input type='hidden' name='initsid'/>").val(linedata.sid)[0].outerHTML + "</td>";
				} else {
					line += "<td style='display:none'>" + $("<input type='hidden' name='initsid'/>").val(linedata.initsid)[0].outerHTML + "</td>";
				}
				line += "</tr>";
			});
			thisObj._reporttable.append(line);

			// 对首列按钮点击操作监听
			if (thisObj.options.button) {// 首列添加操作按钮
				$(".smallbutton", thisObj._reporttable).click(function(){
					var thisdata = thisObj._reportdata[$(this).attr("rowindx")];
					thisObj.options.button.click.call(thisdata,
							thisObj.options.tmpl_sid, thisdata.sid);
				});
			}
			
			// 自适应高
			$("textarea", thisObj._reporttable).initWidgets();
		},

		_createTableCell: function (col, celldata) {

			if (this.options.readonly) { // 若为只读，则直接显示文字
				return $.tidyTextToHtml(celldata);
			}

			var celhtml = celldata;
			if (col.edittype == 'dict') { // 下拉列表
				celhtml = "<select name='"+col.colname+"' >";
				$(col.dictitems).each(function(){
					if (celldata == this.title)
						celhtml += "<option selected value='"+this.title+"'>"+this.title+"</option>";
					else
						celhtml += "<option value='"+this.title+"'>"+this.title+"</option>";
				});
				celhtml += "<select>";
			} else if (col.edittype == 'text' ) { // 文本类型
				celhtml = $("<textarea name='"+col.colname+"' class='reptextarea heightadapt'></textarea>").height(this.options.rowheight - 4).text(celldata)[0].outerHTML;
			} else {
				celhtml += $("<input type='hidden' name='"+col.colname+"'/>").val(celhtml)[0].outerHTML;
			}

			return celhtml;
		},

		// 保存当前数据
		save: function(callback) {

			if (this.options.readonly) {
				alert("只读形式的浮动表不允许执行保存操作，请确认！");
				return;
			}

			var thisObj = this;
			if (this.options.groupsid == null) {
				alert("请设置groupsid后再进行保存！");
				return;
			}

			if (!this._reporttable.parent().is("form")) {
				this._reporttable.wrap("<form/>");
			}

			$.post(ctxPath+"/floatreport/saveReportData.vot?tmplsid="+this.options.tmpl_sid+"&groupsid="+this.options.groupsid,
					encodeURI(thisObj._reporttable.parent().serialize()), function(data) {
				callback(thisObj.options.groupsid);
			});
		},

		// 模板数据上传
		uploadData: function() {

		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );

// 报表模板选择组件(包括浮动表和固定表两种)
$.extend({
	selectFloatReportTemplate: function(origOptions) {
		var options = {
				callback:null,
				groupsid:"",
				plugins:[], // 可以写 [{value:'compsult', label:'自动合规咨询数'}]
				showedit:false,
				title:"报表模板选择"
			};

		var opts = jQuery.extend (true, {}, options, origOptions);

		var dialogdiv = $("<div></div>");

		readReleasedTmpl(dialogdiv, opts.groupsid);


		var buttonss = {
				'确定':function() {
					if ($.isFunction(opts.callback)) {
						if (dialogdiv.datagrid("option", "selectedItem") == null) {
							alert("请选择一个模板！");
							return;
						}
						opts.callback(dialogdiv.datagrid("option", "selectedItem"));
					}
					dialogdiv.dialog("destroy");
				}, '取消':function(){
					dialogdiv.dialog("destroy");
				}
		};

		// 模块备选模板可编辑状态
		if (opts.showedit) {
			buttonss['编辑模板'] = function() {
				var topifram = $.dialogTopIframe({
					url: ctxPath+"/components/floatLineReportTemplates.jsp?options="+encodeURIComponent(encodeURIComponent(JSON.stringify(opts))),
					buttons:{
						'关闭':function(){
							top.$(topifram).dialog("destroy");
							readReleasedTmpl(dialogdiv, opts.groupsid);
						}
					}, width:($(top).width() - 40), height:($(top).height() - 40), title:"编辑模板", modal:true
				});
			}
		}

		dialogdiv.dialog({
			buttons:buttonss, width:900, height:500, title:opts.title
		});

		function readReleasedTmpl(dialogdiv, groupsid) {
			$.getJSON(ctxPath + "/floatreport/getreleasedtmpls.vot?groupsid="+groupsid, function(data){
				dialogdiv.datagrid("destroy").datagrid({
					dataProvider:data,
					selectionMode:'singleRow',
					columns:[{headerText:'标题', dataField:'title', width:'90%'}]
				});
			});
		}
	},

	selectReportTemplate: function(origOptions){

		var options = {
				callback:null,
				groupsid:"",
				plugins:[], // 可以写 [{value:'compsult', label:'自动合规咨询数'}]
				showedit:false,
				excelmanager: null,
				title:"报表模板选择"
			};

		var opts = jQuery.extend (true, {}, options, origOptions);

		var dialogdiv = $("<div></div>");

		readReleasedTmpl(dialogdiv, opts.groupsid);


		var buttonss = {
				'确定':function() {
					if ($.isFunction(opts.callback)) {
						if (dialogdiv.datagrid("option", "selectedItem") == null) {
							alert("请选择一个模板！");
							return;
						}
						opts.callback(dialogdiv.datagrid("option", "selectedItem"));
					}
					dialogdiv.dialog("destroy");
				}, '取消':function(){
					dialogdiv.dialog("destroy");
				}
		};

		// 模块备选模板可编辑状态
		if (opts.showedit) {
			buttonss['编辑模板'] = function() {
				var topifram = $.dialogTopIframe({
					url: ctxPath+"/components/reportTemplates.jsp?options="+encodeURIComponent(encodeURIComponent(JSON.stringify(opts))),
					buttons:{
						'关闭':function(){
							top.$(topifram).dialog("destroy");
							readReleasedTmpl(dialogdiv, opts.groupsid);
						}
					}, width:($(top).width() - 40), height:($(top).height() - 40), title:"编辑模板", modal:true
				});
			}
		}

		dialogdiv.dialog({
			buttons:buttonss, width:900, height:500, title:opts.title
		});

		function readReleasedTmpl(dialogdiv, groupsid) {
			$.getJSON(ctxPath + "/report/getreleasedtmpls.vot?groupsid="+groupsid, function(data){
				dialogdiv.datagrid("destroy").datagrid({
					dataProvider:data,
					selectionMode:'singleRow',
					columns:[{headerText:'标题', dataField:'title', width:'90%'}]
				});
			});
		}
	}
});




// 文件上传下载控件的封装

function downloadFileFromFileUploadInner(downloadurl, needchecklogon, readDocOnline, filedatasid) {
	if (needchecklogon) {
		$.getJSON(ctx + "/common/isLogon.vot", function(data){
			if (data) {
				$.postIframe(downloadurl);
			} else {
				alert("请您登录网站后再下载该文件！");
			}
		});
	} else {
		if (readDocOnline) {
			window.open(ctxPath + "/components/uploaddocview.jsp?sid=" + filedatasid, null,
			"height=1400,width=1600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes");
		} else {
			$.postIframe(downloadurl);
		}
	}
}

(function( $, undefined ) {
	$.widget( "ui.fileupload", {

		options: {
			filegroupsid: null, // 若不传递一个sid,则ui.fileupload将自动添加新的sid
			filegroupsidinput: null, // 保存文件組sid的表单域，若要启用enablePermanent，即自动上传后的后台保存功能，则需要传入此参数
			readonly: false,
			autosave: false,
			enablePermanent: true,
			maxfilesize: 51200, // 文件上传最大值 默认为50M
			maxfilecount: 5, // 文件上传最大个数，默认为5个
			uploadComplete: jQuery.noop,
			showDescription: false,
			checklogon: false, // 下载时是否核对登录状态
			fileChange: jQuery.noop, // 可以是function(kind, fileinfo) 其中 kind 可能为 addFile 或removeFile fileinfo通过fileinfo.filename取得文件名称。
			uploadDateVisible: false,
			readDocOnline: false,
			picturePreview: false,
			selectFileCallback:$.noop, // 通过选择框选择文件后调用，通常用于判断文件是否合适，若不合适则返回false,此次选择将失效。
			extension : null,
			htmlText: "<font color='#005590' face='宋体'><u>" + $.i18n('antelope.ui.fileupload.addattach') + "</u></font>", 
			_uploadtimeid: null, // 本次上传分组标记 （内部使用，非外部参数)
			
			// 高级接口，自定义加载已经上传过的文件的列表，列表中可能包含有如下数据ispermanent(是否已经持久化) uploadtimeid(当次操作时的对应操作id) willdelete(是否将要被删除)
			// 详细实现方式，请参照如下url对应实现方式
			urlsForCustomUpload: {
				urlForLoadUploadedFiles: ctx + "/upload/getuploadedfileinfos.vot",
				urlForDelFile: ctx + "/upload/delfile.vot",
				urlForDownloadFile: ctx + "/upload/downloadfile.vot",
				urlForUploadFile: ctx + "/upload/uploadfiles.vot"
			}
		},

		_widgetdiv:null,

		_uploadbtndiv:null,

		_filesdiv:null,

		_filecount:0,

		_currfileinfos:[],
		
		_currtimeuploadfiles:[],

		_create: function() {
			
			this.options._uploadtimeid = $.getNewsid();
			
			if (!this.options.filegroupsid) {
				if (this.options.filegroupsidinput && this.options.filegroupsidinput.val()) {
					this.options.filegroupsid = this.options.filegroupsidinput.val();
				} else {
					this.options.filegroupsid = $.getNewsid();
				}
			}
			// 寻找保存timeid的隐藏域
			if (this.options.filegroupsidinput) {
				var timeidinput = $(this.options.filegroupsidinput).next("[name=" + $(this.options.filegroupsidinput).attr('name') + "_fluploadtimesid]");
				if (timeidinput.length == 0) {
					$("<input type='hidden' name='" + $(this.options.filegroupsidinput).attr('name') + "_fluploadtimesid'/>")
					.val(this.options._uploadtimeid).insertAfter(this.options.filegroupsidinput);
				}
				timeidinput.val(this.options._uploadtimeid);
				this.options.filegroupsidinput.val(this.options.filegroupsid);
			}
			
			var thisObj = this;
			
			// 修正urls
			function tidyurls(url) {
				if (url.indexOf("?") == -1)
					url += "?";
				else
					url += "&";
				return url;
			}
			
			this.options.urlsForCustomUpload.urlForLoadUploadedFiles = tidyurls(this.options.urlsForCustomUpload.urlForLoadUploadedFiles);
			this.options.urlsForCustomUpload.urlForDelFile = tidyurls(this.options.urlsForCustomUpload.urlForDelFile);
			this.options.urlsForCustomUpload.urlForDownloadFile = tidyurls(this.options.urlsForCustomUpload.urlForDownloadFile);
			this.options.urlsForCustomUpload.urlForUploadFile = tidyurls(this.options.urlsForCustomUpload.urlForUploadFile);

			// 创建容器
			this._widgetdiv = $("<div/>");

			this._filesdiv = $("<div/>");
			this._filesdiv.appendTo(this._widgetdiv);

			this._widgetdiv.appendTo(this.element);
			
			this.element.addClass("fileupload");

			this.appendUpdatedFiles(null, true);

			if (this.element.attr("enabled") == 'false' || this.element.attr("enabled") == false) {
				this.options.readonly = true;
			}

			// 只读的话，不挂接上传按钮
			if (this.options.readonly)
				return;


			this._uploadbtndiv = $("<div class='uploadfileclass'/>");
			this._uploadbtndiv.prependTo(this._widgetdiv);

			this.reloadFileUpDownloader();


			// 添加对当前界面disabled属性 以及 enabled 属性修改事件的监听
			$(this.element).bind("attrchange", function(event, name, value){
				if (name == 'disabled' && value == 'true' || name == 'enabled' && (value == 'false' || value == false)) {
					thisObj._uploadbtndiv.hide();
					thisObj.options.readonly = true;
				} else {
					thisObj._uploadbtndiv.show();
					thisObj.options.readonly = false;
				}
			});
		},
		
		reloadFileUpDownloader: function() {
			var thisObj = this;
			
			this._uploadbtndiv.loadFileUpDownloader ({wmode:'transparent', extension:this.options.extension, append:true, htmlText: this.options.htmlText}, function(){
				var thisbtnobj = this;
				this.selectFile (function (filelist) {
					
					if ($(".sysfilea",thisObj._filesdiv).size() + filelist.length > thisObj.options.maxfilecount) {
						alert ($.i18n('antelope.ui.fileupload.allowmax.prefix')+thisObj.options.maxfilecount+$.i18n('antelope.ui.fileupload.allowmax.suffix'));
						return false;
					}

					for (var i in filelist) {
						if (filelist[i].size > thisObj.options.maxfilesize * 1024) {
							alert ($.i18n('antelope.ui.fileupload.fileistobig'));
							return false;
						}
					}
					
					if (thisObj.options.selectFileCallback(filelist) === false) {
						return false;
					}

					thisObj._filecount += filelist.length;
					for (var i in filelist) {
						with (filelist[i]) {
							$("<a class='sysfilea' title='"+name+"' href='javascript:void(0)' id='"+id+"'><span class='filename'>"+name+"</span><span class='percloaded' style='font-weight:bold'>(0%)</span><span fid='"+id+"' class='ui-icon ui-icon-closethick' style='display:none'/></a>")
							.hover (function (e){
								$(this).find ("span[fid]").show();
							} , function (e) {
								$(this).find ("span[fid]").hide();
							}).appendTo (thisObj._filesdiv).find ("span[fid]")
							.click (function (){
								thisbtnobj.removefiles([$(this).attr ("fid")]);
								$(this).parent().remove();
								thisObj._filecount--;
							});
						}
					}

					if (thisObj.options.autosave) {
						setTimeout(function(){
							thisObj.upload();
						}, 500);
					}

				}).progress (function (info) {
					$("#"+info.id+" .percloaded").html ("(" + Math.min(100, (info.bytesLoaded/info.size  * 100)).toFixed(0) + "%" + ")");
				}).uploadComplete (function (info) {
					$("#"+info.id).html ($("#"+info.id).html().replace (/\([^)]*\); /,  "("+info.size/1000+"K) "));
					thisObj._filecount--;
					info.filename = info.name;
					thisObj.options.fileChange("addFile", info);
					if (!thisObj._filecount) {
						thisObj.appendUpdatedFiles(function(){
							thisObj.element.validate();
						});
						thisObj._uploadbtndiv.removefiles();
						thisObj.options.uploadComplete();
					}
				});
			});
		},

		// 重新加载已上传文件
		appendUpdatedFiles:function(callback, isinit) {
			var thisObj = this;
			
			$.getJSON(this.options.urlsForCustomUpload.urlForLoadUploadedFiles + "filegroupsid=" + this.options.filegroupsid, function(data){
				thisObj._currfileinfos = [];
				thisObj._filesdiv.empty();
				for (var i = 0; i < data.length; i++) {
					if (isinit) {
						if (data[i].ispermanent) {
							thisObj._appendDownload(data[i]);
							thisObj._currfileinfos.push(data[i]);
						}
					} else {
						if (data[i].uploadtimeid == thisObj.options._uploadtimeid && data[i].willdelete == '1')
							continue;
						
						if (!data[i].ispermanent && data[i].uploadtimeid != thisObj.options._uploadtimeid)
							continue;
						
						thisObj._appendDownload(data[i]);
						thisObj._currfileinfos.push(data[i]);
					}
				}

				if ($.isFunction(callback)) {
					callback();
				}
			});
		},

		getFileInfos: function() {
			return this._currfileinfos;
		},

		_setOption: function( key, value ) {
			$.Widget.prototype._setOption.apply( this, arguments );
			if (key == 'filegroupsid' && typeof value != 'undefined') {
				if (this.options.autosave)
					this.appendUpdatedFiles();
			}
			
		},

		// 查看是否有需要上传的文件
		hasFileToUpload:function(){
			return this._filecount != 0;
		},

		// 添加可供下载的文件列表
		_appendDownload:function (data) {
			var thisObj = this;
			var uploadstr = "";
			if (this.options.uploadDateVisible && data.uploaddate) {
				uploadstr = "(" + data.uploaddate + ")";
			}
			if(/(pdf|gif|png|jpg|jpeg)$/.test(data.filename) && thisObj.options.picturePreview) {
				
				var fileline = "<a title='"+data.filename+"' class='sysfilea' filedatasid='"+data.sid+"' href='javascript:void(0)'>\
						<span class='filename'>"+data.filename+uploadstr+"</span><span sid="+data.sid+" class='ui-icon ui-icon-closethick' style='display:none;width:13px; height:13px;'/>";
				
				if (thisObj.options.showDescription) {
					if (data.filedesc) {
						fileline += "<span class='filename'>&nbsp;(" + (data.filedesc||'') + ")</span>";
					}
				}
				
				fileline += "</a>";
				
				thisObj._filesdiv.append($(fileline).data("fileinfo", data).click(function(e){
					if (!Check_AdobeReader().installed) {
						alert("请您下载保存并安装pdf阅读器！");
						$.postIframe(ctxPath + "/ocx/AdbeRdr1010_zh_CN.exe");
						e.preventDefault();
					}
				}).hover (function (e){
					if (thisObj.options.readonly)
						return;
					$(this).find ("span[sid]").show();
				} , function (e) {
					$(this).find ("span[sid]").hide();
				}).find ("span[sid]")
				.click (function (e){
					var imgthis = this;
					
					$.post(thisObj.options.urlsForCustomUpload.urlForDelFile + "sid=" + $(imgthis).attr("sid") + "&enablepermanent=" + thisObj.options.enablePermanent + "&uploadtimeid=" + thisObj.options._uploadtimeid, function(info){
						if (info)
							info = JSON.parse(info);
						thisObj.options.fileChange("removeFile", info);
						for (var f = 0; f < thisObj._currfileinfos.length; ++f) {
							if (thisObj._currfileinfos[f].sid == $(imgthis).attr("sid")) {
								thisObj._currfileinfos.splice(f, 1);
								break;
							}
						}
						$(imgthis).parent().remove();
					});
					e.stopPropagation();
				}).end().click(function(){
					var self = $(this);
					
					if (/(gif|png|jpg|jpeg)$/.test(self.data("fileinfo").filename) && thisObj.options.picturePreview) {
						$("<div><img src='" + thisObj.options.urlsForCustomUpload.urlForDownloadFile + "sid=" + $(this).attr("filedatasid")+"'/></div>")
						.dialog({width:$(window).width() - 50, height:$(window).height() - 50});
					} else if (thisObj.options.readDocOnline) { // 在线文档阅读
						window.open(ctxPath + "/components/uploaddocview.jsp?sid=" + $(this).attr("filedatasid"), null,
						"height=1400,width=1600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes");
					} else {
						window.open(thisObj.options.urlsForCustomUpload.urlForDownloadFile + "sid=" + $(this).attr("filedatasid"), null,
						"height=400,width=600,status=yes,toolbar=no,menubar=no,location=no");
					}
				}));
			} else {
				
				var fileline = "<a title='"+data.filename +
									"' class='sysfilea' href='javascript:void(0)' onclick='downloadFileFromFileUploadInner(\"" + thisObj.options.urlsForCustomUpload.urlForDownloadFile + "sid=" + data.sid + "\", " + thisObj.options.checklogon + ", " + thisObj.options.readDocOnline + ",\"" +  data.sid + "\")'>\
									<span class='filename'>" + data.filename + uploadstr + "</span>";
				
				if (thisObj.options.showDescription) {
					if (data.filedesc) {
						fileline += "<span class='filename'>&nbsp;(" + (data.filedesc||'') + ")</span>";
					}
				}
				
				fileline += "<span sid=" 
									+ data.sid + " class='ui-icon ui-icon-closethick' style='display:none;width:13px; height:13px;'/>";
				
				if (thisObj.options.showDescription) {
					fileline += "<span sid=" 
									+ data.sid + " class='ui-icon ui-icon-key' style='display:none;width:13px; height:13px;' title='编辑说明'/>";
				}
				
				fileline += "</a>";
				
				thisObj._filesdiv.append($(fileline).hover (function (e){
				if (thisObj.options.readonly)
					return;
				$(this).find ("span[sid]").show();
			} , function (e) {
				$(this).find ("span[sid]").hide();
			}).find ("span[sid].ui-icon-closethick")
				.click (function (e){
					e.stopPropagation();
					var imgthis = this;
					$.post(thisObj.options.urlsForCustomUpload.urlForDelFile + "sid=" + $(imgthis).attr("sid") + "&enablepermanent=" + thisObj.options.enablePermanent + "&uploadtimeid=" + thisObj.options._uploadtimeid, function(info){
						if (info)
							info = JSON.parse(info);
						thisObj.options.fileChange("removeFile", info);
						for (var f = 0; f < thisObj._currfileinfos.length; ++f) {
							if (thisObj._currfileinfos[f].sid == $(imgthis).attr("sid")) {
								thisObj._currfileinfos.splice(f, 1);
								break;
							}
						}
						$(imgthis).parent().remove();
					});
				}).end().find ("span[sid].ui-icon-key")
				.click (function (e){
					e.stopPropagation();
					var imgthis = this;
					
					var desc = "";
					for (var i = 0; i < thisObj._currfileinfos.length; ++i) {
						if (thisObj._currfileinfos[i].sid == $(imgthis).attr("sid")) {
							desc = thisObj._currfileinfos[i].filedesc;
							break;
						}
					}
					
					$("<form>" + 
						"文件说明(限30字)：<input name='desc' style='width:323px; height:29px;' maxlength2='30'/>" + 
						"</form>").find("[name=desc]").val(desc||'').end().submitDialog({
							url: ctxPath + "/upload/saveFileDescription.vot?sid=" + $(imgthis).attr("sid"),
							width: 390, title: "文件说明",
							height: 140,
							callback: function(){
								thisObj.appendUpdatedFiles();
							}
						});
					/*
					var imgthis = this;
					$.post(thisObj.options.urlsForCustomUpload.urlForDelFile + "sid=" + $(imgthis).attr("sid") + "&enablepermanent=" + thisObj.options.enablePermanent + "&uploadtimeid=" + thisObj.options._uploadtimeid, function(info){
						thisObj.options.fileChange("removeFile", info);
						for (var f = 0; f < thisObj._currfileinfos.length; ++f) {
							if (thisObj._currfileinfos[f].sid == $(imgthis).attr("sid")) {
								thisObj._currfileinfos.splice(f, 1);
								break;
							}
						}
						$(imgthis).parent().remove();
						
					}, "json");*/

				}).end());
			}
		},
		
		_tidyfileName: function(filename) {
			return filename;
		},

		upload: function(callback) {
			if (this.options.readonly) // 只读模式不上传文件
				return;
			if ($.isFunction(callback))
				this.options.uploadComplete = callback;
			
			this._uploadbtndiv.upload (this.options.urlsForCustomUpload.urlForUploadFile + "uploadtimeid=" + this.options._uploadtimeid + "&filegroupsid="+this.options.filegroupsid + "&enablePermanent=" + this.options.enablePermanent);
		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );

// 搜索引擎结果展现控件的封装
(function( $, undefined ) {
	$.widget( "ui.searchresults", {
		options: {
			dataProvider:null, // 为[{},{}]
			titleField:'title',
			digestField:'digest',
			sourceField:'source',
			timeField:'time',
			linkCallback:$.noop,
			keywords:null // 可以为'A'也可以为['A','B']
		},

		_create: function() {
			if (!$.isArray(this.options.dataProvider))
				return;

			var thisObj = this;

			var htmllist = '<ul class="searchresults">';

			$(this.options.dataProvider).each(function(index){
				htmllist += '<li><div class="stitlediv"><a rownum="'+index+'" href="javascript:void(0)" class="stitlediv">';

				var regexp = null;
				if ($.isArray(thisObj.options.keywords)) {
					regexp = new RegExp("("+thisObj.options.keywords.join("|")+")","ig");
				} else {
					regexp = new RegExp("("+thisObj.options.keywords+")","ig");
				}
				var title = this[thisObj.options.titleField];
				if (title.length > 30)
					title = title.substring(0,30)+"...";
				htmllist += title.replace(regexp,'<span class="titlekeyword">$1</span>');
				htmllist += '</a></div><div class="digestdiv">';
				var digest = this[thisObj.options.digestField];
				if (digest != null && digest.length > 80)  {
					digest = digest.substring(0,80)+"...";
					htmllist += digest.replace(regexp,'<span class="titlekeyword">$1</span>');
				}
				htmllist += '</div><div class="sourcediv">';
				htmllist += this[thisObj.options.sourceField] +" ";
				htmllist += this[thisObj.options.timeField];
				htmllist += '</div></li>';
			});

			htmllist += "</ul>";

			this.element.append(htmllist);

			$(".stitlediv", this.element).click(function(){
				if ($(this).attr("rownum") != null)
					thisObj.options.linkCallback.call(thisObj.options.dataProvider[$(this).attr("rownum")]);
			});
		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );





//挂接流程审批历史
(function( $, undefined ) {
	$.widget( "ui.audithistory", {
		options: {
			proc_inst_id_:null
		},

		_create: function() {
			this.element.datagrid({
				dataProvider: ctxPath + "/common/workflow/WorkflowCommonController/getWorkflowAuditHistory.vot?proc_inst_id_=" + this.options.proc_inst_id_,
				columns: {
					unitname:
			         {headerText:$.i18n('antelope.assigneeunit'), width:'12%'},
			         assigneename: {headerText:$.i18n('antelope.assigneename'), width:'8%'},
			         result: {headerText:$.i18n('antelope.auditresult'), width:'10%', labelFunction:function(obj){
		        		 return obj.result == null? '' : obj.result;
			         }},
			         taskname: {headerText:$.i18n('antelope.auditphase'), width:'15%'},
			         comment_:{headerText:$.i18n('antelope.auditcomment'), width:'37%'},
			         createtime:{headerText:$.i18n('antelope.audittime'), width:'18%'}
				}, showSeqNum: true, numPerPage: 20
			});
		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );


/**用于表格控件单元格的常用格式化方法*****/
// 典型数字格式化方法 格式为 123,456,789.05
function typicalNumberFormatter(num) {
	return $.valueFormat(num);
}

function typicalIntFormatter(num) {
	return $.valueFormat(num,0);
}

// 典型数字格式化方法 格式为 123,456,789.05%
function typicalPercentageFormatter(num) {
	if (num == null || num == '')
		return '';
	return $.valueFormat(parseFloat(num) * 100) + "%";
}

//典型日期格式化方法 格式为 2011-05-07 直接截断的方式
function typicalDateFormatter(datestr) {
	if (!datestr || datestr.length < 10)
		return datestr;
	return datestr.substring(0,10);
}

//典型时间格式化方法 格式为 2011-05-07 11:32:00 直接截断的方式
function typicalTimeFormatter(datestr) {
	if (!datestr || datestr.length < 19)
		return datestr;
	return datestr.substring(0,19);
}

function typicalLinkFormatter(str) {
	str ="<div class='sssss' >"+ str + "</div>";
	return str;
}

//使用验证插件组，对表单进行验证
function revalidateformcell(thisobj, excludeRequired) {
	var self = $(thisobj);
	
	var noerrored = {isnoerror:true, errorscrope:""};
	// 此处只过滤用户自己隐藏过的dom
	
	var lasthidenuitab = null;
	
	// 将tinymce富文本框过滤掉不需要
	if (self.is(":hidden") && !self.is(".tinymce")) {
		var tempanser = self;
		do {
			var displaytext = null;
			try {
				displaytext = tempanser.css("display");
			} catch(e) {}
			
			if (displaytext == 'none' && !tempanser.hasClass("ui-tabs-hide")) {
				return noerrored;
			}
			
			if (displaytext == 'none' && tempanser.hasClass("ui-tabs-hide")) {
				lasthidenuitab = tempanser;
			}
			tempanser = tempanser.parent();
		} while (tempanser.size());
	}
	
	
	for(var i = 0; i < $.validators.length; i++) {
		if (excludeRequired && ($.validators[i].name == 'required' || $.validators[i].name == 'required2'))
			continue;
		
		var validateval = self.prop($.validators[i].name) || self.attr($.validators[i].name);
		if (validateval) {
			var retval = $.validators[i].validate(self);
			if (retval) {
				
				self.attr("title", retval);
				
				if (window['componentsv2']) {
					self.attr("data-toggle", "tooltip");
					self.attr("data-placement", "bottom");
					self.addClass("ui-state-error");
					self.tooltip("show");
				} else {
					self.addClass("ui-state-error");
				}
				
				//  data-toggle="tooltip" data-placement="bottom" 
				
				noerrored.isnoerror = false;
				
				// 在可能的位置寻找表单名称
				var fieldlabel = self.attr("label");
				
				
				if (!fieldlabel) 
					fieldlabel = self.parent().find(".cd_name").html(); 
				
				
				if (!fieldlabel) 
					fieldlabel = self.parent().parent().find(".cd_name").html();
				
				
				if (!fieldlabel) 
					fieldlabel = self.parent().prev().text();
				
				if (fieldlabel != null) {
					fieldlabel = fieldlabel.replace(/:|：/gi,'');
				}
				
				noerrored.fieldlabel = fieldlabel;
				noerrored.retval = retval;
				break;
			}
		}
	}
	
	if (noerrored.isnoerror) {
		if (window['componentsv2']) {
			self.tooltip("destroy");
		}
		
		self.removeClass("ui-state-error").attr("title", "");
	} else {
		if (lasthidenuitab != null) {
			noerrored.errorscrope = "页签:" + $("[href=#" + lasthidenuitab.attr("id") + "]").text(); 
		}
	}
	
	return noerrored;
}

//典型枚举类型格式化方法，对应枚举xml取 enumXml值
var globalenummaps = {};
function typicalEnumFormatter(enumid, theEnumXml) {
	if (theEnumXml) { // 若第二个传入了枚举xml名称，则执行普通查询输出结果
		if (globalenummaps[theEnumXml]) {
			return globalenummaps[theEnumXml][enumid];
		}
		var enummap = {};
		$.ajax({
			url:ctxPath + "/getsystemenumdatas.vot?xmlname="+ theEnumXml,
			async:false,
			dataType:"json",
			success:function(data) {
				$(data).each(function(){
					enummap[this.value] = this.label;
				});
				globalenummaps[theEnumXml] = enummap;
			}
		});
		return enummap[enumid];
	} else {
		var thisObj = this;
		enumid = $.trim(enumid)
		if (!this.enumXmlContent) {
			$.ajax({
				url:ctxPath + "/getsystemenumdatas.vot?xmlname="+ thisObj.enumXml,
				async:false,
				dataType:"json",
				success:function(data) {
					var enummap = {};
					$(data).each(function(){
						enummap[this.value] = this.label;
					});
					thisObj.enumXmlContent = enummap;
				}
			});
		}
		return this.enumXmlContent[enumid] == null ? "":this.enumXmlContent[enumid];
	}
}

// 典型的通过ajax方式后台取数据后返回到前台
function typicalAjaxFormatter(key) {
	var thisObj = this;
	if (!this.ajaxContent)
		this.ajaxContent = {};

	if (this.ajaxContent[key] == null) {
		$.ajax({
			url:thisObj.url+"?key="+encodeURIComponent(encodeURIComponent(key)),
			async:false,
			success:function(data) {
				thisObj.ajaxContent[key] = data;
			}
		});
	}
	return this.ajaxContent[key] == null ? "" : this.ajaxContent[key];
}

//统计图表excel导出组件 begin
var currexportingstatstype = "";
jQuery.extend({
	exportStatsExcel: function(currenttype, chartBrand) {
		currexportingstatstype = currenttype;
		if (chartBrand) {
			if (chartBrand == 'fusionchart')
				$("#" + currenttype + "chart")[chartBrand]("showImage", "flashImgUploadCompleteForFusionChart");
			else
				$("#" + currenttype + "chart").flashchart("showImage", "flashImgUploadComplete");
		} else {
			$("#" + currenttype + "chart").flashchart("showImage", "flashImgUploadComplete");
		}
	}
});

function flashImgUploadComplete(name, currenttype2) {
	$.postIframe(ctxPath + "/common/StatsUtilController/exportStatsExcel.vot",
			{datagriddata:JSON.stringify($("#"+currexportingstatstype).datagrid("getDataForExport")), imgname:name, "component":component, "currenttype":currenttype});
}

function flashImgUploadCompleteForFusionChart(obj) {
	var filenamepart = /fc[\d]*\.png/g.exec(obj.fileName) + "";
	var postpath = ctxPath + "/common/StatsUtilController/exportStatsExcel.vot";
	if (window['jspquerystring']) {
		postpath += "?" + window['jspquerystring'];	
	}
	$.postIframe(postpath,
			{datagriddata:JSON.stringify($("#"+currexportingstatstype).datagrid("getDataForExport")), imgname:filenamepart, "component":component, "currenttype":currenttype});
}

// 统计图表excel导出组件 end

function hideWidgetPartsUtilFunction_(tobehideObj, tobehideclickgroupname) {
	var parentwin = window;
	while(parentwin && parentwin.$) {
		parentwin.$("body").bind("click." + tobehideclickgroupname, function(){
			if (tobehideObj)
				tobehideObj.hide();
		});
		if (parentwin == top) {
			break;
		}
		parentwin = window.parent;
	}
	$(window).unload(function(){
		var parentwin = window;
		while(parentwin && parentwin.$) {
			parentwin.$("body").unbind("click." + tobehideclickgroupname);
			if (parentwin == top) {
				break;
			}
			parentwin = window.parent;
		}
	});
}

//树控件封装，对zTree功能进行扩展
//注意：当在同一个页面使用多棵树结构时，对应挂接树结构的ul必须填写全局唯一id，否则会出现子树节点打不开的问题
(function( $, undefined ) {
	$.widget( "ui.tree", {
		
		options: {
			expandSpeed : "",
			showLine : true,
			checkable : false,
			dataProvider: null, // zTree数据提供者，若为非异步加载模式，则使用它来完成数据提供，格式为 [{"sid":"root","name":"演示标题","isParent":true}]，更具体请参考zTree文档关于zTreeNodes介绍
			async: true,
			asyncUrl: null,
			asyncParam: ['sid'],
			expandTreeNodeByClick: false,
			callback : {
			   click: $.noop,
			   asyncSuccess: null
			},
			click: null, // 会传递三个参数 event, treeId, treeNode
			dblclick: null,
			locatePath: null,
			locateField: "sid",
			locateUrl: null, // 若存在此参数，则locatePath会重新从此url加载
			checkable : false,
			contextMenu : null, // 上下文菜单
			checkType: { "Y": "s", "N": "s" } // "Y": "ps", "N": "ps" 默认仅影响子节点选中状态
		},
		
		_zTree:null,

		// 用于定位相关参数
		_parentnode:null,
		
		_ctxMenuUl: null,
		
		_treekey: null,
		
		_create: function() {
			var thisObj = this;
			this._treekey = $.Guid.New(); 
			// 异步提取数据时
			this.options.callback.asyncSuccess = function(){
				thisObj._asyncSuccess(thisObj);
			}
			// 简化接口
			if (this.options.click)
				this.options.callback.click = this.options.click;
			if (this.options.dblclick)
				this.options.callback.dblclick = this.options.dblclick;
			
			// 上下文右键菜单支持
			if (this.options.contextMenu && !$.isEmptyObject(this.options.contextMenu)) {
				var ulhtml = "<ul class='treectxmenu'>";
				for (var tkey in this.options.contextMenu) {
					ulhtml += "<li methodkey='" + tkey + "'><a href=\"#\">" + tkey + "</a></li>";
				}
				ulhtml += "</ul>";
				this._ctxMenuUl = $(ulhtml);
				
				for (var i = 0; i < thisObj.element.length; ++i) {
					thisObj.element[i].oncontextmenu = function(){return false};
				}
				
				this.options.callback.rightClick = function(event, treeId, treeNode) {
					if (treeNode) {
						thisObj.element.append(thisObj._ctxMenuUl);
						thisObj._ctxMenuUl.data("cmargs", arguments);
						thisObj._ctxMenuUl.css({"top":event.clientY+"px", "left":event.clientX+"px", "display":"block"});
						thisObj._ctxMenuUl.undelegate("li", "click").delegate("li", "click", function() {
							thisObj.options.contextMenu[$(this).attr("methodkey")].click.apply(this, thisObj._ctxMenuUl.data("cmargs"));
						});
					}
				}
				
			}
			
			this._zTree = this.element.zTree(this.options, this.options.dataProvider);
			
			if (this.options.contextMenu) {
				hideWidgetPartsUtilFunction_(thisObj && thisObj._ctxMenuUl, thisObj._treekey.substring(0, 8));
			}
			
			if (this.options.locateUrl) {
				$.getJSON(this.options.locateUrl, function(data){
					thisObj.locate(data);
				});
			}
		},
		
		unwrap: function() {
			return this._zTree;
		}, 
		
		getSelectedNode: function() {
			return this._zTree.getSelectedNode();
		},
		
		getNodes: function() {
			return this._zTree.getNodes();
		},
		
		getCheckedNodes: function() {
			return this._zTree.getCheckedNodes();
		},
		
		reAsyncChildNodes: function(parentnode, funcstr) {
			return this._zTree.reAsyncChildNodes(parentnode, funcstr);
		},
		
		locate: function(pathArray) {
			this.options.locatePath = pathArray;
			this._asyncSuccess(this);
		},
		
		expandNode: function(treeNode, expandSign, sonSign, focus) {
			return this._zTree.expandNode(treeNode, expandSign, sonSign, focus);
		},
		
		_asyncSuccess: function(thisObj) {
			if (thisObj.options.locatePath != null) {
				while (thisObj.options.locatePath.length) {
					thisObj._parentnode = thisObj._zTree.getNodeByParam(thisObj.options.locateField, thisObj.options.locatePath[0]);
					
					if (thisObj._parentnode != null) {
						thisObj._zTree.expandNode(thisObj._parentnode, true);
						thisObj._zTree.selectNode(thisObj._parentnode);
						thisObj.options.locatePath.shift();
					} else {
						var currselectnode = thisObj._zTree.getSelectedNode();
						if (currselectnode && !currselectnode.isAjaxing && thisObj._zTree.getSelectedNode().open) {
							thisObj.options.locatePath.shift();
						} else {
							thisObj._parentnode = thisObj._zTree.getNodeByParam(thisObj.options.locateField, thisObj.options.locatePath[0]);
							break;
						}
					}
				}
				if (thisObj.options.locatePath.length == 0) {
					if (thisObj.options.click)
						thisObj.options.click();
					thisObj.options.locatePath = null;
				}
			}
		},
		
		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
		
	});
})( jQuery );

//菜单控件的封装 内部使用Spry
(function(){
	$.widget( "ui.menubar", {
		options: {
		},
		_create: function() {
			new Spry.Widget.MenuBar(this.element[0]);
		}
	});
})( jQuery );

// 磁块控件的封装
(function(){
	$.widget( "ui.tilegrid", {
		options: {
			dataProvider: null,
			numPerPage: 20,
			pagebuttonnum:7,
			selectedItems: [], // 保存选中的块数据数组
			pageChanging:jQuery.noop,
			pageChange:jQuery.noop,
			selectionMode: "none", // 可以为 singleRow 多选暂未实现
			tileRenderer: function() {
				return '<div style="border:1px solid black; width:200; height: 187px; float:left; ">' + this.name + '</div>';
			}
		},
		
		/** 当前页面总页数 */
		_totalPage:1,
		/** 当前页号 */
		_currPage:1,
		/** 上一页页号 */
		_pre:1,
		/** 下一页页号 */
		_next:1,
		/** 总记录数*/
		_count:0,
		/** 页面数据列表 */
		_currList:null,
		_allList:null,
		
		_prebutton:null,
		_nextbutton:null,
		
		// 通过urlajax取数
		_url:null,
		
		_tilegroup:null,
		
		_create: function() {
			this._initDatagrid();
		},
		
		_initDatagrid: function() {
			
			var griddata = this.options.dataProvider;
			
			if (!griddata)
				return;

			// 字符串则通过ajax取数
			if (typeof griddata === 'string') {
				this._url = griddata;
				this._createTableBasic();
				return;
			}
		},
		
		_createTileByAjax: function() {
			
			var thisObj = this;

			// 防止非法字符
			this._currPage = parseInt(this._currPage);
			if (isNaN(this._currPage)) {
				this._currPage = 1;
			}
		
			var params = {};
			var newurl = this._url; 
			// 转换使用post方式发送数据
			if (this._url.indexOf("?") != -1 && this._url.split("?").length > 1) {
				var myparams = this._url.split("?")[1];
				newurl = this._url.split("?")[0];
				var paramsarray = myparams.split("&");
				for(var i = 0; i < paramsarray.length; ++i) {
					params[paramsarray[i].split("=")[0]] = decodeURIComponent(paramsarray[i].split("=")[1]);
				}
			}
			this.options.pageChanging();
			$.post(newurl, $.extend(true, {}, params, {page:this._currPage, numPerPage: this.options.numPerPage}), function(data){
				thisObj._count = data.count;
				thisObj._currPage = data.currPage;
				thisObj._pre = data.pre;
				thisObj._next = data.next;
				thisObj._totalpage = data.totalPage;
				thisObj._currList = data.currList;
				thisObj._gotoCurrPageInner();
				thisObj.options.pageChange(thisObj._currList);
			},"json");
		},
		
		// 公用创建当前页数据相及翻页元素
		_gotoCurrPageInner: function() {
			var thisObj =this;

			// 创建tiles
			var tileshtml = "";
			thisObj._tilegroup.html('');
			for (var i = 0; i < thisObj._currList.length; ++i) {
				var htmlorjqobj = this.options.tileRenderer.call(thisObj._currList[i], {currPage:thisObj._currPage});
				if (typeof htmlorjqobj === 'string') {
					tileshtml += htmlorjqobj;
				} else {
					thisObj._tilegroup.append(htmlorjqobj);
				}
			}
			
			if (tileshtml) {
				thisObj._tilegroup.html(tileshtml);
			}
			
			// 单选时，记录单选项数据
			if ('singleRow' == thisObj.options.selectionMode) {
				thisObj._tilegroup.children().click(function(){
					thisObj.options.selectedItems = [thisObj._currList[$.inArray(this, thisObj._tilegroup.children())]];
				});
			}

			// 生成某页序列并添加事件
			// 某页序列
			if (this.options.pagebuttonnum != 0) { // 若分页按钮为0 则直接不显示
				var numbtn = Math.min(this.options.pagebuttonnum, this._totalpage);
				var beginpage = Math.max(this._currPage - parseInt(numbtn / 2), 1);
				var endpage = Math.min(this._totalpage, beginpage + numbtn);
				beginpage = Math.max(endpage - numbtn, 1);
	
				var numpagestr = '';
				for (var i = beginpage; i < endpage; i++) {
					numpagestr += createNumpageBtn(this, i);
				}
	
				if (endpage < this._totalpage)
					numpagestr += '<span>...</span>';
				numpagestr += createNumpageBtn(this, this._totalpage);
				if (this._numpagebutton)
					this._numpagebutton.remove();
	
				this._numpagebutton = $(numpagestr);
				this._nextbutton.before(this._numpagebutton);
				this._numpagebutton.click(function(){
					if (thisObj._currPage == this.value)
						return;
					thisObj._currPage = this.value;
					thisObj._createTileByAjax.call(thisObj);
				});
			}

			// 创建数字翻页按钮
			function createNumpageBtn(thisObj, intval) {
				if (thisObj._currPage == intval)
					return '<input class="num_page num_page_focus" type="button" value="' + intval + '">';
				else
					return '<input class="num_page" type="button" value="' + intval + '">';
			}
		},
		// 创建表格基本结构
		_createTableBasic: function() {
			var thisObj = this;
			
			this._tilegroup = $('<div class="tilegroup" style="float:left;"></div>');
			this.element.append(this._tilegroup);

			// 分页区域
			this._pagepos = $('<span class="pagepos" style="float:none; margin:0 auto; min-width:30px; width:auto;"></span>');
			this._pageposouter = $('<div style="margin-top:24px; clear:left; text-align:center;"></div>');
		
			this._pageposouter.append(this._pagepos);

			this.element.append(this._pageposouter);
			// 一系列表格页
			this._firstbutton=$('<input class="first_page" type="button" value="' + $.i18n("antelope.firstpage") + '"/>');
			this._lastbutton=$('<input class="last_page" type="button" value="' + $.i18n('antelope.lastpage') + '"/>');
			this._prebutton=$('<input class="prev_page" type="button" value="' + $.i18n('antelope.prepage') + '"/>');
			this._nextbutton=$('<input class="next_page" type="button" value="' + $.i18n('antelope.nextpage') + '"/>');

			this._currpagefield=$('<input class="currpage" value="1">');
			this._gopagebutton=$('<input class="gopage" type="button" value="' + $.i18n('antelope.ok') + '">');
			
			// 每页显示条目数量可选项
			var canperpages = [10, 20, 40, 100];
			var selecthtml = "<select>";
			var perpageprefix = $.i18n('antelope.rowperpageprefix');
			var perpagesuffix = $.i18n('antelope.rowperpagesuffix');
			for (var i = 0; i < canperpages.length; ++i) {
				selecthtml += '<option value="' + canperpages[i] + '">'+ perpageprefix + canperpages[i] + " " + perpagesuffix + '</option>';
			}
			selecthtml += "</select>";
			
			this._numcurrpagelist=$(selecthtml);
			
			this._numcurrpagelist.val(this.options.numPerPage);

			this._pagecountinfo = $('<span>' + $.i18n('antelope.totalpageprefix') + '<span class="thepagenum"></span>' + $.i18n('antelope.totalpagesuffix') + 
					' <span class="thecountnum"></span>' + $.i18n('antelope.row') + '</span>');

			this._pagepos.append(this._prebutton);
			this._pagepos.append(this._numpagebutton);
			this._pagepos.append(this._nextbutton);

			// 上一页
			this._prebutton.click(function(){
				if (thisObj._currPage == thisObj._pre)
					return;
				thisObj._currPage = thisObj._pre;
				thisObj._createTileByAjax.call(thisObj);
			});

			// 下一页
			this._nextbutton.click(function(){
				if (thisObj._currPage == thisObj._next)
					return;
				thisObj._currPage = thisObj._next;
				thisObj._createTileByAjax.call(thisObj);
			});
			
			// 表格创建完成之后默认进入第一页
			this._createTileByAjax.call(thisObj);
		},
		
		_setOption: function(key, value) {
			$.Widget.prototype._setOption.apply( this, arguments );
			if (key == 'dataProvider') {
				this._url = value;
				this._createTileByAjax();
			}
		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );


//flashslider互联网最常用的flash幻灯片组件
(function(){
	$.widget( "ui.flashslider", {
		options: {
			dataProvider: null,
			width: 312,
			height: 212,
			textheight: 20,
			interval_time: 6
		},
		
		_create: function() {
			var thisObj = this;
			$.getJSON(thisObj.options.dataProvider, function(data){
				var picsstrarr = [];
				var linkstrarr = [];
				var textstrarr = [];
				for (var i = 0; i < data.length; ++i) {
					picsstrarr.push(data[i].path);
					linkstrarr.push(data[i].href);
					textstrarr.push(data[i].name);
				}
				
				var embedstr = '<embed height="' + (thisObj.options.height) + '" \
				width="' + thisObj.options.width + '" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" quality="high" menu="false"\
				flashvars="borderwidth=' + thisObj.options.width + '&amp;borderheight=' + (thisObj.options.height - thisObj.options.textheight) + '&amp;textheight=' + thisObj.options.textheight + '&amp;interval_time=' + thisObj.options.interval_time 
				+ '&amp;pics=' + picsstrarr.join("|") + '&amp;links=' + linkstrarr.join('|') + '&amp;texts=' + textstrarr.join("|") + '" wmode="opaque"\
				src="' + ctx + '/controls/flash/usualfocus.swf" />';
				thisObj.element.append(embedstr);
			});
		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
})( jQuery );

// 表格控件的封装
(function( $, undefined ) {
	$.widget( "ui.datagrid", {

		options: {
			dataProvider: [],
			/**
			 * 可以为{sid:"dd", name:{sortField:"fieldname", headerText:'dd', labelFunction:labelFunc, 
			 * itemEditor: {type:'select', optionField:'opts', labelField:'label', valueField:'val', showWhen:'always或dblclick'} 
			 * // 此参数仅支持前台分页形式,并且仅支持columns有值情况，不支持数组形式, 注意其中的四个参数必须写完整，否则可能会出现问题  showWhen表示显示时机，默认为dblclick即双击时显示，可以配置为always则永远显示
			 * width:'30%', type:'checkbox', textAlign:'left', itemRenderer:'<div>${data}</div>'}, editable:false,
			 *  enumXmlField:'xmlfield'//此项指明数据项是否采用枚举取数，若采用根据的是数据项的那列标明枚举xml文件名}
			 */
			columns: null, 
			locateItemSid: null, // 用于定位页的当前数据项sid，仅在遇到刷新页时使用一次，此选项仅适用于 ajax后台分页
			showSeqNum:false, // 是否显示序号
			// 是否使用小型翻页区域 小型翻页区域仅仅包含有 上页 下页 首页 尾页 等超链接文字，此参数优先级极高，使用它原有作用于翻页区域的参数，如pagebuttonnum均不可再次使用
			usingTinyPageArea: false,
			showBusyState:false,//是否显示加载状态
			showNumperpage:true, // 是否显示调整每页显示数量下拉列表
			sortableColumns:true, // 是否允许按列排序 , 默认为允许
			visibleHeader:true, // 是否显示表头
			numPerPage: 10,
			buttons: null, // 可以是{'query':function(){}}也可以是[{text:'query', click:function(){}, visibleFunction:function(obj){}}]
			pagebuttonnum: 5,
			alwaysShowPageButton: false, // 是否总是显示翻页按钮，若为true,则总是显示翻页按钮
			selectedItems: [], // 当为多选时保存选中的行数据数组
			selectedItem: null, // 当为单选时，存放被选中的行数据
			centerButton: false,
			selectionMode: "none", // 可以为 singleRow 或 multipleRows
			rowClickEnabled: false, // 是否启用行点击事件的监听（用于单行选择或多行选择时支持点击行即选中的情况）
			gridClick:$.noop, // 当单元格被点击时触发事件
			/** 当当前选择行的状态发生变化时触发事件 function(kind, influencedrows)
			 *  kind 为类型 addRow,removeRow 或 singleRow influencedrows为对应影响到了的数据 当为singleRow时为列表单选*/
			cellEditUrl: null,
			selectionChange:$.noop,
			operationHeaderText:$.i18n('antelope.operation'),
			operationHeaderWidth: "70px",
			pageChange: jQuery.noop
		},
		
		// 缓存初始每页显示条数
		_originNumPerPage: 10,

		_buttonclasses:{
			del: {styleName:'ui-icon ui-icon-trash', toolTip:$.i18n('antelope.delete')}, // 垃圾桶
			query: {styleName:'ui-icon ui-icon-circle-zoomout', toolTip:$.i18n('antelope.view')}, // 放大镜
			key: {styleName:'ui-icon ui-icon-key', toolTip:$.i18n('antelope.config')}, // 钥匙
			light: {styleName:'ui-icon ui-icon-lightbulb', toolTip:$.i18n('antelope.explain')}, // 灯泡
			release: {styleName:'ui-icon ui-icon-extlink', toolTip:$.i18n('antelope.release')},
			update: {styleName:'ui-icon ui-icon-pencil', toolTip:$.i18n('antelope.modify')},
			exp: {styleName:'ui-icon ui-icon-arrowreturnthick-1-s', toolTip:$.i18n('antelope.exp')},
			add: {styleName:'ui-icon ui-icon-circle-plus', toolTip:$.i18n('antelope.add')},
			remove: {styleName:'ui-icon ui-icon-circle-close', toolTip:$.i18n('antelope.delete')},
			
			// 自定义icon
			i_handbook: {styleName:'ui-icon2 ui-icon2-handbook', toolTip:'笔记本'},
			i_query: {styleName:'ui-icon2 ui-icon2-query', toolTip:$.i18n('antelope.view')},
			i_queryfolder: {styleName:'ui-icon2 ui-icon2-queryfolder', toolTip:$.i18n('antelope.view')},
			i_release: {styleName:'ui-icon2 ui-icon2-release', toolTip:$.i18n('antelope.release')},
			i_releasebook: {styleName:'ui-icon2 ui-icon2-releasebook', toolTip:'发布作业'},
			i_send: {styleName:'ui-icon2 ui-icon2-send', toolTip:'发送'},
			i_ignore: {styleName:'ui-icon2 ui-icon2-ignore', toolTip:'忽略'},
			i_reply: {styleName:'ui-icon2 ui-icon2-reply', toolTip:'回复'},
			i_money: {styleName:'ui-icon2 ui-icon2-money', toolTip:'缴费信息'},
			i_chat: {styleName:'ui-icon2 ui-icon2-chat', toolTip:'进入讨论'},
			i_gate: {styleName:'ui-icon2 ui-icon2-gate', toolTip:'进入讨论大厅'},
			i_play: {styleName:'ui-icon2 ui-icon2-play', toolTip:'开始讨论'},
			i_input: {styleName:'ui-icon2 ui-icon2-input', toolTip:'录入成绩'},
			i_config: {styleName:'ui-icon2 ui-icon2-config', toolTip:'配置'},
			i_configclass: {styleName:'ui-icon2 ui-icon2-configclass', toolTip:'配置班级'},
			i_configgrid: {styleName:'ui-icon2 ui-icon2-configgrid', toolTip:'配置课程'},
			i_configreply: {styleName:'ui-icon2 ui-icon2-configreply', toolTip:'批改'},
			i_del: {styleName:'ui-icon2 ui-icon2-del', toolTip:'删除'},
			i_audit: {styleName:'ui-icon2 ui-icon2-audit', toolTip:'审核'},
			i_auditpass: {styleName:'ui-icon2 ui-icon2-auditpass', toolTip:'审批通过'},
			i_viewvideo: {styleName:'ui-icon2 ui-icon2-viewvideo', toolTip:'收看'},
			i_stopchat: {styleName:'ui-icon2 ui-icon2-stopchat', toolTip:'讨论结束'},
			i_submit: {styleName:'ui-icon2 ui-icon2-submit', toolTip:'提交'},
			i_synchronize: {styleName:'ui-icon2 ui-icon2-synchronize', toolTip:'同步作业库'},
			i_modify: {styleName:'ui-icon2 ui-icon2-modify', toolTip:'修改'},
			i_viewpaper: {styleName:'ui-icon2 ui-icon2-viewpaper', toolTip:'阅卷'},
			i_live: {styleName:'ui-icon2 ui-icon2-live', toolTip:'直播预览'},
			i_up: {styleName:'ui-icon2 ui-icon2-up', toolTip:'上移'},
			i_down: {styleName:'ui-icon2 ui-icon2-down', toolTip:'下移'},
			i_recall: {styleName:'ui-icon2 ui-icon2-recall', toolTip:'撤回'}
		},

		// 表头可以使用的样式组
		_headerstyles: ['width'],

		// 表体可以使用的样式组
		_cellstyles: {'textAlign':'text-align', 'paddingLeft':'padding-left', 'width':'width', 'whiteSpace':'white-space'},

		/** 当前页面总页数 */
		_totalPage:1,
		/** 当前页号 */
		_currPage:1,
		/** 上一页页号 */
		_pre:1,
		/** 下一页页号 */
		_next:1,
		/** 总记录数*/
		_count:0,
		/** 页面数据列表 */
		_currList:null,
		_allList:null,

		// 表头数据
		_headerdata:null,
		// 表格jquerytable
		_jqtable:null,

		// 一系列表格页按钮和域
		_pagepos:null, // 分页总区域
		_prebutton:null,
		_nextbutton:null,
		_firstbutton:null,
		_lastbutton:null,
		_numpagebutton:null,
		_currpagefield:null,
		_currpagespan:null, // 当使用小翻页区域时设置总页数和当前页数的span
		_gopagebutton:null,
		_pagecountinfo:null,
		_numcurrpagelist:null, // 每页显示数量控件
		

		_widgetcreated:false,
		// 排序相关
		_sortth:null,
		_isup:false,
		_colkey:null, // 当前排序列状态

		// 通过urlajax取数
		_url:null,
		
		_gotopageFunc:null, // 当前使用的页面转到方法

		_create: function() {
			if (this.options.selectedItem != null || this.options.selectedItems != null && this.options.selectedItems.length > 0) {
				alert("注意，在创建datagrid时所传入的已选中项将不会被自动选中，请通过选项获取方式重新获取dataProvider数据后重新设置已选中项！");
			}
			
			// 缓存初始每页条数
			this._originNumPerPage = this.options.numPerPage;
			
			
			this._initDatagrid();
		},

		_initDatagrid: function() {
			var griddata = this.options.dataProvider;
			if (!griddata)
				return;
			//加载开始
			if(this.options.showBusyState)
			$.showBusyState();
			// 数组则为实际数据
			if ($.isArray(griddata)) {
				if (!griddata.length && this.options.columns == null) {
					alert("未发现表头数据，传入表格控件的第一行必须为表头数据！");
					return;
				}
				this._createTableByArray(griddata);

				if (griddata && griddata.length) {
					this._widgetcreated = true;
				}

				return;
			}

			// 字符串则通过ajax取数
			if (typeof griddata === 'string') {
				this._createTableByAjax(griddata);
				return;
			}

			// 方法则通过回调取数
			if ($.isFunction(griddata)) {

				return;
			}
		},

		_setOption: function(key, value) {
			if (!$.isFunction(value)) {
				var copiedvalue = JSON.parse(JSON.stringify(value));
				$.Widget.prototype._setOption.apply( this, arguments );
				if (key == 'dataProvider') {
					if($.isArray(value)) {
						this._allList = copiedvalue;
						this.options.dataProvider = copiedvalue;
						this._sortWhenDataProviderIsArray();
						this._arrgotocurrPage();
						if (value && value.length) {
							this._widgetcreated = true;
						}
					} else {
						this._url = value;
						this._ajaxgotocurrPage();
					}
				}
			}
			
			if (key == 'selectedItems') {
				this._gotoCurrPageWithRightGotoFunc();
			}
		},
		
		_gotoCurrPageWithRightGotoFunc:function() {
			if($.isArray(this.options.dataProvider)) {
				this._arrgotocurrPage();
			} else {
				this._ajaxgotocurrPage();
			}
		},

		// 当使用ajax时创建表格对象
		_createTableByAjax: function(url) {
			this._url = url;


			if (this.options.columns) {
				this._setHeaderDataByColumns();
			} else {
				this._headerdata = [];
			}
			// 创建表格基础（表格，排序，翻页框架）
			this._createTableBasic(this._ajaxgotocurrPage, $.noop);
		},

		// 当columns有值时设置表头数据
		_setHeaderDataByColumns: function() {
			this._headerdata = this.options.columns;
		},

		// ajax方式转入当前页
		_ajaxgotocurrPage: function() {
			//加载开始状态
			if(this.options.showBusyState)
			$.showBusyState();


			var thisObj = this;

			// 防止非法字符
			this._currPage = parseInt(this._currPage);
			if (isNaN(this._currPage)) {
				this._currPage = 1;
			}
			
			// 排序之前将已选中的需要清空
			this.options.selectedItem = null;
			this.options.selectedItems = [];
		
			var params = {};
			var newurl = this._url; 
			// 转换使用post方式发送数据
			if (this._url.indexOf("?") != -1 && this._url.split("?").length > 1) {
				var myparams = this._url.split("?")[1];
				newurl = this._url.split("?")[0];
				var paramsarray = myparams.split("&");
				for(var i = 0; i < paramsarray.length; ++i) {
					params[paramsarray[i].split("=")[0]] = decodeURIComponent(paramsarray[i].split("=")[1]);
				}
			}
			
			if (this.options.locateItemSid) {
				params['locateItemSid'] = this.options.locateItemSid;
				this.options.locateItemSid = null;
			}
			
			$.post(newurl, $.extend(true, {}, params, {page:this._currPage, numPerPage: this.options.numPerPage, sortcol:this._sortth && this._colkey || '', isup:this._isup}), function(data){
				thisObj._count = data.count;
				thisObj._currPage = data.currPage;
				thisObj._pre = data.pre;
				thisObj._next = data.next;
				thisObj._totalpage = data.totalPage;
				thisObj._currList = data.currList;
				thisObj._gotoCurrPageInner(thisObj._ajaxgotocurrPage);
			},"json");
		},

		//数组方式转入当前页
		_arrgotocurrPage: function() {
			//------准备页相关数据
			// 总数
			this._count = this._allList.length;
			// 当前页
			this._currPage = parseInt(this._currPage);
			if (isNaN(this._currPage) || this._currPage < 1) {
				this._currPage = 1;
			}

			this._totalpage = parseInt((this._count - 1) / this.options.numPerPage) + 1;
			if (this._totalpage == 0) // 若总页数，最少为1.
				this._totalpage = 1;
			// 大于最大页数时设置为最大页数
			if (this._currPage > this._totalpage)
				this._currPage = this._totalpage;

			// 上一页
			this._pre = (this._currPage == 1 ? 1 : this._currPage - 1);

			// 下一页
			var len = this._currPage * this.options.numPerPage;
			if (len >= this._count) {
				this._next = this._currPage;
				len = this._count;
			} else {
				this._next = this._currPage + 1;
			}

			// 添加当前页所需要的页面列表数据项
			this._currList = [];
			for (var i = (this._currPage - 1) * this.options.numPerPage; i < len; i++) {
				this._currList.push(this._allList[i]);
			}

			this._gotoCurrPageInner(this._arrgotocurrPage);
		},

		// 公用创建当前页数据相及翻页元素
		_gotoCurrPageInner: function(gotopageFunc) {
			var thisObj =this;
			thisObj.options.pageChange(thisObj._currList); // 回调页面更改事件
			this._currpagefield.val(this._currPage);
			
			if (this.options.usingTinyPageArea) {
				this._currpagefield.val(this._currPage + "/" + this._totalpage);
				this._currpagespan.text(this._currPage + "/" + this._totalpage);
			}

			var bodytr = "";

			// 单选时使用的guid;
			var singleguid = $.Guid.New();
			for (var i = 0; i < this._currList.length; i++) {
				bodytr += "<tr class='datatr'>";

				// 添加对行选择的支持
				// 单行选择
				if (this.options.selectionMode === 'singleRow') {
					var isselected = this._currList[i] == this.options.selectedItem;
					bodytr += "<td><input class='row_select_input' rownum='" + i + "' name='"+singleguid+"' type='radio' "+(isselected?'checked':'')+"/></td>";
				}
				// 多行选择
				if (this.options.selectionMode === 'multipleRows') {
					var isselected = $.inArray(this._currList[i], this.options.selectedItems) != -1;
					bodytr += "<td><input class='row_select_input' rownum='" + i + "' type='checkbox' "+(isselected?'checked':'')+"/></td>";
				}
				// 显示序号
				if (this.options.showSeqNum) {
					bodytr += "<td style='text-align:center;'>"+(this.options.numPerPage*(this._currPage-1)+i+1)+"</td>";
				}

				// 表头为数组
				if ($.isArray(this._headerdata)) {
					$(this._headerdata).each(function(colidx){
						// 添加支持 {html:"<a>33</a>", val:33}显示值和真实值分离的数据格式
						var celldata = thisObj._currList[i][this.dataField];
						if (celldata == null) celldata = '';

						bodytr += "<td rowIndex='"+i+"' columnIndex='"+colidx+"' "+thisObj._createTableCellStyle(this)+">"+thisObj._createTableCell(i, celldata, this)+"</td>";
					});
				} else {
					var colcounter = 0;
					for (var obj in this._headerdata) {
						// 添加支持 {html:"<a>33</a>", val:33}显示值和真实值分离的数据格式
						var celldata = this._currList[i][obj];
						if (celldata == null) celldata = '';
						bodytr += "<td rowIndex='"+i+"' columnKey='"+obj+"' "+thisObj._createTableCellStyle(this._headerdata[obj])+">"+thisObj._createTableCell(i, celldata, this._headerdata[obj], obj)+"</td>";
						colcounter++;
					}
				}

				// 若有操作按钮则添加操作列按钮
				if (this.options.buttons) {
					bodytr += "<td>"
					// 数组或对象
					$.each(this.options.buttons, function(name, props) {
						props = $.isFunction( props ) ? { click: props, text: name } : props;
						if (typeof name == 'string') {
							props.text = name;
						}
						// 根据按钮的属性可见性判断
						var visible = true;
						if ($.isFunction(props.visibleFunction)) {
							visible = props.visibleFunction.call(thisObj._currList[i], thisObj._currList[i]);
						}
						if (!visible)
							return;

						// 工具提示
						var tooltipstr = "";
						if (props.toolTip) {
							tooltipstr = " title='" + props.toolTip + "' ";
						} else {
							if (thisObj._buttonclasses[props.text]) {
								tooltipstr = " title='" + thisObj._buttonclasses[props.text].toolTip + "' ";
							}
						}
						if (thisObj._buttonclasses[props.text]) {
							if (thisObj.options.centerButton) {
								bodytr += "<input _rowpos='"+i+"' " + tooltipstr + "  method='"+props.text+"' style='border:0; margin-left:3px;cursor:pointer;' class='"+thisObj._buttonclasses[props.text].styleName+"' type='button'/>";
							} else {
								bodytr += "<input _rowpos='"+i+"' " + tooltipstr + "  method='"+props.text+"' style='border:0; float:left; margin-left:3px;cursor:pointer;' class='"+thisObj._buttonclasses[props.text].styleName+"' type='button'/>";
							}
						} else {
							bodytr += "<input _rowpos='"+i+"' " + tooltipstr + " type='button' method='"+props.text+"' value='" + props.text + "'/>";
						}
					});

					bodytr += "</td>"
				}

				bodytr += "</tr>";
			}
			$(".datatr", this._jqtable).remove();


			// 表格内容挂接
			this._jqtable.append(bodytr);

			// 挂接完成之后重置行选中状态
			this._resetSelectedClass();

			this._jqtable.find(":button").click(function(){
				// 若按钮为数组形式，则遍历寻找对应触发事件的方法
				var method = $(this).attr("method");
				var rpos = $(this).attr("_rowpos");
				if ($.isArray(thisObj.options.buttons)) {
					for (var i = 0; i < thisObj.options.buttons.length; i++) {
						var btn = thisObj.options.buttons[i];
						if (btn.text == method) {
							btn.click.call(thisObj._currList[rpos], thisObj.element);
							break;
						}
					}
				} else {
					if ($.isFunction(thisObj.options.buttons[method])) {
						thisObj.options.buttons[method].call(thisObj._currList[rpos], thisObj.element);
					} else {
						thisObj.options.buttons[method].click.call(thisObj._currList[rpos], thisObj.element);
					}
				}
			});

			// 设置复选框列点击操作(给非行选择的checkbox添加)
			this._jqtable.find("[dataField]:checkbox").click(function(){
				var self = $(this);
				if (thisObj._currList[self.attr("rownum")][self.attr("dataField")] === true || thisObj._currList[self.attr("rownum")][self.attr("dataField")] === false)
					thisObj._currList[self.attr("rownum")][self.attr("dataField")] = self.prop("checked");
				else
					thisObj._currList[self.attr("rownum")][self.attr("dataField")] = self.prop("checked") ? 1 : 0;
			});

			// 添加行选择checkbox点击事件
			this._jqtable.find(".row_select_input:checkbox").click(function(e){
				checkoneline(this, thisObj);
				e.stopPropagation();
			});
			
			// 对于总是显示下拉列表的添加onchange事件
			this._jqtable.find("[alwaysselshow]").change(function(e) {
				thisObj._cellEndEdit.call(thisObj, e);
			});
			
			// 当行选中时需要执行
			function checkoneline(linecheckbox, jqtableobj) {
				var kind = ""; // 区分当前是选中类型还是取消选中类型
				
				// 单选形式
				if ($(linecheckbox).is(":radio")) {
					jqtableobj.options.selectedItem = jqtableobj._currList[$(linecheckbox).attr("rownum")];
					jqtableobj.options.selectedItems = [jqtableobj.options.selectedItem];
					kind = 'singleRow';
				} else { // 多选形式
					if ($(linecheckbox).prop("checked")) {
						addSelectedItem(jqtableobj, linecheckbox);
						kind = "addRow";
					} else {
						removeFromSelectedItems(jqtableobj, linecheckbox);
						kind = "removeRow";
					}
				}

				jqtableobj._resetSelectedClass();

				// 根据当前选中状态重置全选复选框是否选中的状态
				jqtableobj._jqtable.find(".select_all_row").prop("checked", function(){
					return jqtableobj._jqtable.find(".row_select_input:checkbox:not(:checked)").size() == 0;
				});

				// 触发选项变化事件方法
				jqtableobj.options.selectionChange(kind, [jqtableobj._currList[$(linecheckbox).attr("rownum")]]);
			}

			// 全选当前页的所有行
			this._jqtable.find(".select_all_row").click(function(){
				var kind = ""; // 区分当前是选中类型还是取消选中类型
				var influencedrowdata = [];
				if ($(this).prop("checked")) {
					thisObj._jqtable.find(".row_select_input:checkbox:not(:checked)").prop("checked", $(this).prop("checked")).each(function(){
						addSelectedItem(thisObj, this);
						influencedrowdata.push(thisObj._currList[$(this).attr("rownum")]);
					});
					kind = "addRow";
				} else {
					thisObj._jqtable.find(".row_select_input:checkbox:checked").prop("checked", $(this).prop("checked")).each(function(){
						removeFromSelectedItems(thisObj, this);
						influencedrowdata.push(thisObj._currList[$(this).attr("rownum")]);
					});
					kind = "removeRow";
				}
				thisObj._resetSelectedClass();

				// 触发选项变化事件方法
				thisObj.options.selectionChange(kind, influencedrowdata);
			// 根据当前选中状态重置全选复选框是否选中的状态
			}).prop("checked", function(){
				return thisObj._jqtable.find(".row_select_input:checkbox:not(:checked)").size() == 0 && thisObj._currList.length != 0;
			});

			// 添加对应选择行中数据
			function addSelectedItem(thisObj, checkbox) {
				thisObj.options.selectedItems.push(thisObj._currList[$(checkbox).attr("rownum")]);
			}

			// 从已选数据中移除对应数据
			function removeFromSelectedItems(thisObj, checkbox) {
				thisObj.options.selectedItems = $.grep( thisObj.options.selectedItems, function(item, i) {
					return thisObj._currList[$(checkbox).attr("rownum")] != item;
				});
			}

			// 添加行选择radio点击事件
			this._jqtable.find(".row_select_input:radio").click(function(e){
				checkoneline(this, thisObj);
				e.stopPropagation();
			});

			// 添加全局单元格点击事件
			var jqtabletds = this._jqtable.find("td");
			jqtabletds.click(function(e){
				var self = $(this);
				if (self.attr("columnIndex")) {
					e.column = thisObj.options.columns[self.attr("columnIndex")];
				} else {
					e.column = thisObj._headerdata[self.attr("columnKey")];
				}
				e.item = thisObj._currList[self.attr("rowIndex")];
				thisObj.options.gridClick.call(this, e);
			});
			
			// 添加双击时修改
			jqtabletds.dblclick(function(e){
				var self = $(this);
				
				var colukey = thisObj.options.columns[self.attr("columnKey")];
				if (colukey && colukey['editable']) {
					self.empty();
					// 若存在可选的自定义单元格编辑器则使用它
					var itemeditor = colukey['itemEditor'];
					if (itemeditor && colukey['itemEditor'].showWhen != 'always') {
						var opthtml = thisObj._createEditableCell(itemeditor, self.attr("rowIndex"), self.attr("columnKey"));
						$(opthtml).appendTo(this).keydown(function(e){thisObj._cellEndEdit.call(thisObj,e)})
						.focusout(function(e){thisObj._cellEndEdit.call(thisObj,e)}).focus().click(function(e){
							e.stopPropagation();
						}).dblclick(function(e){
							e.stopPropagation();
						});
					} else {
						$("<input/>").attr({rowIndex: self.attr("rowIndex"), columnKey: self.attr("columnKey")}).css({textAlign:self.css("textAlign"), width:'100%'})
						.val(thisObj._currList[self.attr("rowIndex")][self.attr("columnKey")])
						.appendTo(this).focus().select().keydown(function(e){thisObj._cellEndEdit.call(thisObj,e)})
						.focusout(function(e){thisObj._cellEndEdit.call(thisObj,e)});
					}
				}
			});

			// 表格鼠标所在位置样式
			var tbtrs = this._jqtable.find("tr:not(.grid_thead)");
			tbtrs.hover(function(){
				$(this).toggleClass("gridtrhover");
			});
			
			// 启用行点击事件相应选中模式
			if (this.options.rowClickEnabled && this.options.selectionMode != 'none') {
				tbtrs.click(function(e){
					var checkbox = $(this).find(":checkbox:first,:radio:first");
					checkbox.prop("checked",!checkbox.prop("checked"));
					checkoneline(checkbox[0], thisObj);
				});
			}

			// 若总页数只有一页, 且总记录数小于等于初始化时每页显示的记录数，则分页区隐藏
			if (this._totalpage == 1 &&  this._count <= this._originNumPerPage && !this.options.alwaysShowPageButton) {
				this._pagepos.hide();
			} else {
				this._pagepos.show();
			}

			// 生成某页序列并添加事件
			// 某页序列
			if (this.options.pagebuttonnum != 0) { // 若分页按钮为0 则直接不显示
				var numbtn = Math.min(this.options.pagebuttonnum, this._totalpage);
				var beginpage = Math.max(this._currPage - parseInt(numbtn / 2), 1);
				var endpage = Math.min(this._totalpage, beginpage + numbtn);
				beginpage = Math.max(endpage - numbtn, 1);
	
				var numpagestr = '';
				for (var i = beginpage; i < endpage; i++) {
					numpagestr += createNumpageBtn(this, i);
				}
	
				if (endpage < this._totalpage)
					numpagestr += '<span>...</span>';
				numpagestr += createNumpageBtn(this, this._totalpage);
				if (this._numpagebutton)
					this._numpagebutton.remove();
	
				this._numpagebutton = $(numpagestr);
				this._nextbutton.before(this._numpagebutton);
				this._numpagebutton.click(function(){
					if (thisObj._currPage == this.value)
						return;
					thisObj._currPage = this.value;
					gotopageFunc.call(thisObj);
				});
			}

			// 创建数字翻页按钮
			function createNumpageBtn(thisObj, intval) {
				if (thisObj._currPage == intval)
					return '<input class="num_page num_page_focus" type="button" value="' + intval + '">';
				else
					return '<input class="num_page" type="button" value="' + intval + '">';
			}
			$("tr", this.element).removeClass("evenrow");
			$("tr:even", this.element).addClass("evenrow");

			// 更新总页数和总条数
			this._pagecountinfo.find(".thepagenum").text(this._totalpage);
			this._pagecountinfo.find(".thecountnum").text(this._count);

			//加载结束状态
			if(this.options.showBusyState)
			$.hideBusyState();
			
			// 若未查询到任何数据，则简单显示一个未查询到数据的提示信息即可（抱歉，没有查询到您所需数据，建议您修改查询条件重新查询！）
			if (thisObj._count == 0)
				this._jqtable.append("<tr class='datatr'><td colspan='100'>抱歉，没有查询到您所需数据，建议您修改查询条件重新查询！</td></tr>");
			
		},
		
		refresh: function () {
			this._gotopageFunc.call(this);
		},
		
		_cellEndEdit: function(e) {
			var thisObj = this;
			var self = $(e.target);
			if (self.data('beginedit'))
				return;
			
			var rowdataitem = thisObj._currList[self.attr("rowIndex")];
			
			if (e.type == 'keydown' && e.keyCode != 13)
				return;
			
			if ($.trim(self.val()) == rowdataitem[self.attr("columnKey")]) {
				thisObj._gotoCurrPageWithRightGotoFunc();
				return;
			}
			self.data('beginedit', true);
			if (thisObj.options.cellEditUrl) {
				$.post(thisObj.options.cellEditUrl, {sid: rowdataitem.sid, dataField:self.attr("columnKey"), value:encodeURIComponent(self.val())}, function(data){
					if (data) {
						alert(data);
						setTimeout(function(){
							self.focus().select();
						}, 200)
					} else {
						thisObj._gotoCurrPageWithRightGotoFunc();
						$("body").focus();
					}
					self.data('beginedit', false);
				});
			} else {
				rowdataitem[self.attr("columnKey")] = self.val();
				thisObj._gotoCurrPageWithRightGotoFunc();
			}
		},
		
		_createEditableCell: function(itemeditor, rowIndex, columnKey, changeattr) {
			var thisObj = this;
			//  {type:'select', optionField:'opts', labelField:'label', valueField:'val'}
			if (!itemeditor['type'] || !itemeditor['optionField'] || !itemeditor['labelField'] || !itemeditor['valueField'] ) {
				alert("编辑器选项中  type optionField labelField valueField四个选项必须填写完整！")
				return;
			}
			
			var opthtml = "";
			if (itemeditor['type'] == 'select') { // 下拉列表
				
				opthtml = "<select rowIndex='" + rowIndex + "' columnKey='" + columnKey + "' style='width:auto;' " + (changeattr||'') + " >";
				var optitems = thisObj._currList[rowIndex][itemeditor['optionField']];
				var valu = thisObj._currList[rowIndex][columnKey];
				for (var i = 0; i < optitems.length; ++i) {
					if (valu == optitems[i][itemeditor['valueField']]) {
						opthtml += "<option value='" + optitems[i][itemeditor['valueField']] + "' selected>" + optitems[i][itemeditor['labelField']] + "</option>";
					} else {
						opthtml += "<option value='" + optitems[i][itemeditor['valueField']] + "'>" + optitems[i][itemeditor['labelField']] + "</option>";
					}
				}
				opthtml += "</select>";
			}
			return opthtml;
		},

		// 根据单元格对应列的样式设置样式
		_createTableCellStyle: function(colheader) {
			var styles = "";
			if ($.isPlainObject(colheader)) {
				for (var key in colheader) {
					if (this._cellstyles[key]) {
						styles += this._cellstyles[key]+":"+ colheader[key]+";"
					}
				}
				if (styles)
					styles = " style='"+styles+"' ";
			}

			return styles;
		},

		// 根据单元格具体数据格式生成单元格内部数据
		_createTableCell: function(rownum, dataitem, header, columnKey) {
			var html = dataitem;
			var val = dataitem;

			if ($.isPlainObject(dataitem)) {
				html = dataitem.html;
				val = dataitem.val;
			} else {
				if (header && header['type'] == 'checkbox') {
					return '<input rownum="'+rownum+'" dataField="'+header.dataField+'" type="checkbox" '+ (val ? "checked" : "") + '/>';
				}
			}
			
			if (header.itemEditor && header.itemEditor.showWhen == 'always') {
				var editorhtml = this._createEditableCell(header.itemEditor, rownum, columnKey, "alwaysselshow='true'");
				return editorhtml;
			}

			// 假如说有labelFunction数据标签处理函数，则使用它，别的都不用管, 否则看看有没有formatter
			if ($.isPlainObject(header)) {

				var labelFunc = header.labelFunction;
				if ($.isFunction(labelFunc)) {
					html = labelFunc.call(this.options, this._currList[rownum], header);
				} else if ($.isFunction(window[labelFunc])) {
					html = window[labelFunc].call(this.options, this._currList[rownum], header);
				} else if ($.isFunction(header.formatter)) {
					html = header.formatter(val);
				} else if (header.enumXml) { // 若存在枚举xml则直接执行枚举格式化器
					html = typicalEnumFormatter.call(header, val);
				} else if (header.enumXmlField && this._currList[rownum][header.enumXmlField]) { // 若设置了enumXmlField则数据中某列标明了使用哪个特别的枚举值
					html = typicalEnumFormatter.call({enumXml:this._currList[rownum][header.enumXmlField]}, val);
				}
				
				if (html == null)
					html = '';
			}
			
			return html;
		},

		// 重置行选中状态样式
		_resetSelectedClass:function () {
			this._jqtable.find(".row_select_input:checked").closest("tr").addClass("gridselected");
			this._jqtable.find(".row_select_input:not(:checked)").closest("tr").removeClass("gridselected");
		},

		_createTableByArray:function (arr) {

			// 准备表头数据
			if (this.options.columns) {
				this._setHeaderDataByColumns();
			} else {
				this._headerdata = arr[0];
			}

			// 准备所有列表数据
			if (!this.options.columns) {
				this._allList = arr.splice(0,1);
			}

			this._allList = arr;


			// 创建表格基础（表格，排序，翻页框架）
			this._createTableBasic(this._arrgotocurrPage, this._sortWhenDataProviderIsArray);
		},

		// 数组全数据时排序方式
		_sortWhenDataProviderIsArray:function() {
			var thisObj = this;
			this._allList.sort(function(a, b) {
				var aval = a[thisObj._colkey];
				var bval = b[thisObj._colkey];
				if ($.isPlainObject(aval))
					aval = aval.val;

				if ($.isPlainObject(bval))
					bval = bval.val;

				if (aval == null) aval = '';
				if (bval == null) bval = '';
				if (typeof aval === 'number') {
					return thisObj._isup ? parseFloat(aval) - parseFloat(bval):parseFloat(bval) - parseFloat(aval);
				} else {
					return thisObj._isup ? aval.localeCompare(bval):bval.localeCompare(aval);
				}
			});
		},

		// 创建表头可扩展样式集合
		_createHeaderStyles: function(hdata) {
			var stylestr = "white-space:nowrap;";
			for (var key in hdata) {
				if ($.inArray(key, this._headerstyles) != -1) {
					stylestr+=(key+":"+hdata[key]+";");
				}
			}
			return stylestr;
		},

		// 创建表格基本结构
		_createTableBasic: function(gotopageFunc, sortFunc) {
			var thisObj = this;

			var table = "<table class='datagrid'>";
			// 安装表头
			var headerth = "<tr class='grid_thead'>";

			// 表头隐藏
			if (this.options.visibleHeader == false) {
				headerth = "<tr style='display:none'>";
			}

			// 添加对行选择的支持
			// 单行选择
			if (this.options.selectionMode === 'singleRow') {
				headerth += "<th class='single_rowclass' style='white-space:nowrap;width:3%;'>" + $.i18n('antelope.singleselect') + "</th>";
			}
			// 多行选择
			if (this.options.selectionMode === 'multipleRows') {
				headerth += "<th style='width:3%;'><input class='select_all_row' type='checkbox'/></th>";
			}
			// 显示序号
			if (this.options.showSeqNum) {
				headerth += "<th style='width:3%;white-space:nowrap;'>" + $.i18n('antelope.ordernum') + "</th>";
			}



			// 添加对表样式属性等的支持
			if ($.isArray(this._headerdata)) {
				$(this._headerdata).each(function(){
					var headstyle = thisObj._createHeaderStyles(this);
					if (headstyle)
						headstyle = "style='"+headstyle+"'";
					// 此处，当某列标有类型，如checkbox或明确标明不允许按列排序时，不添加span不进行排序
					var spanstr = "<span></span>";
					if (this.type || !thisObj.options.sortableColumns && (typeof this.sortable == 'undefined' || this.sortable)) {
						spanstr = "";
					}

					// 添加排序字段,若存在排序字段则使用该字段排序
					if (this.sortField) {
						headerth += "<th sortField='" + this.sortField + "' "+headstyle+" key='"+this.dataField+"'>"+(this.headerText||'') + spanstr + "</th>";
					} else {
						headerth += "<th "+headstyle+" key='"+this.dataField+"'>"+(this.headerText||'') + spanstr + "</th>";
					}
					
				})
			} else {
				for (var obj in this._headerdata) {
					var headerTxt = this._headerdata[obj];
					var headstyle = "white-space:nowrap;";
					// 表头对应配置为对象
					if ($.isPlainObject(headerTxt)) {
						headerTxt = headerTxt.headerText;
						headstyle = thisObj._createHeaderStyles(this._headerdata[obj]);
					}
					
					var sortf = "";
					if (this._headerdata[obj].sortField) {
						sortf = " sortField='" + this._headerdata[obj].sortField + "' "; 
					}

					if (this.options.sortableColumns && (typeof this._headerdata[obj].sortable == 'undefined' || this._headerdata[obj].sortable))
						headerth += "<th key='"+obj+"' " + sortf + " style='"+headstyle+"'>"+(headerTxt||'')+"<span></span></th>";
					// 若明确指出不允许排序则不排序
					else
						headerth += "<th key='"+obj+"' " + sortf + "  style='"+headstyle+"'>"+(headerTxt||'')+"</th>";
				}
			}

			// 若有操作按钮则添加操作列
			if (this.options.buttons) {
				headerth += "<th style='width:"+this.options.operationHeaderWidth+";'>"+this.options.operationHeaderText+"</th>";
			}

			headerth += "</tr>";
			table += headerth;
			table += "</table>";
			this._jqtable = $(table);
			this.element.append(this._jqtable);

			// 分页区域
			// 使用小翻页区域时
			if (this.options.usingTinyPageArea) {
				this._pagepos = $('<div class="pagepos" style="display:none; float:none; width:auto; text-align:center;"></div>');
			} else {
				this._pagepos = $('<div class="pagepos" style="display:none;"></div>');
			}

			this.element.append(this._pagepos);
			// 一系列表格页
			
			// 使用小翻页区域
			if (this.options.usingTinyPageArea) {
				this._firstbutton=$('<a href="javascript:void(0)">' + $.i18n("antelope.firstpage") + '</a>');
				this._prebutton=$('<a href="javascript:void(0)">' + $.i18n('antelope.prep') + '</a>');
				this._nextbutton=$('<a href="javascript:void(0)">' + $.i18n('antelope.nextp') + '</a>');
				this._lastbutton=$('<a href="javascript:void(0)">' + $.i18n('antelope.lastpage') + '</a>');
			} else {
				this._firstbutton=$('<input class="first_page" type="button" value="' + $.i18n("antelope.firstpage") + '"/>');
				this._prebutton=$('<input class="prev_page" type="button" value="' + $.i18n('antelope.prepage') + '"/>');
				this._nextbutton=$('<input class="next_page" type="button" value="' + $.i18n('antelope.nextpage') + '"/>');
				this._lastbutton=$('<input class="last_page" type="button" value="' + $.i18n('antelope.lastpage') + '"/>');
			}
			
			this._currpagefield=$('<input class="currpage" value="1">');
			this._gopagebutton=$('<input class="gopage" type="button" value="' + $.i18n('antelope.ok') + '">');
			
			// 每页显示条目数量可选项
			var canperpages = [10, 20, 40, 100];
			var selecthtml = "<select>";
			var perpageprefix = $.i18n('antelope.rowperpageprefix');
			var perpagesuffix = $.i18n('antelope.rowperpagesuffix');
			for (var i = 0; i < canperpages.length; ++i) {
				selecthtml += '<option value="' + canperpages[i] + '">'+ perpageprefix + canperpages[i] + " " + perpagesuffix + '</option>';
			}
			selecthtml += "</select>";
			
			this._numcurrpagelist=$(selecthtml);
			
			this._numcurrpagelist.val(this.options.numPerPage);

			this._pagecountinfo = $('<span>' + $.i18n('antelope.totalpageprefix') + '<span class="thepagenum"></span>' + $.i18n('antelope.totalpagesuffix') + 
					' <span class="thecountnum"></span>' + $.i18n('antelope.row') + '</span>');

			this._pagepos.append(this._firstbutton);
			this._pagepos.append(this._prebutton);
			this._pagepos.append(this._numpagebutton);
			this._pagepos.append(this._nextbutton);
			this._pagepos.append(this._lastbutton);
			this._pagepos.append(this._currpagefield);
			this._pagepos.append(this._gopagebutton);
			this._pagepos.append(this._pagecountinfo);
			this._pagepos.append(this._numcurrpagelist);
			
			if (this.options.usingTinyPageArea) {
				this._currpagespan = $("<span class='currpagespan'>" + this._currpagefield.val() + "</span>");
				this._currpagefield.after(this._currpagespan).hide();
				this._gopagebutton.hide();
				this._pagecountinfo.hide();
				this._numcurrpagelist.hide();
			}
			
			if (!this.options.showNumperpage) {
				this._numcurrpagelist.hide();
			}
			
			var thisObj = this;

			thisObj._gotopageFunc = gotopageFunc;
			// 首页
			this._firstbutton.click(function(){
				if (thisObj._currPage == 1)
					return;
				thisObj._currPage = 1;
				gotopageFunc.call(thisObj);
			});

			// 上一页
			this._prebutton.click(function(){
				if (thisObj._currPage == thisObj._pre)
					return;
				thisObj._currPage = thisObj._pre;
				gotopageFunc.call(thisObj);
			});

			// 下一页
			this._nextbutton.click(function(){
				if (thisObj._currPage == thisObj._next)
					return;
				thisObj._currPage = thisObj._next;
				gotopageFunc.call(thisObj);
			});

			// 尾页
			this._lastbutton.click(function(){
				if (thisObj._currPage == thisObj._totalpage)
					return;
				thisObj._currPage = thisObj._totalpage;
				gotopageFunc.call(thisObj);
			});

			// 转到
			this._gopagebutton.click(function(){
				if (thisObj._currPage == thisObj._currpagefield.val())
					return;
				thisObj._currPage = thisObj._currpagefield.val();
				gotopageFunc.call(thisObj);
			});
			
			// 每页显示条数
			this._numcurrpagelist.change(function(){
				thisObj.options.numPerPage = parseInt(this.value);
				gotopageFunc.call(thisObj);
			});

			// 表头排序(只取含有span用来显示排序列状态的表头)
			var thisObj = this;
			this._jqtable.find("th:has(span)").css("cursor", "pointer").click(function(){
				var thisObj2 = this;
				var index = 0;
				//datatrselector, numcolname
				$("th", thisObj._jqtable).each (function(i) {
					if (this == thisObj2)
						index = i;
				});

				var isup = false;

				if (thisObj._sortth == thisObj2)
					isup = !thisObj._isup;

				$(this).parent().find("span").removeClass();
				$(this).find("span").addClass(isup?"sortup":"sortdown");

				thisObj._sortth = thisObj2;
				thisObj._isup = isup;
				var sortField = $(thisObj2).attr("sortField");
				if (sortField) { // 有明确指明了排序字段则使用那个字段进行排序
					thisObj._colkey = sortField;
				} else {
					thisObj._colkey = $(thisObj2).attr("key");
				}
				sortFunc.call(thisObj);

				gotopageFunc.call(thisObj);
			});

			// 表格创建完成之后默认进入第一页
			gotopageFunc.call(thisObj);


		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		},
		
		getCurrList: function() {
			return this._currList;
		},
		
		// 删除sid对应行
		removeRow: function(rowSid) {
			if ($.isArray(this.options.dataProvider )) {
				var data = this.options.dataProvider;
				for (var i = 0; i < data.length; ++i) {
					if (data[i].sid == rowSid	) {
						data.splice(i, 1);
						break;
					}
				}
				this._gotoCurrPageWithRightGotoFunc();
			} else {
				alert("此方法仅适用于给dataProvider直接传值,前台js分页的情况！");
			}
		},

		getDataForExport: function() {
			var exportdata = [];
			var headerarr = [];
			for(var key in this._headerdata) {
				if ($.isArray(this._headerdata)) {
					headerarr.push({
						headerText: this._headerdata[key].headerText, field: this._headerdata[key].dataField
					});
				} else {
					if ($.isPlainObject(this._headerdata[key])) {
						headerarr.push({
							headerText: this._headerdata[key].headerText, field: key
						});
					} else {
						headerarr.push({
							headerText: this._headerdata[key], field: key
						});
					}
				}

			}
			exportdata.push(headerarr);
			for(var i = 0; i < this._allList.length; i++) {
				exportdata.push(this._allList[i]);
			}
			return exportdata;
		}
	});
})( jQuery );

function getMessage(key) {

}

function beginlist(trclass){
	$(trclass).remove();
	return "";
}


//cookie插件
/**
 * Create a cookie with the given name and value and other optional parameters.
 *
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Set the value of a cookie.
 * @example $.cookie('the_cookie', 'the_value', { expires: 7, path: '/', domain: 'jquery.com', secure: true });
 * @desc Create a cookie with all available options.
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Create a session cookie.
 * @example $.cookie('the_cookie', null);
 * @desc Delete a cookie by passing null as value. Keep in mind that you have to use the same path and domain
 *       used when the cookie was set.
 *
 * @param String name The name of the cookie.
 * @param String value The value of the cookie.
 * @param Object options An object literal containing key/value pairs to provide optional cookie attributes.
 * @option Number|Date expires Either an integer specifying the expiration date from now on in days or a Date object.
 *                             If a negative value is specified (e.g. a date in the past), the cookie will be deleted.
 *                             If set to null or omitted, the cookie will be a session cookie and will not be retained
 *                             when the the browser exits.
 * @option String path The value of the path atribute of the cookie (default: path of page that created the cookie).
 * @option String domain The value of the domain attribute of the cookie (default: domain of page that created the cookie).
 * @option Boolean secure If true, the secure attribute of the cookie will be set and the cookie transmission will
 *                        require a secure protocol (like HTTPS).
 * @type undefined
 *
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */
jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        // CAUTION: Needed to parenthesize options.path and options.domain
        // in the following expressions, otherwise they evaluate to undefined
        // in the packed version for some reason...
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};




function creatAjaxJSON(url, postValue, callbackFun) {
	var obj = CreatXmlRequest();
	obj.open('POST', url);
	obj.setRequestHeader("If-Modified-Since", "0");
	obj.setRequestHeader("Content-Length",postValue.length);  //设置发送内容长度
	obj.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");//设置发送内容类型
	obj.send(postValue);
	obj.onreadystatechange = function() {
		if (obj.readyState == 4 && obj.status == 200) {
			callbackFun(JSON.parse(obj.responseText));
		}
	}
}

function creatAjaxPost(url, postValue, callbackFun) {
	var obj = CreatXmlRequest();
	obj.open('POST', url);
	obj.setRequestHeader("If-Modified-Since", "0");
	obj.setRequestHeader("Content-Length",postValue.length);  //设置发送内容长度
	obj.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");//设置发送内容类型
	obj.send(postValue);
	obj.onreadystatechange = function() {
		if (obj.readyState == 4 && obj.status == 200) {
			callbackFun(obj.responseText);
		}
	}
}

var ajaxobj = null;
function CreatXmlRequest() {
	if (ajaxobj == null) {
		try {
			ajaxobj = new XMLHttpRequest();
		} catch (e) {
			try {
				ajaxobj = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				ajaxobj = new ActiveXObject("Microsoft.XMLHTTP");
			}
		}
	}
	return ajaxobj;
}



/**
 * 格式化数值并以千分位形式展现
 * @param value 数值
 * @param point 保留的小数位 默认2位
 */
jQuery.valueFormat = function (value, point) {
	value = jQuery.numberFormat(value);
	value = /-?[\d]*\.?[\d]*/.exec(value);
	if (value != 0) {
		if (point == null) {
			point = 2;
		}
		value = new Number(value).toFixed(point);
	}
	var re = /(\d{1,3})(?=(\d{3})+(?:$|\.))/g;
	return value.toString().replace(re, "$1,");
};

/**
 * 格式化数值中的非法字符去掉
 * @param value 数值
 */
jQuery.numberFormat = function (value) {
	value = /-?[\d]*\.?[\d]*/.exec(value);

	if (value.length == 0) {
		value = 0;
	}
	return value[0];
}


// 启用/禁用按钮
$.fn.extend({
	disable: function() {
		this.css ("opacity", 0.5).prop ("disabled", true).css("cursor","default").mouseleave();
		return this;
	},
	enable: function(isenable) {
		if (typeof isenable != 'undefined' && isenable == false) {
			this.disable();
		} else {
			this.css ("opacity", 1).prop ("disabled", false).css("cursor","pointer").mouseleave();
		}
		return this;
	}
	
});

// 选择上级领导
$.extend({
	selectParentUser: function(origOptions) {
		var options = {
				username:"",
				parentUserTextInput:"", 
				parentUsernameHiddenInput:"",
				defaultParentUsername:null
			};

		var opts = jQuery.extend (true, {}, options, origOptions);
		
		// 若存在默认的上级领导则直接使用
		
		var parentuserGetUrl = ctxPath + "/common/UserRoleOrgController/getParentUserByUsername.vot?username=" + opts.username;
		
		if (opts.defaultParentUsername) {
			parentuserGetUrl = ctxPath + "/common/UserRoleOrgController/getUserByUsername.vot?username=" + opts.defaultParentUsername; 
		}
		// 获取上级领导
		$.getJSON(parentuserGetUrl, function(parentuser){
			if (parentuser) {
				$(opts.parentUserTextInput).val(parentuser.name);
				$(opts.parentUsernameHiddenInput).val(parentuser.username);
			}
			$(opts.parentUserTextInput).parent().find(".selectparentuserbtn").remove();
			$("<input class='smallbutton selectparentuserbtn' style='width:60px;' value='选择' type='button'/>").click(function() {
				$.selectUsers({
					callback:function(users) {
						if (users.length) {
							$(opts.parentUserTextInput).val(users[0].name);
							$(opts.parentUsernameHiddenInput).val(users[0].username);
						}
					},
					isUsername:true,
					issingle:true
				});
			}).insertAfter(opts.parentUserTextInput);
		});
	}
});

// 表单验证相关插件
$.extend({
	/**
	 * 验证是否成功，可以传回调函数
	 * @return 是否继续执行默认的错误提示方式。true则执行，false则不执行
	 */
	issuccess : function(data, callback) {
		if (typeof data === 'string') {
			try {
				data = window["eval"]("(" + data + ")");
			} catch (e) {
				return true;
			}
		}

		if (!data)
			return true;

		if (data.fieldInvalidmsg && data.fieldInvalidmsg.length > 0) {
			if (typeof callback === 'function') {
				if (callback (data.fieldInvalidmsg))
					alert (data.fieldInvalidmsg[0].title + data.fieldInvalidmsg[0].invalidMsg);
			} else
				alert (data.fieldInvalidmsg[0].title + data.fieldInvalidmsg[0].invalidMsg);
			return false;
		}

		return true;
	},

	/**
	 * 用法和$.post相同
	 */
	postIframe : function (url, params, callback) {
		/**创建表单 begin->*/
		var frm = $("<form method='post' action='" + url + "' target='tar'></form>");
		//$(frm).attr ({method:'post', action:url, target:'tar'});
		if (typeof params === 'function') {
			/*<-创建表单 end*/
			this.createIframe(params);
		} else {
			if (params) {
				if( typeof params === "object") {
					for(var thekey in params) {
						$("<input name='"+thekey+"' type='hidden'/>")
						.val(params[thekey]).appendTo(frm);
					}
				} else {
					params = params.split("&");
					for (var i = 0; i < params.length; i++)
						$("<input name='"+params[i].split("=")[0]+"' type='hidden'/>")
						.val(params[i].split("=")[1]).appendTo(frm);
				}
			}

			/*<-创建表单 end*/
			this.createIframe(callback);
		}
		
		frm.appendTo (document.body);
		frm.submit();
	},
	/**
	 * 创建Iframe，如果已经存在则不创建。创建完之后添加完成事件
	 */
	createIframe : function (callback, iframpos, targetName) {
		/**创建iframe begin->*/
		var ifram = $("iframe[name=tar]");
		if (iframpos) { // 若需要将iframe挂在某个位置则去寻找这个位置的iframe是否存在
			ifram = $("iframe[name="+targetName+"]", iframpos);
		}

		// 不存在则需要创建
		if (ifram.length == 0) {
			if (iframpos) {
				ifram = $("<iframe style='width:100%; height:100%;' frameborder='0' name='"+targetName+"'></iframe>").appendTo(iframpos);
			} else {
				ifram = $("<iframe style='display: none;' name='tar'></iframe>").appendTo(document.body);
			}
		}
		if (typeof callback === 'function') {
			ifram[0].callback = callback;
			
			if (!$.browser.msie) {
				function callbackouter(){
					ifram[0].removeEventListener("load", callbackouter);
					ifram[0].callback(ifram[0].contentWindow.document.body.innerHTML);
				}
				ifram[0].addEventListener("load", callbackouter);
			} else {
				ifram[0].onreadystatechange = function() {
					var iframeobj = this;
					
					if (this.readyState == 'complete') {
						if (!this.callback.executed)
							this.callback(this.contentWindow.document.body.innerHTML);
						this.callback.executed = true;
					}
					if ( this.readyState == 'interactive' ) {
						if (!this.callback.executed) {
							if (this.contentWindow.document.body) {
								this.callback(this.contentWindow.document.body.innerHTML);
							} else {
								this.callback();
							} 
						}
						this.callback.executed = true;
					}
				};
			}
			
		}
		/*<-创建iframe end*/
	},

	// 等待条开始结束
	startWait : function (commonImgDir) {
		if ($("#divwaiterimg").size()==0) {
			$("body").prepend(
				"<div id=divwaiterimg align='center' style='position:absolute; width:100%; height:100%; z-index: 11;'>"+
				"	<table style='width: 100%; height: 100%;'>"+
				"		<tr style='width: 100%; height: 10)%;'>"+
				"			<td style='width: 100%; height: 100%; text-align: center; vertical-align: middle;'><img src='"+commonImgDir+"/waiter.gif'></td>"+
				"		</tr>"+
				"	</table>"+
				"</div>"+
				"<div id=divwaiterdiv style='width: 2000px;height: 2000px;position: absolute;background-color: gray; filter:alpha(opacity=10);z-index:11;'></div>"
			);
		}
	},

	stopWait : function() {
		$("#divwaiterimg,#divwaiterdiv").remove();
	},

	noNull : function (data) {
		if (typeof data == 'undefined' || data == null)
			return '';
		else
			return data;
	},

	tidyRichText: function (str) {
		if (str == null)
			str = '';
		str = str.replace(/(..\/)*upload\/UploadController/ig, ctxPath + '/upload\/UploadController');
		return str;
	},
	
	tidyTextToHtml: function(str) {
		str = str.replace(/\n/g, "<br/>");
		return str;
	},
	
	open: function(origOptions) {
		var options = {
				url:null
			};

		var opts = jQuery.extend (true, {}, options, origOptions);

		window.open(opts.url);
	},
	
	// 禁止session过期方法,定期循环访问后台
	noSessionTimeout: function() {
		setInterval(function(){
			$.get(ctxPath + "/common/SystemParamsController/noSessionTimeout.vot");
		}, 10000);
	}

});
$.fn.extend({

	postIframe : function (url, callback) {
		if (this.length == 0) {
			alert ("未找到表单");
			return;
		}

		for (var i = 0; i < this.length; i++) {
			/**创建表单 begin->*/
			if (this[i].nodeName != 'FORM') {
				alert ("找到了不是表单的节点");
				continue;
			}
			var frm = this[i];
			$(frm).attr ({method:'post', action:url, target:'tar'});
			/*<-创建表单 end*/
		}
		jQuery.createIframe(callback);
		frm.submit();
		return this;
	},

	// iframe方式加载页面
	loadIframe: function(url, params, callback) {
		this.empty();
		var targetName = $.Guid.New().replace(/-/g,'');
		/**创建表单 begin->*/
		var frm = document.createElement("form");
		$(frm).attr ({method:'post', action:url, target:targetName});
		if (typeof params === 'function') {

			/*<-创建表单 end*/
			$.createIframe(callback, this, targetName);
		} else {
			if (params) {
				if( typeof params === "object") {
					for (var key in params) {
						if (!$.isFunction(params[key]))
							$("<input name='"+key+"' type='hidden'/>").val(params[key]).appendTo(frm);
					}
				} else {
					params = params.split("&");
					for (var i = 0; i < params.length; i++)
						$("<input name='"+params[i].split("=")[0]+"' value='"+params[i].split("=")[1]+"' type='hidden'/>").appendTo(frm);
				}
			}

			/*<-创建表单 end*/
			$.createIframe(callback, this, targetName);
		}
		$(frm).appendTo (document.body);
		frm.submit();

		// 删除此表单节点
		frm.removeNode(true);

		return this;
	},

	dialogIframe: function() {
		this.dialog.apply(this, arguments).bind('dialogdragstart dialogresizestart', function() {
	        var overlay = $(this).find('.hidden-dialog-overlay');
	        if (!overlay.length) {
	            overlay = $('<div class="hidden-dialog-overlay" style="position:absolute;top:0;left:0;right:0;bottom:0;z-index:100000;background:white;"></div>');
	            overlay.css("opacity", 0.1);
	            overlay.appendTo(this);
	        }
	        else
	            overlay.show();
	    }).bind('dialogdragstop dialogresizestop', function() {
	        $(this).find('.hidden-dialog-overlay').hide();
	    });

		return this;
	}

});

$.extend({
	/**
	 * 在页面最上层windows窗体中显示对话框
	 * 当确认对话框已经显示之后会自动调用对话框加载的jsp内部的 dialogTopIframeShowed方法，如不存在则不调用
	 */
	dialogTopIframe: function(origOptions) {
		var options = {
			url:null,
			modal:true,
			 show: {
			       effect: "drop",
			       duration: 200,
			       direction:'up'
			 },
			 hide: {
				    effect: "drop",
				    duration: 200,
				    direction:'up'
			 },
			width:top.$(top.window).width() - 50,
			height:top.$(top.window).height() - 50,
			close: jQuery.noop, // 对话框关闭时触发
			data:null,
			loadComplete: $.noop
		}
		var opts = jQuery.extend (true, {}, options, origOptions);
		var iframename = 'usedtotopiframe' + Math.round(Math.random() * 1000);
		if (top.$(".ifrtotopdialog:hidden").size() && top.$(".ifrtotopdialog:hidden").parent().data("uiDialog")) {
			top.$(".ifrtotopdialog:hidden").parent().dialog("destroy");
			top.$(".ifrtotopdialog:hidden").parent().data("uiDialog", null);
		}
		var ifram = top.$("<div style='display:none;padding:0;margin:0;border:0;overflow:hidden;'><iframe class='ifrtotopdialog' name='" + iframename + "' style='width:100%; height:99%;margin:0;padding:0;border:0;' frameborder='0'></iframe></div>")
		.appendTo(top.$("body"));
		function iframeloadedFunction(thisiframe) {
			
			var isvisible = top.$(ifram).is(":visible");
			if (!isvisible) {
				top.$(ifram).dialog(opts).bind('dialogdragstart dialogresizestart', function() {
			        var overlay = $(this).find('.hidden-dialog-overlay');
			        if (!overlay.length) {
			            overlay = top.$('<div class="hidden-dialog-overlay" style="position:absolute;top:0;left:0;right:0;bottom:0;z-order:100000;background:white;"></div>');
			            overlay.css("opacity", 0.1);
			            overlay.appendTo(this);
			        }
			        else
			            overlay.show();
			    }).bind('dialogdragstop dialogresizestop', function() {
			    	top.$(this).find('.hidden-dialog-overlay').hide();
			    });
				if (top.$(ifram).find("iframe")[0].contentWindow['dialogTopIframeShowed']) {
					top.$(ifram).find("iframe")[0].contentWindow['dialogTopIframeShowed']();
				}
			}
			// Chrome时iframe的onload事件会在显示之前触发一次，显示之后再触发一次
			if (!$.browser.msie) {
				if (isvisible) { // 正常显示之后再调用
					opts.loadComplete(thisiframe);
					ifram.find("iframe")[0].onload = null;
				} else {// 若没有正常显示，那么若有表单数据，则需要重新做一下表单提交
					if (opts.data != null) {
						/**创建表单 begin->*/
						var frm = document.createElement("form");
						$(frm).attr ({method:'post', action:opts.url, target:iframename});
						if( typeof opts.data === "object") {
							opts.data = $.param( opts.data );
						}
						var params = opts.data.split("&");
						for (var i = 0; i < params.length; i++)
							$("<input name='"+params[i].split("=")[0]+"' value='"+params[i].split("=")[1]+"' type='hidden'/>").appendTo(frm);
						$(frm).appendTo (document.body);
						frm.submit();
						// 删除此表单节点
						if (frm.removeNode) {
							frm.removeNode(true);
						} else {
							$(frm).remove();
						}
					}
				}
			} else {
				opts.loadComplete(thisiframe);
			}
			$.hideBusyState();
		}
		if (!$.browser.msie) {
			
			function callloadedFunction(ifram, callback, url) {
				
				// 若不再同一访问域，那么直接回调即可。
				if ((/http:\/\/[^\/]*/.exec(url) + "") != (/http:\/\/[^\/:]*/.exec(location.href) + "")) {
					callback(ifram.contentWindow);	
				} else {
					if (ifram.contentWindow.$ && ifram.contentWindow.$("body").length) {
						callback(ifram.contentWindow);
					} else {
						setTimeout(function() {
							callloadedFunction(ifram, callback);
						}, 500);
					}
				}
				
			}
			
			ifram.find("iframe")[0].onload = function() {
				callloadedFunction(this, iframeloadedFunction, opts.url);
			};
			
		} else {
			ifram.find("iframe")[0].onreadystatechange = function() {
				if (this.readyState == 'complete') {
					iframeloadedFunction(this.contentWindow);
					this.onreadystatechange = null;
				}
			};
		}
		
		
		if (opts.data == null) {
			ifram.find("iframe")[0].src = opts.url;
		} else {
			/**创建表单 begin->*/
			var frm = document.createElement("form");
			$(frm).attr ({method:'post', action:opts.url, target:iframename});
			if( typeof opts.data === "object") {
				opts.data = $.param( opts.data );
			}
			var params = opts.data.split("&");
			for (var i = 0; i < params.length; i++)
				$("<input name='"+params[i].split("=")[0]+"' value='"+params[i].split("=")[1]+"' type='hidden'/>").appendTo(frm);
			$(frm).appendTo (document.body);
			frm.submit();
			// 删除此表单节点
			if (frm.removeNode) {
				frm.removeNode(true);
			} else {
				$(frm).remove();
			}
		}
		$.showBusyState();
		return ifram;
	},

	closeDialogTopIframe: function() {
		
		// 当父窗口已经标识了提交直接关闭窗口则直接关闭窗口即可(OA)
		if (location.href.indexOf("closewindowaftersubmit")!=-1) {
			top.close();
			return true;
		}
		var ifrms = top.$("iframe");
		if (ifrms && ifrms.length) {
			for (var i = 0; i < ifrms.length; i++) {
				if (ifrms[i].contentWindow.document === window.document && top.$(ifrms[i]).is(".ifrtotopdialog")) {
					top.$(ifrms[i]).parent().dialog("close");
					return true;
				}
			}
		}
		return false;
	},

	/**
	 * 基于发布订阅的信息接口ajax端
	 */
	subscribeRoutedMessages:function (origOptions) {
		var options = {
			// 信息目标ID
			dest:null,
			// 在ajax执行之前执行，若返回为false则表明本次不执行ajax访问操作
			beforeAjax:jQuery.noop,
			// 在ajax执行之后执行，处理ajax返回的数据，若无数据返回则不执行
			afterAjax:jQuery.noop,
			// 轮询时间 毫秒计算 默认5000毫秒
			pollingInterval:5000
		}
		var opts = jQuery.extend (true, {}, options, origOptions);

		if (!opts.dest) {
			alert("订阅异步信息目标ID必须设置！");
			return;
		}
		setInterval(function(){
			if (opts.beforeAjax() === true) {
				$.getJSON(ctxPath + "/getRoutedMessage.vot?dest="+opts.dest, function(data) {
					if (data) {
						opts.afterAjax(data);
					}
				});
			}
		}, opts.pollingInterval);
	},

	// 显示系统忙碌状态
	showBusyState:function(origOptions) {
		var options = {
			top:null
		}
		
		var opts = jQuery.extend (true, {}, options, origOptions);
		$("body").prepend($("<div class='busidiv'></div>").css("opacity", 0.1).css("top", $("body").attr("scrollTop")));
		if (opts.top) {
			$("body").prepend($("<span class='busispan'></span>").css("left",$("body").width()/2-50).css("top", opts.top));
		} else {
			$("body").prepend($("<span class='busispan'></span>").css("left",$("body").width()/2-50).css("top", $("body").height() / 2 - 50 + $("body").attr("scrollTop")));
		}
		return this;
	},

	// 隐藏系统忙碌状态
	hideBusyState:function() {
		$(".busidiv,.busispan").remove();
		return this;
	},

	// 选择用户
	// modified by xcc
	//opt-->filterdept:false 根据登录用户所属部门过滤
	//opt-->deptheader:false 向上找部门负责人，如果没有，则整个组织机构人员
	selectUsers: function(origOptions) {
		var options = {
			callback:jQuery.noop,
			selectedusers:[],
			isUsername:true,
			issingle:false,
			filterrole:"",
			filterdept:false,
			deptheader:false
		}
		var opts = jQuery.extend (true, {}, options, origOptions);

		var seledusers = [];

		isUsername = opts.isUsername||'';

		if (opts.selectedusers == null)
			selectedusers = [];

		var iframdiag = null;
		if ($("#seluseriframe").length) {
			iframdiag = $("#seluseriframe").parent();
		} else {
			iframdiag = $("<div style='width:100%;height:100%;display:none;overflow:auto;'><iframe name='seluseriframe' id='seluseriframe' frameborder=0 style='padding:0; margin:0; width:100%;height:100%;'></iframe></div>").appendTo("body");
		}

		iframdiag.dialogIframe({
			width:850, height:500, title:'选择用户', modal:true,
			buttons:{
				'确定':function() {
					seledusers = seluseriframe.getSelectedUser();
					if ($.isFunction(opts.callback)) {
						opts.callback(seledusers);
					}
					iframdiag.dialog("destroy");
				}, '取消': function() {
					iframdiag.dialog("destroy");
				}
			}
		});
		
		//修改支持部门负责选择(modify by huanggc)
		if(opts.deptheader){
			$("<form action='"+ctxPath+"/components/usermanagerselect.jsp?isusername="+opts.isUsername+"&issingle="+opts.issingle+"&deptheader="+opts.deptheader+"&filterrole="+encodeURIComponent(encodeURIComponent(opts.filterrole))+"&filterdept="+encodeURIComponent(encodeURIComponent(opts.filterdept))+"' target='seluseriframe' method='post'><input name='users' type='hidden' value='"+encodeURIComponent(JSON.stringify(opts.selectedusers))+"'/></form>").appendTo("body")[0].submit();
		}else{
			$("<form action='"+ctxPath+"/components/userselect.jsp?isusername="+opts.isUsername+"&issingle="+opts.issingle+"&deptheader="+opts.deptheader+"&filterrole="+encodeURIComponent(encodeURIComponent(opts.filterrole))+"&filterdept="+encodeURIComponent(encodeURIComponent(opts.filterdept))+"' target='seluseriframe' method='post'><input name='users' type='hidden' value='"+encodeURIComponent(JSON.stringify(opts.selectedusers))+"'/></form>").appendTo("body")[0].submit();
		}
	},

	// 选择用户, 若通过角色找不到对应人员，则从全体人员中选取
	selectUserByRolesid: function(origOptions) {
		var options = {
			rolesid:null, // 必填，根据角色sid选择人员
			isUsername:true,
			unitsid:"",
			selecteduser:null,
			includeSubunits: true,
			callback:jQuery.noop
		}
		var opts = jQuery.extend (true, {}, options, origOptions);

		if (opts.rolesid == null) {
			alert("根据用户角色选择用户组件，必须提供角色id!");
			return;
		}

		$.getJSON(ctxPath + "/common/getUserByRolesid.vot?rolesid="+opts.rolesid, function(data){
			if ($("#seluseriframebyrole").length) {
				iframdiag = $("#seluseriframebyrole").parent();
			} else {
				iframdiag = $("<div style='width:100%;height:100%;display:none;overflow:auto;'><iframe name='seluseriframebyrole' id='seluseriframebyrole' frameborder=0 style='padding:0; margin:0; width:100%;height:100%;'></iframe></div>").appendTo("body");
			}
			$.getJSON(ctxPath + "/common/getUsersByRolesidAndUnitsid.vot?rolesid=" + opts.rolesid+"&unitsid=" + opts.unitsid, function(userdatas){
				if(userdatas.length) { // 若找到人员则从中选择
					iframdiag.dialogIframe({
						width:300, height:$(window).height() - 70, title:'选择' + (data ? data.name :'') + '用户', modal:true,
						buttons:{
							'确定':function() {
								
								if ($.isFunction(opts.callback)) {
									if (seluseriframebyrole.getSelectedUser() == null)
										return;
									opts.callback(seluseriframebyrole.getSelectedUser());
								}
								iframdiag.dialog("destroy");
							}, '取消': function() {
								iframdiag.dialog("destroy");
							}
						}
					});
					$("<form action='"+ctxPath+"/components/userselectbyrole.jsp?isusername="+opts.isUsername+"&rolesid="+opts.rolesid+"&unitsid="+encodeURIComponent(encodeURIComponent(opts.unitsid))+
							"&includeSubunits=" + opts.includeSubunits + "' target='seluseriframebyrole' method='post'></form>").appendTo("body")[0].submit();
				} else { // 否则从全员选择
					var selectuserss = [];
					if (opts.selecteduser)
						selectuserss.push(opts.selecteduser);
					
					$.selectUsers({
						isUsername:opts.isUsername,
						selectedusers:selectuserss,
						issingle: true,
						callback: function(selectusers){
							if (selectusers.length)
								opts.callback(selectusers[0]);
							else
								opts.callback(null);
						}
					});
				}
			
			});

		});

	},

	selectUnit: function (callback) {
		var iframdiag = null;
		if ($("#selunitiframe").length) {
			iframdiag = $("#selunitiframe").parent();
		} else {
			iframdiag = $("<div style='width:100%;height:100%;display:none;overflow:auto;'><iframe src='" + ctxPath + "/components/unitselect.jsp' name='selunitiframe' id='selunitiframe' frameborder=0 style='padding:0; margin:0; width:100%;height:100%;'></iframe></div>").appendTo("body");
		}
		iframdiag.find("iframe").attr("src", ctxPath + "/components/unitselect.jsp");
		iframdiag.dialogIframe({
			width:450, height:500, title:'选择机构', modal:true,
			buttons:{
				'确定':function() {
					var seledunit = selunitiframe.getSelectedUnit();
					if (seledunit == null) {
						alert("请选择一个组织机构！");
						return;
					}
					if ($.isFunction(callback)) {
						if (callback(seledunit) === false) {
							return;
						}
					}
					iframdiag.dialog("destroy");
				}, '取消': function() {
					iframdiag.dialog("destroy");
				}
			}
		});
	},

	selectUnits:function (origOptions) {
		var options = {
			callback:jQuery.noop,
			selectedunits:[]
		}
		var opts = jQuery.extend (true, {}, options, origOptions);

		var iframdiag = null;
		if ($("#selunitsiframes").length) {
			iframdiag = $("#selunitsiframes").parent();
		} else {
			iframdiag = $("<div style='width:100%;height:100%;display:none;overflow:auto;'><iframe name='selunitsiframes' id='selunitsiframes' frameborder=0 style='padding:0; margin:0; width:100%;height:100%;'></iframe></div>").appendTo("body");
		}

		iframdiag.dialogIframe({
			width:$(window).width() - 100, height:$(window).height() - 50, title:'选择机构', modal:true,
			buttons:{
				'确定':function() {
					var seledunits = selunitsiframes.getSelectedUnits();
					if ($.isFunction(opts.callback)) {
						opts.callback(seledunits);
					}
					iframdiag.dialog("destroy");
				}, '取消': function() {
					iframdiag.dialog("destroy");
				}
			}
		});
		$("<form action='"+ctxPath+"/components/unitsselect.jsp' target='selunitsiframes' method='post'><input name='units' type='hidden' value='"+encodeURIComponent(JSON.stringify(opts.selectedunits))+"'/></form>").appendTo("body")[0].submit();
	},

	selectRoles: function(origOptions) {
		var options = {
			callback:jQuery.noop,
			selectedroles:[]
		}
		var opts = jQuery.extend (true, {}, options, origOptions);

		var iframdiag = null;
		if ($("#selrolesiframes").length) {
			iframdiag = $("#selrolesiframes").parent();
		} else {
			iframdiag = $("<div style='width:100%;height:100%;display:none;overflow:auto;'><iframe name='selrolesiframes' id='selrolesiframes' frameborder=0 style='padding:0; margin:0; width:100%;height:100%;'></iframe></div>").appendTo("body");
		}

		iframdiag.dialogIframe({
			width:$(window).width() - 100, height:$(window).height() - 50, title:'选择机构', modal:true,
			buttons:{
				'确定':function() {
					var seledroles = selrolesiframes.getSelectedRoles();
					if ($.isFunction(opts.callback)) {
						opts.callback(seledroles);
					}
					iframdiag.dialog("destroy");
				}, '取消': function() {
					iframdiag.dialog("destroy");
				}
			}
		});
		
		$("<form action='"+ctxPath+"/components/roleselect.jsp' target='selrolesiframes' method='post'><input name='roles' type='hidden' value='"+encodeURIComponent(JSON.stringify(opts.selectedunits))+"'/></form>").appendTo("body")[0].submit();
	},
	
	// 以列表方式选择数据
	selectByList: function(origOptions) {
		var options = {
				component:"listselectdemo",
				selectionMode:'multipleRows', // 可选的选择方式，默认为多选，单选时传入singleRow字符串即可
				callback: $.noop,
				queryval: "",
				data:'',
				width: $(window).width() - 100,
				height: $(window).height() - 50,
				selectedItemSids: '',// 数组或逗号分割的已选中项sid列表
				parentsid: ''// 关联列表的父级列表sid，将传送到后台供可选查询项
			}
		var opts = jQuery.extend (true, {}, options, origOptions);
		
		var iframdiag = null;
		if ($("#listselectsiframes_" + opts.component).length) {
			iframdiag = $("#listselectsiframes_" + opts.component).parent();
		} else {
			iframdiag = $("<div style='width:100%;height:99%;display:none;overflow:auto;'><iframe name='listselectsiframes_" + opts.component + "' id='listselectsiframes_" + opts.component + "' frameborder=0 style='padding:0; margin:0; width:100%;height:99%;'></iframe></div>").appendTo("body");
		}
		
		// 查询对话框title
		var dialogbtns = {};
		
		dialogbtns[$.i18n('antelope.ok')] = function(e) {
			var seleditems = $("#listselectsiframes_" + opts.component)[0].contentWindow.getSelectedItems();
			if ($.isFunction(opts.callback)) {
				var issucces = opts.callback.call(e.target, seleditems);
				if (issucces) {
					iframdiag.dialog("destroy");
					$("#listselectsiframes_" + opts.component).attr("src", "about:blank");
				}
			} else {
				iframdiag.dialog("destroy");
				$("#listselectsiframes_" + opts.component).attr("src", "about:blank");
			}
		};
		
		dialogbtns[$.i18n('antelope.cancel')] = function() {
			iframdiag.dialog("destroy");
			$("#listselectsiframes_" + opts.component).attr("src", "about:blank");
		};
		
		$.getJSON(ctx + "/common/components/ListSelectController/getListSelectOptions.vot?component=" + opts.component, function(data){
			iframdiag.dialogIframe({
				width: opts.width, height: opts.height, title:$.i18n('antelope.choose') + data.title, modal:true,
				buttons:dialogbtns
			});
			
			var itemsidsnodot = encodeURIComponent(opts.selectedItemSids);
			$("<form action='"+ctxPath+"/components/listselect.jsp' target='listselectsiframes_" + opts.component + "' method='post'>\
					<input name='component' type='hidden' value='" + opts.component + "'/>\
					<input name='parentsid' type='hidden' value='" + opts.parentsid + "'/>\
					<input name='selectionMode' type='hidden' value='" + opts.selectionMode + "'/>\
					<input name='selectedItemSids' type='hidden' value='" + itemsidsnodot + "'/>\
					<input name='formdata' type='hidden' value='" + opts.data + "'/>\
					<input name='queryval' type='hidden' value='" + opts.queryval + "'/>\
					</form>").appendTo("body")[0].submit();
		});
		
	},
	
	// 以列表方式选择数据
	selectByTile: function(origOptions) {
		var options = {
				component:"tileselectdemo",
				callback: $.noop,
				width:top.$(top.window).width() - 50,
				height:top.$(top.window).height() - 50
			}
		var opts = jQuery.extend (true, {}, options, origOptions);
		$.getJSON(ctx + "/common/components/TileSelectController/getListSelectOptions.vot?component=" + opts.component, function(data){
			var iframdiag = $("<div style='display:none;overflow:auto;'/>");
			var dialogbtns = {};
			
			dialogbtns[$.i18n('antelope.ok')] = function(e) {
				var seleditems = iframdiag.find("iframe")[0].contentWindow.getSelectedItems();
				if ($.isFunction(opts.callback)) {
					var issucces = opts.callback.call(e.target, seleditems);
					if (issucces)
						iframdiag.dialog("destroy");
				} else {
					iframdiag.dialog("destroy");
				}
			};
			
			dialogbtns[$.i18n('antelope.cancel')] = function() {
				iframdiag.dialog("destroy");
			};
			
			iframdiag = $.dialogTopIframe({
				url:ctx + "/components/tileselect.jsp",
				width: opts.width,
				height: opts.height,
				buttons: dialogbtns,
				title: $.i18n('antelope.choose') + data.title,
				data: "component=" + opts.component
			});
		});
		
	},
	
	selectByTree: function(origOptions) {
		var options = {
				component:"treeselectdemo",
				selectionMode:'multipleRows', // 可选的选择方式，默认为多选，单选时传入singleRow字符串即可
				callback: $.noop,
				checkable: false,
				queryval: "",
				data:'',
				selectedItemSids: '',// 数组或逗号分割的已选中项sid列表
				parentsid: ''// 关联列表的父级列表sid，将传送到后台供可选查询项
			};
		var opts = jQuery.extend (true, {}, options, origOptions);
		$.getJSON(ctx + "/common/components/TreeSelectController/getTreeSelectOptions.vot?component=" + opts.component, function(data){
			// 查询对话框title
			
			var iframdiag = $("<div style='display:none;overflow:auto;'/>");
			var dialogbtns = {};
			
			dialogbtns[$.i18n('antelope.ok')] = function(e) {
				var seleditems = iframdiag.find("iframe")[0].contentWindow.getSelectedItems();
				if ($.isFunction(opts.callback)) {
					var issucces = opts.callback.call(e.target, seleditems);
					if (issucces)
						iframdiag.dialog("destroy");
				} else {
					iframdiag.dialog("destroy");
				}
			};
			
			dialogbtns[$.i18n('antelope.cancel')] = function() {
				iframdiag.dialog("destroy");
			};
			iframdiag.appendTo("body")
			.loadIframe(ctx + "/components/treeselect.jsp", opts).dialogIframe({
				width:$(window).width() - 50, height:$(window).height() - 30, title:$.i18n('antelope.choose') + data.title,
				buttons: dialogbtns,
				modal:true
			});
		});
	},
	
	selectByTreeList: function(origOptions) {
		var options = {
				component:"treelistselectdemo",
				selectionMode:'multipleRows', // 可选的选择方式，默认为多选，单选时传入singleRow字符串即可
				callback: $.noop,
				queryval: "",
				data:'',
				selectedItemSids: '',// 数组或逗号分割的已选中项sid列表
				parentsid: ''// 关联列表的父级列表sid，将传送到后台供可选查询项
			};
		var opts = jQuery.extend (true, {}, options, origOptions);
	
		$.getJSON(ctx + "/common/components/TreeListSelectController/getTreeSelectOptions.vot?component=" + opts.component, function(data){
			// 查询对话框title
			
			var iframdiag = $("<div style='display:none;overflow:auto;margin:0;padding:0;'/>");
			var dialogbtns = {};
			
			dialogbtns[$.i18n('antelope.ok')] = function(e) {
				var seleditems = iframdiag.find("iframe")[0].contentWindow.getSelectedItems();
				if ($.isFunction(opts.callback)) {
					var issucces = opts.callback.call(e.target, seleditems);
					if (issucces)
						iframdiag.dialog("destroy");
				} else {
					iframdiag.dialog("destroy");
				}
			};
			
			dialogbtns[$.i18n('antelope.cancel')] = function() {
				iframdiag.dialog("destroy");
			};
			
			var jsppath = "/components/treelistselect.jsp";
			if (window['componentVersion'] == 'v2') {
				jsppath = "/componentsv2/treelistselect.jsp";
			}
			
			iframdiag.appendTo("body")
			.loadIframe(ctx + jsppath, opts).dialogIframe({
				width:$(window).width() - 50, height:$(window).height() - 30, title:$.i18n('antelope.choose') + data.title,
				buttons: dialogbtns,
				modal:true
			});
		});
	},
	
	selectByTreesList: function(origOptions) {
		var options = {
				component:"treeslistselectdemo",
				selectionMode:'multipleRows', // 可选的选择方式，默认为多选，单选时传入singleRow字符串即可
				callback: $.noop,
				queryval: "",
				data:'',
				selectedItemSids: '',// 数组或逗号分割的已选中项sid列表
				parentsid: ''// 关联列表的父级列表sid，将传送到后台供可选查询项
			};
		var opts = jQuery.extend (true, {}, options, origOptions);
		$.getJSON(ctx + "/common/components/MultipleTreesListSelectController/getTreesListSelectOptions.vot?component=" + opts.component, function(data){
			// 查询对话框title
			
			var iframdiag = null;
			var dialogbtns = {};
			
			dialogbtns[$.i18n('antelope.ok')] = function(e) {
				var seleditems = iframdiag.find("iframe")[0].contentWindow.getSelectedItems();
				if ($.isFunction(opts.callback)) {
					var issucces = opts.callback.call(e.target, seleditems);
					if (issucces)
						iframdiag.dialog("destroy");
				} else {
					iframdiag.dialog("destroy");
				}
			};
			
			dialogbtns[$.i18n('antelope.cancel')] = function() {
				iframdiag.dialog("destroy");
			};
			
			// 将方法去掉
			var dataopts = {};
			for (var key in opts) {
				if (!$.isFunction(opts[key]))
					dataopts[key] = opts[key];
			}
			
			iframdiag = $.dialogTopIframe({
				url: ctx + "/components/multiple_treeslistselect.jsp",
				data:dataopts,
				buttons: dialogbtns, title:$.i18n('antelope.choose') + data.title, modal:true
			});
			
		});
	},
	
	selectByTrees: function(origOptions) {
		var options = {
				component:"multipletreesselectdemo",
				selectionMode:'multipleRows', // 可选的选择方式，默认为多选，单选时传入singleRow字符串即可
				callback: $.noop,
				queryval: "",
				data:'',
				selectedItemSids: '',// 数组或逗号分割的已选中项sid列表
				parentsid: ''// 关联列表的父级列表sid，将传送到后台供可选查询项
			};
		var opts = jQuery.extend (true, {}, options, origOptions);
		$.getJSON(ctx + "/common/components/MultipleTreesSelectController/getTreesListSelectOptions.vot?component=" + opts.component, function(data){
			// 查询对话框title
			
			var iframdiag = null;
			var dialogbtns = {};
			
			dialogbtns[$.i18n('antelope.ok')] = function(e) {
				var seleditems = iframdiag.find("iframe")[0].contentWindow.getSelectedItems();
				if ($.isFunction(opts.callback)) {
					var issucces = opts.callback.call(e.target, seleditems);
					if (issucces)
						iframdiag.dialog("destroy");
				} else {
					iframdiag.dialog("destroy");
				}
			};
			
			dialogbtns[$.i18n('antelope.cancel')] = function() {
				iframdiag.dialog("destroy");
			};
			
			// 将方法去掉
			var dataopts = {};
			for (var key in opts) {
				if (!$.isFunction(opts[key]))
					dataopts[key] = opts[key];
			}
			
			var jsppath = "/components/multiple_treesselect.jsp";
			if (window['componentVersion'] == 'v2') {
				jsppath = "/componentsv2/multiple_treesselect.jsp";
			}
			
			iframdiag = $.dialogTopIframe({
				url: ctx + jsppath,
				data: dataopts,
				buttons: dialogbtns, title:$.i18n('antelope.choose') + data.title, modal:true
			});
			
		});
	},

	selectRisks:function (origOptions) {
		var options = {
				callback:jQuery.noop,
				selectedrisks:[],
				issingle:false,
				editable:false
			}
		var opts = jQuery.extend (true, {}, options, origOptions);
		var iframdiag = null;
		if ($("#selrisksiframes").length) {
			iframdiag = $("#selrisksiframes").parent();
		} else {
			iframdiag = $("<div style='width:100%;height:100%;display:none;overflow:auto;'><iframe name='selrisksiframes' id='selrisksiframes' frameborder=0 style='padding:0; margin:0; width:100%;height:100%;'></iframe></div>").appendTo("body");
		}

		iframdiag.dialogIframe({
			width:$(window).width() - 100, height:$(window).height() - 50, title:'选择合规点', modal:true,
			buttons:{
				'确定':function() {
					var seledunits = selrisksiframes.getSelectedRisks();
					if ($.isFunction(opts.callback)) {
						if (opts.issingle) {
							if (seledunits.length == 0) {
								alert("请选择一个合规点！");
								return;
							}
							opts.callback(seledunits[0]);
						} else {
							opts.callback(seledunits);
						}
					}
					iframdiag.dialog("destroy");
				}, '取消': function() {
					iframdiag.dialog("destroy");
				}
			}
		});
		
		$("<form action='"+ctxPath+"/components/risksselect.jsp?editable=" + opts.editable + "&issingle="+opts.issingle+"' target='selrisksiframes' method='post'><input name='risks' type='hidden' value='"+encodeURIComponent(JSON.stringify(opts.selectedrisks))+"'/></form>")
		.appendTo("body").submit();
	},

	// 通用删除
	submitDelete: function(origOptions) {
		var options = {
				confirmText:$.i18n('antelope.confirmtodelete')
			}
		var opts = jQuery.extend (true, {}, options, origOptions);
		$.commonSubmit(opts);
	},

	commonSubmit: function(origOptions) {
		var options = {
				url:null,
				callback:jQuery.noop,
				isAutoSave: false, // 若为自动保存，则不提示是否进行保存
				data: null,
				confirmText:$.i18n('antelope.form.confirmoperat'),
				button:null,
				async: true // 提交时是否使用异步形式，默认为使用
			}
		var opts = jQuery.extend (true, {}, options, origOptions);

		if (opts.isAutoSave || confirm(opts.confirmText)) {
			
			if (opts.button) {
				$(opts.button)._setButtonCommitting(true);
			}
			var issuccess = false;
			$.ajax({
				type:'post',
				url:opts.url,
				async: opts.async,
				data: opts.data,
				success: function(data) {
					$(opts.button)._setButtonCommitting(false);
					if (data && data.indexOf("success") == -1 && data != 'null' && data.indexOf("{") == -1) {
						if (data.length < 400) {
							alert(data);
							return;
						} else {
							location.href = ctxPath + '/index.jsp';
							return;
						}
					} else {
						issuccess = true;
					}
					opts.callback(data);
				}
			});
			if (!opts.async)
				return issuccess;
		}
	},

	// 从对象数组中将sid数组抽取出来
	extractSids: function(arr) {
		return $.extractField(arr, 'sid');
	},
	
	extractField: function(arr, fieldname) {
		var fieldvals = [];
		$(arr).each(function(){
			fieldvals.push(this[fieldname]);
		});
		return fieldvals;
	},

	// 通用获取新id值
	getNewsid:function() {
		// 重置新的sid
		var newsid = "";
		$.ajax({
			url:ctxPath + "/common/getanewsid.vot?randomstr=" + Math.random(),
			async:false,
			success:function(data) {
				newsid = data;
			}
		});

		return newsid;
	},

	// 获取系统属性参数
	getSysprop:function(propname) {
		// 重置新的sysprop
		var sysprop = "";
		$.ajax({
			url:ctxPath + "/common/getsysprop.vot?propname=" + propname + "&stopcache=" + Math.random(),
			async:false,
			success:function(data) {
				sysprop = data;
			}
		});

		return sysprop;
	},
	
	getIndexPagePos: function() {
		// 重置新的sysprop
		var sysprop = "";
		$.ajax({
			url:ctxPath + "/common/getIndexPagePos.vot",
			async:false,
			success:function(data) {
				sysprop = data;
			}
		});

		return sysprop;
	},

	// 通用文件上传对话框
	fileuploadDialog: function(origOptions) {
		var options = {
				filegroupsid:null,
				readonly:false,
				autosave:false,
				uploadComplete:jQuery.noop,
				width:400,
				height:300,
				maxfilecount:5,
				extension:null,
				callback:$.noop
			};
		var opts = jQuery.extend (true, {}, options, origOptions);

		var _dialogdiv = $("<div style='display:none'/>");
		_dialogdiv.appendTo("body");
		
		var dialogdivbtns = {};
		dialogdivbtns[$.i18n('antelope.close')] = function() {
			_dialogdiv.dialog("destroy");
			opts.callback(_dialogdiv.fileupload('getFileInfos'));
		};
		
		_dialogdiv.dialog({
			width:opts.width, height:opts.height, title:$.i18n('antelope.ui.uploaddialog.fileupload'),
			buttons:dialogdivbtns
		}).fileupload(opts);
	},
	
	// 修改登录后的人员信息
	changeLogonUserinfo: function() {
		var topiframe = $.dialogTopIframe({
			url: ctxPath + "/components/changeuserinfo.jsp?" + Math.random(),
			width: 440,
			height: 250,
			title: "修改个人信息",
			buttons: {
				'确定': function() {
					topiframe.find("iframe")[0].contentWindow.commit(topiframe);
				}, 
				'取消': function() {
					top.$(topiframe).dialog("destroy");
				}
			}
		});
	}
});

/**
 * jqueryguid生成器
 * jQuery Guid v1.0.0-1
 * Requires jQuery 1.2.6+ (Not tested with earlier versions).
 * Copyright (c) 2010 Aaron E. [jquery at happinessinmycheeks dot com]
 * Licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 *	Usage:
 *		jQuery.Guid.Value() // Returns value of internal Guid. If no guid has been specified, returns a new one (value is then stored internally).
 *		jQuery.Guid.New() // Returns a new Guid and sets it's value internally. Also accepts GUID, Sets it internally.
 *		jQuery.Guid.Empty() // Returns an empty Guid 00000000-0000-0000-0000-000000000000.
 *		jQuery.Guid.IsEmpty() // Returns boolean. True if empty/undefined/blank/null.
 *		jQuery.Guid.IsValid() // Returns boolean. True valid guid, false if not.
 *		jQuery.Guid.Set() // Retrns Guid. Sets Guid to user specified Guid, if invalid, returns an empty guid.
 *
 */
jQuery.extend({
	Guid: {
		Set: function(val) {
			var value;
			if (arguments.length == 1) {
				if (this.IsValid(arguments[0])) {
					value = arguments[0];
				} else {
					value = this.Empty();
				}
			}
			$(this).data("value", value);
			return value;
		},

		Empty: function() {
			return "00000000-0000-0000-0000-000000000000";
		},

		IsEmpty: function(gid) {
			return gid == this.Empty() || typeof (gid) == 'undefined' || gid == null || gid == '';
		},

		IsValid: function(value) {
			rGx = new RegExp("\\b(?:[A-F0-9]{8})(?:-[A-F0-9]{4}){3}-(?:[A-F0-9]{12})\\b");
			return rGx.exec(value) != null;
		},

		New: function() {
			if (arguments.length == 1 && this.IsValid(arguments[0])) {
				$(this).data("value", arguments[0]);
				value = arguments[0];
				return value;
			}

			var res = [], hv;
			var rgx = new RegExp("[2345]");
			for (var i = 0; i < 8; i++) {
				hv = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
				if (rgx.exec(i.toString()) != null) {
					if (i == 3) { hv = "6" + hv.substr(1, 3); }
					res.push("-");
				}
				res.push(hv.toUpperCase());
			}
			value = res.join('');
			$(this).data("value", value);
			return value;
		},

		Value: function() {
			if ($(this).data("value")) {
				return $(this).data("value");
			}
			var val = this.New();
			$(this).data("value", val);
			return val;
		}
	}
})();

/**拓展textarea 字数限制
 *使用 只需在textarea 中 添加max 属性 Demo  <textarea maxlength="1000"   ...
**/
jQuery.fn.extend({
      showWordCount: function() {
          var _max = $(this).attr('maxlength');
          var _length = $(this).text().length;

          if(_length >=_max) {
        	  $(this).parent().find('div').css({color:'red'});
              $(this).text($(this).text().substring(0, _max));
              _length=_max;
          }

          if(_length <_max) {
        	  $(this).parent().find('div').css({color:'#4586b5'});
          }

          _left = $(this).offset().left;
          _top = $(this).offset().top;
          _width = $(this).width();
          _height = $(this).height();
          $(this).parent().find('div').html(_length + '/' + _max);
          $(this).parent().find('div').css({
        	position:'absolute',
      		fontSize:'9pt',
      		color:'#4586b5',
      		width:'60px',
      		textAlign:'right',
              'left':_left + _width - 60,
              'top':_top + _height - 6
          });
      }
  });


/**
 * 判断是否安装了pdf插件
 * if (Check_AdobeReader().installed) {
 * }
 */
function Check_AdobeReader() {
    var displayString;
    var acrobat = new Object();
    acrobat.installed = false;
    acrobat.version = "0.0";

    if (navigator.plugins && navigator.plugins.length) {
        for (x = 0; x < navigator.plugins.length; x++) {
            if (navigator.plugins[x].description.indexOf("Adobe Acrobat") != -1) {
                acrobat.version = parseFloat(navigator.plugins[x].description.split("Version ")[1]);
                if (acrobat.version.toString().length == 1) acrobat.version += ".0";
                acrobat.installed = true;
                displayString = "Acrobat Version: " + acrobat.version;
                break;
            } else if (navigator.plugins[x].description.indexOf("Adobe PDF Plug-In") != -1) {
                acrobat.installed = true;
                acrobat.version = "8+";
                displayString = "Acrobat Version 8 or Above";
            }
        }
    } else if (window.ActiveXObject) {
        for (x = 2; x < 10; x++) {
            try {
                oAcro = eval("new ActiveXObject('PDF.PdfCtrl." + x + "');");
                if (oAcro) {
                    acrobat.installed = true;

                    acrobat.version = x + ".0";
                    displayString = "Acrobat Version: " + acrobat.version;
                }
            }
            catch (e) { }
        }

        try {
            oAcro4 = new ActiveXObject("PDF.PdfCtrl.1");
            if (oAcro4) {
                acrobat.installed = true;
                acrobat.version = '4.0';
                displayString = 'Acrobat Version: ' + acrobat.version;
            }
        }
        catch (e) { }

        try {
            oAcro7 = new ActiveXObject('AcroPDF.PDF.1');
            if (oAcro7) {
                acrobat.installed = true;
                acrobat.version = '7.0';
                displayString = 'Acrobat Version: ' + acrobat.version;
            }
        }
        catch (e) { }
    }

    return acrobat;
}


/*******************************************************************************
Spry菜单组件
SpryMenuBar.js
This file handles the JavaScript for Spry Menu Bar.  You should have no need
to edit this file.  Some highlights of the MenuBar object is that timers are
used to keep submenus from showing up until the user has hovered over the parent
menu item for some time, as well as a timer for when they leave a submenu to keep
showing that submenu until the timer fires.

*******************************************************************************/

(function() { // BeginSpryComponent

if (typeof Spry == "undefined") window.Spry = {}; if (!Spry.Widget) Spry.Widget = {};

Spry.BrowserSniff = function()
{
	var b = navigator.appName.toString();
	var up = navigator.platform.toString();
	var ua = navigator.userAgent.toString();

	this.mozilla = this.ie = this.opera = this.safari = false;
	var re_opera = /Opera.([0-9\.]*)/i;
	var re_msie = /MSIE.([0-9\.]*)/i;
	var re_gecko = /gecko/i;
	var re_safari = /(applewebkit|safari)\/([\d\.]*)/i;
	var r = false;

	if ( (r = ua.match(re_opera))) {
		this.opera = true;
		this.version = parseFloat(r[1]);
	} else if ( (r = ua.match(re_msie))) {
		this.ie = true;
		this.version = parseFloat(r[1]);
	} else if ( (r = ua.match(re_safari))) {
		this.safari = true;
		this.version = parseFloat(r[2]);
	} else if (ua.match(re_gecko)) {
		var re_gecko_version = /rv:\s*([0-9\.]+)/i;
		r = ua.match(re_gecko_version);
		this.mozilla = true;
		this.version = parseFloat(r[1]);
	}
	this.windows = this.mac = this.linux = false;

	this.Platform = ua.match(/windows/i) ? "windows" :
					(ua.match(/linux/i) ? "linux" :
					(ua.match(/mac/i) ? "mac" :
					ua.match(/unix/i)? "unix" : "unknown"));
	this[this.Platform] = true;
	this.v = this.version;

	if (this.safari && this.mac && this.mozilla) {
		this.mozilla = false;
	}
};

Spry.is = new Spry.BrowserSniff();

//Constructor for Menu Bar
//element should be an ID of an unordered list (<ul> tag)
//preloadImage1 and preloadImage2 are images for the rollover state of a menu
Spry.Widget.MenuBar = function(element, opts)
{
	this.init(element, opts);
};

Spry.Widget.MenuBar.prototype.init = function(element, opts)
{
	this.element = this.getElement(element);

	// represents the current (sub)menu we are operating on
	this.currMenu = null;
	this.showDelay = 250;
	this.hideDelay = 600;
	if(typeof document.getElementById == 'undefined' || (navigator.vendor == 'Apple Computer, Inc.' && typeof window.XMLHttpRequest == 'undefined') || (Spry.is.ie && typeof document.uniqueID == 'undefined'))
	{
		// bail on older unsupported browsers
		return;
	}

	// Fix IE6 CSS images flicker
	if (Spry.is.ie && Spry.is.version < 7){
		try {
			document.execCommand("BackgroundImageCache", false, true);
		} catch(err) {}
	}

	this.upKeyCode = Spry.Widget.MenuBar.KEY_UP;
	this.downKeyCode = Spry.Widget.MenuBar.KEY_DOWN;
	this.leftKeyCode = Spry.Widget.MenuBar.KEY_LEFT;
	this.rightKeyCode = Spry.Widget.MenuBar.KEY_RIGHT;
	this.escKeyCode = Spry.Widget.MenuBar.KEY_ESC;

	this.hoverClass = 'MenuBarItemHover';
	this.subHoverClass = 'MenuBarItemSubmenuHover';
	this.subVisibleClass ='MenuBarSubmenuVisible';
	this.hasSubClass = 'MenuBarItemSubmenu';
	this.activeClass = 'MenuBarActive';
	this.isieClass = 'MenuBarItemIE';
	this.verticalClass = 'MenuBarVertical';
	this.horizontalClass = 'MenuBarHorizontal';
	this.enableKeyboardNavigation = true;

	this.hasFocus = false;
	// load hover images now
	if(opts)
	{
		for(var k in opts)
		{
			if (typeof this[k] == 'undefined')
			{
				var rollover = new Image;
				rollover.src = opts[k];
			}
		}
		Spry.Widget.MenuBar.setOptions(this, opts);
	}

	// safari doesn't support tabindex
	if (Spry.is.safari)
		this.enableKeyboardNavigation = false;

	if(this.element)
	{
		this.currMenu = this.element;
		var items = this.element.getElementsByTagName('li');
		for(var i=0; i<items.length; i++)
		{
			if (i > 0 && this.enableKeyboardNavigation)
				items[i].getElementsByTagName('a')[0].tabIndex='-1';

			this.initialize(items[i], element);
			if(Spry.is.ie)
			{
				this.addClassName(items[i], this.isieClass);
				items[i].style.position = "static";
			}
		}
		if (this.enableKeyboardNavigation)
		{
			var self = this;
			this.addEventListener(document, 'keydown', function(e){self.keyDown(e); }, false);
		}

		if(Spry.is.ie)
		{
			if(this.hasClassName(this.element, this.verticalClass))
			{
				this.element.style.position = "relative";
			}
			var linkitems = this.element.getElementsByTagName('a');
			for(var i=0; i<linkitems.length; i++)
			{
				linkitems[i].style.position = "relative";
			}
		}
	}
};
Spry.Widget.MenuBar.KEY_ESC = 27;
Spry.Widget.MenuBar.KEY_UP = 38;
Spry.Widget.MenuBar.KEY_DOWN = 40;
Spry.Widget.MenuBar.KEY_LEFT = 37;
Spry.Widget.MenuBar.KEY_RIGHT = 39;

Spry.Widget.MenuBar.prototype.getElement = function(ele)
{
	if (ele && typeof ele == "string")
		return document.getElementById(ele);
	return ele;
};

Spry.Widget.MenuBar.prototype.hasClassName = function(ele, className)
{
	if (!ele || !className || !ele.className || ele.className.search(new RegExp("\\b" + className + "\\b")) == -1)
	{
		return false;
	}
	return true;
};

Spry.Widget.MenuBar.prototype.addClassName = function(ele, className)
{
	if (!ele || !className || this.hasClassName(ele, className))
		return;
	ele.className += (ele.className ? " " : "") + className;
};

Spry.Widget.MenuBar.prototype.removeClassName = function(ele, className)
{
	if (!ele || !className || !this.hasClassName(ele, className))
		return;
	ele.className = ele.className.replace(new RegExp("\\s*\\b" + className + "\\b", "g"), "");
};

//addEventListener for Menu Bar
//attach an event to a tag without creating obtrusive HTML code
Spry.Widget.MenuBar.prototype.addEventListener = function(element, eventType, handler, capture)
{
	try
	{
		if (element.addEventListener)
		{
			element.addEventListener(eventType, handler, capture);
		}
		else if (element.attachEvent)
		{
			element.attachEvent('on' + eventType, handler);
		}
	}
	catch (e) {}
};

//createIframeLayer for Menu Bar
//creates an IFRAME underneath a menu so that it will show above form controls and ActiveX
Spry.Widget.MenuBar.prototype.createIframeLayer = function(menu)
{
	var layer = document.createElement('iframe');
	layer.tabIndex = '-1';
	layer.src = 'javascript:""';
	layer.frameBorder = '0';
	layer.scrolling = 'no';
	menu.parentNode.appendChild(layer);
	
	layer.style.left = menu.offsetLeft + 'px';
	layer.style.top = menu.offsetTop + 'px';
	layer.style.width = menu.offsetWidth + 'px';
	layer.style.height = menu.offsetHeight + 'px';
};

//removeIframeLayer for Menu Bar
//removes an IFRAME underneath a menu to reveal any form controls and ActiveX
Spry.Widget.MenuBar.prototype.removeIframeLayer =  function(menu)
{
	var layers = ((menu == this.element) ? menu : menu.parentNode).getElementsByTagName('iframe');
	while(layers.length > 0)
	{
		layers[0].parentNode.removeChild(layers[0]);
	}
};

//clearMenus for Menu Bar
//root is the top level unordered list (<ul> tag)
Spry.Widget.MenuBar.prototype.clearMenus = function(root)
{
	var menus = root.getElementsByTagName('ul');
	for(var i=0; i<menus.length; i++)
		this.hideSubmenu(menus[i]);

	this.removeClassName(this.element, this.activeClass);
};

//bubbledTextEvent for Menu Bar
//identify bubbled up text events in Safari so we can ignore them
Spry.Widget.MenuBar.prototype.bubbledTextEvent = function()
{
	return Spry.is.safari && (event.target == event.relatedTarget.parentNode || (event.eventPhase == 3 && event.target.parentNode == event.relatedTarget));
};

//showSubmenu for Menu Bar
//set the proper CSS class on this menu to show it
Spry.Widget.MenuBar.prototype.showSubmenu = function(menu)
{
	if(this.currMenu)
	{
		this.clearMenus(this.currMenu);
		this.currMenu = null;
	}
	
	if(menu)
	{
		this.addClassName(menu, this.subVisibleClass);
		if(typeof document.all != 'undefined' && !Spry.is.opera && navigator.vendor != 'KDE')
		{
			if(!this.hasClassName(this.element, this.horizontalClass) || menu.parentNode.parentNode != this.element)
			{
				menu.style.top = menu.parentNode.offsetTop + 'px';
			}
		}
		if(Spry.is.ie && Spry.is.version < 7)
		{
			this.createIframeLayer(menu);
		}
	}
	this.addClassName(this.element, this.activeClass);
};

//hideSubmenu for Menu Bar
//remove the proper CSS class on this menu to hide it
Spry.Widget.MenuBar.prototype.hideSubmenu = function(menu)
{
	if(menu)
	{
		this.removeClassName(menu, this.subVisibleClass);
		if(typeof document.all != 'undefined' && !Spry.is.opera && navigator.vendor != 'KDE')
		{
			menu.style.top = '';
			menu.style.left = '';
		}
		if(Spry.is.ie && Spry.is.version < 7)
			this.removeIframeLayer(menu);
	}
};

//initialize for Menu Bar
//create event listeners for the Menu Bar widget so we can properly
//show and hide submenus
Spry.Widget.MenuBar.prototype.initialize = function(listitem, element)
{
	var opentime, closetime;
	var link = listitem.getElementsByTagName('a')[0];
	var submenus = listitem.getElementsByTagName('ul');
	var menu = (submenus.length > 0 ? submenus[0] : null);

	if(menu)
		this.addClassName(link, this.hasSubClass);

	if(!Spry.is.ie)
	{
		// define a simple function that comes standard in IE to determine
		// if a node is within another node
		listitem.contains = function(testNode)
		{
			// this refers to the list item
			if(testNode == null)
				return false;

			if(testNode == this)
				return true;
			else
				return this.contains(testNode.parentNode);
		};
	}

	// need to save this for scope further down
	var self = this;
	this.addEventListener(listitem, 'mouseover', function(e){self.mouseOver(listitem, e);}, false);
	this.addEventListener(listitem, 'mouseout', function(e){if (self.enableKeyboardNavigation) self.clearSelection(); self.mouseOut(listitem, e);}, false);

	if (this.enableKeyboardNavigation)
	{
		this.addEventListener(link, 'blur', function(e){self.onBlur(listitem);}, false);
		this.addEventListener(link, 'focus', function(e){self.keyFocus(listitem, e);}, false);
	}
};
Spry.Widget.MenuBar.prototype.keyFocus = function (listitem, e)
{
	this.lastOpen = listitem.getElementsByTagName('a')[0];
	this.addClassName(this.lastOpen, listitem.getElementsByTagName('ul').length > 0 ? this.subHoverClass : this.hoverClass);
	this.hasFocus = true;
};
Spry.Widget.MenuBar.prototype.onBlur = function (listitem)
{
	this.clearSelection(listitem);
};
Spry.Widget.MenuBar.prototype.clearSelection = function(el){
	//search any intersection with the current open element
	if (!this.lastOpen)
		return;

	if (el)
	{
		el = el.getElementsByTagName('a')[0];
		
		// check children
		var item = this.lastOpen;
		while (item != this.element)
		{
			var tmp = el;
			while (tmp != this.element)
			{
				if (tmp == item)
					return;
				try{
					tmp = tmp.parentNode;
				}catch(err){break;}
			}
			item = item.parentNode;
		}
	}
	var item = this.lastOpen;
	while (item != this.element)
	{
		this.hideSubmenu(item.parentNode);
		var link = item.getElementsByTagName('a')[0];
		this.removeClassName(link, this.hoverClass);
		this.removeClassName(link, this.subHoverClass);
		item = item.parentNode;
	}
	this.lastOpen = false;
};
Spry.Widget.MenuBar.prototype.keyDown = function (e)
{
	if (!this.hasFocus)
		return;

	if (!this.lastOpen)
	{
		this.hasFocus = false;
		return;
	}

	var e = e|| event;
	var listitem = this.lastOpen.parentNode;
	var link = this.lastOpen;
	var submenus = listitem.getElementsByTagName('ul');
	var menu = (submenus.length > 0 ? submenus[0] : null);
	var hasSubMenu = (menu) ? true : false;

	var opts = [listitem, menu, null, this.getSibling(listitem, 'previousSibling'), this.getSibling(listitem, 'nextSibling')];
	
	if (!opts[3])
		opts[2] = (listitem.parentNode.parentNode.nodeName.toLowerCase() == 'li')?listitem.parentNode.parentNode:null;

	var found = 0;
	switch (e.keyCode){
		case this.upKeyCode:
			found = this.getElementForKey(opts, 'y', 1);
			break;
		case this.downKeyCode:
			found = this.getElementForKey(opts, 'y', -1);
			break;
		case this.leftKeyCode:
			found = this.getElementForKey(opts, 'x', 1);
			break;
		case this.rightKeyCode:
			found = this.getElementForKey(opts, 'x', -1);
			break;
		case this.escKeyCode:
		case 9:
			this.clearSelection();
			this.hasFocus = false;
		default: return;
	}
	switch (found)
	{
		case 0: return;
		case 1:
			//subopts
			this.mouseOver(listitem, e);
			break;
		case 2:
			//parent
			this.mouseOut(opts[2], e);
			break;
		case 3:
		case 4:
			// left - right
			this.removeClassName(link, hasSubMenu ? this.subHoverClass : this.hoverClass);
			break;
	}
	var link = opts[found].getElementsByTagName('a')[0];
	if (opts[found].nodeName.toLowerCase() == 'ul')
		opts[found] = opts[found].getElementsByTagName('li')[0];

	this.addClassName(link, opts[found].getElementsByTagName('ul').length > 0 ? this.subHoverClass : this.hoverClass);
	this.lastOpen = link;
	opts[found].getElementsByTagName('a')[0].focus();
 
       //stop further event handling by the browser
	return Spry.Widget.MenuBar.stopPropagation(e);
};
Spry.Widget.MenuBar.prototype.mouseOver = function (listitem, e)
{
	var link = listitem.getElementsByTagName('a')[0];
	var submenus = listitem.getElementsByTagName('ul');
	var menu = (submenus.length > 0 ? submenus[0] : null);
	var hasSubMenu = (menu) ? true : false;
	if (this.enableKeyboardNavigation)
		this.clearSelection(listitem);

	if(this.bubbledTextEvent())
	{
		// ignore bubbled text events
		return;
	}

	if (listitem.closetime)
		clearTimeout(listitem.closetime);

	if(this.currMenu == listitem)
	{
		this.currMenu = null;
	}

	// move the focus too
	if (this.hasFocus)
		link.focus();

	// show menu highlighting
	this.addClassName(link, hasSubMenu ? this.subHoverClass : this.hoverClass);
	this.lastOpen = link;
	if(menu && !this.hasClassName(menu, this.subHoverClass))
	{
		var self = this;
		listitem.opentime = window.setTimeout(function(){self.showSubmenu(menu);}, this.showDelay);
	}
};
Spry.Widget.MenuBar.prototype.mouseOut = function (listitem, e)
{
	var link = listitem.getElementsByTagName('a')[0];
	var submenus = listitem.getElementsByTagName('ul');
	var menu = (submenus.length > 0 ? submenus[0] : null);
	var hasSubMenu = (menu) ? true : false;
	if(this.bubbledTextEvent())
	{
		// ignore bubbled text events
		return;
	}

	var related = (typeof e.relatedTarget != 'undefined' ? e.relatedTarget : e.toElement);
	if(!listitem.contains(related))
	{
		if (listitem.opentime) 
			clearTimeout(listitem.opentime);
		this.currMenu = listitem;

		// remove menu highlighting
		this.removeClassName(link, hasSubMenu ? this.subHoverClass : this.hoverClass);
		if(menu)
		{
			var self = this;
			listitem.closetime = window.setTimeout(function(){self.hideSubmenu(menu);}, this.hideDelay);
		}
		if (this.hasFocus)
			link.blur();
	}
};
Spry.Widget.MenuBar.prototype.getSibling = function(element, sibling)
{
	var child = element[sibling];
	while (child && child.nodeName.toLowerCase() !='li')
		child = child[sibling];

	return child;
};
Spry.Widget.MenuBar.prototype.getElementForKey = function(els, prop, dir)
{
	var found = 0;
	var rect = Spry.Widget.MenuBar.getPosition;
	var ref = rect(els[found]);

	var hideSubmenu = false;
	//make the subelement visible to compute the position
	if (els[1] && !this.hasClassName(els[1], this.MenuBarSubmenuVisible))
	{
		els[1].style.visibility = 'hidden';
		this.showSubmenu(els[1]);
		hideSubmenu = true;
	}

	var isVert = this.hasClassName(this.element, this.verticalClass);
	var hasParent = els[0].parentNode.parentNode.nodeName.toLowerCase() == 'li' ? true : false;
	
	for (var i = 1; i < els.length; i++){
		//when navigating on the y axis in vertical menus, ignore children and parents
		if(prop=='y' && isVert && (i==1 || i==2))
		{
			continue;
		}
		//when navigationg on the x axis in the FIRST LEVEL of horizontal menus, ignore children and parents
		if(prop=='x' && !isVert && !hasParent && (i==1 || i==2))
		{
			continue;
		}
			
		if (els[i])
		{
			var tmp = rect(els[i]); 
			if ( (dir * tmp[prop]) < (dir * ref[prop]))
			{
				ref = tmp;
				found = i;
			}
		}
	}
	
	// hide back the submenu
	if (els[1] && hideSubmenu){
		this.hideSubmenu(els[1]);
		els[1].style.visibility =  '';
	}

	return found;
};
Spry.Widget.MenuBar.camelize = function(str)
{
	if (str.indexOf('-') == -1){
		return str;	
	}
	var oStringList = str.split('-');
	var isFirstEntry = true;
	var camelizedString = '';

	for(var i=0; i < oStringList.length; i++)
	{
		if(oStringList[i].length>0)
		{
			if(isFirstEntry)
			{
				camelizedString = oStringList[i];
				isFirstEntry = false;
			}
			else
			{
				var s = oStringList[i];
				camelizedString += s.charAt(0).toUpperCase() + s.substring(1);
			}
		}
	}

	return camelizedString;
};

Spry.Widget.MenuBar.getStyleProp = function(element, prop)
{
	var value;
	try
	{
		if (element.style)
			value = element.style[Spry.Widget.MenuBar.camelize(prop)];

		if (!value)
			if (document.defaultView && document.defaultView.getComputedStyle)
			{
				var css = document.defaultView.getComputedStyle(element, null);
				value = css ? css.getPropertyValue(prop) : null;
			}
			else if (element.currentStyle) 
			{
					value = element.currentStyle[Spry.Widget.MenuBar.camelize(prop)];
			}
	}
	catch (e) {}

	return value == 'auto' ? null : value;
};
Spry.Widget.MenuBar.getIntProp = function(element, prop)
{
	var a = parseInt(Spry.Widget.MenuBar.getStyleProp(element, prop),10);
	if (isNaN(a))
		return 0;
	return a;
};

Spry.Widget.MenuBar.getPosition = function(el, doc)
{
	doc = doc || document;
	if (typeof(el) == 'string') {
		el = doc.getElementById(el);
	}

	if (!el) {
		return false;
	}

	if (el.parentNode === null || Spry.Widget.MenuBar.getStyleProp(el, 'display') == 'none') {
		//element must be visible to have a box
		return false;
	}

	var ret = {x:0, y:0};
	var parent = null;
	var box;

	if (el.getBoundingClientRect) { // IE
		box = el.getBoundingClientRect();
		var scrollTop = doc.documentElement.scrollTop || doc.body.scrollTop;
		var scrollLeft = doc.documentElement.scrollLeft || doc.body.scrollLeft;
		ret.x = box.left + scrollLeft;
		ret.y = box.top + scrollTop;
	} else if (doc.getBoxObjectFor) { // gecko
		box = doc.getBoxObjectFor(el);
		ret.x = box.x;
		ret.y = box.y;
	} else { // safari/opera
		ret.x = el.offsetLeft;
		ret.y = el.offsetTop;
		parent = el.offsetParent;
		if (parent != el) {
			while (parent) {
				ret.x += parent.offsetLeft;
				ret.y += parent.offsetTop;
				parent = parent.offsetParent;
			}
		}
		// opera & (safari absolute) incorrectly account for body offsetTop
		if (Spry.is.opera || Spry.is.safari && Spry.Widget.MenuBar.getStyleProp(el, 'position') == 'absolute')
			ret.y -= doc.body.offsetTop;
	}
	if (el.parentNode)
			parent = el.parentNode;
	else
		parent = null;
	if (parent.nodeName){
		var cas = parent.nodeName.toUpperCase();
		while (parent && cas != 'BODY' && cas != 'HTML') {
			cas = parent.nodeName.toUpperCase();
			ret.x -= parent.scrollLeft;
			ret.y -= parent.scrollTop;
			if (parent.parentNode)
				parent = parent.parentNode;
			else
				parent = null;
		}
	}
	return ret;
};

Spry.Widget.MenuBar.stopPropagation = function(ev)
{
	if (ev.stopPropagation)
		ev.stopPropagation();
	else
		ev.cancelBubble = true;
	if (ev.preventDefault) 
		ev.preventDefault();
	else 
		ev.returnValue = false;
};

Spry.Widget.MenuBar.setOptions = function(obj, optionsObj, ignoreUndefinedProps)
{
	if (!optionsObj)
		return;
	for (var optionName in optionsObj)
	{
		if (ignoreUndefinedProps && optionsObj[optionName] == undefined)
			continue;
		obj[optionName] = optionsObj[optionName];
	}
};

})(); // EndSpryComponent



//界面初始化
jQuery(function($){
	
	// 日期选择控件中文化
	if ($.datepicker) { // 在上下文存在日期选择控件时使用
		$.datepicker.regional['zh_CN'] = {
			closeText: '关闭',
			prevText: '&#x3c;上月',
			nextText: '下月&#x3e;',
			currentText: '今天',
			monthNames: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
			monthNamesShort: ['一','二','三','四','五','六',
			'七','八','九','十','十一','十二'],
			dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
			dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
			dayNamesMin: ['日','一','二','三','四','五','六'],
			weekHeader: '周',
			dateFormat: 'yy-mm-dd',
			firstDay: 1,
			isRTL: false,
			showMonthAfterYear: true,
			yearSuffix: '年'};
		
		$.datepicker.regional['en_US'] = {
			closeText: 'Done',
			prevText: 'Prev',
			nextText: 'Next',
			currentText: 'Today',
			monthNames: ['January','February','March','April','May','June',
			'July','August','September','October','November','December'],
			monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
			'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
			dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
			dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
			dayNamesMin: ['Su','Mo','Tu','We','Th','Fr','Sa'],
			weekHeader: 'Wk',
			dateFormat: 'yy-mm-dd',
			firstDay: 1,
			isRTL: false,
			showMonthAfterYear: true,
			yearSuffix: ''};
		
		$.datepicker.regional['es'] = {
			closeText: 'Cerrar',
			prevText: '&#x3c;Ant',
			nextText: 'Sig&#x3e;',
			currentText: 'Hoy',
			monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
			'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
			monthNamesShort: ['Ene','Feb','Mar','Abr','May','Jun',
			'Jul','Ago','Sep','Oct','Nov','Dic'],
			dayNames: ['Domingo','Lunes','Martes','Mi&eacute;rcoles','Jueves','Viernes','S&aacute;bado'],
			dayNamesShort: ['Dom','Lun','Mar','Mi&eacute;','Juv','Vie','S&aacute;b'],
			dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','S&aacute;'],
			weekHeader: 'Sm',
			dateFormat: 'yy-mm-dd',
			firstDay: 1,
			isRTL: false,
			showMonthAfterYear: true,
			yearSuffix: ''};
		
		$.datepicker.setDefaults($.datepicker.regional[$.i18nCache['locale']]);
	}
	// 全局初始化界面元素
	$("body").initWidgets(true);
	
});




