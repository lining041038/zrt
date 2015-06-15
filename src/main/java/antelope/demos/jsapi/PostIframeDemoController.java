package antelope.demos.jsapi;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;

@Controller
public class PostIframeDemoController extends BaseController {

	
	@RequestMapping("/demos/jsapi/postiframe/PostIframeDemoController/postIframeCalled")
	public void postIframeCalled(String sid, String dataField, String value, HttpServletRequest req, HttpServletResponse res) throws IOException {
		System.out.println("$.postIframeCalled");
		print("postiframeretval", res);
	}
}
