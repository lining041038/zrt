package antelope.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.PDExtendedGraphicsState;
import org.apache.pdfbox.util.PDFOperator;


import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;


public class PdfWaterMarkUtil {
	
	private static String[] samples = new String[2858];
	private static int samplesCounter = 0;
	private static int total = 0;
	private static String samplefile = System.getProperty("user.dir")+"\\res\\sample.pdf";;
	private static final boolean isCompress = false;
	
	/**
	 * 普通方式设置PDF文字水印对象
	 * @param pdffilepath pdf文件路径
	 * @param textval 文字水印
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void setNormalTextWaterMark(String pdffilepath, String textval) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(pdffilepath);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
				pdffilepath.replace(".pdf", "txtmark.pdf")));
		int total = reader.getNumberOfPages() + 1;
		PdfContentByte content;
		BaseFont base = BaseFont.createFont(BaseFont.COURIER_BOLD,
				BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
		base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
				BaseFont.EMBEDDED);
		Font fontChinese = new Font(base, 12, Font.NORMAL);
		int j = textval.length();
		char c = 0;
		int high = 0;
		for (int i = 1; i < total; i++) {
			// 水印的起始
			high = 200;
			// content = stamper.getUnderContent(i);
			content = stamper.getOverContent(i);
			// 开始
			content.beginText();
			// 设置颜色
			content.setColorFill(Color.BLUE);
			content.setColorStroke(Color.BLUE);
			// 设置字体及字号
			content.setFontAndSize(base, 18);
			// 设置起始位置
			content.setTextMatrix(40, 40);
			// 开始写入水印
			for (int k = 0; k < j; k++) {
				content.setTextRise(14);
				c = textval.charAt(k);
				// 将char转成字符串
				content.showText(c + "");
				high -= 5;
			}
			content.endText();

		}
		stamper.close();
	}
	
	/**
	 * 普通方式擦除PDF文字水印对象
	 * @param pdffilepath pdf文件路径
	 */
	public static void removeNormalTextWaterMark(String pdffilepath) {
		
	}
	
