(function($){
//--------------------------------	
	$Board = function(){
		this.getName = function(){
			alert("$Board");
		}
	}
	
	$Board.prototype.searchHref = function(argName){
		argName += "=";
		var value = location.search.substr(location.search.indexOf(argName)+argName.length);
		value = (value.indexOf("&") == -1)?value:(value.substr(0,value.indexOf("&")));
		return value;
	}
	
	$Board.prototype.drawImg = function(cs,imgSrc){
		var img = new Image();
		img.src = imgSrc;
		try {
			img.onload = function() {
				cs.drawImage(img,0,0);
			}
		} catch(e) {
			cs.drawImage(img,0,0);
		}
	}
	
	$Board.prototype.getSpeedId = function (){
		var guid = "";
		for ( var i = 1; i <= 32; i++) {
			var n = Math.floor(Math.random() * 16.0).toString(16);
			guid += n;
			if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
				guid += "-";
		}
		return guid;
	};
	
	$Board.extend = function(subClass,superClass){
	    var F = function(){};
	    F.prototype = superClass.prototype;
	    subClass.prototype = new F();
	    subClass.prototype.constructor = subClass;
	    subClass.superclass = superClass.prototype; //加多了个属性指向父类本身以便调用父类函数
	    if(superClass.prototype.constructor == Object.prototype.constructor){
	        superClass.prototype.constructor = superClass;
	    }
	}
	//--------------------------------
	
	$BoardDraw = function(arg){
		$Board.call(this);
		
		var $canvas = null;
		var cs = null;
		var ws = null;
		var self = this;
		
		var weight = 4;
		var color = "black";
		var type = "pen";
		var times = null;
		
		var pagesid = "";
		var backImgUrl = null;
		var totals = [];
		var dataval = [];
		
		var saveId = null;
		
		var supportTouch = false;
		var downevent = "mousedown";
		var moveevent = "mousemove";
		var	upevent = "mouseup";
		

		var toolbar = function(){
			if (arg.mainId&&arg.toolbarId && arg.toolbars) {
				var toolbar = '<div id="'+arg.toolbarId+'">';
				for ( var i = 0; i < arg.toolbars.length; i++) {
					var val = arg.toolbars[i];
					toolbar += '<div data-type="toolOption" data-operation="model:'+val+'" class="tool'+val+'"></div>';
				}
				toolbar+='</div>';
			}
			$('#'+arg.mainId).append(toolbar);
			
			$('#'+arg.toolbarId).append('<div data-type="toolOption"  data-operation="page:right" class="toolright"></div>');
			$('#'+arg.toolbarId).append('<div  id="pageNo" class="toolmiddle"><span>1</span></div>');
			$('#'+arg.toolbarId).append('<div data-type="toolOption"  data-operation="page:left" class="toolleft"></div>');
			
			if(arg.mainId&&arg.toolbarId && arg.colors){
				var colorsval = "";
				for ( var i = 0; i < arg.colors.length; i++) {
					var val = arg.colors[i];
					colorsval += '<div data-type="toolOption"  data-operation="color:'+val+'" class="tool'+val+'"></div>';
				}
				$('#'+arg.toolbarId).append(colorsval);
			}
			$('#'+arg.toolbarId).append('<div id="warndiv"><span>画笔绘画</span></div>');
			
			$('#'+arg.toolbarId).append("<div id='undoalldiv'><div>清除当前</div><div>清除所有</div></div>");
		}
		
		 var maincanvas = function(){
			if(arg.mainId&&arg.canvasId){
				$('#'+arg.mainId).after("<div id='hidecanvas'><div id='hidetools'></div></div>");
				var canvas = '<div id="container"><canvas id="'+arg.canvasId+'" width="100" height="100">该浏览器版本不支持，请升级浏览器！！</canvas></div>';
				$('#'+arg.mainId).append(canvas);
			}
		}
		
		var initSupportTouch = function(){
			supportTouch = "createTouch" in document;
			downevent = supportTouch?"touchstart" : "mousedown";
			moveevent = supportTouch ? "touchmove" : "mousemove";
			upevent = supportTouch ? "touchend": "mouseup";
		}
		
		var initToolBar = function(){
			var $toolbardiv = $('#'+arg.toolbarId).children("div[data-operation^='model:']");
			$toolbardiv.click(function(){
				$(this).addClass("pressbtn");
				$toolbardiv.not(this).removeClass("pressbtn");
				switch($(this).attr("data-operation")){
					case "model:eraser":
						$("#warndiv > span").text("橡皮擦");
						type="eraser";
						$("#undoalldiv").hide();
						var $colorList = $('#'+arg.toolbarId).children("div[data-operation^='color:']");
						$colorList.each(function(){
							var color_1 = $(this).attr("data-operation").substr(6);
							$(this).css("border","2px "+color_1+" solid")
						});
						break;
					case "model:undo":
						undoBoard();
						$("#undoalldiv").hide();
						break;
					case "model:pen":
						$("#warndiv > span").text("画笔绘画");
						type="pen";
						//color="black";
						break;
					case "model:delay":
						$("#hidecanvas").show();
						$("#warndiv > span").text("绘图重画中.....");
						delayBoard();
						//bardelay();
						break;
					case "model:undoall":
						$("#undoalldiv").toggle();
						break;
					case "model:save":
						console.log("保存当前绘画");
						//saveBorad();
						break;
				}
			});
			
			
			//换页
			var $pageList = $('#'+arg.toolbarId).children("div[data-operation^='page:']");
			$pageList.click(function(){
				$("#undoalldiv").hide();
				var pageNo = pages.getPageNumber();
				var pagesobj = null;
				console.log("换页同步数据");
				ws.syncData($canvas.get(0).toDataURL(),pages.getPageNumber()-1);
				saveBorad();
				switch($(this).attr("data-operation")){
					case "page:left":
						if(pageNo <= 1){
							break;
						}
						pagesobj =	pages.setPageNumber(--pageNo,totals);
						break;
					case "page:right":
						pagesobj =	pages.setPageNumber(++pageNo,totals);
						break;
				}
				if(pagesobj!=null){
					pagesid = pagesobj.pagesid;
					totals = pagesobj.totals;
					if(totals==null){
						totals = [];
					}
					$("#pageNo > span").text(pageNo);
					paintCurrentPage();
				}
			});
			
			//颜色切换
			var $colorList = $('#'+arg.toolbarId).children("div[data-operation^='color:']");
			$colorList.click(function(){
				$("#warndiv > span").text("画笔绘画");
				type="pen";
				$("#undoalldiv").hide();
				$(this).css("border","2px white solid");
				color = $(this).attr("data-operation").substr(6);
				$colorList.not(this).each(function(){
					var color_1 = $(this).attr("data-operation").substr(6);
					$(this).css("border","2px "+color_1+" solid")
				});
			});
			
			//清楚按钮
			var $undoallList = $("#undoalldiv").children("div");
			$undoallList.eq(0).bind("click",function(){
				undoallOnly();
				$("#undoalldiv").hide();
			});
			$undoallList.eq(1).bind("click",function(){
				undoallMore();
				$("#undoalldiv").hide();
			});
			
			//默认布局
			$toolbardiv.filter("[data-operation='model:pen']").addClass("pressbtn");
			$colorList.filter("[data-operation='color:black']").css("border","2px white solid");
			
		}
		
		var initCanvas = function(){
			if(arg.mainId&&arg.canvasId){
				$canvas = $('#'+arg.canvasId); 
				cs = $canvas.get(0).getContext("2d");
			}
		}
		
		
		var initBoard = function(){
			if(arg.mainId&&arg.canvasId){
				$canvas.bind(downevent,function(ev){
					ev = ev||event;
					ev.preventDefault();
					var disX = (downevent=="mousedown"?ev.clientX:ev.originalEvent.touches[0].clientX) - $canvas.offset().left + $(document).scrollLeft();
					var disY = (downevent=="mousedown"?ev.clientY:ev.originalEvent.touches[0].clientY) - $canvas.offset().top + $(document).scrollTop();
					
					var arr = [];
					var starttime = new Date().getTime();
					var tempstart = starttime;
					arr.push({"disX":disX,"disY":disY,"time":0});
					var delaytime = 0;
					if(!!times){
						delaytime = starttime - times;
					}
					times = starttime;
					
					cs.beginPath();
					cs.strokeStyle = color;
					cs.lineWidth = weight;
					
					cs.moveTo(disX,disY);
					if(type=="pen"){
						cs.lineWidth = 3;
						cs.rect(disX,disY,0.01,0.01);
						cs.stroke();
					}
					
					$canvas.closest("html").bind(moveevent,function(ev){
						ev = ev||event;
						ev.preventDefault();
						disX = (moveevent=="mousemove"?ev.clientX:ev.originalEvent.touches[0].clientX) - $canvas.offset().left + $(document).scrollLeft();
						disY = (moveevent=="mousemove"?ev.clientY:ev.originalEvent.touches[0].clientY) - $canvas.offset().top + $(document).scrollTop();
						
						if(disX < 0||disY < 0||disX > $canvas.width()||disY > $canvas.height()){
							cs.closePath();
							var endtime = new Date().getTime();
							times = endtime;
							arr.push({"type":type,"delaytime":delaytime,"usetime":endtime-starttime,"imgData":cs.getImageData(0,0,$canvas.width(),$canvas.height()),"color":color,"weight":weight});
							totals.push(arr);
							arr = null;
							$canvas.closest("html").unbind(moveevent);
							$canvas.closest("html").unbind(upevent);
							return;
						}
						
						var tempend = new Date().getTime();
						  
						cs.beginPath();
						
						cs.moveTo(arr[arr.length-1].disX,arr[arr.length-1].disY);
						if(type=="eraser"){
							cs.fillStyle="white";
							cs.moveTo(disX,disY);
							cs.arc(disX, disY, 15, 0, 2 * Math.PI);
							cs.fill();
						}else{
							cs.lineWidth = weight;
							cs.lineTo(disX,disY);
							cs.stroke();
							cs.moveTo(disX,disY);
							cs.lineWidth = 3;
							cs.rect(disX,disY,0.01,0.01);
							cs.stroke();
						}
						
						cs.closePath();
						
						arr.push({"disX":disX,"disY":disY,"time":tempend-tempstart});
						tempstart = tempend;
					});
					
					
					$canvas.closest("html").bind(upevent,function(ev){
						cs.closePath();
						var endtime = new Date().getTime();
						times = endtime;
						arr.push({"type":type,"delaytime":delaytime,"usetime":endtime-starttime,"imgData":cs.getImageData(0,0,$canvas.width(),$canvas.height()),"color":color,"weight":weight});
						totals.push(arr);
						arr = null;
						$canvas.closest("html").unbind(moveevent);
						$canvas.closest("html").unbind(upevent);
					});
				});
			}
		}
		
		
		var undoBoard = function(){
			if(totals.length <= 1){
				cs.clearRect(0,0,$canvas.width(),$canvas.height());
				totals.pop();
				if(pages.getLoadImg()[pages.getPageNumber()-1]==true){
					self.drawImg(cs,pages.getImgUrl()[pages.getPageNumber()-1]);
				}
				return;
			}
			totals.pop();
			var data = totals[totals.length-1];
			var imgData = data[data.length-1].imgData;
			//console.log(imgData);
			cs.putImageData(imgData,0,0);
			console.log("undoBoard");
		}
		
		
		var undoallBoard = function(){
			cs.clearRect(0,0,$canvas.width(),$canvas.height());
			totals = [];
			pages.getImgUrl()[pages.getPageNumber()-1] = null;
			pages.getLoadImg()[pages.getPageNumber()-1] = false;
			
			clearSyncData();
		}
		
		var undoallOnly = function(){
			cs.clearRect(0,0,$canvas.width(),$canvas.height());
			totals = [];
			pages.getLoadImg()[pages.getPageNumber() - 1] = false;
			pages.getImgUrl()[pages.getPageNumber() - 1] = null;
			pages.getTotal()[pages.getPageNumber() - 1] = [];
			clearSyncData(pages.getPageNumber() - 1);
			times = null;
		}
		
		var undoallMore = function(){
			cs.clearRect(0,0,$canvas.width(),$canvas.height());
			totals = [];
			$.each(pages.getPageTotal(), function(i){
				pages.getPageTotal()[i] = "";
				});
			$.each(pages.getLoadImg(), function(i){
				pages.getLoadImg()[i] = false;
				});
			$.each(pages.getTotal(), function(i){
				pages.getTotal()[i] = [];
				});
			$.each(pages.getImgUrl(), function(i){
			pages.getImgUrl()[i] = null;
				});
			clearSyncData();
			times = null;
		}
		
		var clearSyncData = function(val){
			var userId = self.searchHref("userId");
			var roomId = self.searchHref("roomId");
			var tempdata = {
					"userId":userId,
					"roomId":roomId,
					"flag":val
			};
			$.post("cleartotal.vot",{"data":JSON.stringify(tempdata)},function(){
				
			},"text");
		}
		
		var delayBoard = function(){
			cs.clearRect(0,0,$canvas.width(),$canvas.height());
			//同步数据换页
			if(pages.getLoadImg()[pages.getPageNumber()-1]==true){
				self.drawImg(cs,pages.getImgUrl()[pages.getPageNumber()-1]);
			}
			delayTotals(0);
			console.log("delayBoard");
		}
		
		delayTotals = function(length){
			if(length >= totals.length){
				times = new Date().getTime();
				if(!delay.value()){
					$("#hidecanvas").hide();
					$("#warndiv > span").text(type == "pen"?"画笔绘画":"橡皮擦");
					return;
				}
			}
			if(delay.value()){
				setTimeout("delayTotals("+length+")",500);
				return;
			}
			var data = totals[length];
			cs.beginPath();
			cs.lineWidth = data[data.length-1].weight;
			cs.strokeStyle = data[data.length-1].color;
			dataval = data;
			delay.enable();
			drawDelay(0);
			cs.closePath();
			var time = data[data.length-1].usetime;
			++length;
			data = totals[length];
			if(length <= totals.length-1){
				time += data[data.length-1].delaytime;
			}
			setTimeout("delayTotals("+length+")",time);
		}
		
		 drawDelay = function(length){
			if(!dataval){
				delay.disable();
				return;
			}
			
			if(length >= dataval.length - 1){
				delay.disable();
				return;
			}
			
			cs.beginPath();
			
			if(dataval[dataval.length-1].type=="pen"){
				if(length == 0){
					cs.lineWidth = 3;
					cs.moveTo(dataval[length].disX,dataval[length].disY);
					cs.rect(dataval[length].disX,dataval[length].disY,0.01,0.01);
					cs.stroke();
				}else{
					cs.lineWidth = 4;
					cs.moveTo(dataval[length-1].disX,dataval[length-1].disY);
					cs.lineTo(dataval[length].disX,dataval[length].disY);
					cs.stroke();
					cs.lineWidth = 3;
					cs.moveTo(dataval[length].disX,dataval[length].disY);
					cs.rect(dataval[length].disX,dataval[length].disY,0.01,0.01);
					cs.stroke();
				}
			}else{
				cs.fillStyle="white";
				cs.moveTo(dataval[length].disX,dataval[length].disY);
				cs.arc(dataval[length].disX,dataval[length].disY, 15, 0, 2 * Math.PI);
				cs.fill();
			}
			
			cs.closePath();
			
			++length;
			
			var time = 1;
			if(length <= dataval.length-1){
				time = dataval[length].time;
			}
			setTimeout("drawDelay("+length+")",time);
			
		}
		
		
		var initWindowSize = function(){
			var win_width = $(window).width();
			var win_height = $(window).height();
			var toolheight = $('#'+arg.toolbarId).height();
			$('#'+arg.canvasId).attr({"width":win_width-18,"height":win_height-toolheight-30});
			$("#hidecanvas").css({"width":win_width - 5,"height":win_height-10});
			$("#hidetools").css({"width":win_width - 5,"height":toolheight+10});
			/*if(navigator.userAgent.indexOf("Android") > -1){
				weight = 2;
			}*/
			$('#'+arg.userList).css({"left":win_width - 210,"top":toolheight + 10});
		}
		
		var initSaveId = function(){
			saveId = self.getSpeedId();
		}
		
		
		this.init = function(websocket){
			toolbar();
			maincanvas();
			initSupportTouch();
			initToolBar();
			initCanvas();
			initBoard();
			initWindowSize();
			ws = websocket;
			setTimeout(syncChangeData,3000);
			initSaveId();
		}
		
		
		var delay = (function(){//设置延时开关
			var delay = false;
			return {
				"enable": function(){delay = true;},
				"disable": function(){delay = false;},
				"value":function(){return delay;}
			}
		}());
		
		var pages = (function(){
			var pageNumber = 1;
			var pageTotals =[];
			var totals = [];
			var imgUrls = [];
			var loadImg= [];
			
			//初始化默认页
			pageTotals.push(self.getSpeedId());
			totals.push([]);
			imgUrls.push(null);
			loadImg.push(false);
			
			return {
				"getPageNumber":function(){return pageNumber;},
				"setPageNumber":function(pageNo,arr){
					if(pageNo <= 1){
						pageNo = 1;
					}
					if(!!arr){
						totals[pageNumber - 1] = arr;
					}
					//imgUrls[pageNumber - 1] = $canvas.get(0).toDataURL();
					pageNumber = pageNo;
					if(pageNo > pageTotals.length){
						pageTotals.push(self.getSpeedId());
						totals.push([]);
						imgUrls.push(cs.createImageData($canvas.width(),$canvas.height()));
						loadImg.push(false);
					}
					//console.log(pageNumber,pageTotals,totals);
					return {"pagesid":pageTotals[pageNumber-1],"totals":totals[pageNumber-1],"imageUrl":imgUrls[pageNumber-1]};
				},
				"setAll":function(totalval1,totalval2){
					pageTotals = totalval1;
					totals = totalval2;
					pageNumber = 1;
					return {"pagesid":pageTotals[0],"totals":totals[0]};
				},
				"getAll":function(){
					return {"pageTotals":pageTotals,"dataTotals":totals};
				},
				"clearAll":function(){
					var pageId = pageTotals[0];
					pageTotals = [];
					pageTotals.push(pageId);
					totals=[];
					totals.push([]);
					imgUrls = [];
					loadImg = [];
				},
				"getImgUrl":function(){
					return imgUrls;
				},
				"getPageTotal":function(){
					return pageTotals;
				},
				"getTotal":function(){
					return totals;
				},
				"getLoadImg":function(){
					return loadImg;
				},
				"setPagesImgData":function(pageNo,imgData){
					loadImg[pageNo - 1] = true;
					pageTotals[pageNo - 1] = [];
					totals[pageNo - 1] = [];
					imgUrls[pageNo - 1] = imgData
					pageNumber = pageNo;
					
					//永远加载第一页
					$("#pageNo > span").text("1");
					pageNumber = 1;
					cs.clearRect(0,0,$canvas.width(),$canvas.height());
					self.drawImg(cs,pages.getImgUrl()[0]);
				}
			}
		}());
		
		
		var paintCurrentPage = function(){
			cs.clearRect(0,0,$canvas.width(),$canvas.height());
			if(totals==null||totals.length<=0){
				//同步数据换页
				if(pages.getLoadImg()[pages.getPageNumber()-1]==true){
					self.drawImg(cs,pages.getImgUrl()[pages.getPageNumber()-1]);
				}
				return;
			}
			cs.beginPath();
			var arr = totals[totals.length - 1];
			cs.putImageData(arr[arr.length - 1].imgData,0,0);
			cs.closePath();
		}
		
		var syncChangeData = function(){
			console.log("同步数据");
			ws.syncData($canvas.get(0).toDataURL(),pages.getPageNumber()-1);
			saveBorad();
			setTimeout(syncChangeData,10000);
		}
		
		this.getArguments = function(){
			return arg;
		}
		
		this.getCS = function(){
			return cs;
		}
		
		this.getCanvas = function(){
			return $canvas;
		}
		
		this.setPagesImgData = function(pageNo,imgData){
			pages.setPagesImgData(pageNo,imgData);
		}
		
		
		//保存画板方法---待优化
		var saveBorad = function(){
			pagesobj =	pages.setPageNumber(pages.getPageNumber(),totals);
			var userId = self.searchHref("userId");
			var roomId = self.searchHref("roomId");
			for(var i = 0;i<pages.getPageTotal().length;i++){
				var total = pages.getTotal()[i];
				if(total == null){
					continue;
				}
				var temp_total = total.concat();
				if(temp_total==null||temp_total.length < 1){
					continue;
				}
				for(var j=0;j<temp_total.length;j++){
					temp_total[j] = temp_total[j].concat();
					var temp = temp_total[j];
					var obj = temp[temp.length-1];
					var temp_obj = {
							"type":obj.type,
							"delaytime":obj.delaytime,
							"usetime":obj.usetime,
							"imgData":null,
							"color":obj.color,
							"weight":obj.weight}
					temp[temp.length-1] = temp_obj;
				}
				
				var tempdata = {
						"roomId":roomId,
						"userId":userId,
						"saveId":saveId,
						"pageNo":i,
						"totalData":temp_total,
				};
				//console.log(JSON.stringify(tempdata));
				$.post("savetotal.vot",{"data":JSON.stringify(tempdata)},function(data){
				},"text");
			}
			//alert("保存成功");
		}
		
	}
	$Board.extend($BoardDraw,$Board);
	
	//-------------------------------
	$BoardShow = function(arg){
		$Board.call(this,arg);
		
		var $canvas = null;
		var cs = null;
		var self = this;
		
		var supportTouch = false;
		var downevent = "mousedown";
		var moveevent = "mousemove";
		var	upevent = "mouseup";
		
		
		var pagesid = "";
		var backImgUrl = null;
		var totals = [];
		var dataval = [];
		
		var slider = null;
		var globalBar = null;
		var globalTime = 0;
		
		
		var toolbar = function(){
			if (arg.mainId&&arg.toolbarId && arg.toolbars) {
				var toolbar = '<div id="'+arg.toolbarId+'">';
				for ( var i = 0; i < arg.toolbars.length; i++) {
					var val = arg.toolbars[i];
					toolbar += '<div data-type="toolOption" data-operation="model:'+val+'" class="tool'+val+'"></div>';
				}
				toolbar+='</div>';
			}
			$('#'+arg.mainId).append(toolbar);
			
			$('#'+arg.toolbarId).append('<div data-type="toolOption"  data-operation="page:right" class="toolright"></div>');
			$('#'+arg.toolbarId).append('<div  id="pageNo" class="toolmiddle"><span>1</span></div>');
			$('#'+arg.toolbarId).append('<div data-type="toolOption"  data-operation="page:left" class="toolleft"></div>');
			
			
			$('#'+arg.toolbarId).append('<div id="warndiv"><span>数据加载中。。。。</span></div>');
			
		}
		
		 var maincanvas = function(){
			if(arg.mainId&&arg.canvasId){
				$('#'+arg.mainId).after("<div id='hidecanvas'><div id='hidetools'></div></div>");
				var canvas = '<div id="container"><canvas id="'+arg.canvasId+'" width="100" height="100">该浏览器版本不支持，请升级浏览器！！</canvas></div>';
				$('#'+arg.mainId).append(canvas);
			}
		}
		 
		 var initSupportTouch = function(){
				supportTouch = "createTouch" in document;
				downevent = supportTouch?"touchstart" : "mousedown";
				moveevent = supportTouch ? "touchmove" : "mousemove";
				upevent = supportTouch ? "touchend": "mouseup";
			}
			
			var initToolBar = function(){
				var $toolbardiv = $('#'+arg.toolbarId).children("div[data-operation^='model:']");
				$toolbardiv.click(function(){
					$(this).addClass("pressbtn");
					$toolbardiv.not(this).removeClass("pressbtn");
					switch($(this).attr("data-operation")){
						case "model:delay":
							$("#hidecanvas").show();
							$("#warndiv > span").text("绘图重画中.....");
							delayBoard();
							break;
					}
				});
				
				
				//换页
				var $pageList = $('#'+arg.toolbarId).children("div[data-operation^='page:']");
				$pageList.click(function(){
					$("#slider").hide();
					var pageNo = pages.getPageNumber();
					var pagesobj = null;
					switch($(this).attr("data-operation")){
						case "page:left":
							if(pageNo <= 1){
								break;
							}
							pagesobj =	pages.setPageNumber(--pageNo,totals);
							break;
						case "page:right":
							pagesobj =	pages.setPageNumber(++pageNo,totals);
							break;
					}
					if(pagesobj!=null){
						pagesid = pagesobj.pagesid;
						totals = pagesobj.totals;
						$("#pageNo > span").text(pages.getPageNumber());
						paintCurrentPage();
					}
				});
				
			}
			
			var initCanvas = function(){
				if(arg.mainId&&arg.canvasId){
					$canvas = $('#'+arg.canvasId); 
					cs = $canvas.get(0).getContext("2d");
				}
			}
			
			var delayBoard = function(){
				if(pages.getLoadImg()[pages.getPageNumber()-1]==true&&(totals==null||totals.length<1)){
					alert("您没有查看绘画过程的权限");
					$("#hidecanvas").hide();
					$("#warndiv > span").text("数据加载完毕");
					globalBar.interrupt();
					slider.slider({
						value : globalTime
					});
					return;
				}
				
				updateGlobalTime();
				
				cs.clearRect(0,0,$canvas.width(),$canvas.height());
				//同步数据换页
			/*	if(pages.getLoadImg()[pages.getPageNumber()-1]==true){
					var img = new Image();
					img.src = pages.getImgUrl()[pages.getPageNumber()-1];
					cs.drawImage(img,0,0);
				}*/
				globalBar = new bar();
				cs.beginPath();
				//delayTotals(0,0,false)();
				globalBar.delay(0,0,false)();
				cs.closePath();
				globalBar.create(0)();
				$('#slider').show();
				console.log("delayBoard");
			}
			
			
			var updateGlobalTime = function(){
				 globalTime = 0;
				 for(var i=0;i<totals.length;i++){
					 data = totals[i];
					 console.log( data[data.length - 1].delaytime , data[data.length - 1].usetime);
					 globalTime += data[data.length - 1].delaytime + data[data.length - 1].usetime;
					/* if(i == (totals.length - 1)){
						 globalTime += data[data.length - 1].usetime;
					 }*/
				 }
				// console.log(globalTime);
				 slider.slider();
				 slider.slider({
						max : globalTime
					});
			}
			
			delayTotalsOld = function(length){
				
				if(totals==null||(length >= totals.length)){
					times = new Date().getTime();
					if(!delay.value()){
						$("#hidecanvas").hide();
						$("#warndiv > span").text("数据加载完毕");
						globalBar.interrupt();
						slider.slider({
							value : globalTime
						});
						return;
					}
				}
				if(delay.value()){
					setTimeout("delayTotals("+length+")",500);
					//globalBar.interrupt();
					/* slider.slider({
							max : globalTime
						});*/
					return;
				}
				var data = totals[length];
				cs.beginPath();
				cs.lineWidth = data[data.length-1].weight;
				cs.strokeStyle = data[data.length-1].color;
				dataval = data;
				delay.enable();
			/*	var currentTime = globalBar.current();
				globalBar = new bar();
				globalBar.create(currentTime)();*/
				drawDelay(0);
				cs.closePath();
				var time = data[data.length-1].usetime;
				++length;
				data = totals[length];
				if(length <= totals.length-1){
					time += data[data.length-1].delaytime;
				}
				
				setTimeout("delayTotals("+length+")",time);
			}
			
			 drawDelay = function(length){
					if(!dataval){
						delay.disable();
						return;
					}
					
					if(length >= dataval.length - 1){
						delay.disable();
						return;
					}
					
					cs.beginPath();
					
					if(dataval[dataval.length-1].type=="pen"){
						if(length == 0){
							cs.lineWidth = 3;
							cs.moveTo(dataval[length].disX,dataval[length].disY);
							cs.rect(dataval[length].disX,dataval[length].disY,0.01,0.01);
							cs.stroke();
						}else{
							cs.lineWidth = 4;
							cs.moveTo(dataval[length-1].disX,dataval[length-1].disY);
							cs.lineTo(dataval[length].disX,dataval[length].disY);
							cs.stroke();
							cs.lineWidth = 3;
							cs.moveTo(dataval[length].disX,dataval[length].disY);
							cs.rect(dataval[length].disX,dataval[length].disY,0.01,0.01);
							cs.stroke();
						}
					}else{
						cs.fillStyle="white";
						cs.moveTo(dataval[length].disX,dataval[length].disY);
						cs.arc(dataval[length].disX,dataval[length].disY, 15, 0, 2 * Math.PI);
						cs.fill();
					}
					
					cs.closePath();
					
					++length;
					
					var time = 1;
					if(length <= dataval.length-1){
						time = dataval[length].time;
					}
					setTimeout("drawDelay("+length+")",time);
					
				}
			
			 var type = "pen"; 
			 
				/*var delayTotals = function(totalLength,dataLength,delayFlag){
					return function(){
						$("#hidecanvas").hide();
						if(totals==null||(totalLength >= totals.length)){
							times = new Date().getTime();
							if(!delay.value()){
								$("#hidecanvas").hide();
								$("#warndiv > span").text("数据加载完毕");
								globalBar.interrupt();
								slider.slider({
									value : globalTime
								});
								return;
							}
						}
						if(delayFlag == true){
							for(var i = 0;i <= totalLength; i++ ){
								cs.closePath();
								cs.beginPath();
								var temp_data = totals[i];
								type = temp_data[temp_data.length-1].type;
								cs.strokeStyle = temp_data[temp_data.length-1].color;
								//cs.lineWidth = temp_data[temp_data.length-1].weight;
								for(j = 0;j < temp_data.length ; j++){
									if(type == "pen"){
										if(j == 0){
											cs.lineWidth = 3;
											cs.moveTo(temp_data[j].disX,temp_data[j].disY);
											cs.rect(temp_data[j].disX,temp_data[j].disY,0.01,0.01);
											cs.stroke();
										}else{
											cs.lineWidth = 4;
											cs.moveTo(temp_data[j - 1].disX,temp_data[j - 1].disY);
											cs.lineTo(temp_data[j].disX,temp_data[j].disY);
											cs.stroke();
											cs.lineWidth = 3;
											cs.moveTo(temp_data[j].disX,temp_data[j].disY);
											cs.rect(temp_data[j].disX,temp_data[j].disY,0.01,0.01);
											cs.stroke();
										}
									}else{
										cs.fillStyle="white";
										cs.moveTo(temp_data[j].disX,temp_data[j].disY);
										cs.arc(temp_data[j].disX,temp_data[j].disY, 15, 0, 2 * Math.PI);
										cs.fill();
									}
								}
							}
							delayFlag = false;
						}else{
							var temp_data = totals[totalLength];
							if(type == "pen"){
								if(dataLength == 0){
									cs.lineWidth = 3;
									cs.moveTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
									cs.rect(temp_data[dataLength].disX,temp_data[dataLength].disY,0.01,0.01);
									cs.stroke();
								}else{
									cs.lineWidth = 4;
									cs.moveTo(temp_data[dataLength - 1].disX,temp_data[dataLength - 1].disY);
									cs.lineTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
									cs.stroke();
									cs.lineWidth = 3;
									cs.moveTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
									cs.rect(temp_data[dataLength].disX,temp_data[dataLength].disY,0.01,0.01);
									cs.stroke();
								}
							}else{
								cs.fillStyle="white";
								cs.moveTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
								cs.arc(temp_data[dataLength].disX,temp_data[dataLength].disY, 15, 0, 2 * Math.PI);
								cs.fill();
							}
						}
						
						dataLength++;
						if(dataLength >= totals[totalLength].length){
							cs.closePath();
							cs.beginPath();
							totalLength++;
							if(totalLength >= totals.length ){
								return;
							}
							var temp_data = totals[totalLength];
							type = temp_data[temp_data.length - 1].type;
							cs.strokeStyle = temp_data[temp_data.length - 1].color;
							//cs.lineWidth = temp_data[temp_data.length - 1].weight;
							dataLength = 0;
							setTimeout(delayTotals(totalLength,dataLength,delayFlag),temp_data[temp_data.length - 1].delaytime);
						}else{
							setTimeout(delayTotals(totalLength,dataLength,delayFlag), totals[totalLength][dataLength].time);
						}
					}
				}*/
			
			var initWindowSize = function(){
				var win_width = $(window).width();
				var win_height = $(window).height();
				var toolheight = $('#'+arg.toolbarId).height();
				$('#'+arg.canvasId).attr({"width":win_width-18,"height":win_height-toolheight-80});
				$("#hidecanvas").css({"width":win_width - 5,"height":win_height-60});
				$("#hidetools").css({"width":win_width - 5,"height":toolheight+10});
				if(navigator.userAgent.indexOf("Android") > -1){
					weight = 2;
				}
				$('#'+arg.userList).css({"left":win_width - 210,"top":toolheight + 10});
				$('#slider').css({"left":10,"top":win_height-toolheight- 10,width:win_width-28});
				$('#slider').hide();
			}
			
			
			
		/*	var delay = (function(){//设置延时开关
				var delay = false;
				return {
					"enable": function(){delay = true;},
					"disable": function(){delay = false;},
					"value":function(){return delay;}
				}
			}());*/
			
			var pages = (function(){
				var pageNumber = 1;
				var pageTotals =[];
				var totals = [];
				var imgUrls = [];
				var loadImg= [];
				
				//初始化默认页
				pageTotals.push(self.getSpeedId());
				totals.push([]);
				imgUrls.push(null);
				loadImg.push(false);
				
				return {
					"getPageNumber":function(){return pageNumber;},
					"setPageNumber":function(pageNo){
						if(pageNo <= 1){
							pageNo = 1;
						}
						if(pageNo <= pageTotals.length){
							pageNumber = pageNo;
						}
						return {"pagesid":pageTotals[pageNumber-1],"totals":totals[pageNumber-1],"imageUrl":imgUrls[pageNumber-1]};
					},
					"getImgUrl":function(){
						return imgUrls;
					},
					"getPageTotal":function(){
						return pageTotals;
					},
					"getTotal":function(){
						return totals;
					},
					"getLoadImg":function(){
						return loadImg;
					},
					"setPagesImgData":function(pageNo,imgData){
						loadImg[pageNo - 1] = true;
						pageTotals[pageNo - 1] = [];
						totals[pageNo - 1] = [];
						imgUrls[pageNo - 1] = imgData
						pageNumber = pageNo;
					}
				}
			}());
			
			
			var paintCurrentPage = function(){
				cs.beginPath();
				cs.clearRect(0,0,$canvas.width(),$canvas.height());
				if(pages.getLoadImg()[pages.getPageNumber()-1]==true){
					self.drawImg(cs,pages.getImgUrl()[pages.getPageNumber()-1]);
				}
				cs.closePath();
			}
		 
		 var loadData= function(){
			 var roomId = self.searchHref("roomId");
			 var userId = self.searchHref("userId");
			 var showId = self.searchHref("showId");
			 console.log(roomId,userId);
			 var data = {
					 "roomId":roomId,
					 "userId":userId,
					 "showId":showId
			 }
			 $("#hidecanvas").show();
			 jQuery.ajaxSettings.async = false; 
			 $.post("gettotal.vot",{"data":JSON.stringify(data)},function(data){
				 data = JSON.parse(data);
				 //console.log(data);
				 for(var i = 0;i < data.length ;i++){
					 var temp_data = data[i];
					//console.log("--------");
					// console.log(temp_data.dataTotal);
					 pages.getPageTotal()[temp_data.pageNo]="";
					 pages.getTotal()[temp_data.pageNo]=temp_data.dataTotal;
					 
					 pages.getImgUrl()[temp_data.pageNo]=temp_data.imgUrl;
					 pages.getLoadImg()[temp_data.pageNo]=true;
					 
				 }
			 },"text");
			jQuery.ajaxSettings.async = true; 
			var pagesobj =	pages.setPageNumber(1);
			if(pagesobj!=null){
				pagesid = pagesobj.pagesid;
				totals = pagesobj.totals;
				$("#pageNo > span").text(pages.getPageNumber());
				paintCurrentPage();
			}
			 $("#hidecanvas").hide();
			 $("#warndiv > span").text("数据加载完毕");
			 
		 }
		 
		 
			
		 this.show = function(){
			toolbar();
			maincanvas();
			initSupportTouch();
			initToolBar();
			initCanvas();
			initWindowSize();
			loadData();
			initSlider();
		 }
		 
		 var initSlider = function(){
			 slider = $("#slider");
			 if(totals==null||totals.length < 1){
				 return;
			 }
			 globalTime = 0;
			 for(var i=0;i<totals.length;i++){
				 data = totals[i];
				 console.log( data[data.length - 1].delaytime , data[data.length - 1].usetime);
				 globalTime += data[data.length - 1].delaytime + data[data.length - 1].usetime;
				/* if(i == (totals.length - 1)){
					 globalTime += data[data.length - 1].usetime;
				 }*/
			 }
			 //console.log(globalTime);
			 slider.slider();
			 slider.slider({
					max : globalTime
				});
			 slider.slider({
					step : 1
				});
			 slider.slider({
					slide : function(event, ui) {
						console.log(ui.value);
						$("#hidecanvas").show();
						$("#warndiv > span").text("绘图重画中.....");
						globalBar.interrupt();
						globalBar = new bar();
						globalBar.create(ui.value)();
						var current = getTimeLocation(ui.value);//totalLength":i,"dataLength":j
						console.log(current);
						globalBar.delay(current.totalLength,current.dataLength,true)();
					}
				});
				
		 }
		 
		 var moveSlider = function(val){
				slider.slider({value:val});
			}
		 
		 var bar = function(){
				var interrupt = false;
				return {
					"create":function(val){
						return function(){
							if(interrupt){
								return;
							}
							moveSlider(val);
							var value = slider.slider( "option", "value");
							var max = slider.slider( "option", "max");
							//console.log(value,max);
							if(val > slider.slider( "option", "max")){
								return;
							}
							var s2 = setTimeout(globalBar.create(val+25),25);
						}
					},
					"interrupt":function(){
						interrupt = true;
					},
					"resume":function(){
						interrupt = false;
					},
					"current":function(){
						return slider.slider( "option", "value" );
					},
					"delay":function(totalLength,dataLength,delayFlag){
						//console.log(totalLength,dataLength,delayFlag);
						return function(){
							//console.log(interrupt);
							if(interrupt){
								return;
							}
							//$("#hidecanvas").hide();
							cs.beginPath();
							if(totals==null||(totalLength >= totals.length)){
								times = new Date().getTime();
								//if(!delay.value()){
									$("#hidecanvas").hide();
									$("#warndiv > span").text("数据加载完毕");
									globalBar.interrupt();
									slider.slider({
										value : globalTime
									});
									return;
								//}
							}
							if(delayFlag == true){
								//debugger;
								cs.clearRect(0,0,$canvas.width(),$canvas.height());
								for(var i = 0;i <= totalLength; i++ ){
									var temp_data = totals[i];
									var type = temp_data[temp_data.length-1].type;
									cs.strokeStyle = temp_data[temp_data.length-1].color;
									cs.lineWidth = temp_data[temp_data.length-1].weight;
									for(j = 0;j < temp_data.length ; j++){
										if(i >= totalLength && j > dataLength){
											break;
										}
										cs.beginPath();
										if(type == "pen"){
											if(j == 0){
												cs.lineWidth = 3;
												cs.moveTo(temp_data[j].disX,temp_data[j].disY);
												cs.rect(temp_data[j].disX,temp_data[j].disY,0.01,0.01);
												cs.stroke();
											}else{
												cs.lineWidth = 4;
												cs.moveTo(temp_data[j - 1].disX,temp_data[j - 1].disY);
												cs.lineTo(temp_data[j].disX,temp_data[j].disY);
												cs.stroke();
												cs.lineWidth = 3;
												cs.moveTo(temp_data[j].disX,temp_data[j].disY);
												cs.rect(temp_data[j].disX,temp_data[j].disY,0.01,0.01);
												cs.stroke();
											}
										}else{
											cs.fillStyle="white";
											cs.moveTo(temp_data[j].disX,temp_data[j].disY);
											cs.arc(temp_data[j].disX,temp_data[j].disY, 15, 0, 2 * Math.PI);
											cs.fill();
										}
										cs.closePath();
									}
								}
								delayFlag = false;
							}else{
								var temp_data = totals[totalLength];
								var type = temp_data[temp_data.length - 1].type;
								cs.strokeStyle = temp_data[temp_data.length - 1].color;
								//cs.lineWidth = temp_data[temp_data.length - 1].weight;
								//var temp_data = totals[totalLength];
								if(type == "pen"){
									if(dataLength == 0){
										cs.lineWidth = 3;
										cs.moveTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
										cs.rect(temp_data[dataLength].disX,temp_data[dataLength].disY,0.01,0.01);
										cs.stroke();
									}else{
										cs.lineWidth = 4;
										cs.moveTo(temp_data[dataLength - 1].disX,temp_data[dataLength - 1].disY);
										cs.lineTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
										cs.stroke();
										cs.lineWidth = 3;
										cs.moveTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
										cs.rect(temp_data[dataLength].disX,temp_data[dataLength].disY,0.01,0.01);
										cs.stroke();
									}
								}else{
									cs.fillStyle="white";
									cs.moveTo(temp_data[dataLength].disX,temp_data[dataLength].disY);
									cs.arc(temp_data[dataLength].disX,temp_data[dataLength].disY, 15, 0, 2 * Math.PI);
									cs.fill();
								}
							}
							
							dataLength++;
							cs.closePath();
							if(dataLength >= totals[totalLength].length){
								totalLength++;
								if(totalLength >= totals.length ){
									$("#hidecanvas").hide();
									$("#warndiv > span").text("数据加载完毕");
									globalBar.interrupt();
									slider.slider({
										value : globalTime
									});
									return;
								}
								var temp_data = totals[totalLength];
								dataLength = 0;
								setTimeout(globalBar.delay(totalLength,dataLength,delayFlag),temp_data[temp_data.length - 1].delaytime-10);
							}else{
								setTimeout(globalBar.delay(totalLength,dataLength,delayFlag), totals[totalLength][dataLength].time);
							}
						}
					}
				};
			} 
		 
		 var getTimeLocation = function(valtime){
			 if(totals.length < 1){return;}
			 var currentTime = 0;
			 for(var i = 0;i < totals.length;i++){
				 var data_temp = totals[i];
				 currentTime += data_temp[data_temp.length-1].delaytime + data_temp[data_temp.length-1].usetime;
				// debugger;
				 if(currentTime > valtime){
					 currentTime = currentTime -  data_temp[data_temp.length-1].usetime;
					 for(var j = 0;j < data_temp.length;j++){
						 currentTime += data_temp[j].time;
						 if(currentTime > valtime){
							 //console.log({"totalLength":i,"dataLength":j},"===========");
							 if(i > 1&& j==0){
								 return {"totalLength":i - 1,"dataLength":totals[i - 1].length - 1};
							 }
							 return {"totalLength":i,"dataLength":j};
						 }
					 }
				 }
			 }
		 }
		 
	}
	$Board.extend($BoardShow,$Board);
	//--------------------------------
	
	
	///-------------------------------
	
	
	$WebSocket = function(){
		var ws = null;
		var board = null;
		var self = this;

		
		var initWebSocket = function(){
			var url = location.href.match(/^(http:\/\/)((localhost||(\d{1,3}\.){3}\d{1,3}):\d{1,}\/\w{1,})(.*)/);
			var wsstr = "ws://" + url[2] + "/loadBoard" + location.search;
			console.log(wsstr);
			if ('WebSocket' in window) {
				ws = new WebSocket(wsstr);
			} else if ('MozWebSocket' in window) {
				ws = new MozWebSocket(wsstr);
			} else {
				alert("not support");
			}
			
			ws.onmessage = mesWebSocket;
			ws.onopen = openWebSocket;
			ws.onclose = closeWebSocket;
			
		}
		
		
		var mesWebSocket = function(data){
			if(board == null){
				return;
			}
			console.log("获取同步数据");
			data = JSON.parse(data.data);
			
			if(data.model=='onopen'){
				board.setPagesImgData(data.pageNo+1,data.imgData);
			}else{
				if($('#user'+data.userId).size() < 1){
					var username = data.userId;
					if(username.length > 8){
						username = username.substr(0,8)+"...";
					}
					$('#'+board.getArguments().userList).append("<div id='user"+data.userId+"'>"+username+"<img alt='ss' src='images/left.png' style='width:180px;height:100px;'></div>");
					$('#user'+data.userId).bind("click",function(){
						window.open("showwork.html?roomId="+self.searchHref("roomId")+"&showId="+self.searchHref("userId")+"&userId="+data.userId);
					});
				}
				$('#user'+data.userId).find("img").attr("src",data.imgData);
			}
		}
		
		var openWebSocket = function(){
			console.log("openWebSocket");
			counter.reset();
		}
		
		var closeWebSocket = function(){
			console.log("closeWebSocket");
			if(counter.get() > 0){
				console.log("断开重连中....");
				initWebSocket();
				counter.del();
			}else{
				alert("您已断开连接");
			}
		}
		
		this.createWebSocket=function(boardval){
			board = boardval;
			initWebSocket();
			return ws;
		}
		
		this.syncData = function(data,sid){
			var userId = self.searchHref("userId");
			var roomId = self.searchHref("roomId");
			var tempData = {
					"roomId":roomId,
					"userId":userId,
					"model":"unicast",
					"pageNo":sid,
					"imgData":data
			};
			if((ws!=null)||(ws.readyState)){
				ws.send(JSON.stringify(tempData));
			}
		}
		
		var counter =(function(){
			var num = 5;
			return {
				"reset":function(){
					num = 5;
				},
				"del":function(){
					--num;
				},
				"get":function(){
					return num;
				}
			}
			
		})();
		
	}
	
	$Board.extend($WebSocket, $Board);
	//--------------------------------
})(jQuery);