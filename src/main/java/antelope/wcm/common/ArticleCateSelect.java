package antelope.wcm.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.util.TextUtils;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.interfaces.components.TreeSelect;
import antelope.interfaces.components.supportclasses.SelectionTreeFilter;
import antelope.interfaces.components.supportclasses.TreeSelectionOptions;
import antelope.utils.JSONObject;
import antelope.utils.Pair;


/**
 * 选择文章分类
 * @author lining
 * @since 2014-3-17
 */
@Component("articlecateselect")
public class ArticleCateSelect extends TreeSelect {

	@Override
	public TreeSelectionOptions getOptions(HttpServletRequest req) {
		TreeSelectionOptions options = new TreeSelectionOptions();
		options.title = "文章分类";
		options.columns = new Pair[]{new Pair("name","分类名称")};
		return options;
	}

	@Override
	public List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		sid = TextUtils.noNull(sid, GlobalConsts.TREE_ROOT);
		List<JSONObject> json = DBUtil.queryJSON("select * from WCM_ARTICLE_CATE where parentsid=?", sid);
		for (JSONObject jsonObject : json) {
			jsonObject.put("isParent", true);
		}
		return json;
	}

	@Override
	public List<JSONObject> getSelectedItems(String selectedItemSids, String parentsid, HttpServletRequest req) throws Exception {
		List<JSONObject> json = DBUtil.queryJSON("select * from WCM_ARTICLE_CATE where sid in ('" + join("','", selectedItemSids.split(",")) + "')");
		return json;
	}
}
