package antelope.interfaces.components.supportclasses;

/**
 * 移动数据相关参数
 * @author lining
 * @since 2013-6-28
 */
public class MoveParams {

	/**
	 * 需要移动的对象的sid，注意，若存在多个则逗号进行分割。
	 */
	public String sid;
	
	/**
	 * 是否为向上移动，若不为向上移动则为向下移动
	 */
	public boolean isup;
	
	/**
	 * 移动的次数,默认为1次
	 */
	public int updowntimes = 1;
}
