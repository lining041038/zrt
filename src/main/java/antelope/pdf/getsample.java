package antelope.pdf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDExtendedGraphicsState;

public class getsample {
	public static int px = 0;
	public static int py = 0;
	public static List tokens = null;
	public static PDResources resources = null;
	public static Map<String, PDExtendedGraphicsState> gs = new HashMap<String, PDExtendedGraphicsState>();

	public static void main(String[] args) {
		try {
			work("E:\\pdf\\nssdwatermark2.pdf", 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void work(String path, int px, int py) throws Exception {
		PDDocument document = null;
		document = PDDocument.load(path);

		PDDocumentCatalog catalog = document.getDocumentCatalog();
		List<PDPage> pages = catalog.getAllPages();
		boolean flag = true;
		for (int p = 0; p < pages.size(); p++) {
			PDPage page = (PDPage) pages.get(p);
			resources = page.getResources();
			Map<String, PDExtendedGraphicsState> temp = resources
					.getGraphicsStates();
			System.out
					.println("***********************************************");
			gs.putAll(temp);
			resources.setGraphicsStates(gs);
			for (int i = 0; i < gs.size(); i++) {
				System.out.println(i + gs.toString());
			}
			System.out.println("resources == " + resources);
			PDFStreamParser parser = new PDFStreamParser(page.getContents());
			parser.parse();
			tokens = parser.getTokens();
			System.out.println("**************************start");
			for (int i = 16; i < tokens.size(); i++) {
				System.out.println(tokens.get(i).toString());
				Object item = tokens.get(i);
				
				if (item instanceof COSFloat) {
					float value = ((COSFloat) item).floatValue();
					float pos = 0;
					if (flag) {
						pos = px;
					} else {
						pos = py;
					}
					flag = !flag;
					item = new COSFloat(value + pos);
					tokens.set(i, item);
				}
				if (item instanceof COSInteger) {
					int value = ((COSInteger) item).intValue();
					int pos = 0;
					if (flag) {
						pos = px;
					} else {
						pos = py;
					}
					flag = !flag;
					item = new COSFloat(value + pos);
					tokens.set(i, item);
				}
			}
			System.out.println("**************************end");
		}
		document.close();
	}
}
