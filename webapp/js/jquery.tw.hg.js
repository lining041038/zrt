
// timewalking名空间对象
// dai jun 2011-12-18
var tw = { // 初始化为空，供大家注册
		// page: 翻页部分组件


};

// debug 属性
// 显示一个对象所有属性
tw.debug_showProperties = function(obj){
	var pStr = "";
	for (pros in obj){
		pStr += " "+pros;
	}
	alert(pStr);
}

// 功能类组件
// 生成二级下拉选择框
/*
(function ($,undefined){
	$.widget( "ui.baidusearch", {



	});
})(jQuery);

*/

/*
tw.matrix2LevelSel = {
		var matrixHtml = $('<span class="cd_item2"><span class="cd_name" >业务条线：</span> <span class="cd_val"> \
				<select required="true"  name="busi_MATRIX_PROJECT" id="busi_MATRIX_PROJECT" dataProvider="/common/workflow/matrixlines.vot" state="disabled:true;disabled.deptAudit:false;disabled.create:false;disabled.modify:false"> \
				<option>请选择业务条线</option></select></span></span> \
				<span class="cd_item2"><span class="cd_name" >业务项：</span><span class="cd_val"> \
				<select required="true" name="busi_MATRIX_ITEM" state="disabled:true;disabled.create:false;disabled.modify:false"> \
				<option>请选择业务项</option></select></span></span>');
		matrixHtml.appendTo(target);
		$(target.find("[name=busi_MATRIX_PROJECT]")).bind('change',function(event){
			var matrixitem = $("[name=busi_MATRIX_ITEM]").attr("dataProvider", "/common/workflow/matrixitemsbylinesid.vot?parentCode="+this.value);
			$("[name=busi_MATRIX_ITEM]").html("").parent().initWidgets();
		});

}
*/
// 基础页面类组件
// 翻页组件,用于生成翻页部分，并且能传入点击翻页部分的回调函数。
// 使用时调用_createPagingArea方法。参数参见方法定义。
tw.page = {
	_totalPage:1, // 总页数
	_currPage:1,  // 当前页
	_pre:1,       // 上页
	_next:1,	  // 下页
	_count:0,     // 总记录数
	// 翻页页面对象
	_pagepos:null, // 分页总区域
	_prebutton:null,
	_nextbutton:null,
	_numpagebutton:null, // 具体每个页的按钮
	_currpagefield:null, // 跳转页的输入框
	_gopagebutton:null,  // 跳转页的跳转按钮
	// 创建数字翻页按钮
	_createNumpageBtn: function(thisObj, intval) {
		if (this._currPage == intval)
			return '<input class="num_page num_page_focus" type="button" value="' + intval + '">';
		else
			return '<input class="num_page" type="button" value="' + intval + '">';
	},
	// 创建翻页区域
	_createPagingArea:function(rootObj,pageCallback,iCount,iCurrPage,iPre,iNext,iTotalPage){ // 创建
		this._count = iCount;
		this._currPage = iCurrPage;
		this._pre = iPre;
		this._next = iNext;
		this._totalPage = iTotalPage;

		this._pagepos = $('<div class="pagepos"></div>');
		this._prebutton=$('<input class="prev_page" type="button" value="上一页"/>');
		this._nextbutton=$('<input class="next_page" type="button" value="下一页"/>');
		this._currpagefield=$('<input class="currpage" value="1">');
		this._gopagebutton=$('<input class="gopage" type="button" value="确定">');

		var thisObj = this;
		if (thisObj._currPage){
			thisObj._currpagefield.val(thisObj._currPage);
		}
		var numbtn = Math.min(rootObj.options.pagebuttonnum, thisObj._totalPage); //中间按钮个数
		var beginpage = Math.max(thisObj._currPage - parseInt(numbtn / 2), 1);
		var endpage = Math.min(thisObj._totalPage, beginpage + numbtn);
		beginpage = Math.max(endpage - numbtn, 1);
		var numpagestr = '';
		for (var i = beginpage; i < endpage; i++) {
			numpagestr += thisObj._createNumpageBtn(thisObj, i);
		}
		if (endpage < thisObj._totalPage)
			numpagestr += '<span>...</span>';

		numpagestr += thisObj._createNumpageBtn(thisObj, thisObj._totalPage);
		if (thisObj._numpagebutton)
			thisObj._numpagebutton.remove();
		thisObj._numpagebutton = $(numpagestr);

		thisObj._nextbutton.before(thisObj._numpagebutton);

		thisObj._pagepos.append(thisObj._prebutton);
		thisObj._pagepos.append(thisObj._numpagebutton);
		thisObj._pagepos.append(thisObj._nextbutton);
		thisObj._pagepos.append(thisObj._currpagefield);
		thisObj._pagepos.append(thisObj._gopagebutton);

		// 翻页按钮点击事件响应
		// 下页
		thisObj._nextbutton.click(function(){
			pageCallback(rootObj,thisObj._currPage+1);
		});
		// 上页
		thisObj._prebutton.click(function(){
			pageCallback(rootObj,thisObj._currPage-1);
		});
		// 页面按钮
		thisObj._numpagebutton.click(function(){
			if (thisObj._currPage == this.value)
				return;
			thisObj._currPage = this.value;
			pageCallback(rootObj,this.value);
		});
		// 转到
		thisObj._gopagebutton.click(function(){
			if (thisObj._currPage == thisObj._currpagefield.val())
				return;
			thisObj._currPage = thisObj._currpagefield.val();
			pageCallback(rootObj,thisObj._currPage);
		});
		return thisObj._pagepos;
	}

},


