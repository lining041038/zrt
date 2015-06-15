/*!
 * jQuery Plugin
 * Name: Controls控件集
 * Version: 1.0
 * Author: LiNing
 * Date: 
 */
(function(){
	
// core 核心 仅内部使用
var ctxPath = null;
if (typeof(window.ctxPath) != 'undefined') {
	ctxPath = window.ctxPath;
} else {
	ctxPath = "/" + window.location.href.replace (/http:\/\/[^\/]*\/([^\/]*).*/g,'$1');
	if(!/http:\/\/*/.test(window.location.href)) { // 不在某系统当中调用，提供相对路径访问的方式加载flash
		ctxPath = "../";
	}
}

// 判断flash版本是否足够
function testPlayerVersion () {
	if (!swfobject.hasFlashPlayerVersion ("11.0.0")) {
		alert ("您的Adobe Flash Player版本过低，请下载并安装更高版本！");
		var playername = "install_flash_player_ax.exe";
		if (!jQuery.browser.msie) {
			playername = "install_flash_player.exe";
		}
		
		if (window.top == window) {
			$("<div style='position:absolute; z-index:200; height:100%; width:100%; background-color:white; font-size:12px; color:blue; left:0; top:0; text-align:center; padding-top:100px;'>\
				 <a href='"+ctxPath+"/ocx/"+playername+"'>点击此处下载新版Adobe Flash Player</a>(<font color='red'>安装完成之后请重启浏览器！</font>)\
			  </div>").css ("opacity", 0.8).prependTo ("body");
		} else {
			window.showModalDialog(ctxPath+'/controls/flashdownloadpage.jsp');
		}
		
		return false;
	}
	return true;
}
// 初始化参数
function settingsFn(settings, orig, fn) {
	if (!fn)
		fn = jQuery.noop;

	var s = orig;
	if (jQuery.isPlainObject (settings))
		s = jQuery.extend (true, {}, orig, settings);
	
	if (jQuery.isFunction (settings)) {
		fn = settings;
	}
	
	return [s, fn];
}

// 加载flash
function loadFlash(s, elems, flashPath, completeFn, idhead, initFnName) {
	// 复制数据
	var thes = JSON.parse(JSON.stringify(s));
	var flashvars = thes.flashvars || {};
	// 非显示类型的flash工具
	if (elems == jQuery) {
		if (document.getElementById(idhead))
			return;
		var objid = idhead;
		jQuery("body").append("<div style='width:0px; height:0px;'><div style='display:none' id='flashContent'/></div>");
		swfobject.embedSWF(	flashPath + '?' + Math.random(), 'flashContent', "100%", "100%", "10.0.0", "",
				flashvars, { quality : "high", bgcolor : "#ffffff", allowscriptaccess : "sameDomain", allowFullScreen : "true", allowFullScreenInteractive : "true", wmode : thes.wmode},
				{ id : objid, name : "Controls", align : "middle" });
		var flashObj;
		if (initFnName) {
			function loadComplteAction () {
				flashObj = document.getElementById( objid );
				if (flashObj && flashObj[initFnName]) {
					flashObj[initFnName]( thes );
					completeFn();
				} else {
					setTimeout (loadComplteAction, 300);
				}
			}
			loadComplteAction ();
		}
		return;
	} 
	
	elems.html (function () {
		return "<div id='flashContent" + Math.random() + "'></div>";
	});
	elems.find("div").each (function () {
		if(thes.resumeEnabled) { // 断点续传特殊代码
			this.outerHTML = '<object classid="clsid:9796CB88-9F99-4A0F-B4A7-F5D25D21133E" \
									id="' + idhead + Math.random() + '" style="display:none;" codebase="' + ctxPath +'/ocx/FileUp.CAB#version=1,0,0,1" name="Controls"\
									align="middle" width="100%" height="100%">\
							</object>';
		} else {
			swfobject.embedSWF(	flashPath + '?' + Math.random(), $(this).attr ("id"), "100%", "100%", "10.1.53", "",
					flashvars, { quality : "high", bgcolor : "#ffffff", allowScriptAccess : "always", allowFullScreen : "true", allowFullScreenInteractive : "true",  wmode : thes.wmode},
					{ id : idhead + Math.random(), name : "Controls", align : "middle" });
		}
	});
	
	if (initFnName) {
		
		function callloadAction() {
			var fls = elems.find ("[id^="+idhead+"]");
			if (fls.length < elems.length) {
				setTimeout(callloadAction, 300);
				return;
			}
			var count = fls.size ();
			
			fls.each (function () {
				var thisObj = this;
				function loadComplteAction () {
					
					if ((typeof thisObj[initFnName] == 'unknown') || thisObj[initFnName]) {
						try {
							if(thes.resumeEnabled) { // 断点续传特殊代码
								thisObj[initFnName] ();
							} else {
								thisObj[initFnName] (thes);	
							}
						} catch(e) {
						}
						count--;
					} else {
						setTimeout (loadComplteAction, 300);
					}
					if (!count) {
						try {
							completeFn.call (elems);
						} catch(e){
						}
					}
				}
				loadComplteAction ();
			});
		}
		callloadAction();
	}
	
	return elems;
}

function now() {
	return (new Date).getTime();
}

function addFlashCallback (elem, callback, flashFn) {
	var funcName = '$.callbackname' + parseInt (""+(Math.random()*100000+now()));
	eval (funcName+'=callback');
	elem [flashFn](funcName);
}

function flashEach (elems, header, callback) {
	return elems.find ("[id^="+header+"]").each (callback).end();
}
// 工具型flash控件方法调用
function callToolFunc( header, toolName, funcName, args ) {
	var tool = document.getElementById( header );
	if (!tool) {
		alert("请先加载"+toolName+"工具！");
		return;
	}
	if (tool[funcName])
		tool[funcName].apply( tool, args );
}

//仅用来测试flash版本
(function (){
var header = "onlytestflashversion";
jQuery.fn.extend ({
	onlyTestFlashVersion: function () {
		if (!testPlayerVersion())
			return;
	}
})
})();

//图片切换组件
(function (){
var header = "flashimgswacher";
jQuery.extend ({
	fxswacherSettings : {
	wmode: "window", 			// transparent
	xmlinfo:'<root>\
				<img>\
					<text color="#ff0000">冬季特卖单位的权威的驱动器超长待机超长待机超长待机</text>\
					<link>http://www.sina.com</link>\
					<src>http://img03.taobaocdn.com/tps/i3/T1xVpOXghzXXXXXXXX-470-150.jpg</src>\
				</img>\
				<img>\
					<text color="#ff0000">一元秒杀今天要今天已经提交</text>\
					<link>http://www.csdn.com</link>\
					<src>http://img.alimama.cn/bcrm/adboard/picture/2010-10-15/101015105541336.jpg</src>\
				</img>\
				<img>\
					<text color="#ff0000">湖南卫视fafa费多少等发达</text>\
					<link>http://www.baidu.com</link>\
					<src>http://img.alimama.cn/bcrm/adboard/picture/2010-10-15/101015171314037.jpg</src>\
				</img>\
				<img>\
					<text color="#ff0000">小熊电饭煲各分公司本身</text>\
					<link>http://www.ngacn.cc</link>\
					<src>http://img.alimama.cn/bcrm/adboard/picture/2010-10-14/101014114129945.jpg</src>\
				</img>\
				<config switchtime="5000" />\
			</root>'
	}
});
	
jQuery.fn.extend ({
	loadfxImgSwacher: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.fxswacherSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/ImgSwacher.swf", completeFunc, header, "init");
		return this;
	}
})
})();


//文档阅读组件
(function (){
var header = "flexpaper";
jQuery.extend ({
	flexpaperSettings : {
		wmode: "window", 			// transparent
		flashvars: {SwfFile: ctxPath + '/demos/docs/demodocs.pdf_1.swf?{1,3}'}
	}
});
	
jQuery.fn.extend ({
	loadflexpaper: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.flexpaperSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/FlexPaperViewer.swf", completeFunc, header, "init");
		return this;
	}
})
})();

//基于adobe ams的音视频全功能组件
(function (){
var header = "amscomp";
jQuery.extend ({
	amscompSettings : {
		wmode: "window", // transparent
		videotoselecturl:"/modules/amsmulticastlive/AmsMulticastLiveController/getVideoSelectTreeChildren.vot",
		sid: "1347609630542", // 唯一标识当前打开的组件交互界面
		type: "livesend" // livereceive recordsend recordreceive videochathost videochatguest
	}
});

jQuery.fn.extend({
	loadAmsComp: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.amscompSettings, completeFunc);
		loadFlash (rt[0], this, ctxPath+"/controls/flash/AmsComp.swf", rt[1], header, "init");
		
		$(window).unload(function() {
			$.get(ctxPath + "/common/components/AmsCompController/removeChatUser.vot?sid=" + rt[0].sid);
		});
		
		return this;
	}
});
})();

// flash公式编辑器组件
(function (){
var header = "antelopeformulaeditor";
jQuery.extend ({
	antelopeformulaeditorSettings : {
		wmode: "window" // transparent
	}
});

jQuery.fn.extend({
	loadAntelopeFormulaEditor: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.antelopeformulaeditorSettings, completeFunc);
		loadFlash (rt[0], this, ctxPath+"/controls/flash/AntelopeFormulaEditor.swf", rt[1], header, "init");
		return this;
	},
	
	getFormulaLatex: function() {
		var objs = this.find ("[id^="+header+"]");
		if (objs.size ())
			return objs[0].getLatex ();
		return "";
	}
});
})();

//仪表盘组件
(function (){
var header = "flashinstpanel";
jQuery.extend ({
	fxchartSettings : {
		wmode: "window", 			// transparent
		radian: 180,	 			// 仪表盘角度
		range: [-10, 10], 			// 起始终止的取值范围
		largeGraduat:16, 			// 大刻度数
		smallGraduatPerLarge: 4,    // 大刻度间隔数
		startPoint:0				// 初始刻度
	}
});

jQuery.fn.extend ({
	loadfxInstPanelChart: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.fxchartSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/InstrumentPanel.swf", completeFunc, header, "init");
		return this;
	},
	fxSetValue : function (value) {
		return flashEach (this, header, function () {
			if (this.fxSetValue)
				this.fxSetValue (value);
		});
	}
});
})();


//通用flash 图表组件
(function (){
var header = "commonflashchart";
jQuery.extend ({
	jschartSettings : {
		wmode: "window" 			// transparent
	}
});

jQuery.fn.extend ({
	loadJsCommonChart: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.fxchartSettings, completeFunc);
		loadFlash (rt[0], this, ctxPath+"/controls/flash/flashchart.swf", rt[1], header, "init");
		//loadFlash (rt[0], this, ctxPath+"/controls/flash/JsChart.swf", rt[1], header, "init");
		return this;
	}, 
	
	commonChartReload: function (optsfuncname, data) {
		return flashEach (this, header, function () {
			if (this[optsfuncname])
				this[optsfuncname] (data);
		});
	},
	
	commonChartKeyReload: function (optsfuncname, key, value) {
		return flashEach (this, header, function () {
			if (this[optsfuncname])
				this[optsfuncname] (key, value);
		});
	},
	
	showJsCommonChartImage: function (ctxPath, callbackname, width, height) {
		return flashEach (this, header, function () {
			if (this.showJsCommonChartImage)
				this.showJsCommonChartImage (ctxPath, callbackname, width, height);
		});
	}
});
})();


//气象局用首页菜单
(function (){
var header = "qxjbtnbar";
jQuery.extend ({
	qxjbtnbarSettings : {
		wmode: "window", 			// transparent
		callbackName: null,
		qxjbtnSelected:0,
		showBothSidesRibbon: true,
		buttonInfos:[{label:'测试', sid:"dssd"}]
	}
});

//图片切换组件
(function (){
var header = "flashimgswatcher";
jQuery.extend ({
	fxswatcherSettings : {
	wmode: "window", 			// transparent
	delay:3000, // 切换延迟毫秒数
	imgs:[]
	}
});
	
jQuery.fn.extend ({
	loadfxImgSwatcher: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.fxswatcherSettings, completeFunc);
		loadFlash (rt[0], this, ctxPath+"/controls/flash/ImgSwatcher.swf", rt[1], header, "init");
		return this;
	}
})
})();

jQuery.fn.extend ({
	loadQxjbtnbar: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.qxjbtnbarSettings, completeFunc);
		rt[0].height = this.height();
		loadFlash (rt[0], this, ctxPath+"/controls/flash/FlexBtnBar.swf", rt[1], header, "init");
		return this;
	},
	resetButtonInfos: function (buttonInfos) {
		return flashEach (this, header, function () {
			if (this.resetButtonInfos)
				this.resetButtonInfos (buttonInfos);
		});
	},
	setQxjbtnSelected: function(selectedid) {
		return flashEach (this, header, function () {
			if (this.setQxjbtnSelected)
				this.setQxjbtnSelected (selectedid);
		});
	}
});
})();


//太阳能电池
(function (){
var header = "sunbattery";
jQuery.extend ({
	sunbatterySettings : {
		wmode: "transparent",			// transparent
		data: {
			a1 : 1,
			a2 : 3,
			b1 : 1,
			b2 : 3,
			c1 : 1,
			c2 : 2,
			d1 : 1,
			d2 : 3,
			d3 : 5,
			e1 : 1,
			e2 : 3,
			e3 : 5,
			f1 : 1,
			f2 : 1,
			battery : 45,
			amove : true,
			bmove : false,
			cmove : true,
			horizonmove : false,
			dmove : true,
			emove : true,
			fmove : true
		}, 
		width:150,
		left:5
	}
});

jQuery.fn.extend ({
	loadSunbattery: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.sunbatterySettings, completeFunc);
		loadFlash (rt[0], this, ctxPath+"/controls/flash/Room8.swf", rt[1], header, "init");
		return this;
	},
	reloadSunbatteryData: function (data) {
		return flashEach (this, header, function () {
			if (this.reloadSunbatteryData)
				this.reloadSunbatteryData (data);
		});
	}
});
})();


//人民金行
(function (){
var header = "goldshop";
jQuery.extend ({
	goldShopSettings : {
		wmode: "window",			// transparent
		flashvars: {path:'/ecm2/controls/flash/pictoload.jpg'},
		centerpicinfo:[{
			src: '/ecm2/controls/flash/centersmallpic.png',
			text: '<p><span size="24px"><font size="24px"><b>地方萨芬的萨芬三大1</b></font></span></p>\
<p><span style="font-size: small;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 范德萨反对反对撒</span></p>\
<p><span style="font-size: small;">范德<em>萨范德萨分</em>盛大<br />范德萨分三大<br />十分大方三大地方<span style="text-decoration: underline;">萨芬是大四方达</span>风尚大典飞洒</span></p>'	
		},{
			src: '/ecm2/controls/flash/centersmallpic2.png',
			text: "<p><font size='15' font-weight='bold'>地方地方</font></p><p>dsfsd</p>"
		},{
			src: '/ecm2/controls/flash/centersmallpic2.png',
			text: "<p>sdfd</p><p>dsfsd</p>"
		}],
		stories: ['<p><span size="24px"><font size="24px"><b>地方萨芬的萨芬三大1</b></font></span></p>\
		          <p><span style="font-size: small;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 发大水法大赛范德萨反对反对撒</span></p>\
		          <p><span style="font-size: small;">范德<em>萨范德萨分</em>盛大<br />范德萨分三大<br />十分大方三大地方<span style="text-decoration: underline;">萨芬是大四方达</span>风尚大典飞洒</span></p>'
		          ,
		          '<p><span size="24px"><font size="24px"><b>地方萨芬的萨芬三大</b></font></span></p>\
		          <p><span style="font-size: small;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 范德萨反111范德萨对反对撒</span></p>\
		          <p><span style="font-size: small;">范德<em>萨范德萨分</em>盛大<br />范德萨分三大<br />十分大方三大地方<span style="text-decoration: underline;">萨芬是大四方达</span>风尚大典飞洒</span></p>'
		          ,
		          '<p><span size="24px"><font size="24px" color="#ff0000"><b>地方萨芬的萨芬三大</b></font></span></p>\
		          <p><span style="font-size: small;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 范德萨反对4324反对撒</span></p>\
		          <p><span style="font-size: small;">范德<em>萨范德萨分</em>盛大爱爱爱<br />范德萨分三大<br />十分大方三大地方<span style="text-decoration: underline;">萨芬是大四方达</span>风尚大典飞洒</span></p>'
		          ,
		          '<p><span size="24px"><font size="24px"><b>地方萨芬的萨芬三大</b></font></span></p>\
		          <p><span style="font-size: small;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 范德萨反对反对撒</span></p>\
		          <p><span style="font-size: small;">范德<em>萨范德萨分</em>盛大333 <br />范德萨分三大<br />十分大方三大地方<span style="text-decoration: underline;">萨芬是大四方达</span>风尚大典飞洒</span></p>'
		          ]
	}
});

jQuery.fn.extend ({
	loadGoldShop: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.goldShopSettings, completeFunc);
		loadFlash (rt[0], this, ctxPath+"/controls/flash/GoldShop.swf", rt[1], header, "init");
		return this;
	}
});
})();

// 政府采购柱形图组件
(function (){
var header = "fxflashcolchart";
jQuery.extend ({
	fxcolumnchartSettings : {
		wmode: "window", 			// transparent
		axislabelfunc: "jQuery.noop",
		xmlinfo:'<root>\
					<set>\
						<series>\
							<item data="1487665" label="经济业务" datalabel="50000"/>\
							<item data="2487665" label="人力资源"/>\
							<item data="3487665" label="信息技术"/>\
						</series>\
						<series>\
							<item data="5712665" label="经济业务" datalabel="80000"/>\
							<item data="4712665" label="人力资源"/>\
							<item data="3712665" label="信息技术"/>\
						</series>\
					</set>\
					<set>\
						<series>\
							<item data="1651733" label="经济业务"/>\
							<item data="1651733" label="人力资源"/>\
							<item data="1651733" label="信息技术"/>\
						</series>\
						<series>\
							<item data="1766733" label="经济业务"/>\
							<item data="1766733" label="人力资源"/>\
							<item data="1766733" label="信息技术"/>\
						</series>\
					</set>\
					<set>\
						<series>\
							<item data="2651733" label="经济业务"/>\
							<item data="1851733" label="人力资源"/>\
							<item data="1951733" label="信息技术"/>\
						</series>\
						<series>\
							<item data="3766733" label="经济业务"/>\
							<item data="3766733" label="人力资源"/>\
							<item data="3766733" label="信息技术"/>\
						</series>\
					</set>\
				</root>'
	}
});

jQuery.fn.extend ({
	loadfxColumnChart: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.fxcolumnchartSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/FxColumnChart.swf", completeFunc, header, "init");
		return this;
	}
});
})();

// 颜色选择组件  单加载类型控件
(function (){
var header = "fxcolorpicker";
jQuery.extend ({
	fxcolorpickersSettings : {
		wmode: "transparent", // transparent
		selectedColor: 0xff0000,
		change: "jQuery.noop",
		id: ""
	}
});
jQuery.fn.extend ({
	loadFxColorPicker: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.fxcolorpickersSettings, completeFunc);
		s = rt[0];
		var time = now() + Math.floor(Math.random()*100000);
		var showrangfuncname = "jQuery.fxshowcolorpicker"+time;
		var hiderangfuncname = "jQuery.fxhidecolorpicker"+time;
		s.showSelectRange = showrangfuncname;
		s.hodeSelectRange = hiderangfuncname;
		eval(showrangfuncname+"=function(){"+
				"$('#"+time+"').css({width:250,height:200});"
				+"}");
		eval(hiderangfuncname+"=function(){"+
				"$('#"+time+"').css({width:30,height:30});"
				+"}");
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/FxColorPicker.swf", completeFunc, header, "init");
		$(this).wrapInner("<div id='"+time+"' style='width:30px; height: 30px; position: absolute; z-index: 100'/>");
		return this;
	},

	getFxCPCurrColor: function () {
		var objs = this.find ("[id^="+header+"]");
		if (objs.size ())
			return objs[0].getCurrColor ();
		return 0;
	}
});
})();

// 矩阵树形显示控件
(function (){
var header = "matrixtree";
jQuery.extend ({
	matrixtreeSettings : {
		wmode: "window", // transparent direct
		matrixlineurl:null, // 必填，业务条线获取url
		matrixitemurl:null, // 必填，业务项获取url
		itemdetailurl:null  // 必填，业务项明细信息url
	}
});
jQuery.fn.extend ({
	loadMatrixTree: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.matrixtreeSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/MatrixViewer.swf", completeFunc, header, "init");
		return this;
	},
	
	searchBusiItemHandler: function (keytext) {
		return flashEach (this, header, function () {
			if (this.searchBusiItemHandler)
				this.searchBusiItemHandler (keytext);
		});
	}

});
})();

// antelope音视频播放器组件
(function (){
var header = "antelopeplayer";
jQuery.extend ({
	antelopeplayerSettings : {
		// {flashvars:{"src":ctx + "/demos/assets/mmn.mp4", playermode:"audio"}}
		wmode: "window" // transparent direct
	}
});
jQuery.fn.extend ({
	loadAntelopePlayer: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.antelopeplayerSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/AntelopePlayer.swf", completeFunc, header, "init");
		return this;
	},

	getCurrPlayedTime: function () {
		var objs = this.find ("[id^="+header+"]");
		if (objs.size ())
			return objs[0].getCurrPlayedTime ();
		return 0;
	},
	getCurrSelectedRangeTime: function () {
		var objs = this.find ("[id^="+header+"]");
		if (objs.size ())
			return objs[0].getCurrSelectedRangeTime ();
		return 0;
	}
});
})();


// 图表组件 单加载类型控件
(function (){
var header = "fxchartcol";
jQuery.extend ({
	fxChartSettings : {
		wmode: "window", // transparent
		chartjson: null,
		mode: "edit" // edit
	}
});
jQuery.fn.extend ({
	loadFxChart: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.fxChartSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		var brname = "br"+Math.floor(Math.random()*100000+now());
		s.flashvars = {bridgeName:brname};
		loadFlash (s, this, ctxPath+"/controls/flash/FxChart.swf", function(){
			if (s.mode == 'view') { // 观看交互模式与编辑图表模式不同
				(function initChart() {
					if (FABridge && FABridge[s.flashvars.bridgeName] && FABridge[s.flashvars.bridgeName].create) {
						fab = FABridge[s.flashvars.bridgeName].create("fx.chart.Fab");
						if (!fab) {
							setTimeout(initChart, 13);
							return;
						}
						initChartByJSON(fab, s.chartjson);
						completeFunc(fab, chart);
						return;
					}
					setTimeout(initChart, 13);
				})();
			}
		}, header, "init");
		return this;
	}
});

var fabcreator;
var chart;
var chartPartCreator;
var styleCreator;
var effectCreator;

var objectCache = [];

var creatMap = {
	"chart":"fabcreator.create",
	"annotationElements":"chartPartCreator.createGridLines",
	"backgroundElements":"chartPartCreator.createGridLines",
	"horizontalAlternateFill":"styleCreator.createFill",
	"horizontalFill":"styleCreator.createFill",
	"verticalFill":"styleCreator.createFill",
	"verticalAlternateFill":"styleCreator.createFill",
	"horizontalStroke":"styleCreator.createStroke",
	"series":"chartPartCreator.createSeries",
	"showDataEffect":"effectCreator.createSeriesEffect",
	"hideDataEffect":"effectCreator.createSeriesEffect",
	"areaFill":"styleCreator.createFill",
	"entries":"styleCreator.createGradientEntry",
	"horizontalAxisRenderers":"chartPartCreator.createAxisRenderer",
	"horizontalAxis":"chartPartCreator.createAxis",
	"verticalAxisRenderers":"chartPartCreator.createAxisRenderer",
	"verticalAxis":"chartPartCreator.createAxis",
	"axis":"chartPartCreator.createAxis",
	"axisStroke":"styleCreator.createStroke",
	"minorTickStroke":"styleCreator.createStroke",
	"tickStroke":"styleCreator.createStroke",
	"radiusAxis":"chartPartCreator.createAxis",
	"declineFill":"styleCreator.createFill",
	"boxStroke":"styleCreator.createStroke",
	"closeTickStroke":"styleCreator.createStroke",
	"openTickStroke":"styleCreator.createStroke",
	"stroke":"styleCreator.createStroke",
	"calloutStroke":"styleCreator.createStroke",
	"radialStroke":"styleCreator.createStroke"
	
		
	
	/* "horizontalAxis":"chartPartCreator.createAxis",
	"":"chartPartCreator.createAxisRenderer",
	"":"chartPartCreator.createTitleRenderer",
	"":"chartPartCreator.createSeries",
	"":"chartPartCreator.createGridLines",
	"":"styleCreator.createFill",
	"":"styleCreator.createFilter",
	"":"styleCreator.createStroke",
	"":"effectCreator.createSeriesEffect"
	*/
}

function initChartByJSON(fab, json) {
	if (fab == null || json == null)
		return;
	fabcreator = fab;
	creatorFactory = fab.getCreatorFactory(json.chartType);
	chartPartCreator = creatorFactory.getChartPart();
	styleCreator = creatorFactory.getStyleCreator();
	effectCreator = creatorFactory.getEffectCreator();
	var app = fab.getApp();
	chart = getReadyObject("chart", json)
	app.addElement(chart);
}

function getReadyObject(propname, propvalue) {
	if (!jQuery.isPlainObject(propvalue) && !jQuery.isArray(propvalue)) {
		return propvalue;
	}
	
	// 若对象标识为同一个，则取缓存对象
	if (objectCache[propvalue.objid] != null)
		return objectCache[propvalue.objid];
	
	// dataProvider为不定数据
	if (propname == "dataProvider") {
		return propvalue;
	}
	
	var createFunc = creatMap[propname];
	var obj;
	if (propvalue.objType) {
		eval("obj="+createFunc+"('"+propvalue.objType+"')");
	} else {
		eval("obj="+createFunc+"()");
	}
	
	for (var p in propvalue) {
		if (p == 'objType' || p == 'objid') // 排除对象标识
			continue;
		
		if (p == "style") {// 样式属性
			for (var s in propvalue[p]) {
				setPropOrStyle(obj, s, propvalue[p][s], true);
			}
			continue;
		}
		
		setPropOrStyle(obj, p, propvalue[p], false);
	}
	
	objectCache[propvalue.objid] = obj;// 缓存对象
	return obj;
}

function setPropOrStyle(obj, prop, propvalue, isstyle) {
	var newprop = propvalue;
	if (jQuery.isPlainObject(propvalue)) {
		newprop = getReadyObject(prop, propvalue);
	}
	
	if (jQuery.isArray(propvalue)) {
		newprop = getReadyArray(prop, propvalue);
	}
	
	if (isstyle) {
		obj.setStyle(prop, newprop);
	} else {
		if (prop == "dataProvider") {
			setTimeout(function(){ // dataProvider对象出效果
				obj[camel(prop)](newprop);
			}, 13);
			return;
		}
		
		obj[camel(prop)](newprop);
	}
}

function getReadyArray(propname, array) {
	var objarr = [];
	for (var i = 0; i < array.length; i++) {
		objarr.push(getReadyObject(propname, array[i]));
	}
	return objarr;
}

function camel(propName) {
	return "set" + propName.charAt(0).toUpperCase() + propName.substr(1);
}

})();



// 数据地图组件
(function (){
var header = "datamap";
jQuery.extend ({
	dataMapSettings : {
		wmode: "window", // transparent
		linkcallback: null,
		gridcallback: null,
		onregionclick:null,
		showWorld: false,
		showDateRegion: true,
		openSmallRegion: true,
		openBigProvince: true,
		smallProvRollOverColor:0xffff00,
		smallProvinceclick: null,
		datasources:[],
		readRegionOrgRelateUrl: null,
		county: null,
		province:null,
		highlightxml: null,
			// 演示使用高亮显示问题区域 
		/*'<root color="0xff0000">\
			<p441700 color="0x00ff00"/>\
			<p441800/>\
			<p430000 color="0xff7373"/>\
			<p110000 color="0xff5353"/>\
			<p140000 color="0xffa3a3"/>\
			<p440000/>\
			<p450000 color="0xff9393"/>\
			<p470000/>\
			<p441600/>\
			</root>',*/
		fontxml:'<font bold="true"/>',
		uixml: '<datamap>\
					<areainfos>\
						<p130000>\
							<header>\
								<textformat leading="5"><b><font size="15">石家庄筹建营业部</font></b><br/>\
									<u> <a href="event:需要设置的相关信息" target="_blank"><b><font size="13" color="#06639e">54次审查</font></b></a></u><br/>\
									<b><font size="13" color="#06639e">在初审时出现问题：31个</font></b><br/>\
									<b><font size="13" color="#06639e">在复核时出现问题：20个</font></b><br/>\
								</textformat>\
							</header>\
							<datagrid height="200">\
								<flatdata>\
									<col width="80" datafield="code" label="审查标题"/>\
									<col width="100" datafield="unitname" label="审查内容"/>\
									<col width="100" datafield="unitcode" label="审查人"/>\
									<col width="100" datafield="region" label="审查单位"/>\
									<col width="100" datafield="reexamperson" label="复核人"/>\
									<col width="100" datafield="reexamdept" label="复核单位"/>\
									<data>\
										<code>营销管理</code>\
										<unitname>交易管理-风险揭示</unitname>\
										<unitcode>张三</unitcode>\
										<region>经纪事业部</region>\
										<reexamperson>李四</reexamperson>\
										<reexamdept>投资银行事业部</reexamdept>\
									</data>\
									<data>\
										<code>营销管理</code>\
										<unitname>交易管理-风险揭示</unitname>\
										<unitcode>张三</unitcode>\
										<region>经纪事业部</region>\
										<reexamperson>李四</reexamperson>\
										<reexamdept>投资银行事业部</reexamdept>\
									</data>\
									<data>\
										<code>营销管理</code>\
										<unitname>交易管理-风险揭示</unitname>\
										<unitcode>张三</unitcode>\
										<region>经纪事业部</region>\
										<reexamperson>李四</reexamperson>\
										<reexamdept>投资银行事业部</reexamdept>\
									</data>\
								</flatdata>\
							</datagrid>\
						</p130000>\
					</areainfos>\
				</datamap>'
	}
});
jQuery.fn.extend ({
	loadDataMap: function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.dataMapSettings, completeFunc);
		var s = rt[0];
		s.ctxPath = ctxPath;
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/Map.swf", completeFunc, header, "init");
		return this;
	}
});
})();

