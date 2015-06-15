package antelope.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import antelope.services.supportclasses.FreeMarkerPage;
import antelope.services.supportclasses.PublishParams;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SpringUtils;
import antelope.utils.ClasspathResourceUtil;

/**
 * Freemarker静态发布服务类
 * @author lining
 * @since 2014-1-20
 */
@Service
public class FreeMarkerService extends BaseComponent {
	
	/**
	 * 简单发布所有待发布的静态页面
	 */
	public void publishAll() throws IOException, TemplateException {
		List<FreeMarkerPage> pages = SpringUtils.getBeans(FreeMarkerPage.class);
		
		Configuration cfg = new Configuration();
		cfg.setEncoding(Locale.CHINA, "utf-8");
		cfg.setDefaultEncoding("utf-8");
		cfg.setOutputEncoding("utf-8");
		cfg.setDirectoryForTemplateLoading(ClasspathResourceUtil.getWebappFolderFile("/ftls"));
		
		for (FreeMarkerPage freeMarkerPage : pages) {
			PublishParams publishParams = freeMarkerPage.getPublishParams();
			Template tmpl = cfg.getTemplate(publishParams.ftlName + ".ftl");
			tmpl.setOutputEncoding("utf-8");
			if (stringSet(publishParams.htmlPageName) && publishParams.rootMapList.size() > 0) {
				FileOutputStream file = new FileOutputStream(ClasspathResourceUtil.getWebappFolderFile("/published/" + publishParams.htmlPageName + ".html"));
				Writer out = new OutputStreamWriter(file, "utf-8");
				tmpl.process(publishParams.rootMapList.get(0), out);
				out.flush();
				out.close();
			} else {
				for (int i = 0; i < publishParams.rootMapList.size(); ++i) {
					FileOutputStream file = new FileOutputStream(ClasspathResourceUtil.getWebappFolderFile("/published/" + publishParams.rootMapList.get(i).get("sid") + ".html"));
					Writer out = new OutputStreamWriter(file, "utf-8");
					tmpl.process(publishParams.rootMapList.get(i), out);
					out.flush();
					out.close();
				}
			}
			freeMarkerPage.publishComplete(publishParams);
		}
	}
}