(function ($,undefined){
	$.widget( "ui.baidusearch", {
		options: {
			dataProvider:null, // 为请求的目标地址
			titleField:'title',
			titleMaxLen: 30, //标题的最大字数，超过最大字数，将以...替换
			digestField:'digest',
			digestMaxLen: 80, //摘要的最大字数，超过最大字数，将以...替换
			sourceField:'source',
			timeField:'time',
			linkCallback:$.noop,
			pagebuttonnum: 5, // 上一页、下一页中间的翻页按钮数目
			numPerPage:8,
			keywords:null // 可以为'A'也可以为['A','B']
		},

		_init: function() {
			this._callSearch(this);
		},

		_callSearch: function(obj,begin, size){
			// 清空数据
			obj.element.empty();

			// 发出请求
			var params = obj.options.paramData;
			if (null == begin){
				params.begin = 0;
			}else{
				params.begin = begin;
			}
			if (null ==size ){
				params.size = 20;
			}else{
				params.size = size;
			}
			var thisObj = obj;
			$.getJSON(thisObj.options.dataProvider,params,function(myReturnData){
				var inReturnData;
				var returnData;
				// 兼容后台翻页的
				if (myReturnData.keys){
					returnData = myReturnData.page;
					inReturnData = returnData.currList;
					thisObj.options.keywords = myReturnData.keys.split(/\s+/);
				}else{
					inReturnData = myReturnData.currList;
					returnData = myReturnData;
				}
				var pageArea = tw.page._createPagingArea(thisObj,thisObj._callSearch,returnData.count,returnData.currPage,returnData.pre,returnData.next,returnData.totalPage);
				// 参数校验
				if (!$.isArray(inReturnData)){
					return;
				}
				//将请求返回的数据显示
				var htmlUL = $('<ul class="searchresults"></ul>');
				var htmllist ="";
				$.each(inReturnData,function(index){
					htmllist += '<li><div class="stitlediv"><a rownum="'+index+'" target="_blank" href='+ctxPath+this.url+' class="stitlediv">';
					// 准备关键字
					var regexp = null;
					if ($.isArray(thisObj.options.keywords)) {
						regexp = new RegExp("("+thisObj.options.keywords.join("|")+")","ig");
					} else {
						regexp = new RegExp("("+thisObj.options.keywords+")","ig");
					}
					// 标题部分
					var title = this[thisObj.options.titleField];
					if (title.length > thisObj.options.titleMaxLen)
						title = title.substring(0,thisObj.options.titleMaxLen)+"...";

					htmllist += title.replace(regexp,'<span class="titlekeyword">$1</span>');
					htmllist += '</a></div><div class="digestdiv">';
					// 摘要部分
					var digest = this[thisObj.options.digestField];
					if (undefined ==digest || null == digest){
						digest = "";
					}else if (digest.length > thisObj.options.digestMaxLen){
						digest = digest.substring(0,thisObj.options.digestMaxLen)+"...";
					}
					htmllist += digest.replace(regexp,'<span class="titlekeyword">$1</span>');
					htmllist += '</div><div class="sourcediv">';
					// 来源和时间等部分
					htmllist += this[thisObj.options.sourceField] +" ";
					htmllist += this[thisObj.options.timeField];
					htmllist += '</div></li>';
					// 将这条搜索结果记录添加至ul
				});
				htmlUL.append($(htmllist));
				// 添加翻页相关

				htmlUL.append(pageArea);
				thisObj.element.append(htmlUL);
			});
		},

		destroy: function() {
			this.element.empty();
			$.Widget.prototype.destroy.apply( this, arguments );
		}

	});

})(jQuery);

