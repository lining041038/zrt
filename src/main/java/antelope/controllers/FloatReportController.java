package antelope.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBDataTypeFinder;
import antelope.db.DBUtil;
import antelope.entities.SysFloatRepColumnInfo;
import antelope.entities.SysFloatRepTmpl;
import antelope.springmvc.BaseController;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.RegExpUtil;
import antelope.utils.SpeedIDUtil;

/**
 * 浮动行报表controller
 * @author lining
 */
@Controller
public class FloatReportController extends BaseController {
	
	/**
	 * 获取浮动行模板数据
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/floatreport/getFloatReportTmpls")
	public void getFloatReportTmpls(String groupsid, HttpServletResponse res) throws Exception {
		List<SysFloatRepTmpl> floatreps = new ArrayList<SysFloatRepTmpl>();
		if (stringSet(groupsid)) {
			floatreps = dao.query("select * from SYS_FLOATREP_TMPL where groupsid=?", new Object[]{groupsid}, SysFloatRepTmpl.class);
		} else {
			floatreps = dao.getAll(SysFloatRepTmpl.class);
		}
		getOut(res).print(toJSONArrayBy(floatreps));
	}
	
	
	/**
	 * 获取某个浮动表报表模板详细信息
	 * @param sid 模板sid
	 * @param req
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/floatreport/gettmplinfo")
	public void getTemplInfo(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<JSONObject> colinfos = DBUtil.queryJSON("select t.*,t2.title from SYS_FLOATREP_DBCOLINFO t left join SYS_REP_DICT t2 on t.dictsid=t2.sid where t.tmplsid=?", sid);
		getOut(res).print(toJSONArray(colinfos));
	}
	
	/**
	 * 获取某个浮动表报表模板详细信息,并附带上字典信息
	 * @param sid 模板sid
	 * @param req
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/floatreport/gettmplinfowithdict")
	public void getTemplInfoWithDict(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<JSONObject> colinfos = DBUtil.queryJSON("select t.*,t2.title from SYS_FLOATREP_DBCOLINFO t left join SYS_REP_DICT t2 on t.dictsid=t2.sid where t.tmplsid=?", sid);
		for (JSONObject jsonObject : colinfos) {
			if (jsonObject.has("edittype") && "dict".equals(jsonObject.getString("edittype"))) {
				List<JSONObject> dictitems = DBUtil.queryJSON("select * from SYS_REP_DICT_ITEM where dict_sid=?", jsonObject.get("dictsid"));
				jsonObject.put("dictitems", toJSONArray(dictitems));
			}
		}
		getOut(res).print(toJSONArray(colinfos));
	}
	
	/**
	 * 上传excel表格并解析操作
	 * @param tmplsid 模板sid
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/floatreport/uploadexcel")
	public void uploadExcel(String guid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<FileItem> files = parseRequest(req);
		List<SysFloatRepColumnInfo> infolist = xlsToColInfoItems(files.get(1).get());
		req.getSession().setAttribute(guid, toJSONArrayBy(infolist));
	}
	
	/**
	 * 上传报表模板数据组件。
	 * @param tmplsid
	 * @param groupsid
	 * @param req
	 * @param res
	 * @throws Exception 
	 * @throws SQLException 
	 */
	@RequestMapping("/floatreport/uploaddataexcel")
	public void uploadDataexcel(String tmplsid, String groupsid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		List<FileItem> files = parseRequest(req);
		
		List<JSONObject> colinfos = DBUtil.queryJSON("select t.*,t2.title from SYS_FLOATREP_DBCOLINFO t left join SYS_REP_DICT t2 on t.dictsid=t2.sid where t.tmplsid=?", tmplsid);
		ByteArrayInputStream is = new ByteArrayInputStream(files.get(1).get());
		Workbook wbook = Workbook.getWorkbook(is);
		Sheet sheet = wbook.getSheet(0);
		
		if (sheet.getColumns() < colinfos.size() ) {
			throw new Exception("数据excel上传失败,数据列数量不一致!");
		}
		
		for (int i = 0; i < colinfos.size(); i++) {
			if (!sheet.getCell(i, 0).getContents().equals(colinfos.get(i).getString("coltitle"))) {
				throw new Exception("数据excel上传失败,数据列标题不一致!");
			}
		}
		
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		
		List<String> askflags = new ArrayList<String>();
		askflags.add("?");
		askflags.add("?");
		askflags.add("?");
		for (int i = 0; i < colinfos.size(); i++) {
			askflags.add("?");
		}
		
		dao.updateBySQL("delete from " + tmpl.dbtablename + " where group_sid=?", new Object[]{groupsid});
		
		for (int i = 1; i < sheet.getRows(); i++) {
			boolean hasval = false;
			Cell[] cells = sheet.getRow(i);
			List<Object> params = new ArrayList<Object>();
			params.add(SpeedIDUtil.getId());
			params.add(groupsid);
			params.add(""); // 关联初始化数据
			for(int j = 0; j < cells.length; j++) {
				params.add(cells[j].getContents());
				hasval = hasval || stringSet(cells[j].getContents());
			}
			
			for (int j = cells.length + 3; j < askflags.size(); j++) {
				params.add("");
			}
			
			if (hasval) {
				dao.updateBySQL("insert into " + tmpl.dbtablename + " values("+join(",", askflags)+")", params);
			}
		}
	}
	
	
	/**
	 * 上传报表模板数据组件。
	 * @param tmplsid
	 * @param groupsid
	 * @param req
	 * @param res
	 * @throws Exception 
	 * @throws SQLException 
	 */
	@RequestMapping("/floatreport/saveReportData")
	public void saveReportData(String tmplsid, String groupsid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		List<SysFloatRepColumnInfo> colinfos = dao.query("select * from SYS_FLOATREP_DBCOLINFO where tmplsid=?", tmplsid, SysFloatRepColumnInfo.class);
		
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		
		List<String> askflags = new ArrayList<String>();
		askflags.add("?");
		askflags.add("?");
		askflags.add("?");
		for (int i = 0; i < colinfos.size(); i++) {
			askflags.add("?");
		}
		
		dao.updateBySQL("delete from " + tmpl.dbtablename + " where group_sid = ?", groupsid);
		
		if(null != req.getParameterValues("col0")) {
			for (int i = 0; i < req.getParameterValues("col0").length; i++) {
				List<Object> params = new ArrayList<Object>();
				params.add(SpeedIDUtil.getId());
				params.add(groupsid);
				params.add(req.getParameterValues("initsid")[i]); // 关联初始化数据sid
				for (int j = 0; j < colinfos.size(); j++) {
					params.add(decodeAndTrim(req.getParameterValues("col" + j)[i]));
				}
				dao.updateBySQL("insert into " + tmpl.dbtablename + " values("+join(",", askflags)+")", params);
			}
		}
		
	}

