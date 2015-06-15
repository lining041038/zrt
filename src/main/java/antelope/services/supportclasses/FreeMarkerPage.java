package antelope.services.supportclasses;

import antelope.springmvc.BaseComponent;

/**
 * FreeMarker页面接口抽象类
 * @author lining
 * @since 2014-1-20
 */
public abstract class FreeMarkerPage extends BaseComponent {
	
	/**
	 *	通过实现此方法完成静态页面的发布工作
	 */
	public abstract PublishParams getPublishParams();
	
	
	/**
	 * 子类覆盖实现，在本页面正确发布之后调用，用以完成发布页面完成之后的相关操作
	 * @param params
	 */
	public void publishComplete(PublishParams params) {
	}
}
