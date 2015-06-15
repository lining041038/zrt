<%@ page language="java" pageEncoding="utf-8"%>

<jsp:include page="/include/inc_plugs.jsp">
	<jsp:param name="file" value="system/sysoptsManageform/sysoptsscripts.jsp"/>
</jsp:include>
<div class="condi_container">
	
	<span class="cd_name">${i18n['single_datagrid.systemoptionsmanage.optionvalue'] }：</span>
	<span class="cd_val">
		<input name="value" required='true' maxlength2='500' state='disabled:false; disabled.view:true;'/>
	</span>
	
</div>
