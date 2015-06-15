package antelope.demos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.demos.entites.LeftTreeItem;
import antelope.demos.entites.SingleDataGridItem;
import antelope.interfaces.components.TreeDatagrid;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.CreateUpdateWindowMode;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.MenuItem;
import antelope.interfaces.components.supportclasses.TreeDatagridOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONArray;

/**
 * 左树右列表全界面组件演示类
 * @author lining
 * @since 2012-7-25
 */
@Controller("treedatagriddemo")
@RequestMapping("/demos/tree_datagrid/TreeDatagridController")
public class TreeDatagridController extends TreeDatagrid {
	
	@Override
	public TreeDatagridOptions getOptions(HttpServletRequest req) {
		//I18n i18n = getI18n(req);
		TreeDatagridOptions opts = new TreeDatagridOptions(this);
		opts.viewButtonField = "name";
		
		opts.showTempSaveBtn = true; //显示暂存按钮
		
		opts.showMoveBtns = true;

		opts.expandTreeNodeByClick = true;
		
		opts.showTreeNodeMoveBtns = true; // 显示树结构中节点移动按钮
		
		opts.reInitDatagridByTreeNodeSid = true; // 当点击左侧节点时，重新按照新的规则初始化列表的列
		opts.reLoadQueryFormWhenDatagridReInited = true;
		
		// opts.showBatchDeleteBtn = true; // 显示批量删除按钮
		
		
		opts.treeButtons.put("ui-icon-refresh", new Button("refreshIt", "刷新"));
		
		System.out.println("optstreenode_sid:" + req.getParameter("treenode_sid"));
		long ct = Math.round(Math.random() * 5);
		opts.columns.put("name", new GridColumn("名称", null));
		List<String> qfields = new ArrayList<String>();
		for (int i = 0; i < ct; ++i) {
			qfields.add("name");
			opts.columns.put("name" + i, new GridColumn("名称" + i, null));
		}
		
		opts.buttons.get("update").visibleFunction = "myVisibleFunction";
		
		if (ct % 2 == 0) {
			opts.buttons.clear();
		}
		
		opts.queryfields = qfields.toArray(new String[0]);
		opts.treeRootEditable = false;
		opts.treeNodeEditableFunction = "testTreeEditable";
		
		opts.selectionMode = "singleRow"; // 显示单选框
		opts.funcBtns.put("自定义功能按钮", new Button("customFuncBtn")); // 显示自定义功能按钮
		
		// 树结构上下文菜单
		MenuItem mItem = new MenuItem("onTreeCtxMenu");
		opts.treeContextMenu.put("演示上下文菜单", mItem);
		return opts;
	}
	
	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + d(name) + "%"}, true);
		sqlwhere.outParams.add(0, req.getParameter("treenode_sid"));
		print(DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where treenode_sid=?" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req, "sortfield")), res);
	}
	
	/**
	 * 获取根据父节点sid子节点信息
	 * @param sid 父节点sid
	 */
	public void getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if (!stringSet(sid)) {
			print("[{id:'" + GlobalConsts.TREE_ROOT + "', icon: '" + req.getContextPath() + "/themes/defaults/assets/sim/2.png', open:true, sid:'" + GlobalConsts.TREE_ROOT + "', name:\"左树\", isParent:true}]", res);
		} else {
			JSONArray arr = toJSONArray(DBUtil.queryJSON("select * from DEMO_LEFT_TREE where parentsid=? order by sortfield", sid));
			arr.addFieldToAll("isParent", true);
			arr.addFieldToAll("icon", req.getContextPath() + "/themes/defaults/assets/sim/3.png");
			print(arr, res);
		}
	}
	
	@Override
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.updateBySQL("delete from DEMO_SINGLE_DATAGRID where sid in (" + createQuestionMarksStr(sids.split(",").length) + ")", sids.split(","));
	}
	
	@Override
	public void moveUpOrDown(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, SQLException, Exception {
		dao.moveUpOrDown("select sid, sortfield from DEMO_SINGLE_DATAGRID where treenode_sid in (select treenode_sid DEMO_SINGLE_DATAGRID where sid=?)", new Object[]{sid}, getMoveParams(req), SingleDataGridItem.class);
	}
	
	@Override
	public void moveUpOrDownTreeNode(String sid, String parentsid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		dao.moveUpOrDown("select sid, sortfield from DEMO_LEFT_TREE where parentsid=? ", new Object[]{parentsid}, getMoveParams(req), LeftTreeItem.class, true);
	}
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	public void addOrUpdateOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		TreeDatagridOptions opts = getOptions(req);
		if ("1".equals(req.getParameter("tempsave"))) {
			System.out.println("当点击暂存时，执行此段代码，如给表的某字段设置暂存标志位等！");
		}
		
		LeftTreeItem item = dao.getBy(sid, LeftTreeItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(LeftTreeItem.class, req);
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
		// 可在此方法中验证是否允许添加，验证未通过则使用如下方式提示
		// print("添加失败！", res);
	}
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	public void deleteOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (DBUtil.queryCount("select count(*) from DEMO_LEFT_TREE where parentsid=?", sid) > 0) {
			print("此节点还有子节点，不允许删除！", res);
			return;
		}
		dao.deleteBy(sid, LeftTreeItem.class);
	}
	
	/**
	 * 删除列表项 
	 * @param sid
	 */
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		String[] sids = req.getParameterValues("sid");
		if (sids != null)
			dao.updateBySQL("delete from DEMO_SINGLE_DATAGRID where sid in(" + createQuestionMarksStr(sids.length) + ")", sids);
	}
	
	/**
	 * 添加或修改右侧列表项
	 * @param treenode_sid 左侧树节点sid
	 * @param req
	 * @param res
	 */
	@Override
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		SingleDataGridItem item = dao.getBy(sid, SingleDataGridItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(SingleDataGridItem.class, req);
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
}
