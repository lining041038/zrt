package antelope.wcm.beans;


/**
 * 模板信息类，此类数据基本上为读取各个模板template.properties文件信息而得来
 * @author lining
 * @since 2013-3-15
 */
public class WCMTemplateInfoBean {

	public String sid;
	
	public String name;
	
	/**
	 * 模板所在相对路径，如/defaults
	 */
	public String path;
}
