package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_ROLE")
public class SysRole {
	@Id
	public String sid;
	public String name;
	public String flag;
	
	/**
	 * 允许按照角色决定用户登陆后使用哪个首页面
	 * 此属性存储对应角色的首页面路径，若为空，则使用system-opts中的indexpagepos参数所对应页面
	 */
	public String indexpath;
	public SysRole() {
	}
	public SysRole(String sid, String name) {
		this.sid = sid;
		this.name = name;
	}
}