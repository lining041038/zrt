package antelope.interfaces.components;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import antelope.interfaces.components.supportclasses.TreeListSelectionOptions;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

/**
 * 左树中列表待选，右列表选中组件
 * @author lining
 * @since 2013-3-7
 */
public abstract class TreeListSelect extends TreeSelect {

	@Override
	public abstract TreeListSelectionOptions getOptions(HttpServletRequest req);
	
	@Override
	public abstract List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	/**
	 *  
	 * @param queryval 列表界面上方传入的条件参数
	 * @param treenode_sid 列表左侧树当前激活的节点sid
	 */
	public abstract PageItem getJSONPage(String queryval, String treenode_sid, HttpServletRequest req) throws Exception;

	@Override
	public abstract List<JSONObject> getSelectedItems(String selectedItemSids, String parentsid, HttpServletRequest req) throws Exception;

}
