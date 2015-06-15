package antelope.demos;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import freemarker.template.TemplateException;

import antelope.springmvc.BaseController;

@Controller
@RequestMapping("/demos/freemarker/FreeMarkerDemoController")
public class FreeMarkerDemoController extends BaseController {

	
	@RequestMapping("/publishAll")
	public void publishAll(HttpServletRequest req, HttpServletResponse res) throws IOException, TemplateException {
		freemarker.publishAll();
	}
}
