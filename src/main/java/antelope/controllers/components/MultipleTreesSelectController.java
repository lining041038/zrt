package antelope.controllers.components;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.interfaces.components.MultipleTreesSelect;
import antelope.interfaces.components.supportclasses.MultipleTreesSelectionOptions;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;

/**
 * 多个树加列表结构数据选择通用组件
 * @author lining
 * @since 2013-7-5
 */
@Controller
public class MultipleTreesSelectController extends BaseController{
	
	/**
	 * 获取列表选择控件选项信息
	 */
	@RequestMapping("/common/components/MultipleTreesSelectController/getTreesListSelectOptions")
	public void getTreesListSelectOptions(String component, HttpServletRequest req, HttpServletResponse res) throws IOException {
		print(new JSONObject(SpringUtils.getBean(MultipleTreesSelect.class, component).getOptions(req), true), res);
	}
	
	/**
	 * 获取待选择的树结构项item
	 * @param sid 所打开父节点sid，若为根节点则返回title
	 */
	@RequestMapping("/common/components/MultipleTreesSelectController/getTreesListSelectItemsToSelect")
	public void getTreesListSelectItemsToSelect(String component, String sid, String treekey, String prevtreesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		MultipleTreesSelect bean = SpringUtils.getBean(MultipleTreesSelect.class, component);
		MultipleTreesSelectionOptions opts = bean.getOptions(req);
		if (!stringSet(sid) && !stringSet(prevtreesid)) {
			JSONArray arr = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put("sid", GlobalConsts.TREE_ROOT);
			obj.put("name", opts.firstTreeName);
			obj.put("isParent", true);
			arr.put(obj);
			print(arr, res);
		} else {
			if (GlobalConsts.TREE_ROOT.equals(sid)) {
				print(SpringUtils.getBean(MultipleTreesSelect.class, component).getChildren(null, treekey, prevtreesid, req, res), res);
			} else {
				print(SpringUtils.getBean(MultipleTreesSelect.class, component).getChildren(sid, treekey, prevtreesid, req, res), res);
			}
		}
	}
	
	/**
	 * 获取历史已经选中的列表项
	 * @param component 后台Spring部件名称
	 * @param selectedItemsids 逗号分割的已经选中的列表项sid
	 */
	@RequestMapping("/common/components/MultipleTreesSelectController/getHistSelectedItems")
	public void getHistSelectedItems(String component, String selectedItemSids, String parentsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		MultipleTreesSelect listselect = SpringUtils.getBean(MultipleTreesSelect.class, component);
		List<JSONObject> selectedItems = listselect.getSelectedItems(selectedItemSids, parentsid, req);
		print(selectedItems, res);
	}
}
