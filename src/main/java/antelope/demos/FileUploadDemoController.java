package antelope.demos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;

/**
 * 文件上传控件demo
 * @author lining
 * @since 2012-10-13
 */
@Controller
public class FileUploadDemoController extends BaseController {

	/**
	 * 持久化文件
	 */
	@RequestMapping("/demo/setFilePermanent")
	public void setFilePermanent(HttpServletRequest req, HttpServletResponse res) {
		fileupload.setPermanent(getFileUploadParams(req));
	}
}
