package antelope.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.beans.ToviewListItem;
import antelope.interfaces.ToviewListDataProvider;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;

/**
 * 全局待阅事项访问控制器
 *
 */
@Controller
public class ToviewListController extends BaseController {
	
	/**
	 * 获取全局待阅事项列表
	 * @param req
	 */
	@RequestMapping("/common/getLogonUserAllToviewList")
	public void getLogonUserAllToviewList(HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<ToviewListDataProvider> dataProviders = SpringUtils.getBeans(ToviewListDataProvider.class);
		JSONArray arr = new JSONArray();
		for (ToviewListDataProvider toviewListDataProvider : dataProviders) {
			try {
			  
              List<ToviewListItem> toviewlistitems = toviewListDataProvider.getToviewListItems(getService(req),req);
              for (ToviewListItem toviewListItem : toviewlistitems) {
              	arr.put(new JSONObject(toviewListItem));
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
		}
		getOut(res).print(arr);
	}
}

