package antelope.interfaces.components.supportclasses;

import java.util.Map;

/**
 * 可查询接口
 * @author lining
 * @since 2012-11-28
 */
public interface Queryable {
	public String getQueryformKey();
	
	public Map<String, GridColumn> getColumns();
	
	public String[] getQueryfields();
}
