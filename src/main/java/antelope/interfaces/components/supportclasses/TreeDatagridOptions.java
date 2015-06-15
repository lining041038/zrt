package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.Map;

import antelope.interfaces.components.BaseUIController;

/**
 * 左树右列表增删改功能界面选项
 * @author lining
 * @since 2012-7-14
 */
public class TreeDatagridOptions extends SingleDatagridOptions {
	
	public TreeDatagridOptions(BaseUIController controller) {
		super(controller);
		
		Button btn = new Button("moveUpTreeNode", ":tree_datagrid." + getComponent() + ".addtreenode");
		treeButtons.put("ui-icon-arrowthick-1-n treeeditbtn", btn);
		btn = new Button("moveDownTreeNode", ":tree_datagrid." + getComponent() + ".addtreenode");
		treeButtons.put("ui-icon-arrowthick-1-s treeeditbtn", btn);
		btn = new Button("createTreeNode", ":tree_datagrid." + getComponent() + ".addtreenode");
		treeButtons.put("ui-icon-plusthick", btn);
		btn = new Button("updateTreeNode", ":tree_datagrid." + getComponent() + ".modifytreenode");
		treeButtons.put("ui-icon-pencil treeeditbtn", btn);
		btn = new Button("deletetreeitem", ":tree_datagrid." + getComponent() + ".deletetreenode");
		treeButtons.put("ui-icon-trash treeeditbtn", btn);
		
	}
	
	/**
	 * 左树上部按钮
	 */
	public Map<String, Button> treeButtons = new LinkedHashMap<String, Button>();
	
	/**
	 * 树节点添加按钮是否显示
	 */
	public boolean showTreeNodeCreateBtn = true;
	
	/**
	 * 树节点修改按钮是否显示
	 */
	public boolean showTreeNodeUpdateBtn = true;
	
	/**
	 * 是否仅仅通过点击即可打开树节点，而不需要双击或者点左侧三角或十字
	 */
	public boolean expandTreeNodeByClick = false;
	
	/**
	 * 树节点删除按钮是否显示
	 */
	public boolean showTreeNodeDeleteBtn = true;
	
	/**
	 * 是否显示树节点移动按钮
	 */
	public boolean showTreeNodeMoveBtns = false;
	
	/**
	 * 是否允许根据左侧选中的树的节点重新获取datagrid信息并进行初始化
	 */
	public boolean reInitDatagridByTreeNodeSid = false;
	
	/**
	 * 是否在重新初始化Datagrid之后加载datagrid上方的查询部分界面
	 */
	public boolean reLoadQueryFormWhenDatagridReInited = false;

	/**
	 * 树结构节点上下文菜单，未设置则右键时会显示浏览器上下文菜单。
	 */
	public Map<String, MenuItem> treeContextMenu = new LinkedHashMap<String, MenuItem>();
	
	/**
	 * 树的根节点是否允许编辑
	 */
	public boolean treeRootEditable = true;
	
	/**
	 * 当准备新建一个右侧列表数据项时
	 * 是否提示需要选择一个树节点，默认给与提示
	 */
	public boolean alertNeedSelectTreeNode = true;

	/**
	 * 当加载树节点时用于定位的树节点路径数组
	 */
	public String[] locatePath = null;
	
	/**
	 * 当点击树节点时，对应节点是否允许编辑的js方法名称
	 * 可在对应的XXXtreeform.jsp中书写相应js，方法中参数与树控件对应click回调参数一致，
	 * 为click(event, treeId, treeNode) 详细请查阅相关文档。
	 * 方法返回值如果为true或不返回则表明允许编辑，返回false则表明禁止编辑
	 */
	public String treeNodeEditableFunction = null;
	
	/**
	 * 根据当前选中的树节点核查该节点是否允许添加右侧列表的js方法名称，若改js方法返回true或者将此参数设置为null则允许添加，
	 * 否则不允许添加，提示请在js方法中给出alert
	 * 默认此参数为null
	 */
	public String checkCanAddGridDataByTreeNodeFunction = null;
	
	/**
	 * 设置所有数节点编辑按钮是否显示
	 * @param visible
	 */
	public void setAllTreeButtonsVisible(boolean visible) {
		showTreeNodeCreateBtn = visible;
		showTreeNodeUpdateBtn = visible;
		showTreeNodeDeleteBtn = visible;
	}
}
