package antelope.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 系统选项实体类
 * @author lining
 * @since 2012-6-8
 */
@Entity
@Table(name="SYS_OPTS")
public class SysOptItem {
	
	@Id
	public String sid;
	/**
	 * 系统选项名称
	 */
	public String name;
	
	/**
	 * 获取键
	 */
	@Column(name="key_")
	public String key;
	
	/**
	 * 值
	 */
	public String value;
	
	/**
	 * 若系统选项为枚举时，对应枚举的xml名称，不包含.xml扩展名，若为null或''则均视为不关联枚举
	 */
	public String enumxmlname;
	
	public String group_name;
	
	public String group_key;
	
	/**
	 * 为特殊选项准备formkey
	 */
	public String formkey;
}
