package antelope.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sys_rep_tmpl")
public class SysRepBasicWithHtmlTable {
	@Id
	public String sid;
	public String title;
	public String htmltable;
	public Integer isrelease;
}
