package antelope.interfaces.components;


import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.supportclasses.TreeSelectionOptions;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

/**
 * 通用树数据选择组件
 * 左变显示树，右边显示列表，左边为未选中的，右边为选中的
 * @author lining
 * @since 2012-12-16
 */
public abstract class TreeSelect extends ListSelect{
	
	@Override
	public abstract TreeSelectionOptions getOptions(HttpServletRequest req);
	
	/**
	 * 点击左侧树节点获取
	 * 注意此方法在调用时，除了会携带查询参数区域的参数之外，会额外携带一个parentsid参数，可以使用它来完成左侧树子节点数据的获取工作
	 * @param req
	 * @param res
	 */
	@Override
	public final PageItem getJSONPage(String queryval, HttpServletRequest req) throws Exception {
		throw new Exception("获取左侧树节点对应数据请覆盖getChildren方法！");
	}
	
	/**
	 * 获取根据父节点sid子节点信息，将子节点列表数据输出到前台
	 * @param sid 父节点sid
	 */
	@RequestMapping("/getChildren")
	public abstract List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
}



