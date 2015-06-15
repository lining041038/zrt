package antelope.demos;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.beans.PictureInfo;
import antelope.springmvc.BaseController;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;

@Controller
public class FlashSliderController extends BaseController {
	
	
	@RequestMapping("/demos/flashslider/FlashSliderController/getSliderDatas")
	public void getSliderDatas(HttpServletRequest req, HttpServletResponse res) throws JSONException, IOException {
		PictureInfo[] picinfos = new PictureInfo[]{
				new PictureInfo(req.getContextPath() + "/demos/assets/a111634u1_160_90.jpg", "百度", "http://www.baidu.com"), 
				new PictureInfo(req.getContextPath() + "/demos/assets/a128335u1_160_90.jpg", "新浪", "http://www.sina.com"),
				new PictureInfo(req.getContextPath() + "/demos/assets/a150907u1_160_90.jpg", "搜狐", "http://www.sohu.com")};
		print(new JSONArray(picinfos, true), res);
	}
}
