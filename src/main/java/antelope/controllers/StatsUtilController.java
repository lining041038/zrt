package antelope.controllers;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.StatsChartgrid;
import antelope.interfaces.components.supportclasses.StatsChartgridOptions;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.I18n;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;

/**
 * 统计工具相关
 * @author lining
 * @since 2012-2-15
 */
@Controller
public class StatsUtilController extends BaseController {
	
	/**
	 * 导出统计数据 
	 */
	@RequestMapping("/common/StatsUtilController/exportStatsExcel")
	public void exportStatsExcel(String datagriddata, String currenttype, String component, String imgname, HttpServletRequest req, HttpServletResponse res) throws IOException, JSONException, RowsExceededException, WriteException {
		StatsChartgrid grid = SpringUtils.getBean(StatsChartgrid.class, component);
		StatsChartgridOptions opts = grid.getOptions(req);
		opts.initOptions();
		
		res.setHeader("Content-Disposition","attachment; filename=" + new String(opts.tabs.get(currenttype).exportedExcelName.getBytes("gb2312"), "ISO8859-1" ) + ".xls");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		createStatsExcel2(datagriddata, req, imgname, outputStream);
		ServletOutputStream streamOut = res.getOutputStream();
		try {
			outputStream.writeTo(streamOut);
		} catch( Exception e) { // 用户终止文件的下载操作，异常不进行处理
		}
		streamOut.close();
	}
	
	private void createStatsExcel2(String datagriddata, HttpServletRequest req, String imgname, ByteArrayOutputStream outputStream) throws IOException, JSONException, WriteException, RowsExceededException {
		I18n i18n = getI18n(req);
		
		HSSFWorkbook hbook = new HSSFWorkbook();
		
		HSSFSheet sheet = hbook.createSheet(i18n.get("antelope.statscomponent.statsinfo"));
		
		JSONArray arr = new JSONArray(datagriddata);
		JSONArray header = arr.getJSONArray(0); 
		List<String> theheader = new ArrayList<String>();
		for(int i = 0; i < header.length(); i++) {
			theheader.add(header.getJSONObject(i).getString("field"));
		}
		
//		WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE); 
//		WritableCellFormat headerformat = new WritableCellFormat(wfc);
//		headerformat.setAlignment(Alignment.CENTRE);
//		headerformat.setBackground(Colour.LIGHT_BLUE);
//		headerformat.setBorder(Border.ALL, BorderLineStyle.THIN);
		
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short)0);
		HSSFCellStyle celstyle = hbook.createCellStyle();
		
		HSSFFont font = hbook.createFont();
		font.setFontName("微软雅黑");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setUnderline(HSSFFont.U_NONE);
		font.setColor(HSSFFont.COLOR_NORMAL);
		celstyle.setFont(font);
		celstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		celstyle.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
		celstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		celstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		celstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		celstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cell.setCellStyle(celstyle);
		cell.setCellValue(new HSSFRichTextString(i18n.get("antelope.ordernum")));
		
//		Label headerLabel = new Label(0, 0, "序号");
//		headerLabel.setCellFormat(headerformat);
//		sheet.addCell(headerLabel);
//		sheet.setColumnView(0, 10);
//		sheet.setRowView(0, 414);
		
		for(int i = 1; i <= header.length(); i++) {
			cell = row.createCell(i);
			cell.setCellStyle(celstyle);
			cell.setCellValue(new HSSFRichTextString(header.getJSONObject(i - 1).getString("headerText")));
		}
		
		HSSFCellStyle celstyle2 = hbook.createCellStyle();
		HSSFFont font2 = hbook.createFont();
		font2.setUnderline(HSSFFont.U_NONE);
		font2.setColor(HSSFFont.COLOR_NORMAL);
		celstyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		celstyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		celstyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		celstyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		celstyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		celstyle2.setFont(font2);
		
//		WritableCellFormat format = new WritableCellFormat();
//		format.setAlignment(Alignment.CENTRE);
//		format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GRAY_50);
//		
		for(int i = 1; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			
			HSSFRow newrow = sheet.createRow(i);
			
			HSSFCell celln = newrow.createCell(0);
			celln.setCellStyle(celstyle2);
			celln.setCellValue(new HSSFRichTextString("" + i));
			
			for(int j = 0; j < theheader.size(); j++) {
				String labelText = "";
				if (obj.has(theheader.get(j))) {
					labelText = obj.getString(theheader.get(j));
				}
				HSSFCell datacell = newrow.createCell((j + 1));
				datacell.setCellStyle(celstyle2);
				datacell.setCellValue(new HSSFRichTextString(labelText));
				sheet.setColumnWidth((j + 1), (short)4925);
			}
		}
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		String parentpath = ClasspathResourceUtil.getWebappFolderFile("/").getParent();
		FileInputStream fis = new FileInputStream(parentpath + "/exportimgs/" + imgname);
		byte[] bys = new byte[fis.available()];
		fis.read(bys);
		fis.close();
		
		HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,0 , (short) 4, 1, (short) 6, 1);
		anchor.setAnchorType(2);
		patriarch.createPicture(anchor, hbook.addPicture(bys, HSSFWorkbook.PICTURE_TYPE_PNG)).resize(1);
		hbook.write(outputStream);
	}
}












