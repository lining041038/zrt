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
import antelope.services.supportclasses.WordTmpl;
import antelope.springmvc.BaseComponent;
import antelope.utils.ZipUtils;

/**
 * word导出服务类
 * @author lining
 * @since 2013-11-10
 */
@Service
public class WordExportService extends BaseComponent {
	
	/**
	 * 获取对应的word模板实例
	 * 此种方式获取的模板实例只允许用来导出，若需要导入则请调用含有单个参数的getTmplInstance方法
	 * @param tmplname
	 */
	public WordTmpl getTmplInstance(String tmplname, String filename, HttpServletResponse res) throws UnsupportedEncodingException {
		try {
			ServletOutputStream os = res.getOutputStream();
			res.setHeader("Pragma", "No-cache");
			res.setHeader("Cache-Control", "no-cache");
			res.setDateHeader("Expires", 0);
			res.setHeader("Content-Disposition","attachment; filename="+new String(filename.getBytes("gb2312"), "ISO8859-1" ));
			return new WordTmpl(tmplname, filename, os);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (BiffException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
}
