package antelope.beans;

import java.sql.Timestamp;

/**
 * 全局待办事项列表数据对象
 * @author lining
 */
public class TodoListItem {
	
	/**
	 * 模块名称
	 */
	public String moduleName;
	
	/**
	 * 部门名称
	 */
	public String deptName;
	
	
	/**
	 * 业务标题
	 */
	public String title;
	
	/**
	 * 对应处理界面的url,可能跟相关参数
	 */
	public String url; 
	
	
	/**
	 * 待办创建时间
	 */
	public Timestamp createtime;
}
