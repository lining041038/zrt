package antelope.wcm.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import antelope.interfaces.components.TileSelect;
import antelope.interfaces.components.supportclasses.TileSelectOptions;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageParams;
import antelope.utils.PageUtil;

@Component("tileselecttemplate")
public class TileSelectTemplate extends TileSelect {

	@Override
	public TileSelectOptions getOptions(HttpServletRequest req) {
		TileSelectOptions opts = new TileSelectOptions();
		opts.title = "模板";
		return opts;
	}

	@Override
	public PageItem getJSONPage(String queryval, HttpServletRequest req)
			throws Exception {
		
		File tempfolder = ClasspathResourceUtil.getWebappFolderFile("/wcm/templates");
		String[] folderNames = tempfolder.list();
		PageParams pageParams = getPageParams(req);
		PageItem pageitem = PageUtil.getPage(pageParams.page, folderNames, pageParams.numPerPage);
		List<JSONObject> templateDataJson = new ArrayList<JSONObject>();
		List<String> list = pageitem.getCurrList();
		for (String object : list) {
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream(tempfolder.getAbsolutePath() + "/" + object + "/template.properties");
			props.load(fis);
			fis.close();
			JSONObject jsonObject = new JSONObject();
			Set<Entry<Object, Object>> entries = props.entrySet();
			for (Entry<Object, Object> entry : entries) {
				jsonObject.put(entry.getKey().toString(), new String(entry.getValue().toString().getBytes("ISO-8859-1"), "utf-8"));
			}
			jsonObject.put("imgpath",  req.getContextPath() + "/wcm/templates/" + object + "/screenshot.png");
			templateDataJson.add(jsonObject);
		}
		
		pageitem.setCurrList(templateDataJson);
		return pageitem;
	}

}
