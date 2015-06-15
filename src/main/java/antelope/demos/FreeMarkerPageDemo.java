package antelope.demos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import antelope.services.supportclasses.FreeMarkerPage;
import antelope.services.supportclasses.PublishParams;

@Component
public class FreeMarkerPageDemo extends FreeMarkerPage {

	/**
	 * 获取发布静态页面时的参数
	 */
	@Override
	public PublishParams getPublishParams() {
		PublishParams params = new PublishParams();
		params.ftlName = "demo";
		params.htmlPageName = "demo";
		Map<String, Object> pagedata = new HashMap<String, Object>();
		pagedata.put("demostr", "测试发布静态页面参数" + Math.random());
		params.rootMapList.add(pagedata);
		return params;
	}

	@Override
	public void publishComplete(PublishParams params) {
		System.out.println(params.ftlName + "发布完成");
	}
}
