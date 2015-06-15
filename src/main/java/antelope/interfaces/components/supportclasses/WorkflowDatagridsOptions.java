package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.Assert;

import com.opensymphony.xwork2.util.TextUtils;

import antelope.interfaces.components.BaseUIController;
import antelope.interfaces.components.BaseUIOptions;

/**
 * 左树右列表增删改功能界面选项
 * @author lining
 * @since 2012-7-14
 */
public class WorkflowDatagridsOptions extends BaseUIOptions implements Queryable{
	
	/**
	 * 我创建的列表项
	 * 注意不允许设置为null值，并且若需要重新初始化请使用LinkedHashMap以保证顺序
	 */
	public LinkedHashMap<String, GridColumn> iCreatedColumns = new LinkedHashMap<String, GridColumn>();
	
	/**
	 * 我的待办的列表项
	 * 注意不允许设置为null值，并且若需要重新初始化请使用LinkedHashMap以保证顺序
	 */
	public LinkedHashMap<String, GridColumn> myTodoColumns = new LinkedHashMap<String, GridColumn>();
	
	/**
	 * 我已办的列表项
	 * 注意不允许设置为null值，并且若需要重新初始化请使用LinkedHashMap以保证顺序
	 */
	public LinkedHashMap<String, GridColumn> myDoneColumns = new LinkedHashMap<String, GridColumn>();
	
	/**
	 * 所有的列表项
	 * 注意不允许设置为null值，并且若需要重新初始化请使用LinkedHashMap以保证顺序
	 */
	public LinkedHashMap<String, GridColumn> allColumns = new LinkedHashMap<String, GridColumn>();
	
	/**
	 * 我的待阅列表项
	 * 注意不允许设置为null值，并且若需要重新初始化请使用LinkedHashMap以保证顺序
	 */
	public LinkedHashMap<String, GridColumn> toViewColumns = new LinkedHashMap<String, GridColumn>();
	
	/**
	 * 我的已阅列表项
	 * 注意不允许设置为null值，并且若需要重新初始化请使用LinkedHashMap以保证顺序
	 */
	public LinkedHashMap<String, GridColumn> viewedColumns = new LinkedHashMap<String, GridColumn>();
	
	/**
	 * 必填，创建流程所对应的key值 为bpmn文件中process节点的属性id的值
	 */
	public String processDefinitionKey = null;
	
	/**
	 * 列表查询条件
	 */
	public String[] queryfields = new String[0];
	
	/**
	 * 显示待阅列表（我的待阅，我的已阅）
	 */
	public boolean showToView = false;
	
	/**
	 * 显示我创建的列表
	 */
	public boolean showICreatedList = true;
	
	/**
	 * 是否在我创建的页签下当为暂存时，显示单行的删除按钮，默认不显示
	 */
	public boolean showDeleteBtn = false;
	
	/**
	 * 显示所有的列表
	 */
	public boolean showAllList = true;
	
	/**
	 * 显示创建按钮
	 */
	public boolean showCreateBtn = true;
	
	/**
	 * 是否显示暂存按钮
	 */
	public boolean showTempSaveBtn = true;
	
	/**
	 * 是否开启自动暂存功能，只针对于暂存过的我发起的，还有待办打开对话框时的情况，时间为5分钟一次
	 * 注意若想启用此功能，必须同时启用显示暂存按钮功能
	 */
	public boolean autoTempSaveEnabled = false;
	
	/**
	 * 是否显示导出按钮,默认不显示
	 */
	public boolean showExportBtn = false;
	
	/**
	 * 审批界面流程图显示模式，默认为三页签模式
	 * @see AuditFormDisplayMode
	 */
	public String auditFormDisplayMode = AuditFormDisplayMode.NORMAL_THREE_TAB;
	
	/**
	 * 单独控制列表中是否显示单选或复选框，若希望显示复选框则更改为multipleRows,单选框则为singleRow
	 * 默认为不显示
	 * 注意此选项优先级要低于 batchCompleteTasksEnabled选项，即若batchCompleteTasksEnabled为true，无论此选项为什么值，都会显示复选框
	 */
	public String selectionMode = null;
	
	/**
	 * 是否启用批量完成任务功能，默认为不启用
	 */
	public boolean batchCompleteTasksEnabled = false;
	
	/**
	 * 用于自定义查询区域表单jsp文件所在路径
	 * 此选项优先级高于queryfields，若不为空且不为空字符串，则将使用此属性所对应jsp文件来填充查询区域表单。
	 */
	public String queryformKey = null;
	
	/**
	 * 模拟点击查看明细按钮所使用的数据域，例如数据表中可能存在的title字段。 
	 * 注意：使用此字段之后，将覆盖对应数据域列对象中的labelFunction方法
	 * 由于待办页签与待阅页签无查看按钮，故此选项对这两个页签下的表格无效
	 */
	public String viewButtonField = null;
	
	@Override
	public void initOptions() {
		if (TextUtils.stringSet(viewButtonField)) {
			LinkedHashMap<String, GridColumn>[] gridcolumns = new LinkedHashMap[]{iCreatedColumns, myDoneColumns, allColumns, viewedColumns};
			for (int i = 0; i < gridcolumns.length; ++i) {
				LinkedHashMap<String, GridColumn> columns = gridcolumns[i];
				GridColumn column = columns.get(viewButtonField);
				if (column != null) {
					column.labelFunction = ("changeToViewLinkFunction" + i);
				}
			}
		}
	}
	
	/**
	 * 业务表单jsp所在路径
	 * 此选项优先级高于流程图中初始节点所在路径from key参数，当不为空时将使用它来嵌入业务表单。
	 */
	public String businessFormKey = null;
	
	/**
	 * @param urlprefix 所有流程相关请求的前缀信息 前缀包括jsp路径中去掉.jsp部分以及对应的类名
	 * 如: /demos/workflow_datagrids_demo/WorkflowDatagridsDemo
	 */
	public WorkflowDatagridsOptions(String processDefinitionKey, BaseUIController controller) {
		super(controller);
		Assert.notNull(processDefinitionKey, "processDefinitionKey流程定义对应key（流程定义文件中的id)不能为空");
		this.processDefinitionKey = processDefinitionKey;
	}
	
	/**
	 * 将所有列表项设置同一个列表项值
	 */
	public WorkflowDatagridsOptions setAllColumnFields(LinkedHashMap<String, GridColumn> columns) {
		this.allColumns = getCloneColumnMap(columns);
		this.iCreatedColumns = getCloneColumnMap(columns);
		this.myDoneColumns = getCloneColumnMap(columns);
		this.myTodoColumns = getCloneColumnMap(columns);
		this.toViewColumns = getCloneColumnMap(columns);
		this.viewedColumns = getCloneColumnMap(columns);
		return this;
	}
	
	private LinkedHashMap<String, GridColumn> getCloneColumnMap(LinkedHashMap<String, GridColumn> columns) {
		Set<Entry<String, GridColumn>> entries = columns.entrySet();
		LinkedHashMap<String, GridColumn> newColumns = new LinkedHashMap<String, GridColumn>();
		
		for (Entry<String, GridColumn> entry : entries) {
			try {
				newColumns.put(entry.getKey(), entry.getValue().clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return newColumns;
	}

	@Override
	public String getQueryformKey() {
		return queryformKey;
	}

	@Override
	public Map<String, GridColumn> getColumns() {
		return iCreatedColumns;
	}

	@Override
	public String[] getQueryfields() {
		return queryfields;
	}
}
