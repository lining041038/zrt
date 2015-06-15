package antelope.demos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import antelope.db.DBUtil;
import antelope.interfaces.components.ListSelect;
import antelope.interfaces.components.supportclasses.ListSelectOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.Pair;
import antelope.utils.TextUtils;

@Component("listselectdemo")
public class ListSelectDemo extends ListSelect {

	@Override
	public ListSelectOptions getOptions(HttpServletRequest req) {
		ListSelectOptions options = new ListSelectOptions();
		options.title = "演示标题";
		options.columns = new Pair[]{new Pair("name","姓名"),new Pair("username","用户名")};
		return options;
	}

	@Override
	public PageItem getJSONPage(String queryval, HttpServletRequest req) throws Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?", "username like ?", "sid = ?"}, 
				new Object[]{"%" + queryval+ "%", "%" + queryval+ "%", d(req.getParameter("sid"))}, true);
		return DBUtil.queryJSON("select * from SYS_USER where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
	}

	@Override
	public List<JSONObject> getSelectedItems(String selectedItemSids, String parentsid, HttpServletRequest req) throws Exception {
		return DBUtil.queryJSON("select * from SYS_USER where sid in ('" + join("','", selectedItemSids.split(",")) + "')");
	}
	
	
	public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream("D:/workspace64/hiseye/doc/电子病例/电子病例元数据成品.xlsx");
		FileOutputStream fos = new FileOutputStream("D:/workspace64/hiseye/doc/电子病例/电子病例元数据成品2.xlsx"); 
		
		XSSFWorkbook importedExcel = new XSSFWorkbook(fis); 
		
		for (int k = 0; k < 5; ++k) {
			XSSFSheet sheet = importedExcel.getSheetAt(k);
			int rownum = sheet.getLastRowNum();
			int colnum = 0;
			boolean findprop = false;
			DecimalFormat df = new DecimalFormat("0.########");
			
			Set<String> colnameset = new HashSet<String>();
			
			String sheetName = sheet.getSheetName();
			String tablename = TextUtils.getPingyinByChinese(sheetName);
			
			
			String sql = "drop table HIS_bl_" + tablename + "; \n"
					+ "create table  HIS_bl_" + tablename + "( --" + sheetName + "\n"
					+ " sid varchar(36), \n"
					+ " czkh varchar(30), \n";
			
			
			String inputbiaodan = "";
			
			String javacode = "";
			
			for(int i = 0; i <= rownum; i++) {
				XSSFRow row = sheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				String stringCellValue = cell.getStringCellValue();
				
				String pingyinByChinese = TextUtils.getPingyinByChinese(stringCellValue);
				
				int j = 0; 
				String finalstr = pingyinByChinese;
				while(colnameset.contains(finalstr)) {
					j++;
					finalstr = pingyinByChinese + j;
				}
				
				colnameset.add(finalstr);
				
				XSSFCell cell2 = row.getCell(2);
				if (cell2 == null)
					cell2 = row.createCell(2);
				
				cell2.setCellValue(new XSSFRichTextString(finalstr));
				
				
				// javacode
				javacode += " public String " + finalstr + "; \n";
				
				
				// sql
				sql += finalstr + " varchar(max), --" + stringCellValue + "\n";
				
				
				// 表单html
				inputbiaodan += "<tr> \n" + 
									"	<td style=\"width:128px; text-align: right;vertical-align: middle;\"> \n" +
									"	" + stringCellValue + "： \n" +
									"</td> \n" +
									"<td style=\"width:522px; vertical-align: middle;\"> \n" +
								"		<input type=\"text\" name=\"doctor\"/> \n" +
								"	</td> \n" +
								"</tr> \n"; 
			}
			
			sql+= ""
					+ "primary key(sid) \n"
					+ ");";
			
			
			//System.out.println(inputbiaodan);
			//System.out.println(sql);
			
			System.out.println(sheetName);
			System.out.println(javacode);
			System.out.println("----------------------------------------");
		}
		importedExcel.write(fos);
		
		fis.close();
		fos.close();
	}

}
