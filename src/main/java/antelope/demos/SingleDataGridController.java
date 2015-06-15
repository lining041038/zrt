package antelope.demos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.demos.entites.SingleDataGridItem;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.CreateUpdateWindowMode;
import antelope.interfaces.components.supportclasses.FormField;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.services.supportclasses.ExcelTmpl;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;

/**
 * 单列表增删改全功能
 * @author lining
 * @since 2012-7-14
 */
@Controller("single_datagrid")
@RequestMapping("/demos/single_datagrid/SingleDataGridController")
public class SingleDataGridController extends SingleDatagrid {
	
	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		opts.showCreateBtn = true;
		opts.showExportBtn = true;
		opts.showImportBtn = true;
		opts.showUpdateBtn = true; 
		opts.showBatchDeleteBtn = true; // 显示批量删除按钮
		
		opts.showMoveBtns = true; // 显示移动按钮
		
		opts.showTempSaveBtn = true; // 显示暂存按钮
		
		opts.showBatchMoveBtns = true;
		
		opts.createUpdateWindowMode = CreateUpdateWindowMode.DISPLAY_INLINE;
		
		opts.locatePageBySidAfterCreate = true; // 当创建完成一个新数据项时，定位到此数据项所在页
		
		opts.viewButtonField = "name";
		opts.queryfields = new String[]{"name","name","name","name","name","name","name","name","name"};
		opts.formfields = new FormField[]{
				new FormField("name", "名字", "required='true' maxlength2='10'")};
		opts.columns.put("name", new GridColumn("条件名最大七字", "50%"));
		opts.columns.put("age_", new GridColumn("年龄", "20%"));
		
		return opts;
	}
	
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
		PageItem pageItem = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req, "sortfield"));
		print(pageItem, res);
	}
	
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.deleteBy(sid, SingleDataGridItem.class);
	}
	
	@Override
	public void moveUpOrDown(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, SQLException, Exception {
		dao.moveUpOrDown(getMoveParams(req), SingleDataGridItem.class);
	}
	
	@Override
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.updateBySQL("delete from DEMO_SINGLE_DATAGRID where sid in (" + createQuestionMarksStr(sids.split(",").length) + ")", sids.split(","));
	}
	
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		if ("1".equals(req.getParameter("tempsave"))) {
			System.out.println("当点击暂存时，执行此段代码，如给表的某字段设置暂存标志位等！");
		}
		
		SingleDataGridItem item = dao.getBy(sid, SingleDataGridItem.class);
		boolean update = true;
		if (item == null) {
			item = newInstanceWithCreateInfo(SingleDataGridItem.class, req);
			update = false;
		}
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
		logservice.addOrUpdateOne("测试元数据", update, "数据名称奋数据名称奋数据名称奋数据名称23", req);;
	}
	
	@Override
	public void exportExcel(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
		List<JSONObject> results = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams);
		ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid", "demoexportedfile.xls", res);
		tmpl.wrapData(new String[]{"sid", "name"}, toJSONArray(results));
		tmpl.closeWbook();
	}
	
	@Override
	public void importExcel(InputStream in, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid");
		List<SingleDataGridItem> items = tmpl.getItemsFromExcel(in, SingleDataGridItem.class, req.getAttribute("importfilename").toString());
		for (SingleDataGridItem singleDataGridItem : items)
			singleDataGridItem.sid = SpeedIDUtil.getId();
		dao.batchInsertOrUpdate(items);
	}
	
	@Override
	public void downloadImportExcelTmpl(HttpServletRequest req, HttpServletResponse res) throws BiffException, IOException {
		ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid", "demoimportfile.xls", res);
		tmpl.closeWbook();
	}
}
