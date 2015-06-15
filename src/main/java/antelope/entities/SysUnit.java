package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_UNIT")
public class SysUnit {
	@Id
	public String sid;
	public String parentsid;
	public String name;
	public String code;
	public String leaderusersid;
	/**
	 * 系统特殊单位标识：
	 * 0、null以及''表示普通单位
	 * 1表示自注册用户单位，不允许做单位增删改操作
	 */
	public String flag;
	public String unitsafb;
	/**
	 * enum (
	 */
	public Integer unittype;
	public SysUnit() {
	}
	public SysUnit(String sid, String parentsid, String name, String code, String leaderusersid) {
		this.sid = sid;
		this.parentsid = parentsid;
		this.name = name;
		this.code = code;
		this.leaderusersid = leaderusersid;
		this.unitsafb = unitsafb;
	}
	
}