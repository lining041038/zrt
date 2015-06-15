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
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.utils.I18n;
import antelope.utils.RegExpUtil;

/**
 * 通用单列表增删改界面
 * @author lining
 * @since 2012-7-14
 */
public abstract class SingleDatagrid extends BaseUIController{
	@Override
	public abstract SingleDatagridOptions getOptions(HttpServletRequest req) throws SQLException, Exception;

	@RequestMapping("/getSingleGridList")
	public abstract void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	/**
	 * 子类覆盖，用于导出excel数据
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Assert.fail("exportExcel 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 此导入方法为内部使用，不允许子类进行覆盖
	 */
	@RequestMapping("/importExcelInner")
	public final void importExcelInner(HttpServletRequest req, HttpServletResponse res) throws BiffException, IllegalArgumentException, IOException, SQLException, IllegalAccessException, InvocationTargetException, Exception {
		List<FileItem> files = parseRequest(req);
		for (FileItem fileItem : files) {
			String filetype = RegExpUtil.getFirstMatched("(\\.xlsx|\\.xls)$", fileItem.getName());
			if(!(".xlsx".equals(filetype)||".xls".equals(filetype))){
				print(getI18n(req).get("antelope.components.pleaseselectxlsxorxls"),res);
				return ;
			}
		}
		
		for (FileItem fileItem : files) {
			if (!fileItem.isFormField()) {
				req.setAttribute("importfilename", fileItem.getName());
				importExcel(fileItem.getInputStream(), req, res);
			}
		}
	}
	
	/**
	 * 下载导入用样例模板,需子类覆盖
	 */
	@RequestMapping("/downloadImportExcelTmpl")
	public void downloadImportExcelTmpl(HttpServletRequest req, HttpServletResponse res) throws BiffException, IOException {
		Assert.fail("exportExcel 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * excel前台做excel导入时调用
	 * 注意，文件名将作为request属性设置进去，key值为importfilename
	 * @param in 导入excel时的excel文件数据流
	 * @param res
	 */
	public void importExcel(InputStream in, HttpServletRequest req, HttpServletResponse res) throws BiffException, IOException, IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException, Exception {
		Assert.fail("importExcel 方法在调用之前必须被子类所覆盖！");
	}
	
	
	@RequestMapping("/deleteOneLine")
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, SQLException, Exception {
		Assert.fail("deleteOneLine 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 单行数据上移或下移，若上移已经到顶端或下移时已经到底端则无效果
	 */
	@RequestMapping("/moveUpOrDown")
	public void moveUpOrDown(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, SQLException, Exception {
		Assert.fail("moveUpOrDown 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 批量删除按钮点击时调用。
	 * @param sids 逗号分割的批量删除数据sid
	 */
	@RequestMapping("/batchDeleteLines")
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, IOException, SQLException, Exception {
		Assert.fail("batchDeleteLines 方法在调用之前必须被子类所覆盖！");
	}
	
	@RequestMapping("/addOrUpdateOne")
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Assert.fail("addOrUpdateOne 方法在调用之前必须被子类所覆盖！");
	}
	
	protected void addDataGridColumn(String i18nkey, String width, String enumXml, String columnfield, SingleDatagridOptions opts, I18n i18n) {
		addDataGridColumn(i18nkey, width, enumXml, columnfield, null, opts, i18n);
	}
	
	protected void addDataGridColumn(String i18nkey, String width, String enumXml, String columnfield, String textAlign, SingleDatagridOptions opts, I18n i18n) {
		GridColumn column = new GridColumn(i18n.get(i18nkey), width);
		column.enumXml = enumXml;
		column.textAlign = textAlign;
		opts.columns.put(columnfield, column);
	}

	protected void addDataGridBtn(String btnclass, String clickfuncname, String i18nkey, SingleDatagridOptions opts, I18n i18n) {
		Button btn = new Button(clickfuncname);
		btn.toolTip = i18n.get(i18nkey);
		opts.buttons.put(btnclass, btn);
	}
}
