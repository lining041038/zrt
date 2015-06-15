package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_USER_UNIT_RELATE")
public class SysUserUnit {

	private String usersid;
	private String unitsid;
	
	public SysUserUnit(){
		
	}
	public SysUserUnit(String usersid,String unitsid){
		this.usersid = usersid;
		this.unitsid = unitsid;
	}
	
	public String getUnitsid() {
		return unitsid;
	}
	@Id
	public String getUsersid() {
		return usersid;
	}
	
	public void setUsersid(String usersid) {
		this.usersid = usersid;
	}
	public void setUnitsid(String unitsid) {
		this.unitsid = unitsid;
	}

}
