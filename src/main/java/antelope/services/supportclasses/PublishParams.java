package antelope.services.supportclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 静态页面发布时所需要的参数类
 * @author lining
 * @since 2014-1-20
 */
public class PublishParams {

	/**
	 * 保存在webapp下ftls文件夹下的ftl文件名称(不包含.ftl后缀名)
	 */
	public String ftlName;
	
	/**
	 * 生成在published中的静态页面名称(不包含.html后缀名)
	 * 注意若要生成多个静态页面，可以保持此参数为空，这时将会使用各个静态页面对应rootMap的sid作为文件名如1365302751188.html
	 */
	public String htmlPageName;
	
	/**
	 * 生成静态页面时所需要freemarker根数据列表
	 */
	public List<Map<String, Object>> rootMapList = new ArrayList<Map<String, Object>>();
	
}
