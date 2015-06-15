package antelope.pdf;

import java.lang.reflect.Method;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;


/**
 * pdf元数据工具提取类
 * @author lining
 * @since 2013-12-24
 */
public class PdfMetaDataUtil {
	
	/* 标准业务接口  begin*/
	
	/**
	 * 查看PDF标题
	 * @param pdffilepath pdf文件路径
	 * @return PDF标题
	 */
	public static String viewPdfTitle(String pdffilepath) {
		return getInfoInner(pdffilepath, "getTitle");
	}
	
	/**
	 * 查看PDF作者
	 * @param pdffilepath pdf文件路径
	 * @return PDF作者
	 */
	public static String viewPdfAuthor(String pdffilepath) {
		return getInfoInner(pdffilepath, "getAuthor");
	}
	
	/**
	 * 查看PDF主题
	 * @param pdffilepath pdf文件路径
	 * @return PDF主题
	 */
	public static String viewPdfSubject(String pdffilepath) {
		return getInfoInner(pdffilepath, "getSubject");
	}
	
	/**
	 * 查看PDF关键字
	 * @param pdffilepath pdf文件路径
	 * @return PDF关键字
	 */
	public static String viewPdfKeywords(String pdffilepath) {
		return getInfoInner(pdffilepath, "getKeywords");
	}
	
	/**
	 * 查看PDF应用程序
	 * @param pdffilepath pdf文件路径
	 * @return PDF应用程序
	 */
	public static String viewPdfCreator(String pdffilepath) {
		return getInfoInner(pdffilepath, "getCreator");
	}
	
	/**
	 * 查看PDF制作程序
	 * @param pdffilepath pdf文件路径
	 * @return PDF制作程序
	 */
	public static String viewPdfProducer(String pdffilepath) {
		return getInfoInner(pdffilepath, "getProducer");
	}

	/**
	 * 擦除PDF标题
	 * @param pdffilepath pdf文件路径
	 */
	public static void removePdfTitle(String pdffilepath) {
		setInfoInner(pdffilepath, "setTitle", "");
	}
	
	/**
	 * 擦除PDF作者
	 * @param pdffilepath pdf文件路径
	 */
	public static void removePdfAuthor(String pdffilepath) {
		setInfoInner(pdffilepath, "setAuthor", "");
	}
	
	/**
	 * 擦除PDF主题
	 * @param pdffilepath pdf文件路径
	 */
	public static void removePdfSubject(String pdffilepath) {
		setInfoInner(pdffilepath, "setSubject", "");
	}
	
	/**
	 * 擦除PDF关键字
	 * @param pdffilepath pdf文件路径
	 */
	public static void removePdfKeywords(String pdffilepath) {
		setInfoInner(pdffilepath, "setKeywords", "");
	}
	
	/**
	 * 擦除PDF应用程序
	 * @param pdffilepath pdf文件路径
	 */
	public static void removePdfApp(String pdffilepath) {
	
	}
	
	/**
	 * 擦除PDF制作程序
	 * @param pdffilepath pdf文件路径
	 */
	public static void removePdfMakerApp(String pdffilepath) {
		setInfoInner(pdffilepath, "setProducer", "");
	}
	
	/**
	 * 设置PDF标题
	 * @param pdffilepath pdf文件路径
	 * @param title pdf标题
	 */
	public static void setPdfTitle(String pdffilepath, String title) {
		setInfoInner(pdffilepath, "setTitle", title);
	}
	
	/**
	 * 设置PDF作者
	 * @param pdffilepath pdf文件路径
	 * @param author pdf作者
	 */
	public static void setPdfAuthor(String pdffilepath, String author) {
		setInfoInner(pdffilepath, "setAuthor", author);
	}
	
	/**
	 * 设置PDF主题
	 * @param pdffilepath pdf文件路径
	 * @param subject pdf主题
	 */
	public static void setPdfSubject(String pdffilepath, String subject) {
		setInfoInner(pdffilepath, "setSubject", subject);
	}
	
	/**
	 * 设置PDF关键字
	 * @param pdffilepath pdf文件路径
	 * @param keywords pdf关键字
	 */
	public static void setPdfKeywords(String pdffilepath, String keywords) {
		setInfoInner(pdffilepath, "setKeywords", keywords);
	}
	
	/**
	 * 设置PDF应用程序
	 * @param pdffilepath pdf文件路径
	 * @param app pdf应用程序
	 */
	public static void setPdfCreator(String pdffilepath, String app) {
		setInfoInner(pdffilepath, "setCreator", app);
	}
	
	/**
	 * 设置PDF制作程序
	 * @param pdffilepath pdf文件路径
	 * @param makerapp pdf制作程序
	 */
	public static void setPdfProducer(String pdffilepath, String makerapp) {
		setInfoInner(pdffilepath, "setProducer", makerapp);
	}
	/* 标准业务接口  end*/
	
	
	private static String getInfoInner(String pdffilepath, String funcname) {
		PDDocument document = null;
		try {
			document = PDDocument.load(pdffilepath);
			PDDocumentInformation info = document.getDocumentInformation();
			
			Method method = info.getClass().getMethod(funcname);
			if(method.invoke(info) == null)return "";
			return method.invoke(info) + "";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	static void setInfoInner(String pdffilepath, String funcname, String val)
	{
		PDDocument document = null;
		try {
			document = PDDocument.load(pdffilepath);
			PDDocumentInformation info = document.getDocumentInformation();
			Method method = info.getClass().getMethod(funcname,String.class);
//			Method method = info.getClass().getMethod(funcname);
			method.invoke(info, val);
			document.save(pdffilepath.replace(".pdf", "addinfo.pdf"));
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
}