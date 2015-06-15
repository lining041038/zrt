package antelope.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import flex.messaging.util.URLDecoder;

import antelope.db.DBUtil;
import antelope.springmvc.BaseController;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.SystemOpts;
import antelope.utils.TextUtils;
import antelope.utils.XmlEnumItem;

/**
 * 用户主题信息处理控制器
 * @author lining
 * @since 2012-6-3
 */
@Controller
public class UserThemeController extends BaseController {
	
	/**
	 * 查询当前系统中存在的所有可选主题
	 */
	@RequestMapping("/common/UserThemeController/getThemesToSelect")
	public void getThemesToSelect(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		String usersid = getService(req).getUsersid();
		String usertheme = DBUtil.querySingleString("select theme from SYS_USER where sid=?", new Object[]{usersid});
		
		if (!stringSet(usertheme)) {
			usertheme = "defaults";
		}
		
		String file = this.getClass().getResource("/").getFile();
		file = file.replaceFirst("^/*", "");
		File fileObj = new File(file);
		String themefolders = fileObj.getParentFile().getParentFile().getAbsolutePath() + "/themes";
		File themfolder = new File(themefolders);
		String[] themefolderNames = themfolder.list();
		JSONArray themesCanSelect = new JSONArray();
		
		String themesopend = SystemOpts.getProperty("openedthemes");
		Set<String> themesopendsets = new HashSet<String>();
		if (!stringSet(themesopend)) 
			themesopend = "all";
		
		if (!"all".equals(themesopend)) {
			themesopendsets = TextUtils.putStrsIntoSet(themesopend.split(","));
		}
		
		for (String themefolderName : themefolderNames) {
			if ("base".equals(themefolderName)) // 过滤基础css样式
				continue;
			
			File themecss = new File(themefolders + "/" + themefolderName + "/" + themefolderName + ".css");
			if (themecss.exists()) {
				FileReader reader = new FileReader(themecss);
				BufferedReader bfReader = new BufferedReader(reader);
				String themelabel = new String(bfReader.readLine().getBytes(System.getProperty("file.encoding")), "utf-8");
				bfReader.close();
				reader.close();
				themelabel = themelabel.replaceAll("^/\\*|\\*/$", "");
				themelabel = getThemeLabel(themelabel.split(",|，"), req);
				
				System.out.println(themelabel);
				
				if (themelabel.indexOf("%") != -1) {
					themelabel = URLDecoder.decode(themelabel);
				}
				
				JSONObject themeobj = new JSONObject();
				String theme = themecss.getName().replaceFirst("\\.css$", "");
				themeobj.put("theme", theme);
				themeobj.put("themelabel", themelabel);
				if (usertheme.equals(theme)) {
					themeobj.put("selected", true);
				}
				
				if ("all".equals(themesopend) || themesopendsets.contains(theme))
					themesCanSelect.put(themeobj);
			}
		}
		print(themesCanSelect, res);
	}
	
	/**
	 * 获取主题标签，传入数组为以下类型   zh_CN:中文,en_US:英文
	 * @param alllables
	 */
	private String getThemeLabel(String[] alllables, HttpServletRequest req) {
		String locale = getService(req).getLocale();
		for (String label : alllables) {
			label = label.trim();
			String[] namev = label.split(":");
			if (locale.equals(namev[0]))
				return namev[1];
		}
		return "";
	}
	
	/**
	 * 保存用户选中的默认主题关联到用户本身
	 */
	@RequestMapping("/common/UserThemeController/saveSelectedTheme")
	public void saveSelectedTheme(String theme, HttpServletRequest req, HttpServletResponse res) {
		String sid = getService(req).getUsersid();
		if (sid != null) {
			dao.updateBySQL("update SYS_USER set theme=? where sid=?", new Object[]{theme, sid});
		}
	}
	
	/**
	 * 获取所有可选的本地化locale语言
	 */
	@RequestMapping("/common/UserThemeController/getAllLocales")
	public void getAllLocales(HttpServletRequest req, HttpServletResponse res) throws IOException, DocumentException, JSONException {
		XmlEnumItem[] items = getXmlEnumItems("sys_language", getService(req).getLocale());
		
		String locale = getService(req).getLocale();
		JSONArray arr = new JSONArray(items, true);
		for (int i = 0; i < arr.length(); ++i) {
			if (locale.equals(arr.getJSONObject(i).get("value"))) {
				arr.getJSONObject(i).put("selected", true);
			}
		}
		
		print(arr, res);
	}
	
	/**
	 * 获取所有可选的本地化locale语言
	 */
	@RequestMapping("/common/UserThemeController/saveSelectedLocale")
	public void saveSelectedLocale(String locale, HttpServletRequest req, HttpServletResponse res) throws IOException, DocumentException, JSONException {
		getService(req).setLocale(locale);
	}
}







