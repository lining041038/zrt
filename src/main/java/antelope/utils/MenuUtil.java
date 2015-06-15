package antelope.utils;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import antelope.services.SessionService;
import antelope.services.UserRoleOrgService;

/**
 * 菜单工具类，完成菜单组装，菜单获取等操作过程
 * @author lining
 * @since 2012-7-18
 */
public class MenuUtil {
	private static MenuUtil menuUtil;
	
	private MenuUtil() {
	}
	
	public static MenuUtil getInstance() {
		if (menuUtil == null)
			menuUtil = new MenuUtil();
		return menuUtil;
	}
	
	/**
	 * 根据module-cfg.xml获取导航信息
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray getMenuJSON(SessionService session) throws DocumentException, IOException, JSONException {
		Document moduleXml = MenuUtil.getInstance().getModuleXml(session.getLocale());
		JSONArray menuJson = new JSONArray();
		appendChildMenu(menuJson, moduleXml.getRootElement(), true, null);
		return menuJson;
	}
	
	
	/**
	 * 根据module-cfg.xml获取导航信息, 并根据当前登录用户信息对菜单进行过滤
	 * @return
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public static JSONArray getMenuJSONAndFilter(SessionService session, UserRoleOrgService service) throws SQLException, Exception {
		boolean all = false;
		Set<String> authoredfuncs = new HashSet<String>();
		if (session != null && session.isAdmin()) {
			all = true;
		} else {
			authoredfuncs = service.getAuthorities(session.getUsersid());
		}
		Document moduleXml = MenuUtil.getInstance().getModuleXml(session.getLocale());
		JSONArray menuJson = new JSONArray();
		appendChildMenu(menuJson, moduleXml.getRootElement(), all, authoredfuncs);
		return menuJson;
	}
	
	/**
	 * 递归挂接子菜单
	 */
	public static void appendChildMenu(JSONArray menuJson, Element parentmenu, boolean isAll, Set<String> authoredfuncs) throws JSONException {
		List<Element> elems = parentmenu.elements();
		for (int i = 0; i < elems.size(); i++) {
			if (checkHasTheRight(isAll, authoredfuncs, elems, i))
				continue;
			
			JSONObject obj = new JSONObject();
			putAttrIntoJson(obj, elems.get(i));
			JSONArray submenujson = new JSONArray();
			obj.put("submenu", submenujson);
			menuJson.put(obj);
			appendChildMenu(submenujson, elems.get(i), isAll, authoredfuncs);
		}
	}

	private static void putAttrIntoJson(JSONObject obj, Element menu) throws JSONException {
		List<Attribute> attrs = menu.attributes();
		for (Attribute attr : attrs) {
			String val = attr.getValue();
			if (attr.getName().equals("path")) { // path
				if (!val.startsWith("http:")) {
					if (val.endsWith(".jsp") || val.endsWith(".vot") || val.endsWith(".action")) {
						val += "?";
					} else {
						val += "&";
					}
					val += "moduleid=" + menu.attributeValue("id");
				}
			}
			obj.put(attr.getName(), val);
		}
	}

	private static boolean checkHasTheRight(boolean all, Set<String> authoredfuncs, List<Element> menus, int i) {
		if (menus.get(i).selectNodes(".//*[@ispublic='true']").size() > 0 || "true".equals(menus.get(i).attributeValue("ispublic"))) {
			return false;
		}
		return !all && !authoredfuncs.contains(menus.get(i).attributeValue("id"));
	}
	
	public static Document getModuleXml(String locale) throws DocumentException, IOException {
		return ClasspathResourceUtil.getXMLDocumentByPath("/module-cfg_" + locale + ".xml");
	}
}
