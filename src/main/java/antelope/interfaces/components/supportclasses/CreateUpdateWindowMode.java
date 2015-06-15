package antelope.interfaces.components.supportclasses;

/**
 * 创建更新窗口模式
 * @author lining
 * @since 2013-10-7
 */
public class CreateUpdateWindowMode {
	
	/**
	 * 页面（或者iframe框架内）窗口模式，默认的窗口模式
	 */
	public static final String DIALOG = "dialog";
	
	/**
	 * 使用top窗口对话框模式, 注意此模式下某些参数需要使用WindowModeDialogTopIframeParams类来完成设置
	 */
	public static final String DIALOG_TOP_IFRAME = "dialog_top_iframe";
	
	
	/**
	 * 使用替换当前界面的方式（行内）显示
	 */
	public static final String DISPLAY_INLINE = "display_inline";
	
}
