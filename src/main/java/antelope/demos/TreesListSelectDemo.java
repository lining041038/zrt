package antelope.demos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.interfaces.components.MultipleTreesListSelect;
import antelope.interfaces.components.supportclasses.MultipleTreesListSelectionOptions;
import antelope.interfaces.components.supportclasses.Tree4MtlsOption;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.Pair;
import antelope.utils.TextUtils;

@Component("treeslistselectdemo")
public class TreesListSelectDemo extends MultipleTreesListSelect {

	@Override
	public MultipleTreesListSelectionOptions getOptions(HttpServletRequest req) {
		MultipleTreesListSelectionOptions options = new MultipleTreesListSelectionOptions();
		options.title = "演示标题";
		options.firstTreeName = "第一个树根节点";
		options.columns = new Pair[]{new Pair("sid","sid"),new Pair("name","区域名")};
		
		Tree4MtlsOption tree4opts = new Tree4MtlsOption();
		tree4opts.label = "左树一";
		options.treeOptionMap.put("lefttree1", tree4opts);
		
		Tree4MtlsOption tree4opts2 = new Tree4MtlsOption();
		tree4opts2.label = "左树二";
		options.treeOptionMap.put("lefttree2", tree4opts2);
		
		Tree4MtlsOption tree4opts3 = new Tree4MtlsOption();
		tree4opts3.label = "左树三";
		options.treeOptionMap.put("lefttree3", tree4opts3);
		
		return options;
	}

	@Override
	public List<JSONObject> getChildren(String sid, String treekey, String prevtreesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		System.out.println("treekey:" + treekey);
		List<JSONObject> json = DBUtil.queryJSON("select * from DEMO_LEFT_TREE where parentsid=?", TextUtils.noNull(sid, prevtreesid, GlobalConsts.TREE_ROOT));
		for (JSONObject jsonObject : json) {
			jsonObject.put("isParent", true);
			jsonObject.put("open", false);
		}
		return json;
	}

	@Override
	public PageItem getJSONPage(String queryval, String treenode_sid, HttpServletRequest req) throws Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + d(queryval) + "%"}, true);
		sqlwhere.outParams.add(0, treenode_sid);
		PageItem pageItem = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where treenode_sid=?" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		return pageItem;
	}

	@Override
	public List<JSONObject> getSelectedItems(String selectedItemSids, String parentsid, HttpServletRequest req) throws Exception {
		return DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where sid in ('" + join("','", selectedItemSids.split(",")) + "')");
	}

}
