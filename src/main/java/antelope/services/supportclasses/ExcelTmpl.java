package antelope.services.supportclasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import antelope.springmvc.BaseComponent;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;

import com.opensymphony.xwork2.util.TextUtils;


/**
 * excel导入导出模板
 * @author lining
 * @since 2012-2-24
 */
public class ExcelTmpl {
	
	
	private HSSFWorkbook hbook = null;
	
	private OutputStream out;
	
	private static Pattern ptSqlatoms = Pattern.compile("(?<=\\$\\{)[^\\}]*(?=\\})");
	
	public ExcelTmpl(String tmplname, String filename, ServletOutputStream os) throws IOException, BiffException {
		genCopyOfTemp2(tmplname,filename,os);
	}
	
	public ExcelTmpl(String tmplname) throws IOException, BiffException {
		genCopyOfTemp2(tmplname);
	}
	
	public ExcelTmpl(String tmplname, String filename, FileOutputStream out) throws IOException, BiffException {
		genCopyOfTemp2(tmplname,filename,out);
	}
	
	public <A> List<A> getItemsFromExcel(InputStream in, Class<A> clazz) throws IOException, SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {
		List<A> list = new ArrayList<A>();
		HSSFWorkbook importedExcel = new HSSFWorkbook(in); 
		
		if (hbook != null) {
			HSSFSheet sheet = hbook.getSheetAt(0);
			int rownum = sheet.getLastRowNum();
			int colnum = 0;
			boolean findprop = false;
			DecimalFormat df = new DecimalFormat("0.########");
			for(int i = 0; i <= rownum; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row == null)
					continue;
				colnum = row.getLastCellNum();
				for (int j = 0; j < colnum; j++) {
					HSSFCell cell = row.getCell(j);
					if (cell == null)
						continue;
					String contents = cell.getRichStringCellValue().getString();
					if (Pattern.matches("\\$\\{[\\s]*[^}]*[\\s]*\\}", contents)) {
						Matcher sqlatoms = ptSqlatoms.matcher(contents);
						sqlatoms.find();
						String param = sqlatoms.group();
						Field field = clazz.getField(param);
						if (field == null) // 未找到属性
							continue;
						findprop = true;
						// 一旦找到属性，将整行属性名称取出，使用导入的数据进行数据的抽取
						List<String> propnames = new ArrayList<String>();
						int startxpos = j;
						for (; j < colnum; j++) {
							cell = row.getCell(j);
							contents = cell.getRichStringCellValue().getString();
							if (Pattern.matches("\\$\\{[\\s]*[^}]*[\\s]*\\}", contents)) {
								Matcher sqlatoms2 = ptSqlatoms.matcher(contents);
								sqlatoms2.find();
								propnames.add(sqlatoms2.group());
							} else {
								propnames.add(null);
							}
						}
						
						// 开始进行数据抽取
						HSSFSheet datasheet = importedExcel.getSheetAt(0);
						rownum = datasheet.getLastRowNum();
						
						for (;i <= rownum; i++) {
							HSSFRow datarow = datasheet.getRow(i);
							A item = clazz.newInstance();
							for (int k = 0; k < propnames.size(); k++) {
								
								Field fielval = clazz.getField(propnames.get(k));
								
								//读取后面的下拉列表的字段
								if(datarow == null) {
									break ;
								}
								if (datarow.getCell(k + startxpos) == null ) {
									continue;
								}
								if (fielval != null) {
									String val = datarow.getCell(k + startxpos).toString();
									if (datarow.getCell(k + startxpos).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
										val = df.format(datarow.getCell(k + startxpos).getNumericCellValue());
									}
									Object value= getParamValue(val, fielval.getType());
									if(value!=null) {
										fielval.set(item,value );
									}
								}
							}
							list.add(item);
						}
						
					}
					
					if (findprop)
						break;
				}
				
				if (findprop)
					break;
			}
		}
		
