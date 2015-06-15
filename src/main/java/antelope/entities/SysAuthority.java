package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_AUTHORITY")
public class SysAuthority {
	@Id
	public String roleorusersid;
	public String functionid;
	
	public SysAuthority() {
	}
	
	public SysAuthority(String roleorusersid, String func_statename) {
		this.roleorusersid = roleorusersid;
		this.functionid = func_statename;
	}
}