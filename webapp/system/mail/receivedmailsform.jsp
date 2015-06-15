<%@ page language="java" pageEncoding="utf-8"%>

<div style="width: 686px; height: 480px;">
	<div class="condi_container" includeIn="create">
		<span class="cd_name">${i18n['antelope.mail.mailreceiver'] }:</span>
		<span class="cd_val">
			<input name="mailreceiver" required="true" state="disabled:false;disabled.view:true"/>${i18n['antelope.mail.mailreceivertip'] }
		</span>
	</div>
	<div class="condi_container" includeIn="view">
		<span class="cd_name">${i18n['antelope.mail.mailsender'] }:</span>
		<span class="cd_val">
			<input name="mailsender" required="true" state="disabled:false;disabled.view:true"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.mail.mailsubject'] }:</span>
		<span class="cd_val">
			<input name="mailsubject" required="true" state="disabled:false;disabled.view:true"/>
		</span>
	</div>
	<div class="condi_container">
		<span class="cd_name">${i18n['antelope.mail.mailcontent'] }:</span>
		<span class="cd_val">
			<textarea name="mailcontent" style="width: 600px; height: 350px;" class="tinymce" state="disabled:false;disabled.view:true"></textarea>
		</span>
	</div>
</div>
