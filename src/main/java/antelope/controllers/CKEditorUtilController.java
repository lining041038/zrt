package antelope.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.entities.SysFile;
import antelope.springmvc.BaseController;
import antelope.utils.SpeedIDUtil;

@Controller
public class CKEditorUtilController extends BaseController {
	
	/**
	 * 生成新的公式并返回公式对应的sid 
	 * @throws ServletException 
	 */
	@RequestMapping("/common/CKEditorUtilController/uploadImageFile")
	public void uploadImageFile(HttpServletRequest req, HttpServletResponse res) throws FileUploadException, IOException, IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException, ServletException {
		
		List<FileItem> files = parseRequest(req);
		SysFile file = new SysFile();
		file.sid = SpeedIDUtil.getId();
		file.filegroupsid = "ckeditorimg";
		file.filename = files.get(0).getName();
		file.filedata = files.get(0).get();
		file.filesize = file.filedata.length;
		file.uploaddate = now();
		file.uploadtimeid = "ckeditorimgtimeid";
		file.ispermanent = 1;
		dao.insertOrUpdate(file);
		req.getRequestDispatcher("/js/ckeditor/imageupload.jsp?sid=" + file.sid).forward(req, res);
	}
}










