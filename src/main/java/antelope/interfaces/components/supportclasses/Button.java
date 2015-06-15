package antelope.interfaces.components.supportclasses;


/**
 * 按钮，用于全界面组件中按钮的控制操作
 * @author lining
 * @since 2012-8-29
 */
public class Button {
	public String click;
	public String visibleFunction;
	public String toolTip;
	
	public Button(String click) {
		this.click = click;
	}
	
	public Button(String click, String toolTip) {
		this.click = click;
		this.toolTip = toolTip;
	}
}
