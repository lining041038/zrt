package antelope.wcm.consts;

import java.util.HashMap;
import java.util.Map;

public class WCMSiteSettingConsts {

	public static final String LOGO_IMG_SID = "logoimgsid";

	public static final String NAVIGATION_TREE = "navigationtree";

	public static final String WEB_FOOTERINFO = "webfooterinfo";

	public static final String WEB_HEADERINFO = "web_headerinfo";

	public static final String WEB_BANNERSID = "web_bannersid";

	public static final String WEB_INDEXPORTLETS = "web_indexportlets";

	public static final String WEB_IMGPORTLETS = "web_imgportlets";

	private static final Map<String, String> names = new HashMap<String, String>();

	static {
		names.put(LOGO_IMG_SID, "logo图片sid");
		names.put(NAVIGATION_TREE, "导航树结构");
		names.put(WEB_FOOTERINFO, "网站底部信息");
		names.put(WEB_BANNERSID, "banner图片");
		names.put(WEB_INDEXPORTLETS, "首页模块设置");
		names.put(WEB_IMGPORTLETS, "首页幻灯片设置");
	}

	public static String getName(String code) {
		return names.get(code);
	}

}
