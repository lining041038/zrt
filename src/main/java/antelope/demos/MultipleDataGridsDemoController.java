package antelope.demos;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.demos.entites.SingleDataGridItem;
import antelope.interfaces.components.MultipleDatagrids;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.MultipleDatagridsOptions;
import antelope.interfaces.components.supportclasses.SingleDatagridOptionsExtended;
import antelope.services.supportclasses.ExcelTmpl;
import antelope.springmvc.SimpleRequest;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;

/**
 * 多列表增删改全功能
 * @author lining
 * @since 2012-7-14
 */
@Controller("multipledatagriddemo")
@RequestMapping("/demos/multiple_datagridsdemo/MultipleDataGridsDemoController")
public class MultipleDataGridsDemoController extends MultipleDatagrids {
	
	@Override
	public MultipleDatagridsOptions getOptions(HttpServletRequest req) {
		MultipleDatagridsOptions opts = new MultipleDatagridsOptions(this);
		
		SingleDatagridOptionsExtended singleopts = new SingleDatagridOptionsExtended(this);
		
		singleopts.showCreateBtn = true;
		singleopts.showExportBtn = true;
		singleopts.showImportBtn = true;
		singleopts.showUpdateBtn = false;
		singleopts.formKeyField = "formkey";
		
		singleopts.showTempSaveBtn = true; // 实现暂存按钮
		
		singleopts.queryfields = new String[]{"name"};
		singleopts.viewButtonField = "name";
		singleopts.columns.put("name", new GridColumn("名字", "50%"));
		singleopts.columns.put("age_", new GridColumn("年龄", "20%"));
		
		singleopts.label = "人员";
		
		singleopts.selectionMode = "multipleRows";
		singleopts.funcBtns.put("自定义功能按钮", new Button("customFuncBtn"));
		
		opts.singleDatagridOptionMap.put("user", singleopts);
		
		singleopts = new SingleDatagridOptionsExtended(this);
		
		singleopts.showCreateBtn = true;
		singleopts.showExportBtn = true;
		singleopts.showImportBtn = true;
		singleopts.showDeleteBtn = false;
		singleopts.queryfields = new String[]{"name"};
		singleopts.columns.put("name", new GridColumn("名字2", "50%"));
		singleopts.columns.put("age_", new GridColumn("年龄2", "20%"));
		
		singleopts.selectionMode = "singleRow";
		singleopts.funcBtns.put("自定义功能按钮2", new Button("customFuncBtn2"));
		
		singleopts.label = "人员2";
		singleopts.buttons.clear();
		
		opts.singleDatagridOptionMap.put("user2", singleopts);
		
		return opts;
	}
	
	public void getSingleGridList(String gridkey, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if ("user".equals(gridkey)) {
			SimpleRequest simplereq = getSimpleRequest(req);
			SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + simplereq.d("name") + "%"});
			PageItem pageItem = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
			print(pageItem, res);
		} else if ("user2".equals(gridkey)) {
			SimpleRequest simplereq = getSimpleRequest(req);
			SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + simplereq.d("name") + "%"});
			PageItem pageItem = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
			print(pageItem, res);
		}
	}
	
	public void deleteOneLine(String gridkey, String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		if ("user".equals(gridkey)) {
			dao.deleteBy(sid, SingleDataGridItem.class);
		} else if ("user2".equals(gridkey)) {
			dao.deleteBy(sid, SingleDataGridItem.class);
		}
	}
	
	public void addOrUpdateOne(String gridkey, String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if ("user".equals(gridkey)) {
			SingleDataGridItem item = dao.getBy(sid, SingleDataGridItem.class);
			if (item == null)
				item = newInstanceWithCreateInfo(SingleDataGridItem.class, req);
			wrapToEntity(req, item);
			dao.insertOrUpdate(item);
		} else if ("user2".equals(gridkey)) {
			SingleDataGridItem item = dao.getBy(sid, SingleDataGridItem.class);
			if (item == null)
				item = newInstanceWithCreateInfo(SingleDataGridItem.class, req);
			wrapToEntity(req, item);
			dao.insertOrUpdate(item);
		}
	}
	
	@Override
	public void exportExcel(String gridkey, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if ("user".equals(gridkey)) {
			SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(req.getParameter("name")) + "%"});
			List<JSONObject> results = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams);
			ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid", "demoexportedfile.xls", res);
			tmpl.wrapData(new String[]{"sid", "name"}, toJSONArray(results));
			tmpl.closeWbook();
		} else if ("user2".equals(gridkey)) {
			SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(req.getParameter("name")) + "%"});
			List<JSONObject> results = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams);
			ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid", "demoexportedfile.xls", res);
			tmpl.wrapData(new String[]{"sid", "name"}, toJSONArray(results));
			tmpl.closeWbook();
		}
	}
	
	@Override
	public void importExcel(String gridkey, InputStream in, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if ("user".equals(gridkey)) {
			ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid");
			List<SingleDataGridItem> items = tmpl.getItemsFromExcel(in, SingleDataGridItem.class);
			for (SingleDataGridItem singleDataGridItem : items)
				singleDataGridItem.sid = SpeedIDUtil.getId();
			dao.batchInsertOrUpdate(items);
		} else if ("user2".equals(gridkey)) {
			ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid");
			List<SingleDataGridItem> items = tmpl.getItemsFromExcel(in, SingleDataGridItem.class);
			for (SingleDataGridItem singleDataGridItem : items)
				singleDataGridItem.sid = SpeedIDUtil.getId();
			dao.batchInsertOrUpdate(items);
		}
	}
}
