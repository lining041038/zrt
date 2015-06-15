package antelope.utils;



import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public abstract class Export {
	protected HttpServletRequest request;
	protected WritableWorkbook wkbook;
	protected WritableSheet sheet;
	protected ByteArrayOutputStream os;
	// 常用单元格格式0.普通文本1.人员姓名2.数字.3.表头 .4.最左边侧列 
	private static WritableCellFormat[] format;
	//private static WritableFont[] font; 对字体做相应格式化处理,
	// 初始化单元格格式
	{
		format = new WritableCellFormat[8];
		for (int i = 0; i < format.length; i++) {
			format[i] = new WritableCellFormat();
			try {
				format[i].setBorder(Border.ALL, BorderLineStyle.THIN);
				format[i].setWrap(true);
				format[i].setVerticalAlignment(VerticalAlignment.CENTRE);
				switch (i) {
					case 0 :// 普通文本 左对齐
						format[i].setAlignment(Alignment.LEFT);
						break;
					case 1 :// 人员姓名 居中
						format[i].setAlignment(Alignment.CENTRE);
						break;
					case 2 :// 数字 靠右对齐
						format[i].setAlignment(Alignment.RIGHT);
						break;
					case 3:// 表头 局中对齐,后面为黄色
						format[i].setAlignment(Alignment.CENTRE);
						format[i].setBackground(Colour.VERY_LIGHT_YELLOW);
						break;
					case 4:// 最左侧列 局中对齐后面为黄色
						format[i].setAlignment(Alignment.CENTRE);
						break;
					case 5://一级汇总
						format[i].setAlignment(Alignment.CENTRE);
						format[i].setBackground(Colour.GREY_50_PERCENT);
						break;
					case 6://二级汇总
						format[i].setAlignment(Alignment.CENTRE);
						format[i].setBackground(Colour.GREY_40_PERCENT);
						break;
					case 7://三级汇总
						format[i].setAlignment(Alignment.CENTRE);
						format[i].setBackground(Colour.GRAY_25);
						break;
				}
			} catch (WriteException e) {
				System.err.println("初始化表格常用格式发生错误");
				e.printStackTrace();
			}
		}
	}
	public Export(ByteArrayOutputStream os, HttpServletRequest request) throws IOException {
		this.request = request;
		this.os = os;
		wkbook = Workbook.createWorkbook(os);
	}
	
	/**
	 * 添加单元格,并设置格式<br>
	 * 常用单元格格式0.普通文本1.人员姓名2.数字.3.表头 .4.最左边侧列 5.一级汇总 6.二级汇总 7 三级汇总
	 */
	protected void addCellAndFormat(int col,int row,String value,int fmNo) throws RowsExceededException, WriteException {
		if ("null".equals(value) || "-1".equals(value)) value = "";
		sheet.addCell(setFormat(new Label(col,row,value),fmNo));
	}
	/**
	 * 添加单元格,并设置格式<br>
	 * 常用单元格格式0.普通文本1.人员姓名2.数字.3.表头 .4.最左边侧列 5.一级汇总 6.二级汇总 7 三级汇总
	 */
	protected void addCellAndFormatAndSetColume(int col,int row,String value,int fmNo) throws RowsExceededException, WriteException {
		if ("null".equals(value) || "-1".equals(value)) value = "";
		sheet.addCell(setFormat(new Label(col,row,value),fmNo));
		sheet.setColumnView(col,16);
	}
	/**
	 * 合并单元格
	 */
	protected void mergeCell(int colf, int rowf, int cole, int rowe) throws RowsExceededException, WriteException {
		if (colf > cole || rowf > rowe)return;
		if(colf!=cole || rowf!=rowe) sheet.mergeCells(colf, rowf, cole, rowe);
	}
	
	/**
	 * 创建表头
	 */
	protected void creatHeads(String[] heads,int[] colsWidth) throws RowsExceededException, WriteException {
		Label label;
		for (int i = 0; i < heads.length; i++) {
			label = new Label(i, 0, heads[i]);
			sheet.addCell(setFormat(label, 3));
			sheet.setColumnView(i, colsWidth[i]);
		}
	}
	
	/**
	 * 为单元格设置相应格式
	 */
	private WritableCell setFormat(Label label,int fmNum) {
		label.setCellFormat(format[fmNum]);
		return label;
	}
	public abstract void  export() throws Exception;
}
