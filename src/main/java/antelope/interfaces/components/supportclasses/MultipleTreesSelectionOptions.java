package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.Map;


public class MultipleTreesSelectionOptions extends ListSelectOptions {
	public Map<String, Tree4MtlsOption> treeOptionMap = new LinkedHashMap<String, Tree4MtlsOption>();
	
	/**
	 * 第一颗树根节点名称
	 */
	public String firstTreeName;
}
