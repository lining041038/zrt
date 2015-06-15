package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sys_rep_dict_item")
public class SysRepDictItem {
	@Id
	public String sid;
	public String title;
	public String dict_sid;
}