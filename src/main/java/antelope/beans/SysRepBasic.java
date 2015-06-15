package antelope.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sys_rep_tmpl")
public class SysRepBasic {
	@Id
	public String sid;
	public String title;
	public String groupsid;
	
	public Integer isrelease;
}
