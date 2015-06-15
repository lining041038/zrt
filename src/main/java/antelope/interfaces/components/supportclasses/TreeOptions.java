package antelope.interfaces.components.supportclasses;

public class TreeOptions {
	
	/**
	 * 列表标签名称，对于多列表来说可显示为页签标题
	 */
	public String label;
	
	/**
	 * 树节点添加按钮是否显示
	 */
	public boolean showTreeNodeCreateBtn = true;
	
	/**
	 * 树节点修改按钮是否显示
	 */
	public boolean showTreeNodeUpdateBtn = true;
	
	/**
	 * 树节点删除按钮是否显示
	 */
	public boolean showTreeNodeDeleteBtn = true;
	
	/**
	 * 树的根节点是否允许编辑
	 */
	public boolean treeRootEditable = true;
	
	
	/**
	 * 当点击树节点时，对应节点是否允许编辑的js方法名称
	 * 可在对应的XXXtreeform.jsp中书写相应js，方法中参数与树控件对应click回调参数一致，
	 * 为click(event, treeId, treeNode) 详细请查阅相关文档。
	 * 方法返回值如果为true或不返回则表明允许编辑，返回false则表明禁止编辑
	 */
	public String treeNodeEditableFunction = null;
	
	/**
	 * 当加载树节点时用于定位的树节点路径数组
	 */
	public String[] locatePath = null;
	
	public TreeOptions(String label) {
		this.label = label;
	}
	
	public TreeOptions() {
		
	}
}