	/**
	 * 普通方式设置PDF图片水印对象
	 * @param pdffilepath pdf文件路径
	 * @param imagepath 图片水印路径
	 */
	public static void setNormalImageWaterMark(String pdffilepath, String imagepath) throws IOException, DocumentException {
		
		PdfReader reader = new PdfReader(pdffilepath, "PDF".getBytes());
		int pagesize = reader.getNumberOfPages();
		
		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(pdffilepath.replace(".pdf", "img.pdf")));
		Image img = Image.getInstance(imagepath);
		img.setAbsolutePosition(150, 100);
		for (int i = 1; i <= pagesize; i++) {
			PdfContentByte under = stamp.getUnderContent(i);
			under.addImage(img);
		}
		stamp.close();
		
	}
	
	/**
	 * 普通方式擦除PDF图片水印对象
	 * @param pdffilepath pdf文件路径
	 */
	public static void removeNormalImageWaterMark(String pdffilepath) {
		
	}
	
	/**
	 * 擦除知网PDF水印对象
	 * @param pdffilepath pdf文件路径
	 */
	public static void removeZhiWangWaterMark(String pdffilepath) {
		
		getSample();
		
		PDDocument document = null;
		File f = new File(pdffilepath);
		try {
			document = PDDocument.load(f);
			PDDocumentCatalog catalog = document.getDocumentCatalog();
			// List<PDPage> pages = catalog.getAllPages();
			if (null == catalog) {
				System.out.println("null error:" + f.getCanonicalPath());
			}
			List<PDPage> pages = catalog.getAllPages();

			for (Object pageObj : catalog.getAllPages()) {
				PDPage page = (PDPage) pageObj;
				PDFStreamParser parser = new PDFStreamParser(page.getContents());
				parser.parse();
				List tokens = parser.getTokens();
				int t = matchSample(tokens);
				// System.out.println(t);
				// int t1 = tokens.size()-sampleLen-2;
				if (t == -1) {

					t = tokens.size();
				}
				List head = tokens.subList(0, t);
				// int ll = tokens.size();
				// List tail = tokens.subList(ll-1, ll);
				// PDFOperator p = PDFOperator.getOperator("*Q");
				// head.add(p);

				PDStream newContents = new PDStream(document);
				ContentStreamWriter writer = new ContentStreamWriter(
						newContents.createOutputStream());
				writer.writeTokens(head);
				// writer.writeTokens(tokens );
				newContents.addCompression();
				page.setContents(newContents);
			}
			// 鐢熸垚鏂版枃浠�
			String path2 = f.getAbsolutePath();
			path2 = path2.replace(".pdf", "removed.pdf");
			File f2 = new File(path2);
			if (!f2.getParentFile().exists()) {
				f2.getParentFile().mkdirs();
			}
			document.save(path2);
			System.out.println("save " + path2);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(f.getAbsolutePath() + "文件没有保存成功！！！");
		} finally {
			try {
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * 设置增强型PDF水印对象
	 * @param pdffilepath pdf文件路径
	 * @param samplefilepath pdf水印样例文件所在路径
	 * @param px 水印样例x偏移量
	 * @param py 水印样例y偏移量
	 */
	public static void setPowerWaterMark(String pdffilepath, String samplefilepath) throws Exception {
		setWaterMarkInner(pdffilepath, samplefilepath, 0, 0);
	}
	
	/**
	 * 设置增强型PDF水印对象的偏移坐标
	 * @param pdffilepath pdf文件路径
	 * @param x 水印对象x坐标
	 * @param y 水印对象y坐标
	 */
	public static void setPowerWaterMarkOffset(String pdffilepath,String samplefilepath, int x, int y) throws Exception {
		setWaterMarkInner(pdffilepath, samplefilepath, x, y);
	}

	private static void setWaterMarkInner(String pdffilepath,
			String samplefilepath, int px, int py) throws Exception {
		if (!pdffilepath.endsWith(".pdf"))
			return;
		getsample.work(samplefilepath,px,py);
		PDDocument document = null;
		PDFOperator savestate = PDFOperator.getOperator("q");
		PDFOperator regainstate = PDFOperator.getOperator("Q");
		PDFOperator fillpath = PDFOperator.getOperator("f");
		PDFOperator strokepath = PDFOperator.getOperator("s");
		PDFOperator endpath = PDFOperator.getOperator("n");
		PDFOperator cmop = PDFOperator.getOperator("cm");
		try {
			document = PDDocument.load(pdffilepath);
			PDDocumentCatalog catalog = document.getDocumentCatalog();
			List<PDPage> pages = catalog.getAllPages();

//			for (Object pageObj : catalog.getAllPages()) {
			for (int p = 0;p<pages.size();p++) {
				System.out.println("page*****"+p); 
				PDPage page = (PDPage) pages.get(p);
				
				PDResources resources = page.getResources();
				Map<String, PDExtendedGraphicsState> graphicstate = new HashMap<String, PDExtendedGraphicsState>();
				graphicstate.putAll(getsample.gs);
				Map<String, PDExtendedGraphicsState> item = resources
						.getGraphicsStates();
				if (item != null) {
					graphicstate.putAll(item);
				}
				resources.setGraphicsStates(graphicstate);

				PDFStreamParser parser = new PDFStreamParser(page.getContents());
				parser.parse();
				List tokens = parser.getTokens();
				List mark = getsample.tokens;
				List head = mark.subList(0, 16);
				mark = mark.subList(16, mark.size());

				List resulttokens = new ArrayList<Object>();

				int len_tokens = tokens.size();
				int len_mark = mark.size() - 1;
				int index_tokens = 0;
				int index_mark = 0;
				boolean currentismark = false;
				int markcount = 0;

				resulttokens.add(savestate);
				String staterecord = "";
				while (true) {
					if (index_tokens >= len_tokens) {
						System.out.println("链接剩下的mark链");
						resulttokens.addAll(head);
						resulttokens.addAll(mark.subList(index_mark, len_mark));
						resulttokens.add(strokepath);
						break;
					}
					if (index_mark >= len_mark) {
						System.out.println("链接剩下的tokens链");
						resulttokens.add(regainstate);
						resulttokens.addAll(tokens.subList(index_tokens,len_tokens));
						break;
					}
					if (Math.random() >= 0.5) {
						currentismark = true;
					} else {
						currentismark = false;
					}
					if (currentismark) {
						System.out.println("--------------mark------------"+markcount++);
						
						double [] pos = toolkits.getpxpy(staterecord);
						System.out.println("位置为："+pos[0]+"   "+pos[1]);
						resulttokens.addAll(head);
						boolean xflag = true;
						while (true) {
							Object tp = mark.get(index_mark++);
							if(tp instanceof COSFloat){
								float value = ((COSFloat) tp).floatValue();
								System.out.println("oldvalue == "+value);
								if(xflag){
									value -= pos[0];
								}
								else{
									value -= pos[1];
								}
								System.out.println("newvalue == "+value);
								xflag = !xflag;
								tp = new COSFloat(value);
							}
							if(tp instanceof COSInteger){
								float value = ((COSInteger) tp).intValue();
								System.out.println("oldvalue == "+value);
								if(xflag){
									value -= pos[0];
								}
								else{
									value -= pos[1];
								}
								System.out.println("newvalue == "+value);
								xflag = !xflag;
								tp = new COSFloat(value);
							}
							resulttokens.add(tp);
							if (tp.toString().equals("PDFOperator{h}")) {
								resulttokens.add(strokepath);
								break;
							}
							if (index_mark >= len_mark)
								break;
						}
					} else {

						System.out.println("*************tokens*************");

						resulttokens.add(regainstate);
						while (true) {
							Object tm = tokens.get(index_tokens++);
							resulttokens.add(tm);
							if (tm.toString().equals("PDFOperator{cm}")) {
									Object x = tokens.get(index_tokens-3);
									Object y = tokens.get(index_tokens-2);
									float xvalue = 0;
									float yvalue = 0;
									if(x instanceof COSInteger)
									{
										xvalue = ((COSInteger) x).intValue();
									}
									if(x instanceof COSFloat)
									{
										xvalue = ((COSFloat) x).floatValue();
									}
									if(y instanceof COSInteger)
									{
										yvalue = ((COSInteger) y).intValue();
									}
									if(y instanceof COSFloat)
									{
										yvalue = ((COSFloat) y).floatValue();
									}
									staterecord += "sx"+xvalue+"exsy"+yvalue+"ey";
								}
							if (tm.toString().equals("PDFOperator{ET}"))
								break;
							/*
							 * if (tm.toString().equals("PDFOperator{h}"))
							 * break;
							 */
							if (tm.toString().equals("PDFOperator{EI}"))
								break;
							if (tm.toString().equals("PDFOperator{q}"))
							{
								System.out.println("the operator is q");
								staterecord += "q";
							}
							if (tm.toString().equals("PDFOperator{Q}"))
							{
								System.out.println("the operator is Q");
								staterecord = staterecord.substring(0,staterecord.lastIndexOf("q"));
							}	
							if (index_tokens >= len_tokens)
								break;
						}
						resulttokens.add(savestate);
						resulttokens.add(endpath);
					}
				}
				PDStream newContents = new PDStream(document);
				ContentStreamWriter writer = new ContentStreamWriter(
						newContents.createOutputStream());
				writer.writeTokens(resulttokens);
				if (isCompress)
					newContents.addCompression();
				page.setContents(newContents);
				page.setCropBox(page.getMediaBox());
			}
			System.out.println("处理完成" + pdffilepath);
			document.save(pdffilepath.replace(".pdf", "powermark.pdf"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	
	
	private static int matchSample(List ts) {
		int len = ts.size(), cur = 1;
		boolean flag = true;
		for (int i = 0; i < len; i++) {
			Object token = ts.get(i);
			if (token instanceof PDFOperator) {
				PDFOperator op = (PDFOperator) token;
				// System.out.print(op.getOperation());
				if (op.getOperation().equals("g")) {
					// System.out.print(debugCounter++);
					/*
					 * if (debugCounter == 11){ System.out.print(1); }
					 */
					flag = true;
					for (int j = 1; j < 100; j++) {
						Object token2 = ts.get(i + j);
						if (token2 instanceof PDFOperator) {
							PDFOperator op2 = (PDFOperator) token2;
							if (!op2.getOperation().equals(samples[cur])) {
								flag = false;
								cur = 1;
								break;
							} else {
								cur++;
							}
						}
					}
					if (flag)
						return i - 1;
				}
			}
		}
		return -1;
	}
	
	private static void getSample() {
		try {
			PDDocument document = PDDocument.load(samplefile);
			PDDocumentCatalog catalog = document.getDocumentCatalog();
			List<PDPage> pages = catalog.getAllPages();
			PDPage page = (PDPage) pages.get(0);
			PDFStreamParser parser = new PDFStreamParser(page.getContents());
			parser.parse();
			List tokens = parser.getTokens();
			List newTokens = new ArrayList();
			for (int j = 0; j < tokens.size(); j++) {
				Object token = tokens.get(j);
				if (token instanceof PDFOperator) {
					total++;
					PDFOperator op = (PDFOperator) token;
					if (total >= 10 && total <= 2867) {
						samples[samplesCounter++] = op.getOperation();
						System.out.println(op.getOperation());
					}
				}
			}
			document.close();
			System.out.println("----------------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
