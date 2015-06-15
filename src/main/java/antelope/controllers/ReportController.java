package antelope.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import org.apache.commons.fileupload.FileItem;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.beans.SysRepBasic;
import antelope.beans.SysRepBasicWithHtmlTable;
import antelope.db.DBDataTypeFinder;
import antelope.entities.SysRepDict;
import antelope.entities.SysRepDictItem;
import antelope.entities.SysRepTmpl;
import antelope.interfaces.ReportExcelManager;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;
import antelope.utils.RegExpUtil;
import antelope.utils.SpeedIDUtil;

/**
 * <p>Title: 通用自定义报表控制器 </p>
 * <p>Copyright: Smartdot Corporation Copyright (c) 2011</p>
 * <p>Company: BEIJING Smartdot SOFTWARE CO.,LTD</p>
 * @author lining
 * @version 1.0
 */
@Controller
public class ReportController  extends BaseController {
	
	
	private static Pattern ptParam = Pattern.compile("\\$\\{[\\s]*[^}]*[\\s]*\\}");
	private static Pattern ptParam2 = Pattern.compile("(?<=\\$\\{)[\\s]*[^}]*[\\s]*(?=\\})");
	/**
	 * 获取所有报表模板
	 * @param req
	 * @param res
	 */
	@RequestMapping("/report/gettmpls")
	public void getTemplates(String groupsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		getOut(res).print(toJSONArrayBy(dao.query(
				"select * from sys_rep_tmpl" + (stringSet(groupsid)?" where groupsid = '"+groupsid+"'":"")
				, SysRepBasic.class)));
	}
	
	/**
	 * 只获取获取所有已发布的报表模板
	 * @param req
	 * @param res
	 */
	@RequestMapping("/report/getreleasedtmpls")
	public void getReleasedTemplates(String groupsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		getOut(res).print(toJSONArrayBy(dao.query(
				"select * from sys_rep_tmpl where isrelease=1" + (stringSet(groupsid)?" and groupsid = '"+groupsid+"'":"")
				, SysRepBasic.class)));
	}
	
	/**
	 * 获取某个报表模板详细信息
	 * @param sid 模板sid
	 * @param req
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/report/gettmplinfo")
	public void getTemplInfo(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		SysRepBasicWithHtmlTable repinfo = dao.getBy(sid, SysRepBasicWithHtmlTable.class);
		getOut(res).print(new JSONObject(repinfo, true));
	}
	
	/**
	 * 上传excel表格并解析操作
	 * @param tmplsid 模板sid
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/report/uploadexcel")
	public void uploadExcel(String guid, String managername, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<FileItem> files = parseRequest(req);
		if (stringSet(managername)) {
			ReportExcelManager manager = spring.getBean(ReportExcelManager.class, managername);
			if (!manager.validateExcel(Workbook.getWorkbook(new ByteArrayInputStream(files.get(1).get())))) {
				req.getSession().setAttribute(guid, null);
				return;
			}
		}
		String htmltable = xlsToHtmlTable(files.get(1).get());
		req.getSession().setAttribute(guid, htmltable);
	}
	
	/**
	 * 删除报表模版
	 * @param tmplsid
	 * @param res
	 */
	@RequestMapping("/report/deleteTmpl")
	public void deleteTmpl(String tmplsid, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.deleteBy(tmplsid, SysRepTmpl.class);
	}
	
