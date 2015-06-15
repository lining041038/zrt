package antelope.interfaces.components;


import javax.servlet.http.HttpServletRequest;

import antelope.interfaces.components.supportclasses.TileSelectOptions;
import antelope.utils.PageItem;

/**
 * 通用列表选择组件
 * 左右均显示列表，左边为未选中的，右边为选中的
 * @author lining
 * @since 2012-7-6
 */
public abstract class TileSelect extends BaseUIComponent{
	
	@Override
	public abstract TileSelectOptions getOptions(HttpServletRequest req);
	
	public abstract PageItem getJSONPage(String queryval, HttpServletRequest req) throws Exception;
}
