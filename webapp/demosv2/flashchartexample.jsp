<%@page import="antelope.utils.I18n"%>
<%@page import="com.sun.xml.internal.fastinfoset.sax.Properties"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<script>
$(function(){
	/* */
	$("#dsfds7").flashchart({
		title: "合规报告营业部统计图表",
		labelField: "label",
		style:'qxj',
		type:"columnChart3D",
		columnChartType:"100%",
		axisVisible: false,
		legendVisible: false,
		titleVisible: false,
		valueAxisMinimum:-25,
		valueAxisMaximum:125,
		field: "result", 
		series:[
		   {displayName:"序列一", dataProvider:[{
			  label: "测试一", result:1
			}, {
			  label: "测试二", result:30
			}, {
			  label: "测试四", result:70
			}
		   ]
		}, {displayName:"序列二", fillColor:0xA0F0A0, dataProvider:[{
			  label: "测试一", result:35
			}, {
			  label: "测试二", result:30
			}, {
			  label: "测试四", result:1
			}
		   ]
		}]
	});
	/**/
	$("#dsfds2").flashchart({
		style:'qxj',
		title: "廓线图",
		labelField: "label",
		titleVisible:false,
		type:"lineChart2",
		displayName:"放的地方",
		field: "result", 
		markerHeight:10,
		categoryTitle:"测试轮困",
		valueTitle:"值标题过长的时候看看是什么样一个情况",
		dataProvider: [{
			  label: "100", result:23, label2: "测试一新", result2: 40
		},{
			  label: "20", result:53, label2: "测试一新", result2: 55
		},{
			  label: "30", result:43, label2: "测试一新", result2: 20
		},{
			  label: "3", label2: "测试一新", result2: 20
		},{
			  label: "1", label2: "测试一新", result2: 20
		},{
			  label: "40", result:43, label2: "测试一新", result2: 20
		},{
			  label: "50", result:43, label2: "测试一新", result2: 20
		},{
			  label: "70", result:43, label2: "测试一新", result2: 20
		}
	   ]
	});
	$("#dsfds4").flashchart({
		style:'qxj',
		title: "廓线图",
		labelField: "label",
		titleFontSize:10,
		type:"lineChart",
		displayName:"放的地方",
		field: "result", 
		axisLabelPadding:40,
		markerHeight:10,
		categoryTitle:"测试轮困",
		valueTitle:"值标题",
		numToVertCategoryLabel:10,
		dataProvider: [{
			  label: "2012-05-03 03:33:11", result:23, label2: "测试一新", result2: 40
		},{
			  label: "2012-05-04 03:33:11", result:53, label2: "测试一新", result2: 55
		},{
			  label: "2012-05-05 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-08 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-03 03:33:11", result:23, label2: "测试一新", result2: 40
		},{
			  label: "2012-05-04 03:33:11", result:53, label2: "测试一新", result2: 55
		},{
			  label: "2012-05-05 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-08 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-03 03:33:11", result:23, label2: "测试一新", result2: 40
		},{
			  label: "2012-05-04 03:33:11", result:53, label2: "测试一新", result2: 55
		},{
			  label: "2012-05-05 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-08 03:33:11", result:43, label2: "测试一新", result2: 20
		}
		
	   ]
	});
	
	/*
	$("#dsfds3").flashchart({
		wmode:'transparent',
		dataProvider:[{"result":1,"advisory_dept":"合规管理总部"}],
		labelField:"advisory_dept",
		type: "lineChart",
		field:"result",
		displayName:"营业部门名称",
		title:"部门咨询量统计"
	});*/
	/**/
	$("#dsfds3").flashchart({
		style:'qxj',
		title: "廓线图",
		categoryTitle:"测试轮困2",
		valueTitle:"值标题2",
		labelField: "label",
		type:"columnChart",
	//	fillColor:0x00ff00,
		displayName:"放的地方",
		field: "result", 
		categoryTitle:"测试轮困",
		valueTitle:"值标题",
		markerHeight:10,
		dataProvider: [{
			  label: "2012-05-03 03", result:-10, label2: "测试一新", result2: 40
		},{
			  label: "2012-06-03 0", result:300, label2: "测试一新", result2: 55
		},{
			  label: "2012-07-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-08-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-09-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-03 03", result:-10, label2: "测试一新", result2: 40
		},{
			  label: "2012-06-03 0", result:300, label2: "测试一新", result2: 55
		},{
			  label: "2012-07-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-08-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-09-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-03 03", result:-10, label2: "测试一新", result2: 40
		},{
			  label: "2012-06-03 0", result:300, label2: "测试一新", result2: 55
		},{
			  label: "2012-07-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-08-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-09-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-03 03", result:-10, label2: "测试一新", result2: 40
		},{
			  label: "2012-06-03 0", result:300, label2: "测试一新", result2: 55
		},{
			  label: "2012-07-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-08-03 0", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-09-03 0", result:43, label2: "测试一新", result2: 20
		}
	   ]
	});
	/**/
	$("#dsfds").flashchart({
		type : "blowDirection",
		blowBgColorField: "bgcolor",
		blowLabelFontSize:9,
		titleFontSize: 10,
		tileWidth:140,
		tileHeight:155,
		title: "防守打法三大四方达",
		dataProvider: getRandomBlow(),
		style:'qxj'
	});
	
	
	$("#dsfds5").flashchart({
		style:'qxj',
		title: "廓线图",
		labelField: "label",
		type:"columnChart",
		displayName:"放的地方",
		field: "result", 
		axisLabelPadding:40,
		markerHeight:10,
		showChartSelect: false,
		categoryTitle:"km/h",
		valueTitle:"m3",
		numToVertCategoryLabel:10,
		dataProvider: [{
			  label: "2012-05-03 03:33:11", result:23, label2: "测试一新", result2: 40, bgcolor:0xff0000
		},{
			  label: "2012-05-04 03:33:11", result:53, label2: "测试一新", result2: 55, bgcolor:0x00ff00
		},{
			  label: "2012-05-05 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-08 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-09 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-10 03:33:11", result:43, label2: "测试一新", result2: 20
		}]
	});
	
	
	$("#dsfds6").flashchart({
		wmode:'transparent',
		dataProvider: [{"paiming":1, "lawexplain":"现行有效", "ct":3}],
		labelField: "lawexplain",
		displayName:"放的地方",
		interval: 1,
		field: "ct",
		type: 'pieChart',
		title: "解读数量分布"
	});
});

