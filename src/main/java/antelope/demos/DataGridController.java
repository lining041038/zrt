package antelope.demos;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.cli2.validation.NumberValidator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.services.SessionService;
import antelope.springmvc.BaseController;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageParams;
import antelope.utils.PageUtil;

@Controller
public class DataGridController extends BaseController {

	@RequestMapping("/demos/DataGridController/loadManyDatas")
	public void loadManyDatas(HttpServletRequest req, HttpServletResponse res) throws JSONException, IOException {
		
		SessionService service = getService(req);
		
		System.out.println(service.getUsername());
		System.out.println(service.getUser());
		System.out.println(service.getUsersid());
		
		List<JSONObject> arr = new ArrayList<JSONObject>();
		for(int i = 0; i < 10000; i++) {
			JSONObject obj = new JSONObject();
			obj.put("test", "测试" + i);
			arr.add(obj);
		}
		
		PageParams pageParam = getPageParams(req);
		PageItem pageItem = PageUtil.getPage(pageParam, arr);
		print(new JSONObject(pageItem, true), res);
	}
	
	@RequestMapping("/demos/DataGridController/cellEdit")
	public void cellEdit(String sid, String dataField, String value, HttpServletRequest req, HttpServletResponse res) throws IOException {
		if (!noNull(value).matches("\\d+")) {
			print("请填写整数！", res);
			return;
		}
		dao.updateBySQL("update DEMO_SINGLE_DATAGRID set " + dataField+ " =? where sid=?", new Object[]{decodeAndTrim(value),sid});
	}
}
