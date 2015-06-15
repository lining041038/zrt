package antelope.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import sun.util.logging.resources.logging;
 
/**
 *
 * @author huanggc
 * @since 2012-3-25
 */
public class ZipUtils {
	
	private static Logger log = Logger.getLogger(ZipUtils.class);
		/**
		 * 递归压缩
		 * @param sourceDir
		 * @param zipDir
		 * @param student_id
		 * @return
		 */
	    public static String zip(String sourceDir, String zipDir) {
	       OutputStream os;
	       String zipName="";
	       ZipOutputStream zos=null;
	       try {
	           File file = new File(sourceDir);
	           String basePath = null ;
	           if (file.isDirectory()) {
	              basePath = file.getParent();
	              zipName=file.getName();
	           } else {
	              basePath = file.getParent();
	              zipName=file.getName();
	              
	           }
	           os = new FileOutputStream(zipDir+zipName+".zip");
	           BufferedOutputStream bos = new BufferedOutputStream(os);
	           zos = new ZipOutputStream(bos);
	           zos.setEncoding("UTF-8");//设置的和文件名字格式一样

	           zipFile (file, basePath, zos);
	       } catch (Exception e) {
	           e.printStackTrace();
	       }finally{
	    	   try {
				zos.closeEntry();
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	       }
	 
	       return zipName;
	    }
	 
	    private static void zipFile(File source, String basePath,
	           ZipOutputStream zos) {
	    	zos.setEncoding("GBK");
	       File[] files = new File[0];
	 
	       if (source.isDirectory()) {
	           files = source.listFiles();
	       } else {
	           files = new File[1];
	           files[0] = source;
	       }
	 
	       String pathName;
	       byte [] buf = new byte [1024];
	       int length = 0;
	       try {
	           for (File file : files) {
	              if (file.isDirectory()) {
	                  pathName = file.getPath().substring(basePath.length() + 1)
	                         + "/" ;
	                  zos.putNextEntry( new ZipEntry(pathName));
	                  zipFile (file, basePath, zos);
	              } else {
	              	  int plusone = 1;
	            	  if (basePath.endsWith("\\")) 
	            		  plusone = 0;
	                  pathName = file.getPath().substring(basePath.length() + plusone);
	                  InputStream is = new FileInputStream(file);
	                  BufferedInputStream bis = new BufferedInputStream(is);
	                  zos.putNextEntry( new ZipEntry(pathName));
	                  while ((length = bis.read(buf)) > 0) {
	                     zos.write(buf, 0, length);
	                  }
	                  is.close();
	              }
	           }
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	 
	    } 
	 
	    /**
	      * 解压 zip 文件 
	      * @param zipfile
	      * @param destDir
	      * @throws IOException
	      */
	    public static void unZip(String zipfile, String destDir) {
	 
	       destDir = destDir.endsWith( "\\" ) ? destDir : destDir + "\\" ;
	       
	       byte b[] = new byte [1024];
	       int length;
	 
	       ZipFile zipFile = null;
	       try {
	           zipFile = new ZipFile( new File(zipfile));
	           Enumeration enumeration = zipFile.getEntries();
	           ZipEntry zipEntry = null ;
	 
	           while (enumeration.hasMoreElements()) {
	              zipEntry = (ZipEntry) enumeration.nextElement();
	              File loadFile = new File(destDir + zipEntry.getName());
	 
	              if (zipEntry.isDirectory()) {
	                  // 这段都可以不要，因为每次都貌似从最底层开始遍历的
	                  loadFile.mkdirs();
	              } else {
	                  if (!loadFile.getParentFile().exists())
	                     loadFile.getParentFile().mkdirs();
	 
	                  OutputStream outputStream = new FileOutputStream(loadFile);
	                  InputStream inputStream = zipFile.getInputStream((org.apache.tools.zip.ZipEntry) zipEntry);
	 
	                  while ((length = inputStream.read(b)) > 0)
	                     outputStream.write(b, 0, length);
	 
	              }
	           }
	           log.info( " 文件解压成功 " );
	       } catch (IOException e) {
	           e.printStackTrace();
	       }finally{
	    	   try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		       }
	 
	    }
}
