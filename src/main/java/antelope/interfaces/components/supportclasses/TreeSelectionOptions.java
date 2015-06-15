package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通用树结构选择组件选项对象
 * @author lining
 * @since 2012-12-16
 */
public class TreeSelectionOptions extends ListSelectOptions {

	public final Map<String, SelectionFilter> treefilters = new LinkedHashMap<String, SelectionFilter>();
}