function getRandomBlow() {
	var arr = [];
	for(var i = 0; i < 15; i++) {
		arr.push({label:Math.round(Math.random()*100), value: Math.round(Math.random()*1000), bgcolor: 0xff * Math.random() * 0x10000 + 
			0xff * Math.random() * 0x100 + 0xff * Math.random()});
	}
	
	return arr;
}

function testone() {
	$("#dsfds3").flashchart("option", "series" , [
                              		   {displayName:"序列一", dataProvider:[{
                             			  label: "测试一", result:23
                             			}, {
                             			  label: "<font color='#ff0000' size='15'><b>测试二<b></font>", result:40
                             			}, {
                             			  label: "测试四", result:35
                             			}
                             		   ]
                             		} ,{displayName:"序列二", dataProvider:[{
                           			  label: "测试一", result:231
                         			}, {
                         			  label: "<font color='#ff0000' size='15'><b>测试二<b></font>", result:30
                         			}, {
                         			  label: "测试四", result:10
                         			}
                         		   ]
                         		}]);
}


function testmulte() {
	$("#dsfds7").flashchart("option", "dataProvider", [{
		  label: "测试一", result:23, label2: "测试一新", result2: 30
	},{
		  label: "测试二", result:33, label2: "测试二新", result2: 30
	}
   ]);
}

function testchangtochange() {
	$("#dsfds3").flashchart("option", "dataProvider", [{
		  label: "测试一", result:23, label2: "测试一新", result2: 30
	}
 ]);
}

