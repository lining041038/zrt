<%@page import="antelope.wcm.assets.BaseAsset"%>
<%@page import="java.util.List"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>
<jsp:include page="/include/header2.jsp"/>
<%
	List<BaseAsset> assetbeans = SpringUtils.getBeans(BaseAsset.class);
%>
<script>


$(function(){
	$("#repeat").buttonset();
	$("[name=assettype]:eq(0)").click().attr("checked", true);
	$( "#repeat" ).buttonset("refresh");
});

function setVisibleConfigdiv(idx) {
	$(".configdivclx").hide();
	$("#configdiv" + idx).show();
}

function getSelectedAssetData() {
	var sid = window['getSelected' + $("[name=assettype]:checked").val()]();
	
	if (sid == false)
		return false;
	
	var html = window['getSelected' + $("[name=assettype]:checked").val() + "html"](sid);
	
	var assetdata = {"sid":sid, "html":html, assettype: $("[name=assettype]:checked").val()};
	
	if (window['getSelected' + $("[name=assettype]:checked").val() + "jsfuncname"]) {
		assetdata['jsfunc'] = window['getSelected' + $("[name=assettype]:checked").val() + "jsfuncname"](sid);
	}
	
	if (window['getSelected' + $("[name=assettype]:checked").val() + "jsfuncdata"]) {
		assetdata['data'] = window['getSelected' + $("[name=assettype]:checked").val() + "jsfuncdata"](sid);
	}
	
	return assetdata;
}

</script>
</head>
<body class="sm_main" style="overflow: auto; text-align: center;">
	<span id="repeat" style="margin-top: 10px; display: block;">
		<%for (int i = 0; i < assetbeans.size(); ++i) { %>
			<input type="radio" id="repeat<%=i %>" name="assettype" value="<%=assetbeans.get(i).getAssetType()%>" onclick="setVisibleConfigdiv(<%=i %>)" /><label for="repeat<%=i%>"><%=assetbeans.get(i).getAssetTypeName() %></label>
		<%} %>
	</span>
	<%for (int i = 0; i < assetbeans.size(); ++i) {
		String includepage = "/wcm/assets/" + assetbeans.get(i).getAssetRelativePath() + "_config.jsp";
	%>
	<div id="configdiv<%=i%>" class="configdivclx" style="display: none;">
		<jsp:include page="<%=includepage %>"></jsp:include>
	</div>
	<%} %>
</body>
</html>




