package antelope.demos;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.services.supportclasses.WordTmpl;
import antelope.springmvc.BaseController;
import antelope.utils.JSONObject;
import antelope.utils.SpeedIDUtil;

@Controller
public class WordExportDemoController extends BaseController {
	
	@RequestMapping("/demos/wordexport/WordExportDemoController/exportWord")
	public void exportWord(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		WordTmpl tmplInstance = word.getTmplInstance("demoarticle", "exporttheword.docx", res);
		JSONObject obj = new JSONObject();
		obj.put("sid", SpeedIDUtil.getId());
		obj.put("name", "蒋光华");
		obj.put("year", "2013");
		obj.put("money", 500);
		tmplInstance.wrapData(obj);
		tmplInstance.closeWord();
	}
}