function testnonenone() {
	$("#dsfds3").flashchart("option", "series" , []);
}

function changeCategoryField() {
	$("#dsfds3").flashchart("option", "labelField" , "label2");
}

function changefield() {
	$("#dsfds3").flashchart("option", "field" , "result2");
}

function changeDisplayName() {
	$("#dsfds3").flashchart("option", "displayName" , "更改了图例");
}

function changeLeftBiaoji() {
	$("#dsfds2").flashchart("option", "valueTitle" , "更改了左标记");
}


function displayImg() {
	$("#dsfds").flashchart("showImage");
}

function modifydirect(thisObj) {
	$("#dsfds").flashchart("option", "dataProvider", getRandomBlow());
}

function changeDataProvider() {
	
	var sss = [];
	for(var i = 0; i < 180; i++) {
		sss.push({
			label: "2018-18" + i, result:Math.floor(Math.random()*100), label2: "测试一新", result2: 40
		});
	}
	$("#dsfds4").flashchart("option", "dataProvider", sss);
	
	$("#dsfds5").flashchart("option", "dataProvider", sss);
	
	$("#dsfds3").flashchart("option", "dataProvider", sss);
}

function testVcd() {
	$("#dsfds2").flashchart("option", "valueTitle", "valueTitle奋斗");
	$("#dsfds2").flashchart("option", "categoryTitle", "categoryTitle奋斗");
	$("#dsfds2").flashchart("option", "displayName", "displayName奋斗");
	$("#dsfds2").flashchart("option", "dataProvider",  [{
		  label: "100", result:13, label2: "测试一新", result2: 40
	},{
		  label: "20", result:23, label2: "测试一新", result2: 55
	},{
		  label: "30", result:53, label2: "测试一新", result2: 20
	},{
		  label: "3", label2: "测试一新", result2: 20
	},{
		  label: "1", label2: "测试一新", result2: 20
	},{
		  label: "40", result:23, label2: "测试一新", result2: 20
	},{
		  label: "50", result:13, label2: "测试一新", result2: 20
	},{
		  label: "70", result:33, label2: "测试一新", result2: 20
	}
   ]);
	
	$("#dsfds4").flashchart("option", 'field', 'result2');
	
	$("#dsfds4").flashchart("option", 'dataProvider',
		[{
			  label: "2012-05-03 03:33:11", result:13, label2: "测试一新", result2: 40
		},{
			  label: "2012-05-04 03:33:11", result:23, label2: "测试一新", result2: 55
		},{
			  label: "2012-05-05 03:33:11", result:33, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-08 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-03 03:33:11", result:23, label2: "测试一新", result2: 40
		},{
			  label: "2012-05-04 03:33:11", result:53, label2: "测试一新", result2: 55
		},{
			  label: "2012-05-05 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-08 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-03 03:33:11", result:23, label2: "测试一新", result2: 40
		},{
			  label: "2012-05-04 03:33:11", result:53, label2: "测试一新", result2: 55
		},{
			  label: "2012-05-05 03:33:11", result:43, label2: "测试一新", result2: 20
		},{
			  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
		},{
			  label: "2012-05-08 03:33:11", result:43, label2: "测试一新", result2: 20
		}
		
	   ]);
}

</script>
</head>
<body style="height: 800px; overflow: scroll;">
<div style="height: 2000px;">
<button onclick="testone()">测试一项</button>
<button onclick="testVcd()">dsfds2_valueTitle_categoryTitle_displayName_dataProvider</button>
<button onclick="testmulte()">测试转多项</button>
<button onclick="testchangtochange()">测试转单项</button>
<button onclick="testnonenone()">测试传空</button>
<button onclick="changeCategoryField()">测试更改横坐标</button>
<button onclick="changeDataProvider()">测试更改数据值</button>
<button onclick="changefield()">测试更改数据域</button>
<button onclick="changeDisplayName()">测试更改图例</button>
<button onclick="changeLeftBiaoji()">测试更改左标记</button>
<button onclick="displayImg()">显示二进制图片</button>
<button onclick="modifydirect()">测试旋转角度</button>
<!---->

