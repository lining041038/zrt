package antelope.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.SpeedIDUtil;

/**
 * Flash导出图片相关控制器
 * @author lining
 * @since 2012-2-15
 */
@Controller
public class FlashExportImgController extends BaseController {
	
	/**
	 * 获取Flash二进制数据 
	 * @throws IOException 
	 */
	@RequestMapping("/common/FlashExportImgController/receiveFlashBitmapData")
	public void receiveFlashBitmapData(HttpServletRequest req, HttpServletResponse res) throws FileUploadException, IOException {
		List<FileItem> files = parseRequest(req);
		FileItem fileItem = files.get(0);
		String filesid = SpeedIDUtil.getId();
		String parentpath = ClasspathResourceUtil.getWebappFolderFile("/").getParent();
		FileOutputStream fos = new FileOutputStream(parentpath + "/exportimgs/" + filesid + ".png");
		fos.write(fileItem.get());
		fos.flush();
		fos.close();
		print(filesid + ".png", res);
	}
}















