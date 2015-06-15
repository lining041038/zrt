package antelope.interfaces.components;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import antelope.interfaces.components.supportclasses.ListSelectOptions;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

/**
 * 通用列表选择组件
 * 左右均显示列表，左边为未选中的，右边为选中的
 * @author lining
 * @since 2012-7-6
 */
public abstract class ListSelect extends BaseUIComponent{
	
	@Override
	public abstract ListSelectOptions getOptions(HttpServletRequest req);
	
	public abstract PageItem getJSONPage(String queryval, HttpServletRequest req) throws Exception;
	
	/**
	 * 获取已选中的列表项 
	 * @param selectedItemSids 逗号分割的选中的sid  如  1303928383,3283432,432228 此列表项sid可能存在空
	 */
	public abstract List<JSONObject> getSelectedItems(String selectedItemSids, String parentsid, HttpServletRequest req) throws Exception;
}