/*图片辅助导出方法*/
jQuery.extend ({
	flashExportImgFunc: function(data) {
		$.postIframe(ctxPath + "/common/DataMapController/exportFlashImg.vot?imgname=" + data);
	}
});

//rpc基于发布订阅通信工具
(function (){
var header = "rpctool";
jQuery.extend ({
	rpctoolSettings : {
		wmode : "window" // transparent
	},
	
	loadRpctool : function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
		var rt = settingsFn (origSettings, jQuery.rpctoolSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/rpc.swf", completeFunc, header, "init");
		
		return this;
	},
	
	addDestination : function (dest) {
		callToolFunc( header, "rpc", "addDestination", arguments );
		return this;
	},
	
	subscribe: function(callback) {
		var tool = document.getElementById( header );
		if (!tool) {
			alert("请先加载rpc工具！");
			return;
		}
		if (callback) {
			var funcName = '$.callbackname' + parseInt (""+(Math.random()*100000+now()));
			eval (funcName+'=callback');
			tool.subscribe( funcName );
		} else
			tool.subscribe( null );
		return this;
	},
	
	unsubscribe: function( callback, preserveDurable ) {
		preserveDurable = !!preserveDurable;
		var tool = document.getElementById( header );
		if (!tool) {
			alert("请先加载rpc工具！");
			return;
		}
		if (callback) {
			var funcName = '$.callbackname' + parseInt (""+(Math.random()*100000+now()));
			eval (funcName+'=callback');
			tool.unsubscribe( funcName, preserveDurable );
		} else
			tool.unsubscribe(null, preserveDurable);
		return this;
	},
	
	sendMessage: function( dest, message ) {
		callToolFunc( header, "rpc", "sendMessage", arguments );
		return this;
	},
	
	addMessageCallback: function( dest, callback ) {
		var tool = document.getElementById( header );
		if (!tool) {
			alert("请先加载rpc工具！");
			return;
		}
		var funcName = '$.callbackname' + parseInt (""+(Math.random()*100000+now()));
		eval (funcName+'=callback');
		tool.addMessageCallBack( dest, funcName );
	}
});
})();

