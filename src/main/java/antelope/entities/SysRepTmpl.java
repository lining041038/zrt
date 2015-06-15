package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="sys_rep_tmpl")
public class SysRepTmpl {
	@Id
	public String sid;
	public String title;
	public String htmltable;
	public String dbtablename;
	public String groupsid;
	
	/**
	 * 改报表模板是否发布， 0为未发布 1为已发布
	 */
	public Integer isrelease;
}