<div id="dsfds" style="height: 300px; width:500px;float: left; border: 1px solid green">
</div> 
 
<div id="dsfds2" style="height: 500px; width:500px;float: left;">
</div>
 
<div id="dsfds3" style="height: 500px; width:500px;float: left;">
</div>
<div id="dsfds4" style="height: 500px; width:500px;float: left;">
</div>
<div id="dsfds5" style="height: 500px; width:500px;float: left;">
</div>

<div id="dsfds6" style="height: 500px; width:500px;float: left;">
</div>

<div id="dsfds7" style="height: 125px; width:500px;float: left; clear: right;">
</div>

<!-- 动态完整仪表盘  -->
<div style="font-size: 15px;">完整仪表盘部分</div>
<script>

$(function(){
	$("#dsfds8").flashchart({
		chartsTileWidth:500,
		wmode:'transparent',
		chartsTileHeight:515,
		charts:{
			mypiechart:{
				type: 'pieChart',
				dataProvider: [{"paiming":1, "lawexplain":"现行有效", "ct":3},{"paiming":2, "lawexplain":"现行有效2", "ct":5}],
				labelField: "lawexplain",
				displayName:"放的地方",
				interval: 1,
				field: "ct",
				title: "解读数量分布"
			}, mylinechart:{
				style:'qxj',
				title: "廓线图",
				labelField: "label",
				titleVisible:false,
				type:"lineChart2",
				displayName:"放的地方",
				field: "result", 
				markerHeight:10,
				categoryTitle:"测试轮困",
				valueTitle:"值标题",
				series: [{
						displayName:"序列一", lineColor:0xff0000,
						dataProvider: [{
							  label: "100", result:23, label2: "测试一新", result2: 40
						},{
							  label: "20", result:53, label2: "测试一新", result2: 55
						},{
							  label: "30", result:43, label2: "测试一新", result2: 20
						},{
							  label: "3", label2: "测试一新", result2: 20
						},{
							  label: "1", label2: "测试一新", result2: 20
						},{
							  label: "40", result:43, label2: "测试一新", result2: 20
						},{
							  label: "50", result:43, label2: "测试一新", result2: 20
						},{
							  label: "70", result:43, label2: "测试一新", result2: 20
						}
					   ]
					},{
						displayName:"序列二",  lineColor:0xffff00,
						dataProvider: [{
							  label: "100", result:13, label2: "测试一新", result2: 40
						},{
							  label: "20", result:33, label2: "测试一新", result2: 55
						},{
							  label: "30", result:23, label2: "测试一新", result2: 20
						},{
							  label: "3", label2: "测试一新", result2: 20
						},{
							  label: "1", label2: "测试一新", result2: 20
						},{
							  label: "40", result:33, label2: "测试一新", result2: 20
						},{
							  label: "50", result:13, label2: "测试一新", result2: 20
						},{
							  label: "70", result:23, label2: "测试一新", result2: 20
						}
					   ]
					}]
				
			},
			mycolchart:{
				style:'qxj',
				title: "廓线图",
				labelField: "label",
				type:"columnChart",
				columnChartType:"clustered",
				displayName:"放的地方",
				field: "result", 
				axisLabelPadding:40,
				markerHeight:10,
				showChartSelect: false,
				categoryTitle:"km/h",
				valueTitle:"m3",
				numToVertCategoryLabel:10,
				series: [
                		   {displayName:"序列一", dataProvider:[{
                  			  label: "测试一", result:23
                  			}, {
                  			  label: "<font color='#ff0000' size='15'><b>测试二<b></font>", result:40
                  			}, {
                  			  label: "测试四", result:35
                  			}
                  		   ]
                  		}, {displayName:"序列二", dataProvider:[{
                			  label: "测试一", result:231
              			}, {
              			  label: "<font color='#ff0000' size='15'><b>测试二<b></font>", result:30
              			}, {
              			  label: "测试四", result:10
              			}
              		   ]
              		}]
			},
			myblowchart:{
				type : "blowDirection",
				blowBgColorField: "bgcolor",
				blowLabelFontSize:9,
				titleFontSize: 10,
				tileWidth:100,
				tileHeight:115,
				title: "防守打法三大四方达",
				dataProvider: getRandomBlow(),
				style:'qxj'
			}
		}
	});
});

