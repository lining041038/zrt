<%@page import="antelope.utils.JSONArray"%>
<%@page import="antelope.interfaces.components.supportclasses.GridColumn"%>
<%@page import="antelope.utils.TextUtils"%>
<%@page import="antelope.utils.JSONObject"%>
<%@page import="antelope.interfaces.components.supportclasses.StatsChartgridTab"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="antelope.utils.I18n"%>
<%@page import="antelope.interfaces.components.supportclasses.StatsChartgridOptions"%>
<%@ page language="java" pageEncoding="utf-8"%>
<% request.setAttribute("componentVersion", "v2"); %>
<jsp:include page="/include/header2.jsp"/>
<%
	StatsChartgridOptions opts = (StatsChartgridOptions) request.getAttribute("opts");
	I18n i18n = (I18n) request.getAttribute("i18n");
	String component = request.getParameter("component");
%>
<title>${i18n['antelope.systemname']}</title>
<style></style>
<script type="text/javascript">

$(function(){
	
	$(window).resize(function(){
		$(".tabcontent").height($(this).height()-$("#form1").height()-$("#tabul").height() - 10);
	}).resize();
	$( ".tabs" ).bind( "tabsactivate", function(event, ui) {
		setTimeout(function(){
			$(ui.newPanel).layout({
				fxName:'none',
				west: { 
					size:'53%'
				}
			});
			read($(ui.newPanel).find(".tbiner").attr("id"));
		}, 100);
	});
	<%
	int layouti = 0;
	for (Entry<String, StatsChartgridTab> entry: opts.tabs.entrySet()) {
		layouti++;
		if (TextUtils.stringSet(opts.selectedTabKey)) {
			if (entry.getKey().equals(opts.selectedTabKey)) {
				%>read("<%=entry.getKey()%>");<%
				break;
			}
		} else {
	%>
	read("<%=entry.getKey()%>");
	<%break;}}%>
	$('#tabs-<%=layouti%>').layout({
		fxName:'none',
		west: { 
			size:'53%'
		}
	});
});
var currenttype = "";
var typechartbrandmap = {};
var jspquerystring = "<%=TextUtils.noNull(request.getQueryString())%>";
function read(key) {
	currenttype = key;
	$.getJSON(ctx + opts.urlprefix + "/getData.vot?<%=request.getQueryString()%>&key="+key + "&" + encodeURI($("#form" + key).serialize()), function(data){
		<%
			int indx = 0;
			for (Entry<String, StatsChartgridTab> entry: opts.tabs.entrySet()) {
				// 获取label枚举
				String labelEnum = null;
				GridColumn column = entry.getValue().columns.get(entry.getValue().chartLabelField);
				if (column != null)
					labelEnum = column.enumXml; 
		%>
			typechartbrandmap['<%=entry.getKey()%>'] = '<%=entry.getValue().chartBrand%>';	
		
			if (key == '<%=entry.getKey()%>') {
				var currpagelist = [];
				// .datagrid("destroy")
				
				if ($("#"+key).data("uiDatagrid")) {
				//	$("#"+key).datagrid("destroy");	
				}
				$("#"+key).datagrid({
					dataProvider:data,
					usingTinyPageArea:true,
					pagebuttonnum:0,
					gridDrillField: "<%=TextUtils.noNull(entry.getValue().gridDrillField)%>",
					numPerPage:<%=entry.getValue().numPerPage%>,
					pageChange: function(currlist) {
						currpagelist = currlist;
						if ($("#" + key + "chart").data("uiFusionchart")) {
							$("#" + key + "chart").<%=entry.getValue().chartBrand%>("option", "dataProvider", currlist);
						}
					},
					columns:<%=new JSONObject(entry.getValue().columns, true)%>
				});
				
				<%if (opts.chartShowPrevNum != null){ %>
				var data2 = [];
				for (var k = 0; k < data.length && k < <%=opts.chartShowPrevNum%>; ++k) {
					data2.push(data[k]);
				}
				data = data2;
				<%}%>
				<%if (entry.getValue().series == null || entry.getValue().series.length == 0) {%>
				if ($("#" + key + "chart").data("inited")) {
					// $("#" + key + "chart").<%=entry.getValue().chartBrand%>("option", "dataProvider", currpagelist);
				} else {
					$("#" + key + "chart").<%=entry.getValue().chartBrand%>({
						wmode:'transparent',
						dataProvider:currpagelist,
						itemClick: "<%=opts.chartItemClickFunction%>",
						chartItemClickParamFields:<%=new JSONArray(opts.chartItemClickParamFields)%>,
						labelField:"<%=entry.getValue().chartLabelField%>",
						<%if (TextUtils.stringSet(labelEnum)) {%>
						labelEnum: "<%=labelEnum%>",
						<%}%>
						<%if (TextUtils.stringSet(entry.getValue().displayName)) {%>
						displayName: "<%=entry.getValue().displayName%>",
						<%}%>
						interval:1,
						field:"<%=entry.getValue().chartField%>",
						type:'<%=entry.getValue().chartType%>',
						title:"<%=entry.getValue().chartTitle%>",
						columnChartType:"<%=entry.getValue().columnChartType%>"
					});
					$("#" + key + "chart").data("inited", true);
				}
				<%} else {%>
				
				var tmpseries = [];
				<%for (int j = 0; j < entry.getValue().series.length; ++j) {%>
				tmpseries.push (
						{dataProvider:currpagelist, 
							<%if (TextUtils.stringSet(labelEnum)) {%>
							labelEnum: "<%=labelEnum%>",
							<%}%>
							<%if (TextUtils.stringSet( entry.getValue().series[j].displayName)) {%>
							displayName: "<%=entry.getValue().series[j].displayName%>",
							<%}%>
							field:"<%=entry.getValue().series[j].chartField%>"
						})
				<%}%>
				if ($("#" + key + "chart").data("inited")) {
					$("#" + key + "chart").<%=entry.getValue().chartBrand%>("option", "series", tmpseries);
				} else {
					$("#" + key + "chart").<%=entry.getValue().chartBrand%>({
						labelField:"<%=entry.getValue().chartLabelField%>",
						wmode:'transparent',
						series: tmpseries,
						itemClick: "<%=opts.chartItemClickFunction%>",
						chartItemClickParamFields:<%=new JSONArray(opts.chartItemClickParamFields)%>,
						interval:1,
						type:'<%=entry.getValue().chartType%>',
						title:"<%=entry.getValue().chartTitle%>",
						columnChartType:"<%=entry.getValue().columnChartType%>"
					});
					$("#" + key + "chart").data("inited", true);
				}
				<%} %>
			}
		<%} %>
	});
}

