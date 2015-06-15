package antelope.interfaces.components.supportclasses;

/**
 * 选择组件待选过滤条件类
 * @author lining
 * @since 2013-3-12
 */
public abstract class SelectionFilter {
	
	public abstract int getWidth();
	
	public abstract String getTitle();
	
	public abstract int getHeight();
	public abstract String getSupportjspname();
	
	/**
	 * 获取默认的过滤条件参数值
	 */
	public abstract String getDefaultParamValue();
	
}
