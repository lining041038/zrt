package antelope.demos;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.demos.entites.SingleDataGridItem;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.CreateUpdateWindowMode;
import antelope.interfaces.components.supportclasses.FormField;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.PageItem;

/**
 * 单列表增删改全功能
 * @author lining
 * @since 2012-7-14
 */
@Controller("customsingledatagriddemo")
@RequestMapping("/demos/selfform_datagrid/CustomFormSingleGridController")
public class CustomFormSingleGridController extends SingleDatagrid {
	
	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		opts.showCreateBtn = true;
		//opts.queryformKey = "/demos/selfformquery.jsp"; // 此处允许设置自定义查询区域jsp
		opts.queryfields = new String[]{"name","gender:checkbox","age_:sys_userage"};
		opts.formKey = "/demos/demosupports/singlegridform.jsp";
		opts.columns.put("name", new GridColumn("名字", "20%"));
		opts.columns.put("age_", new GridColumn("年龄", "20%"));
		GridColumn gender = new GridColumn("性别", "20%");
		gender.enumXml = "sys_usergender";
		opts.columns.put("gender", gender);
		
		opts.showTempSaveBtn = true;
		
		//添加自定义功能按钮区按钮
		opts.selectionMode = "multipleRows";
		opts.funcBtns.put("批量附加1", new Button("batchadd1"));
		
		opts.createUpdateWindowMode = CreateUpdateWindowMode.DISPLAY_INLINE;// 使用顶级窗口弹出模式弹出创建修改对话框
		
		opts.windowModeParams.dialogHeight = 400;
		opts.windowModeParams.dialogWidth = 1000;
		
		return opts;
	}
	
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		String gender = req.getParameter("gender");
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?","gender = ?",}, new Object[]{"%" + decodeAndTrim(name) + "%", gender});
		PageItem pageItem = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		print(pageItem, res);
	}
	
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.deleteBy(sid, SingleDataGridItem.class);
	}
	
	@RequestMapping("/batchAdd1")
	public void batchAdd1(String sids, HttpServletRequest req, HttpServletResponse res) {
		dao.updateBySQL("update DEMO_SINGLE_DATAGRID set age_=1 where age_ is null");
		dao.updateBySQL("update DEMO_SINGLE_DATAGRID set age_= age_ + 1 where sid in ('" + join("','",sids.split(",")) + "')");
	}
	
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		SingleDataGridItem item = dao.getBy(sid, SingleDataGridItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(SingleDataGridItem.class, req);
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
		
		// 一下代码为solr搜索引擎接口代码，若未开启solr项目，则一下代码会报错不正常运行，但不影响前面数据库操作相关代码的运行。
		try {
			String serverUrl = "http://localhost:" + req.getLocalPort() + "/solr/collection1";
			SolrServer solr = new HttpSolrServer(serverUrl);
			SolrPingResponse pingres = solr.ping();
			SolrInputDocument doc1 = new SolrInputDocument();
			
			doc1.setField("id", "1");
			doc1.setField("name", item.name);
			doc1.setField("screen_name_s", item.name);
			doc1.setField("type_s", "post");
			doc1.setField("lang_s", "en");
			doc1.setField("timestamp_tdt", "2012-05-22T09:30:22Z/HOUR");
			doc1.setField("features", item.name);
			doc1.setField("text_t",item.clobtest);
			solr.add(doc1);
			solr.commit(true,  true);
		} catch (Exception e) {
//			System.out.println("");
		}
	}
}
