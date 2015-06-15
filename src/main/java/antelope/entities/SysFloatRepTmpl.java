package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统浮动行报表模板
 * @author lining
 */
@Entity
@Table(name="SYS_FLOATREP_TMPL")
public class SysFloatRepTmpl {
	@Id
	public String sid;
	public String groupsid;
	public String title;
	public String dbtablename;
	/**
	 * 是否已经发布
	 */
	public Integer isrelease;
}
