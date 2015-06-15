package antelope.interfaces.components;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.supportclasses.TreeDatagridOptions;

/**
 * 通用左树结构，右列表加搜索组件
 * @author lining
 * @since 2012-7-25
 */
public abstract class TreeDatagrid extends SingleDatagrid {
	
	@Override
	public abstract TreeDatagridOptions getOptions(HttpServletRequest req) throws SQLException, Exception;
	
	
	/**
	 * 获取根据父节点sid子节点信息，将子节点列表数据输出到前台
	 * @param sid 父节点sid
	 */
	@RequestMapping("/getChildren")
	public abstract void getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	/**
	 * 点击左侧树节点获取右侧列表项
	 * 注意此方法在调用时，除了会携带查询参数区域的参数之外，会额外携带一个treenode_sid参数，可以使用它来完成左侧树的节电sid的获取工作
	 * @param req
	 * @param res
	 */
	@RequestMapping("/getSingleGridList")
	public abstract void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	@RequestMapping("/addOrUpdateOneTreeNode")
	public void addOrUpdateOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Assert.fail("addOrUpdateOneTreeNode 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 树节点移动
	 * @param sid
	 */
	@RequestMapping("/moveUpOrDownTreeNode")
	public void moveUpOrDownTreeNode(String sid, String parentsid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Assert.fail("moveUpOrDownTreeNode 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 树节点删除方法
	 * @param sid 父节点sid
	 */
	@RequestMapping("/deleteOneTreeNode")
	public void deleteOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res)  throws Exception {
		Assert.fail("deleteOneTreeNode 方法在调用之前必须被子类所覆盖！");
	}
}
