package antelope.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import antelope.beans.SysFileInfo;
import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.entities.SysFile;
import antelope.modules.controllers.AmsMulticastLiveController.AmsFileInfo;
import antelope.springmvc.BaseController;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.JSONObject;
import antelope.utils.RegExpUtil;
import antelope.utils.SpeedIDUtil;


/**
 * 文件上传控制器
 * @author lining
 * @version 1.0
 */
@Controller
public class UploadController extends BaseController{
	
	/**
	 * 获取文件上传当前文件传输数据位置
	 * @param filegroupsid 文件组sid;
	 * @param req
	 * @param res
	 */
	@RequestMapping("/upload/getUploadFilePosition")
	@Transactional
	public void getUploadFilePosition(String fileselectedpath, String fileid, HttpServletRequest req, HttpServletResponse res) throws FileUploadException, IOException, IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException {
		System.out.println(fileid);
		File file = new File("D:/tmp.mp4");
		if (!file.exists()) {
			print(0, res);
		} else {
			FileInputStream fis = new FileInputStream("D:/tmp.mp4");
			print(fis.available(), res);
			fis.close();
		}
	}
	
	/**
	 * 文件上传
	 * @param filegroupsid 文件组sid;
	 * @param req
	 * @param res
	 */
	@RequestMapping("/upload/uploadfiles")
	@Transactional
	public void uploadFiles(String filegroupsid, String uploadtimeid,  String enablePermanent, HttpServletRequest req, HttpServletResponse res) throws FileUploadException, IOException, IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException {
		List<FileItem> files = parseRequest(req);
		SysFile file = new SysFile();
		file.sid = SpeedIDUtil.getId();
		file.filegroupsid = filegroupsid;
		file.filename = files.get(0).getString("utf-8");
		file.filedata = files.get(1).get();
		file.filesize = file.filedata.length;
		file.uploaddate = now();
		file.uploadtimeid = uploadtimeid;
		if ("true".equals(enablePermanent))
			file.ispermanent = 1;
		else
			file.ispermanent = 0;
		dao.insertOrUpdate(file);
	}
	
	/**
	 * 设置永久保存状态
	 * @param ispermanent
	 * @param filegroupsid
	 * @param res
	 */
	@RequestMapping("/upload/UploadController/setPermanent")
	public void setPermanent(String ispermanent, String filegroupsid, String uploadtimeid, HttpServletResponse res) {
		dao.updateBySQL("update sys_files set ispermanent=" + ispermanent + " where filegroupsid=? and uploadtimeid=?", new Object[]{filegroupsid, uploadtimeid});
	}
	
	/**
	 * 通过文件分组sid获取组内文件明细信息
	 */
	@RequestMapping("/upload/getuploadedfileinfos")
	public void getFileInfosByFileGroupSid(String filegroupsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String token = req.getParameter("token");
		
		List<JSONObject> result = DBUtil.queryJSON("select sid, filegroupsid, filename, filesize, uploaddate, ispermanent, uploadtimeid, willdelete, filedesc from sys_files where filegroupsid=?", new Object[]{filegroupsid});

		if (stringSet(token)) { // app访问 
			JSONObject obj = new JSONObject();
			obj.put("filegroupsid", filegroupsid);
			obj.put("fileinfos", toJSONArray(result));
			getOut(res).print(obj);		
		} else {
			getOut(res).print(toJSONArray(result));			
		}
		
	}
	
	/**
	 * 获取图片数据
	 * @param imagesid
	 * @param res
	 */
	@RequestMapping("/upload/UploadController/getSingleImageData")
	public void getSingleImageData(String imagesid, HttpServletResponse res) throws IOException, Exception {
		printBytes(fileupload.getFileDataBySid(imagesid), res);
	}
	
	/**
	 * 根据文件组sid获取属于文件组的第一个文件的二进制数据
	 * @param filegroupsid
	 * @param res
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping("/upload/UploadController/getSingleImageDataByFilegroupsid")
	public void getSingleImageDataByFilegroupsid(String filegroupsid, HttpServletResponse res) throws IOException, Exception {
		List<String> filesids = fileupload.getFilesidsByGroupSid(filegroupsid);
		if (filesids.isEmpty()) {
			return;
		}
		printBytes(fileupload.getFileDataBySid(filesids.get(0)), res);
	} 
	
	/**
	 * 文件下载
	 * @param sid
	 * @param req
	 * @param res
	 */
	@RequestMapping("/upload/downloadfile")
	public void downloadFile (String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		SysFile sysfile = dao.querySingle("select * from sys_files where sid=?", new Object[]{sid}, SysFile.class);
		if (sysfile != null && sysfile.filedata != null) {
			outputStream.write(sysfile.filedata);
		}
		
		res.setHeader("Content-Disposition","attachment; filename=" + URLEncoder.encode(sysfile.filename, "utf-8" ));
		ServletOutputStream streamOut = res.getOutputStream();
		try {
			outputStream.writeTo(streamOut);
		} catch( Exception e) { // 用户终止文件的下载操作，异常不进行处理
		}
		streamOut.close();
		return; 
	}
	
