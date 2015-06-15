package antelope.example;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;


@Controller
public class ExportWordExample extends BaseController{
	
	/**
	 * 导出word例子
	 * @param res
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	@RequestMapping("/example/ExportWordExample/exportword")
	public void exportword(HttpServletResponse res) throws DocumentException, IOException {
		//创建word文档   
		  Document document=new Document(PageSize.A4);
		  //输入word文档   
		  RtfWriter2.getInstance(document,new FileOutputStream("d:\\wordss.doc"));   
		  document.open();   
		  //中文字体   
		  BaseFont bfChinese=BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);   
		  Font fontChinese=new Font(bfChinese,12,Font.HELVETICA);   
		  //创建有3列的表格   
		  Table table=new Table(3);   
		 // document.add(new Paragraph("生成rft文档2222！",fontChinese));
		  table.setBorderWidth(1);   
		  table.setBorderColor(new Color(0,0,0));   
		  //table.setPadding(5);   
		  //table.setSpacing(5);   
		    
		  //添加表头元素   
		  Cell cell=new Cell("header");   
		  Cell cell2=new Cell("header");   
		  Cell cell3=new Cell("header");   
		  cell.setHeader(true);   
		  //cell.setColspan(3);   
		  table.addCell(cell);   
		  table.addCell(cell2);   
		  table.addCell(cell3);   
		  table.endHeaders();//表头结束   
		    
		  //表格主体   
		  cell=new Cell("Example cell with colspan 1 and rowspan 2");   
		  cell.setRowspan(2);   
		  cell.setBorderColor(new Color(0,0,0));   
		  table.addCell(cell);   
		  table.addCell("1.1");   
		  table.addCell("2.1");   
		  table.addCell("1.2");   
		  table.addCell("2.2");   
		  table.addCell(new Paragraph("测试1",fontChinese));   
		  table.addCell("big cell");   
		  cell.setRowspan(2);   
		  cell.setColspan(2);   
		//  table.addCell(cell);   
		  table.addCell(new Paragraph("测试2",fontChinese));   
		  document.add(table);   
		  //在表格末尾添加图片   
	//	  Image png=Image.getInstance("d:\\duck.jpg");   
		//  document.add(png);   
		  document.close();  
		  
		  FileInputStream fis = new FileInputStream("d:\\wordss.doc");
		  byte[] byts = new byte[fis.available()];
		  fis.read(byts);
		  printBytes(byts, "测试中文.doc", res);
	}
}