		return list;
	}
	
	public <A> List<A> getItemsFromExcel(InputStream in, Class<A> clazz, String filename) throws IOException, SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {
		if (filename.endsWith(".xls")) {
			return getItemsFromExcel(in, clazz);
		} else {
			List<A> list = new ArrayList<A>();
			XSSFWorkbook importedExcel = new XSSFWorkbook(in); 
			
			if (hbook != null) {
				HSSFSheet sheet = hbook.getSheetAt(0);
				int rownum = sheet.getLastRowNum();
				int colnum = 0;
				boolean findprop = false;
				DecimalFormat df = new DecimalFormat("0.########");
				for(int i = 0; i <= rownum; i++) {
					HSSFRow row = sheet.getRow(i);
					if (row == null)
						continue;
					colnum = row.getLastCellNum();
					for (int j = 0; j < colnum; j++) {
						HSSFCell cell = row.getCell(j);
						if (cell == null)
							continue;
						String contents = cell.getRichStringCellValue().getString();
						if (Pattern.matches("\\$\\{[\\s]*[^}]*[\\s]*\\}", contents)) {
							Matcher sqlatoms = ptSqlatoms.matcher(contents);
							sqlatoms.find();
							String param = sqlatoms.group();
							Field field = clazz.getField(param);
							if (field == null) // 未找到属性
								continue;
							findprop = true;
							// 一旦找到属性，将整行属性名称取出，使用导入的数据进行数据的抽取
							List<String> propnames = new ArrayList<String>();
							int startxpos = j;
							for (; j < colnum; j++) {
								cell = row.getCell(j);
								contents = cell.getRichStringCellValue().getString();
								if (Pattern.matches("\\$\\{[\\s]*[^}]*[\\s]*\\}", contents)) {
									Matcher sqlatoms2 = ptSqlatoms.matcher(contents);
									sqlatoms2.find();
									propnames.add(sqlatoms2.group());
								} else {
									propnames.add(null);
								}
							}
							
							// 开始进行数据抽取
							XSSFSheet datasheet = importedExcel.getSheetAt(0);
							rownum = datasheet.getLastRowNum();
							
							for (;i <= rownum; i++) {
								XSSFRow datarow = datasheet.getRow(i);
								A item = clazz.newInstance();
								for (int k = 0; k < propnames.size(); k++) {
									
									Field fielval = clazz.getField(propnames.get(k));
									
									//读取后面的下拉列表的字段
									if(datarow == null) {
										break ;
									}
									if (datarow.getCell(k + startxpos) == null ) {
										continue;
									}
									if (fielval != null) {
										String val = datarow.getCell(k + startxpos).toString();
										if (datarow.getCell(k + startxpos).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
											val = df.format(datarow.getCell(k + startxpos).getNumericCellValue());
										}
										Object value= getParamValue(val, fielval.getType());
										if(value!=null) {
											fielval.set(item,value );
										}
									}
								}
								list.add(item);
							}
							
						}
						
						if (findprop)
							break;
					}
					
					if (findprop)
						break;
				}
			}
			
			return list;
		}
		
	}
	
	private final static Object getParamValue(String cellvalstr, Class<?> type) {
		String typeName = type.getName();
		
		if (!TextUtils.stringSet(cellvalstr))
			return null;
		
		if (typeName.equals("java.lang.String")) {
			return cellvalstr;
		} else if (typeName.equals("int") || typeName.equals("java.lang.Integer")) {
			return Double.valueOf(cellvalstr).intValue();
		} else if (typeName.equals("double") || typeName.equals("java.lang.Double")) {
			return Double.valueOf(cellvalstr);
		} else if (typeName.equals("java.sql.Date") || typeName.equals("java.util.Date")) {
			try {
				if (TextUtils.stringSet(cellvalstr)) {
					if (cellvalstr.length() <= 10)
						return new Timestamp(BaseComponent.getNewSdf().parse(cellvalstr).getTime());
					else
						return new Timestamp(BaseComponent.getNewTimeSdf().parse(cellvalstr).getTime());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (typeName.equals("java.sql.Timestamp")) {
			try {
				if (TextUtils.stringSet(cellvalstr)) {
					if (cellvalstr.length() <= 10)
						return new Timestamp(BaseComponent.getNewSdf().parse(cellvalstr).getTime());
					else
						return new Timestamp(BaseComponent.getNewTimeSdf().parse(cellvalstr).getTime());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (typeName.equals("long") || typeName.equals("java.lang.Long")) {
			return Double.valueOf(cellvalstr).longValue();
		} else if (typeName.equals("float") || typeName.equals("java.lang.Float")) {
			return Float.valueOf(cellvalstr);
		}

		return null;
	}
	
	private  void  genCopyOfTemp2(String tmplname)throws IOException,BiffException{
		String filePath = this.getClass().getResource("/exceltmpls/" + tmplname + ".xls").getFile();
		
		// linux下不要去除最前面的斜线
		if (filePath.indexOf(":") != -1)
			filePath = filePath.replaceFirst("^/*", "");
		
		FileInputStream file = new FileInputStream(filePath);
		POIFSFileSystem fs1 = new POIFSFileSystem(file); 
		hbook = new HSSFWorkbook(fs1);
		file.close();
	}
	
	private  void  genCopyOfTemp2(String tmplname, String filename, OutputStream os)throws IOException,BiffException{
		
		String filePath = this.getClass().getResource("/exceltmpls/" + tmplname + ".xls").getFile();
		
		// linux下不要去除最前面的斜线
		if (filePath.indexOf(":") != -1)
			filePath = filePath.replaceFirst("^/*", "");
		
		FileInputStream file = new FileInputStream(filePath);
		POIFSFileSystem fs1 = new POIFSFileSystem(file); 
		hbook = new HSSFWorkbook(fs1);
		out = os; 
		file.close();
	}
	
	public void wrapData(JSONObject obj) {
		
		if (hbook != null) {
			
			HSSFSheet sheet = hbook.getSheetAt(0);
			
			int rownum = sheet.getLastRowNum();
			int colnum = 0;
			for(int i = 0; i <= rownum; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row == null)
					continue;
				colnum = row.getLastCellNum();
				for (int j = 0; j < colnum; j++) {
					HSSFCell cell = row.getCell(j);
					if (cell == null)
						continue;
					String contents = cell.getRichStringCellValue().getString();
					if (Pattern.matches("\\$\\{[\\s]*[^}]*[\\s]*\\}", contents)) {
						Matcher sqlatoms = ptSqlatoms.matcher(contents);
						sqlatoms.find();
						String param = sqlatoms.group();
						if (obj.has(param)) {
							try {
								HSSFCellStyle cellStyle = cell.getCellStyle();
								cellStyle.setWrapText(true);
								cell.setCellValue(new HSSFRichTextString(obj.getString(param)));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			return;
		}
	}
	
	public void wrapData(String[] keys, JSONArray arr) throws JSONException {
		Set<String> keyset = new HashSet<String>();
		for(int i = 0; i < keys.length; i++) {
			keyset.add(keys[i]);
		}
		
		if (hbook != null) {
			
			if (hbook.getNumberOfSheets() > 0) {
				HSSFSheet sheet = hbook.getSheetAt(0);
				int rownum = sheet.getLastRowNum();
				int colnum = 0;
				
				HSSFDataFormat createDataFormat = hbook.createDataFormat();
				short format = createDataFormat.getFormat("@");
				
				for(int i = 0; i <= rownum; i++) {
					HSSFRow row = sheet.getRow(i);
					if (row == null)
						continue;
					colnum = row.getLastCellNum();
					for (int j = 0; j < colnum; j++) {
						HSSFCell cell = row.getCell(j);
						if (cell == null)
							continue;
						String contents = cell.getRichStringCellValue().getString();
						HSSFCellStyle cellStyle = cell.getCellStyle();
						if (Pattern.matches("\\$\\{[\\s]*[^}]*[\\s]*\\}", contents)) {
							Matcher sqlatoms = ptSqlatoms.matcher(contents);
							sqlatoms.find();
							String param = sqlatoms.group();
							if (keyset.contains(param)) {
							 
								for(int k = 0; k < arr.length(); k++) {
									JSONObject obj = arr.getJSONObject(k);
									try {
										HSSFRow row2 = sheet.getRow(i + k);
										
										if(j == 0 && k != arr.length() - 1) {//仅第一列遍历完,同时移动行
											try {
												sheet.shiftRows(i + k + 1, sheet.getLastRowNum(), 1, true, true);
											} catch (IllegalArgumentException e) {
												/* 此处报与公式相关的异常，但不影响正常导出，所以给与屏蔽
												 * java.lang.IllegalArgumentException: firstMovedIndex, lastMovedIndex out of order
													at org.apache.poi.ss.formula.FormulaShifter.<init>(FormulaShifter.java:56)
													at org.apache.poi.ss.formula.FormulaShifter.createForRowShift(FormulaShifter.java:81)
												 * */
												//	e.printStackTrace();
											}
											//row2 = sheet.createRow(i + k+1);
										}
										
										if (row2 == null) {
											row2 = sheet.createRow(i + k);
										}
										
										HSSFCell cell2 = row2.getCell(j);
										if (cell2 == null)
											cell2 = row2.createCell(j);
										cellStyle.setWrapText(true);
										cellStyle.setDataFormat(format);
										cell2.setCellStyle(cellStyle);
										cell2.setCellValue(new HSSFRichTextString(""));
										if (obj.has(param)) {
											cell2.setCellValue(new HSSFRichTextString(obj.getString(param)));
										}
								
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								
							}
						}
					}
				}
			}
			return;
		}
	}
	
	private void removeAllParams() throws RowsExceededException, WriteException {
		
		if (hbook != null) {
			HSSFSheet sheet = hbook.getSheetAt(0);
			sheet.getLastRowNum();
			int rownum = sheet.getLastRowNum();
			int colnum = 0;
			for(int i = 0; i <= rownum; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row == null)
					continue;
				colnum = row.getLastCellNum();
				for (int j = 0; j < colnum; j++) {
					HSSFCell cell = row.getCell(j);
					if (cell != null) {
						String contents = cell.getRichStringCellValue().getString();
						if (Pattern.matches("\\$\\{[\\s]*[^}]*[\\s]*\\}", contents)) {
							cell.setCellValue(new HSSFRichTextString(""));
						}
					}
				}
			}
			
			return;
		}
		
	}
	
	public void mergeCells(int i, int j, int k, int l) throws RowsExceededException, WriteException {
		if (hbook != null) {
			if (hbook.getNumberOfSheets() > 0) {
				HSSFSheet sheet = hbook.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(i, j, k, l));
			}
			return;
		}
		
	}
	
	public void closeWbook() {
		if (hbook != null) {
			try {
				removeAllParams();
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			try {
				hbook.write(out);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
	}

}
