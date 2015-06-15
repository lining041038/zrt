package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;

import com.opensymphony.xwork2.util.TextUtils;

/**
 * 统计图表组件中，单页签相关配置
 * @author lining
 * @since 2012-9-11
 */
public class StatsChartgridTab {

	/**
	 * 页签显示标题
	 */
	public String label;
	
	/**
	 * 表单键 允许为空，此表单为统计图表全界面组件提供查询条件以及其他额外扩展支持
	 */
	public String formKey = null;
	
	/*图表配置 begin->*/
	/**
	 * 图表类型( columnChart lineChart pieChart)
	 */
	public String chartType = "columnChart";
	
	/**
	 * 当为柱形图时，可以调整此参数 100% stacked clustered
	 */
	public String columnChartType = "stacked";
	
	
	/**
	 * 导出的excel名称，不包含.xls后缀
	 */
	public String exportedExcelName = "statsexcel";
	
	/**
	 * 图表label数据域
	 */
	public String chartLabelField = null;
	
	/**
	 * 图表值数据域（单序列时）
	 */
	public String chartField = null;
	
	/**
	 * 图表中图例显示名称（单序列时）
	 */
	public String displayName = null;
	
	/**
	 * 若图表中含有多个序列，则使用此选项，此选项优先级高于选项中用于单序列的选项
	 */
	public Series[] series = null;
	
	/**
	 * 图表标题，默认使用页签显示标题
	 */
	public String chartTitle = null;
	
	/*<-图表配置  end */
	
	/**
	 * chart品牌，包括：flashchart(flex自带的基本chart), fusionchart:(fusionchart公司产品) 
	 * 默认为flexChart
	 */
	public String chartBrand = "flashchart";
	
	/**
	 * 用于钻取的数据域名称，它会修改对应数据列的labelFunction,优先级高于调用者
	 */
	public String gridDrillField = null;
	
	void initTab() {
		if (TextUtils.stringSet(gridDrillField)) {
			GridColumn column = this.columns.get(gridDrillField);
			if (column != null) {
				column.labelFunction = "changeToDrillLinkFunction";
			}
		}
	}
	
	public LinkedHashMap<String, GridColumn> columns = new LinkedHashMap<String, GridColumn>();
	
	
	public Integer numPerPage = 10;
	
	public StatsChartgridTab(String label, String chartLabelField, String chartField) {
		this.label = label;
		this.chartTitle = label;
		this.chartLabelField = chartLabelField;
		this.chartField = chartField;
	}
}
