package antelope.demos;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;

@Controller
public class DemoIndexController extends BaseController {

	@RequestMapping("/demos/index/DemoIndexController/getDemoData")
	public void getDemoData(HttpServletRequest req, HttpServletResponse res) throws IOException {
		print("操作成功", res);
	}
}
