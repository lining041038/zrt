package antelope.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import antelope.consts.GlobalConsts;
import antelope.services.SessionService;

public class I18n extends HashMap<String, String>{
	private static final long serialVersionUID = 1L;

	private static Map<String, Properties> props = new HashMap<String, Properties>();
	
	private SessionService service;
	
	public I18n(HttpServletRequest req) {
		HttpSession session = req.getSession();
		this.service = SessionUtil.getSessionService(null, session);
	}
	
	public I18n(HttpSession session) {
		this.service = SessionUtil.getSessionService(null, session);
	}
	
	public I18n(SessionService sessionService) {
		this.service = SessionUtil.getSessionService(sessionService, null);
	}
	
	/**
	 * 获取属性
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	private String getPropertyInner(String key) throws IOException {
		if (key == null)
			return null;
		
		String localestr = service.getLocale();
		return get(key, localestr);
	}
	
	public static String get(String key, String localestr) throws IOException {
		
		String labelstr = "";
		if (!props.containsKey(localestr)) {
			Properties propsinner = new Properties();
			propsinner.load(I18n.class.getResourceAsStream("/i18n/messages_antelope_" + localestr + ".properties"));
			propsinner.load(I18n.class.getResourceAsStream("/i18n/messages_components_" + localestr + ".properties"));
			propsinner.load(I18n.class.getResourceAsStream("/i18n/messages_project_" + localestr + ".properties"));
			props.put(localestr, propsinner);
		}
		
		if (GlobalConsts.isDevelopMode) {
			Properties propsinner = new Properties();
			InputStream is = ClasspathResourceUtil.getInputStreamByPathNoCached("/i18n/messages_antelope_" + localestr + ".properties");
			propsinner.load(is);
			is.close();
			is = ClasspathResourceUtil.getInputStreamByPathNoCached("/i18n/messages_components_" + localestr + ".properties");
			propsinner.load(is);
			is.close();
			is = ClasspathResourceUtil.getInputStreamByPathNoCached("/i18n/messages_project_" + localestr + ".properties");
			propsinner.load(is);
			is.close();
			props.put(localestr, propsinner);
		}
		
		labelstr = props.get(localestr).getProperty(key);
		
		if (TextUtils.stringSet(labelstr))
			return new String(labelstr.getBytes("ISO-8859-1"), "utf-8");
		
		// 当为全界面组件，且未找到个性化i18n字符串时，尝试寻找默认项
		if (key.matches("^(tree_tilegrid|multiple_trees_datagrid|multiple_datagrids|stats_chartgrid|single_datagrid|tree_datagrid|workflow_datagrids)\\.[^\\.]*\\.[^\\.]*$")) {
			labelstr = props.get(localestr).getProperty(
					RegExpUtil.getFirstMatched("^(tree_tilegrid|multiple_trees_datagrid|multiple_datagrids|stats_chartgrid|single_datagrid|tree_datagrid|workflow_datagrids)\\.", key) + "default" +
					RegExpUtil.getFirstMatched("\\.[^\\.]*$", key)
			);
			if (TextUtils.stringSet(labelstr))
				return new String(labelstr.getBytes("ISO-8859-1"), "utf-8");
		}
		return "";
	}
	
	@Override
	public String get(Object key) {
		try {
			return getPropertyInner(TextUtils.noNull(key + "").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
