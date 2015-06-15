package antelope.interfaces.components;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * 通用界面选项基类，提供界面选项通用选项功能
 * 提供给基于BaseUIController类的基类所组成全界面组件的通用选项功能
 * @author lining
 * @since 2012-8-17
 */
public class BaseUIOptions {
	
	/**
	 * 界面
	 */
	private String urlprefix;
	
	private String component;

	public BaseUIOptions(BaseUIController controller) {
		RequestMapping reqmapping = controller.getClass().getAnnotation(RequestMapping.class);
		Controller controll = controller.getClass().getAnnotation(Controller.class);
		Assert.notNull(reqmapping, "请在对应的 UIController上面添加RequestMapping全界面组件子url");
		this.urlprefix = reqmapping.value()[0];
		Assert.notNull(reqmapping, "请在对应的 UIController上面添加Controller全界面组件部件名称");
		this.component = controll.value();
	}
	public String getUrlprefix() {
		return urlprefix;
	}
	
	public String getComponent() {
		return component;
	}
	
	/**
	 * 用于子类覆盖，使用最终获取到的option选项值对选项进行初始化（ 一般为option选项中的值根据其他值做影响性修正）
	 */
	public void initOptions() {
	}
}




