package antelope.demos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
import antelope.utils.JSONObject;


/**
 * 统计图表通用全界面组件demo
 * @author lining
 * @since 2012-9-11
 */
@Controller("statsfusionchartdemo")
@RequestMapping("/demos/statsfusionchartdemo/StatsFusionChartDemoController")
public class StatsFusionChartDemoController extends StatsChartgrid {
	
	@Override
	public StatsChartgridOptions getOptions(HttpServletRequest req) {
		StatsChartgridOptions opts = new StatsChartgridOptions(this);
		
		opts.chartItemClickFunction = "myStatsItemClick";
		opts.chartItemClickParamFields = new String[]{"ct"};
		
		StatsChartgridTab tab = null;
		
		tab = new StatsChartgridTab("系统按角色统计人数", "rolename", "ct");
		tab.displayName = "数量一";
		tab.numPerPage = 12;
		tab.chartBrand = "fusionchart";
		
		tab.gridDrillField = "rolename"; // 用来做钻取的数据域
		
		tab.exportedExcelName = "fusionchart中测试自定义excelexportname";
		
		tab.chartType = "columnChart";
		tab.series = new Series[]{new Series("ct", "人数"), new Series("genderct", "男性人数")};
		tab.columns.put("rolename", new GridColumn("角色名称"));
		tab.columns.put("ct", new GridColumn("人数"));
		tab.columns.put("genderct", new GridColumn("男性人数"));
		opts.tabs.put("users", tab);
		opts.chartShowPrevNum = 2;
		
		tab = new StatsChartgridTab("系统按组织机构统计人数", "rolename", "ct");
		tab.formKey = "/demos/statsform.jsp";
		tab.displayName = "人数";
		tab.chartBrand = "fusionchart";
		tab.chartType = "columnChart";
		tab.numPerPage = 15;
		tab.columnChartType = "single";
		tab.columns.put("rolename", new GridColumn("单位名称"));
		tab.columns.put("ct", new GridColumn("数量"));
		opts.tabs.put("users2", tab);
		
		opts.selectedTabKey = "users3";
		
		tab = new StatsChartgridTab("系统性别统计人数", "gender", "ct");
		tab.formKey = "/demos/statsform.jsp";
		tab.displayName = "人数";
		tab.chartBrand = "fusionchart";
		tab.chartType = "pieChart";
		tab.columnChartType = "single";
		
		GridColumn gridColumn = new GridColumn("性别");
		gridColumn.enumXml = "sys_usergender";
		tab.columns.put("gender", gridColumn);
		tab.columns.put("ct", new GridColumn("数量"));
		opts.tabs.put("users3", tab);
		
		tab = new StatsChartgridTab("barChart", "gender", "ct");
		tab.formKey = "/demos/statsform.jsp";
		tab.displayName = "人数";
		tab.chartBrand = "fusionchart";
		tab.chartType = "barChart";
		
		gridColumn = new GridColumn("性别");
		gridColumn.enumXml = "sys_usergender";
		tab.columns.put("gender", gridColumn);
		tab.columns.put("ct", new GridColumn("数量"));
		opts.tabs.put("users5", tab);
		
		return  opts;
	}

	@Override
	public void getData(String key, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		if ("users".equals(key)) {
			print(DBUtil.queryJSON(getSql("statsuserrole").toString()), res);
		} else if ("users2".equals(key)) {
			List<JSONObject> queryJSON = DBUtil.queryJSON(getSql("statsuserorg").toString() + " and t.name like ?", "%" + req.getParameter("test") + "%");
			for (JSONObject jsonObject : queryJSON) {
				jsonObject.put("ct", Math.floor(Math.random() * 100));
			}
			print(queryJSON, res);
		} else if ("users3".equals(key)) {
			print(DBUtil.queryJSON(getSql("statsusergender").toString()), res);
		} else if ("users5".equals(key)) {
			print(DBUtil.queryJSON(getSql("statsusergender").toString()), res);
		}
	}
}
