package antelope.controllers.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.ListSelect;
import antelope.interfaces.components.supportclasses.ListSelectOptions;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONObject;

/**
 * 列表选择通用组件
 * @author lining
 * @since 2012-7-6
 */
@Controller
public class ListSelectController extends BaseController { 
	
	/**
	 * 获取列表选择控件选项信息
	 */
	@RequestMapping("/common/components/ListSelectController/getListSelectOptions")
	public void getListSelectOptions(String component, HttpServletRequest req, HttpServletResponse res) throws IOException {
		print(new JSONObject(SpringUtils.getBean(ListSelect.class, component).getOptions(req)), res);
	}
	
	/**
	 * 获取待选择的列表项item
	 */
	@RequestMapping("/common/components/ListSelectController/getListSelectItemsToSelect")
	public void getListSelectItemsToSelect(String component, String queryval, HttpServletRequest req, HttpServletResponse res) throws Exception {
		print(SpringUtils.getBean(ListSelect.class, component).getJSONPage(decodeAndTrim(queryval), req), res);
	}
	
	/**
	 * 获取历史已经选中的列表项
	 * @param component 后台Spring部件名称
	 * @param selectedItemsids 逗号分割的已经选中的列表项sid
	 */
	@RequestMapping("/common/components/ListSelectController/getHistSelectedItems")
	public void getHistSelectedItems(String component, String selectedItemSids, String parentsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ListSelect listselect = SpringUtils.getBean(ListSelect.class, component);
		List<JSONObject> selectedItems = listselect.getSelectedItems(selectedItemSids, parentsid, req);
		print(selectedItems, res);
	}
}









