package antelope.demos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.interfaces.components.TreeListSelect;
import antelope.interfaces.components.supportclasses.SelectionTreeFilter;
import antelope.interfaces.components.supportclasses.TreeListSelectionOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.Pair;

import com.opensymphony.xwork2.util.TextUtils;


@Component("treelistselectdemo")
public class TreeListSelectDemo extends TreeListSelect {

	@Override
	public TreeListSelectionOptions getOptions(HttpServletRequest req) {
		TreeListSelectionOptions options = new TreeListSelectionOptions();
		options.title = "演示标题";
		options.treetitle = "演示树标题";
		options.columns = new Pair[]{new Pair("sid","sid"),new Pair("name","区域名")};
		
//		options.treefilters.put("demofiltertree", new SelectionTreeFilter(){
//			@Override
//			public List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
//				sid = TextUtils.noNull(sid, GlobalConsts.TREE_ROOT);
//				List<JSONObject> json = DBUtil.queryJSON("select * from DEMO_LEFT_TREE where parentsid=?", sid);
//				for (JSONObject jsonObject : json) {
//					jsonObject.put("isParent", true);
//				}
//				return json;
//			}
//
//			@Override
//			public String getTitle() {
//				return "树过滤";
//			}
//			
//			@Override
//			public String getDefaultParamValue() {
//				return "1355638052657";
//			}
//		});
		
		return options;
	}

	@Override
	public List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		System.out.println(req.getParameter("demofiltertree"));
		
		sid = TextUtils.noNull(sid, GlobalConsts.TREE_ROOT);
		List<JSONObject> json = DBUtil.queryJSON("select * from DEMO_LEFT_TREE where parentsid=?", sid);
		for (JSONObject jsonObject : json) {
			jsonObject.put("isParent", true);
		}
		return json;
	}

	@Override
	public List<JSONObject> getSelectedItems(String selectedItemSids,
			String parentsid, HttpServletRequest req) throws Exception {
		return new ArrayList<JSONObject>();
	}

	@Override
	public PageItem getJSONPage(String queryval, String treenode_sid,
			HttpServletRequest req) throws Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + queryval + "%"}, true);
		sqlwhere.outParams.add(0, req.getParameter("treenode_sid"));
		return DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where treenode_sid=?" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
	}
}