// fileUpDownloader 上传下载工具
(function (){
var header = "fileupdownloader";
jQuery.extend ({
	fileUpDownloaderSettings : {
		wmode : "window", // transparent
		single : false,
		resumeEnabled: false,
		htmlText : null,
		type : "upload", // download
		extension : null,
		append : false
	},
	downloadSettings : {
		url : null,
		defaultFileName : null,
		complete : jQuery.noop,
		open : jQuery.noop
	}
});
	
jQuery.fn.extend ({
	
	/**
	 * @name loadFileUpDownloader(options, [callback])
	 * @desc 加载文件上传下载组件按钮
	 * @longdesc 在所有选中的dom节点中加载文件上传下载组件按钮
	 * 
	 * @example 在div中添加上传下载组件按钮
	 * 		@html <div></div>
	 * 		@jquery $("div").loadFileUpDownloader ();
	 * 		@result <div>浏览 </div>
	 * @end
	 * 
	 * @example 为按钮添加样式,采用多文件上传模式并添加回调函数
	 * 		@html <div></div>
	 * 		@jquery $("div").loadFileUpDownloader ({
	 * 					htmlText : '<font color="#ff0000"><i><b>选择文件</b></i></font>',
	 * 					single:false
	 * 				}, function () {
	 * 					alert ('回调');
	 * 				});
	 * 		@result <div>(此位置为红色斜体加粗字样的‘选择文件’按钮字样) </div>
	 * @end
	 * 
	 * @param callback (Function) : (可选) flash完成加载后的回调函数 @end
	 * @param options (Options) : (可选) 一组初始化时所需选项
	 * 		@option wmode (String) : (默认值: "window")(可用值: "window","transparent")  设置初始化时主图形窗口是否透明
	 * 		@option single (Boolean) : (默认值: true) 设置是否为单文件选择按钮（当点击弹出文件选择框后，只能选择单个文件)
	 * 		@option htmlText (String) : (默认值: null) 设置按钮显示样式，可用一些html代码改变按钮样式。
	 * 		@option type (String) : (默认值: upload)(可用值: "upload","download") 设置按钮类型，为上传按钮还是下载按钮。
	 * 		@option extension (String) : (默认值: null)(可用值: "*.txt;*.ppt"等) 设置文件过滤类型，默认允许全部文件，按照相应格式设置后按照相应格式过滤文件。
	 * @end
	 * 
	 * @return jQuery
	 */
	loadFileUpDownloader : function (origSettings, completeFunc) {
		
		
		if (!testPlayerVersion())
			return;
		
		var rt = settingsFn (origSettings, jQuery.fileUpDownloaderSettings, completeFunc);
		var s = rt[0];
		
		this.data("setting", s);
		
		completeFunc = rt[1];
		loadFlash (s, this, ctxPath+"/controls/flash/Controls.swf", completeFunc, header, "init");
		
		if (s.resumeEnabled) {
			var thisObj = this;
			$("<div>" + s.htmlText + "</div>").css("cursor","pointer").click(function(){
				var fileselectedpath = $(thisObj).find("object")[0].selectFile();
				var fileid = $(thisObj).find("object")[0].getFileId();
				if (fileselectedpath) {
					$.get(ctx + "/upload/getUploadFilePosition.vot?fileselectedpath=" + encodeURIComponent(encodeURIComponent(fileselectedpath)) + "&fileid=" + fileid, function(size) {
						$(thisObj).find("object")[0].SeekBeginPosition(parseInt(size, 10));
					});
				}
				
			}).appendTo(this);
		}
		
		return this;
	},
	
	/**
	 * @name selectFile(callback)
	 * @desc 添加文件选择监听
	 * @longdesc 当用户打开文件选择框，选择完文件点击选择按钮时触发
	 * 
	 * @example 在div包含的对应组件中添加监听，并提示选中文件信息
	 * 		@jquery $("div").selectFile (function (fileinfolist) {alert (fileinfolist);});
	 * @end
	 * @param callback (Function) : 点击选择按钮时触发的事件 @end
	 * @return jQuery
	 */
	selectFile : function (callback) {
		
		var thisObj3 = this;
		if (thisObj3.data("setting").resumeEnabled) {
			
			function processChange3() {
				
				if (thisObj3.find("object")[0].isFileSelected()) {
					thisObj3.find("object")[0].setSelectFile(0);
					var fileid = $(thisObj3).find("object")[0].getFileId();
					callback([{id:fileid, bytesLoaded:thisObj3.find("object")[0].GetBytesLoaded(), size:thisObj3.find("object")[0].GetSize(), bytesTotal:thisObj3.find("object")[0].GetSize()}]); 
				}
				
				setTimeout(processChange3, 1000);
			}
			processChange3();
			return thisObj3;
		}
		
		return flashEach (this, header, function () {
			addFlashCallback (this, callback, "addSelectCallBack");
		});
	},
	/**
	 * @name progress(callback)
	 * @desc 添加文件上传下载进度监控监听
	 * @longdesc 文件上传或下载过程中不定期触发，以监控上传或下载进度
	 * 
	 * @example 在div包含的对应组件中添加过程监听，并获得已下载百分比
	 * 		@jquery $("div").progress (function (info) {alert (info.bytesLoaded / info.bytesTotal)});
	 * @end
	 * @param callback (Function) : 点击选择按钮时触发的事件 @end
	 * @return jQuery
	 */
	progress : function (callback) {
		var thisObj2 = this;
		if (thisObj2.data("setting").resumeEnabled) {
			
			function processChange() {
				
				if (!thisObj2.find("object")[0].IsComplete()) {
					setTimeout(processChange, 1000);
				}
				var fileid = $(thisObj2).find("object")[0].getFileId();
				callback({id:fileid, bytesLoaded:thisObj2.find("object")[0].GetBytesLoaded(), size:thisObj2.find("object")[0].GetSize(), bytesTotal:thisObj2.find("object")[0].GetSize()}); 
			}
			processChange();
			return thisObj2;
		}
		
		return flashEach (this, header, function () {
			addFlashCallback (this, callback, "progress");
		});
	},
	/**
	 * @name upload(url, [uploadids], [uploadDataFieldName])
	 * @desc 触发文件上传操作
	 * @longdesc 触发文件上传操作
	 * 
	 * @example 上传div包含的对应组件中所关联的所有文件
	 * 		@jquery $("div").upload("fileupdownloader.jsp");
	 * @end
	 * @example 上传div包含的对应组件中所关联的部分id组件并修改文件数据域名
	 * 		@jquery $("div").upload("fileupdownloader.jsp",['id1','id2'],'dataform');
	 * @end
	 * @param url (String) : 所要上传到的地址（必须为相同域之内） @end
	 * @param uploadids (Array) : (可选) 需要上传的文件id数组(id在selectFile回调函数中回传时指定) 若不传入则视为与改按钮相关联所有文件全部上传 @end
	 * @param uploadDataFieldName (String) : (可选) 服务器端数据域名称,java为getParameter时的参数 @end
	 * @return jQuery
	 */
	upload : function (url, uploadids, uploadDataFieldName) {
		
		if (this.data("setting").resumeEnabled) {
			this.find("object")[0].upload(url);
			return this;
		}

		return flashEach (this, header, function () {
			if (this.upload)
				this.upload (url, uploadids, uploadDataFieldName);
		});
	},
	
	/**
	 * @name uploadComplete(callback)
	 * @desc 添加文件上传完成回调
	 * @longdesc 为对应组件添加上传完成回调函数 
	 * 
	 * @example 对div包含的对应组件添加完成时回调，并提示信息
	 * 		@jquery $("div").uploadComplete(function (info){
	 * 				alert ("id为"+info.id+"的文件:"+info.name+"上传完成！");
	 * 				});
	 * @end
	 * @param callback (Function) : 回调函数 @end
	 * @return jQuery
	 */
	uploadComplete : function (callback) {
		
		var thisObj2 = this;
		if (thisObj2.data("setting").resumeEnabled) {
			
			function processChange2() {
				
				if (!thisObj2.find("object")[0].IsComplete()) {
					setTimeout(processChange2, 1000);
				} else {
					callback({bytesLoaded:thisObj2.find("object")[0].GetBytesLoaded(), size:thisObj2.find("object")[0].GetSize(), bytesTotal:thisObj2.find("object")[0].GetSize()}); 
				}
			}
			processChange2();
			return thisObj2;
		}
		
		return flashEach (this, header, function () {
			addFlashCallback (this, callback, "addCompleteListener");
		});
	},
	
	/**
	 * @name uploadFail(callback)
	 * @desc 添加文件上传完成回调
	 * @longdesc 为对应组件添加上传完成回调函数 
	 * 
	 * @example 对div包含的对应组件添加完成时回调，并提示信息
	 * 		@jquery $("div").uploadComplete(function (info){
	 * 				alert ("id为"+info.id+"的文件:"+info.name+"上传完成！");
	 * 				});
	 * @end
	 * @param callback (Function) : 回调函数 @end
	 * @return jQuery
	 */
	uploadFail : function (callback) {
		return flashEach (this, header, function () {
			addFlashCallback (this, callback, "ioerror");
		});
	},
	
	/**
	 * @name download(downloadSettings)
	 * @desc 设置下载之前与下载相关的信息
	 * @longdesc 设置下载之前与下载相关的信息到组件当中，如url地址，默认的文件名称，下载开始时以及完成时的回调函数
	 * @example 为div包含的对应组件添加下载文件url路径
	 * 		@jquery $("div").download({url:'file.zip'});
	 * @end
	 * @example 为div包含的对应组件添加下载文件url路径及开始结束回调
	 * 		@jquery $("div").download({url:'file.zip', open:function(info){}, complete:function(info){}});
	 * @end
	 * @param downloadSettings (Options) : 一组初始化时所需选项
	 * 		@option url (String) : 下载时所需要的文件对应url地址
	 * 		@option defaultFileName (String) : (可选) 文件下载的默认名称，可以单独设置成与服务器端不同
	 * 		@option complete (Function) : (可选) 下载完成时的回调函数（只win7测试通过）
	 * 		@option open (Function) : (可选) 下载开始时的回调函数（只win7测试通过） @end
	 * @return jQuery
	 */
	download : function (origSettings) {
		var s = jQuery.extend (true, {}, jQuery.downloadSettings, origSettings);
		return flashEach (this, header, function () {
			addFlashCallback (this, s.complete, "addCompleteListener");
			addFlashCallback (this, s.open, "addOpenListener");
			if (this.setDownloadInfo)
				this.setDownloadInfo (s);
		});
	},
	
	/**
	 * @name cancel(fileids)
	 * @desc 取消文件的上传或下载
	 * @longdesc 取消当前正在下载文件，后台会报数据中断传输相关异常
	 * @example 取消div包含的对应组件中所有文件的上传或下载
	 * 		@jquery $("div").cancel();
	 * @end
	 * @example 取消div包含的对应组件中指定文件的上传或下载
	 * 		@jquery $("div").cancel(['id1','id2']);
	 * @end
	 * @param fileids (Array) : (可选) 需要终止上传或下载的文件id数组 @end
	 * @return jQuery
	 */
	cancel : function (downloadids) {
		return flashEach (this, header, function () {
			if (this.cancel)
				this.cancel (downloadids);
		});
	},
	
	removefiles : function (removefileids) {
		return flashEach (this, header, function () {
			if (this.removefiles)
				this.removefiles (removefileids);
		});
	}
});
})();

