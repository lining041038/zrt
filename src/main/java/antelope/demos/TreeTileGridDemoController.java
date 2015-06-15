package antelope.demos;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.interfaces.components.TreeTilegrid;
import antelope.interfaces.components.supportclasses.TreeTilegridOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONArray;

@Controller("tree_tilegriddemo")
@RequestMapping("/demos/tree_tilegriddemo/TreeTileGridDemoController")
public class TreeTileGridDemoController extends TreeTilegrid {

	@Override
	public TreeTilegridOptions getOptions(HttpServletRequest req) {
		TreeTilegridOptions opts = new TreeTilegridOptions(this);
		opts.treeOptions.locatePath = new String[]{GlobalConsts.TREE_ROOT, "1355638052583"};
		opts.tileRendererFunction = "myrenderer";
		opts.queryformKey = "/demos/selfformquery.jsp";
		return opts;
	}

	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + d(name) + "%"}, true);
		sqlwhere.outParams.add(0, req.getParameter("treenode_sid"));
		print(DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where treenode_sid=?" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req)), res);
	}
	
	/**
	 * 获取根据父节点sid子节点信息
	 * @param sid 父节点sid
	 */
	public void getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if (!stringSet(sid)) {
			print("[{id:'" + GlobalConsts.TREE_ROOT + "', open:true, sid:'" + GlobalConsts.TREE_ROOT + "', name:\"左树\", isParent:true}]", res);
		} else {
			JSONArray arr = toJSONArray(DBUtil.queryJSON("select * from DEMO_LEFT_TREE where parentsid=?", sid));
			arr.addFieldToAll("isParent", true);
			print(arr, res);
		}
	}

}
