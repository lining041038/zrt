package antelope.services;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import antelope.interfaces.OptionsProvider;
import antelope.interfaces.components.BaseUIOptions;
import antelope.services.supportclasses.Header2Params;
import antelope.services.supportclasses.Header2WebParams;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SpringUtils;
import antelope.utils.I18n;
import antelope.utils.JSONObject;
import antelope.utils.SystemOpts;
import antelope.utils.TextUtils;

/**
 * jsp服务类，将嵌入到jsp页面中的过多java代码迁移到类当中进行维护
 * @author lining
 * @since 2013-8-15
 */
@Service
public class JSPUtilService extends BaseComponent {
	
	/**
	 * 初始化header2
	 */
	public Header2Params initHeader2(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		req.setCharacterEncoding("utf-8");
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		
		SessionService service = (SessionService) req.getSession().getAttribute("service");
		TransactionStatus status = SpringUtils.beginTransaction();
		Object optsobj = null;
		String theme = "defaults";
		String opts = "{}";
		try {
			if (service != null) {
				 theme = service.getTheme();
			}
			
			// 获取部件options的json格式
			String component = req.getParameter("component");
			req.setAttribute("component", component);
			
			if (TextUtils.stringSet(component)) {
				OptionsProvider optsprovider = SpringUtils.getBean(OptionsProvider.class, component);
				optsobj = optsprovider.getOptions(req);
				
				if (optsobj instanceof BaseUIOptions) {
					((BaseUIOptions) optsobj).initOptions();
				}
				
				opts = new JSONObject(optsobj, true).toString();
			}
		} finally {
			SpringUtils.commitTransaction(status);
		}

		req.setAttribute("ctx", req.getContextPath());
		req.setAttribute("opts", optsobj);
		req.setAttribute("i18n", new I18n(req));
		
		Header2Params header2 = new Header2Params();
		header2.opts = opts;
		header2.theme = theme;
		return header2;
	}
	
	
	/**
	 * 初始化header2Web，用于一般网站网页（区别于系统后台管理或者纯管理系统）的页面头
	 */
	public Header2WebParams initHeader2Web(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		req.setCharacterEncoding("utf-8");
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		Header2WebParams header2 = new Header2WebParams();
		header2.theme = "zzcp";
		req.setAttribute("ctx", req.getContextPath());
		String webtheme = SystemOpts.getProperty("default.webtheme");
		if (stringSet(webtheme)) {
			header2.theme = webtheme;
		}
		return header2;
	}
}