	/**
	 * 保存新增的报表模版
	 * @param req
	 * @param res
	 * @throws DocumentException 
	 */
	@RequestMapping("/report/addnewtmpl")
	public void addNewTmpl(HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException, DocumentException {
		SysRepTmpl tmpl = wrapToEntity(req, SysRepTmpl.class);
		tmpl.sid = stringSet(tmpl.sid) ? tmpl.sid : SpeedIDUtil.getId();
		tmpl.isrelease = 0;
		
		// 特殊处理htmltable
		Matcher matcher = Pattern.compile("\\w*=[^\"'>\\s]+").matcher(tmpl.htmltable);
		String str = RegExpUtil.getNextMatched(matcher);
		List<String> strs = new ArrayList<String>();
		while(str != null) {
			strs.add(str);
			str = RegExpUtil.getNextMatched(matcher);
		}
		matcher = Pattern.compile("jQuery[^\\s>]*+").matcher(tmpl.htmltable);
		str = RegExpUtil.getNextMatched(matcher);
		while(str != null) {
			tmpl.htmltable = tmpl.htmltable.replaceAll(str, "");
			str = RegExpUtil.getNextMatched(matcher);
		}
		
		for (int i = 0; i < strs.size(); i++) {
			tmpl.htmltable = tmpl.htmltable.replaceAll(strs.get(i), strs.get(i).replaceFirst("=", "=\"")+"\"");
		}
	
		dao.insertOrUpdate(tmpl);
	}
	
	/**
	 * 发布报表，以使得报表处于可以使用的状态
	 * @param sid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/report/release")
	public void releaseTmpl(String sid, HttpServletResponse res) throws Exception {
		SysRepTmpl repTmpl = dao.getBy(sid, SysRepTmpl.class);
		
		Document doc = DocumentHelper.parseText(repTmpl.htmltable);
		List<Element> trelems = doc.getRootElement().element("TBODY").elements("TR");
		
		// 收集数据列 并做标记
		
		List<String> cols = new ArrayList<String>();
		int colindex = 0;
		for (int i = 0; i < trelems.size(); i++) {
			Element trelem = trelems.get(i);
			List<Element> tdelems = trelem.elements("TD");
			for (Element tdelem : tdelems) {
				if (stringSet(tdelem.attributeValue("edittype"))) {
					cols.add("col" + (colindex) + " " + DBDataTypeFinder.find(tdelem.attributeValue("edittype")));
					tdelem.addAttribute("dbcol", "col" + (colindex ++));
				}
			}
		}
		
		repTmpl.htmltable = doc.asXML().replaceFirst("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
		repTmpl.isrelease = 1;
		repTmpl.dbtablename = "sys_rep_tb" + sid;
		dao.updateBySQL("create table sys_rep_tb" + sid + " (sid "+DBDataTypeFinder.find("text", 36)+", tmpl_sid "+DBDataTypeFinder.find("text", 36)+", "+join(",", cols)+", primary key (sid) )");
		dao.insertOrUpdate(repTmpl);
	}
	
	/**
	 * 根据excel数据生成对应的html表格
	 * @param xlsdata
	 * @return 生成的html表格字符串
	 */
	private String xlsToHtmlTable(byte[] xlsdata) throws BiffException, IOException, DocumentException {
		ByteArrayInputStream is = new ByteArrayInputStream(xlsdata);
		Workbook wbook = Workbook.getWorkbook(is);
		Sheet sheet = wbook.getSheet(0);
		
		String htmltable = "<table class=\"report-table\" cellspacing='0' cellpadding='0'><tr class=\"tbheader\">";
		for (int i = 0; i < sheet.getColumns(); i++) {
			int colsize = sheet.getColumnView(i).getSize();
			if (colsize <= 8) {
				colsize = 2300;
			}
			htmltable += "<th style=\"width:" + colsize / 32.4 + "px;\"></th>";
		}
		
		htmltable += "</tr>";
		
		for (int i = 0; i < sheet.getRows(); i++) {
			Cell[] cells = sheet.getRow(i);
			htmltable += "<tr style=\"height:"+sheet.getRowView(i).getSize() / 15 + "px;\">";	
			for (int j = 0; j < cells.length; j++) {
				// 单元格样式博取
				CellFormat cellFormat = cells[j].getCellFormat();
				Colour color = cellFormat.getBackgroundColour();
				String r = Integer.toString(color.getDefaultRGB().getRed(), 16);
				String g = Integer.toString(color.getDefaultRGB().getGreen(), 16); 
				String b = Integer.toString(color.getDefaultRGB().getBlue(), 16);
				if (r.length() == 1) r = "0"+r;
				if (g.length() == 1) g = "0"+g;
				if (b.length() == 1) b = "0"+b;
				String colorstr = r + g + b;
				Alignment align = cellFormat.getAlignment();
				int value = align.getValue();
				int boldWeight = cellFormat.getFont().getBoldWeight();
				String weight = "normal";
				if (boldWeight == 700) {
					weight = "bold";
				} else {
					weight = "normal";
				}
				if (stringSet( cells[j].getContents() )) {
					// 解析编辑方式
					String fixedatt = "";
					String content = cells[j].getContents();
					if (Pattern.matches(ptParam.pattern(), cells[j].getContents() )) {
						String edittypestr = RegExpUtil.getFirstMatched(ptParam2, cells[j].getContents());
						fixedatt = " fixed=\"true\" edittype=\""+edittypestr+"\" ";
						if ("text".equals(edittypestr)) {
							content = "<DIV>可写文本</DIV>";
						} else if ("num".equals(edittypestr)) {
							content = "<DIV>可写数字</DIV>";
						} else if ("date".equals(edittypestr)) {
							content = "<DIV>可选日期</DIV>";
						} else if ("file".equals(edittypestr)) {
							content = "<DIV>可传附件</DIV>";
						}
					}
					htmltable += "<td " + fixedatt + " style=\"background-color:#"+colorstr+";font-weight:" + weight + ";text-align:"+new String[]{"left","left","center","right","",""}[value] + ";\">" + content + "</td>";
				} else {
					htmltable += "<td style=\"font-weight:" + weight + ";text-align:"+new String[]{"left","left","center","right","",""}[value] + ";\">" + cells[j].getContents() + "</td>";
				}
			}
			htmltable += "</tr>";
		}
		
		htmltable += "</table>";
		Document htmltabledom = DocumentHelper.parseText(htmltable);
		
		Range[] mergedCells = sheet.getMergedCells();
		List<Element> elems = htmltabledom.getRootElement().elements("tr");
		Element thhead = elems.remove(0);
		
		for (Range range : mergedCells) {
			Cell cellleft = range.getTopLeft();
			Cell cellright = range.getBottomRight();
			Element elem = elems.get(cellleft.getRow());
			List<Element> tdelems = elem.elements();
			Element tdelem = tdelems.get(cellleft.getColumn());
			int colspan = cellright.getColumn() - cellleft.getColumn() + 1;
			int rowspan = cellright.getRow() - cellleft.getRow() + 1;
			tdelem.addAttribute("colspan", colspan + "");
			tdelem.addAttribute("rowspan", rowspan + "");
		}
		
		for (int i = 0; i < elems.size(); i++) {
			Element rowelem = elems.get(i);
			List<Element> tdelems = rowelem.elements();
			for (int j = 0; j < tdelems.size(); j++) {
				Element tdelem = tdelems.get(j);
				String colspanattr = tdelem.attributeValue("colspan");
				String rolspanattr = tdelem.attributeValue("rowspan");
				int colspan = 1;
				int rowspan = 1;
				if (colspanattr != null) {
					colspan = Integer.parseInt(colspanattr);
				}
				if (rolspanattr != null) {
					rowspan = Integer.parseInt(rolspanattr);
				}
				if (colspan > 1) {
					for (int k = 1; k < colspan; k++) {
						tdelems.get(j + k).addAttribute("todel", "true");
						// rowelem.remove(tdelems.get(j + k));
					}
					j += (colspan - 1);
				}
				if (rowspan > 1) {
					for (int k = i + 1; k < i + rowspan; k++) {
						Element row = elems.get(k);
						List<Element> tds = row.elements();
						for (int s = 0; s < colspan; s++) {
							if (tds.size() > j + s)
								tds.get(j + s).addAttribute("todel", "true");
						}	
					}
				}
			}
		}
		
		for (int i = 0; i < elems.size(); i++) {
			Element rowelem = elems.get(i);
			List<Element> tdelems = rowelem.elements();
			for (int j = 0; j < tdelems.size(); j++) {
				Element tdelem = tdelems.get(j);
				if (tdelem.attributeValue("todel") != null) {
					rowelem.remove(tdelem);
				}
			}
		}
		elems.add(0, thhead);
		return htmltabledom.asXML().replaceFirst("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
	}
	
	/**
	 * 获取所有字典列表
	 * @param res
	 */
	@RequestMapping("/report/getdicts")
	public void getDicts(HttpServletResponse res) throws IOException, Exception {
		getOut(res).print(toJSONArrayBy(dao.getAll(SysRepDict.class)));
	}
	
	/**
	 * 获取字典item详细信息
	 * @param sid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/report/getdictiteminfo")
	public void getDictItemInfo(String sid, HttpServletResponse res) throws Exception {
		SysRepDict dict = dao.getBy(sid, SysRepDict.class);
		List<SysRepDictItem> dictitems = dao.query("select * from sys_rep_dict_item where dict_sid=?", new Object[]{sid}, SysRepDictItem.class);
		JSONObject obj = new JSONObject();
		obj.put("dict", new JSONObject(dict, true));
		obj.put("dictitems", toJSONArrayBy(dictitems));
		getOut(res).print(obj);
	}
	
	/**
	 * 添加新字典列表
	 * @throws Exception 
	 */
	@RequestMapping("/report/addOrUpdatenewdict")
	public void addOrUpdateNewDict(String dict_title, String dict_sid, String dicttitles, String selectmode, HttpServletResponse res) throws Exception {
		dicttitles = decodeAndTrim(dicttitles);
		dict_title = decodeAndTrim(dict_title);
		SysRepDict srd = null;
		if (stringSet(dict_sid)) {
			srd = dao.getBy(dict_sid, SysRepDict.class);
		} else {
			dict_sid = SpeedIDUtil.getId();
			srd = new SysRepDict();
		}
		
		srd.sid = dict_sid;
		srd.title = dict_title;
		srd.selectmode = Integer.parseInt(selectmode);
		
		dao.insertOrUpdate(srd);
		
		// 更新字典项目信息
		dao.updateBySQL("delete from sys_rep_dict_item where dict_sid=?", new Object[]{dict_sid});
		
		JSONArray arr = new JSONArray(dicttitles);
		for (int i = 0; i < arr.length(); i++) {
			SysRepDictItem srdi = new SysRepDictItem();
			srdi.sid = SpeedIDUtil.getId();
			srdi.dict_sid = dict_sid;
			srdi.title = arr.getString(i);
			dao.insertOrUpdate(srdi);
		}
	}
	
	/**
	 * 获取模版html表格
	 * @param res
	 */
	@RequestMapping("/report/gethtmltable")
	public void getTmplHtmlTable(String guid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		getOut(res).print(req.getSession().getAttribute(guid));
	}
	
	/**
	 * 导出excel模板
	 * @throws IOException 
	 * @throws WriteException 
	 */
	@RequestMapping("/report/exportExcelTmpl")
	public void exportExcelTmpl(String managername, HttpServletRequest req, HttpServletResponse res) throws WriteException, IOException {
		ReportExcelManager manager = SpringUtils.getBean(ReportExcelManager.class, managername);
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		res.setHeader("Content-Disposition","attachment; filename=reporttemplate.xls");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		manager.exportExcelTemplate(outputStream);
		ServletOutputStream streamOut = res.getOutputStream();
		try {
			outputStream.writeTo(streamOut);
		} catch( Exception e) { // 用户终止文件的下载操作，异常不进行处理
		}
		streamOut.close();
	}
}


