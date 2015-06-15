package antelope.entities;

import java.io.Serializable;

public class SysUserUnitDBPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String usersid;
	private String unitsid;
	public String getUsersid() {
		return usersid;
	}
	public void setUsersid(String usersid) {
		this.usersid = usersid;
	}
	public String getUnitsid() {
		return unitsid;
	}
	public void setUnitsid(String unitsid) {
		this.unitsid = unitsid;
	}
	
	@Override
	public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  int usid = this.usersid.hashCode();
	  int uusid = this.unitsid.hashCode();
	  result = prime* result + usid;
	  result = prime* result + uusid;
	  return result;
	}

	@Override
	public boolean equals(Object obj){
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() !=obj.getClass())
	        return false;
	    SysUserUnitDBPK other = (SysUserUnitDBPK) obj;
	    if (usersid != other.usersid)
	        return false;
	    if (unitsid != other.unitsid)
	        return false;
	    return true;
	  }
}
