package antelope.wcm.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.interfaces.components.TreeListSelect;
import antelope.interfaces.components.supportclasses.TreeListSelectionOptions;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.Pair;

/**
 * 文章选择器
 * @author lining
 * @since 2014-3-19
 */
@Component("articleselect")
public class ArticleSelect extends TreeListSelect {

	@Override
	public TreeListSelectionOptions getOptions(HttpServletRequest req) {
		TreeListSelectionOptions options = new TreeListSelectionOptions();
		options.treetitle = "文章分类";
		options.title = "文章";
		options.columns = new Pair[]{new Pair("name","文章名称")};
		return options;
	}

	@Override
	public List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		List<JSONObject> json = new ArrayList<JSONObject>();
		
		// 文章分类
		if (!stringSet(sid)) {
			sid = GlobalConsts.TREE_ROOT;
		}
		
		json = DBUtil.queryJSON("select * from WCM_ARTICLE_CATE where parentsid=?", sid);
		for (JSONObject jsonObject : json) {
			jsonObject.put("isParent", true);
			jsonObject.put("sid", jsonObject.getString("sid"));
		}
		
		return json;
	}

	@Override
	public List<JSONObject> getSelectedItems(String selectedItemSids,
			String parentsid, HttpServletRequest req) throws Exception {
		return new ArrayList<JSONObject>();
	}

	@Override
	public PageItem getJSONPage(String queryval, String treenode_sid, HttpServletRequest req) throws Exception {
		return DBUtil.queryJSON("select sid, name from WCM_ARTICLE where catesid=?", new Object[]{treenode_sid}, getPageParams(req));
	}
}
