package antelope.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;




public class ClasspathResourceUtil {
	private static ClasspathResourceUtil resouceutil;
	private static Logger log =Logger.getRootLogger();
	
	private ClasspathResourceUtil() {
	}
	
	private static ClasspathResourceUtil getInstance() {
		if (resouceutil == null)
			resouceutil = new ClasspathResourceUtil(); 
		return resouceutil;
	}
	
	/**
	 * 根据路径获取文本数据
	 * @param classpath
	 * @return
	 * @throws IOException 
	 */
	public static String getTextByPath(String classpath) throws IOException {
		InputStream in = getInstance().getClass().getResourceAsStream(classpath);
		if (in == null) {
			throw new FileNotFoundException(classpath + "（系统找不到指定的文件。）");
		}
		byte[] chs = new byte[in.available()];
		in.read(chs);
		in.close();
		return new String(chs, "utf-8");
	}
	/**
	 * 读取文件，若文件修改后则重新加载，否则使用缓存
	 * @since 2012/01/29
	 * @param classpath
	 * @return
	 * @throws IOException
	 */
	public static String getTextByPathNoCached(String classpath) throws IOException {
		return getTextByPathNoCachedReturnResult(classpath).result;
	}
	
	/**
	 * 加载项目中WEB_INF下lib目录下的文件
	 * 一般用户加载jni调用c程序的dll文件
	 */
	public static void load_WEB_INF_lib_File(String filename) {
		URL url = getInstance().getClass().getResource("/");
		File file = new File(url.getFile());
		System.load(file.getParentFile().getAbsolutePath() + "/lib/" + filename);
	}
	
	/**
	 * 获取未缓存的配置文件的流数据,注意使用完此流一定要进行关闭
	 */
	public static InputStream getInputStreamByPathNoCached(String classpath) throws FileNotFoundException {
		NoCacheReturn cachresult = new NoCacheReturn();
		return getInputStreamNoCached(classpath, cachresult);
	}
	
	/**
	 * 读取文件，若文件修改后则重新加载，否则使用缓存
	 * @since 2012/01/29
	 * @param classpath
	 * @return
	 * @throws IOException
	 */
	public static NoCacheReturn getTextByPathNoCachedReturnResult(String classpath) throws IOException {
		NoCacheReturn cachresult = new NoCacheReturn();
		InputStream in = getInputStreamNoCached(classpath, cachresult);
		byte[] chs = new byte[in.available()];
		in.read(chs);
		in.close();
		cachresult.result = new String(chs, "utf-8");
		return cachresult;
	}

	private static InputStream getInputStreamNoCached(String classpath, NoCacheReturn cachresult) throws FileNotFoundException {
		// linux下 classpath有可能为全路径，需要进行修剪
		if (classpath != null && classpath.contains("WEB-INF")) {
			classpath = classpath.substring(classpath.indexOf("WEB-INF/classes") + 15);
		}
		Assert.assertNotNull( "您请求的文件" + classpath + "不存在！", getInstance().getClass().getResource(classpath));
		String filePath=getInstance().getClass().getResource(classpath).getFile();
		File currFile=new File(filePath);
		InputStream in=null;
		boolean modifiedB=isModified(classpath,currFile.lastModified());
		if(modifiedB){//重新加载
			cachresult.ismodified = true;
			  in =new FileInputStream(filePath);
		}else {
			  in = getInstance().getClass().getResourceAsStream(classpath);
			if (in == null) {
				throw new FileNotFoundException(classpath + "（系统找不到指定的文件。）");
			}
		}
		return in;
	}
	
	public static class NoCacheReturn {
		public String result;
		public boolean ismodified = false;
	}
	
	/**
	 * 判断文件是否修改
	 * @param keypath
	 * @return
	 */
	public static boolean isModified(String keypath,long curr){	
		boolean isModified=false;
		String lastModified=System.getProperty(keypath);
		if(null==lastModified){
			System.setProperty(keypath, Long.valueOf(curr).toString());
		}else {
			long lastModifiedL=Long.valueOf(System.getProperty(keypath));
			isModified=curr!=lastModifiedL;
		}
		
		return isModified;
		
	}
	
	public static Document getXMLDocumentByPath(String classpath) throws IOException, DocumentException {
		String text = getTextByPathNoCached(classpath);
		// 首行出现特殊字符处理
		int i = 0;
		while(text.charAt(i) != '<') {
			++i;
		}
		text = text.substring(i);
		return DocumentHelper.parseText(text);
	}
	
	/**
	 * 获取项目webapp目录下某文件夹路径如想获取 webapp/themes路径则调用方式为
	 * File folder = getWebappFolderFile("/themes");
	 * @param webappPath
	 * @return
	 */
	public static File getWebappFolderFile(String webappPath) {
		String file = getInstance().getClass().getResource("/").getFile();
		// linux下不要去除最前面的斜线
		if (file.indexOf(":") != -1)
			file = file.replaceFirst("^/*", "");
		File fileObj = new File(file);
		String webappfolders = fileObj.getParentFile().getParentFile().getAbsolutePath() + webappPath;
		return new File(webappfolders);
	}
	
	
	/**
	 * 从webapp目录下的文件中获取文本数据
	 * @param webappPath
	 * @return
	 */
	public static String getTextByWebappPath(String webappPath) throws IOException {
		File file = getWebappFolderFile(webappPath);
		FileInputStream fis = new FileInputStream(file);
		byte[] byts = new byte[fis.available()];
		fis.read(byts);
		fis.close();
		return new String(byts, "utf-8");
	}
}




