package antelope.demos;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.interfaces.components.StatsChartgrid;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.Series;
import antelope.interfaces.components.supportclasses.StatsChartgridOptions;
import antelope.interfaces.components.supportclasses.StatsChartgridTab;


/**
 * 统计图表通用全界面组件demo
 * @author lining
 * @since 2012-9-11
 */
@Controller("statschartgriddemo")
@RequestMapping("/demos/statschartgriddemo/demos/StatsChartgridDemoController")
public class StatsChartgridDemoController extends StatsChartgrid {
	
	@Override
	public StatsChartgridOptions getOptions(HttpServletRequest req) {
		StatsChartgridOptions opts = new StatsChartgridOptions(this);
		
		opts.chartItemClickFunction = "myStatsItemClick";
		
		
		// 从前台获取的参数
		System.out.println("getOptions时获取参数：" + d(req.getParameter("ct")));
		
		StatsChartgridTab tab = null;
		
		tab = new StatsChartgridTab("系统按角色统计人数", "rolename", "ct");
		tab.displayName = "数量一";
		
		tab.gridDrillField = "rolename";
		
		tab.exportedExcelName = "测试自定义excel名称";
		
		tab.columnChartType = "clustered";
		tab.series = new Series[]{new Series("ct", "人数"), new Series("genderct", "男性人数")};
		tab.columns.put("rolename", new GridColumn("角色名称"));
		tab.columns.put("ct", new GridColumn("人数"));
		tab.columns.put("genderct", new GridColumn("男性人数"));
		opts.tabs.put("users", tab);
		opts.chartShowPrevNum = 6;
		
		tab = new StatsChartgridTab("系统按组织机构统计人数", "rolename", "ct");
		tab.formKey = "/demos/statsform.jsp";
		tab.displayName = "人数";
		tab.columns.put("rolename", new GridColumn("单位名称"));
		tab.columns.put("ct", new GridColumn("数量"));
		opts.tabs.put("users2", tab);
		
		opts.selectedTabKey = "users2";
		
		tab = new StatsChartgridTab("barChart", "filename", "ct");
		tab.formKey = "/demos/statsform.jsp";
		tab.displayName = "数量";
		tab.chartType = "barChart";
		tab.columns.put("filename", new GridColumn("动漫名称"));
		tab.columns.put("ct", new GridColumn("数量"));
		opts.tabs.put("user3", tab);
		
		return  opts;
	}

	@Override
	public void getData(String key, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		// 从前台获取的参数
		System.out.println("getData时获取参数：" + d(req.getParameter("ct")));
		
		if ("users".equals(key)) {
			print(DBUtil.queryJSON(getSql("statsuserrole").toString()), res);
		} else if ("users2".equals(key)) {
			print(DBUtil.queryJSON(getSql("statsuserorg").toString() + " and t.name like ? ", "%" + req.getParameter("test") + "%"), res);
		} else if ("user3".equals(key)) {
			print(DBUtil.queryJSON(getSql("statsfilm").toString()), res);
		}
	}
}
