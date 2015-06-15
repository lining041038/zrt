package antelope.interfaces.components;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.supportclasses.MultipleTreesDatagridOptions;

/**
 * 通用左边多树结构，右列表加搜索组件
 * @author lining
 * @since 2013-2-28
 */
public abstract class MultipleTreesDatagrid extends SingleDatagrid {
	
	@Override
	public abstract MultipleTreesDatagridOptions getOptions(HttpServletRequest req);
	
	/**
	 * 获取根据父节点sid子节点信息，将子节点列表数据输出到前台
	 * @param sid 父节点sid
	 * @param treekey treeOptionMap的key值，标明希望获取的是哪颗树的子节点数据
	 */
	@RequestMapping("/getChildren")
	public abstract void getChildren(String sid, String treekey, String prevtreesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
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
	 * @param treekey treeOptionMap的key值，标明希望添加或更新的是哪颗树的子节点数据
	 */
	@RequestMapping("/addOrUpdateOneTreeNode")
	public void addOrUpdateOneTreeNode(String sid, String treekey, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Assert.fail("addOrUpdateOneTreeNode 方法在调用之前必须被子类所覆盖！");
	}
	
	/**
	 * 树节点删除方法
	 * @param sid 父节点sid
	 * @param treekey treeOptionMap的key值，标明希望添加或更新的是哪颗树的子节点数据
	 */
	@RequestMapping("/deleteOneTreeNode")
	public void deleteOneTreeNode(String sid, String treekey, HttpServletRequest req, HttpServletResponse res)  throws Exception {
		Assert.fail("deleteOneTreeNode 方法在调用之前必须被子类所覆盖！");
	}
}
