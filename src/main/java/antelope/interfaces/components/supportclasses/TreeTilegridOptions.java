package antelope.interfaces.components.supportclasses;

import java.util.Map;

import antelope.interfaces.components.BaseUIController;
import antelope.interfaces.components.BaseUIOptions;


/**
 * 
 * @author lining
 * @since 2013-3-4
 */
public class TreeTilegridOptions extends BaseUIOptions {
	
	public TreeOptions treeOptions = new TreeOptions();
	
	/**
	 * 用于自定义查询区域表单jsp文件所在路径
	 * 此选项优先级高于queryfields，若不为空且不为空字符串，则将使用此属性所对应jsp文件来填充查询区域表单。
	 */
	public String queryformKey = null;
	
	/**
	 * 用于拼接tile的方法名称，可以写在 XXXtreeform.jsp中
	 */
	public String tileRendererFunction = null;

	public TreeTilegridOptions(BaseUIController controller) {
		super(controller);
	}

}
