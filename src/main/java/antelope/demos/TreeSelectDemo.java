package antelope.demos;

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


@Component("treeselectdemo")
public class TreeSelectDemo extends TreeSelect {

	@Override
	public TreeSelectionOptions getOptions(HttpServletRequest req) {
		TreeSelectionOptions options = new TreeSelectionOptions();
		options.title = "演示标题";
		options.treefilters.put("demotreefilter", new SelectionTreeFilter() {
			
			@Override
			public String getTitle() {
				return "过滤树";
			}
			
			@Override
			public String getDefaultParamValue() {
				return "defaultparamsid";
			}
			
			@Override
			public List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
				System.out.println(req.getParameter("treeparentsid"));
				sid = TextUtils.noNull(sid, GlobalConsts.TREE_ROOT);
				List<JSONObject> json = DBUtil.queryJSON("select * from DEMO_LEFT_TREE where parentsid=?", sid);
				for (JSONObject jsonObject : json) {
					jsonObject.put("isParent", true);
				}
				return json;
			}
		});
		options.columns = new Pair[]{new Pair("sid","sid"),new Pair("name","区域名")};
		return options;
	}

	@Override
	public List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
	// 树过滤
	//	System.out.println("demotreefilter" + req.getParameter("demotreefilter"));
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
}
