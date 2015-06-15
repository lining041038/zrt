package antelope.wcm.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import com.opensymphony.xwork2.util.TextUtils;

import antelope.db.DBUtil;
import antelope.services.SystemPageInfoService;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SpringUtils;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.JSONObject;
import antelope.utils.RegExpUtil;
import antelope.utils.SpeedIDUtil;
import antelope.utils.SystemOpts;
import antelope.wcm.beans.QueryHandler;
import antelope.wcm.beans.WCMTemplateInfoBean;
import antelope.wcm.beans.WebPageDataBean;
import antelope.wcm.consts.WCMSiteSettingConsts;
import antelope.wcm.entities.WCMSiteTemplateSettingItem;

@Service
public class TemplateInfoService extends BaseComponent {
	
	public WCMTemplateInfoBean getTemplateInfoBySid(String sid) throws IOException {
		
		File tempfolder = ClasspathResourceUtil.getWebappFolderFile("/wcm/templates");
		String[] folderNames = tempfolder.list();
		
		WCMTemplateInfoBean infobean = new WCMTemplateInfoBean();
		
		for (String object : folderNames) {
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream(tempfolder.getAbsolutePath() + "/" + object + "/template.properties");
			props.load(fis);
			fis.close();
			if (props.getProperty("sid").equals(sid)) {
				infobean.sid = sid;
				infobean.name = props.getProperty("name");
				infobean.path = "/wcm/templates/" + object;
				return infobean;
			}
		}
		
		return null;
	}
	
	public void addOrUpdateSetting(String sitesid, String settingcode, String value) throws Exception {
		List<WCMSiteTemplateSettingItem> setting = dao.query("select * from WCM_SITE_TEMPLATE_SETTING where sitesid=? and settingcode=?", new Object[]{sitesid, settingcode}, WCMSiteTemplateSettingItem.class);
		
		if (setting.size() > 0) {
			setting.get(0).settingvalue = value;
			dao.insertOrUpdate(setting.get(0));
		} else {
			WCMSiteTemplateSettingItem newsetting = new WCMSiteTemplateSettingItem();
			newsetting.name = WCMSiteSettingConsts.getName(settingcode);
			newsetting.settingcode = settingcode;
			newsetting.settingvalue = value;
			newsetting.sid = SpeedIDUtil.getId();
			newsetting.sitesid = sitesid;
			dao.insertOrUpdate(newsetting);
		}
		
	}
	
	
	public String getSettingStr(String sitesid, String settingcode) throws Exception {
		List<WCMSiteTemplateSettingItem> setting = dao.query("select * from WCM_SITE_TEMPLATE_SETTING where sitesid=? and settingcode=?", new Object[]{sitesid, settingcode}, WCMSiteTemplateSettingItem.class);
		if (setting.size() > 0) {
			return setting.get(0).settingvalue;
		}
		return null;
	}
	
	
	public JSONObject getJSON(String sitesid, String settingcode) throws Exception {
		List<WCMSiteTemplateSettingItem> setting = dao.query("select * from WCM_SITE_TEMPLATE_SETTING where sitesid=? and settingcode=?", new Object[]{sitesid, settingcode}, WCMSiteTemplateSettingItem.class);
		
		if (setting.size() > 0) {
			return new JSONObject(setting.get(0).settingvalue);
		}
		return null;
	}
	
	/**
	 * 获取所有模板关联配置数据
	 * @param sitesid
	 * @return
	 * @throws Exception
	 */
	public JSONObject getJSON(String sitesid) throws Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<WCMSiteTemplateSettingItem> setting = null;
		try {
			setting = dao.query("select * from WCM_SITE_TEMPLATE_SETTING where sitesid=?", new Object[]{sitesid}, WCMSiteTemplateSettingItem.class);
		} finally {
			SpringUtils.commitTransaction(status);
		}
		JSONObject obj = new JSONObject();
		for (WCMSiteTemplateSettingItem wcmSiteTemplateSettingItem : setting) {
			obj.put(wcmSiteTemplateSettingItem.settingcode, wcmSiteTemplateSettingItem.settingvalue);
		}
		
		return obj;
	}
	
	public static WebPageDataBean getWebPageData(HttpServletRequest req) throws Exception {
		return SpringUtils.getBean(TemplateInfoService.class).getWebPageDataInner(req.getParameter("sitesid"), req);
	}
	
	
	public static String getActivatedTemplatePath(HttpServletRequest req) throws SQLException, Exception {
		TemplateInfoService bean = SpringUtils.getBean(TemplateInfoService.class);
		
		String domainname = RegExpUtil.getFirstMatched("(?<=http://)[^/:]*(?=[:/])", req.getRequestURL().toString());
		String templatesid = DBUtil.querySingleString("select templatesid from WCM_SITE where activated='1' and domainname=?", new Object[]{domainname});
		if (stringSet(templatesid)) {
			WCMTemplateInfoBean templateInfo = bean.getTemplateInfoBySid(templatesid);
			return templateInfo.path;
		} else {
			templatesid = DBUtil.querySingleString("select templatesid from WCM_SITE where activated='1'", new Object[0]);
			if (stringSet(templatesid)) {
				WCMTemplateInfoBean templateInfo = bean.getTemplateInfoBySid(templatesid);
				return templateInfo.path;
			}	
		}
		SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
		
		return SystemOpts.getProperty("loginpagepos");
	}
	
	private WebPageDataBean getWebPageDataInner(String sitesid, HttpServletRequest req) throws Exception {

		// 若未找到sitesid则寻找是否有激活的sitesid直接访问
		if (!TextUtils.stringSet(sitesid)) {
			String activatedsitesid = (String) doQueryInTrans(new QueryHandler() {
				public Object execute() throws Exception {
					return DBUtil.querySingleString("select sid from WCM_SITE where activated='1'", new Object[0]);
				}
			});
			sitesid = activatedsitesid;
		}
		
		final String innersitesid = sitesid;
		String templatepath = (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				String templatesid = DBUtil.querySingleString("select templatesid from WCM_SITE where sid=?", new Object[]{innersitesid});
				WCMTemplateInfoBean templateInfo = getTemplateInfoBySid(templatesid);
				return templateInfo.path;
			}
		});
		
		return new WebPageDataBean(getJSON(sitesid), sitesid, templatepath, req);
	}

	
	private Object doQueryInTrans(QueryHandler handler) throws Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		Object execute = null;
		try {
			execute = handler.execute();
		} finally {
			SpringUtils.commitTransaction(status);
		}
		return execute;
	}
}










