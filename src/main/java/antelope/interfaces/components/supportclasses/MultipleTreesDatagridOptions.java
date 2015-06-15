package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.Map;

import antelope.interfaces.components.BaseUIController;

public class MultipleTreesDatagridOptions extends SingleDatagridOptions{

	public MultipleTreesDatagridOptions(BaseUIController controller) {
		super(controller);
	}
	
	public Map<String, TreeOptions> treeOptionMap = new LinkedHashMap<String, TreeOptions>();
	

	/**
	 * 是否将树节点数据获取隔离开，默认为不隔离
	 */
	public boolean isIsolatedTrees = false; 
}