// 工作流
(function () {
var header = "jiuqiworkflow";
	
jQuery.extend ({
	workFlowSettings : {
		wmode : "window", // transparent
		toolbarAlign : 'left', // right
		status : "edit",//edit view view_with_nodetoolbar_contextmenu
		uixml : '<workflow>\
				<delcallback>delcallback</delcallback>\
				<deletedNodes></deletedNodes>\
				<toolbar>\
					<toolbarbtn iconindex="0">\
						<title>起点</title>\
						<content>默认显示</content>\
						<color>0x00ffff</color>\
						<diamond>false</diamond>\
						<needicon>true</needicon>\
						<nodetoolbar>\
							<toolbarbtn>\
								<type>line</type>\
								<color>0x9999FF</color>\
								<content>新建线条</content>\
								<dash>true</dash>\
								<contextmenu>\
									<menu>\
										<title>配置转移条件</title>\
										<callback>configureFormula</callback>\
									</menu>\
						        </contextmenu>\
							</toolbarbtn>\
							<toolbarbtn>\
								<type>detail</type>\
								<callback>mydetail</callback>\
							</toolbarbtn>\
						</nodetoolbar>\
						<paras iconindex="0">\
						    <baseinfo>\
						       <nodeName/>\
						       <nodeCode/>\
						       <nodeType>0</nodeType>\
						       <nodeTypeTitle>起点</nodeTypeTitle>\
						       <nodeStatus/>\
						       <isAudit>0</isAudit>\
						    </baseinfo>\
						</paras>\
						<contextmenu>\
							<menu>\
								<title>配置节点信息</title>\
								<callback>mydetail</callback>\
							</menu>\
						</contextmenu>\
					</toolbarbtn>\
					<toolbarbtn iconindex="1">\
						<title>手工节点</title>\
						<content>新建手工节点</content>\
						<color>0x00ff00</color>\
						<diamond>false</diamond>\
						<nodes rotation="90" distance="0" duration="800">\
							<node iconindex="1" content="手工节点"/>\
						</nodes>\
						<nodetoolbar>\
							<toolbarbtn>\
								<type>line</type>\
								<color>0x9999ff</color>\
								<content>新建线条</content>\
								<dash>false</dash>\
								<contextmenu>\
									<menu>\
										<title>配置转移条件</title>\
										<callback>configureFormula</callback>\
									</menu>\
						        </contextmenu>\
							</toolbarbtn>\
							<toolbarbtn>\
								<type>detail</type>\
								<callback>mydetail</callback>\
							</toolbarbtn>\
						</nodetoolbar>\
						<paras iconindex="1">\
						    <baseinfo>\
						       <nodeName/>\
						       <nodeCode/>\
						       <nodeType>1</nodeType>\
						       <nodeTypeTitle>手工节点</nodeTypeTitle>\
						       <nodeStatus/>\
						       <isAudit>0</isAudit>\
						    </baseinfo>\
						</paras>\
						<contextmenu>\
							<menu>\
								<title>配置节点信息</title>\
								<callback>mydetail</callback>\
							</menu>\
						</contextmenu>\
					</toolbarbtn>\
					<toolbarbtn iconindex="2">\
						<title>终点</title>\
						<content>新建结束节点</content>\
						<color>0x00ffff</color>\
						<diamond>false</diamond>\
						<nodes rotation="90" distance="0" duration="800">\
							<node iconindex="2" content="终点"/>\
						</nodes>\
						<nodetoolbar>\
							<toolbarbtn>\
								<type>detail</type>\
								<callback>mydetail</callback>\
							</toolbarbtn>\
						</nodetoolbar>\
						<paras iconindex="2">\
						    <baseinfo>\
						       <nodeName/>\
						       <nodeCode/>\
						       <nodeType>2</nodeType>\
						       <nodeTypeTitle>终点</nodeTypeTitle>\
						       <nodeStatus/>\
						       <isAudit>0</isAudit>\
						    </baseinfo>\
						</paras>\
						<contextmenu>\
							<menu>\
								<title>配置节点信息</title>\
								<callback>mydetail</callback>\
							</menu>\
						</contextmenu>\
					</toolbarbtn>\
					<toolbarbtn iconindex="3">\
						<title>或并行发散汇聚器</title>\
						<content>新建或并行发散汇聚器</content>\
						<color>0x00ffff</color>\
						<diamond>true</diamond>\
						<nodes rotation="90" distance="200" duration="1000">\
							<node iconindex="6" content="或并行汇聚器"  incolor="0xFF0000">\
							   <incontextmenu>\
								</incontextmenu>\
								<nodetoolbar>\
									<toolbarbtn>\
										<type>line</type>\
										<color>0x9999ff</color>\
										<content>新建线条</content>\
										<dash>false</dash>\
										<contextmenu>\
											<menu>\
												<title>配置转移条件</title>\
												<callback>configureFormula</callback>\
											</menu>\
										</contextmenu>\
									</toolbarbtn>\
									<toolbarbtn>\
									<type>detail</type>\
									<callback>mydetail</callback>\
									</toolbarbtn>\
								</nodetoolbar>\
							</node>\
							<node iconindex="5" content="或并行发散器"/>\
			<node iconindex="5" content="或并行发散器"/>\
			<node iconindex="5" content="或并行发散器"/>\
			<node iconindex="5" content="或并行发散器"/>\
			<node iconindex="5" content="或并行发散器"/>\
			<node iconindex="5" content="或并行发散器"/>\
			<node iconindex="5" content="或并行发散器"/>\
						</nodes>\
						<nodetoolbar>\
							<toolbarbtn>\
								<type>line</type>\
								<color>0xFF0000</color>\
								<content>新建线条</content>\
								<dash>false</dash>\
							</toolbarbtn>\
							<toolbarbtn>\
								<type>detail</type>\
								<callback>mydetail</callback>\
							</toolbarbtn>\
						</nodetoolbar>\
						<paras  iconindex="6">\
						   <baseinfo>\
						         <nodeName>或并行汇聚器</nodeName>\
						         <nodeCode/>\
						         <nodeType>6</nodeType>\
						         <nodeTypeTitle>或并行汇聚器</nodeTypeTitle>\
						         <nodeStatus></nodeStatus>\
						         <isAudit>0</isAudit>\
						   </baseinfo>\
						</paras>\
						<paras  iconindex="5">\
						   <baseinfo>\
						         <nodeName>或并行发散器</nodeName>\
						         <nodeCode/>\
						         <nodeType>5</nodeType>\
						         <nodeTypeTitle>或并行发散器</nodeTypeTitle>\
						         <nodeStatus></nodeStatus>\
						         <isAudit>0</isAudit>\
						   </baseinfo>\
						</paras>\
						<contextmenu>\
							<menu>\
								<title>配置节点信息</title>\
								<callback>mydetail</callback>\
							</menu>\
						</contextmenu>\
					</toolbarbtn>\
					<toolbarbtn iconindex="4">\
						<title>与并行发散汇聚器</title>\
						<content>新建与并行发散汇聚器</content>\
						<color>0x00ffff</color>\
						<diamond>true</diamond>\
						<nodes rotation="90" distance="100" duration="800">\
							<node iconindex="4" content="与并行汇聚器" incolor="0xff0000">\
							    <incontextmenu>\
								</incontextmenu>\
								<nodetoolbar>\
									<toolbarbtn>\
										<type>line</type>\
										<color>0x9999ff</color>\
										<content>新建线条</content>\
										<dash>false</dash>\
										<contextmenu>\
											<menu>\
												<title>配置转移条件</title>\
												<callback>configureFormula</callback>\
											</menu>\
										</contextmenu>\
									</toolbarbtn>\
									<toolbarbtn>\
										<type>detail</type>\
										<callback>mydetail</callback>\
									</toolbarbtn>\
								</nodetoolbar>\
							</node>\
							<node iconindex="3" content="与并行发散器"/>\
						</nodes>\
						<nodetoolbar>\
							<toolbarbtn>\
								<type>line</type>\
								<color>0xff0000</color>\
								<content>新建线条</content>\
								<dash>false</dash>\
							</toolbarbtn>\
							<toolbarbtn>\
								<type>detail</type>\
								<callback>mydetail</callback>\
							</toolbarbtn>\
						</nodetoolbar>\
						<paras  iconindex="4">\
						   <baseinfo>\
						         <nodeName>与并行汇聚器</nodeName>\
						         <nodeCode/>\
						         <nodeType>4</nodeType>\
						         <nodeTypeTitle>与并行汇聚器</nodeTypeTitle>\
						         <nodeStatus></nodeStatus>\
						         <isAudit>0</isAudit>\
						   </baseinfo>\
						</paras>\
						<paras  iconindex="3">\
						   <baseinfo>\
						         <nodeName>与并行发散器</nodeName>\
						         <nodeCode/>\
						         <nodeType>3</nodeType>\
						         <nodeTypeTitle>与并行发散器</nodeTypeTitle>\
						         <nodeStatus></nodeStatus>\
						         <isAudit>0</isAudit>\
						   </baseinfo>\
						</paras>\
						<contextmenu>\
							<menu>\
								<title>配置节点信息</title>\
								<callback>mydetail</callback>\
							</menu>\
						</contextmenu>\
					</toolbarbtn>\
				</toolbar>\
			</workflow>'
	},
	
	WorkFlowEvent : {
		MOUSE_DOWN : "__mouseDown",
		MOUSE_UP : "__mouseUp",
		CLICK : "__click",
		FOCUS_OUT : "__focusOut"
	}
});


jQuery.fn.extend ({
	
	/**
	 * @name loadWorkFlow(options, [callback])
	 * @desc 加载工作流
	 * @longdesc 在所有选中的dom节点中加载工作流图形界面
	 * 
	 * @example 在div中设置工作流界面
	 * 		@html <div></div>
	 * 		@jquery $("div").loadWorkFlow ();
	 * 		@result <div>(workflow flash)</div>
	 * @end
	 * 
	 * @param callback (Function) : (可选) flash完成加载后的回调函数 @end
	 * @param options (Options) : (可选) 一组初始化时所需选项
	 * 		@option wmode (String) : (默认值: "window")(可用值: "window","transparent")  设置初始化时主图形窗口是否透明
	 * 		@option toolbarAlign (String) : (默认值: "left")(可用值: "left","right") 设置工具条位置
	 * 		@option editable (Boolean) : (默认值: true) 设置是否可以编辑
	 * 		@option uixml (String) : (默认值: 查看workflow源文件) 设置图形界面样式以及其它相关参数的xml
	 * @end
	 * 
	 * @return jQuery
	 */
	loadWorkFlow : function (origSettings, completeFunc) {
		if (!testPlayerVersion())
			return;
	
		var rt = settingsFn(origSettings, jQuery.workFlowSettings, completeFunc);
		s = rt[0];
		completeFunc = rt[1];
		loadFlash (s, this,  ctxPath+"/controls/flash/WorkFlow.swf", completeFunc, header, "showWorkFlow");
		
		return this;
	},
	// 保存旧的 bind方法
	_bind : jQuery.fn.bind,
	
	/**
	 * @name bind(type, callback)
	 * @desc 为工作流绑定事件
	 * @longdesc 在工作流中绑定事件，可以是	jQuery.WorkFlowEvent : {
	 * 		MOUSE_DOWN : "__mouseDown",
	 *		MOUSE_UP : "__mouseUp",
	 *		FOCUS_OUT : "__focusOut"
	 *	} 中的一种
	 * 
	 * @example 在div中的工作流中绑定事件（必须为工作流相关事件）
	 * 		@jquery $("div").bind ($.WorkFlowEvent.CLICK, function (xmlinfo) {
	 * 					return xmlinfo;
	 *				});
	 * @end
	 * 
	 * @param type (String) : (可用值$.WorkFlowEvent.MOUSE_DOWN,$.WorkFlowEvent.MOUSE_UP,$.WorkFlowEvent.MOUSE_OUT,$.WorkFlowEvent.CLICK,) 工作流事件类型 @end
	 * @param callback (Function) : 事件回调函数，回传所点击时间对应的图形相关xml @end
	 * 
	 * @return jQuery
	 */
	bind : function (type, callback) {
		if (type && (jQuery.isPlainObject (type) || type.indexOf ("__") == -1)) {
			this._bind.apply( this, arguments );
		} else {
			return flashEach (this, header, function () {
				var funcName = '$.workflowevent' + parseInt (""+Math.random()*100000);
				eval (funcName+'=callback');
				this.bind (type.replace ("__", ""), funcName);
			});
		}
		return this;
	},
	
	/**
	 * @name graphicXml(xml)
	 * @desc 设置或获取工作流对应xml
	 * @longdesc 工作流将根据当前图形状态获取图形对应xml数据，或者当传入xml文本时，根据xml文本生成对应图形
	 * @example 设置对应div中的工作流图形
	 * 		@jquery $("div").graphicXml (workflowxmlvalue);
	 * @end
	 * @example 获取对应div中的工作流图形
	 * 		@jquery var xmlinfo = $("div").graphicXml ();
	 * @end
	 * @param xml (String) : (可选) 形成图形所需xml文本,若传入文本则工作流将根据文本生成相应图形，否则将返回当前所选工作流中第一个工作流中图形所形成的xml文本 @end
	 * @return jQuery (传入xml时) 或 String(未传入xml时)
	 */
	graphicXml : function (value) {
		var workflows = this.find ("[id^="+header+"]");
		if (value === undefined) {
			if (workflows.size ())
				return workflows[0].getGraphicXmlInfo ();
		} else {
			workflows.each (function () {
				this.setGraphicXmlInfo (value);
			});
		}
		return this;
	},
	
	clickWorkNode : function(id) {
		var workflows = this.find ("[id^=jiuqiworkflow]");
		workflows.each (function () {
			this.clickItem (id);
		});
		return this;
	}
});
})();




