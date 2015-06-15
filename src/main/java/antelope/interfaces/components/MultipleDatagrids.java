package antelope.interfaces.components;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.supportclasses.MultipleDatagridsOptions;

/**
 * 通用多列表增删改界面
 * @author lining
 * @since 2012-7-14
 */
public abstract class MultipleDatagrids extends BaseUIController{

	@Override
	public abstract MultipleDatagridsOptions getOptions(HttpServletRequest req) throws SQLException, Exception;
	

	@RequestMapping("/getSingleGridList")
	public abstract void getSingleGridList(String gridkey, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	/**
	 * 子类覆盖，用于导出excel数据
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(String gridkey, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Assert.fail("exportExcel 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 此导入方法为内部使用，不允许子类进行覆盖
	 */
	@RequestMapping("/importExcelInner")
	public final void importExcelInner(String gridkey, HttpServletRequest req, HttpServletResponse res) throws BiffException, IllegalArgumentException, IOException, SQLException, IllegalAccessException, InvocationTargetException, Exception {
		List<FileItem> files = parseRequest(req);
		for (FileItem fileItem : files) {
			if (!fileItem.isFormField()) {
				importExcel(gridkey, fileItem.getInputStream(), req, res);
			}
		}
	}
	
	/**
	 * excel前台做excel导入时调用
	 * @param in 导入excel时的excel文件数据流
	 * @param res
	 */
	public void importExcel(String gridkey, InputStream in, HttpServletRequest req, HttpServletResponse res) throws BiffException, IOException, IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException, Exception {
		Assert.fail("importExcel 方法在调用之前必须被子类所覆盖！");
	}
	
	
	@RequestMapping("/deleteOneLine")
	public void deleteOneLine(String gridkey, String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		Assert.fail("deleteOneLine 方法在调用之前必须被子类所覆盖！");
	}
	
	@RequestMapping("/addOrUpdateOne")
	public void addOrUpdateOne(String gridkey, String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Assert.fail("addOrUpdateOne 方法在调用之前必须被子类所覆盖！");
	}
}
