package antelope.system;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.james.JamesMailUtil;
import antelope.utils.I18n;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageUtil;
import antelope.utils.SystemOpts;

@Controller("receivedmails")
@RequestMapping("/system/mail/receivedmails/MailReceiveController")
public class MailReceiveController extends SingleDatagrid {

	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		I18n i18n = getI18n(req);
		
		opts.columns.put("mailsubject", new GridColumn(i18n.get("antelope.mail.mailsubject")));
		opts.columns.put("mailsender", new GridColumn(i18n.get("antelope.mail.mailsender")));
		opts.columns.put("maildate", new GridColumn(i18n.get("antelope.mail.mailsenddate")));
		opts.buttons.remove("update");
		opts.buttons.remove("del");
		return opts;
	}

	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		JamesMailUtil utl = getMailUtil(req);
		
		Message[] mailList = utl.getMailList(false);
		
		List<JSONObject> arr = new ArrayList<JSONObject>();
		for (Message message : mailList) {
			JSONObject obj = new JSONObject();
			obj.put("mailcontent", message.getContent());
			obj.put("mailsubject", message.getSubject());
			obj.put("mailsender", message.getFrom()[0]);
			obj.put("maildate", getNewSdf().format(now()));
			arr.add(obj);
		}
		
		PageItem page = PageUtil.getPage(getPageParams(req), arr);
		print(page, res);
	}
	
	@Override
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		JamesMailUtil util = getMailUtil(req);
		String receivers = d(req.getParameter("mailreceiver"));
		if (stringSet(receivers)) {
			String[] recierverarr = receivers.split(";");
			for (String string : recierverarr) {
				util.sendMessage(string, d(req.getParameter("mailsubject")), d(req.getParameter("mailcontent")), null);
			}
		}
	}
	
	private JamesMailUtil getMailUtil(HttpServletRequest req)
			throws SQLException, Exception, IOException {
		String encryptpwd = DBUtil.querySingleString("select password from SYS_USER where username=?", new Object[]{getService(req).getUsername()});
		JamesMailUtil utl = new JamesMailUtil(getService(req).getUsername(), encryptpwd, SystemOpts.getProperty("antelope_jamesdomain"));
		return utl;
	}
}










