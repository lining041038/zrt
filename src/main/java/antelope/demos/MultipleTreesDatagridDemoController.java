package antelope.demos;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.demos.entites.LeftTreeItem;
import antelope.demos.entites.SingleDataGridItem;
import antelope.interfaces.components.MultipleTreesDatagrid;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.MultipleTreesDatagridOptions;
import antelope.interfaces.components.supportclasses.TreeOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONArray;

@Controller("multiple_trees_datagriddemo")
@RequestMapping("/demos/multiple_trees_datagriddemo/MultipleDataGridsDemoController")
public class MultipleTreesDatagridDemoController extends MultipleTreesDatagrid {

	@Override
	public MultipleTreesDatagridOptions getOptions(HttpServletRequest req) {
		MultipleTreesDatagridOptions opts = new MultipleTreesDatagridOptions(this);
		
		TreeOptions tree = new TreeOptions("左树一");
		tree.locatePath = new String[]{GlobalConsts.TREE_ROOT};
		tree.treeRootEditable = false;
		tree.treeNodeEditableFunction = "testTreeEditable";
		opts.treeOptionMap.put("lefttree1", tree);
		
		TreeOptions tree2 = new TreeOptions("左树二");
		tree2.showTreeNodeCreateBtn = false;
		tree2.treeNodeEditableFunction = "testTreeEditable";
		opts.treeOptionMap.put("lefttree2", tree2);
		
		TreeOptions tree3 = new TreeOptions("左树三");
		tree3.showTreeNodeUpdateBtn = false;
		opts.treeOptionMap.put("lefttree3", tree3);
		
		opts.queryfields = new String[]{"name"};
		opts.showBatchDeleteBtn = true;
		opts.columns.put("name", new GridColumn("名称", null));
		
		opts.showTempSaveBtn = true; // 显示暂存按钮
		
		opts.isIsolatedTrees = true; // 树之间是否孤立
		
		opts.selectionMode = "multipleRows";
		
		opts.funcBtns.put("自定义功能按钮", new Button("customFuncBtn"));
		
		return opts;
	}

	@Override
	public void getChildren(String sid, String treekey, String prevtreesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		System.out.println("treekey:" + treekey);
		if (!stringSet(sid) && !stringSet(prevtreesid)) {
			print("[{id:'" + GlobalConsts.TREE_ROOT + "', open:true, sid:'" + GlobalConsts.TREE_ROOT + "', name:\"左树\", isParent:true}]", res);
		} else {
			JSONArray arr = toJSONArray(DBUtil.queryJSON("select * from DEMO_LEFT_TREE where parentsid=?", noNull(sid, prevtreesid)));
			arr.addFieldToAll("isParent", true);
			print(arr, res);
		}
	}

	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + d(name) + "%"}, true);
		sqlwhere.outParams.add(0, req.getParameter("treenode_sid"));
		print(DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where treenode_sid=?" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req)), res);
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
	
	@Override
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.updateBySQL("delete from DEMO_SINGLE_DATAGRID where sid in (" + createQuestionMarksStr(sids.split(",").length) + ")", sids.split(","));
	}
	
	/**
	 * 添加或修改右侧列表项
	 * @param treenode_sid 左侧树节点sid
	 * @param req
	 * @param res
	 */
	@Override
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		if ("1".equals(req.getParameter("tempsave"))) {
			System.out.println("当点击暂存时，执行此段代码，如给表的某字段设置暂存标志位等！");
		}
		
		System.out.println("treenode_sid:" + req.getParameter("treenode_sid"));
		SingleDataGridItem item = dao.getBy(sid, SingleDataGridItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(SingleDataGridItem.class, req);
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	public void addOrUpdateOneTreeNode(String sid, String treekey,  HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("addtreenodetreekey:" + treekey);
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
	public void deleteOneTreeNode(String sid, String treekey, HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("deletetreenodetreekey:" + treekey);
		if (DBUtil.queryCount("select count(*) from DEMO_LEFT_TREE where parentsid=?", sid) > 0) {
			print("此节点还有子节点，不允许删除！", res);
			return;
		}
		dao.deleteBy(sid, LeftTreeItem.class);
	}
}




