package antelope.interfaces.components;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import antelope.interfaces.components.supportclasses.MultipleTreesListSelectionOptions;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;


public abstract class MultipleTreesListSelect extends ListSelect{

	@Override
	public abstract MultipleTreesListSelectionOptions getOptions(HttpServletRequest req);
	
	
	public abstract List<JSONObject> getChildren(String sid, String treekey, String prevtreesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	@Override
	public final PageItem<JSONObject> getJSONPage(String queryval, HttpServletRequest req) throws Exception {
		throw new Exception("获取左侧树节点对应数据请覆盖getChildren方法！");
	}
	
	/**
	 *  
	 * @param queryval 列表界面上方传入的条件参数
	 * @param treenode_sid 列表左侧树当前激活的节点sid
	 */
	public abstract PageItem getJSONPage(String queryval, String treenode_sid, HttpServletRequest req) throws Exception;

	@Override
	public abstract List<JSONObject> getSelectedItems(String selectedItemSids, String parentsid, HttpServletRequest req) throws Exception;

}