function exportit() {
	$.exportStatsExcel(currenttype, typechartbrandmap[currenttype]);
}

function changeToDrillLinkFunction(obj) {
	return "<a href='javascript:void(0)' style='color:#0066cc;' onclick='clickDrillLinkTitle(this)'>" + obj[this.gridDrillField] + "</a>";
}

function clickDrillLinkTitle(thisObj) {
	<%if (TextUtils.stringSet(opts.chartItemClickFunction)) {%>
		<%=opts.chartItemClickFunction%>($("#" + currenttype).datagrid("getCurrList")[$(thisObj).closest("td").attr("rowIndex")]);
	<%}%>
}

</script>
</head>
<body class="sm_main">
   	<div class="tabs">
		<ul id="tabul" style="float: left;">
			<%
			int i = 1;
			for (Entry<String, StatsChartgridTab> entry: opts.tabs.entrySet()) { %>
			<li><a href="#tabs-<%=i++ %>" tabkey="<%=entry.getKey()%>"
			<%if (TextUtils.stringSet(opts.selectedTabKey) && opts.selectedTabKey.equals(entry.getKey())) { %>selected2="true"<%} %>
			><%=entry.getValue().label %></a></li>
			<%} %>
		</ul>
		<button onclick='read(currenttype)' class="smallbutton" style="float: left;" type="button">${i18n['antelope.query'] }</button>
		<button onclick='exportit()' class="smallbutton" style="float: left; margin-left: 10px;" type="button">${i18n['antelope.exp'] }</button>
		<%
		i = 1;
		for (Entry<String, StatsChartgridTab> entry: opts.tabs.entrySet()) { %>
		<div id="tabs-<%=i++ %>" class="tabcontent" style="clear: left;">
			<div class="ui-layout-west" style="overflow: auto; width: 400px;">
				<form id="form<%=entry.getKey()%>">
					<%if (TextUtils.stringSet(entry.getValue().formKey)) { %>
					<jsp:include page="<%=entry.getValue().formKey %>"></jsp:include>
					<%} %>
				</form>
				<div id="<%= entry.getKey()%>" class="tbiner"></div>
			</div>
			<div class="ui-layout-center">
				<div id="<%= entry.getKey()%>chart" style="height: 100%;"></div>
			</div>
		</div>
		<%} %>
	</div>
</body>
</html>
