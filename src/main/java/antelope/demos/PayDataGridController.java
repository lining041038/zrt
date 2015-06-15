package antelope.demos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.demos.entites.MyOrderItem;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.services.supportclasses.ExcelTmpl;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;

/**
 * 在线支付接口demo
 * @author lining
 * @since 2014-05-04
 */
@Controller("pay_datagrid")
@RequestMapping("/demos/pay_datagrid/PayDataGridController")
public class PayDataGridController extends SingleDatagrid {
	
	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		opts.showCreateBtn = true;
		
		opts.showDeleteBtn = false;
		opts.showUpdateBtn = false;
		
		opts.viewButtonField = "name";
		opts.queryfields = new String[]{"name"};
		opts.columns.put("name", new GridColumn("订单名称", "50%"));
		opts.columns.put("orderprice", new GridColumn("总价", "20%"));
		
		opts.buttons.put("去支付", new Button("gotoPay"));
		
		
		GridColumn paystate = new GridColumn("支付状态", "20%");
		paystate.enumXml = "sys_demo_paycomplete";
		opts.columns.put("paycomplete", paystate);
		return opts;
	}
	
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
		PageItem pageItem = DBUtil.queryJSON("select * from DEMO_MY_ORDER where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		print(pageItem, res);
	}
	
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		MyOrderItem item = dao.getBy(sid, MyOrderItem.class);
		if (item == null) {
			item = newInstanceWithCreateInfo(MyOrderItem.class, req);
			item.paycomplete = "0";
		}
		
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
	
	@Override
	public void exportExcel(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
		List<JSONObject> results = DBUtil.queryJSON("select * from DEMO_MY_ORDER where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams);
		ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid", "demoexportedfile.xls", res);
		tmpl.wrapData(new String[]{"sid", "name"}, toJSONArray(results));
		tmpl.closeWbook();
	}
	
	@Override
	public void importExcel(InputStream in, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid");
		List<MyOrderItem> items = tmpl.getItemsFromExcel(in, MyOrderItem.class, req.getAttribute("importfilename").toString());
		for (MyOrderItem MyOrderItem : items)
			MyOrderItem.sid = SpeedIDUtil.getId();
		dao.batchInsertOrUpdate(items);
	}
	
	@Override
	public void downloadImportExcelTmpl(HttpServletRequest req, HttpServletResponse res) throws BiffException, IOException {
		ExcelTmpl tmpl = excel.getTmplInstance("demosinglegrid", "demoimportfile.xls", res);
		tmpl.closeWbook();
	}
}
