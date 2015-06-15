package antelope.controllers.components;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.TreeListSelect;
import antelope.interfaces.components.TreeSelect;
import antelope.interfaces.components.supportclasses.SelectionTreeFilter;
import antelope.interfaces.components.supportclasses.TreeListSelectionOptions;
import antelope.interfaces.components.supportclasses.TreeSelectionOptions;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;

/**
 * 树加列表结构数据选择通用组件
 * @author lining
 * @since 2013-3-7
 */
@Controller
public class TreeListSelectController extends BaseController{
	
	/**
	 * 获取列表选择控件选项信息
	 */
	@RequestMapping("/common/components/TreeListSelectController/getTreeSelectOptions")
	public void getTreeSelectOptions(String component, HttpServletRequest req, HttpServletResponse res) throws IOException {
		print(new JSONObject(SpringUtils.getBean(TreeListSelect.class, component).getOptions(req), true), res);
	}
	
	/**
	 * 获取待选择的树结构项item
	 * @param sid 所打开父节点sid，若为根节点则返回title
	 */
	@RequestMapping("/common/components/TreeListSelectController/getTreeSelectItemsToSelect")
	public void getTreeSelectItemsToSelect(String component, String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		TreeListSelect bean = SpringUtils.getBean(TreeListSelect.class, component);
		TreeListSelectionOptions opts = bean.getOptions(req);
		
		if (!stringSet(sid)) {
			JSONArray arr = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put("sid", "root");
			obj.put("name", opts.treetitle);
			obj.put("isParent", true);
			arr.put(obj);
			print(arr, res);
		} else {
			if ("root".equals(sid)) {
				print(SpringUtils.getBean(TreeListSelect.class, component).getChildren(null, req, res), res);
			} else {
				print(SpringUtils.getBean(TreeListSelect.class, component).getChildren(sid, req, res), res);
			}
		}
	}
	
	/**
	 * 获取待选择的过滤树结构信息
	 * @param sid 所打开父节点sid
	 */
	@RequestMapping("/common/components/TreeListSelectController/getFilterTreeItemsToSelect")
	public void getFilterTreeItemsToSelect(String component, String treefilterkey, String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		TreeSelect bean = SpringUtils.getBean(TreeSelect.class, component);
		TreeSelectionOptions opts = bean.getOptions(req);
		SelectionTreeFilter selectionFilter = (SelectionTreeFilter) opts.treefilters.get(treefilterkey);
		print(selectionFilter.getChildren(sid, req, res), res);
	}
	
	/**
	 * 获取待选择的列表项item
	 */
	@RequestMapping("/common/components/TreeListSelectController/getListSelectItemsToSelect")
	public void getListSelectItemsToSelect(String component, String queryval, HttpServletRequest req, HttpServletResponse res) throws Exception {
		print(SpringUtils.getBean(TreeListSelect.class, component).getJSONPage(decodeAndTrim(queryval), req.getParameter("treenode_sid"), req), res);
	}
	
	/**
	 * 获取历史已经选中的列表项
	 * @param component 后台Spring部件名称
	 * @param selectedItemsids 逗号分割的已经选中的列表项sid
	 */
	@RequestMapping("/common/components/TreeListSelectController/getHistSelectedItems")
	public void getHistSelectedItems(String component, String selectedItemSids, String parentsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		TreeListSelect listselect = SpringUtils.getBean(TreeListSelect.class, component);
		List<JSONObject> selectedItems = listselect.getSelectedItems(selectedItemSids, parentsid, req);
		print(selectedItems, res);
	}
}