/**
 * json2内容，非controls接口内容
 */

if (!this.JSON) {
    JSON = {};
}
(function () {

    function f(n) {
        // Format integers to have at least two digits.
        return n < 10 ? '0' + n : n;
    }

    if (typeof Date.prototype.toJSON !== 'function') {

        Date.prototype.toJSON = function (key) {

            return this.getUTCFullYear()   + '-' +
                 f(this.getUTCMonth() + 1) + '-' +
                 f(this.getUTCDate())      + 'T' +
                 f(this.getUTCHours())     + ':' +
                 f(this.getUTCMinutes())   + ':' +
                 f(this.getUTCSeconds())   + 'Z';
        };

        String.prototype.toJSON =
        Number.prototype.toJSON =
        Boolean.prototype.toJSON = function (key) {
            return this.valueOf();
        };
    }

    var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        gap,
        indent,
        meta = {    // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        },
        rep;


    function quote(string) {

// If the string contains no control characters, no quote characters, and no
// backslash characters, then we can safely slap some quotes around it.
// Otherwise we must also replace the offending characters with safe escape
// sequences.

        escapable.lastIndex = 0;
        return escapable.test(string) ?
            '"' + string.replace(escapable, function (a) {
                var c = meta[a];
                return typeof c === 'string' ? c :
                    '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
            }) + '"' :
            '"' + string + '"';
    }


    function str(key, holder) {

// Produce a string from holder[key].

        var i,          // The loop counter.
            k,          // The member key.
            v,          // The member value.
            length,
            mind = gap,
            partial,
            value = holder[key];

// If the value has a toJSON method, call it to obtain a replacement value.

        if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }

// If we were called with a replacer function, then call the replacer to
// obtain a replacement value.

        if (typeof rep === 'function') {
            value = rep.call(holder, key, value);
        }

// What happens next depends on the value's type.

        switch (typeof value) {
        case 'string':
            return quote(value);

        case 'number':

// JSON numbers must be finite. Encode non-finite numbers as null.

            return isFinite(value) ? String(value) : 'null';

        case 'boolean':
        case 'null':

// If the value is a boolean or null, convert it to a string. Note:
// typeof null does not produce 'null'. The case is included here in
// the remote chance that this gets fixed someday.

            return String(value);

// If the type is 'object', we might be dealing with an object or an array or
// null.

        case 'object':

// Due to a specification blunder in ECMAScript, typeof null is 'object',
// so watch out for that case.

            if (!value) {
                return 'null';
            }

// Make an array to hold the partial results of stringifying this object value.

            gap += indent;
            partial = [];

// Is the value an array?

            if (Object.prototype.toString.apply(value) === '[object Array]') {

// The value is an array. Stringify every element. Use null as a placeholder
// for non-JSON values.

                length = value.length;
                for (i = 0; i < length; i += 1) {
                    partial[i] = str(i, value) || 'null';
                }

// Join all of the elements together, separated with commas, and wrap them in
// brackets.

                v = partial.length === 0 ? '[]' :
                    gap ? '[\n' + gap +
                            partial.join(',\n' + gap) + '\n' +
                                mind + ']' :
                          '[' + partial.join(',') + ']';
                gap = mind;
                return v;
            }

// If the replacer is an array, use it to select the members to be stringified.

            if (rep && typeof rep === 'object') {
                length = rep.length;
                for (i = 0; i < length; i += 1) {
                    k = rep[i];
                    if (typeof k === 'string') {
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (gap ? ': ' : ':') + v);
                        }
                    }
                }
            } else {

// Otherwise, iterate through all of the keys in the object.

                for (k in value) {
                    if (Object.hasOwnProperty.call(value, k)) {
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (gap ? ': ' : ':') + v);
                        }
                    }
                }
            }

// Join all of the member texts together, separated with commas,
// and wrap them in braces.

            v = partial.length === 0 ? '{}' :
                gap ? '{\n' + gap + partial.join(',\n' + gap) + '\n' +
                        mind + '}' : '{' + partial.join(',') + '}';
            gap = mind;
            return v;
        }
    }

// If the JSON object does not yet have a stringify method, give it one.

    if (typeof JSON.stringify !== 'function') {
        JSON.stringify = function (value, replacer, space) {

// The stringify method takes a value and an optional replacer, and an optional
// space parameter, and returns a JSON text. The replacer can be a function
// that can replace values, or an array of strings that will select the keys.
// A default replacer method can be provided. Use of the space parameter can
// produce text that is more easily readable.

            var i;
            gap = '';
            indent = '';

// If the space parameter is a number, make an indent string containing that
// many spaces.

            if (typeof space === 'number') {
                for (i = 0; i < space; i += 1) {
                    indent += ' ';
                }

// If the space parameter is a string, it will be used as the indent string.

            } else if (typeof space === 'string') {
                indent = space;
            }

// If there is a replacer, it must be a function or an array.
// Otherwise, throw an error.

            rep = replacer;
            if (replacer && typeof replacer !== 'function' &&
                    (typeof replacer !== 'object' ||
                     typeof replacer.length !== 'number')) {
                throw new Error('JSON.stringify');
            }

// Make a fake root object containing our value under the key of ''.
// Return the result of stringifying the value.

            return str('', {'': value});
        };
    }


// If the JSON object does not yet have a parse method, give it one.

    if (typeof JSON.parse !== 'function') {
        JSON.parse = function (text, reviver) {

// The parse method takes a text and an optional reviver function, and returns
// a JavaScript value if the text is a valid JSON text.

            var j;

            function walk(holder, key) {

// The walk method is used to recursively walk the resulting structure so
// that modifications can be made.

                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }


// Parsing happens in four stages. In the first stage, we replace certain
// Unicode characters with escape sequences. JavaScript handles many characters
// incorrectly, either silently deleting them, or treating them as line endings.

            cx.lastIndex = 0;
            if (cx.test(text)) {
                text = text.replace(cx, function (a) {
                    return '\\u' +
                        ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                });
            }

// In the second stage, we run the text against regular expressions that look
// for non-JSON patterns. We are especially concerned with '()' and 'new'
// because they can cause invocation, and '=' because it can cause mutation.
// But just to be safe, we want to reject all unexpected forms.

// We split the second stage into 4 regexp operations in order to work around
// crippling inefficiencies in IE's and Safari's regexp engines. First we
// replace the JSON backslash pairs with '@' (a non-JSON character). Second, we
// replace all simple value tokens with ']' characters. Third, we delete all
// open brackets that follow a colon or comma or that begin the text. Finally,
// we look to see that the remaining characters are only whitespace or ']' or
// ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

            if (/^[\],:{}\s]*$/.
test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').
replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

// In the third stage we use the eval function to compile the text into a
// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.

                j = eval('(' + text + ')');

// In the optional fourth stage, we recursively walk the new structure, passing
// each name/value pair to a reviver function for possible transformation.

                return typeof reviver === 'function' ?
                    walk({'': j}, '') : j;
            }

// If the text is not JSON parseable, then a SyntaxError is thrown.

            throw new SyntaxError('JSON.parse');
        };
    }
})();