	/**
	 * 文件删除 
	 * @param sid
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/upload/delfile")
	public void deleteFile(String sid, String enablepermanent, String uploadtimeid, HttpServletResponse res) throws Exception {
		if ("true".equals(enablepermanent)) { // 若为自动持久化则直接删除
			JSONObject obj = new JSONObject();
			obj.put("filename", DBUtil.querySingleVal("select filename from SYS_FILES where sid=?", sid, DataType.String));
			dao.deleteBy(sid, SysFile.class);
			getOut(res).print(obj);
		} else { // 否则标志待删除
			dao.updateBySQL("update SYS_FILES set willdelete='1', uploadtimeid=? where sid=?", new Object[]{uploadtimeid, sid});
		}
	}
	
	/**
	 * 文件预览前swf转换
	 * @param sid
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/upload/convertDocToSwf")
	public void convertDocToSwf(final String sid, HttpServletResponse res) throws Exception {
		String parentpath = ClasspathResourceUtil.getWebappFolderFile("/").getParent();
		File exportimgfile = new File(parentpath + "/exportimgs/");
		
		File[] listFiles = exportimgfile.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.equals(sid);
			}
		});
		
		
		if (listFiles.length == 0) { // 未转换过，开始转换
			
			SysFile sysfile = dao.getBy(sid, SysFile.class);
			
			
			String suffix = RegExpUtil.getFirstMatched(Pattern.compile("\\.[^\\.]*$"), sysfile.filename);
			
			File thedocfile = new File(exportimgfile.getAbsolutePath() + "/" + sid + "/" + sid + suffix);
			
			FileUtils.writeByteArrayToFile(thedocfile, sysfile.filedata);
			
			// 数据转换为swf格式 begin
			if (!suffix.endsWith("pdf")) {
				
				File dllfile = new File (new File(this.getClass().getResource("/").getPath()).getParent() + "/lib/jacob-1.16.1-x64.dll");
				
				System.getProperties().put("jacob.dll.path", dllfile.getAbsolutePath());
				
				String[][] convertParams = new String[][]{
														  {".docx", "word.application", "Documents", "17", "close"},
														  {".doc", "word.application", "Documents", "17", "close"},
														  {".pptx", "PowerPoint.application", "Presentations", "32", "no"},
														  {".ppt", "PowerPoint.application", "Presentations", "32", "no"}};
				
				// 选择转换参数
				String[] convertParam = null;
				for (int i = 0; i < convertParams.length; ++i) {
					if (suffix.equals(convertParams[i][0])) {
						convertParam = convertParams[i];
						break;
					}
				}
				
				ActiveXComponent word = new ActiveXComponent(convertParam[1]);
				try {
					Dispatch dispatch = null;
					if (suffix.matches("^\\.doc|\\.docx$"))
						dispatch = word.getPropertyAsComponent(convertParam[2]).invoke("Open", thedocfile.getAbsolutePath()).toDispatch();
					if (suffix.matches("^\\.ppt|\\.pptx$")) {
						dispatch = word.getPropertyAsComponent(convertParam[2]).invoke("Open", new Variant(thedocfile.getAbsolutePath()), new Variant(true), new Variant(false), new Variant(false)).toDispatch();
					}
					
					// 删除旧文件
					File oldpdf = new File(thedocfile.getAbsolutePath().replaceFirst("\\.[^\\.]*$", "") + ".pdf");
					if (oldpdf.exists()) {
						for (int i = 1; oldpdf.exists(); ++i) {
							oldpdf.delete();
							oldpdf = new File(thedocfile.getAbsolutePath().replaceFirst("\\.[^\\.]*$", "") + i + ".swf");
						}
					}
					
					Dispatch.invoke(dispatch, "SaveAs",  
						  Dispatch.Method, new Object[] {thedocfile.getAbsolutePath().replaceFirst("\\.[^\\.]*$", "") + ".pdf", new Variant(Integer.parseInt(convertParam[3]))}, new int[1]); // 设置17,32，即转为pdf
					if ("close".equals(convertParam[4]))
						Dispatch.invoke(dispatch, "Close", Dispatch.Method, new Object[]{new Variant(false)}, new int[1]);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					word.invoke("Quit");
				}
			}
			
			URL url = this.getClass().getResource("/pdf2swf.exe");
			Process process = Runtime.getRuntime().exec(url.getFile().substring(1) + " " + thedocfile.getAbsolutePath().replaceFirst("\\.[^\\.]*$", "") + ".pdf -o " + thedocfile.getAbsolutePath().replaceFirst("\\.[^\\.]*$", "") + ".pdf_%.swf -f -T 9 -t -s storeallcharacters -s linknameurl");
			InputStream procin = process.getInputStream();
			for(;procin.read() != -1;); // 确保完成文件转换
			// 转换数据为swf格式end
			// 计算总页数
			
//			process.waitFor();
		}
		
		int i = 1;
		File pagefile = new File(exportimgfile.getAbsolutePath() + "/" + sid + "/" + sid + ".pdf_" + i + ".swf");
		for (; pagefile.exists(); ++i) {
			pagefile = new File(exportimgfile.getAbsolutePath() + "/" + sid + "/" + sid + ".pdf_" + i + ".swf");
		}
		print(i - 2, res);
	}
	
	/**
	 * 保存文件说明
	 * @param desc
	 * @param sid
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/upload/saveFileDescription")
	public void saveFileDescription(String desc, String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		dao.updateBySQL("update SYS_FILES set filedesc=? where sid=?", new Object[]{d(desc), sid});
	}
}