	/**
	 * 获取模板列信息
	 * @param res
	 */
	@RequestMapping("/floatreport/getcolinfoarr")
	public void getTmplHtmlTable(String guid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		getOut(res).print(req.getSession().getAttribute(guid));
	}
	
	/**
	 * 保存新增的报表模版
	 * @param req
	 * @param res
	 * @throws DocumentException 
	 * @throws JSONException 
	 */
	@RequestMapping("/floatreport/addnewtmpl")
	public void addNewTmpl(String htmltable, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException, DocumentException, JSONException {
		htmltable = decodeAndTrim(htmltable);
		
		SysFloatRepTmpl tmpl = wrapToEntity(req, SysFloatRepTmpl.class);
		tmpl.sid = stringSet(tmpl.sid) ? tmpl.sid : SpeedIDUtil.getId();
		tmpl.isrelease = 0;
		
		// 特殊处理htmltable
		Matcher matcher = Pattern.compile("\\w*=[^\"'>\\s]+").matcher(htmltable);
		String str = RegExpUtil.getNextMatched(matcher);
		List<String> strs = new ArrayList<String>();
		while(str != null) {
			strs.add(str);
			str = RegExpUtil.getNextMatched(matcher);
		}
		matcher = Pattern.compile("jQuery[^\\s>]*+").matcher(htmltable);
		str = RegExpUtil.getNextMatched(matcher);
		while(str != null) {
			htmltable = htmltable.replaceAll(str, "");
			str = RegExpUtil.getNextMatched(matcher);
		}
		
		for (int i = 0; i < strs.size(); i++) {
			htmltable = htmltable.replaceAll(strs.get(i), strs.get(i).replaceFirst("=", "=\"")+"\"");
		}
		
		Document dom = DocumentHelper.parseText(htmltable);
		List<Element> nodes = dom.selectNodes("//*[@id='tbcells']/TD");
		List<Element> headernodes = dom.selectNodes("//*[@id='reportheadertr']/TH");
		List<SysFloatRepColumnInfo> colInfoList = new ArrayList<SysFloatRepColumnInfo>();
		for (int i = 0; i < nodes.size(); i++) {
			Element node = nodes.get(i);
			
			SysFloatRepColumnInfo colinfo = new SysFloatRepColumnInfo();
			colinfo.colname = "col"+i;
			colinfo.coltitle = headernodes.get(i).getTextTrim(); 
			colinfo.colwidth = Double.parseDouble(node.attributeValue("width"));
			colinfo.edittype = nodes.get(i).attributeValue("edittype");
			if ("dict".equals(colinfo.edittype)) {
				String dictinfos = decodeAndTrim(nodes.get(i).attributeValue("dict"));
				JSONObject obj = new JSONObject(dictinfos);
				colinfo.dictsid = obj.getString("sid");
			}
			colinfo.sumtype = decodeAndTrim(node.attributeValue("sumtype"));
			colinfo.sid = SpeedIDUtil.getId();
			colinfo.tmplsid = tmpl.sid;
			colInfoList.add(colinfo);
		}
		
		dao.updateBySQL("delete from SYS_FLOATREP_DBCOLINFO where tmplsid=?", new Object[]{tmpl.sid});
		dao.insertOrUpdate(tmpl);
		dao.batchInsertOrUpdate(colInfoList);
	}
	
	/**
	 * 发布报表，以使得报表处于可以使用的状态
	 * @param sid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/floatreport/release")
	public void releaseTmpl(String sid, HttpServletResponse res) throws Exception {
		SysFloatRepTmpl repTmpl = dao.getBy(sid, SysFloatRepTmpl.class);
		List<SysFloatRepColumnInfo> colinfos = dao.query("select * from SYS_FLOATREP_DBCOLINFO where tmplsid=?", new Object[]{sid}, SysFloatRepColumnInfo.class);
		
		// 收集数据列 并做标记
		List<String> cols = new ArrayList<String>();
		for (int i = 0; i < colinfos.size(); i++) {
			SysFloatRepColumnInfo elem = colinfos.get(i);
			if (stringSet(elem.edittype)) {
				cols.add("col" + (i) + " " + DBDataTypeFinder.find(elem.edittype));
			} else {// 所有列都进行设置
				cols.add("col" + (i) + " " + DBDataTypeFinder.find("text"));
			}
		}
		repTmpl.isrelease = 1;
		repTmpl.dbtablename = "sys_rep_tb" + sid;
		dao.updateBySQL("create table sys_rep_tb" + sid + " (sid "+DBDataTypeFinder.find("text", 36)+", group_sid "+DBDataTypeFinder.find("text", 36)+", initsid " + DBDataTypeFinder.find("text", 36)+", "+ join(",", cols)+", primary key (sid) )");
		dao.insertOrUpdate(repTmpl);
		
	}
	
	/**
	 * 只获取获取所有已发布的报表模板
	 * @param req
	 * @param res
	 */
	@RequestMapping("/floatreport/getreleasedtmpls")
	public void getReleasedTemplates(String groupsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		getOut(res).print(toJSONArrayBy(dao.query(
				"select * from SYS_FLOATREP_TMPL where isrelease=1" + (stringSet(groupsid)?" and groupsid = '"+groupsid+"'":"")
				, SysFloatRepTmpl.class)));
	}
	
	/**
	 * 获取报表数据
	 * @param groupsid 单组
	 * @param groupsids 多组，以JSONArray的方式表达
	 * @throws Exception 
	 */
	@RequestMapping("/floatreport/getreportdata")
	public void getReportData(String datasids, String groupsid, String groupsids, String tmplsid, HttpServletResponse res) throws Exception {
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		if (stringSet(datasids)) {
			JSONArray arr = new JSONArray(datasids);
			List<JSONObject> results = DBUtil.queryJSON("select * from "+tmpl.dbtablename+" where sid in ('"+join("','", arr)+"')");
			getOut(res).print(toJSONArray(results));
		} else if (stringSet(groupsid)) {
			List<JSONObject> results = DBUtil.queryJSON("select * from "+tmpl.dbtablename+" where group_sid=?", groupsid);
			getOut(res).print(toJSONArray(results));
		} else {
			JSONArray arr = new JSONArray(groupsids);
			List<JSONObject> results = DBUtil.queryJSON("select * from "+tmpl.dbtablename+" where group_sid in ('"+join("','", arr)+"') order by group_sid");
			getOut(res).print(toJSONArray(results));
		}
	}
	
	/**
	 * 根据excel数据生成对应的列信息数据
	 */
	private List<SysFloatRepColumnInfo> xlsToColInfoItems(byte[] xlsdata) throws BiffException, IOException, DocumentException {
		ByteArrayInputStream is = new ByteArrayInputStream(xlsdata);
		Workbook wbook = Workbook.getWorkbook(is);
		Sheet sheet = wbook.getSheet(0);
		List<SysFloatRepColumnInfo> colinfolist = new ArrayList<SysFloatRepColumnInfo>();
		for (int i = 0; i < sheet.getColumns(); i++) {
			SysFloatRepColumnInfo colinfo = new SysFloatRepColumnInfo();
			colinfo.colname = "col" + i;
			colinfo.coltitle = sheet.getCell(i, 0).getContents();
			int colsize = sheet.getColumnView(i).getSize();
			if (colsize <= 8) {
				colsize = 2300;
			}
			colinfo.colwidth = colsize / 32.4;
			colinfolist.add(colinfo);
		}
		return colinfolist;
	}
	
}







