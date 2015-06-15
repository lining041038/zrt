package antelope.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.springmvc.BaseController;
import antelope.utils.I18n;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.MenuUtil;

/**
 * 系统数据获取Controller
 * @author pc
 */
@Controller
public class SystemParamsController extends BaseController {
	
	/**
	 * 获取系统级xml枚举信息
	 * @param xmlname
	 * @param res
	 */
	@RequestMapping("/getsystemenumdatas")
	public void getSystemEnumDatas(String xmlname, HttpServletRequest req, HttpServletResponse res) throws IOException, JSONException, DocumentException {
		getOut(res).print(new JSONArray(getXmlEnumItems(xmlname, getService(req).getLocale()), true));
	}
	
	/**
	 * 前台js端获取国际化字符串
	 * @param key
	 * @throws IOException
	 */
	@RequestMapping("/common/getI18nStr")
	public void getI18nStr(String key, HttpServletRequest req, HttpServletResponse res) throws IOException {
		print(getService(req).getI18n().get(key), res);
	}
	
	/**
	 * 批量获取国际化字符串,逗号分割key
	 * @param keys 逗号分割的key
	 * @throws IOException
	 */
	@RequestMapping("/common/getBatchI18nStr")
	public void getBatchI18nStr(String keys, HttpServletRequest req, HttpServletResponse res) throws IOException {
		JSONObject obj = new JSONObject();
		if (stringSet(keys)) {
			I18n i18n = getService(req).getI18n();
			String[] keyarr = keys.split(",");
			for (String key : keyarr) {
				obj.put(key, i18n.get(key));
			}
		}
		// 附带locale
		obj.put("locale", getService(req).getLocale());
		print(obj, res);
	}
	
	/**
	 * 获取当前系统locale
	 * @throws IOException 
	 */
	@RequestMapping("/common/getLocale")
	public void getLocale(HttpServletRequest req, HttpServletResponse res) throws IOException {
		print(getService(req).getLocale(), res);
	}
	
	/**
	 * 根据模块id获取模块对应的title
	 * @param moduleid
	 * @param res
	 * @throws DocumentException
	 * @throws IOException
	 */
	@RequestMapping("/common/SystemParamsController/getModuleTitleById")
	public void getModuleTitleById(String moduleid, HttpServletResponse res, HttpServletRequest req) throws DocumentException, IOException {
		Document moduleXml = MenuUtil.getInstance().getModuleXml(getService(req).getLocale());
		Element elem = (Element) moduleXml.selectSingleNode(".//*[@id='"+moduleid+"']");
		if (elem != null)
			print(elem.attributeValue("title"), res);
	}
	
	/**
	 * 根据模块id获取模块信息
	 * @param moduleid
	 * @param res
	 */
	@RequestMapping("/common/SystemParamsController/getModuleInfoById")
	public void getModuleInfoById(String moduleid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Document moduleXml = MenuUtil.getInstance().getModuleXml(getService(req).getLocale());
		Element elem = (Element) moduleXml.selectSingleNode(".//*[@id='"+moduleid+"']");
		Set<String> authorities = userroleorg.getAuthorities(getService(req).getUsersid());
		JSONArray menuJson = new JSONArray();
		MenuUtil.appendChildMenu(menuJson, elem, getService(req).isAdmin(), authorities);
		print(menuJson, res);
	}
	
	/**
	 * 获取当前系统启动模式，返回boolean 为true则为开发模式，否则为生产模式
	 */
	@RequestMapping("/common/getSystemMode")
	public void getSystemMode(HttpServletResponse res) throws IOException {
		print(GlobalConsts.isDevelopMode, res);
	}
	
	/**
	 * 防止服务器session过期
	 */
	@RequestMapping("/common/SystemParamsController/noSessionTimeout")
	public void noSessionTimeout(HttpServletResponse res) {
	//	System.out.println("fsddfs");
	}
}



