package antelope.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.entities.SysFormulaItem;
import antelope.springmvc.BaseController;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.SpeedIDUtil;
import antelope.utils.SystemOpts;


@Controller
public class AfeUtilController extends BaseController {

	
	/**
	 * 生成新的公式并返回公式对应的sid 
	 */
	@RequestMapping("/common/AfeUtilController/addNewLatex")
	public void addNewFormula(HttpServletRequest req, HttpServletResponse res) throws FileUploadException, IOException, IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException {
		SysFormulaItem formula = new SysFormulaItem();
		formula.sid = SpeedIDUtil.getId();
		formula.latex = d(req.getParameter("latex"));
		dao.insertOrUpdate(formula);
		print(formula.sid, res);
	}
	

	/**
	 * 生成新的公式并返回公式对应的sid， 注意执行这个方法需要安装最新的MikLatex
	 */
	@RequestMapping("/common/AfeUtilController/getLatexImgData")
	public synchronized void getLatexImgData(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String parentpath = ClasspathResourceUtil.getWebappFolderFile("/").getParent();
		File exportimgfile = new File(parentpath + "/exportimgs/");
		
		SysFormulaItem by = dao.getBy(sid, SysFormulaItem.class);
		
		Process proc1 = Runtime.getRuntime().exec(SystemOpts.getProperty("miklatex_bin_dir") + "\\latex  -job-name afe -output-directory " + exportimgfile.getAbsolutePath() + 
				" \\documentclass[a4paper]{book}\\pagestyle{empty}\\begin{document}\\begin{large}\\begin{displaymath}" + by.latex + "\\end{displaymath}\\end{large}\\end{document}");
		
		InputStream procin = proc1.getInputStream();
		for(;procin.read() != -1;);
		procin.close();
		proc1.destroy();
		
		proc1 = Runtime.getRuntime().exec(SystemOpts.getProperty("miklatex_bin_dir") + "\\dvipng -o " + exportimgfile.getAbsolutePath() + "/afe.png -T tight " + exportimgfile.getAbsolutePath() + "/afe");
		procin = proc1.getInputStream();
		for(;procin.read() != -1;);
		procin.close();
		proc1.destroy();
		
		byte[] byteArray = FileUtils.readFileToByteArray(new File(exportimgfile.getAbsolutePath() + "/afe.png"));
		printBytes(byteArray, res);
	}
	
}




