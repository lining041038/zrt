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
import antelope.interfaces.components.supportclasses.SelectionTreeFilter;
import antelope.interfaces.components.supportclasses.TreeListSelectionOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageUtil;
import antelope.utils.Pair;

import com.opensymphony.xwork2.util.TextUtils;

/**
 * 网页属性选择器
 * @author lining
 * @since 2014-3-19
 */
@Component("pagepropertyselect")
public class PagePropertySelect extends TreeListSelect {

	@Override
	public TreeListSelectionOptions getOptions(HttpServletRequest req) {
		TreeListSelectionOptions options = new TreeListSelectionOptions();
		options.title = "网页属性";
		options.treetitle = "网页属性";
		options.columns = new Pair[]{new Pair("name","名称"), new Pair("typename","类型")};
		return options;
	}

	@Override
	public List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		
//		 文章列表 
//		 产品列表 
//		 下载列表 
//		 友情链接 
//		  文章内容 
//		 产品介绍 
//		 自定义页面 
//		 外部链接 
//		  首页 
//		 公司介绍 
//		 联系我们 
//		 留言 
		
		List<JSONObject> json = new ArrayList<JSONObject>();
		if (!stringSet(sid)) {
			String[] proptypes = new String[]{"articlelist", "文章列表 ", "productlist", "产品列表 ", "peoplemsg",
					"用户留言", "indexpage", "首页 ", "custompage", "自定义页面"};
			for (int i = 0; i < proptypes.length; i += 2) {
				JSONObject obj = new JSONObject();
				obj.put("sid", proptypes[i]);
				obj.put("name", proptypes[i + 1]);
				obj.put("isParent", true);
				json.add(obj);
			}
			
			return json;
		}
		
		// 文章列表
		if (sid.startsWith("articlelist")) {
			if (sid.equals("articlelist"))
				sid = GlobalConsts.TREE_ROOT;
			else
				sid = sid.substring("articlelist".length());
			json = DBUtil.queryJSON("select * from WCM_ARTICLE_CATE where parentsid=?", sid);
			for (JSONObject jsonObject : json) {
				jsonObject.put("isParent", true);
				jsonObject.put("sid", "articlelist" + jsonObject.getString("sid"));
			}
		}
		
		// 产品列表
		if (sid.startsWith("productlist")) {
			if (sid.equals("productlist"))
				sid = GlobalConsts.TREE_ROOT;
			else
				sid = sid.substring("productlist".length());
			json = DBUtil.queryJSON("select * from WCM_PRODUCT_CATE where parentsid=?", sid);
			for (JSONObject jsonObject : json) {
				jsonObject.put("isParent", true);
				jsonObject.put("sid", "productlist" + jsonObject.getString("sid"));
			}
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
		
		if (!stringSet(treenode_sid)) {// 没有树节点id不查询任何数据
			return DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=2", new Object[]{}, getPageParams(req));
		}
		
		// 首页
		if (treenode_sid != null && treenode_sid.startsWith("indexpage")) {
			JSONObject obj = new JSONObject();
			obj.put("sid", "indexpage");
			obj.put("name", "首页");
			obj.put("type", "indexpage");
			obj.put("typename", "首页");
			List<JSONObject> objlist = new ArrayList<JSONObject>();
			objlist.add(obj);
			PageItem pageItem = PageUtil.getPage(getPageParams(req), objlist);
			return pageItem;
		}
		
		// 用户留言
		if (treenode_sid.startsWith("peoplemsg")) {
			JSONObject obj = new JSONObject();
			obj.put("sid", "peoplemsg");
			obj.put("name", "用户留言");
			obj.put("type", "peoplemsg");
			obj.put("typename", "用户留言");
			List<JSONObject> objlist = new ArrayList<JSONObject>();
			objlist.add(obj);
			PageItem pageItem = PageUtil.getPage(getPageParams(req), objlist);
			return pageItem;
		}
		
		// 文章列表
		if (treenode_sid.startsWith("articlelist")) {
			if (treenode_sid.equals("articlelist"))
				treenode_sid = GlobalConsts.TREE_ROOT;
			else
				treenode_sid = treenode_sid.substring("articlelist".length());
		
			return DBUtil.queryJSON("select * from (select sid, name, 'article_cate' type, '文章分类' typename from WCM_ARTICLE_CATE where parentsid=?" +
					"			union all select sid, name, 'article' type, '文章' typename from WCM_ARTICLE where catesid=?) t", new Object[]{treenode_sid, treenode_sid}, getPageParams(req));
		}
		
		
		// 产品列表
		if (treenode_sid.startsWith("productlist")) {
			if (treenode_sid.equals("productlist"))
				treenode_sid = GlobalConsts.TREE_ROOT;
			else
				treenode_sid = treenode_sid.substring("productlist".length());
		
			return DBUtil.queryJSON("select * from (select sid, name, 'product_cate' type, '产品分类' typename from WCM_PRODUCT_CATE where parentsid=?" +
					"			union all select sid, name, 'product' type, '产品' typename from WCM_PRODUCT where catesid=?) t", new Object[]{treenode_sid, treenode_sid}, getPageParams(req));
		}
		
		// 自定义页面
		if (treenode_sid.startsWith("custompage")) {
			return DBUtil.queryJSON("select sid, name, 'custompage' type, '页面' typename from WCM_PAGE where sitesid=?", new Object[]{req.getParameter("sitesid")}, getPageParams(req));
		}
		
		return DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where 1=2", new Object[]{}, getPageParams(req));
	}
}