// 官方 swfobject.js文件内容，非controls接口内容
/*!	SWFObject v2.2 <http://code.google.com/p/swfobject/> 
	is released under the MIT License <http://www.opensource.org/licenses/mit-license.php> 
*/
var swfobject = function() {
	
	var UNDEF = "undefined",
		OBJECT = "object",
		SHOCKWAVE_FLASH = "Shockwave Flash",
		SHOCKWAVE_FLASH_AX = "ShockwaveFlash.ShockwaveFlash",
		FLASH_MIME_TYPE = "application/x-shockwave-flash",
		EXPRESS_INSTALL_ID = "SWFObjectExprInst",
		ON_READY_STATE_CHANGE = "onreadystatechange",
		
		win = window,
		doc = document,
		nav = navigator,
		
		plugin = false,
		domLoadFnArr = [main],
		regObjArr = [],
		objIdArr = [],
		listenersArr = [],
		storedAltContent,
		storedAltContentId,
		storedCallbackFn,
		storedCallbackObj,
		isDomLoaded = false,
		isExpressInstallActive = false,
		dynamicStylesheet,
		dynamicStylesheetMedia,
		autoHideShow = true,
	
	/* Centralized function for browser feature detection
		- User agent string detection is only used when no good alternative is possible
		- Is executed directly for optimal performance
	*/	
	ua = function() {
		var w3cdom = typeof doc.getElementById != UNDEF && typeof doc.getElementsByTagName != UNDEF && typeof doc.createElement != UNDEF,
			u = nav.userAgent.toLowerCase(),
			p = nav.platform.toLowerCase(),
			windows = p ? /win/.test(p) : /win/.test(u),
			mac = p ? /mac/.test(p) : /mac/.test(u),
			webkit = /webkit/.test(u) ? parseFloat(u.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : false, // returns either the webkit version or false if not webkit
			ie = !+"\v1", // feature detection based on Andrea Giammarchi's solution: http://webreflection.blogspot.com/2009/01/32-bytes-to-know-if-your-browser-is-ie.html
			playerVersion = [0,0,0],
			d = null;
		if (typeof nav.plugins != UNDEF && typeof nav.plugins[SHOCKWAVE_FLASH] == OBJECT) {
			d = nav.plugins[SHOCKWAVE_FLASH].description;
			if (d && !(typeof nav.mimeTypes != UNDEF && nav.mimeTypes[FLASH_MIME_TYPE] && !nav.mimeTypes[FLASH_MIME_TYPE].enabledPlugin)) { // navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin indicates whether plug-ins are enabled or disabled in Safari 3+
				plugin = true;
				ie = false; // cascaded feature detection for Internet Explorer
				d = d.replace(/^.*\s+(\S+\s+\S+$)/, "$1");
				playerVersion[0] = parseInt(d.replace(/^(.*)\..*$/, "$1"), 10);
				playerVersion[1] = parseInt(d.replace(/^.*\.(.*)\s.*$/, "$1"), 10);
				playerVersion[2] = /[a-zA-Z]/.test(d) ? parseInt(d.replace(/^.*[a-zA-Z]+(.*)$/, "$1"), 10) : 0;
			}
		}
		else if (typeof win.ActiveXObject != UNDEF) {
			try {
				var a = new ActiveXObject(SHOCKWAVE_FLASH_AX);
				if (a) { // a will return null when ActiveX is disabled
					d = a.GetVariable("$version");
					if (d) {
						ie = true; // cascaded feature detection for Internet Explorer
						d = d.split(" ")[1].split(",");
						playerVersion = [parseInt(d[0], 10), parseInt(d[1], 10), parseInt(d[2], 10)];
					}
				}
			}
			catch(e) {}
		}
		return { w3:w3cdom, pv:playerVersion, wk:webkit, ie:ie, win:windows, mac:mac };
	}(),
	
	/* Cross-browser onDomLoad
		- Will fire an event as soon as the DOM of a web page is loaded
		- Internet Explorer workaround based on Diego Perini's solution: http://javascript.nwbox.com/IEContentLoaded/
		- Regular onload serves as fallback
	*/ 
	onDomLoad = function() {
		if (!ua.w3) { return; }
		if ((typeof doc.readyState != UNDEF && doc.readyState == "complete") || (typeof doc.readyState == UNDEF && (doc.getElementsByTagName("body")[0] || doc.body))) { // function is fired after onload, e.g. when script is inserted dynamically 
			callDomLoadFunctions();
		}
		if (!isDomLoaded) {
			if (typeof doc.addEventListener != UNDEF) {
				doc.addEventListener("DOMContentLoaded", callDomLoadFunctions, false);
			}		
			if (ua.ie && ua.win) {
				doc.attachEvent(ON_READY_STATE_CHANGE, function() {
					if (doc.readyState == "complete") {
						doc.detachEvent(ON_READY_STATE_CHANGE, arguments.callee);
						callDomLoadFunctions();
					}
				});
				if (win == top) { // if not inside an iframe
					(function(){
						if (isDomLoaded) { return; }
						try {
							doc.documentElement.doScroll("left");
						}
						catch(e) {
							setTimeout(arguments.callee, 0);
							return;
						}
						callDomLoadFunctions();
					})();
				}
			}
			if (ua.wk) {
				(function(){
					if (isDomLoaded) { return; }
					if (!/loaded|complete/.test(doc.readyState)) {
						setTimeout(arguments.callee, 0);
						return;
					}
					callDomLoadFunctions();
				})();
			}
			addLoadEvent(callDomLoadFunctions);
		}
	}();
	
	function callDomLoadFunctions() {
		if (isDomLoaded) { return; }
		try { // test if we can really add/remove elements to/from the DOM; we don't want to fire it too early
			var t = doc.getElementsByTagName("body")[0].appendChild(createElement("span"));
			t.parentNode.removeChild(t);
		}
		catch (e) { return; }
		isDomLoaded = true;
		var dl = domLoadFnArr.length;
		for (var i = 0; i < dl; i++) {
			domLoadFnArr[i]();
		}
	}
	
	function addDomLoadEvent(fn) {
		if (isDomLoaded) {
			fn();
		}
		else { 
			domLoadFnArr[domLoadFnArr.length] = fn; // Array.push() is only available in IE5.5+
		}
	}
	
	/* Cross-browser onload
		- Based on James Edwards' solution: http://brothercake.com/site/resources/scripts/onload/
		- Will fire an event as soon as a web page including all of its assets are loaded 
	 */
	function addLoadEvent(fn) {
		if (typeof win.addEventListener != UNDEF) {
			win.addEventListener("load", fn, false);
		}
		else if (typeof doc.addEventListener != UNDEF) {
			doc.addEventListener("load", fn, false);
		}
		else if (typeof win.attachEvent != UNDEF) {
			addListener(win, "onload", fn);
		}
		else if (typeof win.onload == "function") {
			var fnOld = win.onload;
			win.onload = function() {
				fnOld();
				fn();
			};
		}
		else {
			win.onload = fn;
		}
	}
	
	/* Main function
		- Will preferably execute onDomLoad, otherwise onload (as a fallback)
	*/
	function main() { 
		if (plugin) {
			testPlayerVersion();
		}
		else {
			matchVersions();
		}
	}
	
	/* Detect the Flash Player version for non-Internet Explorer browsers
		- Detecting the plug-in version via the object element is more precise than using the plugins collection item's description:
		  a. Both release and build numbers can be detected
		  b. Avoid wrong descriptions by corrupt installers provided by Adobe
		  c. Avoid wrong descriptions by multiple Flash Player entries in the plugin Array, caused by incorrect browser imports
		- Disadvantage of this method is that it depends on the availability of the DOM, while the plugins collection is immediately available
	*/
	function testPlayerVersion() {
		var b = doc.getElementsByTagName("body")[0];
		var o = createElement(OBJECT);
		o.setAttribute("type", FLASH_MIME_TYPE);
		var t = b.appendChild(o);
		if (t) {
			var counter = 0;
			(function(){
				if (typeof t.GetVariable != UNDEF) {
					var d = t.GetVariable("$version");
					if (d) {
						d = d.split(" ")[1].split(",");
						ua.pv = [parseInt(d[0], 10), parseInt(d[1], 10), parseInt(d[2], 10)];
					}
				}
				else if (counter < 10) {
					counter++;
					setTimeout(arguments.callee, 10);
					return;
				}
				b.removeChild(o);
				t = null;
				matchVersions();
			})();
		}
		else {
			matchVersions();
		}
	}
	
	/* Perform Flash Player and SWF version matching; static publishing only
	*/
	function matchVersions() {
		var rl = regObjArr.length;
		if (rl > 0) {
			for (var i = 0; i < rl; i++) { // for each registered object element
				var id = regObjArr[i].id;
				var cb = regObjArr[i].callbackFn;
				var cbObj = {success:false, id:id};
				if (ua.pv[0] > 0) {
					var obj = getElementById(id);
					if (obj) {
						if (hasPlayerVersion(regObjArr[i].swfVersion) && !(ua.wk && ua.wk < 312)) { // Flash Player version >= published SWF version: Houston, we have a match!
							setVisibility(id, true);
							if (cb) {
								cbObj.success = true;
								cbObj.ref = getObjectById(id);
								cb(cbObj);
							}
						}
						else if (regObjArr[i].expressInstall && canExpressInstall()) { // show the Adobe Express Install dialog if set by the web page author and if supported
							var att = {};
							att.data = regObjArr[i].expressInstall;
							att.width = obj.getAttribute("width") || "0";
							att.height = obj.getAttribute("height") || "0";
							if (obj.getAttribute("class")) { att.styleclass = obj.getAttribute("class"); }
							if (obj.getAttribute("align")) { att.align = obj.getAttribute("align"); }
							// parse HTML object param element's name-value pairs
							var par = {};
							var p = obj.getElementsByTagName("param");
							var pl = p.length;
							for (var j = 0; j < pl; j++) {
								if (p[j].getAttribute("name").toLowerCase() != "movie") {
									par[p[j].getAttribute("name")] = p[j].getAttribute("value");
								}
							}
							showExpressInstall(att, par, id, cb);
						}
						else { // Flash Player and SWF version mismatch or an older Webkit engine that ignores the HTML object element's nested param elements: display alternative content instead of SWF
							displayAltContent(obj);
							if (cb) { cb(cbObj); }
						}
					}
				}
				else {	// if no Flash Player is installed or the fp version cannot be detected we let the HTML object element do its job (either show a SWF or alternative content)
					setVisibility(id, true);
					if (cb) {
						var o = getObjectById(id); // test whether there is an HTML object element or not
						if (o && typeof o.SetVariable != UNDEF) { 
							cbObj.success = true;
							cbObj.ref = o;
						}
						cb(cbObj);
					}
				}
			}
		}
	}
	
	function getObjectById(objectIdStr) {
		var r = null;
		var o = getElementById(objectIdStr);
		if (o && o.nodeName == "OBJECT") {
			if (typeof o.SetVariable != UNDEF) {
				r = o;
			}
			else {
				var n = o.getElementsByTagName(OBJECT)[0];
				if (n) {
					r = n;
				}
			}
		}
		return r;
	}
	
	/* Requirements for Adobe Express Install
		- only one instance can be active at a time
		- fp 6.0.65 or higher
		- Win/Mac OS only
		- no Webkit engines older than version 312
	*/
	function canExpressInstall() {
		return !isExpressInstallActive && hasPlayerVersion("6.0.65") && (ua.win || ua.mac) && !(ua.wk && ua.wk < 312);
	}
	
	/* Show the Adobe Express Install dialog
		- Reference: http://www.adobe.com/cfusion/knowledgebase/index.cfm?id=6a253b75
	*/
	function showExpressInstall(att, par, replaceElemIdStr, callbackFn) {
		isExpressInstallActive = true;
		storedCallbackFn = callbackFn || null;
		storedCallbackObj = {success:false, id:replaceElemIdStr};
		var obj = getElementById(replaceElemIdStr);
		if (obj) {
			if (obj.nodeName == "OBJECT") { // static publishing
				storedAltContent = abstractAltContent(obj);
				storedAltContentId = null;
			}
			else { // dynamic publishing
				storedAltContent = obj;
				storedAltContentId = replaceElemIdStr;
			}
			att.id = EXPRESS_INSTALL_ID;
			if (typeof att.width == UNDEF || (!/%$/.test(att.width) && parseInt(att.width, 10) < 310)) { att.width = "310"; }
			if (typeof att.height == UNDEF || (!/%$/.test(att.height) && parseInt(att.height, 10) < 137)) { att.height = "137"; }
			doc.title = doc.title.slice(0, 47) + " - Flash Player Installation";
			var pt = ua.ie && ua.win ? "ActiveX" : "PlugIn",
				fv = "MMredirectURL=" + encodeURI(window.location).toString().replace(/&/g,"%26") + "&MMplayerType=" + pt + "&MMdoctitle=" + doc.title;
			if (typeof par.flashvars != UNDEF) {
				par.flashvars += "&" + fv;
			}
			else {
				par.flashvars = fv;
			}
			// IE only: when a SWF is loading (AND: not available in cache) wait for the readyState of the object element to become 4 before removing it,
			// because you cannot properly cancel a loading SWF file without breaking browser load references, also obj.onreadystatechange doesn't work
			if (ua.ie && ua.win && obj.readyState != 4) {
				var newObj = createElement("div");
				replaceElemIdStr += "SWFObjectNew";
				newObj.setAttribute("id", replaceElemIdStr);
				obj.parentNode.insertBefore(newObj, obj); // insert placeholder div that will be replaced by the object element that loads expressinstall.swf
				obj.style.display = "none";
				(function(){
					if (obj.readyState == 4) {
						obj.parentNode.removeChild(obj);
					}
					else {
						setTimeout(arguments.callee, 10);
					}
				})();
			}
			createSWF(att, par, replaceElemIdStr);
		}
	}
	
	/* Functions to abstract and display alternative content
	*/
	function displayAltContent(obj) {
		if (ua.ie && ua.win && obj.readyState != 4) {
			// IE only: when a SWF is loading (AND: not available in cache) wait for the readyState of the object element to become 4 before removing it,
			// because you cannot properly cancel a loading SWF file without breaking browser load references, also obj.onreadystatechange doesn't work
			var el = createElement("div");
			obj.parentNode.insertBefore(el, obj); // insert placeholder div that will be replaced by the alternative content
			el.parentNode.replaceChild(abstractAltContent(obj), el);
			obj.style.display = "none";
			(function(){
				if (obj.readyState == 4) {
					obj.parentNode.removeChild(obj);
				}
				else {
					setTimeout(arguments.callee, 10);
				}
			})();
		}
		else {
			obj.parentNode.replaceChild(abstractAltContent(obj), obj);
		}
	} 

	function abstractAltContent(obj) {
		var ac = createElement("div");
		if (ua.win && ua.ie) {
			ac.innerHTML = obj.innerHTML;
		}
		else {
			var nestedObj = obj.getElementsByTagName(OBJECT)[0];
			if (nestedObj) {
				var c = nestedObj.childNodes;
				if (c) {
					var cl = c.length;
					for (var i = 0; i < cl; i++) {
						if (!(c[i].nodeType == 1 && c[i].nodeName == "PARAM") && !(c[i].nodeType == 8)) {
							ac.appendChild(c[i].cloneNode(true));
						}
					}
				}
			}
		}
		return ac;
	}
	
	/* Cross-browser dynamic SWF creation
	*/
	function createSWF(attObj, parObj, id) {
		var r, el = getElementById(id);
		if (ua.wk && ua.wk < 312) { return r; }
		if (el) {
			if (typeof attObj.id == UNDEF) { // if no 'id' is defined for the object element, it will inherit the 'id' from the alternative content
				attObj.id = id;
			}
			if (ua.ie && ua.win) { // Internet Explorer + the HTML object element + W3C DOM methods do not combine: fall back to outerHTML
				var att = "";
				for (var i in attObj) {
					if (attObj[i] != Object.prototype[i]) { // filter out prototype additions from other potential libraries
						if (i.toLowerCase() == "data") {
							parObj.movie = attObj[i];
						}
						else if (i.toLowerCase() == "styleclass") { // 'class' is an ECMA4 reserved keyword
							att += ' class="' + attObj[i] + '"';
						}
						else if (i.toLowerCase() != "classid") {
							att += ' ' + i + '="' + attObj[i] + '"';
						}
					}
				}
				var par = "";
				for (var j in parObj) {
					if (parObj[j] != Object.prototype[j]) { // filter out prototype additions from other potential libraries
						par += '<param name="' + j + '" value="' + parObj[j] + '" />';
					}
				}
				el.outerHTML = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"' + att + '>' + par + '</object>';
				objIdArr[objIdArr.length] = attObj.id; // stored to fix object 'leaks' on unload (dynamic publishing only)
				r = getElementById(attObj.id);	
			}
			else { // well-behaving browsers
				var o = createElement(OBJECT);
				o.setAttribute("type", FLASH_MIME_TYPE);
				for (var m in attObj) {
					if (attObj[m] != Object.prototype[m]) { // filter out prototype additions from other potential libraries
						if (m.toLowerCase() == "styleclass") { // 'class' is an ECMA4 reserved keyword
							o.setAttribute("class", attObj[m]);
						}
						else if (m.toLowerCase() != "classid") { // filter out IE specific attribute
							o.setAttribute(m, attObj[m]);
						}
					}
				}
				for (var n in parObj) {
					if (parObj[n] != Object.prototype[n] && n.toLowerCase() != "movie") { // filter out prototype additions from other potential libraries and IE specific param element
						createObjParam(o, n, parObj[n]);
					}
				}
				el.parentNode.replaceChild(o, el);
				r = o;
			}
		}
		return r;
	}
	
	function createObjParam(el, pName, pValue) {
		var p = createElement("param");
		p.setAttribute("name", pName);	
		p.setAttribute("value", pValue);
		el.appendChild(p);
	}
	
	/* Cross-browser SWF removal
		- Especially needed to safely and completely remove a SWF in Internet Explorer
	*/
	function removeSWF(id) {
		var obj = getElementById(id);
		if (obj && obj.nodeName == "OBJECT") {
			if (ua.ie && ua.win) {
				obj.style.display = "none";
				(function(){
					if (obj.readyState == 4) {
						removeObjectInIE(id);
					}
					else {
						setTimeout(arguments.callee, 10);
					}
				})();
			}
			else {
				obj.parentNode.removeChild(obj);
			}
		}
	}
	
	function removeObjectInIE(id) {
		var obj = getElementById(id);
		if (obj) {
			for (var i in obj) {
				if (typeof obj[i] == "function") {
					obj[i] = null;
				}
			}
			obj.parentNode.removeChild(obj);
		}
	}
	
	/* Functions to optimize JavaScript compression
	*/
	function getElementById(id) {
		var el = null;
		try {
			el = doc.getElementById(id);
		}
		catch (e) {}
		return el;
	}
	
	function createElement(el) {
		return doc.createElement(el);
	}
	
	/* Updated attachEvent function for Internet Explorer
		- Stores attachEvent information in an Array, so on unload the detachEvent functions can be called to avoid memory leaks
	*/	
	function addListener(target, eventType, fn) {
		target.attachEvent(eventType, fn);
		listenersArr[listenersArr.length] = [target, eventType, fn];
	}
	
	/* Flash Player and SWF content version matching
	*/
	function hasPlayerVersion(rv) {
		var pv = ua.pv, v = rv.split(".");
		v[0] = parseInt(v[0], 10);
		v[1] = parseInt(v[1], 10) || 0; // supports short notation, e.g. "9" instead of "9.0.0"
		v[2] = parseInt(v[2], 10) || 0;
		return (pv[0] > v[0] || (pv[0] == v[0] && pv[1] > v[1]) || (pv[0] == v[0] && pv[1] == v[1] && pv[2] >= v[2])) ? true : false;
	}
	
	/* Cross-browser dynamic CSS creation
		- Based on Bobby van der Sluis' solution: http://www.bobbyvandersluis.com/articles/dynamicCSS.php
	*/	
	function createCSS(sel, decl, media, newStyle) {
		if (ua.ie && ua.mac) { return; }
		var h = doc.getElementsByTagName("head")[0];
		if (!h) { return; } // to also support badly authored HTML pages that lack a head element
		var m = (media && typeof media == "string") ? media : "screen";
		if (newStyle) {
			dynamicStylesheet = null;
			dynamicStylesheetMedia = null;
		}
		if (!dynamicStylesheet || dynamicStylesheetMedia != m) { 
			// create dynamic stylesheet + get a global reference to it
			var s = createElement("style");
			s.setAttribute("type", "text/css");
			s.setAttribute("media", m);
			dynamicStylesheet = h.appendChild(s);
			if (ua.ie && ua.win && typeof doc.styleSheets != UNDEF && doc.styleSheets.length > 0) {
				dynamicStylesheet = doc.styleSheets[doc.styleSheets.length - 1];
			}
			dynamicStylesheetMedia = m;
		}
		// add style rule
		if (ua.ie && ua.win) {
			if (dynamicStylesheet && typeof dynamicStylesheet.addRule == OBJECT) {
				dynamicStylesheet.addRule(sel, decl);
			}
		}
		else {
			if (dynamicStylesheet && typeof doc.createTextNode != UNDEF) {
				dynamicStylesheet.appendChild(doc.createTextNode(sel + " {" + decl + "}"));
			}
		}
	}
	
	function setVisibility(id, isVisible) {
		if (!autoHideShow) { return; }
		var v = isVisible ? "visible" : "hidden";
		if (isDomLoaded && getElementById(id)) {
			getElementById(id).style.visibility = v;
		}
		else {
			createCSS("#" + id, "visibility:" + v);
		}
	}

	/* Filter to avoid XSS attacks
	*/
	function urlEncodeIfNecessary(s) {
		var regex = /[\\\"<>\.;]/;
		var hasBadChars = regex.exec(s) != null;
		return hasBadChars && typeof encodeURIComponent != UNDEF ? encodeURIComponent(s) : s;
	}
	
	/* Release memory to avoid memory leaks caused by closures, fix hanging audio/video threads and force open sockets/NetConnections to disconnect (Internet Explorer only)
	*/
	var cleanup = function() {
		if (ua.ie && ua.win) {
			window.attachEvent("onunload", function() {
				// remove listeners to avoid memory leaks
				var ll = listenersArr.length;
				for (var i = 0; i < ll; i++) {
					listenersArr[i][0].detachEvent(listenersArr[i][1], listenersArr[i][2]);
				}
				// cleanup dynamically embedded objects to fix audio/video threads and force open sockets and NetConnections to disconnect
				var il = objIdArr.length;
				for (var j = 0; j < il; j++) {
					removeSWF(objIdArr[j]);
				}
				// cleanup library's main closures to avoid memory leaks
				for (var k in ua) {
					ua[k] = null;
				}
				ua = null;
				for (var l in swfobject) {
					swfobject[l] = null;
				}
				swfobject = null;
			});
		}
	}();
	
	return {
		/* Public API
			- Reference: http://code.google.com/p/swfobject/wiki/documentation
		*/ 
		registerObject: function(objectIdStr, swfVersionStr, xiSwfUrlStr, callbackFn) {
			if (ua.w3 && objectIdStr && swfVersionStr) {
				var regObj = {};
				regObj.id = objectIdStr;
				regObj.swfVersion = swfVersionStr;
				regObj.expressInstall = xiSwfUrlStr;
				regObj.callbackFn = callbackFn;
				regObjArr[regObjArr.length] = regObj;
				setVisibility(objectIdStr, false);
			}
			else if (callbackFn) {
				callbackFn({success:false, id:objectIdStr});
			}
		},
		
		getObjectById: function(objectIdStr) {
			if (ua.w3) {
				return getObjectById(objectIdStr);
			}
		},
		
		embedSWF: function(swfUrlStr, replaceElemIdStr, widthStr, heightStr, swfVersionStr, xiSwfUrlStr, flashvarsObj, parObj, attObj, callbackFn) {
			var callbackObj = {success:false, id:replaceElemIdStr};
			if (ua.w3 && !(ua.wk && ua.wk < 312) && swfUrlStr && replaceElemIdStr && widthStr && heightStr && swfVersionStr) {
				setVisibility(replaceElemIdStr, false);
				addDomLoadEvent(function() {
					widthStr += ""; // auto-convert to string
					heightStr += "";
					var att = {};
					if (attObj && typeof attObj === OBJECT) {
						for (var i in attObj) { // copy object to avoid the use of references, because web authors often reuse attObj for multiple SWFs
							att[i] = attObj[i];
						}
					}
					att.data = swfUrlStr;
					att.width = widthStr;
					att.height = heightStr;
					var par = {}; 
					if (parObj && typeof parObj === OBJECT) {
						for (var j in parObj) { // copy object to avoid the use of references, because web authors often reuse parObj for multiple SWFs
							par[j] = parObj[j];
						}
					}
					if (flashvarsObj && typeof flashvarsObj === OBJECT) {
						for (var k in flashvarsObj) { // copy object to avoid the use of references, because web authors often reuse flashvarsObj for multiple SWFs
							if (typeof par.flashvars != UNDEF) {
								par.flashvars += "&" + k + "=" + flashvarsObj[k];
							}
							else {
								par.flashvars = k + "=" + flashvarsObj[k];
							}
						}
					}
					if (hasPlayerVersion(swfVersionStr)) { // create SWF
						var obj = createSWF(att, par, replaceElemIdStr);
						if (att.id == replaceElemIdStr) {
							setVisibility(replaceElemIdStr, true);
						}
						callbackObj.success = true;
						callbackObj.ref = obj;
					}
					else if (xiSwfUrlStr && canExpressInstall()) { // show Adobe Express Install
						att.data = xiSwfUrlStr;
						showExpressInstall(att, par, replaceElemIdStr, callbackFn);
						return;
					}
					else { // show alternative content
						setVisibility(replaceElemIdStr, true);
					}
					if (callbackFn) { callbackFn(callbackObj); }
				});
			}
			else if (callbackFn) { callbackFn(callbackObj);	}
		},
		
		switchOffAutoHideShow: function() {
			autoHideShow = false;
		},
		
		ua: ua,
		
		getFlashPlayerVersion: function() {
			return { major:ua.pv[0], minor:ua.pv[1], release:ua.pv[2] };
		},
		
		hasFlashPlayerVersion: hasPlayerVersion,
		
		createSWF: function(attObj, parObj, replaceElemIdStr) {
			if (ua.w3) {
				return createSWF(attObj, parObj, replaceElemIdStr);
			}
			else {
				return undefined;
			}
		},
		
		showExpressInstall: function(att, par, replaceElemIdStr, callbackFn) {
			if (ua.w3 && canExpressInstall()) {
				showExpressInstall(att, par, replaceElemIdStr, callbackFn);
			}
		},
		
		removeSWF: function(objElemIdStr) {
			if (ua.w3) {
				removeSWF(objElemIdStr);
			}
		},
		
		createCSS: function(selStr, declStr, mediaStr, newStyleBoolean) {
			if (ua.w3) {
				createCSS(selStr, declStr, mediaStr, newStyleBoolean);
			}
		},
		
		addDomLoadEvent: addDomLoadEvent,
		
		addLoadEvent: addLoadEvent,
		
		getQueryParamValue: function(param) {
			var q = doc.location.search || doc.location.hash;
			if (q) {
				if (/\?/.test(q)) { q = q.split("?")[1]; } // strip question mark
				if (param == null) {
					return urlEncodeIfNecessary(q);
				}
				var pairs = q.split("&");
				for (var i = 0; i < pairs.length; i++) {
					if (pairs[i].substring(0, pairs[i].indexOf("=")) == param) {
						return urlEncodeIfNecessary(pairs[i].substring((pairs[i].indexOf("=") + 1)));
					}
				}
			}
			return "";
		},
		
		// For internal usage only
		expressInstallCallback: function() {
			if (isExpressInstallActive) {
				var obj = getElementById(EXPRESS_INSTALL_ID);
				if (obj && storedAltContent) {
					obj.parentNode.replaceChild(storedAltContent, obj);
					if (storedAltContentId) {
						setVisibility(storedAltContentId, true);
						if (ua.ie && ua.win) { storedAltContent.style.display = "block"; }
					}
					if (storedCallbackFn) { storedCallbackFn(storedCallbackObj); }
				}
				isExpressInstallActive = false;
			} 
		}
	};
}();

})();


