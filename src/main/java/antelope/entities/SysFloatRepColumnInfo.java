package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 浮动行模板列信息
 * @author lining
 */
@Entity
@Table(name="SYS_FLOATREP_DBCOLINFO")
public class SysFloatRepColumnInfo {

	@Id
	public String sid;
	public String colname;
	public String coltitle;
	public Double colwidth;
	public String edittype;
	public String sumtype;
	public String tmplsid;
	public String dictsid;
}
