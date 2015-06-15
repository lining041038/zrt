package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.util.TextUtils;

import antelope.interfaces.components.BaseUIController;
import antelope.interfaces.components.BaseUIOptions;

/**
 * 单列表增删改功能界面选项
 * @author lining
 * @since 2012-7-14
 */
public class SingleDatagridOptions extends BaseUIOptions implements Queryable{
	
	public SingleDatagridOptions(BaseUIController controller) {
		super(controller);
		
		// 默认创建三个按钮
		Button btn = new Button("singlegrid_moveup__");
		buttons.put("i_up", btn);
		btn = new Button("singlegrid_movedown__");
		buttons.put("i_down", btn);
		btn = new Button("singlegrid_view__");
		buttons.put("query", btn);
		btn = new Button("singlegrid_update__");
		buttons.put("update", btn);
		btn = new Button("singlegrid_del__");
		buttons.put("del", btn);
		
		// 默认创建四个按钮
		btn = new Button("exportit");
		funcBtns.put(":antelope.exp", btn);
		btn = new Button("importit");
		funcBtns.put(":antelope.imp", btn);
		
		btn = new Button("singlegrid_batchmoveup__");
		funcBtns.put(":antelope.batchmoveup", btn);
		btn = new Button("singlegrid_batchmovedown__");
		funcBtns.put(":antelope.batchmovedown", btn);
		
		btn = new Button("create");
		funcBtns.put(":single_datagrid.${component}.add", btn);
		btn = new Button("batchDeleteLines");
		funcBtns.put(":antelope.batchdelete", btn);
	}
	
	/**
	 * 是否显示创建按钮 ，默认显示
	 */
	public boolean showCreateBtn = true;
	
	/**
	 * 点击创建按钮时，所触发的js方法名称，默认为create,组件中自带，允许设置其他类型
	 * 注意：此属性仅适用于singledategrid组件，并且它的优先级高于直接修改funcBtns中对应默认创建按钮的click方法名称。
	 */
	public String createBtnClickFunction = "create";

	/**
	 * 是否显示单行的删除按钮，默认显示
	 */
	public boolean showDeleteBtn = true;
	
	/**
	 * 是否显示单行更新（编辑）按钮，默认显示
	 */
	public boolean showUpdateBtn = true;
	
	/**
	 * 是否显示导入按钮,默认不显示
	 */
	public boolean showImportBtn = false;
	
	/**
	 * 是否显示导出按钮,默认不显示
	 */
	public boolean showExportBtn = false;
	
	/**
	 * 是否显示批量删除按钮，若显示批量删除按钮，则列表左侧将显示复选框
	 * 注意此删除不会根据右侧单行删除按钮的显示与否来决定某行的复选框是否显示，也就是说复选框永远都会全部显示。
	 * 请覆盖batchDeleteLines方法
	 */
	public boolean showBatchDeleteBtn = false;
	
	/**
	 * 在新增和修改对话框右下角功能按钮区域中是否显示暂存按钮，默认为不显示
	 * 此暂存按钮在点击时，界面上必填项将不进行校验，其他表单项正常校验
	 * 此暂存按钮点击时，将不提示“是否确定”
	 */
	public boolean showTempSaveBtn = false;
	
	/**
	 * 是否在列表中显示上移下移按钮，默认不显示
	 */
	public boolean showMoveBtns = false;
	
	/**
	 * 界面是否现在批量移动按钮，默认不显示
	 */
	public boolean showBatchMoveBtns = false;
	
	/**
	 * 创建更新窗口模式，默认为dialog
	 * 详见CreateUpdateWindowMode静态变量类
	 * 当前适用组件: single_datagrid全界面组件
	 */
	public String createUpdateWindowMode = CreateUpdateWindowMode.DIALOG;
	
	/**
	 * 顶级窗口模式下额外参数,如可设置窗口的宽度及高度
	 */
	public WindowModeDialogTopIframeParams windowModeParams = new WindowModeDialogTopIframeParams();
	
	/**
	 * 是否在创建完毕之后根据当前创建的数据项sid定位列表当前页
	 */
	public boolean locatePageBySidAfterCreate = false;
	
	/**
	 * 表单域
	 */
	public FormField[] formfields = new FormField[0];
	
	/**
	 * 表单键，存储表单所在jsp路径，此选项将覆盖FormFields选项，若不为空，则将使用此路径下jsp作为表单
	 */
	public String formKey = null;
	
	public Map<String, GridColumn> columns = new LinkedHashMap<String, GridColumn>();
	
	/**
	 * 可用于查询的字段，注意必须为列表中显示出来的字段
	 * 若为日期或日期时间类型则允许如下方式书写fieldname:date 或  fieldname:datetime
	 * 当找到对应表中的列数据对应存在枚举时，则默认查询字段将会以下拉列表形式展示，如若希望使用复选框checkbox显示则需要如下书写方式
	 * fieldname:checkbox
	 */
	public String[] queryfields = new String[0];
	
	/**
	 * 用于自定义查询区域表单jsp文件所在路径
	 * 此选项优先级高于queryfields，若不为空且不为空字符串，则将使用此属性所对应jsp文件来填充查询区域表单。
	 */
	public String queryformKey = null;
	
	/**
	 * 模拟点击查看明细按钮所使用的数据域，例如数据表中可能存在的title字段。 
	 * 注意：使用此字段之后，将覆盖对应数据域列对象中的labelFunction方法
	 */
	public String viewButtonField = null;
	
	public Map<String, Button> buttons = new LinkedHashMap<String, Button>();
	
	/**
	 * 功能按钮区域（如添加按钮）按钮 当前只SingleDatagrid、TreeDatagrid支持，其他子类暂不适用
	 */
	public Map<String, Button> funcBtns = new LinkedHashMap<String, Button>();
	
	/**
	 * 单独控制列表中是否显示单选或复选框，若希望显示复选框则更改为multipleRows,单选框则为singleRow
	 * 默认为不显示
	 * 注意此选项优先级要低于 showBatchDeleteBtn选项，即若showBatchDeleteBtn为true，无论此选项为什么值，都会显示复选框
	 */
	public String selectionMode = null;
	
	@Override
	public void initOptions() {
		if (TextUtils.stringSet(viewButtonField)) {
			GridColumn column = this.columns.get(viewButtonField);
			if (column != null) {
				column.labelFunction = "changeToViewLinkFunction";
			}
		}
	}

	@Override
	public String getQueryformKey() {
		return queryformKey;
	}

	@Override
	public String[] getQueryfields() {
		return queryfields;
	}

	@Override
	public Map<String, GridColumn> getColumns() {
		return columns;
	} 
}




