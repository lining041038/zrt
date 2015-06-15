package antelope.controllers;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.entities.SysRepDictItem;
import antelope.entities.SysRepTmpl;
import antelope.springmvc.BaseController;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;
import antelope.utils.SpeedIDUtil;

/**
 * 使用报表组件控制器，报表组件后台对外接口
 * @author lining
 */
@Controller
public class UseReportController extends BaseController{
	
	/**
	 * 保存报表相关数据
	 * @param req
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/report/savereportdata")
	public void saveReportData(String tmpl_sid, String data_sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		Set<Entry<String, String[]>> ents = req.getParameterMap().entrySet();
		
		List<String> colnames = new ArrayList<String>();
		List<Object> colvalues = new ArrayList<Object>();
		SysRepTmpl repTmpl = dao.getBy(tmpl_sid, SysRepTmpl.class);
		Map<String,String> typemaps = getReportColTypes(repTmpl.htmltable);
		
		String sql = null;
		
		Integer itg = (Integer) DBUtil.querySingleVal("select count(*) from "+repTmpl.dbtablename+" where sid=?", new Object[]{data_sid}, DataType.Integer);
		
		if (stringSet(data_sid) && itg > 0) { // 若存在数据对应sid,则进行数据更新
			for (Entry<String, String[]> entry : ents) {
				if (entry.getKey().startsWith("col")) {
					String val = decodeAndTrim(entry.getValue()[0]);
					if (!stringSet(val)) {
						colnames.add(entry.getKey()+"=null");
						continue;
					} else
						colnames.add(entry.getKey()+"=?");
					if ("date".equals(typemaps.get(entry.getKey()))) {
						colvalues.add(new Timestamp(getNewSdf().parse(val).getTime()));
					} else {
						colvalues.add(val);
					}
				}
			}
			
			colvalues.add(data_sid);
			sql = "update " + repTmpl.dbtablename + " set "+ join(",", colnames)+ " where sid=?";
		} else { // 否则进行添加操作
			List<String> questionMarks = new ArrayList<String>();
			
			// 表数据主键
			if (!stringSet(data_sid))
				data_sid = SpeedIDUtil.getId();
			colvalues.add(data_sid);
			questionMarks.add("?");
			
			// 对应模板
			colvalues.add(tmpl_sid);
			questionMarks.add("?");
			
			for (Entry<String, String[]> entry : ents) {
				if (entry.getKey().startsWith("col")) {
					String val = decodeAndTrim(entry.getValue()[0]);
					colnames.add(entry.getKey());
					if (!stringSet(val)) {
						questionMarks.add("null");
						continue;
					} else
						questionMarks.add("?");
					if ("date".equals(typemaps.get(entry.getKey()))) {
						colvalues.add(new Timestamp(getNewSdf().parse(val).getTime()));
					} else {
						colvalues.add(val);
					}
				}
			}
			
			sql = "insert into " + repTmpl.dbtablename + "(sid, tmpl_sid, " + join(",", colnames) + ") values("+join(",", questionMarks)+")";
		}
		
		dao.updateBySQL(sql, colvalues);
		getOut(res).print(data_sid);
	}
	
	/**
	 * 获取
	 * @param sid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/report/getreportdata")
	public void getReportData(String sid, String sids, String tmpl_sid, HttpServletResponse res) throws Exception {
		SysRepTmpl repTmpl = dao.getBy(tmpl_sid, SysRepTmpl.class);
		if (stringSet(sid)) {
			JSONObject obj = DBUtil.querySingleJSON("select * from "+repTmpl.dbtablename+" where sid=?", sid);
			getOut(res).print(obj);
		} else {
			JSONArray arr = new JSONArray(sids);
			List<Map<String, Object>> results = DBUtil.query("select * from "+repTmpl.dbtablename+" where sid in ('"+join("','", arr)+"')");
			JSONObject obj = new JSONObject();
			for (Map<String, Object> map : results) {
				Set<Entry<String, Object>> entries = map.entrySet();
				for (Entry<String, Object> entry : entries) {
					String key = entry.getKey();
					Object val = entry.getValue();
					if (!obj.has(key)){
						obj.put(key, val);
					} else {
						Object sumval = obj.get(key);
						if (sumval instanceof String) {
							obj.put(key, sumval + "\r" + val);
						} else if (sumval instanceof Double) {
							obj.put(key, ((Double)sumval).doubleValue() + ((Double)val).doubleValue());
						}
					}
				}
			}
			getOut(res).print(obj);
		}
	}
	
	// 根据htmltable获取列数据类型相关信息
	private Map<String, String> getReportColTypes(String htmltable) throws DocumentException {
		DocumentHelper.parseText(htmltable);
		
		Map<String, String> map = new HashMap<String, String>();
		
		// 对枚举类型进行补充
		Document doc = DocumentHelper.parseText(htmltable);
		
		List<Element> trelems = doc.getRootElement().element("TBODY").elements("TR");
		
		// 收集数据列 并做标记
		
		List<String> cols = new ArrayList<String>();
		int colindex = 0;
		for (int i = 0; i < trelems.size(); i++) {
			Element trelem = trelems.get(i);
			List<Element> tdelems = trelem.elements("TD");
			for (Element tdelem : tdelems) {
				String edittype = tdelem.attributeValue("edittype");
				if (edittype != null)
					map.put(tdelem.attributeValue("dbcol"), edittype);
			}
		}

		return map;
	}
	
	/**
	 * 获取数据表信息
	 * @param sid
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/report/getreporthtmltable")
	public void getReportHtmlTable(String webmode, String sid, HttpServletResponse res) throws Exception {
		SysRepTmpl repTmpl = dao.getBy(sid, SysRepTmpl.class);
		
		// 对枚举类型进行补充
		Document doc = DocumentHelper.parseText(repTmpl.htmltable);
		
		List<Element> trelems = doc.getRootElement().element("TBODY").elements("TR");
		
		// 收集数据列 并做标记
		
		List<String> cols = new ArrayList<String>();
		int colindex = 0;
		for (int i = 0; i < trelems.size(); i++) {
			Element trelem = trelems.get(i);
			List<Element> tdelems = trelem.elements("TD");
			for (Element tdelem : tdelems) {
				if ("dict".equals(tdelem.attributeValue("edittype"))) {
					String dictinfo = URLDecoder.decode(tdelem.attributeValue("dict"), "utf-8");
					JSONObject obj = new JSONObject(dictinfo);
					String dictsid = obj.getString("sid");
					List<SysRepDictItem> dictitems = dao.query("select * from sys_rep_dict_item where dict_sid=?", new Object[]{dictsid}, SysRepDictItem.class);
					obj.put("dictitems", toJSONArrayBy(dictitems));
					tdelem.addAttribute("dict", URLEncoder.encode(obj.toString(), "utf-8"));
				}
			}
		}
		
		repTmpl.htmltable = doc.asXML().replaceFirst("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
		
		if ("true".equals(webmode)) {
			repTmpl.htmltable = tidyToWebModeTable(repTmpl.htmltable);
		}
		
		getOut(res).print(repTmpl.htmltable);
	}
	
	
	/**
	 * 将htmltable 转换为类似网页的表现形式
	 */
	public String tidyToWebModeTable(String htmltable) {
		// System.out.println(htmltable);
		
		return htmltable;
	}
}


