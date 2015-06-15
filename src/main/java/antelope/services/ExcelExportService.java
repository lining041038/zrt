package antelope.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;

import org.springframework.stereotype.Service;

import antelope.services.supportclasses.ExcelTmpl;
import antelope.springmvc.BaseComponent;
import antelope.utils.ZipUtils;

/**
 * excel导出服务类
 * @author lining
 * @since 2012-2-23
 */
@Service
public class ExcelExportService extends BaseComponent {
	
	/**
	 * 获取对应的excel模板实例
	 * 此种方式获取的模板实例只允许用来导出，若需要导入则请调用含有单个参数的getTmplInstance方法
	 * @param tmplname
	 */
	public ExcelTmpl getTmplInstance(String tmplname, String filename, HttpServletResponse res) throws UnsupportedEncodingException {
		try {
			ServletOutputStream os = res.getOutputStream();
			res.setHeader("Pragma", "No-cache");
			res.setHeader("Cache-Control", "no-cache");
			res.setDateHeader("Expires", 0);
			res.setHeader("Content-Disposition","attachment; filename="+new String(filename.getBytes("gb2312"), "ISO8859-1" ));
			return new ExcelTmpl(tmplname, filename, os);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (BiffException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * 获取对应的excel模板实例
	 * 此种方式获取的模板实例只允许用来导入，若需要导出则请调用含有多个参数的getTmplInstance方法
	 * @param tmplname
	 */
	public ExcelTmpl getTmplInstance(String tmplname) throws BiffException, IOException {
		return new ExcelTmpl(tmplname);
	}
	
	private void downLoadFile(String filename, HttpServletResponse res) throws UnsupportedEncodingException {
		try {
 			res.setHeader("Pragma", "No-cache");
			res.setHeader("Cache-Control", "no-cache");
			res.setDateHeader("Expires", 0);
			res.setCharacterEncoding("UTF-8");
			res.setHeader("Content-Disposition","attachment; filename="+java.net.URLEncoder.encode(filename, "UTF-8")+";");
			FileInputStream  fis=new FileInputStream(System.getProperty("java.io.tmpdir")+"/package.zip");
			byte[] byts = new byte[fis.available()];
		  	    fis.read(byts);
		  	    fis.close();
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	          outputStream.write(byts);
	          ServletOutputStream streamOut = res.getOutputStream();
	          outputStream.writeTo(streamOut);
	          streamOut.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 
	 
	public void downLoad(String filename, HttpServletResponse res,MutiTempFileDownLoad  mutitempfile) throws Exception{
		File zipTemp=new File(System.getProperty("java.io.tmpdir"),"package");
		if (!zipTemp.exists()){
			zipTemp.mkdir();
		}else{
			for(File child:zipTemp.listFiles()){
				child.delete();
			}
		}
		//匿名类生成文件
		mutitempfile.genFiles(zipTemp);
		//打包下载
		ZipUtils.zip(zipTemp.getAbsolutePath(), System.getProperty("java.io.tmpdir"));
		downLoadFile(filename,res);
	}
	
	
}
