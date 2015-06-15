package antelope.interfaces.components.supportclasses;

/**
 * 单个序列配置
 * @author lining
 * @since 2012-12-20
 */
public class Series {
	
	/**
	 * 图表值数据域
	 */
	public String chartField = null;
	
	/**
	 * 图表中图例显示名称
	 */
	public String displayName = null;
	

	public Series(String chartField, String displayName) {
		this.chartField = chartField;
		this.displayName = displayName;
	}
}
