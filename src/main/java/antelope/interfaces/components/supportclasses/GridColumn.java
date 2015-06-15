package antelope.interfaces.components.supportclasses;

/**
 * datagrid列信息
 * @author lining
 * @since 2012-7-14
 */
public class GridColumn{
	public String headerText;
	/**
	 * 列宽度，可以用整数，也可以用百分比 如 50%
	 */
	public String width;
	
	public String enumXmlField;
	
	public boolean sortable = true;
	
	/**
	 * 列枚举xml文件名（不包括.xml后缀）
	 */
	public String enumXml;
	
	
	public String textAlign;
	
	public String labelFunction;
	
	public GridColumn(String headerText) {
		this.headerText = headerText;
	}
	public GridColumn(String headerText, String width) {
		this.headerText = headerText;
		this.width = width;
	}
	public GridColumn(String headerText, String width, boolean sortable) {
		this.headerText = headerText;
		this.width = width;
		this.sortable = sortable;
	}
	public GridColumn(String headerText, String width, String enumXmlField) {
		this.headerText = headerText;
		this.width = width;
		this.enumXmlField = enumXmlField;
	}
	
	@Override
	protected GridColumn clone() throws CloneNotSupportedException {
		GridColumn column = new GridColumn(headerText);
		column.width = this.width;
		column.enumXmlField = this.enumXmlField;
		column.sortable = this.sortable;
		column.enumXml = this.enumXml;
		column.textAlign = this.textAlign;
		column.labelFunction = this.labelFunction;
		return column;
	}
}
