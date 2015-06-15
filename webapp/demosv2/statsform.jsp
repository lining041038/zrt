<%@ page language="java" pageEncoding="utf-8"%>
<script>
function myStatsItemClick(item) {
	alert(JSON.stringify(item));
	location = ctx + "/demos/statschartgriddemo.jsp?ct=" + encodeURIComponent(encodeURIComponent(item.ct));
}
</script>
<div>
	<input name="test"/>
</div>