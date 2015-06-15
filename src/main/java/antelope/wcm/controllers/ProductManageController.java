package antelope.wcm.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.interfaces.components.TreeDatagrid;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.TreeDatagridOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONArray;
import antelope.utils.PageItem;
import antelope.wcm.entities.WCMProductCateItem;
import antelope.wcm.entities.WCMProductItem;
import antelope.wcm.services.ArticleQueryService;
import antelope.wcm.services.ProductQueryService;

/**
 * 产品管理界面
 * @author lining
 * @since 2014-3-10
 */
@Controller("productmanage")
@RequestMapping("/wcm/productmanage/ProductManageController")
public class ProductManageController extends TreeDatagrid {
	
	
	@Resource
	private ProductQueryService productservice;
	
	@Override
	public TreeDatagridOptions getOptions(HttpServletRequest req) {
		TreeDatagridOptions opts = new TreeDatagridOptions(this);
		opts.columns.put("name", new GridColumn("产品名称", "30%"));
		opts.columns.put("catename", new GridColumn("分类", "25%"));
		opts.columns.put("productprice", new GridColumn("价格", "15%"));
		opts.queryfields = new String[]{"name"};
		opts.treeRootEditable = false;
		return opts;
	}
	
	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"t.name like ?"}, new Object[]{"%" + d(name) + "%"}, true);
		String treenode_sid = req.getParameter("treenode_sid");
		
		PageItem json = productservice.getProductsPageDataByCatesid(treenode_sid, getPageParams(req), sqlwhere);
		print(json, res);
	}
	
	/**
	 * 获取根据父节点sid子节点信息
	 * @param sid 父节点sid
	 */
	public void getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if (!stringSet(sid)) {
			print("[{id:'" + GlobalConsts.TREE_ROOT + "', open:true, sid:'" + GlobalConsts.TREE_ROOT + "', name:\"所有分类\", isParent:true}]", res);
		} else {
			JSONArray arr = toJSONArray(DBUtil.queryJSON("select * from WCM_PRODUCT_CATE where parentsid=?", sid));
			arr.addFieldToAll("isParent", true);
			print(arr, res);
		}
	}
	
	@Override
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.updateBySQL("delete from WCM_PRODUCT where sid in (" + createQuestionMarksStr(sids.split(",").length) + ")", sids.split(","));
	}
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	public void addOrUpdateOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMProductCateItem item = dao.getBy(sid, WCMProductCateItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(WCMProductCateItem.class, req);
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	public void deleteOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		if (DBUtil.queryCount("select count(*) from WCM_PRODUCT_CATE where parentsid=?", sid) > 0) {
			print("此分类还有子分类，不允许删除！", res);
			return;
		}
		
		if (DBUtil.queryCount("select count(*) from WCM_PRODUCT where catesid=?", sid) > 0) {
			print("此分类还有产品，不允许删除！", res);
			return;
		}
		dao.deleteBy(sid, WCMProductCateItem.class);
	}
	
	/**
	 * 删除列表项 
	 * @param sid
	 */
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		String[] sids = req.getParameterValues("sid");
		if (sids != null)
			dao.updateBySQL("delete from WCM_PRODUCT where sid in(" + createQuestionMarksStr(sids.length) + ")", sids);
	}
	
	/**
	 * 添加或修改
	 * @param treenode_sid 左侧树节点sid
	 * @param req
	 * @param res
	 */
	@Override
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMProductItem item = dao.getBy(sid, WCMProductItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(WCMProductItem.class, req);
		item.catesid = req.getParameter("treenode_sid");
		wrapToEntity(req, item);
		fileupload.setPermanent(getFileUploadParams(req));
		dao.insertOrUpdate(item);
	}
	
}





