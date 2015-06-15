package antelope.interfaces.components.supportclasses;

/**
 * 菜单对象
 * @author lining
 * @since 2013-8-24
 */
public class MenuItem {
	
	/**
	 * 用于树结构菜单时，对应调用的js方法将传递下列参数 event, treeid, treenode.
	 * 其中treenode为当前点击的树节点的数据对象
	 */
	public String click;
	
	public MenuItem(String click) {
		this.click = click;
	}
}
