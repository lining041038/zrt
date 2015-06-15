package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import antelope.interfaces.components.BaseUIController;
import antelope.interfaces.components.BaseUIOptions;

public class StatsChartgridOptions extends BaseUIOptions {
	
	
	public LinkedHashMap<String, StatsChartgridTab> tabs = new LinkedHashMap<String, StatsChartgridTab>();
	
	/**
	 * 图表与表格显示的数据数量不一致时，固定显示的前n项，若为null则显示所有。
	 */
	public Integer chartShowPrevNum = null;
	
	public StatsChartgridOptions(BaseUIController controller) {
		super(controller);
	}
	
	@Override
	public void initOptions() {
		super.initOptions();
		for (Entry<String, StatsChartgridTab> entry : tabs.entrySet()) {
			entry.getValue().initTab();
		}
	}
	
	/**
	 * 图表项点击时触发的函数的函数名称，如柱形图的单个柱子，或者线图对应的数据点等
	 */
	public String chartItemClickFunction = "";
	
	/**
	 * 默认定位到的页签key值，此页签初始化时将被设置为选中状态
	 */
	public String selectedTabKey = null;
	
	/**
	 * fusionchart时图表项点击时触发的函数的函数对应传递参数的数据域数组
	 * 请注意：由于fusionchart未支持中文传递，此数据域若添加含有中文的数据域存在缺陷，点击失灵。
	 * 当为flashchart时，点击所在行数据将全部予以传递，不受此参数影响
	 */
	public String[] chartItemClickParamFields = new String[0];
}