/*
/* FABridge内容 非controls接口，占用全局FABridge命名空间
Copyright 2006 Adobe Systems Incorporated

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.


THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

*/


/*
 * The Bridge class, responsible for navigating AS instances
 */
function FABridge(target,bridgeName)
{
    this.target = target;
    this.remoteTypeCache = {};
    this.remoteInstanceCache = {};
    this.remoteFunctionCache = {};
    this.localFunctionCache = {};
    this.bridgeID = FABridge.nextBridgeID++;
    this.name = bridgeName;
    this.nextLocalFuncID = 0;
    FABridge.instances[this.name] = this;
    FABridge.idMap[this.bridgeID] = this;

    return this;
}

// type codes for packed values
FABridge.TYPE_ASINSTANCE =  1;
FABridge.TYPE_ASFUNCTION =  2;

FABridge.TYPE_JSFUNCTION =  3;
FABridge.TYPE_ANONYMOUS =   4;

FABridge.initCallbacks = {}

FABridge.argsToArray = function(args)
{
    var result = [];
    for (var i = 0; i < args.length; i++)
    {
        result[i] = args[i];
    }
    return result;
}

function instanceFactory(objID)
{
    this.fb_instance_id = objID;
    return this;
}

function FABridge__invokeJSFunction(args)
{  
    var funcID = args[0];
    var throughArgs = args.concat();//FABridge.argsToArray(arguments);
    throughArgs.shift();
   
    var bridge = FABridge.extractBridgeFromID(funcID);
    return bridge.invokeLocalFunction(funcID, throughArgs);
}

