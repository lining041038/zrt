package antelope.demos.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import antelope.utils.SpeedIDUtil;

@Entity
@Table(name="DEMO_SINGLE_DATAGRID")
public class SingleDataGridItem {
	@Id
	public String sid;
	
	public String name;
	
	
	public String gender;
	
	/**
	 * 关联到LeftTreeItem sid, 标明此列表项属于书中的某节点
	 */
	public String treenode_sid;
	
	@Column(name="age_")
	public Integer age;
	
	/**
	 * clob字段测试列，用于测试系统在不同服务器，不同数据库对clob处理的一致性
	 */
	public String clobtest;
	
	/**
	 * 多页签列表时增改对应表单加载页面数据
	 */
	public String formkey;
	
	/**
	 * 添加图片加载路径字段，添加测试数据
	 */
	public String imgpath;
	
	/**
	 * 排序字段，使用sid加0000做排序值
	 */
	public String sortfield;
	
}







