package antelope.controllers.components;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.ListSelect;
import antelope.interfaces.components.TileSelect;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONObject;

/**
 * 块选择通用组件
 * @author lining
 * @since 2012-7-6
 */
@Controller
public class TileSelectController extends BaseController { 
	
	/**
	 * 获取列表选择控件选项信息
	 */
	@RequestMapping("/common/components/TileSelectController/getListSelectOptions")
	public void getListSelectOptions(String component, HttpServletRequest req, HttpServletResponse res) throws IOException {
		print(new JSONObject(SpringUtils.getBean(TileSelect.class, component).getOptions(req)), res);
	}
	
	/**
	 * 获取待选择的列表项item
	 */
	@RequestMapping("/common/components/TileSelectController/getListSelectItemsToSelect")
	public void getListSelectItemsToSelect(String component, String queryval, HttpServletRequest req, HttpServletResponse res) throws Exception {
		print(SpringUtils.getBean(TileSelect.class, component).getJSONPage(decodeAndTrim(queryval), req), res);
	}
	
}









