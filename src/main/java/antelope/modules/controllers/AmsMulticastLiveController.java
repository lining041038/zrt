package antelope.modules.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.RegExpUtil;
import antelope.utils.SystemOpts;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

@Controller
public class AmsMulticastLiveController extends BaseController {
	
	private static final int timeoutMilling = 60000;
	
	private static final Map<String, List<AmsMessage>> msgCacheMap = new HashMap<String, List<AmsMessage>>();
	
	private static final Map<String, AmsFileInfo> amsFileInfos = new HashMap<String, AmsFileInfo>();
	
	
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/getSenderOpts")
	public void getSenderOpts(HttpServletRequest req, HttpServletResponse res) throws IOException, JSONException {
		JSONObject optsobj = new JSONObject();
		optsobj.put("ipMulticastAddress", SystemOpts.getProperty("antelope_ipmulticastaddr"));
		optsobj.put("publishPassword", SystemOpts.getProperty("antelope_fmspublishpassword"));
		optsobj.put("user", getService(req).getUser());
		print(optsobj, res);
	}
	
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/addAmsMessage")
	public void addAmsMessage(String streamname, String senderName, String message, HttpServletRequest req, HttpServletResponse res) {
		List<AmsMessage> AmsMessages = msgCacheMap.get(streamname);
		if (AmsMessages == null) {
			AmsMessages = new ArrayList<AmsMessage>();
			msgCacheMap.put(streamname, AmsMessages);
		}
		
		AmsMessage AmsMessage = new AmsMessage();
		AmsMessage.createTime = now();
		AmsMessage.senderName = d(senderName);
		AmsMessage.message = d(message);
		AmsMessages.add(AmsMessage);
	}
	
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/getRecentMessages")
	public void getRecentMessages(String streamname, HttpServletRequest req, HttpServletResponse res) throws IOException {
		List<AmsMessage> AmsMessages = msgCacheMap.get(streamname);
		if (AmsMessages == null)
			AmsMessages = new ArrayList<AmsMessage>();
		
		print(toJSONArrayBy(AmsMessages), res);
	}
	
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/getCurrentPage")
	public void getCurrentPage(String streamname, HttpServletRequest req, HttpServletResponse res) throws IOException {
		if (amsFileInfos.get(streamname) == null)
			print("null", res);
		else
			print(new JSONObject(amsFileInfos.get(streamname), true), res);
	}
	
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/getTotalPage")
	public void getTotalPage(String streamname, HttpServletRequest req, HttpServletResponse res) throws IOException {
		String uploadedfmsfileNameprefix = "D:/amsmlivefiles/" + streamname;
		int i = 1;
		File file = new File(uploadedfmsfileNameprefix + i + ".swf");
		for (; file.exists(); ++i) {
			file = new File(uploadedfmsfileNameprefix + i + ".swf");
		}
		print(i - 2, res);
	}
	
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/saveCurrPage")
	public void saveCurrPage(String streamname, String page, HttpServletRequest req, HttpServletResponse res) {
		AmsFileInfo fileinfo = amsFileInfos.get(streamname);
		if (fileinfo == null) {
			fileinfo = new AmsFileInfo();
			amsFileInfos.put(streamname, fileinfo);
		}
		fileinfo.currpage = Integer.parseInt(page);
	}
	
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/getUploadedFile")
	public void getUploadedFile(String streamname, HttpServletRequest req, HttpServletResponse res) throws FileUploadException, IOException {
		List<FileItem> files = parseRequest(req);
		String filename = files.get(0).getString("utf-8");
		File fmsfilecontainer = new File("D:/amsmlivefiles/");
		if (!fmsfilecontainer.exists()) {
			fmsfilecontainer.mkdirs();
		}
		
		String uploadedfmsfileNameprefix = "D:/amsmlivefiles/" + streamname;
		String suffix = RegExpUtil.getFirstMatched(Pattern.compile("\\.[^\\.]*$"), filename);
		String uploadedfmsfileName = uploadedfmsfileNameprefix + suffix;
		
		FileOutputStream file = new FileOutputStream(uploadedfmsfileName + "");
		IOUtils.copyLarge(files.get(1).getInputStream(), file);
		file.close();
		
		// 数据转换为swf格式 begin
		File dllfile = new File (new File(this.getClass().getResource("/").getPath()).getParent() + "/lib/jacob-1.16.1-x86.dll");
		
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
				dispatch = word.getPropertyAsComponent(convertParam[2]).invoke("Open", uploadedfmsfileName).toDispatch();
			if (suffix.matches("^\\.ppt|\\.pptx$")) {
				dispatch = word.getPropertyAsComponent(convertParam[2]).invoke("Open", new Variant(uploadedfmsfileName), new Variant(true), new Variant(false), new Variant(false)).toDispatch();
			}
			
			// 删除旧文件
			File oldpdf = new File(uploadedfmsfileNameprefix + ".pdf");
			if (oldpdf.exists()) {
				for (int i = 1; oldpdf.exists(); ++i) {
					oldpdf.delete();
					oldpdf = new File(uploadedfmsfileNameprefix + i + ".swf");
				}
			}
			
			Dispatch.invoke(dispatch, "SaveAs",  
				  Dispatch.Method, new Object[] {uploadedfmsfileNameprefix + ".pdf", new Variant(Integer.parseInt(convertParam[3]))}, new int[1]); // 设置17,32，即转为pdf
			if ("close".equals(convertParam[4]))
				Dispatch.invoke(dispatch, "Close", Dispatch.Method, new Object[]{new Variant(false)}, new int[1]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			word.invoke("Quit");
		}
		URL url = this.getClass().getResource("/pdf2swf.exe");
		
		// D:\SDSDDS\fdsfdsdaa.pdf -o d:\flexpaper\docs\fdsfdsdaa.pdf_1.swf -f -T 9 -t -s storeallcharacters -s linknameurl -p 1
		Process process = Runtime.getRuntime().exec(url.getFile().substring(1) + " " + uploadedfmsfileNameprefix + ".pdf -o " + uploadedfmsfileNameprefix + "%.swf  -f -T 9 -t -s storeallcharacters -s linknameurl -p 1");
		//InputStream procin = process.getInputStream();
		
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		for(;procin.read() != -1;); // 确保完成文件转换
		// 转换数据为swf格式end
		
		AmsFileInfo fileinfo = amsFileInfos.get(streamname);
		if (fileinfo == null) {
			fileinfo = new AmsFileInfo();
			amsFileInfos.put(streamname, fileinfo);
		}
		++fileinfo.version;
		fileinfo.currpage = 1;
		
		// 计算总页数
		int i = 1;
		File pagefile = new File(uploadedfmsfileNameprefix + i + ".swf");
		for (; pagefile.exists(); ++i) {
			pagefile = new File(uploadedfmsfileNameprefix + i + ".swf");
		}
		fileinfo.totalpage = i - 2;
	}

	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/getSwfFileDatas")
	public void getSwfFileDatas(String streamname, String page, HttpServletRequest req, HttpServletResponse res) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream("D:/amsmlivefiles/" + streamname + page + ".swf");
		try {
			IOUtils.copyLarge(fis, res.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Scheduled(cron="0 * * ? * *")
	public void clearCycle() {
		Set<Entry<String, List<AmsMessage>>> entries = msgCacheMap.entrySet();
		List<String> keys = new ArrayList<String>();
		for (Entry<String, List<AmsMessage>> entry : entries) {
			keys.add(entry.getKey());
		}
		
		for (String key : keys) {
			List<AmsMessage> AmsMessages = msgCacheMap.get(key);
			for(int i = 0; i < AmsMessages.size();) {
				AmsMessage fmsmsag = AmsMessages.get(i);
				if (fmsmsag.createTime.getTime() + timeoutMilling < System.currentTimeMillis()) {
					AmsMessages.remove(i);
				} else {
					++i;
				}
			}
			if (AmsMessages.isEmpty()) {
				msgCacheMap.remove(key);
			}
		}
	}
	
	/**
	 * 组播组件弹出文件选择树形结构时调用。
	 * 此方法为举例方法，可以在$("body").loadAmsMulticastLive()的调用参数中传入想要获取文件树形结构列表的url地址，以完成定制化，如
	 * $("body").loadAmsMulticastLive({
	 *		wmode:'transparent',
	 *      ...
	 *		videotoselecturl: "/modules/amsmulticastlive/AmsMulticastLiveController/getVideoSelectTreeChildren.vot"
	 *	});
	 * @param parentsid 父路径节点目录
	 */
	@RequestMapping("/modules/amsmulticastlive/AmsMulticastLiveController/getVideoSelectTreeChildren")
	public void getVideoSelectTreeChildren(String parentVirtualDirectory, HttpServletRequest req, HttpServletResponse res) throws IOException {
		String vodmediadict = "D:/Program Files/Adobe/Adobe Media Server 5/applications/vod/media";
		parentVirtualDirectory = d(noNull(parentVirtualDirectory));
		File file = new File(vodmediadict + parentVirtualDirectory);
		File[] subfiles = file.listFiles();
		List<AmsVideoInfoItem> videoinfos = new ArrayList<AmsVideoInfoItem>();
		for (File subfile : subfiles) {
			AmsVideoInfoItem info = new AmsVideoInfoItem();
			info.virtualDirectory = parentVirtualDirectory + "/" + subfile.getName();
			info.name = subfile.getName();
			info.hasChildren = subfile.isDirectory();
			videoinfos.add(info);
		}
		print(toJSONArrayBy(videoinfos), res);
	}
	
	public static class AmsVideoInfoItem {
		public String virtualDirectory;
		public String name;
		public boolean hasChildren = false;
	}
	
	public static class AmsFileInfo {
		public int version;
		public int totalpage;
		public int currpage;
	}
	
	public static class AmsMessage {
		public Timestamp createTime;
		public String senderName;
		public String message;
	}
}
