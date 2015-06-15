package antelope.demos.entites;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 左树右列表全界面组件
 * @author lining
 * @since 2012-7-25
 */
@Entity
@Table(name="DEMO_LEFT_TREE")
public class LeftTreeItem {
	@Id
	public String sid;
	public String name;
	public String parentsid;
	
	/**
	 * 排序字段，使用sid加0000做排序值
	 */
	public String sortfield;
}
