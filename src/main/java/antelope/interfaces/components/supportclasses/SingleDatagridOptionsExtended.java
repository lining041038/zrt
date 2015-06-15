package antelope.interfaces.components.supportclasses;


import antelope.interfaces.components.BaseUIController;

/**
 * 单列表增删改功能界面选项扩展选项，为多列表界面或其他界面提供更多选项
 * @author lining
 * @since 2012-7-14
 */
public class SingleDatagridOptionsExtended extends SingleDatagridOptions{
	
	public SingleDatagridOptionsExtended(BaseUIController controller) {
		super(controller);
	}
	
	/**
	 * 列表标签名称，对于多列表来说可显示为页签标题
	 */
	public String label;
	
	/**
	 * 表单对应jsp页面域，若设置，则根据此数据域包含数据的jsp页面目录如/system/sss.jsp加载表单
	 * 若未设置，则依然根据之前的方式加载
	 */
	public String formKeyField;
}