function yibiaopanColumnDataChange() {
	$("#dsfds8").flashchart("option", "charts", {
		mycolchart: {
			dataProvider: [{
				  label: "2012-05-03 03:33:11", result:13, label2: "测试一新", result2: 40, bgcolor:0xff0000
			},{
				  label: "2012-05-04 03:33:11", result:223, label2: "测试一新", result2: 55, bgcolor:0x00ff00
			},{
				  label: "2012-05-05 03:33:11", result:313, label2: "测试一新", result2: 20
			},{
				  label: "2012-05-06 03:33:11", label2: "测试一新", result2: 20
			},{
				  label: "2012-05-07 03:33:11", label2: "测试一新", result2: 20
			},{
				  label: "2012-05-08 03:33:11", result:433, label2: "测试一新", result2: 20
			},{
				  label: "2012-05-09 03:33:11", result:523, label2: "测试一新", result2: 20
			},{
				  label: "2012-05-10 03:33:11", result:613, label2: "测试一新", result2: 20
			}]
		}
	});
}

function yibiaopanblowDataChange() {
	$("#dsfds8").flashchart("option", "charts", {
		myblowchart: {
			dataProvider: getRandomBlow()
		}
	});
}

function yibiaopanPieDataChange() {
	$("#dsfds8").flashchart("option", "charts", {
		mypiechart: {
			dataProvider: [{"paiming":1, "lawexplain":"现行有效", "ct":5},{"paiming":2, "lawexplain":"现行有效2", "ct":2}]
		}
	});
}

function yibiaopanLineDataChange() {
	$("#dsfds8").flashchart("option", "charts", {
		 mylinechart:{
				series: [{
						displayName:"序列一", lineColor:0xff0000,
						dataProvider: [{
							  label: "100", result:13, label2: "测试一新", result2: 40
						},{
							  label: "20", result:33, label2: "测试一新", result2: 55
						},{
							  label: "30", result:33, label2: "测试一新", result2: 20
						},{
							  label: "3", label2: "测试一新", result2: 20
						},{
							  label: "1", label2: "测试一新", result2: 20
						},{
							  label: "40", result:43, label2: "测试一新", result2: 20
						},{
							  label: "50", result:43, label2: "测试一新", result2: 20
						},{
							  label: "70", result:43, label2: "测试一新", result2: 20
						}
					   ]
					},{
						displayName:"序列二",  lineColor:0xffff00,
						dataProvider: [{
							  label: "100", result:13, label2: "测试一新", result2: 40
						},{
							  label: "20", result:33, label2: "测试一新", result2: 55
						},{
							  label: "30", result:23, label2: "测试一新", result2: 20
						},{
							  label: "3", label2: "测试一新", result2: 20
						},{
							  label: "1", label2: "测试一新", result2: 20
						},{
							  label: "40", result:33, label2: "测试一新", result2: 20
						},{
							  label: "50", result:13, label2: "测试一新", result2: 20
						},{
							  label: "70", result:23, label2: "测试一新", result2: 20
						}
					   ]
					}]
				
			}
	});
}

</script>
<div>
<button onclick="yibiaopanColumnDataChange()">测试柱形图数据变化</button>
<button onclick="yibiaopanblowDataChange()">测试风向图数据变化</button>
<button onclick="yibiaopanPieDataChange()">测试饼状图数据变化</button>
<button onclick="yibiaopanLineDataChange()">测试线图数据变化</button>
</div>
<div id="dsfds8" style="height: 1000px; width:100%;float: left;">
</div>
</div>
</body>
</html>