FABridge.addInitializationCallback = function(bridgeName, callback)
{
    var inst = FABridge.instances[bridgeName];
    if (inst != undefined)
    {
        callback.call(inst);
        return;
    }

    var callbackList = FABridge.initCallbacks[bridgeName];
    if(callbackList == null)
    {
        FABridge.initCallbacks[bridgeName] = callbackList = [];
    }

    callbackList.push(callback);
}

function FABridge__bridgeInitialized(bridgeName) {
    var objects = document.getElementsByTagName("object");
    var ol = objects.length;
    var activeObjects = [];
    if (ol > 0) {
		for (var i = 0; i < ol; i++) {
			if (typeof objects[i].SetVariable != "undefined") {
				activeObjects[activeObjects.length] = objects[i];
			}
		}
	}
    var embeds = document.getElementsByTagName("embed");
    var el = embeds.length;
    var activeEmbeds = [];
    if (el > 0) {
		for (var j = 0; j < el; j++) {
			if (typeof embeds[j].SetVariable != "undefined") {
            	activeEmbeds[activeEmbeds.length] = embeds[j];
            }
        }
    }
    var aol = activeObjects.length;
    var ael = activeEmbeds.length;
    var searchStr = "bridgeName="+ bridgeName;
    if ((aol == 1 && !ael) || (aol == 1 && ael == 1)) {
    	FABridge.attachBridge(activeObjects[0], bridgeName);	 
    }
    else if (ael == 1 && !aol) {
    	FABridge.attachBridge(activeEmbeds[0], bridgeName);
        }
    else {
                var flash_found = false;
		if (aol > 1) {
			for (var k = 0; k < aol; k++) {
				 var params = activeObjects[k].childNodes;
				 for (var l = 0; l < params.length; l++) {
					var param = params[l];
					if (param.nodeType == 1 && param.tagName.toLowerCase() == "param" && param["name"].toLowerCase() == "flashvars" && param["value"].indexOf(searchStr) >= 0) {
						FABridge.attachBridge(activeObjects[k], bridgeName);
                            flash_found = true;
                            break;
                        }
                    }
                if (flash_found) {
                    break;
                }
            }
        }
		if (!flash_found && ael > 1) {
			for (var m = 0; m < ael; m++) {
				var flashVars = activeEmbeds[m].attributes.getNamedItem("flashVars").nodeValue;
				if (flashVars.indexOf(searchStr) >= 0) {
					FABridge.attachBridge(activeEmbeds[m], bridgeName);
					break;
    }
            }
        }
    }
    return true;
}

// used to track multiple bridge instances, since callbacks from AS are global across the page.

FABridge.nextBridgeID = 0;
FABridge.instances = {};
FABridge.idMap = {};
FABridge.refCount = 0;

FABridge.extractBridgeFromID = function(id)
{
    var bridgeID = (id >> 16);
    return FABridge.idMap[bridgeID];
}

FABridge.attachBridge = function(instance, bridgeName)
{
    var newBridgeInstance = new FABridge(instance, bridgeName);

    FABridge[bridgeName] = newBridgeInstance;

/*  FABridge[bridgeName] = function() {
        return newBridgeInstance.root();
    }
*/
    var callbacks = FABridge.initCallbacks[bridgeName];
    if (callbacks == null)
    {
        return;
    }
    for (var i = 0; i < callbacks.length; i++)
    {
        callbacks[i].call(newBridgeInstance);
    }
    delete FABridge.initCallbacks[bridgeName]
}

// some methods can't be proxied.  You can use the explicit get,set, and call methods if necessary.

FABridge.blockedMethods =
{
    toString: true,
    get: true,
    set: true,
    call: true
};

FABridge.prototype =
{


// bootstrapping

    root: function()
    {
        return this.deserialize(this.target.getRoot());
    },
//clears all of the AS objects in the cache maps
    releaseASObjects: function()
    {
        return this.target.releaseASObjects();
    },
//clears a specific object in AS from the type maps
    releaseNamedASObject: function(value)
    {
        if(typeof(value) != "object")
        {
            return false;
        }
        else
        {
            var ret =  this.target.releaseNamedASObject(value.fb_instance_id);
            return ret;
        }
    },
//create a new AS Object
    create: function(className)
    {
        return this.deserialize(this.target.create(className));
    },


    // utilities

    makeID: function(token)
    {
        return (this.bridgeID << 16) + token;
    },


    // low level access to the flash object

//get a named property from an AS object
    getPropertyFromAS: function(objRef, propName)
    {
        if (FABridge.refCount > 0)
        {
            throw new Error("You are trying to call recursively into the Flash Player which is not allowed. In most cases the JavaScript setTimeout function, can be used as a workaround.");
        }
        else
        {
            FABridge.refCount++;
            retVal = this.target.getPropFromAS(objRef, propName);
            retVal = this.handleError(retVal);
            FABridge.refCount--;
            return retVal;
        }
    },
//set a named property on an AS object
    setPropertyInAS: function(objRef,propName, value)
    {
        if (FABridge.refCount > 0)
        {
            throw new Error("You are trying to call recursively into the Flash Player which is not allowed. In most cases the JavaScript setTimeout function, can be used as a workaround.");
        }
        else
        {
            FABridge.refCount++;
            retVal = this.target.setPropInAS(objRef,propName, this.serialize(value));
            retVal = this.handleError(retVal);
            FABridge.refCount--;
            return retVal;
        }
    },

//call an AS function
    callASFunction: function(funcID, args)
    {
        if (FABridge.refCount > 0)
        {
            throw new Error("You are trying to call recursively into the Flash Player which is not allowed. In most cases the JavaScript setTimeout function, can be used as a workaround.");
        }
        else
        {
            FABridge.refCount++;
            retVal = this.target.invokeASFunction(funcID, this.serialize(args));
            retVal = this.handleError(retVal);
            FABridge.refCount--;
            return retVal;
        }
    },
//call a method on an AS object
    callASMethod: function(objID, funcName, args)
    {
        if (FABridge.refCount > 0)
        {
            throw new Error("You are trying to call recursively into the Flash Player which is not allowed. In most cases the JavaScript setTimeout function, can be used as a workaround.");
        }
        else
        {
            FABridge.refCount++;
            args = this.serialize(args);
            retVal = this.target.invokeASMethod(objID, funcName, args);
            retVal = this.handleError(retVal);
            FABridge.refCount--;
            return retVal;
        }
    },

    // responders to remote calls from flash

    //callback from flash that executes a local JS function
    //used mostly when setting js functions as callbacks on events
    invokeLocalFunction: function(funcID, args)
    {
        var result;
        var func = this.localFunctionCache[funcID];

        if(func != undefined)
        {
            result = this.serialize(func.apply(null, this.deserialize(args)));
        }

        return result;
    },

    // Object Types and Proxies
	
    // accepts an object reference, returns a type object matching the obj reference.
    getTypeFromName: function(objTypeName)
    {
        return this.remoteTypeCache[objTypeName];
    },
    //create an AS proxy for the given object ID and type
    createProxy: function(objID, typeName)
    {
        var objType = this.getTypeFromName(typeName);
	        instanceFactory.prototype = objType;
	        var instance = new instanceFactory(objID);
        this.remoteInstanceCache[objID] = instance;
        return instance;
    },
    //return the proxy associated with the given object ID
    getProxy: function(objID)
    {
        return this.remoteInstanceCache[objID];
    },

    // accepts a type structure, returns a constructed type
    addTypeDataToCache: function(typeData)
    {
        newType = new ASProxy(this, typeData.name);
        var accessors = typeData.accessors;
        for (var i = 0; i < accessors.length; i++)
        {
            this.addPropertyToType(newType, accessors[i]);
        }

        var methods = typeData.methods;
        for (var i = 0; i < methods.length; i++)
        {
            if (FABridge.blockedMethods[methods[i]] == undefined)
            {
                this.addMethodToType(newType, methods[i]);
            }
        }


        this.remoteTypeCache[newType.typeName] = newType;
        return newType;
    },

    //add a property to a typename; used to define the properties that can be called on an AS proxied object
    addPropertyToType: function(ty, propName)
    {
        var c = propName.charAt(0);
        var setterName;
        var getterName;
        if(c >= "a" && c <= "z")
        {
            getterName = "get" + c.toUpperCase() + propName.substr(1);
            setterName = "set" + c.toUpperCase() + propName.substr(1);
        }
        else
        {
            getterName = "get" + propName;
            setterName = "set" + propName;
        }
        ty[setterName] = function(val)
        {
            this.bridge.setPropertyInAS(this.fb_instance_id, propName, val);
        }
        ty[getterName] = function()
        {
            return this.bridge.deserialize(this.bridge.getPropertyFromAS(this.fb_instance_id, propName));
        }
    },

    //add a method to a typename; used to define the methods that can be callefd on an AS proxied object
    addMethodToType: function(ty, methodName)
    {
        ty[methodName] = function()
        {
            return this.bridge.deserialize(this.bridge.callASMethod(this.fb_instance_id, methodName, FABridge.argsToArray(arguments)));
        }
    },

    // Function Proxies

    //returns the AS proxy for the specified function ID
    getFunctionProxy: function(funcID)
    {
        var bridge = this;
        if (this.remoteFunctionCache[funcID] == null)
        {
            this.remoteFunctionCache[funcID] = function()
            {
                bridge.callASFunction(funcID, FABridge.argsToArray(arguments));
            }
        }
        return this.remoteFunctionCache[funcID];
    },
    
    //reutrns the ID of the given function; if it doesnt exist it is created and added to the local cache
    getFunctionID: function(func)
    {
        if (func.__bridge_id__ == undefined)
        {
            func.__bridge_id__ = this.makeID(this.nextLocalFuncID++);
            this.localFunctionCache[func.__bridge_id__] = func;
        }
        return func.__bridge_id__;
    },

    // serialization / deserialization

    serialize: function(value)
    {
        var result = {};

        var t = typeof(value);
        //primitives are kept as such
        if (t == "number" || t == "string" || t == "boolean" || t == null || t == undefined)
        {
            result = value;
        }
        else if (value instanceof Array)
        {
            //arrays are serializesd recursively
            result = [];
            for (var i = 0; i < value.length; i++)
            {
                result[i] = this.serialize(value[i]);
            }
        }
        else if (t == "function")
        {
            //js functions are assigned an ID and stored in the local cache 
            result.type = FABridge.TYPE_JSFUNCTION;
            result.value = this.getFunctionID(value);
        }
        else if (value instanceof ASProxy)
        {
            result.type = FABridge.TYPE_ASINSTANCE;
            result.value = value.fb_instance_id;
        }
        else
        {
            result.type = FABridge.TYPE_ANONYMOUS;
            result.value = value;
        }

        return result;
    },

    //on deserialization we always check the return for the specific error code that is used to marshall NPE's into JS errors
    // the unpacking is done by returning the value on each pachet for objects/arrays 
    deserialize: function(packedValue)
    {

        var result;

        var t = typeof(packedValue);
        if (t == "number" || t == "string" || t == "boolean" || packedValue == null || packedValue == undefined)
        {
            result = this.handleError(packedValue);
        }
        else if (packedValue instanceof Array)
        {
            result = [];
            for (var i = 0; i < packedValue.length; i++)
            {
                result[i] = this.deserialize(packedValue[i]);
            }
        }
        else if (t == "object")
        {
            for(var i = 0; i < packedValue.newTypes.length; i++)
            {
                this.addTypeDataToCache(packedValue.newTypes[i]);
            }
            for (var aRefID in packedValue.newRefs)
            {
                this.createProxy(aRefID, packedValue.newRefs[aRefID]);
            }
            if (packedValue.type == FABridge.TYPE_PRIMITIVE)
            {
                result = packedValue.value;
            }
            else if (packedValue.type == FABridge.TYPE_ASFUNCTION)
            {
                result = this.getFunctionProxy(packedValue.value);
            }
            else if (packedValue.type == FABridge.TYPE_ASINSTANCE)
            {
                result = this.getProxy(packedValue.value);
            }
            else if (packedValue.type == FABridge.TYPE_ANONYMOUS)
            {
                result = packedValue.value;
            }
        }
        return result;
    },
    //increases the reference count for the given object
    addRef: function(obj)
    {
        this.target.incRef(obj.fb_instance_id);
    },
    //decrease the reference count for the given object and release it if needed
    release:function(obj)
    {
        this.target.releaseRef(obj.fb_instance_id);
    },

    // check the given value for the components of the hard-coded error code : __FLASHERROR
    // used to marshall NPE's into flash
    
    handleError: function(value)
    {
        if (typeof(value)=="string" && value.indexOf("__FLASHERROR")==0)
        {
            var myErrorMessage = value.split("||");
            if(FABridge.refCount > 0 )
            {
                FABridge.refCount--;
            }
            throw new Error(myErrorMessage[1]);
            return value;
        }
        else
        {
            return value;
        }   
    }
};

// The root ASProxy class that facades a flash object

ASProxy = function(bridge, typeName)
{
    this.bridge = bridge;
    this.typeName = typeName;
    return this;
};
//methods available on each ASProxy object
ASProxy.prototype =
{
    get: function(propName)
    {
        return this.bridge.deserialize(this.bridge.getPropertyFromAS(this.fb_instance_id, propName));
    },

    set: function(propName, value)
    {
        this.bridge.setPropertyInAS(this.fb_instance_id, propName, value);
    },

    call: function(funcName, args)
    {
        this.bridge.callASMethod(this.fb_instance_id, funcName, args);
    }, 
    
    addRef: function() {
        this.bridge.addRef(this);
    }, 
    
    release: function() {
        this.bridge.release(this);
    }
};
